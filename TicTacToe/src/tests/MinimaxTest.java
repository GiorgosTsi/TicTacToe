package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import controller.GameController;
import model.GameBoard;
import model.GameModel;
import model.GodPlayer;
import model.HumanPlayer;

/**
 * This class contains tests
 * for the mini_max algorithm.
 * Tests if the god player places
 * his moves at the right spot,and
 * if he makes the suitable decisions. 
 *  */
class MinimaxTest {
	GameBoard gameBoard;
	GameBoard gameBoard1;
	GameBoard gameBoard2;
	GameBoard gameBoard3;
	GameBoard gameBoard4;
	GameController gameController;
	GameModel model2;
	int[] move;
	final int NO_PLAYER = 0;
	final int PLAYER_O = -1;
	final int PLAYER_X = 1;
	final int MAX_MOVES = 9;
	
	@Test
	@BeforeEach
	void init() {
		this.gameBoard = new GameBoard(0);
		this.gameBoard1 = new GameBoard(0);
		this.gameBoard2 = new GameBoard(0);
		this.gameBoard3 = new GameBoard(1);
		this.gameBoard4 = new GameBoard(1);
		//the X player starts.Mover = true
		this.gameController = new GameController();
		this.model2 = new GameModel(this.gameController);
		this.model2.setGameBoard(this.gameBoard);
		this.gameController.setModel(model2);
	}
	
