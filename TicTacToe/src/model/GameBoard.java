package model;

import java.io.Serializable; 
import java.util.Random;


/**
 * This class represents the tic tac toe Board
 * 
 */
public final class GameBoard implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Constants for the game
	 */
	public static final int NO_PLAYER = 0;
	public static final int PLAYER_O = -1;
	public static final int PLAYER_X = 1;
	public static final int MAX_MOVES = 9;
	// the board is a 3x3 matrix
	private final int[][] board;
	private int moves;// counts the total moves of the game(Max 9)
	private boolean end;// if it is true then the game is Over
	private boolean mover;
	/**
	 * if true , it's X turn == 1
	 */
	// private GameModel gameModel;
	private String combination;//indicates which triplet is made
	private int winner;//The winner of the game

	
	
	public GameBoard(int pos) {
		this.board = new int[3][3];
		/*If pos=0 then the leftPlayer panel click
		 *start game button , so the game starts with X.Else starts with O */
		this.mover = pos==0? true : false;
		this.end = false;
		this.combination = null;
		this.winner=0;
	}
	
	/** 
	 * Copy Constructor
	 * @param the game Board which we want a copy of
	 * @return a copy of this game board
	 * */
	public GameBoard(GameBoard copyBoard) {
		this.board = new int[3][3];

		for(int i=0 ; i<this.board[1].length ; i++)
			for(int j=0 ; j<this.board.length ; j++)
				this.board[i][j] = copyBoard.getBoardMark(i, j);
		
		this.mover = copyBoard.getMover();
		this.end = copyBoard.getEnd();
		//this.combination = new String(copyBoard.getCombination());
		this.combination = copyBoard.getCombination();
		this.winner = copyBoard.getWinner();
		this.moves = copyBoard.getMoves();
	}

	/**
	 * Checks if this is a move in bounds
	 * 
	 * @param row,column
	 */
	public void checkDimValidity(int row, int col) {
		if (row < 0 || col < 0 || row > 2 || col > 2) {
			throw new IndexOutOfBoundsException(row + "," + col + " is not a valid board cell");
		}
	}

	/**
	 * Checks if this move is taken or not
	 * 
	 * @param row , column
	 */
	public void checkMoveValidity(int row, int col) {
		checkDimValidity(row, col);
		if (this.board[row][col] != NO_PLAYER) {
			throw new IllegalArgumentException("Non playable cell");
		}

	}

	public void makeMove(int row, int col) {
	
		int moverMark = this.mover ? PLAYER_X : PLAYER_O;
		this.checkMoveValidity(row, col);
		this.board[row][col] = moverMark;
		mover = !mover;
		moves++;
		/* Check if this player won */
		this.end = this.playerWon(moverMark);
		  
	}
	
	public void undoMove(int row, int col) {
		//int previousMoverMark = this.mover ? PLAYER_O : PLAYER_X;
		this.board[row][col] = NO_PLAYER;
		mover = !mover;
		moves--;
		this.combination = null;
		this.end = false;
		this.winner = 0;
	}
	
	/**
	 * Makes a random move,chosen from the 
	 * available cells.
	 * @return Returns this move.Index0 for row,index1 for column.
	 */
	public int[] makeRandomMove() {
		Random r = new Random();
		int row,col;
		int moverMark = this.mover ? PLAYER_X : PLAYER_O;
		int[] move = new int[2];
		
		while(true) {
				row = r.nextInt(3);
				col = r.nextInt(3);
				if(this.board[row][col] == NO_PLAYER)
					break;
		}
		
		this.board[row][col] = moverMark;
		move[0] = row; move[1] = col;
		mover = !mover;
		moves++;
		//check if player won
		this.end =this.playerWon(moverMark);
		
		return move;
	}

	public int[][] getGameBoard() {
		return this.board;
	}

	public boolean getMover() {
		return this.mover;
	}

	public void setMover(int moverMark) {
		this.mover = moverMark == GameBoard.PLAYER_X ? true : false;
	}
	public int getMoverMark() {
		return getMover() ? PLAYER_X : PLAYER_O;
	}

	/**
	 * Returns the primitive of player's move in this Point
	 * 
	 * @return int
	 */
	public int getBoardMark(int row, int col) {
		this.checkDimValidity(row, col);
		return this.board[row][col];

	}

	public int getMoves() {
		return this.moves;
	}

	public boolean getEnd() {
		return this.end;
	}

	/**
	 * Returns a list with the available cells
	 * @return List<Integer>
	 */
	public MyList<Integer> getAvaillableCells() {
		MyList<Integer> availableCellsList = new MyList<>(4);
			
		for(int i =0;i<3;i++)
			for(int j=0;j<3;j++) 
				if(this.getBoardMark(i, j)==NO_PLAYER) 
					availableCellsList.addExpandable(3*i+j);
				
		
		return availableCellsList;
	}


	/**
	 * Checks if the input player Mark has complete a triplet
	 * @param int player
	 * @return boolean 
	 *  */
	public boolean playerWon(int player) {
		/* Check diagonally and anti diagonally the matrix */
		if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] == player) {
			this.combination = "diag";
			this.winner=player;
			return true;
		}

		if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] == player) {
			this.combination = "antidiag";
			this.winner=player;
			return true;
		}
		/*Checks horizontal and vertical lines */
		
		
		for (int i = 0; i < 3; i++) {
			//checks horizontally
			if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] == player){
				this.combination = "row"+i;
				this.winner=player;
				return true;
			}
		
			//checks vertically
			if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] == player) {
				this.combination = "col"+i;
				this.winner=player;
				return true;
			}
		}
		return false;
	}

	public String getCombination() {
		return this.combination;
	}
	
	public int getWinner() {
		return this.winner;
	}
}