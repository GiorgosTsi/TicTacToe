package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Timer;

import controller.GameController;


@SuppressWarnings("serial")
public class GameBoard extends GamePanel {
	BoardCell[] cells;//array of nine cells
	Graphics2D graphics2d;
	
	public GameBoard(GameController gc) {
		super(gc);

		this.setLayout(null);
		//set the size of this board
		this.setSize(new Dimension(MainWindow.WIDTH-2*MainWindow.PLAYER_WIDTH, MainWindow.HEIGHT-MainWindow.TOP_HEIGHT));
		this.setBackground(Color.WHITE);
		this.cells = new BoardCell[9];
		//Create the cells
		for(int row=0 ; row<3 ; row++) {
			for(int col=0 ; col<3 ; col++) {
				BoardCell cell = new BoardCell(gc, row, col,this);
				cell.setBounds(boardZero().x + col*cellSize() + BoardCell.CELL_PADDING,
							   boardZero().y + row*cellSize() + BoardCell.CELL_PADDING,
							   cellSize() - 2*BoardCell.CELL_PADDING,
							   cellSize() - 2*BoardCell.CELL_PADDING);
				//add the cell to the panel
				this.add(cell);
				this.cells[row*3 + col] = cell;
			}
		}
		
	}
	
	/*Computes the size of each cell of this board */
	public int cellSize() {
		int minSide = Integer.min(this.getWidth(), this.getHeight());
		return minSide/5;
	}
	
	private int boardSize() {
		//both sides of this board are the same.
		// it is like a 3x3 matrix
		return this.cellSize() * 3;
	}
	
	/**
	 * This method will be used to place X or O
	 * when Mr.Bean,Hal or both are playing
	 * Because they do not click the boardCell
	 * we need another way to show their move
	 * @param row
	 * @param col
	 */
	public void placeMove(int row , int col,int mark) {
			
		for(BoardCell boardCell : this.cells) {
			if(boardCell.getRow()==row && boardCell.getCol()==col) {
				if(mark==1) {
					boardCell.paintX(this.graphics2d, boardCell.getSize().width - 2 * 10);
				}
				else if(mark==-1) {
					boardCell.paintO(this.graphics2d, boardCell.getSize().width - 2 * 10);
				}
			}
		}
		
		repaint();
	}	
	
	/**
	 * Returns the upper left corner of the board */
	private Point boardZero() {
		
		int x= (this.getWidth() - boardSize())/2;
		int y= (this.getHeight() - boardSize())/2;
		return new Point(x,y);
	}
	
	private void drawGrid(Graphics2D g2d) {
		//draws the vertical and the horizontal lines of the game
		//set the Stroke
		g2d.setStroke(new BasicStroke(6));
		
		for (int i=1 ; i<3 ; i++) {
			//draw The Vertical Line
			g2d.drawLine(
					boardZero().x +i*cellSize(),boardZero().y, 				// i-based x,  	upperLeft.y 
					boardZero().x+i*cellSize(),boardZero().y+boardSize() 	// i-based x,	boardBottom.y (boardBottom = y + boardSize)
					);
			
			//Draw Horizontal Line
			g2d.drawLine(
					boardZero().x,  boardZero().y+i*cellSize(), 			// upperLeft.x, i-based y
					boardZero().x+boardSize(), boardZero().y+i*cellSize()	// boardRight.x, i-based y (boardRight = x + boardSize)
					);
		}
	}
	
	/**
	 * Draws a red horizontal line in the input row.
	 * @param Graphics2D , row 
	 *  */
	private void drawHorizontalRedLine(Graphics2D g2d,int row) {
		int halfCellSize = this.cellSize()/2;
		g2d.setStroke(new BasicStroke(6));
		//g2d.setColor(Color.red);
		g2d.drawLine(
				boardZero().x,  boardZero().y+(row*cellSize()) + halfCellSize, 			// upperLeft.x, i-based y
				boardZero().x+boardSize(), boardZero().y+(row*cellSize()) + halfCellSize	// boardRight.x, i-based y (boardRight = x + boardSize)
				);		
	}
	
	/**
	 * Draws a red vertical line in the input column.This will be drawn in the winning triple.
	 * @param Graphics2D , column 
	 *  */
	private void drawVerticalRedLine(Graphics2D g2d,int col) {//zero numbering
		int halfCellSize = this.cellSize()/2;
		g2d.setStroke(new BasicStroke(6));
		//g2d.setColor(Color.red);
		g2d.drawLine(
				boardZero().x +(col*cellSize()) + halfCellSize,boardZero().y, 				// i-based x,  	upperLeft.y 
				boardZero().x +(col*cellSize()) + halfCellSize,boardZero().y+boardSize() 	// i-based x,	boardBottom.y (boardBottom = y + boardSize)
				);
	}
	
