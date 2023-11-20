package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.border.LineBorder;

import controller.GameController;
import model.RandomPlayer;

@SuppressWarnings("serial")
public class BoardCell extends GamePanel  implements MouseListener{
	
	public static final int CELL_PADDING = 10;
	
	private int row;//rows
	private int col;//columns
	private boolean highlighted;
	private GameBoard gameBoard;
	
	public BoardCell(GameController gc, int row , int col,GameBoard gameBoard) {
		super(gc);
		//set the background color
		this.setBackground(Color.white);
		this.row = row;
		this.col = col;
		this.highlighted = false;
		this.gameBoard = gameBoard;
		//add action listener for mouse events
		this.addMouseListener(this);
		this.setOpaque(false);
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//cast to 2d graphics
		Graphics2D g2d = (Graphics2D)g;
		super.paintComponents(g);
		g2d.setStroke(new BasicStroke(6));
		/*Get the integer of the player who did that move in this cell.
		 *if ==1 X made this move.If == 0 this is an empty cell, else O 
		 *  */
		int player = gc.getModel().getGameBoard().getBoardMark(row, col);
		
		int size = this.getSize().width - 2 * CELL_PADDING;
		
		if(player == 0 && highlighted) {
			this.fillRectangle(g2d,size);
		}
		else if(player == 1) 
			this.paintX(g2d,size);
		else if(player == -1)
			this.paintO(g2d,size);
		
		
	}
		
	private void fillRectangle(Graphics2D g2d,int size) {
		
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(CELL_PADDING, CELL_PADDING, size, size);
	}
	
	public void paintX(Graphics2D g2d,int size) {
		/**
		 * This method will be called
		 * in the paintComponent method to print the X 
		 *  */
		g2d.drawLine(CELL_PADDING, CELL_PADDING, CELL_PADDING + size, CELL_PADDING + size);
		g2d.drawLine(CELL_PADDING + size, CELL_PADDING, CELL_PADDING, CELL_PADDING + size);
	}
		
	
	public void paintO(Graphics2D g2d, int size) {
		/**
		 * This method will be called
		 * in the paint Component method to print the 0
		 *  */
		g2d.drawOval(CELL_PADDING, CELL_PADDING, size, size);
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("Mouse clicked on cell:" + this);
		if (getModel().inPlay()) {
			/*We need to check the inPlay
			 * because if the board is full then a user
			 * can click in this panel, so change this cell.
			 * we do not want this so check if inPlay, which checks if the board is full;
			 *  */
			//if the move is already taken by a player,exception will be thrown
			try {
				getModel().placeMove(row, col);
			} catch (IllegalArgumentException ex) {
				System.err.println(ex.getMessage());
			}
			/*So if the board is not full and this move
			 *is a valid move, then repaint and put the X or the O 
			 *  */
			repaint();
			if(this.gc.existRandomPlayer()) {
				
				this.gc.randomGame();
				repaint();
			}
			
			if(this.gc.existGodPlayer()) {
				this.gc.godGame();
				repaint();
			}
			/*repaint the gameBoard panel so it shows the next move and other info*/
			gameBoard.repaint();
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		System.out.println("Mouse entered the cell: " + this);
		this.highlight();		
	}

	@Override
	public void mouseExited(MouseEvent e) {

		System.out.println("Mouse exited on cell " + this);
		this.unHighlight();

		this.highlighted = false;
	}
	
	@Override
	public String toString() {
		return "("+ this.row +", " + this.col +")";
	}

	private void highlight() {
		if (!highlighted && getModel().inPlay()) {
			highlighted = true;
			repaint();
		}
	}
	
	private void unHighlight() {
		if (highlighted && getModel().inPlay()) {
			highlighted = false;
			repaint();
		}
	}
	
	public int getRow() {
		return this.row;
	}
	
	public int getCol() {
		return this.col;
	}
}
