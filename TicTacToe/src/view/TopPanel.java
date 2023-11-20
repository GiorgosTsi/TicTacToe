package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;


import controller.GameController;
import model.GodPlayer;
import model.HumanPlayer;
import model.RandomPlayer;

@SuppressWarnings("serial")
public class TopPanel extends GamePanel {
	
	JButton quitBtn;
	JButton doneBtn;
	JButton addPlayerBtn;
	
	public TopPanel(GameController gc) {
		super(gc);
		this.gc=gc;
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.setPreferredSize(new Dimension(MainWindow.WIDTH,MainWindow.TOP_HEIGHT));
		this.setBorder(new LineBorder(Color.GRAY,1,true));
		this.quitBtn = new JButton("Quit App");	
		this.quitBtn.setPreferredSize(new Dimension(100, 40));
		this.quitBtn.setFocusable(false);
		this.quitBtn.setBackground(Color.RED);
		this.quitBtn.setBorder(new LineBorder(Color.black,1));
		this.quitBtn.addActionListener((e)->{this.gc.quit();});		
		
		
		this.doneBtn = new JButton("Done");		
		this.doneBtn.setPreferredSize(new Dimension(100, 40));		
		this.doneBtn.setEnabled(false);
		this.doneBtn.setFocusable(false);
		this.doneBtn.setBackground(Color.LIGHT_GRAY);
		this.doneBtn.setBorder(new LineBorder(Color.black,1));
		this.doneBtn.addActionListener((e)->{this.gc.donePressed();});
		
		this.addPlayerBtn=new JButton("Add Player");
		this.addPlayerBtn.setPreferredSize(new Dimension(100, 40));	
		this.addPlayerBtn.setFocusable(false);
		this.addPlayerBtn.setBackground(Color.LIGHT_GRAY);
		this.addPlayerBtn.setBorder(new LineBorder(Color.black,1));
		this.addPlayerBtn.setEnabled(true);
		this.addPlayerBtn.addActionListener((e) -> {this.addPlayer();});
		
		add(addPlayerBtn);
		add(doneBtn);
		add(quitBtn);					
	}
	
	
	public void addPlayer() {
		//String name = JOptionPane.showInputDialog("Enter the name of the player(length must be 1-20 characters");
		String name = JOptionPane.showInputDialog("Enter the name of the player(length must be 1-20 characters");
		if (name.equals("") || name == null) {
		    JOptionPane.showMessageDialog(this, "You do not enter a Name!Try again!");
		    return;
		}
		name = name.strip();//remove blanks and white spaces
		if(name.length()>20) {
			JOptionPane.showMessageDialog(gc.getView(), 						
			"The length of the name must be smaller than 20 characters",
			"Ooops...",
			JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(name.equals("Mr.Bean")) {
			RandomPlayer randomPlayer = new RandomPlayer("Mr.Bean",gc.getModel());
			this.gc.addPlayer(randomPlayer);
		}
		
		if(name.equals("Hall")) {
			GodPlayer godPlayer = new GodPlayer(name);
			this.gc.addPlayer(godPlayer);
			return;
		}
			
		HumanPlayer humanPlayer = new HumanPlayer(name);
		this.gc.addPlayer(humanPlayer);
			
	}

	public JButton getQuitBtn() {
		return this.quitBtn;
	}


	public JButton getDoneBtn() {
		return this.doneBtn;
	}
	
	public JButton getAddPlayerBtn() {
		return this.addPlayerBtn;
	}

	
	
}
