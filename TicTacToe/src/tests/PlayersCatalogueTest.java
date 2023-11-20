package tests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.HumanPlayer;
import model.MyList;
import model.Player;
import model.PlayersCatalogue;
import model.RandomPlayer;

/**
 * This JUnit class tests some methods of
 * PlayerCatalog class and also tests:
 * compareTo , Score Formula , equals 
 * for the player class.
 *  */
class PlayersCatalogueTest {
	PlayersCatalogue playersCatalogue;
	
	@BeforeEach
	void init() {
		this.playersCatalogue = new PlayersCatalogue();
		this.playersCatalogue.addPlayer(new HumanPlayer("bill"));
		this.playersCatalogue.addPlayer(new HumanPlayer("george"));
		this.playersCatalogue.addPlayer(new HumanPlayer("Mr.Bean"));
		
	}
	
	@Test
	@DisplayName("Check get names of players")
	void testGetPlayerNames() {
		String[] nameStrings;
		
		nameStrings = this.playersCatalogue.getPlayerNames();
		
		assertAll("Check player names",
				()->assertEquals(nameStrings[0], "bill","Name of first player is bill"),
				()->assertEquals(nameStrings[1],"george","Name of second player is george"),
				()->assertEquals(nameStrings[2],"Mr.Bean","Name of third player is Mr.Bean"));
		
	}
	
	@Test
	@DisplayName("Check get player")
	void testGetPlayer() {
		HumanPlayer p1 = new HumanPlayer("Minws");
		
		this.playersCatalogue.addPlayer(p1);
		
		assertTrue(this.playersCatalogue.getPlayer(0).getName().equals("bill"));
		assertTrue(this.playersCatalogue.getPlayer(3)==p1);
		
		assertThrows(ArrayIndexOutOfBoundsException.class, ()->this.playersCatalogue.getPlayer(-1));
		Throwable throwableException=assertThrows(ArrayIndexOutOfBoundsException.class,()->this.playersCatalogue.getPlayer(-1));
		assertEquals(throwableException.getMessage(),"-1 is not a valid index");
		
	}
	
	@Test
	@DisplayName("Check if find player by name works as expected")
	void testFindPlayerByName() {
		Player p1 = this.playersCatalogue.getPlayer(0);
		Player p2 = this.playersCatalogue.getPlayer(1);
		Player p3 = this.playersCatalogue.getPlayer(2);
		
		assertAll("Find players by name",
				()->assertEquals(this.playersCatalogue.findPlayerByName("bill"), p1,"The fisrt player should be bill"),
				()->assertEquals(this.playersCatalogue.findPlayerByName("george"),p2,"The second player should be george"),
				()->assertEquals(this.playersCatalogue.findPlayerByName("Mr.Bean"),p3,"The third player should be Mr.Bean"),
				()->assertEquals(this.playersCatalogue.findPlayerByName("kappas"), null,"The name kappas does not represent a player"));
				
	}
	
	@Test
	@DisplayName("Check if it finds the hall of fame as expected")
	void testFindHallOfFame() {
		MyList<Player>hallOfFame;
		
		Player p1 = new HumanPlayer();
		p1.setScore(30);
		Player p2 = new HumanPlayer();
		p2.setScore(50);
		Player p3 = new HumanPlayer();
		p3.setScore(20.5);
		
		this.playersCatalogue.addPlayer(p1);
		this.playersCatalogue.addPlayer(p2);
		this.playersCatalogue.addPlayer(p3);
		
		hallOfFame = this.playersCatalogue.findHallOfFame(2);
		
		assertEquals(hallOfFame.get(0),p2,"The first player should be p2");
		assertEquals(hallOfFame.get(1),p1,"The second player should be p1");
			
	}
	
	@Test
	@DisplayName("Check if compare to for players works properly")
	void testCompareTo() {
		
		Player p1 = new HumanPlayer();
		p1.setScore(30);
		Player p2 = new HumanPlayer();
		p2.setScore(50);
		Player p3 = new HumanPlayer();
		p3.setScore(30);
		
		//Check REVERSE SORTING
		assertAll("Check if compare to for players works properly",
				()->assertEquals(p1.compareTo(p2),1,"p1 has smaller score than p2"),
				()->assertEquals(p1.compareTo(p3),0,"p1 and p3 have the same score"),
				()->assertEquals(p2.compareTo(p3),-1,"p2 has bigger score than p3"));
	
	}

	@Test
	@DisplayName("Check if the score is calculated correctly")
	void testScoreFormula() {
		Player p1 = new HumanPlayer();
		Player p2 = new HumanPlayer();
		Player p3 = new HumanPlayer();
		
		p1.setWinCounter(1);
		p1.setLoseCounter(2);
		p1.setGameCounter(4);
		p1.updateScore();
		
		p2.setGameCounter(5);
		p2.setWinCounter(3);
		p2.setLoseCounter(0);
		p2.updateScore();
		
		p3.setGameCounter(9);
		p3.setWinCounter(5);
		p3.setLoseCounter(4);
		p3.updateScore();
		
		assertAll("Check if score formula works",
				()->assertEquals(p1.getScore(), 37.5),
				()->assertEquals(p2.getScore(), 80.0),
				()->assertEquals(p3.getScore(), 55.55555555555556));

	}
	
	@Test
	@DisplayName("Check if the equals override for the player works correctly")
	void testEquals() {
		
		Player p1 = this.playersCatalogue.getPlayer(0);
		Player p2 = new HumanPlayer("bill");
		Player p3 = new RandomPlayer();
		
		assertAll("Check equals method",
				()->assertEquals(p1.equals(p1),true,"p1 should equal to p1"),
				()->assertEquals(p1.equals(null), false,"p1 should not equal to null"),
				()->assertEquals(p1.equals(p3),false,"p1 does not equal to p3 humanPlayer!=RandomPlayer"),
				()->assertEquals(p1.equals(p2),true,"p1 should be equal to p2 based the name")
				);
	}

}