package tests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll; 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import controller.GameController;
import model.GameBoard;
import model.GameModel;
import model.GodPlayer;
import model.HumanPlayer;
import model.MyList;
/**
 * This class tests all the methods of
 * GameBoard class. 
 *  */
class GameBoardTest {
	GameBoard gameBoard;
	GameModel model;
	int[] move;
	final int NO_PLAYER = 0;
	final int PLAYER_O = -1;
	final int PLAYER_X = 1;
	final int MAX_MOVES = 9;
	
	@BeforeEach
	void init() {
		this.gameBoard  = new GameBoard(0);
		//the X player starts.Mover = true
		this.model = new GameModel(null);
		this.model.setGameBoard(gameBoard);
	}
	
	@Test
	@DisplayName("Check if the move is in bound")
	void testCheckDimValidity() {
		int row=-1;
		int col=4;
		
		assertThrows(IndexOutOfBoundsException.class, ()->this.gameBoard.checkDimValidity(row, col));
		Throwable exception=assertThrows(IndexOutOfBoundsException.class, ()->this.gameBoard.checkDimValidity(row, col));	
		assertEquals(exception.getMessage(), row + "," + col + " is not a valid board cell");
		
	}
	
	@Test
	@DisplayName("Check if move is valid")
	void testMoveValidity() {
		
		//make a move
		this.gameBoard.makeMove(0, 1);//it's X player(mover==true)
		
		assertThrows(IllegalArgumentException.class,()->this.gameBoard.makeMove(0, 1));
		Throwable exceptionThrowable=assertThrows(IllegalArgumentException.class,()->this.gameBoard.makeMove(0, 1));
		assertEquals(exceptionThrowable.getMessage(), "Non playable cell");
		
		//The board mark should remain untouched
		assertEquals(this.gameBoard.getBoardMark(0,1), PLAYER_X);
		
	}
	
	@Test
	@DisplayName("Check if mover changes and the mark of the player is the expected")
	void testMoverAndBoardMark() {
		
		assertEquals(this.gameBoard.getMoverMark(), PLAYER_X);
		this.gameBoard.makeMove(2, 2);
		
		assertAll("Check Mover and Board Mark",
				()->assertEquals(this.gameBoard.getMover(), false,"The mover should be" + this.gameBoard.getMover()),
				()->assertEquals(this.gameBoard.getBoardMark(2, 2), PLAYER_X,"Expected: " + this.gameBoard.getBoardMark(2,2)),
				()->assertEquals(this.gameBoard.getMoverMark(),PLAYER_O,"Expected: " + this.gameBoard.getMoverMark()));
		
		this.gameBoard.makeMove(1, 1);
		assertAll("Check Mover and Board Mark after O move",
				()->assertEquals(this.gameBoard.getMover(), true,"The mover should be" + this.gameBoard.getMover()),
				()->assertEquals(this.gameBoard.getBoardMark(1, 1),PLAYER_O ,"Expected: " + this.gameBoard.getBoardMark(1,1)),
				()->assertEquals(this.gameBoard.getMoverMark(), PLAYER_X,"Expected: " + this.gameBoard.getMoverMark()));
	}
	
	
	
	@Test
	@DisplayName("Test max moves,inPlay(),noPlay()")
	void testMaxMoves() {
		assertEquals(this.gameBoard.getMoves(), 0);
		//make some moves
		//X starts
		this.gameBoard.makeMove(0,0);//x
		this.gameBoard.makeMove(0,1);//o
		this.gameBoard.makeMove(0,2);//x
		this.gameBoard.makeMove(1,1);//o
		this.gameBoard.makeMove(1,0);//x
		this.gameBoard.makeMove(1,2);//o
		
		
		assertAll("Moves equals to 6",
				()->assertEquals(this.gameBoard.getMoves(), 6,"Total Moves should be:" + this.gameBoard.getMoves()),
				()->assertEquals(this.model.inPlay(), true,"The Game should be On"),
				()->assertEquals(this.model.noPlay(), false,"The Game is still On"),
				()->assertEquals(this.gameBoard.getEnd(),false,"The game is On"));
		
		this.gameBoard.makeMove(2, 1);//x
		this.gameBoard.makeMove(2, 0);//o
		this.gameBoard.makeMove(2, 2);//x
		//The game is Tie
		
		assertAll("Moves equals to 9,The game is Over",
				()->assertEquals(this.model.inPlay(), false,"The Game is Over"),
				()->assertEquals(this.model.noPlay(), true,"The Game is Over"),
				()->assertEquals(this.gameBoard.getMoves(), 9,"Total Moves should be:" + this.gameBoard.getMoves()));
		
		assertThrows(IllegalArgumentException.class,()->this.gameBoard.makeMove(0, 1));
		//Moves should remain 9!
		assertEquals(this.gameBoard.getMoves(),9,"Max Moves are 9!");
		
	}
	
