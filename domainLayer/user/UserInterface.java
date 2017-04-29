package user;

import java.util.LinkedList;

import Game.Game;
import Game.Player;

public interface UserInterface {

	public void GameUpdated();



	public boolean takeAction();


	public boolean fold();


	public boolean check();

	public boolean bet(int money);

	public boolean leaveGame();


	public boolean giveMoney(int money);



	public boolean createGame(Player player);


	public LinkedList<Game> Search(String playerName,int potSize);

	public boolean joinGame(Game game, Player player);
}
