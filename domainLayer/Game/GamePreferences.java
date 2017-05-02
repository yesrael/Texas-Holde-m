package Game;

import Game.Enum.GameType;

public class GamePreferences {
	 private GameType gameTypePolicy;
	 private int buyInPolicy;
	 private int chipPolicy;
	 private int minBet;
	 private int minPlayersNum;
	 private int maxPlayersNum;
	 private boolean spectatable;
	 
	 public GamePreferences(GameType gameTypePolicy, int buyInPolicy,
			int chipPolicy, int minBet, int minPlayersNum, int maxPlayersNum,
			boolean spectatable) throws Exception {

		 if(buyInPolicy < 0 || chipPolicy < 0 || minBet <= 0 || minPlayersNum < 2 || 
				 maxPlayersNum < 2 || minPlayersNum > maxPlayersNum)
			 throw new Exception("illegal arguments");
		this.gameTypePolicy = gameTypePolicy;
		this.buyInPolicy = buyInPolicy;
		this.chipPolicy = chipPolicy;
		this.minBet = minBet;
		this.minPlayersNum = minPlayersNum;
		this.maxPlayersNum = maxPlayersNum;
		this.spectatable = spectatable;
	}

	public GameType getGameTypePolicy() {
		return gameTypePolicy;
	}

	public int getBuyInPolicy() {
		return buyInPolicy;
	}

	public int getChipPolicy() {
		return chipPolicy;
	}

	public int getMinBet() {
		return minBet;
	}

	public int getMinPlayersNum() {
		return minPlayersNum;
	}

	public int getMaxPlayersNum() {
		return maxPlayersNum;
	}

	public boolean isSpectatable() {
		return spectatable;
	}
}