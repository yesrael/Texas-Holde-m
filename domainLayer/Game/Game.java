package Game;

import java.util.LinkedList;

import user.UserInterface;
import System.GameLogs;



/**
 * Please use this for logging http://stackoverflow.com/questions/15758685/how-to-write-logs-in-text-file-when-using-java-util-logging-logger , make the File name to be "GameID.log" according the GameID field
 * @author ahmad
 *
 */
public class Game implements GameInterface, Runnable{

	private LinkedList<Player> activePlayers;
	private LinkedList<Player> players;
	private Player CurrentPlayer;
	
	public Player getCurrentPlayer() {
		return CurrentPlayer;
	}

	public Card[] getTable() {
		return table;
	}


	public int getCurrentBet() {
		return CurrentBet;
	}


	private Deck deck;	
	private GamePreferences preferences;
	private int activePlayersNumber;
	private int cashOnTheTable = 0;
	private String GameID;
	private int CurrentBet;
	private LinkedList<Spectator> user_watches;
	GameLogs log_game;
	private Card[] table;
	private int cardsOnTable;
	private int blindBit;
    private boolean isCalled;
	
	public Game(GamePreferences preferences, String GameID){
		isCalled = false;
		CurrentPlayer = null;
		activePlayers = new LinkedList<Player>();
		players = new LinkedList<Player>();
		this.GameID = GameID;
		deck = new Deck();
		deck.shuffle();
		this.preferences = preferences;
		activePlayersNumber = 0;
		log_game = new  GameLogs(GameID);
		user_watches = new LinkedList<Spectator>();
		table = new Card[5];
		cardsOnTable = 0;
		blindBit = 0;
	}
	
	public int getPlayerNumber(){
		return players.size();
	}
	public int getBlindBit(){
		return blindBit;
	}
	
	public int getMinBid(){
		return preferences.getMinBet();
	}
	
	public GamePreferences getpreferences(){
		return preferences;
	}
	public String getGameID(){
		return GameID;
	}
	
	public GameLogs getGameLog(){
		return log_game;
	}
	
	public Player[] getPlayers(){
		Player[] array = new Player[players.size()];
		for(int i = 0; i < players.size(); i++) array[i] = players.get(i);
		return array;
		//return players.toArray(new Player[players.size()]);
	}
	public Player[] getActivePlayers(){
		Player[] array = new Player[activePlayers.size()];
		for(int i = 0; i < activePlayers.size(); i++) array[i] = activePlayers.get(i);
		return array;
		//return activePlayers.toArray(new Player[activePlayers.size()]);
	}

	
	public synchronized boolean spectate(UserInterface p){
		if(preferences.isSpectatable()){
		log_game.addLog(p.getName() + " Watching the Game");
		
		user_watches.add(new Spectator(p));
		return true;}
		log_game.addLog(p.getName() + " Failed to Spectate the Game");
		
		return false;
	}
	
