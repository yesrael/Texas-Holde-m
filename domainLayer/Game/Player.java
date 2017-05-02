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
		 hand = new Card[2];
		 
	 }
	 public UserInterface getUser(){
		 return User;
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
		 User.GameUpdated();
		 
	 }
	 
	 
	 
	 /**
	  * This method will be used by the Game to tell the player that now is his turn, and request him to play, The player object request from the User to play
	  * @return true if the action was performed , else return false
	  */
	 public boolean takeAction(){
		 return User.takeAction();
		 
	 }
	 
	 /**
	  * this method will be used by the user to make fold action, the player object will notify the game for this
	  * @return true if the player can do this action in the current time, else  (this is not his turn, or he is not playing)return false
	  */
	 public boolean fold(){
		 if(Game!=null)return  Game.fold(this);
		 else return false;
		 
		 
	 }
	 
	 /**
	  * this method will be used by the user to make check action, the player object will notify the game for this
	  * @return true if the player can do this action in the current time, else  (this is not his turn, or he is not playing, or there's another player batted)return false
	  */
	 public boolean check(){
		 if(Game!=null)return  Game.check(this);
		 else return false;

	 }
	 /**
	  * this method will be used by the user to make bet action, the player object will notify the game for this
	  * @return true if the player can do this action in the current time, else  (this is not his turn, or he is not playing, or there's another player batted more the @money, or he hasn't this amount of money)return false
	  */
	 public boolean bet(int money){
		
		 if(Game!=null) return Game.bet(this,money);
		 else return false;

	 }
	 /**
	  * this method will be used by the user to make leaveGame action, the player object will notify the game for this
	  * @return true if the player can do this action in the current time, else return false
	  */
	 public boolean leaveGame(){
		 Game.leaveGame(this);
		 return false;
	 }
	 
	 /**
	  * This method will be used by the Game to give the player money that he win, The player object notify the User about this
	  * @return true if the action was performed , else return false
	  */
	 public boolean giveMoney(int money){
		 cash += money;
		 return User.giveMoney(money);
	 }

	@Override
	public int getCash() {
	return cash;
	}

	@Override
	public Card[] getCards() {
		return hand;
	}

	@Override
	public boolean takeMoney(int money) {
		if(money > cash) 
		return false;
		cash -=money;
		return true;
	}
	public GameInterface getGame() {
		return Game;
	}

}
