package Game;

import java.util.LinkedList;

import Game.Enum.GameType;

public class GamePreferences {
	 private GameType gameTypePolicy;
	 private int buyInPolicy;
	 private int chipPolicy;
	 private int minBet;
	 private int minPlayersNum;
	 private int maxPlayersNum;
	 private boolean spectatable;
	private boolean leaguable;
	 private int league;
	 private LinkedList<checker> CheckPlayer;
	 private boolean  checking;
	 
	 public GamePreferences(GameType gameTypePolicy, int buyInPolicy,
			int chipPolicy, int minBet, int minPlayersNum, int maxPlayersNum,
			boolean spectatable,boolean leaguable,int league) throws Exception {
       CheckPlayer = new LinkedList<checker>();
       checking = true;
       if(buyInPolicy >= 0){
    	   this.buyInPolicy = buyInPolicy;
    	   CheckPlayer.add((checker)player->{
    		   checking = checking && false;
    	   });
    	   
       }
       if(chipPolicy >= 0 ){
    	   
    	   this.chipPolicy = chipPolicy;
    	   CheckPlayer.add((checker)player->{
    		   checking = checking && false;
    	   });
       }
       if(minBet > 0){
    	   this.minBet = minBet;   
    	   CheckPlayer.add((checker)player->{
    		   checking = checking && false;
    	   });
       }
		 if(   minPlayersNum < 2 || 
				 maxPlayersNum < 2 || minPlayersNum > maxPlayersNum)
			 throw new Exception("illegal arguments");
		 
		this.gameTypePolicy = gameTypePolicy;
		
		
		
		this.minPlayersNum = minPlayersNum;
		this.maxPlayersNum = maxPlayersNum;
		this.spectatable = spectatable;
		if(leaguable){
		this.leaguable = leaguable;
		this.league = league;
		CheckPlayer.add((checker)player->{
 		   checking = checking && player.getUser().getLeague() == this.league;
 	   });
		}
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
	 public boolean isLeaguable() {
		return leaguable;
	}

	public int getLeague() {
		return league;
	}
	
	public boolean checkPlayer(Player player){
		CheckPlayer.forEach(c ->{c.check(player);});
		boolean checkingResult = checking;
		checking = true;
		return checkingResult;
		
	}

}