package System;

import java.util.LinkedList;

import Game.Game;
import Game.Player;
import user.User;

public interface GmaeCenterInterface {

	
	/**
	 * 
	 *this function get all the details of unregistered user, check them under the game policy, if there is'nt problem with one or more of the details make a new user and add him to the system
	 */
	   public boolean register(String ID, String password, String name, String email);
	
	   public void addUser(User user);
	   
	   /**
	    * in this function please see the Create game requirment in the Assignment 1 and add the relevant params according to the game preferences, 
	    * @param user
	    * @return true if the user can init game with the giver preferences, 
	    */
	   public boolean createGame(Player player);
	   
	   /**
	    * see Search/filter activegames in assignment 1 and the relevant usecase
	    * @param playerName
	    * @param potSize
	    * @return
	    */
	   public LinkedList<Game> Search(String playerName,int potSize);
	   
	   public boolean joinGame(Game game, Player player);
	   
	   public boolean editUserPassword(String userID, String newPassword);
		
		public boolean editUserName(String userID, String newName);
		
		public boolean editUserEmail(String userID, String newEmail);
}
