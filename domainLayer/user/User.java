package user;

import java.util.LinkedList;

import com.sun.istack.internal.logging.Logger;

import Game.Game;
import Game.Player;
import System.GameCenter;
import System.GameLogs;

public class User implements UserInterface {
	
private	String ID;
private String name;
private LinkedList<Player> inGamePlayers;
private int totalCash;
private int score;
Logger my_log;

public User(String ID, String name,int totalCash,int score){
	this.ID = ID;
	this.name = name;
	this.totalCash = totalCash;
	this.score = score;
	my_log = Logger.getLogger(User.class);
	
}
public String getName(){
	return name;
}
public int geTotalCash(){
	return totalCash;
}
public void getLog(LinkedList<String> i_game_logs){
	
	   for(String s :i_game_logs){
		   my_log.info(s);
	   }
}
public void getLog(String  i_game_logs){
	 
	  
	 my_log.info(i_game_logs);
	  
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

public boolean leaveGame(){
	 
	 return false;
}


public boolean giveMoney(int money){
	 
	 return false;
}



public boolean createGame(Player player){
	   
	   
	   
	   return false;
}


public LinkedList<Game> Search(String playerName,int potSize){
	   
	   
	   return null;
}

public boolean joinGame(Game game, Player player){
	   
	   return false;
}

}
