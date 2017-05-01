package Game;


/**
 * Please use this for logging http://stackoverflow.com/questions/15758685/how-to-write-logs-in-text-file-when-using-java-util-logging-logger , make the File name to be "GameID.log" according the GameID field
 * @author ahmad
 *
 */
public class Game implements GameInterface,Runnable{

	private Player[] players;
	private Deck deck;
	private int minBid;
	private int playersNumber;
	private int GameID;
	private boolean isActive;
	private int CurrentBet;
	public Game(Player Creator,int minBid,int GameID){
		players = new Player[8];
		this.GameID = GameID;
		deck = new Deck();
		deck.shuffle();
		isActive = false;
		this.minBid = minBid;
		players[0] = Creator;
		playersNumber = 1;
		CurrentBet = 0;
	}
	/**
	 * This method Used by GameCenter to add new player to the game
	 * @param player this player will be holded by the relevant user 
	 * @return true if this player can join the Game, else (there's more than 8 players, or his cash not enough) return false
	 */
	public boolean joinGame(Player player){
		if(playersNumber < 8)
		{   if(player.getCash() < minBid) return false;
			players[playersNumber] = player;
			playersNumber++;
			return true;
			
		}
		return false;
	}
	
	@Override
	public boolean fold(Player player) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean check(Player player) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean bet(Player player, int money) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean leaveGame(Player player) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private boolean dealCardsForPlayers(){
		for(int i=0;i<playersNumber; i++ ){
			Card card1 = deck.getCard();
			Card card2 = deck.getCard();
			if(card1 == null || card2 == null) return false;
			players[i].giveCards(card1, card2);
			
		}
		if (playersNumber <2) return false; 
		return true;
	}
	
	
	@Override
	public void run() {
		
		while(playersNumber > 0){
			
			while (playersNumber > 1){
				dealCardsForPlayers();
				
			}
			
			
			
		}
		
	}
	
	

	
}