	@Test
	@DisplayName("Check The winning triplets horizontally")
	void testHorizontalWinningTriplet() {
		//check also the winner,end
		assertEquals(this.gameBoard.getMoves(), 0);
		
		//make move
		this.gameBoard.makeMove(0, 0);//x
		this.gameBoard.makeMove(1, 0);//o
		this.gameBoard.makeMove(0, 1);//x
		this.gameBoard.makeMove(1, 1);//o
		this.gameBoard.makeMove(0, 2);//x
		//horizontal triplet for the X in the first row
		
		assertAll("Moves equals to 5,and The Game is Over",
				()->assertEquals(this.gameBoard.getMoves(), 5,"Total Moves should be:" + this.gameBoard.getMoves()),
				()->assertEquals(this.model.inPlay(), false,"The Game should be Over.Triplet is made"),
				()->assertEquals(this.model.noPlay(), true,"The Game is Over"),
				()->assertEquals(this.gameBoard.getEnd(),true,"Triplet is Made"),
				()->assertEquals(this.gameBoard.getWinner(),PLAYER_X),
				()->assertEquals(this.gameBoard.getCombination(),"row0"));
		
		//With the same way we can check the other two horizontal triplets
		
		//We should check that board is final, and user cannot make other move
		
		this.model.placeMove(2, 2);
		
		assertEquals(this.gameBoard.getBoardMark(2, 2), NO_PLAYER);
		assertEquals(this.gameBoard.getMoves(), 5);
	}
	
	@Test
	@DisplayName("Check the Winning triplet Vertically")
	void testVerticalWinningTriplet() {
		assertEquals(this.gameBoard.getMoves(), 0);
		
		//make move
		this.gameBoard.makeMove(0, 0);//x
		this.gameBoard.makeMove(0, 1);//o
		this.gameBoard.makeMove(1, 0);//x
		this.gameBoard.makeMove(1, 1);//o
		this.gameBoard.makeMove(2, 2);//x
		this.gameBoard.makeMove(2, 1);//o
		
		//Vertical triplet for the O player, in the second Column
		
		assertAll("Moves equals to 6,and The Game is Over",
				()->assertEquals(this.gameBoard.getMoves(), 6,"Total Moves should be:" + this.gameBoard.getMoves()),
				()->assertEquals(this.model.inPlay(), false,"The Game should be Over.Triplet is made"),
				()->assertEquals(this.model.noPlay(), true,"The Game is Over"),
				()->assertEquals(this.gameBoard.getEnd(),true,"Triplet is Made"),
				()->assertEquals(this.gameBoard.getWinner(),PLAYER_O),
				()->assertEquals(this.gameBoard.getCombination(),"col1"));
		
		//With the same way we can check the other two vertical triplets
		
		//We should check that board is final, and user cannot make other move
		
		this.model.placeMove(0, 2);
		
		assertAll("The Game is Over!",
				()->assertEquals(this.gameBoard.getBoardMark(0, 2),NO_PLAYER),
				()->assertEquals(this.gameBoard.getMoves(), 6));
	}
	
	
	@Test
	@DisplayName("Check the diagonal winning triplet")
	void testDiagonalTriplet() {
		assertEquals(this.gameBoard.getMoves(), 0);
		
		//make Move
		this.gameBoard.makeMove(0, 0);//x
		this.gameBoard.makeMove(0, 1);//o
		this.gameBoard.makeMove(1, 1);//x
		this.gameBoard.makeMove(2, 1);//o
		this.gameBoard.makeMove(2, 2);//x
		
		assertAll("Move equals to 5,and the game is over",
				()->assertEquals(this.gameBoard.getMoves(), 5,"Total moves should be 5"),
				()->assertEquals(this.model.inPlay(),false,"The game should be over"),
				()->assertEquals(this.model.noPlay(),true,"The game should be over"),
				()->assertEquals(this.gameBoard.getEnd(), true,"The game should be over"),
				()->assertEquals(this.gameBoard.getWinner(), PLAYER_X,"X player is the winner"),
				()->assertEquals(this.gameBoard.getCombination(), "diag","Triplet happened at the diagonal"));
		
		this.model.placeMove(0, 2);
		
		assertAll("The Game is Over!",
				()->assertEquals(this.gameBoard.getBoardMark(0, 2),NO_PLAYER,"This move is not valid,the board is already final"),
				()->assertEquals(this.gameBoard.getMoves(), 5,"The total moves should not change"));
		
		
	}
	
	
	@Test
	@DisplayName("Check the anti-diagonal winning triplet")
	void testAntiDiagonalTriplet() {
		assertEquals(this.gameBoard.getMoves(), 0);
		
		this.gameBoard.makeMove(0, 1);//X
		this.gameBoard.makeMove(0, 2);//O
		this.gameBoard.makeMove(1, 0);//X
		this.gameBoard.makeMove(1, 1);//O
		this.gameBoard.makeMove(2, 2);//X
		this.gameBoard.makeMove(2, 0);//O
		
		assertAll("Move equals to 6,and the game is over",
				()->assertEquals(this.gameBoard.getMoves(), 6,"Total moves should be 6"),
				()->assertEquals(this.model.inPlay(),false,"The game should be over"),
				()->assertEquals(this.model.noPlay(),true,"The game should be over"),
				()->assertEquals(this.gameBoard.getEnd(), true,"The game should be over"),
				()->assertEquals(this.gameBoard.getWinner(), PLAYER_O,"O player is the winner"),
				()->assertEquals(this.gameBoard.getCombination(), "antidiag","Triplet happened at the anti-diagonal"));
		
		this.model.placeMove(2, 1);
		
		assertAll("The Game is Over!",
				()->assertEquals(this.gameBoard.getBoardMark(2, 1),NO_PLAYER,"This move is not valid,the board is already final"),
				()->assertEquals(this.gameBoard.getMoves(), 6,"The total moves should not change"));
		
	}
	