	@Test
	@DisplayName("Check if the minimax algorithm and the best move works as expected")
	void testBestMove() {
		GodPlayer godPlayer = new GodPlayer("Hall"); 
		HumanPlayer humanPlayer = new HumanPlayer("Kappas");
		
		
		this.model2.selectPlayer(godPlayer,0);
		this.model2.selectPlayer(humanPlayer,1);

		
		this.gameBoard.makeMove(0,2);//X
		this.gameBoard.makeMove(0,0);//O
		this.gameBoard.makeMove(1,1);//X
		
		assertEquals(this.gameBoard.getMoves(), 3,"Total num of placed moves should be 3");
		
		this.gameBoard.makeMove(2,0);//O
		this.gameBoard.makeMove(1,0);//X
		this.gameBoard.makeMove(1,2);//O
		this.gameBoard.makeMove(0,1);//X
		this.gameBoard.makeMove(2,1);//O
		
		assertEquals(this.gameBoard.getMoves(), 8,"Total num of placed moves should be 8");

		//This move should be placed by X_Player at (2,2) cell
		this.move = this.gameController.bestMove(this.gameBoard.getMoverMark());
		
		this.gameBoard.makeMove(move[0],move[1]);
		
		System.out.println(move[0]+","+move[1]);
		
		assertAll("Check moves and mark",
				()->assertEquals(this.gameBoard.getMoves(),9,"Total num of placed moves should be 9"),
				()->assertEquals(this.gameBoard.getBoardMark(2,2),PLAYER_X,"The last move should be placed by X player")
				);

		
		
		
		this.model2.setGameBoard(gameBoard1);
		
		this.gameBoard1.makeMove(0,0);//X
		this.gameBoard1.makeMove(0,1);//O
		this.gameBoard1.makeMove(1,0);//X
		this.gameBoard1.makeMove(1,1);//O
		
		/*It is player's X turn to place move
		 *but we see that he wins if the move is placed 
		 *at (2,0) ,so the god player should place 
		 *it there
		 *  */
		this.move = this.gameController.bestMove(this.gameBoard1.getMoverMark());
		
		System.out.println(move[0]+","+move[1]);
		
		//Make the move
		this.gameBoard1.makeMove(move[0],move[1]);
		
		assertAll("Check moves and mark",
				()->assertEquals(this.gameBoard1.getMoves(),5,"Total num of placed moves should be 5"),
				()->assertEquals(this.gameBoard1.getBoardMark(2,0),PLAYER_X,"The last move should be placed by X player"),
				()->assertEquals(this.model2.noPlay(),true,"The game must be over"),
				()->assertEquals(this.gameBoard1.getWinner(),PLAYER_X,"The winner of the game must be player X"),
				()->assertEquals(this.gameBoard1.getCombination(),"col0","The triplet must be at row 1")
				);
		
		
		
		
		this.model2.setGameBoard(gameBoard2);
		
		this.gameBoard2.makeMove(0,0);//X
		this.gameBoard2.makeMove(0,1);//O
		this.gameBoard2.makeMove(1,1);//X
		this.gameBoard2.makeMove(2,2);//O
		
		/*
		 * Now we can see that the best
		 * possible move for player X is 
		 * (1,0) because he can make a triplet
		 * at row1 if the opponent places
		 * a move at (2,0) or at col0 if
		 * the opponent places a move at
		 * (1,2)
		 *	*/
		this.move = this.gameController.bestMove(this.gameBoard2.getMoverMark());
		
		System.out.println(move[0]+","+move[1]);
		
		//Make the move
		this.gameBoard2.makeMove(move[0],move[1]);
		
		assertAll("Check moves and mark",
				()->assertEquals(this.gameBoard2.getMoves(),5,"Total num of placed moves should be 5"),
				()->assertEquals(this.gameBoard2.getBoardMark(1,0),PLAYER_X,"The last move should be placed by X player"),
				()->assertEquals(this.model2.noPlay(),false,"The game must not be over"),
				()->assertEquals(this.gameBoard2.getWinner(),NO_PLAYER,"The winner of the game must be player X")
				);
		
		this.gameBoard2.makeMove(2,0);//O
		
		/*
		 * Now the god player must place a move at 
		 * (1,2) in order to win the game
		 *	*/
		this.move = this.gameController.bestMove(this.gameBoard2.getMoverMark());
		
		System.out.println(move[0]+","+move[1]);
		
		//Make the move
		this.gameBoard2.makeMove(move[0],move[1]);
		
		assertAll("Check moves and mark",
				()->assertEquals(this.gameBoard2.getMoves(),7,"Total num of placed moves should be 5"),
				()->assertEquals(this.gameBoard2.getBoardMark(1,2),PLAYER_X,"The last move should be placed by X player"),
				()->assertEquals(this.model2.noPlay(),true,"The game must be over"),
				()->assertEquals(this.gameBoard2.getWinner(),PLAYER_X,"The winner of the game must be player X")
				);
		
		
		
		
		
		/*Empty the game board*/
		this.model2.setGameBoard(gameBoard3);
		
		this.gameBoard3.makeMove(0,1);//O
		this.gameBoard3.makeMove(0,0);//X
		this.gameBoard3.makeMove(1,1);//O
		this.gameBoard3.makeMove(1,0);//X
		this.gameBoard3.makeMove(2,0);//O
		this.gameBoard3.makeMove(2,1);//X
		this.gameBoard3.makeMove(2,2);//O
		
		
		/*
		 * Now we can see that there is no 
		 * possible way for Player X to win 
		 * but Player O being his opponent
		 * can still win the game,so the 
		 * god player should prefer for the 
		 * game to end as a tie instead of 
		 * losing it. Now the god player must place a move at 
		 * (0,2) in order to end the game as a tie
		 * in order to block the Player O from 
		 * making an anti diagonal win
		 *	*/

		this.move = this.gameController.bestMove(this.gameBoard3.getMoverMark());
		
		System.out.println(move[0]+","+move[1]);
		
		//Make the move
		this.gameBoard3.makeMove(move[0],move[1]);
		
		assertAll("Check moves and mark",
				()->assertEquals(this.gameBoard3.getMoves(),8,"Total num of placed moves should be 8"),
				()->assertEquals(this.gameBoard3.getBoardMark(0,2),PLAYER_X,"The last move should be placed by X player"),
				()->assertEquals(this.model2.noPlay(),false,"The game must not be over"),
				()->assertEquals(this.gameBoard3.getWinner(),NO_PLAYER,"There is no winner for this game")
				);	
		
		/*Test that god player also block his opponent:*/
		
		this.model2.setGameBoard(gameBoard4);//new game_board
		//O's turn:
		this.model2.placeMove(0, 0);//O==Human player
		this.model2.placeMove(1,1);//X==god player will place move at 1,1
		this.model2.placeMove(0, 1);//O
		/*Now the god player should
		 *place his move  at 0,2 so
		 *he block his opponent triplet. 
		 *  */
		this.move = this.gameController.bestMove(1);//God's player mark == X == 1
		
		this.model2.placeMove(move[0], move[1]);
		
		assertAll("Check moves and mark",
				()->assertEquals(this.gameBoard4.getMoves(),4,"Total num of placed moves should be 4"),
				()->assertEquals(this.gameBoard4.getBoardMark(0,2),PLAYER_X,"This move should be placed by X player==God"),
				()->assertEquals(this.model2.noPlay(),false,"The game must not be over")
				);	
		
	}
	
	

	
}
