package Game;

import java.util.LinkedList;
import java.util.Queue;

import user.UserInterface;
import System.GameLogs;



/**
 * Please use this for logging http://stackoverflow.com/questions/15758685/how-to-write-logs-in-text-file-when-using-java-util-logging-logger , make the File name to be "GameID.log" according the GameID field
 * @author ahmad
 *
 */
public class Game implements GameInterface,Runnable{

	private LinkedList<Player> players;
	private Queue<Player> WantToJoinPlayers;
	private Player CurrentPlayer;
	private Deck deck;	
	private int num_of_want_to_join;
	private GamePreferences preferences;
	private int playersNumber;
	private int cashOnTheTable = 0;
	private int GameID;
	private int CurrentBet;
	private LinkedList<UserInterface> user_waches;
	GameLogs log_game;
	private Card[] table;
	private int cardsOnTable;
	private int blindBit;
	private LinkedList<Player> folded = new LinkedList<Player>();
	
	public Game(Player Creator, GamePreferences preferences, int GameID){
		players = new LinkedList<Player>();
		WantToJoinPlayers = new LinkedList<Player>();
		this.GameID = GameID;
		deck = new Deck();
		deck.shuffle();
		num_of_want_to_join = 0;
		this.preferences = preferences;
		players.add(Creator);
		playersNumber = 1;
		log_game = new  GameLogs(GameID);
		user_waches = new LinkedList<UserInterface>();
		table = new Card[5];
		cardsOnTable = 0;
		blindBit = 0;
	}
	
	public int getPlayerNumber(){
		return players.size();
	}
	
	public int getMinBid(){
		return preferences.getMinBet();
	}
	
	public GamePreferences getpreferences(){
		return preferences;
	}
	public int getGameID(){
		return GameID;
	}
	
	public GameLogs getGameLog(){
		return log_game;
	}
	
	public Player[] getPlayers(){
		return players.toArray(new Player[0]);
	}
	
	public Player[] getWantToJoinPlayers(){
		return WantToJoinPlayers.toArray(new Player[0]);
	}
	
	public void AddUserToWatch(UserInterface p){
		log_game.addLog(p.getName() + " Watching the Game");
		user_waches.add(p);
	}
	
	/**
	 * This method Used by GameCenter to add new player to the game
	 * @param player this player will be holded by the relevant user 
	 * @return true if this player can join the Game, else (there's more than MaxPlayers players, or his cash not enough) return false
	 */
	public boolean joinGame(Player player){
		if(player.getUser().geTotalCash() < preferences.getMinBet()){
			log_game.addLog(player.getUser().getName() + "failed to joing this Game because of his total cash");
			return false;
		}
		if(players.size()+WantToJoinPlayers.size() < preferences.getMaxPlayersNum()) {
			synchronized (this) {
				if(players.size() +WantToJoinPlayers.size() < preferences.getMaxPlayersNum()) {
					WantToJoinPlayers.add(player);
					num_of_want_to_join++;
					log_game.addLog(player.getUser().getName() + "Joined The Game");
				}
				else return false;
			}
		}
		else //The game is full and there are enough players waiting to join.
			return false;
		return true;
	}
	
	public boolean fold(Player player) {
		if(player == CurrentPlayer){
		WantToJoinPlayers.add(player);
		num_of_want_to_join++;
		players.remove(player);
		playersNumber--;
		log_game.addLog(player.getUser().getName() + "Fold for This round");
		
		return true;}
		log_game.addLog(player.getUser().getName() + "Failed to fold");
		return false;
	}
	
	public boolean check(Player player) {
		if(player == CurrentPlayer){
		log_game.addLog(player.getUser().getName() + "Checked for This Round");
		
		return true;}
		else {
			log_game.addLog(player.getUser().getName() + "Failed To Check");
			return false;
		}
	}
	
	public boolean bet(Player player, int money) {
		if(player.getCash() >= money && money>=CurrentBet && player == CurrentPlayer)
		 {cashOnTheTable += money;
		 log_game.addLog(player.getUser().getName() + "Betted for This Round: "+ money);
		return true;}
		log_game.addLog(player.getUser().getName() + "Failed To Bet");
		return false;
	}
	
