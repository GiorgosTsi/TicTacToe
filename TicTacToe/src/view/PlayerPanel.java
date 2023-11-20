package view;

import java.awt.BorderLayout;  
import java.awt.Color;  
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import controller.GameController;
import model.Player;

@SuppressWarnings("serial")
public class PlayerPanel extends GamePanel{
	
	private JButton startGameButton;
	private JButton selectPlayerBtn;
	private int pos;
	private Player currentPlayer;
	private JTextField plName;
	private JLabel plMark;
	private JTextArea plStats;
	
	public PlayerPanel(GameController gc, int pos) {
		super(gc);
		
		this.pos=pos;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setPreferredSize(new Dimension(MainWindow.PLAYER_WIDTH, MainWindow.HEIGHT-MainWindow.TOP_HEIGHT));
		this.setBorder(new LineBorder(Color.green,0,true));
		this.setAlignmentX(CENTER_ALIGNMENT);
		this.setBackground(Color.WHITE);
		
		/*Button use to add new players inside the game*/
		this.selectPlayerBtn = new JButton("Choose Player");
		this.selectPlayerBtn.setPreferredSize(new Dimension(150,40));
		this.selectPlayerBtn.setAlignmentX(CENTER_ALIGNMENT);
		this.selectPlayerBtn.addActionListener((e)->{changePlayer();});
		this.selectPlayerBtn.setFocusable(false);
		this.selectPlayerBtn.setBackground(Color.lightGray);
		this.selectPlayerBtn.setBorder(new LineBorder(Color.BLACK,1));
		
		/*The start game button*/
		this.startGameButton = new JButton("Start Game");
		this.startGameButton.setPreferredSize(new Dimension(150,40));
		this.startGameButton.setAlignmentX(CENTER_ALIGNMENT);
		this.startGameButton.addActionListener((e)->{this.gc.startGame(this.pos);});
		this.startGameButton.setEnabled(false);
		this.startGameButton.setFocusable(false);
		this.startGameButton.setBackground(Color.LIGHT_GRAY);
		this.startGameButton.setBorder(new LineBorder(Color.black,1));
		
		/*Player mark ,it depends in which panel he was chosen*/
		this.plMark = new JLabel(pos==0? "X" : "O");
		this.plMark.setPreferredSize(new Dimension(MainWindow.PLAYER_WIDTH,80));
		this.plMark.setAlignmentX(CENTER_ALIGNMENT);
		this.plMark.setHorizontalAlignment(JTextField.CENTER);
		this.plMark.setEnabled(false);
		Font markf = new Font("SansSerif", Font.BOLD,90);
		this.plMark.setFont(markf);
		
		/*Here will be placed the name of the chosen player*/
		this.plName = new JTextField();
		this.plName.setPreferredSize(new Dimension(MainWindow.PLAYER_WIDTH,40));
		this.plName.setAlignmentX(CENTER_ALIGNMENT);
		this.plName.setHorizontalAlignment(JTextField.CENTER);
		this.plName.setEnabled(false);
		this.plName.setBackground(Color.WHITE);
		
		/*The area we will show the statistics for each of the players*/
		this.plStats = new JTextArea(10,100);		
		this.plStats.setPreferredSize(new Dimension(MainWindow.PLAYER_WIDTH,400));
		this.plStats.setAlignmentX(CENTER_ALIGNMENT);
		Font statsf = new Font("SansSerif", Font.BOLD,20);
		this.plStats.setFont(statsf);
		this.plStats.setEnabled(false);		
		this.plStats.setMargin(new Insets(10, 10, 10, 10));
		this.plStats.setText("<Stat of player..>");

		
		
		
		this.add(selectPlayerBtn);
		this.add(startGameButton);
		this.add(plMark);
		this.add(plName);
		this.add(plStats);
		
		
	}

	public void changePlayer() {
		//Get the list of all players
		String[] allPlayers = gc.getModel().getPlayerCatalogue().getPlayerNames();
		Arrays.sort(allPlayers);
		Player opponent;

		//Show Player Selection Dialog
		String selPlayer = (String) JOptionPane.showInputDialog(this, 
				"Choose a Player...",
				"Player selection",
				JOptionPane.PLAIN_MESSAGE,
				null,
				allPlayers,
				null
				);
		
		opponent = gc.getModel().getGamePlayers()[pos==0?1:0];
		//Set the selected player		
		if(selPlayer != null) {
			if (opponent != null && selPlayer.equals(opponent.getName())) {
				JOptionPane.showMessageDialog(gc.getView(), 						
						"Player already selected",
						"Ooops...",
						JOptionPane.ERROR_MESSAGE);
				return;
			}			
			this.currentPlayer=gc.getModel().getPlayerCatalogue().findPlayerByName(selPlayer);
			gc.selectPlayer(this.currentPlayer,pos);
			this.plName.setText(currentPlayer.getName());
			this.plStats.setText(this.gc.getModel().getPlayerStats(selPlayer));
			//gc.selectPlayer(this.currentPlayer,pos);
			this.repaint();
		}
	}	

	
	/**************************************************GETTERS & SETTERS**************************************************/
	
	public int getPos() {
		return pos;
	}

	public JButton getStartGameButton() {
		return startGameButton;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public JTextField getPlName() {
		return plName;
	}
	
	public JTextArea getPlStats(){
		return this.plStats;
	}

	public void setPlName(JTextField plName) {
		this.plName = plName;
	}

	public JButton getSelectPlayerBtn() {
		return selectPlayerBtn;
	}
		
}
