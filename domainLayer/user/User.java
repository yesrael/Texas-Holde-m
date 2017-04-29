package user;

import java.util.LinkedList;

import Game.Game;
import Game.Player;

public class User implements UserInterface {
	
private	String ID;
private String name;
private LinkedList<Player> inGamePlayers;
private int totalCash;
private int score;

public User(String ID, String name,int totalCash,int score){
	this.ID = ID;
	this.name = name;
	this.totalCash = totalCash;
	this.score = score;
	
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
