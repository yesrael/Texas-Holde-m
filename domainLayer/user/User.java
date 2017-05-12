package user;

import java.util.LinkedList;

import com.sun.istack.internal.logging.Logger;

import Game.Game;
import Game.Player;
import Game.Enum.GameType;
import System.GameCenter;
import System.GameLogs;

public class User implements UserInterface {
	
	private	String ID;
	private String name;
	private String email;
	private String password;
	private LinkedList<Player> inGamePlayers;
	private int totalCash;
	private int score;
	private UserStatus status;
	private int league;
	Logger my_log;

	public User(String ID, String password, String name, String email, int totalCash, int score,int league){
		this.ID = ID;
		this.password=password;
		this.name = name;
		this.email = email;
		this.totalCash = totalCash;
		this.score = score;
		status = UserStatus.DISCONNECTED;
		my_log = Logger.getLogger(User.class);
		this.setLeague(league);
	}
	
	public String getName(){
		return name;
	}
	  

	public int geTotalCash(){
		return totalCash;
	}

	public String toString()
	{
		return "ID: "+ID+"\n"+"name: "+name+"\n";
	}
	
	public String getID()
	{
		return this.ID;
	}
	
	public String getEmail()
	{
		return this.email;
	}
	
	public String getPassword()
	{
		return this.password;
	}
	
	public UserStatus getStatus()
	{
		return this.status;
	}
	  
	public void GameUpdated(){
		 
	}
	
	public boolean takeAction(){
		 
		 
		 return false;
	}
	
	
	public boolean fold(){
		 
		 return false;
	}
	
	
	public boolean check(){
		 
		 return false;
	}
	
	public boolean bet(int money){
		 
		 return false;
	}
	
	public boolean leaveGame(Player player){
		 
		 return false;
	}
	
	
	public boolean giveMoney(int money){
		 
		 return false;
	}
	
	
	

	
	
	public LinkedList<Game> Search(String playerName,int potSize){
		   
		   
		   return null;
	}
	
	public boolean joinGame(Player player){
		   
		   return false;
	}
	
	public void editPassword(String newPassword) {
		this.password = newPassword;
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

	public void login() {
		this.status = UserStatus.CONNECTED;
	}
	
	
	public void logout() {
		this.status = UserStatus.DISCONNECTED;
	}

	public int getLeague() {
		return league;
	}

	public void setLeague(int league) {
		this.league = league;
	}

}
