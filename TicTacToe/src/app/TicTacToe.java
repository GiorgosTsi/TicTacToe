package app;


import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import controller.GameController;    
import model.GodPlayer;
import model.HumanPlayer;
import model.Player;
import model.MyList;
import model.PlayersCatalogue;
import model.RandomPlayer;

public class TicTacToe {
	
	
	public static void main(String[] args) {	
		
				
		GameController gc = new GameController();
		gc.start();
		
		/*PlayerList pList = new PlayerList();
		Player[] arr = new Player[11];
		for(Player player : arr) {
			player = new GodPlayer();
			pList.add(player);
		}*/
		
	}
	
}
