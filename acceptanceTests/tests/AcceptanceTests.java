package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import serviceLayer.*;

public class AcceptanceTests {
	
	private serviceLayerInterface service = serviceLayer.getInstance();

	@Test
	public void registerTestCase() {
		//missing argument
		assertEquals(service.register("usr1 123456 user_name user@name.com"), "REG FAILED");
		assertEquals(service.register("REG 123456 user_name user@name.com"), "REG FAILED");
		assertEquals(service.register("REG usr1 user_name user@name.com"), "REG FAILED");
		assertEquals(service.register("REG usr1 123456 user@name.com"), "REG FAILED");
		assertEquals(service.register("REG usr1 123456 user_name "), "REG FAILED");
		
		//short password
		assertEquals(service.register("REG usr1 1 user_name user@name.com"), "REG FAILED");
		assertEquals(service.register("REG usr1 1234567 user_name user@name.com"), "REG FAILED");
		
		//illegal email
		assertEquals(service.register("REG usr1 12345678 user_name user123"), "REG FAILED");
		assertEquals(service.register("REG usr1 12345678 user_name @.com"), "REG FAILED");
		assertEquals(service.register("REG usr1 12345678 user_name asd@.com"), "REG FAILED");
		assertEquals(service.register("REG usr1 12345678 user_name asd@qwe"), "REG FAILED");
		
		//successful registration
		assertEquals(service.register("REG usr1 12345678 user1 user1@example.com"), "REG DONE");
		assertEquals(service.register("REG usr2 12345678 user2 user2@example.com"), "REG DONE");
		assertEquals(service.register("REG usr3 12345678 user3 user3@example.com"), "REG DONE");
		assertEquals(service.register("REG usr4 12345678 user4 user4@example.com"), "REG DONE");
		assertEquals(service.register("REG usr5 12345678 user5 user5@example.com"), "REG DONE");
		
		//already exists user
		assertEquals(service.register("REG usr1 12345678 agbaria agbaria@gmail.com"), "REG FAILED");
	}

	
	
	@Test
	public void loginTestCase() {
		//missing argument
		assertEquals(service.login("usr 123456789", null), "LOGIN FAILED");
		assertEquals(service.login("LOGIN 123456789", null), "LOGIN FAILED");
		assertEquals(service.login("LOGIN usr", null), "LOGIN FAILED");
		
		//user does not exist
		assertEquals(service.login("LOGIN usr 123456789", null), "LOGIN FAILED");
		
		//incorrect password
		assertEquals(service.login("LOGIN usr1 123456789", null), "LOGIN FAILED");
		
		//successful login
		service.register("REG usr6 12345678 user6 user6@example.com");
		assertEquals(service.login("LOGIN usr6 12345678", null), "LOGIN DONE user6 0 0");
	}

	@Test
	public void logoutTestCase() {
		//missing argument
		assertEquals(service.logout("LOGOUT"), "LOGOUT FAILED NAME OR FULL INSTRUCTION MISSED");
		assertEquals(service.logout("user1"), "LOGOUT FAILED NAME OR FULL INSTRUCTION MISSED");

		//successful logout
		service.login("LOGIN usr6 12345678", null);
		assertEquals(service.logout("LOGOUT usr6"), "LOGOUT DONE");
	}

	@Test
	public void editUserPasswordTestCase() {
		//missing argument
		assertEquals(service.editUserPassword("*USER NAME* *OLDPASSWORD* *NEWPASSWORD*"), "EDITPASS FAILED");
		assertEquals(service.editUserPassword("EDITPASS *OLDPASSWORD* *NEWPASSWORD*"), "EDITPASS FAILED");
		assertEquals(service.editUserPassword("EDITPASS *USER NAME* *NEWPASSWORD*"), "EDITPASS FAILED");
		assertEquals(service.editUserPassword("EDITPASS *USER NAME* *OLDPASSWORD*"), "EDITPASS FAILED");
		
		//wrong old password
		assertEquals(service.editUserPassword("EDITPASS usr6 12345679 1234567899"), "EDITPASS FAILED");
		
		//short password
		assertEquals(service.editUserPassword("EDITPASS usr6 12345678 123"), "EDITPASS FAILED");
		
		//successful password editing
		assertEquals(service.editUserPassword("EDITPASS usr6 12345678 123123123"), "EDITPASS DONE");
	}
	