	@Test
	@DisplayName("Check if retunrs the available cells")
	void testReturnAvailableCells() {
		MyList<Integer> availableCells;
		
		
		this.gameBoard.makeMove(0,0);//X
		this.gameBoard.makeMove(1,0);//O
		this.gameBoard.makeMove(0,2);//X
		this.gameBoard.makeMove(0,1);//O
		
		assertAll("Check if they placed correctly",
				()->assertEquals(this.gameBoard.getMoves(), 4),
				()->assertEquals(this.gameBoard.getBoardMark(0,0),PLAYER_X),
				()->assertEquals(this.gameBoard.getBoardMark(1,0),PLAYER_O),
				()->assertEquals(this.gameBoard.getBoardMark(0,2),PLAYER_X),
				()->assertEquals(this.gameBoard.getBoardMark(0,1),PLAYER_O));
		
		availableCells=this.gameBoard.getAvaillableCells();
		
		assertAll("Check the available cells",
				()->assertEquals(availableCells.isEmpty(), false),
				()->assertEquals(availableCells.getSize(),5,"The available cells should be 5"),
				()->assertEquals(availableCells.get(0), 4,"The first cell should be the fourth one"),
				()->assertEquals(availableCells.get(1), 5,"The second cell should be the fifth one"),
				()->assertEquals(availableCells.get(2), 6,"The third cell should be the sixth one"),
				()->assertEquals(availableCells.get(3), 7,"The fourth cell should be the seventh one"),
				()->assertEquals(availableCells.get(4), 8,"The fifth cell should be the eighth one"),
				()->assertEquals(availableCells.capacity(),8,"The capacity should be doubled"));
		
	}
	
	@Test
	@DisplayName("Check if availableCell list is empty")
	void testAvailableCellListIsEmpty() {
		MyList<Integer> availableCells;
		
		this.gameBoard.makeMove(0,0);//x
		this.gameBoard.makeMove(0,1);//o
		this.gameBoard.makeMove(0,2);//x
		this.gameBoard.makeMove(1,1);//o
		this.gameBoard.makeMove(1,0);//x
		this.gameBoard.makeMove(1,2);//o
		this.gameBoard.makeMove(2,1);//x
		this.gameBoard.makeMove(2,0);//o
		this.gameBoard.makeMove(2,2);//x
		
		availableCells =this.gameBoard.getAvaillableCells();
		
		assertAll("Check empty list",
				()->assertEquals(availableCells.isEmpty(), true,"There are no available cells"),
				()->assertEquals(availableCells.getSize(),0,"The list should be empty"),
				()->assertEquals(availableCells.capacity(),4,"The initial capacity should not change"));
			
	}
	
	@Test
	@DisplayName("Check if random move works as expected")
	void testRandomMove() {
		GameBoard newGameBoard=new GameBoard(0);
		
		this.gameBoard.makeMove(0,0);//X
		this.gameBoard.makeMove(2,0);//O
		this.gameBoard.makeMove(1,1);//X
		
		assertEquals(this.gameBoard.getMoves(), 3);
		
		move=this.gameBoard.makeRandomMove();
		
		assertAll("Check if the move is placed in an available cell",
				()->assertEquals(move.length, 2),
				()->assertTrue(this.gameBoard.getBoardMark(move[0],move[1])==PLAYER_O),
				()->assertEquals(this.gameBoard.getMoves(),4));				
		
		
		/*
		 * empty the gameBoard and fill it again with moves
		 * to test that the cell that random player chooses doesnt have a mark on
		 */
		this.gameBoard=newGameBoard;
		
		this.gameBoard.makeMove(0,0);//x
		this.gameBoard.makeMove(0,1);//o
		this.gameBoard.makeMove(0,2);//x
		this.gameBoard.makeMove(1,1);//o
		this.gameBoard.makeMove(1,0);//x
		this.gameBoard.makeMove(1,2);//o
		this.gameBoard.makeMove(2,1);//x
		this.gameBoard.makeMove(2,0);//o
		
		assertTrue(this.gameBoard.getBoardMark(2,2)==NO_PLAYER);
		
		move = this.gameBoard.makeRandomMove();
		
		assertTrue(this.gameBoard.getBoardMark(2,2)==PLAYER_X);
	}	
}