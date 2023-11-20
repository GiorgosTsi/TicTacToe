package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.io.IOException;
import java.security.KeyStore.PrivateKeyEntry;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import controller.GameController;

@SuppressWarnings("serial")
public class MainWindow extends JFrame{
	
	/*constants for defining the dimentions for the components*/
	static public final int WIDTH = 1200;
	static public final int HEIGHT = 800;
	static public final int TOP_HEIGHT = 80;
	static public final int PLAYER_WIDTH = 300;
	
	private PlayerPanel leftPanel;
	private PlayerPanel rightPanel;
	private TopPanel topPanel;
	private MainAreaPanel mainPanel;
	private GameController gc;
	
	
	public MainWindow(GameController gc)  {
		super("TUC-TAC-TOE");//set the Text of frame
		
		ImageIcon icon = new ImageIcon("tictactoeimage2.png");
		
		this.gc=gc;
		Container c = this.getContentPane();
		c.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setIconImage(icon.getImage());
		
		topPanel = new TopPanel(this.gc);		
				
		this.add(topPanel,BorderLayout.PAGE_START);
		
		leftPanel = new PlayerPanel(gc,0);
		this.add(leftPanel,BorderLayout.LINE_START);
		
		rightPanel = new PlayerPanel(gc, 1);
		this.add(rightPanel,BorderLayout.LINE_END);

		mainPanel = new MainAreaPanel(this.gc);
		this.add(mainPanel,BorderLayout.CENTER);
		this.pack();			
	}

	
	
	/***********************************************GETTERS & SETTERS**********************************************/
	
	public PlayerPanel getLeftPanel() {
		return leftPanel;
	}

	public void setLeftPanel(PlayerPanel leftPanel) {
		this.leftPanel = leftPanel;
	}

	public PlayerPanel getRightPanel() {
		return rightPanel;
	}

	public void setRightPanel(PlayerPanel rightPanel) {
		this.rightPanel = rightPanel;
	}

	public TopPanel getTopPanel() {
		return topPanel;
	}

	public void setTopPanel(TopPanel topPanel) {
		this.topPanel = topPanel;
	}	
		
	public MainAreaPanel getMainPanel() {
		return mainPanel;
	}
	
	public GameBoard getGameBoard() {
		return this.mainPanel.gameBoard;
	}
	
}