package model;

public class GodPlayer extends Player{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GodPlayer(String name) {
		super();
		this.setName(name);
	}
	
	//copy constructor
	
	public GodPlayer(Player copyPlayer) {
		super(copyPlayer);
	}
	
	
	/**
	 * This is the method which will be called
	 * by the god player,to place the suitable
	 * move in the game board.This method works recursively
	 * and can find the best score in this position that can be obtained
	 * with an optimal move.
	 * @param the current state of the board of this game
	 * @param bool if the invoking player is maximizing or minimizing.
	 * @return the best static score, that can be obtained in this position.(-1 0 1) 
	 *  */
	public int minimax(GameBoard board,boolean maximazing) {
		
		
		MyList<Integer> availableCells ;
		int minEval ;
		int maxEval ;
		int evaluation;
		GameBoard gameBoard = new GameBoard(board);//a copy of the input parameter board
		availableCells = gameBoard.getAvaillableCells();
		
		/*Check if any of the player won*/
		if(gameBoard.playerWon(GameBoard.PLAYER_X))
			return GameBoard.PLAYER_X;
		
		if(gameBoard.playerWon(GameBoard.PLAYER_O))
			return GameBoard.PLAYER_O;
		
		
		/*Check if the game is over,because there are no other moves*/
		if(availableCells.isEmpty())
			return 0;
		
		if(maximazing) {//if the player who is maximizing moves
			//traverse the array of available cells and search all the children
			maxEval = Integer.MIN_VALUE;
			for(int i=0; i<availableCells.getSize(); i++) {
				/*
				 * place the move for the next available cell and search recursively
				 * all the children of this child board with the same way.
				 *  */
				gameBoard.makeMove(availableCells.get(i)/3, availableCells.get(i)%3);
				evaluation = minimax(gameBoard, false);
				//after all the checks, compare this evaluation with the previous one
				//but first set this move equals to no player again.
				gameBoard.undoMove(availableCells.get(i)/3, availableCells.get(i)%3);
				//gameBoard.getGameBoard()[availableCells.get(i)/3][availableCells.get(i)%3] = GameBoard.NO_PLAYER;
				maxEval = Math.max(evaluation, maxEval);
				//minEval = Math.min(evaluation, minEval);
			}
			return maxEval;
			//return minEval;
		}
		else {//if the player who is moving is the minimizing
			//availableCells = gameBoard.getAvaillableCells();
			minEval = Integer.MAX_VALUE;
			for(int i=0; i<availableCells.getSize(); i++) {
				/*
				 * place the move for the next available cell and search recursively
				 * all the children of this child board with the same way.
				 *  */
				gameBoard.makeMove(availableCells.get(i)/3, availableCells.get(i)%3);
				evaluation = minimax(gameBoard,true);
				//after all the checks, compare this evaluation with the previous one
				//but first set this move equals to no player again.
				gameBoard.undoMove(availableCells.get(i)/3, availableCells.get(i)%3);
				//gameBoard.getGameBoard()[availableCells.get(i)/3][availableCells.get(i)%3] = GameBoard.NO_PLAYER;
				//maxEval = Math.max(evaluation, maxEval);
				minEval = Math.min(evaluation, minEval);
			}
			return minEval;
			//return maxEval;
		}
	}
	
	
}