package Game;

import user.UserInterface;

public class Player implements PlayerInterface{
   
	
	 private Card[] hand;
	 private int cash;
	 private UserInterface User;
	 private GameInterface Game;
	 public Player (int cash,UserInterface User,GameInterface Game){
		 
		 this.cash = cash;
		 this.User = User;
		 this.Game = Game;
		 
		 
	 }
	 
	 /**
	  * This method will be used by the Game to give the player cards
	  * */ 
	 public void giveCards(Card card1, Card card2){
		 hand[0] = card1;
		 hand[1] = card2;
	 }
	 
	 /**
	  * This method will be used by the Game to tell the player that the game updated, The player object will notify the user for this to update him
	  */
	 public void GameUpdated(){
		 
		 
	 }
	 
	 
	 /**
	  * This method will be used by the Game to tell the player that now is his turn, and request him to play, The player object request from the User to play
	  * @return true if the action was performed , else return false
	  */
	 public boolean takeAction(){
		 
		 
		 return false;
	 }
	 
	 /**
	  * this method will be used by the user to make fold action, the player object will notify the game for this
	  * @return true if the player can do this action in the current time, else  (this is not his turn, or he is not playing)return false
	  */
	 public boolean fold(){
		 
		 return false;
	 }
	 
	 /**
	  * this method will be used by the user to make check action, the player object will notify the game for this
	  * @return true if the player can do this action in the current time, else  (this is not his turn, or he is not playing, or there's another player batted)return false
	  */
	 public boolean check(){
		 
		 return false;
	 }
	 /**
	  * this method will be used by the user to make bet action, the player object will notify the game for this
	  * @return true if the player can do this action in the current time, else  (this is not his turn, or he is not playing, or there's another player batted more the @money, or he hasn't this amount of money)return false
	  */
	 public boolean bet(int money){
		 
		 return false;
	 }
	 /**
	  * this method will be used by the user to make leaveGame action, the player object will notify the game for this
	  * @return true if the player can do this action in the current time, else return false
	  */
	 public boolean leaveGame(){
		 
		 return false;
	 }
	 
	 /**
	  * This method will be used by the Game to give the player money that he win, The player object notify the User about this
	  * @return true if the action was performed , else return false
	  */
	 public boolean giveMoney(int money){
		 
		 return false;
	 }

	@Override
	public int getCash() {
	return cash;
	}
	 
}
