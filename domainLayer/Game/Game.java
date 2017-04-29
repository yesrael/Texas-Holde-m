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
	private int playerNumber;
	private int GameID;
	public Game(Player Creator,int minBid,int GameID){
		players = new Player[8];
		this.GameID = GameID;
		deck = new Deck();
		deck.shuffle();
		
		this.minBid = minBid;
		players[0] = Creator;
		playerNumber = 1;
		
	}
	/**
	 * This method Used by GameCenter to add new player to the game
	 * @param player this player will be holded by the relevant user 
	 * @return true if this player can join the Game, else (there's more than 8 players, or his cash not enough) return false
	 */
	public boolean joinGame(Player player){
		
		
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
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	

	
}
