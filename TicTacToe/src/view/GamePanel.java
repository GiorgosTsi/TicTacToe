package view;

import javax.swing.JPanel;

import controller.GameController;
import model.GameModel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel{
	/*A parent class for all the panels in our frame*/
	
	protected GameController gc;
	
	public GamePanel(GameController gc) {
		this.gc = gc;
	}
	
	public GameModel getModel() {
		return this.gc.getModel();
	}

}
