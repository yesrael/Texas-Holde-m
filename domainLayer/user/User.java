package user;

import java.util.LinkedList;

import com.sun.istack.internal.logging.Logger;

import Game.Game;
import Game.GameInterface;
import Game.Player;
import communicationLayer.ConnectionHandler;

public class User implements UserInterface {
	
	private	String ID;
	private String name;
	private String email;
	private String password;
	private int totalCash;
	private int score;
	private UserStatus status;
	private int league;
	Logger my_log;
	private boolean isWaitingForAction;
	private ConnectionHandler handler;

	public User(String ID, String password, String name, String email, int totalCash, int score, int league){
		this.ID = ID;
		this.password=password;
		this.name = name;
		this.email = email;
		this.totalCash = totalCash;
		this.score = score;
		isWaitingForAction=false;
		status = UserStatus.DISCONNECTED;
		my_log = Logger.getLogger(User.class);
		this.league = league;
	}
	
	public void getLog(LinkedList<String> i_game_logs){
		
		   for(String s :i_game_logs){
			   my_log.info(s);
		   }
	}
	
	public void getLog(String  i_game_logs){
		 
		  
		 my_log.info(i_game_logs);
		  
	}
	
	public void editName(String newName) {
		this.name = newName;
	}
	
	public void editEmail(String newEmail) {
		this.email = newEmail;
	}
	
	public void editPassword(String newPassword) {
		this.password = newPassword;
	}
	
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTotalCash() {
		return totalCash;
	}

	public void setTotalCash(int totalCash) {
		this.totalCash = totalCash;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public int getLeague() {
		return league;
	}

	public void setLeague(int league) {
		this.league = league;
	}

	/**
	 * this function sends GAMEUPDATED message to the client "GAMEUPDATE *GAME FULL DETAILS*"
	 */
	public void GameUpdated(GameInterface game) {
		if(handler !=null&&this.status == UserStatus.CONNECTED)
		this.handler.send("GAMEUPDATE "+GameToString((Game)game));
		
	}

	private String GameToString(Game game){

		String result="GameID="+game.getGameID();
		result= result+"&players=";
		Player[] players= game.getPlayers();
		for(int i=0;i<players.length;i++){
			result = result+players[i].getUser().getID()+","+ players[i].getUser().getName()+",";
		}
		result = result + "&activePlayers=";
		 players= game.getActivePlayers();
		for(int i=0;i<players.length;i++){
			result = result+players[i].getUser().getID()+","+ players[i].getUser().getName()+",";
		}
		result = result + "&blindBit="+game.getBlindBit();
		result = result + "&CurrentPlayer="+game.getCurrentPlayer().getUser().getID();
		result = result + "&table=";
		
		for(int i=0;i<game.getCardsOnTable();i++){
			
			result = result+game.getTable()[i].getNumber()+","+ game.getTable()[i].getType()+",";

		}
		result = result + "&MaxPlayers="+game.getpreferences().getMaxPlayersNum();
		result = result + "&cashOnTheTable="+game.getCashOnTheTable();
		return result;
	}
	/**
	 * this function sends TAKEACTION request to the client to make some action "TAKEACTION *GAME ID*"
	 */
	@Override
	public boolean takeAction(String GameID) {
		if(this.handler!=null&&this.status == UserStatus.CONNECTED){
		this.isWaitingForAction = true;
		
		
		this.handler.send("TAKEACTION "+GameID);
		while(this.isWaitingForAction);
		return true;
	}
	return false;
}
	@Override
	public boolean giveMoney(int money) {
		if(totalCash  + money >0){
		totalCash+=money;
		return true;}
		return false;
	}




	public void giveHandler(ConnectionHandler handler) {
	    this.handler = handler;
	}

	@Override
	public void actionMaked() {
		this.isWaitingForAction = false;
		
	}
	public boolean isWaiting(){
		
		return this.isWaitingForAction;
	}

}
