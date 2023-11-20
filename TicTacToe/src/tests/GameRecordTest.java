package tests;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.SecondaryLoop;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.GameRecord;
import model.HumanPlayer;
import model.MyList;
import model.Player;

/**
 * This JUnit class tests all the methods
 * of GameeRecord class and also checks
 * if addGood and Recent game of player's
 * lists works properly.
 *  */
class GameRecordTest {
	GameRecord[] games;
	Player[] gamePlayers;
	GameRecord gameRecord;
	GameRecord secondGame;
	Player p1 ;
	Player p2 ;
	Player p3 ;
	Player p4 ;

	@BeforeEach
	void init() {
		this.games = new GameRecord[5];
		this.gameRecord = new GameRecord();
		this.secondGame = new GameRecord();
		this.gamePlayers = this.gameRecord.getGamePlayers();
		this.p1 = new HumanPlayer("manos");
		this.p2 = new HumanPlayer("giorgos");
		this.p3 = new HumanPlayer("kappas");
		this.p4 = new HumanPlayer("keepos");
	}

	
	@Test
	@DisplayName("Check if select player works as expected")
	void testSelectPlayer() {
		HumanPlayer p1 = new HumanPlayer();
		HumanPlayer p2 = new HumanPlayer();
		
		assertEquals(this.gameRecord.getGamePlayers().length, 2);
		
		this.gameRecord.selectPlayer(p1, 0);
		
		assertAll("select player",
				()->assertEquals(this.gameRecord.getGamePlayers()[0],p1,"The first slot should contain the p1 player"),
				()->assertEquals(this.gameRecord.getGamePlayers()[1], null,"The second slot should not contain a player"),
				()->assertEquals(this.gameRecord.getGamePlayers().length, 2,"The length should be 2, it should not be  from the selection")
				);
		
		this.gameRecord.selectPlayer(p2, 1);
		
		    assertAll("player selected",
				()->assertEquals(this.gameRecord.getGamePlayers()[0], p1,"first player should be equal to p1"),
				()->assertEquals(this.gameRecord.getGamePlayers()[1], p2,"second player should be equal to p2"),
				()->assertEquals(this.gameRecord.getGamePlayers().length, 2,"The length of the list of game players should be 2")
				);
	}
	
	
	@Test
	@DisplayName("Check if myCompareTo works as expected")
	void testMyCompareToGameRecords() {
		
		this.gameRecord.selectPlayer(this.p1, 0);
		this.gameRecord.selectPlayer(this.p2, 1);
		
		this.secondGame.selectPlayer(this.p1, 0);
		this.secondGame.selectPlayer(this.p4, 1);
		
		//the second game is better than the first
		this.gameRecord.setResult(-1);
		this.secondGame.setResult(1);
		
		assertEquals(this.gameRecord.myCompareToGameRecords(secondGame, p1), 1,"second game is better than the first,it should return 1");
		
		
		
		/*
		 * The 2 games that we want to compare have the same result
		 * let's say that X wins, that means result==1
		 *	*/
		this.gameRecord.setResult(1);
		this.secondGame.setResult(1);
		
		/*
		 * The second opponent is a better player than the second one
		 * this means that the second game is a better one
		 * it has a bigger "value"
		 */
		this.p2.setScore(30);
		this.p4.setScore(40);
		
		assertEquals(this.gameRecord.myCompareToGameRecords(secondGame, p1), 1,"The 2 games have the same result but the second player is a better opponent,should return 1");
		
		
		
		/*
		 * The 2 games that we want to compare have the same result
		 * let's say that X wins, that means result==1
		 *	*/
		this.gameRecord.setResult(1);
		this.secondGame.setResult(1);
		
		/*
		 * The 2 opponents has the same score,
		 * that means that they are equally good players
		 *	*/
		this.p2.setScore(50);
		this.p4.setScore(50);
		
		//The second game is after the first game
		this.secondGame.setGameTime(LocalDateTime.now().plusMinutes(30));
		
		assertEquals(this.gameRecord.myCompareToGameRecords(secondGame, p1), 1,"The 2 games have the same result and the opponents are equally good,but the second game is newer");
		
		
		
		//The first game is better than the second one
		this.gameRecord.setResult(1);
		this.secondGame.setResult(0);
		
		//We don't care about the score of the opponents right now
		
		assertEquals(this.gameRecord.myCompareToGameRecords(secondGame, p1), -1,"The first game is better than the 2,it should return -1");
		
		
		
		/*
		 * The 2 games have the same result
		 * let's say that both games end as Tie
		 * that means that result==0
		 *	*/
		this.gameRecord.setResult(0);
		this.secondGame.setResult(0);
		
		//The first player is a better opponent
		this.p2.setScore(60);
		this.p4.setScore(55);
		
		/*
		 * The games have the same result but the first opponent is a better player,
		 * that makes the first game a better game in general
		 *	*/
		assertEquals(this.gameRecord.myCompareToGameRecords(secondGame, p1),-1,"The 2 games have same result but 2opponent less good than 1opponent");
		
		
		
		/*
		 * The 2 games have the same result
		 * let's say that both games end as win
		 * for the O player,that means result==-1
		 *	*/
		this.gameRecord.setResult(-1);
		this.secondGame.setResult(-1);
		
		
		//both opponents are equally good players
		this.p2.setScore(60);
		this.p4.setScore(60);
		
		//The first game is a newer game
		this.gameRecord.setGameTime(LocalDateTime.now().plusMinutes(40));//plus 40 because the second game is already increased by 30 above
		
		/*
		 * The 2 games have the same result and 
		 * the 2 opoonents have the same score,that means that
		 * they are equally good opponents for the player but
		 * the first game is a newer game so it has to be higher than
		 * the second one on the board
		 *	*/
		assertEquals(this.gameRecord.myCompareToGameRecords(secondGame, p1),-1,"Should return -1");
		
		
		/*If nothing from the above happends than means first game equals the second one*/
		
		
		this.gameRecord.setResult(0);
		this.secondGame.setResult(0);
		
		this.gameRecord.selectPlayer(p1, 0);
		this.gameRecord.selectPlayer(p2, 1);
		
		this.secondGame.selectPlayer(p1, 0);
		this.secondGame.selectPlayer(p4, 1);
		
		this.gameRecord.setGameTime(LocalDateTime.now());
		this.secondGame.setGameTime(LocalDateTime.now());
		
		assertEquals(this.gameRecord.myCompareToGameRecords(secondGame, p1),0);
	}
	
	
	@Test
	@DisplayName("Check if myToString method works as expected")
	void testMyToString() {
		
		/*
		 * Assume that p1 is our player and
		 * p2 is the opponent,it doesn't matter who we choose
		 * the opposite the same but with opposite result message 
		 * will appear to the opponent of the player
		 *	*/
		
		//set the game players to the spesific positions
		this.gameRecord.selectPlayer(this.p1, 0);//X player
		this.gameRecord.selectPlayer(this.p2, 1);//O player
		
		/*
		 * assume that X is the winner,in others words
		 * the gamePlayers[0] is the winner
		 * and the result of the game is 1
		 *	*/
		this.gameRecord.setResult(1);
		
		assertAll("the return of my to string,for X winner",
				()->assertEquals(this.gameRecord.getGamePlayers()[0], p1,"The first player is p1"),
				()->assertEquals(this.gameRecord.getGamePlayers()[1], p2,"The second player is p2"),
				()->assertEquals(this.gameRecord.getResult(),1,"The result equals 1"),
				//Check if the String returned for the winner of the game is the expected one
				()->assertEquals(this.gameRecord.myToString(p1, p2),"Win "+"vs "+p2.getName()+" "+this.gameRecord.getGameTime().format(DateTimeFormatter.ofPattern("dd-MM-HH:mm"))),
				//Chech if the String returned for the loser of the game is the expected one
				()->assertEquals(this.gameRecord.myToString(p2, p1),"Lose "+"vs "+p1.getName()+" "+this.gameRecord.getGameTime().format(DateTimeFormatter.ofPattern("dd-MM-HH:mm")))
				);
		
		/*
		 * assume that O is the winner of the game
		 * that means that gamePlayers[1] is the winner
		 * and the result of the game is -1
		 *	*/
		this.gameRecord.setResult(-1);
		
		assertAll("the return of my to string,for O winner",
				()->assertEquals(this.gameRecord.getGamePlayers()[0], p1,"The first player is p1"),
				()->assertEquals(this.gameRecord.getGamePlayers()[1], p2,"The second player is p2"),
				()->assertEquals(this.gameRecord.getResult(),-1,"The result equals -1"),
				//Check if the String returned for the loser of the game is the expected one
				()->assertEquals(this.gameRecord.myToString(p1, p2),"Lose "+"vs "+p2.getName()+" "+this.gameRecord.getGameTime().format(DateTimeFormatter.ofPattern("dd-MM-HH:mm"))),
				//Chech if the String returned for the winner of the game is the expected one
				()->assertEquals(this.gameRecord.myToString(p2, p1),"Win "+"vs "+p1.getName()+" "+this.gameRecord.getGameTime().format(DateTimeFormatter.ofPattern("dd-MM-HH:mm")))
				);
		
		/*
		 * assume that the game is a Tie
		 * that means that no one wins and
		 * the result of the game is 0
		 *	*/
		this.gameRecord.setResult(0);
		
		assertAll("the return of my to string for tie game",
				()->assertEquals(this.gameRecord.getGamePlayers()[0], p1,"The first player is p1"),
				()->assertEquals(this.gameRecord.getGamePlayers()[1], p2,"The second player is p2"),
				()->assertEquals(this.gameRecord.getResult(),0,"The result equals 0,the game is a Tie"),
				//Check if the String returned is the same for both of the game players,that happends because the game is a Tie 
				()->assertEquals(this.gameRecord.myToString(p1, p2),"Tie "+"vs "+p2.getName()+" "+this.gameRecord.getGameTime().format(DateTimeFormatter.ofPattern("dd-MM-HH:mm"))),
				//Check if the String returned is the same for both of the game players,that happends because the game is a Tie
				()->assertEquals(this.gameRecord.myToString(p2, p1),"Tie "+"vs "+p1.getName()+" "+this.gameRecord.getGameTime().format(DateTimeFormatter.ofPattern("dd-MM-HH:mm")))
				);
		
	}
	
	
	@Test
	@DisplayName("Check if addGoodGame method works as expected")
	void testAddGoodGame() {
		
		GameRecord g1= new GameRecord();
		GameRecord g2= new GameRecord();
		GameRecord g3= new GameRecord();
		GameRecord g4= new GameRecord();
		GameRecord g5= new GameRecord();
		GameRecord g6= new GameRecord();
		GameRecord g7= new GameRecord();
		GameRecord g8= new GameRecord();
		GameRecord g9= new GameRecord();
		GameRecord g10= new GameRecord();


		
		Player p5 = new HumanPlayer();
		Player p6 = new HumanPlayer();


		
		assertAll("The list of best games is empty",
				()->assertEquals(this.p1.getFiveBestGames()[0], null),
				()->assertEquals(this.p1.getFiveBestGames()[1], null),
				()->assertEquals(this.p1.getFiveBestGames()[2], null),
				()->assertEquals(this.p1.getFiveBestGames()[3], null),
				()->assertEquals(this.p1.getFiveBestGames()[4], null)
				);
		
		this.p1.setScore(50);
		this.p2.setScore(60);
		this.p3.setScore(70);
		this.p4.setScore(5);
		p5.setScore(0);
		p6.setScore(70);
		
		
		
		
		/* The 2 games have the same result,let's say it's a Tie*/
		g1.setResult(0);
		g2.setResult(0);
		
		
		g1.selectPlayer(p1, 0);
		g1.selectPlayer(p2, 1);
		
		g2.selectPlayer(p1, 0);
		g2.selectPlayer(p3, 1);
		
		
		/*
		 * Because the 2 games have the same result
		 * The best game is that with the better opponent
		 * for the player,that means g2 has to be higher on
		 * the board than g1
		 */
		p1.addGoodGame(g1);
		p1.addGoodGame(g2);
		
		
		assertAll("The list of best games is empty",
				()->assertEquals(this.p1.getFiveBestGames()[0], g2),
				()->assertEquals(this.p1.getFiveBestGames()[1], g1),
				()->assertEquals(this.p1.getFiveBestGames()[2], null),
				()->assertEquals(this.p1.getFiveBestGames()[3], null),
				()->assertEquals(this.p1.getFiveBestGames()[4], null)
				);
		

		//The player p1 wins
		g3.setResult(1);
		
		g3.selectPlayer(p1, 0);
		g3.selectPlayer(p2, 1);
		
		p1.addGoodGame(g3);
		
		
		/*
		 * Because the games g1 and g2 ended as Tie
		 * and g3 ended as win,the game g3 is better 
		 * than g1&g2
		 */
		assertAll("The list of best games is empty",
				()->assertEquals(this.p1.getFiveBestGames()[0], g3),
				()->assertEquals(this.p1.getFiveBestGames()[1], g2),
				()->assertEquals(this.p1.getFiveBestGames()[2], g1),
				()->assertEquals(this.p1.getFiveBestGames()[3], null),
				()->assertEquals(this.p1.getFiveBestGames()[4], null)
				);
		
		
		
		//The player p1 lost
		g4.setResult(-1);
		
		g4.selectPlayer(p1, 0);
		g4.selectPlayer(p4, 1);
		
		p1.addGoodGame(g4);
		
		
		/*
		 * The game g4 end as a lose for the player p1
		 * that means it was a less good game for him
		 * but there are empty slots in the list
		 * so it will be stored there down from the
		 * last one
		 */
		assertAll("The list of best games is empty",
				()->assertEquals(this.p1.getFiveBestGames()[0], g3),
				()->assertEquals(this.p1.getFiveBestGames()[1], g2),
				()->assertEquals(this.p1.getFiveBestGames()[2], g1),
				()->assertEquals(this.p1.getFiveBestGames()[3], g4),
				()->assertEquals(this.p1.getFiveBestGames()[4], null)
				);
		
		
		
		//The player p1 lost the game
		g5.setResult(-1);
		
		
		g5.selectPlayer(p1, 0);
		g5.selectPlayer(p2, 1);
		
		p1.addGoodGame(g5);
		
		/*
		 * The game g5 ends as a lose for the player
		 * p1 as the g4 does,but the opponent of g5
		 * is a better player than the opponent of the 
		 * g4,that fact makes the g5 a better game for the 
		 * p1
		 */
		assertAll("The list of best games is empty",
				()->assertEquals(this.p1.getFiveBestGames()[0], g3),
				()->assertEquals(this.p1.getFiveBestGames()[1], g2),
				()->assertEquals(this.p1.getFiveBestGames()[2], g1),
				()->assertEquals(this.p1.getFiveBestGames()[3], g5),
				()->assertEquals(this.p1.getFiveBestGames()[4], g4)
				);
		
		
		
		 
		g6.setResult(-1);
		
		g6.selectPlayer(p1, 0);
		g6.selectPlayer(p5, 1);
		
		
		p1.addGoodGame(g6);
		
		
		/*
		 * The game g6 ends as a lose for the p1
		 * but the opponent player p5 is a less good 
		 * player than the opponent of the g4 game,
		 * that means that g6 should not be placed 
		 * inside the list
		 */
		assertAll("The list of best games is empty",
				()->assertEquals(this.p1.getFiveBestGames()[0], g3),
				()->assertEquals(this.p1.getFiveBestGames()[1], g2),
				()->assertEquals(this.p1.getFiveBestGames()[2], g1),
				()->assertEquals(this.p1.getFiveBestGames()[3], g5),
				()->assertEquals(this.p1.getFiveBestGames()[4], g4)
				);
	
		
		
		g7.setResult(1);
		
		g7.selectPlayer(p1, 0);
		g7.selectPlayer(p2, 1);
		
		g7.setGameTime(LocalDateTime.now().plusMinutes(10));
		
		p1.addGoodGame(g7);
		
		/*
		 * The result of g7 is the same as the g3 and 
		 * the opponent is the same,but the g7 is a 
		 * more recent game than g3 so it should be 
		 * placed above g3 
		 */
		assertAll("The list of best games is empty",
				()->assertEquals(this.p1.getFiveBestGames()[0], g7),
				()->assertEquals(this.p1.getFiveBestGames()[1], g3),
				()->assertEquals(this.p1.getFiveBestGames()[2], g2),
				()->assertEquals(this.p1.getFiveBestGames()[3], g1),
				()->assertEquals(this.p1.getFiveBestGames()[4], g5)
				);
		
		
		
		g8.setResult(1);
		
		g8.selectPlayer(p1, 0);
		g8.selectPlayer(p4, 1);
		
		
		p1.addGoodGame(g8);
		
		/*
		 * The game g8 has the same result with
		 * the game g7 & g3 but the opponent for the p1
		 * at the g8 is a worst player so it a 
		 * less good game than the g7 & g3 in general
		 */
		assertAll("The list of best games is empty",
				()->assertEquals(this.p1.getFiveBestGames()[0], g7),
				()->assertEquals(this.p1.getFiveBestGames()[1], g3),
				()->assertEquals(this.p1.getFiveBestGames()[2], g8),
				()->assertEquals(this.p1.getFiveBestGames()[3], g2),
				()->assertEquals(this.p1.getFiveBestGames()[4], g1)
				);
		
		
		
		g9.setResult(0);
		
		g9.selectPlayer(p1, 0);
		g9.selectPlayer(p6, 1);
		
		g9.setGameTime(LocalDateTime.now().plusMinutes(50));
		
		p1.addGoodGame(g9);
		
		
		/*
		 * The game g9 has the same result as the g2
		 * and the opponents for the 2 games are equaly
		 * good players(have the same score) but the g9 is more recent than 
		 * g2,so it should be placed above it
		 */
		assertAll("The list of best games is empty",
				()->assertEquals(this.p1.getFiveBestGames()[0], g7),
				()->assertEquals(this.p1.getFiveBestGames()[1], g3),
				()->assertEquals(this.p1.getFiveBestGames()[2], g8),
				()->assertEquals(this.p1.getFiveBestGames()[3], g9),
				()->assertEquals(this.p1.getFiveBestGames()[4], g2)
				);
		
		
		g10.setResult(0);
		
		g10.selectPlayer(p1, 0);
		g10.selectPlayer(p6, 1);
		
		
		g10.setGameTime(LocalDateTime.now().minusMinutes(30));
		
		p1.addGoodGame(g10);
		
		/*
		 * The game g10 has the same result as the g9 and g2
		 * and the opponents are equally good players
		 * that means they have the same score,but
		 * the game g10 is an older game than g9 and g2 
		 * so it should not replace them 
		 */
		assertAll("The list of best games is empty",
				()->assertEquals(this.p1.getFiveBestGames()[0], g7),
				()->assertEquals(this.p1.getFiveBestGames()[1], g3),
				()->assertEquals(this.p1.getFiveBestGames()[2], g8),
				()->assertEquals(this.p1.getFiveBestGames()[3], g9),
				()->assertEquals(this.p1.getFiveBestGames()[4], g2)
				);
	
	}
	
