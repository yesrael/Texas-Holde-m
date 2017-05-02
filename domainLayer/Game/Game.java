package Game;

import java.util.LinkedList;

import user.UserInterface;
import System.GameLogs;



/**
 * Please use this for logging http://stackoverflow.com/questions/15758685/how-to-write-logs-in-text-file-when-using-java-util-logging-logger , make the File name to be "GameID.log" according the GameID field
 * @author ahmad
 *
 */
public class Game implements GameInterface,Runnable{

	private Player[] players;
	private Player [] WantToJoinPlayers;
	private Deck deck;	
	private int num_of_want_to_join;
	private int minBid;
	private int playersNumber;
	private int cashOnTheTable = 0;
	private int GameID;
	private boolean isActive;
	private int CurrentBet;
	private LinkedList<UserInterface> user_waches;
	GameLogs log_game;
	private Card[] table;
	private int cardsOnTable;
	private int blindBit;
	private LinkedList<Player> folded = new LinkedList<Player>();

	public Game(Player Creator,int minBid,int GameID){
		players = new Player[8];
		WantToJoinPlayers= new Player[8];
		this.GameID = GameID;
		deck = new Deck();
		deck.shuffle();
		isActive = false;
		this.minBid = minBid;
		players[0] = Creator;
		playersNumber = 1;
		CurrentBet = 0;
		num_of_want_to_join =0;
		log_game = new  GameLogs(GameID);
		user_waches = new LinkedList<UserInterface>();
		table = new Card[5];
		cardsOnTable = 0;
		blindBit = 0;
	}
	public int getPlayerNumber(){
		return playersNumber;
	}
	public int getMinBid(){
		return minBid;
	}
	public int getGameID(){
		return GameID;
	}
	public GameLogs getGameLog(){
		return log_game;
	}
	public Player [] getPlayers(){
		return players;
	}
	public Player [] getWantToJoinPlayers(){
		return WantToJoinPlayers;
	}
	public void AddUserToWatch(UserInterface p){
		user_waches.add(p);
	}
	/**
	 * This method Used by GameCenter to add new player to the game
	 * @param player this player will be holded by the relevant user 
	 * @return true if this player can join the Game, else (there's more than 8 players, or his cash not enough) return false
	 */
	public boolean joinGame(Player player){
		if(playersNumber+num_of_want_to_join<8){
			if(player.getUser().geTotalCash()>=minBid){
				WantToJoinPlayers[num_of_want_to_join]=player;
				num_of_want_to_join++;
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean fold(Player player) {
		folded.add(player);
		return true;
	}
	@Override
	public boolean check(Player player) {
		
		return true;
	}
	@Override
	public boolean bet(Player player, int money) {
		if(player.getCash() >= money && money>CurrentBet)
		 {cashOnTheTable += money;
		return true;}
		return false;
	}
	@Override
	public boolean leaveGame(Player player) {
		boolean flag = false;
		for(int i=0;i < playersNumber; i++)
		{
			if(flag) players[i] = players[i+1];
			if(players[i] == player){
				flag = true;
				playersNumber --;
				players[i] =null;
			}
			
		}
		if (!flag){
			
			for(int i=0;i < num_of_want_to_join; i++)
			{
				if(flag) WantToJoinPlayers[i] = WantToJoinPlayers[i+1];
				if(WantToJoinPlayers[i] == player){
					flag = true;
					num_of_want_to_join--;
					WantToJoinPlayers[i]=null;
				}
				
			}
			
		}
		
		return flag;
	}
	
	private boolean dealCardsForPlayers(){
		for(int i=0;i<playersNumber; i++ ){
			Card card1 = deck.getCard();
			Card card2 = deck.getCard();
			if(card1 == null || card2 == null) return false;
			players[i].giveCards(card1, card2);
			
		}
		if (playersNumber <2) return false; 
		return true;
	}
	
	private boolean dealCardsForTable(int number){
		
		if(number + cardsOnTable > 5) return false;
		
		for(int i=cardsOnTable;i<number + cardsOnTable; i++ ){
			Card card1 = deck.getCard();

			if(card1 == null ) return false;
                table[i] = card1;
			
		}
		cardsOnTable+=number;
		return true;
	}
	
	private Player[] checkWinner(){
		
		return null;
	}
	@Override
	public void run() {
		
		while(playersNumber > 0){
			
			takeWaitingPlayers();
			
			while (playersNumber > 1){
				
				takeWaitingPlayers();
				
				dealCardsForPlayers();
				players[blindBit].takeMoney(minBid/2);
				players[((blindBit + 1)%playersNumber)].takeMoney(minBid);
				blindBit = (blindBit+1)%playersNumber;
				oneTurn();
				folded.clear();
				Player[] winners= checkWinner();
				for(int i=0;i<winners.length;i++){
					
					winners[i].giveMoney(cashOnTheTable/winners.length);
				}
				deck = new Deck();
				deck.shuffle();
				cashOnTheTable=0;
				table = new Card[5];
				cardsOnTable=0;
				
				
			}
			
			
			
		}
		
	}
	private void oneTurn() {
		int RoundNumber=0;
		int currentPlayer = 1;
		int played=1;

		while(RoundNumber < 4 && playersNumber - folded.size()>1){
			CurrentBet= minBid;
			while(played < playersNumber - folded.size()){
				Player current = players[(blindBit+currentPlayer)%playersNumber];
				int prevCashOnTheTable = cashOnTheTable;
				if(!folded.contains(current)){
					if(!current.takeAction()){
						folded.add(current);
						
					}
					else if (cashOnTheTable > prevCashOnTheTable + CurrentBet) 
					{played = 1; CurrentBet=cashOnTheTable - prevCashOnTheTable;}
					else played++;
				}
				currentPlayer++;
			}
			
			played = 0;
			if(RoundNumber == 0) dealCardsForTable(3);
			else if(RoundNumber!=3) dealCardsForTable(1);
			
			
		}
	}
	private void takeWaitingPlayers() {
		for(int i=0;i<num_of_want_to_join;i++){
			players[playersNumber+i] = WantToJoinPlayers[i];
			
		}
		playersNumber +=num_of_want_to_join;
	}
	
	

	
}