	/**
	 * This method Used by GameCenter to add new player to the game
	 * @param player this player will be holded by the relevant user 
	 * @return true if this player can join the Game, else (there's more than MaxPlayers activePlayers, or his cash not enough) return false
	 */
	public synchronized boolean joinGame(UserInterface p){
		for(Player ppp: players)
		{
			if(ppp.getUser().getID().equals(p.getID())){
				log_game.addLog(p.getName() + "failed to joing this Game because he is there");
				return false;}
			
			
		}

		Player player;
		if(preferences.getChipPolicy() == 0){
			player = new Player(p.getTotalCash(), p);
			
			
		}
		else{
			if(preferences.getChipPolicy() > p.getTotalCash())
				return false;
			else 
				player= new Player (preferences.getChipPolicy(),p);
			
		}
		
		if(!preferences.checkPlayer(player)){
			log_game.addLog(player.getUser().getName() + "failed to joing this Game because of his total cash");
			return false;
		}
		
		if(players.size() < preferences.getMaxPlayersNum()) {
			synchronized (this) {
				if(players.size() < preferences.getMaxPlayersNum()) {

					players.add(player);
					player.takeMoney(preferences.getBuyInPolicy());
					log_game.addLog(player.getUser().getName() + "Joined The Game");
					return true;
				}
				else return false;
			}
		}
		else //The game is full and there are enough activePlayers waiting to join.
			return false;
		
	}
	
	
	
public boolean isJoinAbleGame(UserInterface p){
		
		for(Player ppp: players)
		{
			if(ppp.getUser().getID().equals(p.getID())){
				return false;
			}
			
			
		}

		Player player;
		if(preferences.getChipPolicy() == 0){
			player = new Player(p.getTotalCash(), p);
			
			
		}
		else{
			if(preferences.getChipPolicy() > p.getTotalCash())
				return false;
			else 
				player= new Player (preferences.getChipPolicy(),p);
			
		}
		
		if(!preferences.checkPlayer(player)){
			return false;
		}
		else
		if(players.size() < preferences.getMaxPlayersNum()) {
			
				if(players.size() < preferences.getMaxPlayersNum()) {

					return true;
				}
				else return false;
			
		}
		else //The game is full and there are enough activePlayers waiting to join.
			return false;
		
	}
	private Player getPlayerByUser(UserInterface user){
		for(Player ppp: players)
		{
			if(ppp.getUser().getID().equals(user.getID())){
	
				return ppp;}
			
			
		}

		return null;
		
	}
	
	public synchronized boolean fold(UserInterface user) {
		Player player =getPlayerByUser(user);
		
		if(player == CurrentPlayer){
		activePlayers.remove(player);
		activePlayersNumber--;
		log_game.addLog(user.getName() + "Fold for This round");
		user.actionMaked(this.GameID);
		return true;}
		log_game.addLog(user.getName() + "Failed to fold");
		return false;
	}
	
	public synchronized boolean check(UserInterface user) {
		Player player =getPlayerByUser(user);
		if(player == CurrentPlayer && !isCalled){
		log_game.addLog(user.getName() + "Checked for This Round");
		user.actionMaked(this.GameID);
		return true;}
		else {
			
			log_game.addLog(user.getName() + "Failed To Check");
			return false;
		}
	}
	
	public synchronized boolean bet(UserInterface user, int money) {
		Player player =getPlayerByUser(user);
		if(player !=null &&player.getCash() >= money && money>=CurrentBet && player == CurrentPlayer&&player.giveMoney(money))
		 {cashOnTheTable += money;
		 log_game.addLog(user.getName() + "Betted for This Round: "+ money);
		 
		 user.actionMaked(this.GameID);
		return true;}
		log_game.addLog(user.getName() + "Failed To Bet");
		return false;
	}
	
	public synchronized boolean leaveGame(UserInterface user) {
		Player player =getPlayerByUser(user);
		synchronized (this) {
			boolean found =  players.remove(player);
			
			if(activePlayers.contains(player)) {
				if(activePlayers.indexOf(player) <= blindBit) 
					if(blindBit == 0) blindBit = activePlayers.size() -1;
					else blindBit --;
				log_game.addLog(player.getUser().getName() + "Left The Game");
				activePlayersNumber--;
				activePlayers.removeFirstOccurrence(player);
			}
			return found;
		}
	}

	public synchronized boolean dealCardsForPlayers() {
		for(int i=0;i < activePlayers.size(); i++ ){
			Card card1 = deck.getCard();
			Card card2 = deck.getCard();
			if(card1 == null || card2 == null) return false;
			activePlayers.get(i).giveCards(card1, card2);
			log_game.addLog(activePlayers.get(i).getUser().getName() + " got this cards: "+ card1.getNumber() + "of Kind: " + card1.getType()+" And "+ card2.getNumber() + " of Kind: " + card2.getType());
			
		}
		if (activePlayers.size() <2) return false; 
		return true;
	}
	