	@Test
	@DisplayName("Check if recent games are sorted")
	void testAddRecentGames() {
		
		GameRecord g1= new GameRecord();
		GameRecord g2= new GameRecord();
		GameRecord g3= new GameRecord();
		GameRecord g4= new GameRecord();
		GameRecord g5= new GameRecord();
		GameRecord g6= new GameRecord();
		GameRecord g7= new GameRecord();
		GameRecord g8= new GameRecord();

		
		assertAll("The list of best games is empty",
				()->assertEquals(this.p1.getFiveLastGames()[0], null),
				()->assertEquals(this.p1.getFiveLastGames()[1], null),
				()->assertEquals(this.p1.getFiveLastGames()[2], null),
				()->assertEquals(this.p1.getFiveLastGames()[3], null),
				()->assertEquals(this.p1.getFiveLastGames()[4], null)
				);
		

		p1.addRecentGame(g1);
		p1.addRecentGame(g2);
		p1.addRecentGame(g3);
		p1.addRecentGame(g4);
		p1.addRecentGame(g5);
		
		assertAll("The list of recent games is empty",
				()->assertEquals(this.p1.getFiveLastGames()[0], g1),
				()->assertEquals(this.p1.getFiveLastGames()[1], g2),
				()->assertEquals(this.p1.getFiveLastGames()[2], g3),
				()->assertEquals(this.p1.getFiveLastGames()[3], g4),
				()->assertEquals(this.p1.getFiveLastGames()[4], g5)
				);
		
		g6.setGameTime(LocalDateTime.now().plusMinutes(30));
		
		p1.addRecentGame(g6);
		
		assertAll("The list of recent games is empty",
				()->assertEquals(this.p1.getFiveLastGames()[0], g6),
				()->assertEquals(this.p1.getFiveLastGames()[1], g1),
				()->assertEquals(this.p1.getFiveLastGames()[2], g2),
				()->assertEquals(this.p1.getFiveLastGames()[3], g3),
				()->assertEquals(this.p1.getFiveLastGames()[4], g4)
				);
		
		
		g7.setGameTime(LocalDateTime.now().plusMinutes(20));
		
		p1.addRecentGame(g7);
		
		assertAll("The list of recent games is empty",
				()->assertEquals(this.p1.getFiveLastGames()[0], g6),
				()->assertEquals(this.p1.getFiveLastGames()[1], g7),
				()->assertEquals(this.p1.getFiveLastGames()[2], g1),
				()->assertEquals(this.p1.getFiveLastGames()[3], g2),
				()->assertEquals(this.p1.getFiveLastGames()[4], g3)
				);
		
		
		g8.setGameTime(LocalDateTime.now().minusMinutes(5));
		
		p1.addRecentGame(g8);
		
		assertAll("The list of recent games is empty",
				()->assertEquals(this.p1.getFiveLastGames()[0], g6),
				()->assertEquals(this.p1.getFiveLastGames()[1], g7),
				()->assertEquals(this.p1.getFiveLastGames()[2], g1),
				()->assertEquals(this.p1.getFiveLastGames()[3], g2),
				()->assertEquals(this.p1.getFiveLastGames()[4], g3)
				);
	}

	
}