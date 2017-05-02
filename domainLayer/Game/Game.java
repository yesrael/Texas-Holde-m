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
	private int playerNumber;
	private int GameID;
	private LinkedList<UserInterface> user_waches;
	GameLogs log_game;
	public Game(Player Creator,int minBid,int GameID){
		players = new Player[8];
		WantToJoinPlayers= new Player[8];
		this.GameID = GameID;
		deck = new Deck();
		deck.shuffle();
		num_of_want_to_join =0;
		this.minBid = minBid;
		players[0] = Creator;
		playerNumber = 1;
		log_game = new  GameLogs(GameID);
		user_waches = new LinkedList<UserInterface>();
		
	}
	public int getPlayerNumber(){
		return playerNumber;
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
		
		if(playerNumber+num_of_want_to_join<8){
			if(player.getUser().geTotalCash()>=minBid){
				WantToJoinPlayers[num_of_want_to_join]=player;
				return true;
			}
		}
		return false;
	}
	
	public boolean fold(Player player) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean check(Player player) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean bet(Player player, int money) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean leaveGame(Player player) {
		for(Player p : players){
			if(p.equals(player)){
				p = null;
				playerNumber--;
				return true;
			}
		}
		return false;
	}
	
	
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	

	
}
