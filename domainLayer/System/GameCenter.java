package System;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import Game.Game;
import Game.Player;
import user.User;

public class GameCenter implements GmaeCenterInterface{
   private  ConcurrentLinkedQueue<User> users;
   private ConcurrentLinkedQueue<Game> games;
   private static GmaeCenterInterface singleton = new GameCenter( );
   
   public static GmaeCenterInterface getInstance( ) {
	      return singleton;
	   }
   
   private GameCenter(){
	   users = new ConcurrentLinkedQueue<User>();
	   games= new ConcurrentLinkedQueue<Game>();
   }
   
   public void addUser(User user){
	   
	   users.add(user);
	   
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
	   
	   
	   return null;
   }
   
   public boolean joinGame(Game game, Player player){
	   
	   return false;
   }
   
   
   
}