	public boolean leaveGame(Player player) {

		synchronized (this) {
			 
			boolean found = players.contains(player);
			if(found) {
				if(players.indexOf(player) <= blindBit) 
					if(blindBit == 0) blindBit = players.size() -1;
					else blindBit --;
				log_game.addLog(player.getUser().getName() + "Left The Game");
				playersNumber--;
				players.removeFirstOccurrence(player);
			}
			else if (WantToJoinPlayers.remove(player)) 
			{num_of_want_to_join--;
			log_game.addLog(player.getUser().getName() + "Left The Game");
			return true;
				
				
			}
			return found;
		}
	}

	private boolean dealCardsForPlayers() {
		for(int i=0;i < players.size(); i++ ){
			Card card1 = deck.getCard();
			Card card2 = deck.getCard();
			if(card1 == null || card2 == null) return false;
			players.get(i).giveCards(card1, card2);
			log_game.addLog(players.get(i).getUser().getName() + " got this cards: "+ card1.getNumber() + "of Kind: " + card1.getType()+" And "+ card2.getNumber() + " of Kind: " + card2.getType());
			
		}
		if (players.size() <2) return false; 
		return true;
	}
	
	private boolean dealCardsForTable(int number){
		
		if(number + cardsOnTable > 5) return false;
		
		for(int i=cardsOnTable;i<number + cardsOnTable; i++ ){
			Card card1 = deck.getCard();

			if(card1 == null ) return false;
                table[i] = card1;
                
    			log_game.addLog("New Card to The Table"+ card1.getNumber() + "of Kind: " + card1.getType());

			
		}
		cardsOnTable += number;
		return true;
	}
	
	private Player[] checkWinner(){
		Player [] result = null;
		 
		if(players.size() == 1) {
			 result = new Player[1];
		     result[0] = players.element();}
		else if(players.size()!=0){
			
			result = RoyalFlush();
			if(result!= null && result.length !=0)
				return result;
			result = StraightFlush();
			if(result!= null && result.length !=0)
				return result;
			result = FourOfAKind();
			if(result!= null && result.length !=0)
				return result;
			result = FullHouse();
			if(result!= null && result.length !=0)
				return result;
			result = Flush();
			if(result!= null && result.length !=0)
				return result;
			result = Straight();
			if(result!= null && result.length !=0)
				return result;
			result = ThreeOFKind();
			if(result!= null && result.length !=0)
				return result;
			result = TwoPair();
			if(result!= null && result.length !=0)
				return result;
			result = OnePair();
			if(result!= null && result.length !=0)
				return result;
			result = HighCard();
			
			log_game.addLog("The Winners In This Round: "+ result.toString());
			return result;
			
		} 
		 
		 
		 
		return result;
	}
	
	private Player[] FourOfAKind(){

		LinkedList<Player> result = new LinkedList<Player>();
		for(int i=0;i<players.size();i++){
			if(players.get(i).getCards()[0].getNumber() == players.get(i).getCards()[1].getNumber()){
				int counterTable =0;
				for(int j=0;j<table.length;j++){
					if(table[j].getNumber()==players.get(i).getCards()[0].getNumber())
					      counterTable++;
					if(counterTable == 2) {
						result.add(players.get(i));
						break;}
				}
				
				
				
			}
			else{
				int counterTable =0;
				for(int j=0;j<table.length;j++){
					if(table[j].getNumber()==players.get(i).getCards()[0].getNumber())
					      counterTable++;
					if(counterTable == 3) {
						result.add(players.get(i));
						break;}
				}
				if(counterTable!=3){
					counterTable = 0;
					for(int j=0;j<table.length;j++){
						if(table[j].getNumber()==players.get(i).getCards()[1].getNumber())
						      counterTable++;
						if(counterTable == 3) {
							result.add(players.get(i));
							break;}
					}
				}
				
				
			}
			
			
		}
		
		return (Player[]) result.toArray();
	}
	
	
	
