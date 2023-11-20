package tests;

import static org.junit.jupiter.api.Assertions.assertAll;      
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import model.HumanPlayer;
import model.Player;
import model.PlayersCatalogue;
import model.MyList;

/**
 * This JUnit class tests
 * if our generic list works properly.
 * We test it by using players
 * on it. 
 *  */
class PlayerListTest {
	Player[] pls;
	MyList<Player> players;
	MyList<Player> players2;
	
	@SuppressWarnings("unchecked")
	@BeforeEach
	void Init() {
		pls = new Player[5];	
		players = new MyList<Player>();
		players2 = new MyList<Player>(5);
	}
	

	@Test
	@DisplayName("Checks The size and the capacity of the list after some adds")
	void testAdd() {
		
		assertEquals(players.getSize(),0,"The initial size should be 0");
			
		players.addExpandable(new HumanPlayer());
		players.addExpandable(new HumanPlayer());
		players.addExpandable(new HumanPlayer());
		players.addExpandable(new HumanPlayer());
		players.addExpandable(new HumanPlayer());
		players.addExpandable(new HumanPlayer());
		players.addExpandable(new HumanPlayer());
		players.addExpandable(new HumanPlayer());
		players.addExpandable(new HumanPlayer());

		
		assertAll("capacity and size",
				()->assertFalse(players.isEmpty()),
				()->assertEquals(players.getSize(),9,"the size of the list should be 9"),
				()->assertEquals(players.capacity(), 10,"the capacity should be 10")
				);
		
	}
	
	@Test
	@DisplayName("Test static add")
	void testAddStatic() {
		
		assertEquals(players2.getSize(),0,"initial size should be 0");
		
		players2.addStatic(new HumanPlayer());
		
		assertEquals(players2.getSize(), 1);
		
		players2.addStatic(new HumanPlayer());
		players2.addStatic(new HumanPlayer());
		players2.addStatic(new HumanPlayer());
		players2.addStatic(new HumanPlayer());
		
		assertEquals(players2.getSize(), 5);
		
		players2.addStatic(new HumanPlayer());
		players2.addStatic(new HumanPlayer());
		//the size remains 5 , because the list is immutable
		assertEquals(players2.getSize(),5);
	}
	
	@Test
	@DisplayName("Check if the sort by score works")
	void testAddScoreSorting() {
		
		players2.addStatic(new HumanPlayer(10));
		players2.addStatic(new HumanPlayer(20));
		players2.addStatic(new HumanPlayer(0));
		players2.addStatic(new HumanPlayer(40));
		
		assertEquals(players2.getSize(), 4);
		
		assertAll("Order Preserving",
				()->assertEquals(players2.get(0).getScore(),40,"the first score player should be equal to 50"),
				()->assertEquals(players2.get(1).getScore(),20,"the second score player should be equal to 20"),
				()->assertEquals(players2.get(2).getScore(),10,"the first score player should be equal to 10"),
				()->assertEquals(players2.get(3).getScore(),0,"the first score player should be equal to 0")
				);
		
		players2.addStatic(new HumanPlayer(50));
		players2.addStatic(new HumanPlayer(4));
		
		assertEquals(players2.capacity(), 5,"The capacity should not change");
		
		assertAll("Order Preserving",
				()->assertEquals(players2.get(0).getScore(),50,"the first score player should be equal to 50"),
				()->assertEquals(players2.get(1).getScore(),40,"the second score player should be equal to 40"),
				()->assertEquals(players2.get(2).getScore(),20,"the third score player should be equal to 20"),
				()->assertEquals(players2.get(3).getScore(),10,"the fourth score player should be equal to 10"),
				()->assertEquals(players2.get(4).getScore(),4,"the fifth score player should be equal to 4")
				);
	}
	
	@Test
	@DisplayName("Chart list access should respects List boundaries")
	void testGetOutOfBoundsThrowsException() {
		assertThrows(ArrayIndexOutOfBoundsException.class, ()->players2.get(-1));		
		Throwable exception = assertThrows(ArrayIndexOutOfBoundsException.class, ()->players2.get(6));
		assertEquals(exception.getMessage(), "6 is not a valid index");
	}

	@Test
	@DisplayName("Checks if the list can expand")
	void testEnlarge() {
		
		/* adding 10 players inside the list,just as the initial capacity*/
		for(int i=0;i<10;i++) {
			players.addExpandable(new HumanPlayer());
		}
		
		assertEquals(players.capacity(), 10,"The intitial capacity is 10");
		
		players.addExpandable(new HumanPlayer());
			
		assertAll("resizeable",
				()->assertEquals(players.capacity(), 20,"The size musth be resized to 20"),
				()->assertEquals(players.getSize(),11,"The final num of players should be 11")
				);
	}
		
	@Test
	@DisplayName("Checks if the isEmpty routine works")
	void testEmpty() {
		
		assertTrue(players.isEmpty());
		
		players.addStatic(new HumanPlayer());
		
		assertFalse(players.isEmpty());
	}
	