	@Test
	public void editUserEmailTestCase() {
		//missing argument
		assertEquals(service.editUserEmail("*USER NAME* *PASSWORD* *NEWEMAIL*"), "EDITEMAIL FAILED");
		assertEquals(service.editUserEmail("EDITEMAIL *PASSWORD* *NEWEMAIL*"), "EDITEMAIL FAILED");
		assertEquals(service.editUserEmail("EDITEMAIL *USER NAME* *NEWEMAIL*"), "EDITEMAIL FAILED");
		assertEquals(service.editUserEmail("EDITEMAIL *USER NAME* *PASSWORD*"), "EDITEMAIL FAILED");
		
		//incorrect password
		assertEquals(service.editUserEmail("EDITEMAIL usr6 1234456123 new@example.com"), "EDITEMAIL FAILED");
		
		//illegal email
		assertEquals(service.editUserEmail("EDITEMAIL usr6 12345678 newEmail"), "EDITEMAIL FAILED");
		assertEquals(service.editUserEmail("EDITEMAIL usr6 12345678 @.com"), "EDITEMAIL FAILED");
		assertEquals(service.editUserEmail("EDITEMAIL usr6 12345678 asd@.com"), "EDITEMAIL FAILED");
		assertEquals(service.editUserEmail("EDITEMAIL usr6 12345678 asd@qwe"), "EDITEMAIL FAILED");
		
		//successful email editing
		assertEquals(service.editUserEmail("EDITEMAIL usr6 12345678 new@example.com"), "EDITEMAIL FAILED");
		
	}
	
	@Test
	public void editUserAvatarTestCase() {
		// TODO Auto-generated method stub
		
	}

	@Test
	public void createGameTestCase() {
		//missing argument
		assertEquals(service.createGame("*USER NAME* *GAME PREF*"), "CREATEGAME FAILED");
		assertEquals(service.createGame("CREATEGAME *GAME PREF*"), "CREATEGAME FAILED");
		assertEquals(service.createGame("CREATEGAME *USER NAME*"), "CREATEGAME FAILED");
		
		String prefs = "gameTypePolicy=NO_LIMIT&"
				+ "potLimit=00&"
				+ "buyInPolicy=00&"
				+ "chipPolicy=0&"
				+ "minBet=00&"
				+ "minPlayersNum=2&"
				+ "maxPlayersNum=8&"
				+ "spectatable=T&"
				+ "leaguable=T&"
				+ "league=0";
		
		String details = "GameID=1&"
				+ "players=usr6&"
				+ "activePlayers=usr6&"
				+ "blindBit=00&"
				+ "CurrentPlayer=usr6&"
				+ "table=&"
				+ "MaxPlayers=8&"
				+ "activePlayersNumber=1&"
				+ "cashOnTheTable=0&"
				+ "CurrentBet=0";
		//non existing user
		assertEquals(service.createGame("CREATEGAME usr " + prefs), "CREATEGAME FAILED");
		
		//successful create game
		assertEquals(service.createGame("CREATEGAME usr6 " + prefs), "CREATEGAME usr6 " + details);
	}

	@Test
	public void listJoinableGamesTestCase() {
		// TODO Auto-generated method stub
		
	}

	@Test
	public void searchGamesByPrefsTestCase() {
		// TODO Auto-generated method stub
		
	}

	@Test
	public void searchGamesByPlayerNameTestCase() {
		// TODO Auto-generated method stub
		
	}

	@Test
	public void searchGamesByPotSizeTestCase() {
		// TODO Auto-generated method stub
		
	}

	@Test
	public void joinGameTestCase() {
		// TODO Auto-generated method stub
		
	}

	@Test
	public void listSpectatableGamesTestCase() {
		// TODO Auto-generated method stub
		
	}

	@Test
	public void spectateGameTestCase() {
		// TODO Auto-generated method stub
		
	}

	@Test
	public void leaveGameTestCase() {
		// TODO Auto-generated method stub
		
	}

	@Test
	public void ActionTestCase() {
		// TODO Auto-generated method stub
		
	}

}
