package System;


import java.util.LinkedList;


public class GameLogs {
	private int IDGame;
	 LinkedList<String> log = new LinkedList<String>();
	 public GameLogs(int IDGame){
		 this.IDGame = IDGame;
		 
	 }
	 public void addLog(String l){
		 log.add(l);
		
	 }
	 public LinkedList<String> getLog(){
		 return log;
	 }
	 public int getGameID(){
		 return IDGame;
	 }
	 
}
