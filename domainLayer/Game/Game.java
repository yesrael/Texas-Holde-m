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
	private int GameID;
	private boolean isActive;
	private int CurrentBet;
	private LinkedList<UserInterface> user_waches;
	GameLogs log_game;

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
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean fold(Player player) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean check(Player player) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean bet(Player player, int money) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean leaveGame(Player player) {
		for(Player p : players){
			if(p.equals(player)){
				p = null;
				playersNumber--;
				return true;
			}
		}
		return false;
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
	
	
	@Override
	public void run() {
		
		while(playersNumber > 0){
			
			while (playersNumber > 1){
				dealCardsForPlayers();
				
			}
			
			
			
		}
		
	}
	
	

	
}
