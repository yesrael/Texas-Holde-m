package user;

import java.util.LinkedList;

import Game.Game;
import Game.Player;
import Game.Enum.GameType;

public interface UserInterface {

	public void GameUpdated();

    public void editPassword(String newPassword);
	
	public void editName(String newName);
	
	public void editEmail(String newEmail);

	public boolean takeAction();


	public boolean fold();

	public String getName();
	public int geTotalCash();
	public boolean check();

	public boolean bet(int money);

	public boolean leaveGame(Player player);
	public void getLog(LinkedList<String> i_game_logs);
	public void getLog(String i_game_logs);


	public boolean giveMoney(int money);



	public boolean createGame(Player player, GameType type, int buyIn, int chipPolicy, int minBet, 
			   int minPlayers, int maxPlayers, boolean spectatable);


	public LinkedList<Game> Search(String playerName,int potSize);

	public boolean joinGame(Player player);
	
    public void login();
	
	public void logout();
}
