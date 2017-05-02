package Game;

import java.util.LinkedList;
import java.util.Queue;

import user.UserInterface;
import System.GameLogs;



/**
 * Please use this for logging http://stackoverflow.com/questions/15758685/how-to-write-logs-in-text-file-when-using-java-util-logging-logger , make the File name to be "GameID.log" according the GameID field
 * @author ahmad
 *
 */
public class Game implements GameInterface,Runnable{

	private LinkedList<Player> players;
	private Queue<Player> WantToJoinPlayers;
	private Deck deck;	
	private int num_of_want_to_join;
	private GamePreferences preferences;
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
	
	public Game(Player Creator, GamePreferences preferences, int GameID){
		players = new LinkedList<Player>();
		WantToJoinPlayers = new LinkedList<Player>();
		this.GameID = GameID;
		deck = new Deck();
		deck.shuffle();
		num_of_want_to_join = 0;
		this.preferences = preferences;
		players.add(Creator);
		playersNumber = 1;
		log_game = new  GameLogs(GameID);
		user_waches = new LinkedList<UserInterface>();
		table = new Card[5];
		cardsOnTable = 0;
		blindBit = 0;
	}
	
	public int getPlayerNumber(){
		return players.size();
	}
	
	public int getMinBid(){
		return preferences.getMinBet();
	}
	
	public int getGameID(){
		return GameID;
	}
	
	public GameLogs getGameLog(){
		return log_game;
	}
	
	public Player[] getPlayers(){
		return players.toArray(new Player[0]);
	}
	
	public Player[] getWantToJoinPlayers(){
		return WantToJoinPlayers.toArray(new Player[0]);
	}
	
	public void AddUserToWatch(UserInterface p){
		user_waches.add(p);
	}
	
	/**
	 * This method Used by GameCenter to add new player to the game
	 * @param player this player will be holded by the relevant user 
	 * @return true if this player can join the Game, else (there's more than MaxPlayers players, or his cash not enough) return false
	 */
	public boolean joinGame(Player player){
		if(player.getUser().geTotalCash() < preferences.getMinBet())
			return false;
		
		if(players.size() < preferences.getMaxPlayersNum() || WantToJoinPlayers.size() < preferences.getMaxPlayersNum()) {
			synchronized (this) {
				if(players.size() < preferences.getMaxPlayersNum()) {
					players.add(player);
					player.getUser().joinGame(this, player);
					playersNumber++;
				}
				else if(WantToJoinPlayers.size() < preferences.getMaxPlayersNum()) {
					WantToJoinPlayers.add(player);
					num_of_want_to_join++;
				}
				else return false;
			}
		}
		else //The game is full and there are enough players waiting to join.
			return false;
		return true;
	}
	
	public boolean fold(Player player) {
		folded.add(player);
		return true;
	}
	
	public boolean check(Player player) {
		
		return true;
	}
	
	public boolean bet(Player player, int money) {
		if(player.getCash() >= money && money>CurrentBet)
		 {cashOnTheTable += money;
		return true;}
		return false;
	}
	
	public boolean leaveGame(Player player) {

		synchronized (this) {
			boolean found = players.removeFirstOccurrence(player);
			if(found) {
				playersNumber--;
				if(WantToJoinPlayers.size() > 0) {
					Player p = WantToJoinPlayers.remove();
					players.add(p);
					p.getUser().joinGame(this, p);
					playersNumber++;
					num_of_want_to_join--;
				}
			}
			return found;
		}
	}

	private boolean dealCardsForPlayers() {
		for(int i=0;i < players.size(); i++ ){
			Card card1 = deck.getCard();
			Card card2 = deck.getCard();
			if(card1 == null || card2 == null) return false;
			players.get(i).giveCards(card1, card2);
			
		}
		if (players.size() <2) return false; 
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
	
	public void run() {
		int minBid = preferences.getMinBet();
		
		while(playersNumber > 0){

			while (playersNumber > 1){
				
				dealCardsForPlayers();
				players.get(blindBit).takeMoney(minBid/2);
				players.get(((blindBit + 1)%playersNumber)).takeMoney(minBid);
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
			CurrentBet = preferences.getMinBet();
			while(played < playersNumber - folded.size()){
				Player current = players.get((blindBit+currentPlayer)%playersNumber);
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
}