	/**
	 * Draws a red diagonal line for the win triplet
	 * @param Graphics2D
	 *  */
	private void drawDiagonalRedLine(Graphics2D g2d) {
		g2d.setStroke(new BasicStroke(6));
		//g2d.setColor(Color.red);
		g2d.drawLine(boardZero().x, boardZero().y, boardZero().x + boardSize(), boardZero().y + boardSize());
	}
	
	private void drawAntiDiagonalRedLine(Graphics2D g2d) {
		g2d.setStroke(new BasicStroke(6));
		//g2d.setColor(Color.red);
		g2d.drawLine(boardZero().x + boardSize(),boardZero().y, boardZero().x, boardZero().y + boardSize());
	}
	
	private void drawWinningTriplet(Graphics2D g2d,String combination) {
		int moves = gc.getGameMoves();
		
		/*If the combination is null and moves<9 , the game is not over.Don't do something */
		if(combination == null && moves<9)
			return;
		
		/*If combination==null and moves=9, then the game is a tie.Enable the done button, so user can start again */
		if(combination == null && moves == 9) {
			this.gc.getView().getTopPanel().getDoneBtn().setEnabled(true);
			return;
		}
		
		/*Checks horizontal and vertical lines */
		//enable the done button if it is the end of the game(there is a triplet)
		for(int i=0 ; i<3 ; i++) {
			if(combination.equals("row"+i)) {
				this.gc.getView().getTopPanel().getDoneBtn().setEnabled(true);
				this.drawHorizontalRedLine(g2d, i);
				return;
			}else if(combination.equals("col"+i)) {
				this.gc.getView().getTopPanel().getDoneBtn().setEnabled(true);
				this.drawVerticalRedLine(g2d, i);
				return;
			}
		}
		/*Checks diagonal and anti diagonal lines */
		if(combination.equals("diag")) {
			this.gc.getView().getTopPanel().getDoneBtn().setEnabled(true);
			this.drawDiagonalRedLine(g2d);
			return;
		}else if(combination.equals("antidiag")) {
			this.gc.getView().getTopPanel().getDoneBtn().setEnabled(true);
			this.drawAntiDiagonalRedLine(g2d);
			return;
		}
		
	}
	
	//When X wins prints the message:X WINS!!!
	private void drawXWinner(Graphics2D graphics2d) {
		graphics2d.setStroke(new BasicStroke(5));
		
		Font winnerFont= new Font("Sanserif",Font.ITALIC,50);
		graphics2d.setFont(winnerFont);
		
		String winner=this.gc.getModel().getGamePlayers()[0].getName();
	
		graphics2d.drawString(winner+" WINS!!!",boardZero().x+cellSize()-120,70);
	}
	
	/*When X wins prints the message:X WINS!!!*/
	private void drawOWinner(Graphics2D graphics2d) {
		graphics2d.setStroke(new BasicStroke(5));
		
		Font winnerFont= new Font("Sanserif",Font.ITALIC,50);
		graphics2d.setFont(winnerFont);
		
		String winner=this.gc.getModel().getGamePlayers()[1].getName();
	
		graphics2d.drawString(winner+" WINS!!!",boardZero().x+cellSize()-120,70);
	}
	
	private void drawTie(Graphics2D graphics2d) {
		graphics2d.setStroke(new BasicStroke(5));
		
		Font winnerFont= new Font("Sanserif",Font.ITALIC,50);
		graphics2d.setFont(winnerFont);
	
		graphics2d.drawString("THE GAME IS A TIE",boardZero().x+cellSize()-170,70);
	}
	
	/*let the user know who won with text*/
	private void DrawWinner(Graphics2D g2d,int winner) {
		if(winner==1) {
			this.drawXWinner(g2d);
		}
		else if(winner==-1){
			this.drawOWinner(g2d);
		}
		else if(winner==0 && gc.getModel().noPlay()) {
			this.drawTie(g2d);
		}
	}
	
	
	
	private void drawMover(Graphics2D g2d) {
		String name=null;
		int mover = this.gc.getModel().getMover();
		Font moverFont= new Font("Sanserif",Font.ITALIC,50);
		int pos = mover == 1 ? 0 : 1;//X is at the left player panel==0, and O at the right==1
		g2d.setFont(moverFont);
		g2d.drawString("Move:", boardZero().x, 50);
		
		if(gc.getModel().getGamePlayers()[pos] != null) {//den xreiazetai o elegxos,nomizw.
			name = this.gc.getModel().getGamePlayers()[pos].getName();
			g2d.drawString(name, boardZero().x+150, 50);
		}
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//cast to 2D graphics
		Graphics2D g2d = (Graphics2D)g;
		this.graphics2d=g2d;
		//call the drawGrid method
		this.drawGrid(g2d);
		//this.drawHorizontalRedLine(g2d, 2);
		this.drawWinningTriplet(g2d,gc.getModel().getGameBoard().getCombination());
		//When the game ends prints the winner of the game
		this.DrawWinner(g2d, gc.getModel().getGameBoard().getWinner());
		//Shows which player has move
		if(this.gc.getModel().inPlay()) {
			this.drawMover(g2d);
		}
		
	}
	
}
