package System;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.sun.istack.internal.logging.Logger;

import Game.Game;
import Game.Player;
import user.User;

public class GameCenter implements GmaeCenterInterface{
   private  ConcurrentLinkedQueue<User> users;
   private ConcurrentLinkedQueue<Game> games;

   private static GmaeCenterInterface singleton = new GameCenter( );
   private LinkedList<GameLogs> game_logs;
   
   public static GmaeCenterInterface getInstance( ) {
	      return singleton;
	   }
   
   private GameCenter(){
	   users = new ConcurrentLinkedQueue<User>();
	   games= new ConcurrentLinkedQueue<Game>();
	   game_logs = new  LinkedList<GameLogs>();
   }
   
   public void addUser(User user){
	   
	   users.add(user);
	   
   }
   public void saveFavoriteGame(int GameID){
	   
	   for(Game i_game : games){
		   if(i_game.getGameID()==GameID)
			   game_logs.add(i_game.getGameLog());			   
		   
	   }
   }
public void replaySavedTurn(int GameID){
	   
	   for(GameLogs i_game_logs : game_logs){
		   if(i_game_logs.getGameID()==GameID){
			   Logger my_log = Logger.getLogger(GameCenter.class);
			   for(String s :i_game_logs.getLog()){
				   my_log.info(s);
			   }
		   }
			   			   
		   
	   }
   }
   
   /**
    * in this function please see the Create game requirment in the Assignment 1 and add the relevant params according to the game preferences, 
    * @param user
    * @return true if the user can init game with the giver preferences, 
    */
   public boolean createGame(Player player){
	   
	   
	   
	   return false;
   }
   
   /**
    * see Search/filter activegames in assignment 1 and the relevant usecase
    * @param playerName
    * @param potSize
    * @return
    */
   public LinkedList<Game> Search(String playerName,int potSize){
	   
	  LinkedList<Game> can_join = new LinkedList<Game>();
	  if(playerName.equals("")&& potSize==-1){
		  for(Game i_game : games){
			 can_join.add(i_game);			 
		  }
	  }
	  else if(playerName.equals("")&& potSize!=-1){
		  for(Game i_game : games){
			  if(i_game.getPlayerNumber()==potSize){
				 can_join.add(i_game);	
			  }
		  }
		  
	  }else if(!playerName.equals("")&& potSize==-1){
		  for(Game i_game : games){			 
			  Player [] players = i_game.getPlayers();
			  for(Player p : players){
				 if(p.getUser().getName().equals(playerName)){
					  can_join.add(i_game);
				 }
			  }
		  }
		  
		  
	  }else{
		  for(Game i_game : games){			 
			  Player [] players = i_game.getPlayers();
			  for(Player p : players){
				 if(p.getUser().getName().equals(playerName) && i_game.getPlayerNumber()==potSize){
					  can_join.add(i_game);
				 }
			  }
		  }
	  }
		  
	  return  can_join;
   }
   
   public boolean joinGame(Game game, Player player){
	   
	   return false;
   }
   
   
   
}