	@Test
	@DisplayName("Check if the contains routine works")
	void testContains() {
		HumanPlayer p1 = new HumanPlayer();
		
		HumanPlayer p2 = new HumanPlayer();

		assertFalse(players.contains(p1));
		assertFalse(players.contains(p2));
		
		players.addStatic(p1);
		
		assertAll("Contains",
				()->assertTrue(players.contains(p1)),
				()->assertFalse(players.contains(p2))
				);
	}
	
	@Test
	@DisplayName("Check if get works as expected")
	void testGet() {
		HumanPlayer p1 = new HumanPlayer();
		HumanPlayer p2 = new HumanPlayer();
		HumanPlayer p3 = new HumanPlayer();
		HumanPlayer p4 = new HumanPlayer();
		HumanPlayer p5 = new HumanPlayer();
		HumanPlayer p6 = new HumanPlayer();
		
		players.addExpandable(p1);
		players.addExpandable(p2);
		players.addExpandable(p3);
		players.addExpandable(p4);
		players.addExpandable(p5);
		players.addExpandable(p6);
		
		assertAll("Get",
				()->assertEquals(players.get(0), p1),
				()->assertEquals(players.get(1), p2),
				()->assertEquals(players.get(2), p3),
				()->assertEquals(players.get(3), p4),
				()->assertEquals(players.get(4), p5),
				()->assertEquals(players.get(5), p6)
				);
		
	}
	
	@Test
	@DisplayName("Check if the getArrayofPlayers routine works as expected")
	void testGetArrayOfPlayers() {

		HumanPlayer p1 = new HumanPlayer(97);
		HumanPlayer p2 = new HumanPlayer(10.28);
		HumanPlayer p3 = new HumanPlayer(80.5);
		HumanPlayer p4 = new HumanPlayer(100);
		HumanPlayer p5 = new HumanPlayer(15);
		
		assertEquals(players2.getSize(), 0);
		assertEquals(players2.capacity(), 5);
		
		players2.addStatic(p1);
		players2.addStatic(p2);
		players2.addStatic(p3);
		players2.addStatic(p4);
		players2.addStatic(p5);
		
		assertEquals(players2.getSize(), 5);
		assertEquals(players2.capacity(), 5);
		
		pls = players2.toArray(pls);
		
		assertAll("Array same as list",
				()->assertEquals(pls.length,5),
				()->assertEquals(pls[0], p4),
				()->assertEquals(pls[1], p1),
				()->assertEquals(pls[2], p3),
				()->assertEquals(pls[3], p5),
				()->assertEquals(pls[4], p2)
				);	
	}
	
	@Test
	@DisplayName("Check if clear works properly")
	void testClear() {
		
		for(int i=0;i<25;i++) {
			players.addExpandable(new HumanPlayer());
		}
		
		assertEquals(players.getSize(), 25,"Size after add equals to the num of adds");
		assertEquals(players.capacity(), 40,"Capacity should be equal to 40");
		
		
		players.clear();
		
		assertEquals(players.getSize(), 0,"Number of players should be 0");
		assertEquals(players.capacity(), 40,"Total initial capacity should be equal to 40");
	}
	
	@Test
	@DisplayName("Check if remove works as expected without caring about sorting")
	void testRemoveExpandableList() {
		
		HumanPlayer p1 = new HumanPlayer(40);
		HumanPlayer p2 = new HumanPlayer(10);
		HumanPlayer p3 = new HumanPlayer(80.5);
		HumanPlayer p4 = new HumanPlayer(100);
		HumanPlayer p5 = new HumanPlayer(0);
		HumanPlayer p6 = new HumanPlayer(11);
		HumanPlayer p7 = new HumanPlayer(30);
		
		assertEquals(players.getSize(), 0,"Intial size should be 0");
		assertEquals(players.capacity(), 10,"Initial capacity should be 10");
		
		players.addExpandable(p1);
		players.addExpandable(p2);
		players.addExpandable(p3);
		players.addExpandable(p4);
		players.addExpandable(p5);
		players.addExpandable(p6);
		players.addExpandable(p7);
		
		assertAll("Add",
				()->assertEquals(players.get(0), p1,"first player should be p1"),
				()->assertEquals(players.get(1), p2,"second player should be p2"),
				()->assertEquals(players.get(2), p3,"third player should be p3"),
				()->assertEquals(players.get(3), p4,"fourth player should be p4"),
				()->assertEquals(players.get(4), p5,"fifth player should be p5"),
				()->assertEquals(players.get(5), p6,"sixth player should be p6"),
				()->assertEquals(players.get(6), p7,"seventh player should be p7"),
				()->assertEquals(players.getSize(), 7,"Size after 7 adds should be 7"),
				()->assertEquals(players.capacity(), 10,"Capacity should be 10")
				);
		
		players.remove(3);
		
		assertAll("Remove",
				()->assertEquals(players.get(0), p1,"first player should be p1"),
				()->assertEquals(players.get(1), p2,"second player should be p2"),
				()->assertEquals(players.get(2), p3,"third player should be p3"),
				()->assertEquals(players.get(3), p5,"fourth player should be p5"),
				()->assertEquals(players.get(4), p6,"fifth player should be p6"),
				()->assertEquals(players.get(5), p7,"sixth player should be p7"),
				()->assertEquals(players.getSize(), 6,"Size after removing 1 element should be 6"),
				()->assertEquals(players.capacity(), 10,"Size should not be changed by the process")
				);
		
		players.remove(4);
		players.remove(1);
		
		assertAll("Remove",
				()->assertEquals(players.get(0), p1,"first player should be p1"),
				()->assertEquals(players.get(1), p3,"second player should be p4"),
				()->assertEquals(players.get(2), p5,"third player should be p5"),
				()->assertEquals(players.get(3), p7,"fourth player should be p7"),
				()->assertEquals(players.getSize(), 4,"Size after removing 2 more elements should be 4"),
				()->assertEquals(players.capacity(), 10,"Size should not be changed by the process")
				);		
	}
	