	private Player[] ThreeOFKind(){

		LinkedList<Player> result = new LinkedList<Player>();
		for(int i=0;i<players.size();i++){
			if(players.get(i).getCards()[0].getNumber() == players.get(i).getCards()[1].getNumber()){
				int counterTable =0;
				for(int j=0;j<table.length;j++){
					if(table[j].getNumber()==players.get(i).getCards()[0].getNumber())
					      counterTable++;
					if(counterTable == 1) {
						result.add(players.get(i));
						break;}
				}
				
				
				
			}
			else{
				int counterTable =0;
				for(int j=0;j<table.length;j++){
					if(table[j].getNumber()==players.get(i).getCards()[0].getNumber())
					      counterTable++;
					if(counterTable == 2) {
						result.add(players.get(i));
						break;}
				}
				if(counterTable!=2){
					counterTable = 0;
					for(int j=0;j<table.length;j++){
						if(table[j].getNumber()==players.get(i).getCards()[1].getNumber())
						      counterTable++;
						if(counterTable == 2) {
							result.add(players.get(i));
							break;}
					}
				}
				
				
			}
			
			
		}
		
		return (Player[]) result.toArray();
	}
	
	
	private Player[] Flush(){

		LinkedList<Player> result = new LinkedList<Player>();
		for(int i=0;i<players.size();i++){
			if(players.get(i).getCards()[0].getType() == players.get(i).getCards()[1].getType()){
				int counterTable =0;
				for(int j=0;j<table.length;j++){
					if(table[j].getType()==players.get(i).getCards()[0].getType())
					      counterTable++;
					if(counterTable == 3) {
						result.add(players.get(i));
						break;}
				}
				
				
				
			}
			else{
				int counterTable =0;
				for(int j=0;j<table.length;j++){
					if(table[j].getType()==players.get(i).getCards()[0].getType())
					      counterTable++;
					if(counterTable == 4) {
						result.add(players.get(i));
						break;}
				}
				if(counterTable!=4){
					counterTable = 0;
					for(int j=0;j<table.length;j++){
						if(table[j].getType()==players.get(i).getCards()[1].getType())
						      counterTable++;
						if(counterTable == 4) {
							result.add(players.get(i));
							break;}
					}
				}
				
				
			}
			
			
		}
		
		return (Player[]) result.toArray();
	}
	
	private Player[] TwoPair(){

		LinkedList<Player> result = new LinkedList<Player>();
		for(int i=0;i<players.size();i++){
			if(players.get(i).getCards()[0].getNumber() != players.get(i).getCards()[1].getNumber()){
				boolean isOnePair = false;
				for(int j=0;j<table.length;j++){
					if(table[j].getNumber()==players.get(i).getCards()[0].getNumber())
					{
						isOnePair = true;
						break;}
				}
				if(isOnePair){
	
					for(int j=0;j<table.length;j++){
						if(table[j].getNumber()==players.get(i).getCards()[1].getNumber())
							{result.add(players.get(i));
						break;}
						
					}
				}
				
				
			}
			
			
		}
		
		return (Player[]) result.toArray();
	}
	
	private Player[] FullHouse(){

		LinkedList<Player> result = new LinkedList<Player>();
		for(int i=0;i<players.size();i++){
			if(players.get(i).getCards()[0].getNumber() != players.get(i).getCards()[1].getNumber()){
				boolean ThreeOfKind = false;
				boolean isFirstCard = false;
				int counterTable =0;
				for(int j=0;j<table.length;j++){
					if(table[j].getNumber()==players.get(i).getCards()[0].getNumber())
					      counterTable++;
					if(counterTable == 2) {
						ThreeOfKind = true;
						isFirstCard = true;
						break;}
				}
				if(counterTable!=2){
					counterTable = 0;
					for(int j=0;j<table.length;j++){
						if(table[j].getNumber()==players.get(i).getCards()[1].getNumber())
						      counterTable++;
						if(counterTable == 2) {
							ThreeOfKind = true;
							break;}
					}
				}
				if(ThreeOfKind){
	              if(isFirstCard){
					for(int j=0;j<table.length;j++){
						if(table[j].getNumber()==players.get(i).getCards()[1].getNumber())
							{result.add(players.get(i));
						break;}
						
					}}
				else{
					for(int j=0;j<table.length;j++){
						if(table[j].getNumber()==players.get(i).getCards()[0].getNumber())
							{result.add(players.get(i));
						break;}
						
					}
				}
			
					
				}
				
				
			}
			
			
		}
		return (Player[]) result.toArray();
	}
	
	private Player[] OnePair(){

		LinkedList<Player> result = new LinkedList<Player>();
		for(int i=0;i<players.size();i++){
			if(players.get(i).getCards()[0].getNumber() == players.get(i).getCards()[1].getNumber()){
				result.add(players.get(i));
			}
			else{
				for(int j=0;j<table.length;j++){
					if(table[j].getNumber()==players.get(i).getCards()[0].getNumber())
					{
						result.add(players.get(i));
						break;}
				}
				if(!result.contains(players.get(i))){
	
					for(int j=0;j<table.length;j++){
						if(table[j].getNumber()==players.get(i).getCards()[1].getNumber())
							{result.add(players.get(i));
						break;}
						
					}
				}
				
				
			}
			
			
		}
		
		return (Player[]) result.toArray();
	}
	
