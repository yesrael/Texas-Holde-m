package System;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.istack.internal.logging.Logger;

import Game.Game;
import Game.Player;
import user.User;
import user.UserInterface;

public class GameCenter implements GmaeCenterInterface{
   private  ConcurrentLinkedQueue<User> users; 
   private ConcurrentLinkedQueue<Game> games;

   private static GmaeCenterInterface singleton = new GameCenter( );
   private LinkedList<GameLogs> game_logs;
   private final static Logger LOGGER = Logger.getLogger(GameCenter.class.getName());
   
   public static GmaeCenterInterface getInstance( ) {
	      return singleton;
	   }
   
   private GameCenter(){
	   users = new ConcurrentLinkedQueue<User>();
	   games= new ConcurrentLinkedQueue<Game>();
	   game_logs = new  LinkedList<GameLogs>();
   }
   
   
   public User getUser(String ID)
   {
	   User user = null;
	   for (User usr : users) 
		     if(usr.getID().equals(ID))
		    	 user=usr;
	   return user;
   }
   
   /**
    * 
    * @param email
    * @return true if email is valid email address
    */
   private boolean isValidEmailAddress(String email)
   {
	   Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
       Matcher mat = pattern.matcher(email);
       return mat.matches();
   }
   
  
  /**
   * this function get all the details of unregistered user, check them under the game policy, if there is'nt problem with one or more of the details make a new user and add him to the system
   *  @return true if the user can register to the system, else return false;
   */
   public boolean register(String ID, String password, String name, String email) 
   {
	   User newUser;

	   for (User usr : users) 
		     if(usr.getID().equals(ID))
		     {
		    	 LOGGER.info("Error: this ID already exist in the system");
		         return false;
		     }
	   if(!isValidEmailAddress(email))
		   {
		    LOGGER.info("Error: invalid email address");
		    return false;
		   }
	   if(password.length()<8)
		   {
		     LOGGER.info("Error: the password is too short");
		     return false;
		   }
	   newUser=new User(ID, password, name, email, 0, 0);
	   addUser(newUser);
	   return true;
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
public void replaySavedTurn(int GameID, UserInterface user){
	   
	   for(GameLogs i_game_logs : game_logs){
		   if(i_game_logs.getGameID()==GameID){
			   user.getLog(i_game_logs.getLog());
		   }
			   			   
		   
	   }
   }
public void addUserToSpectatingGame (int GameID, UserInterface user){
	   
	   for(Game i_game : games){
		   if(i_game.getGameID()==GameID){
			  i_game.AddUserToWatch(user);
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
   

@Override
public boolean editUserPassword(String userID, String newPassword) {
	if(newPassword.isEmpty())
		{
		  LOGGER.warning("Error: empty password is invalid");
		  return false;
		}
	if(newPassword.length()<8)
		{
		  LOGGER.info("Error: the password is too short");
		  return false;
		}
	
	for (User usr : users) {
	     if(usr.getID().equals(userID))
	     {
	    	usr.editPassword(newPassword);
	    	break;
	     }
	    }
	return true;
}

@Override
public boolean editUserName(String userID, String newName) {
	if(newName.isEmpty())
		{
		  LOGGER.info("Error: empty name is invalid");
		  return false;
		}
	
	for (User usr : users) {
	     if(usr.getID().equals(userID))
	     {
	    	usr.editName(newName);
	    	break;
	     }
	    }
	return true;
}

@Override
public boolean editUserEmail(String userID, String newEmail) {
	if(newEmail.isEmpty())
		{
		  LOGGER.info("Error: empty email is invalid");
		  return false;
		}
	if(!isValidEmailAddress(newEmail))
	{    
		LOGGER.info("Error: invalid email address");
		 return false;
	}
	
	for (User usr : users) {
	     if(usr.getID().equals(userID))
	     {
	    	usr.editEmail(newEmail);
	    	break;
	     }
	    }
	return true;
}

@Override
public boolean login(String ID, String password) {
	for (User usr : users) {
	     if(usr.getID().equals(ID))
	     {
	    	 if(usr.getPassword().equals(password))
	    	 {
	    	  usr.login();
	          return true;
	    	 }
	    	 else
	    	 {
	    		 LOGGER.info("Error: incorrect password");
	    		 return false;
	    	 }
	     }
	    }
	LOGGER.info("Error: unrecognize id");
	return false;
}

@Override
public void logout(String ID) {
	for (User usr : users) 
	   if(usr.getID().equals(ID))
         usr.logout();
}

}
