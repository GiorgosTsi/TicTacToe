package view;

import java.awt.Color;  
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.text.DecimalFormat;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import controller.GameController;
import model.Player;

@SuppressWarnings("serial")
public class HallOfFame extends GamePanel{
	
	private JLabel bannerLabel;
	private JTextArea bestPlayers;

	
	public HallOfFame(GameController gc) {
		super(gc);
		this.gc=gc;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setAlignmentX(CENTER_ALIGNMENT);
		this.setBackground(Color.ORANGE);
				
		this.bannerLabel = new JLabel(); 
		this.bestPlayers = new JTextArea(100,100);
		this.bestPlayers.setPreferredSize(new Dimension(WIDTH - 2 * MainWindow.PLAYER_WIDTH, MainWindow.HEIGHT - MainWindow.TOP_HEIGHT));
		//this.bestPlayers.setAlignmentX(CENTER_ALIGNMENT);
		Font bestPlayersFont = new Font("Consolas",Font.ITALIC,30);
		this.bestPlayers.setFont(bestPlayersFont);
		this.bestPlayers.setMargin(new Insets(5, 0, 0,0));
		//this.bestPlayers.setText("HALL OF FAME");
		this.bannerLabel.setPreferredSize(new Dimension(WIDTH - 2 * MainWindow.PLAYER_WIDTH, MainWindow.HEIGHT - MainWindow.TOP_HEIGHT-100));
		this.bannerLabel.setFont(new Font("MV Boli",Font.PLAIN,25));
		this.bannerLabel.setText("Hall Of Fame");
		this.bannerLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		this.bestPlayers.setEditable(false);
		this.bestPlayers.setOpaque(false);
		
		
		this.add(bannerLabel);
		this.add(bestPlayers);
	}	
	
	
	/**
	 * We put players down from each other
	 * They are reverse sorted from the list
	 * using addStatic method
	 * @return The string created 
	 */
	private  String getHallOfFame() {
		StringBuilder stringBuilder = new StringBuilder("");
		Player[] topPlayers  = this.gc.getHallOfFame();
		DecimalFormat df = new DecimalFormat("##.##");//format to get only 2 decimal digits
		int i=1;
		for(Player p: topPlayers) {
			if(p.getName().length()<5) {
				stringBuilder.append(i+". "+p.getName()+"\t\t\t     "+ df.format(p.getScore()) + "\n\n");
			}
			else
				stringBuilder.append(i+". "+p.getName()+"\t\t     "+ df.format(p.getScore()) + "\n\n");
			i++;
			
		}
		
		return stringBuilder.toString();//return the stringBuilder as string
	}
	
	/*
	 * Sets the the best player text area to 
	 * the best players taken fron the 
	 * getHallOfFame method
	 */
	public void setHallOfFame() {
		this.bestPlayers.setText(this.getHallOfFame());
	}
	
}