	@Test
	@DisplayName("Check if remove works as expected,we care about sorting")
	void testRemoveStaticList() {
		HumanPlayer p1 = new HumanPlayer(30);
		HumanPlayer p2 = new HumanPlayer(10);
		HumanPlayer p3 = new HumanPlayer(80.5);
		HumanPlayer p4 = new HumanPlayer(100);
		HumanPlayer p5 = new HumanPlayer(11);
		
		assertEquals(players2.getSize(), 0);
		assertEquals(players2.capacity(), 5);
		
		players2.addStatic(p1);
		players2.addStatic(p2);
		players2.addStatic(p3);
		players2.addStatic(p4);
		players2.addStatic(p5);
		
		assertAll("Add",
				()->assertEquals(players2.get(0),p4,"first player should be p4"),
				()->assertEquals(players2.get(1),p3,"second player should be p3"),
				()->assertEquals(players2.get(2),p1,"third player should be p1"),
				()->assertEquals(players2.get(3),p5,"fourth player should be p5"),
				()->assertEquals(players2.get(4),p2,"fifth player should be p2"),
				()->assertEquals(players2.getSize(), 5,"Size after adding 5 players should be 5"),
				()->assertEquals(players2.capacity(), 5,"Size should not be changed by the process")
				);		
		
		players2.remove(2);
		
		assertAll("Remove",
				()->assertEquals(players2.get(0), p4,"first player should be p4"),
				()->assertEquals(players2.get(1), p3,"second player should be p3"),
				()->assertEquals(players2.get(2), p5,"third player should be p5"),
				()->assertEquals(players2.get(3), p2,"fourth player should be p2"),
				()->assertEquals(players2.getSize(), 4,"Size after removing 1 player should be 4"),
				()->assertEquals(players2.capacity(), 5,"Size should not be changed by the process")
				);	
		
		players2.remove(3);
		players2.remove(0);
		
		assertAll("Remove",
				()->assertEquals(players2.get(0), p3,"first player should be p3"),
				()->assertEquals(players2.get(1), p5,"second player should be p5"),
				()->assertEquals(players2.getSize(), 2,"Size after removing 2 more players should be 2"),
				()->assertEquals(players2.capacity(), 5,"Size should not be changed by the process")
				);	

	}
	
	@Test
	@Disabled
	void testReverseSort() {
		
		HumanPlayer p1 = new HumanPlayer(40);
		HumanPlayer p2 = new HumanPlayer(10);
		HumanPlayer p3 = new HumanPlayer(80.5);
		HumanPlayer p4 = new HumanPlayer(100);
		HumanPlayer p5 = new HumanPlayer(0);
		HumanPlayer p6 = new HumanPlayer(11);
		HumanPlayer p7 = new HumanPlayer(30);

		players.addExpandable(p1);
		players.addExpandable(p2);
		players.addExpandable(p3);
		players.addExpandable(p4);
		players.addExpandable(p5);
		players.addExpandable(p6);
		players.addExpandable(p7);

		assertTrue(players.get(0).getScore()==40);
		assertTrue(players.get(1).getScore()==10);
		assertTrue(players.get(2).getScore()==80.5);
		assertTrue(players.get(3).getScore()==100);
		assertTrue(players.get(4).getScore()==0);
		assertTrue(players.get(5).getScore()==11);
		assertTrue(players.get(6).getScore()==30);
		
		
		//players.reverseSort();
		
		
		assertTrue(players.get(0).getScore()==100);
		assertTrue(players.get(1).getScore()==80.5);
		assertTrue(players.get(2).getScore()==40);
		assertTrue(players.get(3).getScore()==30);
		assertTrue(players.get(4).getScore()==11);
		assertTrue(players.get(5).getScore()==10);
		assertTrue(players.get(6).getScore()==0);	
	}
}