	public synchronized boolean dealCardsForTable(int number){
		
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
	
	public int getCashOnTheTable() {
		return cashOnTheTable;
	}

	public void setCashOnTheTable(int cashOnTheTable) {
		this.cashOnTheTable = cashOnTheTable;
	}

	public Player[] checkWinner(){
		Player [] result = null;
		 
		if(activePlayers.size() == 1) {
			 result = new Player[1];
		     result[0] = activePlayers.element();}
		else if(activePlayers.size()!=0){
			
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
	
	public Player[] FourOfAKind(){

		LinkedList<Player> result = new LinkedList<Player>();
		for(int i=0;i<activePlayers.size();i++){
			if(activePlayers.get(i).getCards()[0].getNumber() == activePlayers.get(i).getCards()[1].getNumber()){
				int counterTable =0;
				for(int j=0;j<table.length;j++){
					if(table[j].getNumber()==activePlayers.get(i).getCards()[0].getNumber())
					      counterTable++;
					if(counterTable == 2) {
						result.add(activePlayers.get(i));
						break;}
				}
				
				
				
			}
			else{
				int counterTable =0;
				for(int j=0;j<table.length;j++){
					if(table[j].getNumber()==activePlayers.get(i).getCards()[0].getNumber())
					      counterTable++;
					if(counterTable == 3) {
						result.add(activePlayers.get(i));
						break;}
				}
				if(counterTable!=3){
					counterTable = 0;
					for(int j=0;j<table.length;j++){
						if(table[j].getNumber()==activePlayers.get(i).getCards()[1].getNumber())
						      counterTable++;
						if(counterTable == 3) {
							result.add(activePlayers.get(i));
							break;}
					}
				}
				
				
			}
			
			
		}
		
		return  result.toArray(new Player[result.size()]);
	}
	
	
	
	private Player[] ThreeOFKind(){

		LinkedList<Player> result = new LinkedList<Player>();
		for(int i=0;i<activePlayers.size();i++){
			if(activePlayers.get(i).getCards()[0].getNumber() == activePlayers.get(i).getCards()[1].getNumber()){
				int counterTable =0;
				for(int j=0;j<table.length;j++){
					if(table[j].getNumber()==activePlayers.get(i).getCards()[0].getNumber())
					      counterTable++;
					if(counterTable == 1) {
						result.add(activePlayers.get(i));
						break;}
				}
				
				
				
			}
			else{
				int counterTable =0;
				for(int j=0;j<table.length;j++){
					if(table[j].getNumber()==activePlayers.get(i).getCards()[0].getNumber())
					      counterTable++;
					if(counterTable == 2) {
						result.add(activePlayers.get(i));
						break;}
				}
				if(counterTable!=2){
					counterTable = 0;
					for(int j=0;j<table.length;j++){
						if(table[j].getNumber()==activePlayers.get(i).getCards()[1].getNumber())
						      counterTable++;
						if(counterTable == 2) {
							result.add(activePlayers.get(i));
							break;}
					}
				}
				
				
			}
			
			
		}
		
		return  result.toArray(new Player[result.size()]);
	}
	
	
	private Player[] Flush(){

		LinkedList<Player> result = new LinkedList<Player>();
		for(int i=0;i<activePlayers.size();i++){
			if(activePlayers.get(i).getCards()[0].getType() == activePlayers.get(i).getCards()[1].getType()){
				int counterTable =0;
				for(int j=0;j<table.length;j++){
					if(table[j].getType()==activePlayers.get(i).getCards()[0].getType())
					      counterTable++;
					if(counterTable == 3) {
						result.add(activePlayers.get(i));
						break;}
				}
				
				
				
			}
			else{
				int counterTable =0;
				for(int j=0;j<table.length;j++){
					if(table[j].getType()==activePlayers.get(i).getCards()[0].getType())
					      counterTable++;
					if(counterTable == 4) {
						result.add(activePlayers.get(i));
						break;}
				}
				if(counterTable!=4){
					counterTable = 0;
					for(int j=0;j<table.length;j++){
						if(table[j].getType()==activePlayers.get(i).getCards()[1].getType())
						      counterTable++;
						if(counterTable == 4) {
							result.add(activePlayers.get(i));
							break;}
					}
				}
				
				
			}
			
			
		}
		
		return  result.toArray(new Player[result.size()]);
	}
	
	private Player[] TwoPair(){

		LinkedList<Player> result = new LinkedList<Player>();
		for(int i=0;i<activePlayers.size();i++){
			if(activePlayers.get(i).getCards()[0].getNumber() != activePlayers.get(i).getCards()[1].getNumber()){
				boolean isOnePair = false;
				for(int j=0;j<table.length;j++){
					if(table[j].getNumber()==activePlayers.get(i).getCards()[0].getNumber())
					{
						isOnePair = true;
						break;}
				}
				if(isOnePair){
	
					for(int j=0;j<table.length;j++){
						if(table[j].getNumber()==activePlayers.get(i).getCards()[1].getNumber())
							{result.add(activePlayers.get(i));
						break;}
						
					}
				}
				
				
			}
			
			
		}
		
		return  result.toArray(new Player[result.size()]);
	}
	
	private Player[] FullHouse(){

		LinkedList<Player> result = new LinkedList<Player>();
		for(int i=0;i<activePlayers.size();i++){
			if(activePlayers.get(i).getCards()[0].getNumber() != activePlayers.get(i).getCards()[1].getNumber()){
				boolean ThreeOfKind = false;
				boolean isFirstCard = false;
				int counterTable =0;
				for(int j=0;j<table.length;j++){
					if(table[j].getNumber()==activePlayers.get(i).getCards()[0].getNumber())
					      counterTable++;
					if(counterTable == 2) {
						ThreeOfKind = true;
						isFirstCard = true;
						break;}
				}
				if(counterTable!=2){
					counterTable = 0;
					for(int j=0;j<table.length;j++){
						if(table[j].getNumber()==activePlayers.get(i).getCards()[1].getNumber())
						      counterTable++;
						if(counterTable == 2) {
							ThreeOfKind = true;
							break;}
					}
				}
				if(ThreeOfKind){
	              if(isFirstCard){
					for(int j=0;j<table.length;j++){
						if(table[j].getNumber()==activePlayers.get(i).getCards()[1].getNumber())
							{result.add(activePlayers.get(i));
						break;}
						
					}}
				else{
					for(int j=0;j<table.length;j++){
						if(table[j].getNumber()==activePlayers.get(i).getCards()[0].getNumber())
							{result.add(activePlayers.get(i));
						break;}
						
					}
				}
			
					
				}
				
				
			}
			
			
		}
		return  result.toArray(new Player[result.size()]);
	}
	
	private Player[] OnePair(){

		LinkedList<Player> result = new LinkedList<Player>();
		for(int i=0;i<activePlayers.size();i++){
			if(activePlayers.get(i).getCards()[0].getNumber() == activePlayers.get(i).getCards()[1].getNumber()){
				result.add(activePlayers.get(i));
			}
			else{
				for(int j=0;j<table.length;j++){
					if(table[j].getNumber()==activePlayers.get(i).getCards()[0].getNumber())
					{
						result.add(activePlayers.get(i));
						break;}
				}
				if(!result.contains(activePlayers.get(i))){
	
					for(int j=0;j<table.length;j++){
						if(table[j].getNumber()==activePlayers.get(i).getCards()[1].getNumber())
							{result.add(activePlayers.get(i));
						break;}
						
					}
				}
				
				
			}
			
			
		}
		
		return  result.toArray(new Player[result.size()]);
	}
	
	private Player[] HighCard(){

		int maxCard = 0;
		LinkedList<Player> result = new LinkedList<Player>();
		for(int i=0;i<activePlayers.size();i++){
                  
			if((maxCard<activePlayers.get(i).getCards()[0].getNumber() && activePlayers.get(i).getCards()[0].getNumber() != 1)|| activePlayers.get(i).getCards()[0].getNumber() == 1) maxCard = activePlayers.get(i).getCards()[0].getNumber();
			if ((maxCard<activePlayers.get(i).getCards()[1].getNumber() && activePlayers.get(i).getCards()[1].getNumber() != 1)|| activePlayers.get(i).getCards()[1].getNumber() == 1) maxCard = activePlayers.get(i).getCards()[1].getNumber();
			
			
			
		}
		
		for(int i=0;i<activePlayers.size();i++){
            
			if(maxCard==activePlayers.get(i).getCards()[0].getNumber() || maxCard==activePlayers.get(i).getCards()[1].getNumber()) result.add(activePlayers.get(i));
			
			
			
		}
		
		return  result.toArray(new Player[result.size()]);
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

	
	private synchronized void GameUpated(){
		user_watches.forEach(a -> {a.GameUpdated(this);});
		
		players.forEach(a -> {a.GameUpdated(this);});

	}
	public void SendMSG(String msg){
		user_watches.forEach(a -> {a.SendMSG(msg);});
		
		players.forEach(a -> {a.SendMSG(msg);});

	}
	public void run() {
		try {
			ExchangeWaitingPlayers();
			while(activePlayersNumber > 0){
				
				ExchangeWaitingPlayers();
				GameUpated();
				while (activePlayersNumber > 1){
					ExchangeWaitingPlayers();
					GameUpated();
					initTableForNewTurn();
					GameUpated();
					oneTurn();
					findWinnersAndGiveMoney();
					GameUpated();
	
					
					
				}
			}
			GameUpated();
		}
		catch(Exception e) {
			GameUpated();
			System.out.println("Game " + GameID + " has crashed!");
			e.printStackTrace();
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
		activePlayers.get(blindBit).takeMoney(preferences.getMinBet() / 2);
		activePlayers.get(((blindBit + 1) % activePlayersNumber)).takeMoney(preferences.getMinBet());
		blindBit = (blindBit+1) % activePlayersNumber;
		dealCardsForPlayers();
		log_game.addLog("Cards Dealed For the Players, And BlinBet Was Betted And The Game Starting");
	}

	public int getCardsOnTable() {
		return cardsOnTable;
	}


	public void ExchangeWaitingPlayers() {
		activePlayers = new LinkedList<Player>();
		activePlayersNumber = 0;
		for(Player ppp:players){
			if(preferences.checkPlayer(ppp))
			{activePlayers.add(ppp);
			activePlayersNumber++;
			}
			else {
				leaveGame(ppp.getUser());
				
			}
		}
		
		log_game.addLog(" Players Waiting for playing was Inserted To The list of The Active Players");
	}
	
	private void oneTurn() {
		int RoundNumber = 0;
		int currentPlayer = 1;
		int played = 1;
		isCalled= false;
		while(RoundNumber < 4 && activePlayersNumber > 1) {
			log_game.addLog("Round number: "+ RoundNumber + " Started");
			CurrentBet = 0;
			if(RoundNumber == 0) CurrentBet = preferences.getMinBet(); 
			while(played < activePlayersNumber) {
				Player current = activePlayers.get((blindBit + currentPlayer) % activePlayersNumber);
				int prevCashOnTheTable = cashOnTheTable;
				CurrentPlayer = current;
					if(!current.takeAction(this.GameID)){
						leaveGame(current.getUser());
					}
					else if (cashOnTheTable > prevCashOnTheTable + CurrentBet) {
						played = 1; 
						CurrentBet=cashOnTheTable - prevCashOnTheTable;
						isCalled = true;
					}
					else played++;
					
					currentPlayer++;
					GameUpated();
				}
			isCalled=false;
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

		@Override
		public boolean equals(Object obj) {
			Game game = (Game) obj;
			return this.GameID.equals(game.getGameID());
		}
		
		/**
		 * KILL GAME
		 */
		
		public void killGame() {
			for (Player player : players) {
				leaveGame(player.getUser());
			}
		}

	}