	private Player[] HighCard(){

		int maxCard = 0;
		LinkedList<Player> result = new LinkedList<Player>();
		for(int i=0;i<players.size();i++){
                  
			if((maxCard<players.get(i).getCards()[0].getNumber() && players.get(i).getCards()[0].getNumber() != 1)|| players.get(i).getCards()[0].getNumber() == 1) maxCard = players.get(i).getCards()[0].getNumber();
			if ((maxCard<players.get(i).getCards()[1].getNumber() && players.get(i).getCards()[1].getNumber() != 1)|| players.get(i).getCards()[1].getNumber() == 1) maxCard = players.get(i).getCards()[1].getNumber();
			
			
			
		}
		
		for(int i=0;i<players.size();i++){
            
			if(maxCard==players.get(i).getCards()[0].getNumber() || maxCard==players.get(i).getCards()[1].getNumber()) result.add(players.get(i));
			
			
			
		}
		
		return (Player[]) result.toArray();
	}
	
	private Player[] RoyalFlush(){
		Player [] result = null;
	
          

		 
		 
		 
		return result;
	}
	private Player[] StraightFlush(){
		Player [] result = null;
	
          

		 
		 
		 
		return result;
	}
	
	private Player[] Straight(){
		Player [] result = null;
	
          

		 
		 
		 
		return result;
	}

	
	private void GameUpated(){
		user_waches.forEach(a -> {a.GameUpdated();});
		
		players.forEach(a -> {a.GameUpdated();});
		WantToJoinPlayers.forEach(a -> {a.GameUpdated();});;
		
	}
	public void run() {
		
		while(playersNumber > 0){
			
			ExchangeWaitingPlayers();
			GameUpated();
			while (playersNumber > 1){
				ExchangeWaitingPlayers();
				GameUpated();
				initTableForNewTurn();
				GameUpated();
				oneTurn();
				findWinnersAndGiveMoney();
				GameUpated();

				
				
			}
		}
	}

	private void findWinnersAndGiveMoney() {
		Player[] winners = checkWinner();
		if(winners!=null){
		for(int i = 0; i < winners.length; i++){
			
			winners[i].giveMoney(cashOnTheTable / winners.length);
		}
		log_game.addLog("Every Winner Got: "+(cashOnTheTable/winners.length));
		}
		
	}

	private void initTableForNewTurn() {
		deck = new Deck();
		deck.shuffle();
		cashOnTheTable = 0;
		table = new Card[5];
		cardsOnTable = 0;
		players.get(blindBit).takeMoney(preferences.getMinBet() / 2);
		players.get(((blindBit + 1) % playersNumber)).takeMoney(preferences.getMinBet());
		blindBit = (blindBit+1) % playersNumber;
		dealCardsForPlayers();
		log_game.addLog("Cards Dealed For the Players, And BlinBet Was Betted And The Game Starting");
	}

	private void ExchangeWaitingPlayers() {
		for(int i=0;i<WantToJoinPlayers.size();i++){
			players.add(WantToJoinPlayers.poll());
			playersNumber++;
			
		}
		log_game.addLog(WantToJoinPlayers.size()+" Players Waiting for playing was Inserted To The list of The Active Players");
	}
	
	private void oneTurn() {
		int RoundNumber = 0;
		int currentPlayer = 1;
		int played = 1;
           
		while(RoundNumber < 4 && playersNumber > 1) {
			log_game.addLog("Round number: "+ RoundNumber + " Started");
			CurrentBet = preferences.getMinBet();
			while(played < playersNumber - folded.size()) {
				Player current = players.get((blindBit + currentPlayer) % playersNumber);
				int prevCashOnTheTable = cashOnTheTable;
				CurrentPlayer = current;
					if(!current.takeAction()){
						current.leaveGame();
						
					}
					else if (cashOnTheTable > prevCashOnTheTable + CurrentBet) {
						played = 1; 
						CurrentBet=cashOnTheTable - prevCashOnTheTable;
					}
					else played++;
					
					currentPlayer++;
					GameUpated();
				}
			CurrentPlayer = null;
				played = 0;
				if(RoundNumber == 0) dealCardsForTable(3);
				else if(RoundNumber != 3) dealCardsForTable(1);
				GameUpated();
				log_game.addLog("Round number: "+ RoundNumber + " Ended");
			}
		
		GameUpated();
		log_game.addLog("One Turn Done");

		}
	}

