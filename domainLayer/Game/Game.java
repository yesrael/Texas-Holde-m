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
	private int playerNumber;
	private int GameID;
	private boolean isActive;
	private int CurrentBet;
	private LinkedList<UserInterface> user_waches;
	GameLogs log_game;
	
	public Game(Player Creator, GamePreferences preferences, int GameID){
		players = new LinkedList<Player>();
		WantToJoinPlayers = new LinkedList<Player>();
		this.GameID = GameID;
		deck = new Deck();
		deck.shuffle();
		num_of_want_to_join = 0;
		this.preferences = preferences;
		players.add(Creator);
		playerNumber = 1;
		log_game = new  GameLogs(GameID);
		user_waches = new LinkedList<UserInterface>();
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
	 * @return true if this player can join the Game, else (there's more than 8 players, or his cash not enough) return false
	 */
	public boolean joinGame(Player player){
		if(player.getUser().geTotalCash() < preferences.getMinBet())
			return false;
		
		if(players.size() < preferences.getMaxPlayersNum() || WantToJoinPlayers.size() < preferences.getMaxPlayersNum()) {
			synchronized (this) {
				if(players.size() < preferences.getMaxPlayersNum())
					players.add(player);
				else if(WantToJoinPlayers.size() < preferences.getMaxPlayersNum())
					WantToJoinPlayers.add(player);
				else return false;
			}
		}
		else //The game is full and there are enough players waiting to join.
			return false;
		return true;
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
		synchronized (this) {
			return players.removeFirstOccurrence(player);
		}
	}

	private boolean dealCardsForPlayers(){
		for(int i=0;i < players.size(); i++ ){
			Card card1 = deck.getCard();
			Card card2 = deck.getCard();
			if(card1 == null || card2 == null) return false;
			players.get(i).giveCards(card1, card2);
			
		}
		if (players.size() <2) return false; 
		return true;
	}
	
	public void run() {

		
	}
}
