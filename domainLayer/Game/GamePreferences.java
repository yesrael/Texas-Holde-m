package Game;

import java.util.LinkedList;

import Game.Enum.GameType;

public class GamePreferences {
	 private GameType gameTypePolicy;
	 private int Limit;
	 private int buyInPolicy;
	 private int chipPolicy;
	 private int minBet;
	 private int minPlayersNum;
	 private int maxPlayersNum;
	 private boolean spectatable;
	 private boolean leaguable;
	 private int league;
	 private LinkedList<checker> CheckPlayer;
	 private LinkedList<checkingEquality> CheckEquality;
	 private boolean  checking;
	 private boolean  checkingEquality;
	 
	 public GamePreferences(GameType gameTypePolicy,int potLimit, int buyInPolicy,
			int chipPolicy, int minBet, int minPlayersNum, int maxPlayersNum,
			boolean spectatable,boolean leaguable,int league) throws Exception {
       CheckPlayer = new LinkedList<checker>();
       CheckEquality = new LinkedList<checkingEquality>();
       checking = true;
       checkingEquality = true;
	   this.buyInPolicy = buyInPolicy;
       if(buyInPolicy > 0){

    	   CheckPlayer.add((checker)player->{
    		   checking = checking && player.getUser().getTotalCash() > this.buyInPolicy;
    	   });
    	   
    	   CheckEquality.add(game->{
    		   checkingEquality = checkingEquality && game.getBuyInPolicy()== this.buyInPolicy;
    	   });
    	   
       }
	   this.chipPolicy = chipPolicy;
       if(chipPolicy > 0 ){
    	   

    	   CheckPlayer.add((checker)player->{
    		   checking = checking && player.getUser().getTotalCash() > this.chipPolicy + this.buyInPolicy;
    	   });
    	   
    	   CheckEquality.add(game->{
    		   checkingEquality = checkingEquality && game.getChipPolicy()== this.chipPolicy;
    	   });
    	   
       }
       this.minBet = minBet; 
       if(minBet > 0){ 
    	   CheckPlayer.add((checker)player->{
    		   checking = checking && player.getUser().getTotalCash() > this.minBet + this.chipPolicy + this.buyInPolicy;
    	   });
    	   
    	   CheckEquality.add(game->{
    		   checkingEquality = checkingEquality && game.getMinBet()== this.minBet;
    	   });
    	   
       }
       
       if(  ( minPlayersNum < 2 || 
    		 maxPlayersNum < 2 || minPlayersNum > maxPlayersNum) && (minPlayersNum!=-1 || maxPlayersNum!=-1))
			 throw new Exception("illegal arguments");
		 
		this.gameTypePolicy = gameTypePolicy;
		this.Limit=potLimit;
		   CheckEquality.add(game->{
    		   checkingEquality = checkingEquality && game.getGameTypePolicy()== this.gameTypePolicy && game.getLimit() == this.Limit;
    	   });
    	
		this.minPlayersNum = minPlayersNum;
		this.maxPlayersNum = maxPlayersNum;
		   CheckEquality.add(game->{
    		   checkingEquality = checkingEquality && (this.minPlayersNum == -1|| this.minPlayersNum == game.getMinPlayersNum()) && (this.maxPlayersNum == -1|| this.maxPlayersNum == game.getMaxPlayersNum());
    	   });
		
		this.spectatable = spectatable;
		   CheckEquality.add(game->{
    		   checkingEquality = checkingEquality && game.isSpectatable()== this.spectatable;
    	   });
		if(this.leaguable){
			this.leaguable = leaguable;
			this.league = league;
			CheckPlayer.add((checker)player->{
	 		   checking = checking && player.getUser().getLeague() == this.league;
	 	   });
			
			CheckEquality.add(game->{
	    		   checkingEquality = checkingEquality && game.getLeague() == this.league;
	    	   });
			
		}
		this.league = 0;
		
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

	
	public boolean checkEquality(GamePreferences game){
		CheckEquality.forEach(c ->{c.check(game);});
		boolean checkingResult = checkingEquality;
		checkingEquality = true;
		return checkingResult;
		
	}
	public int getLimit() {
		return Limit;
	}


}