package controller;

import java.awt.event.WindowAdapter; 
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Serializable;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Timer;

import model.GameBoard;
import model.GameModel;
import model.GodPlayer;
import model.MyList;
import model.Player;
import model.RandomPlayer;
import view.MainAreaPanel;
import view.MainWindow;

/**
 * @author Tsikritzakis Georgios_Marios
 * Arithmos mhtrwoy:2020030055
 *
 *@author Messaritakis Georgios
 *Arithmos mhtrwoy:2020030116
 *
 */

public class GameController extends WindowAdapter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MainWindow view;
	private GameModel model;
	private Timer timer1;
	private Timer timer2;
	
	//Constructor//
	public GameController() {
		//create timers
		this.timer1 = new Timer(1000,(e)->{
			if(this.getModel().inPlay() && this.existRandomPlayer())
				this.randomGame();
				this.timer2.restart();
		});
		
		this.timer2 = new Timer(1000,(e)->{
			if(this.getModel().inPlay() && this.existGodPlayer())
				this.godGame();
				this.timer1.restart();
		});
		
		//set repeats false
		this.timer1.setRepeats(false);
		this.timer2.setRepeats(false);
	}
	
	@Override
	public void windowClosing(WindowEvent event) {
		quit();
	}
	
	
	public void start() {
		this.view= new MainWindow(this);
		this.model = new GameModel(this);
		this.view.addWindowListener(this);
		this.view.setVisible(true);
		/*load players when controller is created*/
		this.model.getPlayerCatalogue().loadPlayers();
		/*every time the app starts we want to show the hall of fame*/
		this.view.getMainPanel().getHallOfFame().setHallOfFame();
		
	}
	
	public void quit() {
		//when quit button is pressed we must store the players in the file
		this.model.getPlayerCatalogue().storePlayers();
		System.out.println("bye bye...");		
		System.exit(0);
	}
	
	public void donePressed() {
		this.getView().getMainPanel().showCard("HALL_OF_FAME");
		//enable the buttons so user can start a new game
		this.getView().getLeftPanel().getStartGameButton().setEnabled(true);
		this.getView().getRightPanel().getStartGameButton().setEnabled(true);
		this.getView().getTopPanel().getAddPlayerBtn().setEnabled(true);
		this.getView().getLeftPanel().getSelectPlayerBtn().setEnabled(true);
		this.getView().getRightPanel().getSelectPlayerBtn().setEnabled(true);
		this.getView().getTopPanel().getQuitBtn().setEnabled(true);	
		//at the end disable again the done button
		this.getView().getTopPanel().getDoneBtn().setEnabled(false);
		//if user do not press done button, the game will not be saved!
		//update winner should be done just when the game is over
		this.getModel().updateWinner();
		Player[] gamePlayers = this.getModel().getGamePlayers();
		this.getView().getLeftPanel().getPlStats().setText(this.getModel().getPlayerStats(gamePlayers[0].getName()));
		this.getView().getRightPanel().getPlStats().setText(this.getModel().getPlayerStats(gamePlayers[1].getName()));
		/*when done is pressed and the game shows the hof we want to update it to*/
		this.view.getMainPanel().getHallOfFame().setHallOfFame();
		
		
	}
	
	
	public void selectPlayer(Player p, int pos) {
		this.model.selectPlayer(p, pos);
		System.out.println("Player " + pos + " set to " + p);
		this.view.getLeftPanel().getStartGameButton().setEnabled(model.ready());
		this.view.getRightPanel().getStartGameButton().setEnabled(model.ready());
	}
	
	public void startGame(int pos) {

		this.model.setGameBoard(new GameBoard(pos));
		this.view.getTopPanel().getAddPlayerBtn().setEnabled(false);
		this.view.getTopPanel().getQuitBtn().setEnabled(false);
		this.view.getMainPanel().showCard(MainAreaPanel.BOARD);
		this.view.getLeftPanel().getSelectPlayerBtn().setEnabled(model.noPlay());
		this.view.getRightPanel().getSelectPlayerBtn().setEnabled(model.noPlay());
		this.view.getLeftPanel().getStartGameButton().setEnabled(false);
		this.view.getRightPanel().getStartGameButton().setEnabled(false);
		//increase the game counter
		this.model.getGamePlayers()[0].addGame();
		this.model.getGamePlayers()[1].addGame();
		
		/*If there is a game for the PC only:*/
		if(this.existPCgame()) {
			System.out.println("GodPlayer vs RandomPlayer");
			this.pcGame();
			return;
		}
		//if there is a random player within the selected players
		//start a random game
		if(this.existRandomPlayer()) {
			System.out.println("Mr.Bean game");
			randomGame();//call this method , so the first move take place, if it's random player's turn in the beginning.
			return;
		}
		
		if(this.existGodPlayer()) {
			System.out.println("Hall game!");
			godGame();
			return;
		}
	}
	
	public void randomGame() {
		//pos 0 == X mark!
		//int myMark = pos==0? GameBoard.PLAYER_X : GameBoard.PLAYER_O ;
		int randomPlayerMark=this.getModel().getGamePlayers()[0] instanceof RandomPlayer? GameBoard.PLAYER_X : GameBoard.PLAYER_O;
		int[] move;
		
		if(this.getModel().getMover() ==randomPlayerMark && this.getModel().inPlay()) {
			
			move = this.getModel().getGameBoard().makeRandomMove();
			
			try {
				this.getView().getGameBoard().placeMove(move[0], move[1], randomPlayerMark);
			} catch (NullPointerException e) {}
		}
	}
	
	public void godGame() {
		
		int godPlayerMark = this.getModel().getGamePlayers()[0] instanceof GodPlayer ? GameBoard.PLAYER_X : GameBoard.PLAYER_O;
		int move[] ;
		
		if(this.model.getMover() == godPlayerMark && this.model.inPlay()) {
			
			move = this.bestMove(godPlayerMark);
			this.model.placeMove(move[0], move[1]);
			//place this move also in view's board.
			try {
				this.getView().getGameBoard().placeMove(move[0], move[1], godPlayerMark);
			} catch (NullPointerException e) {}
		}
	}
		
	/**
	 * Finds the optimal move for the AI 
	 * player, using the mini_max algorithm.
	 * Does not place the move,just finds that and returns it.
	 * @param the god's player mark(X or O)
	 * @return an integer array with the best move.0 index
	 * indicates the row,and index 1 indicates the column.
	 *  */
	public int[] bestMove(int godPlayerMark) {
		
		Player godPlayer = godPlayerMark == GameBoard.PLAYER_X ? this.getModel().getGamePlayers()[0] : this.getModel().getGamePlayers()[1];
		int[] move = new int[2];
		int score;
		/**
		 * If god's player mark == 1 == X his best possible score == 1
		 * But if he is the O player his best possible score == -1.
		 * So we must reverse the infinity sign.
		 *  */
		int bestScore =godPlayerMark == GameBoard.PLAYER_X? Integer.MIN_VALUE : Integer.MAX_VALUE;
		MyList<Integer> availableCells = this.getModel().getGameBoard().getAvaillableCells();
		GodPlayer god = (GodPlayer)godPlayer;//casting to god player
		
		for(int i=0; i<availableCells.getSize(); i++) {
			/*
			 * Place a move in every available cell, then check the score that
			 * mini_max algorithm returns,and then place a move to the cell,which
			 * gives the best result.
			 *  */
			this.model.placeMove(availableCells.get(i)/3, availableCells.get(i)%3);
			
			if(godPlayerMark ==GameBoard.PLAYER_X)
				score = god.minimax(this.model.getGameBoard(), false);
			
			else //godPlayerMark == GameBoard.PLAYER_O
				score = god.minimax(this.model.getGameBoard(), true);
			//undo that move , so you can check with the same way all the other available slots.
			this.model.getGameBoard().undoMove(availableCells.get(i)/3, availableCells.get(i)%3);
			/*
			 * If the AI player is the X the best score for him is 1.
			 * So the best score variable should be the minimum integer value,
			 * and then this code will check if there exists in the other empty
			 * slots,a slot that will give a better score than 1.So if score>bestScore.
			 * But if the AI player is the O, the best score for him is -1.
			 * So the best score variable should be initialized in Maximum integer value.
			 * And then we will check if there is any next slot in the board , which
			 * gives a better score for O,that means that it should be a smaller number.
			 * So we check is the score is LESS than the bestScore! 
			 *  */
			if(godPlayerMark == GameBoard.PLAYER_X) {
				if(score > bestScore) {
					bestScore = score;
					move[0]=availableCells.get(i)/3;//row
					move[1]=availableCells.get(i)%3;//column
				}
			}
			else {//if the AI player is the player O.
				if(score < bestScore) {
					bestScore = score;
					move[0]=availableCells.get(i)/3;//row
					move[1]=availableCells.get(i)%3;//column
				}
			}
		}
		//return that best move.
		
		return move;
	}
	
	
	
	/**
	 * This method will be called if the game
	 * which is going to start is between
	 * the random player and the AI player.
	 * It starts the game, and make their moves
	 * using a timer with 1 second delay between every move.
	 *  */
	public void pcGame() {
		
		int godPlayerMark = this.model.getGamePlayers()[0] instanceof GodPlayer? 1 : -1;
		//if the god player begins:
		
		if(this.model.getMover() == godPlayerMark) 
			timer2.restart();//the game starts and god player makes the first move
			
		else //if random player begins:
			timer1.restart();//the game starts and the random AI player makes first move	
	}
	
	
	
	/**
	 * finds if exists a Random Player in the game
	 * @return true if exists a god player(AI).
	 *  */
	public boolean existGodPlayer() {
		if(this.model.getGamePlayers()[0] instanceof GodPlayer ||
				this.model.getGamePlayers()[1]  instanceof GodPlayer)
			return true;
		
		return false;
	}
		
	
	/**
	 * finds if exists a Random Player in the game
	 * @return true if exists a random player.
	 *  */
	public boolean existRandomPlayer() {
		if(this.model.getGamePlayers()[0] instanceof RandomPlayer ||
				this.model.getGamePlayers()[1]  instanceof RandomPlayer)
			return true;
		
		return false;
	}
	/**
	 * finds if exist a game between the random
	 * and the god Player
	 *  @return true if the game is between the AI
	 *  and the random player
	 *  */
	public boolean existPCgame() {
		if((this.model.getGamePlayers()[0] instanceof RandomPlayer &&
				this.model.getGamePlayers()[1] instanceof GodPlayer) ||
				(this.model.getGamePlayers()[0] instanceof GodPlayer &&
				this.model.getGamePlayers()[1] instanceof RandomPlayer))
			return true;
		
		return false;
	}
	
	public void addPlayer(Player player) {
		
		if(!this.existPlayerWithSameName(player)) {
			this.model.getPlayerCatalogue().addPlayer(player);	
		}
		//every time we add a player we want to update the hof
		this.view.getMainPanel().getHallOfFame().setHallOfFame();
	}
		
	public boolean existPlayerWithSameName(Player player) {
		
		//for-each loop to find out if the player we want to add has the same
		//name with an existing player
		for(String name : this.model.getPlayerCatalogue().getPlayerNames())	{
			if(name.equals(player.getName())){
				return true ;//means that already exists a player with the same name
			}	
		}
		
		return false;//means that there is no player with the same name inside the list		
	}
	
	/**
	 * Gets the top size players
	 * in order to be used to make 
	 * the hall of fame
	 * @return Player[] the hall of fame
	 *	*/
	public Player[] getHallOfFame() {
		//before we call this method we MUST have load the players from the file!
		int size = this.model.getPlayerCatalogue().getTotalPlayers().getSize();
		MyList<Player> hallOfFame;
		try {
			hallOfFame = this.model.getPlayerCatalogue().findHallOfFame(size);
		}catch(IllegalArgumentException e) {
			System.err.println(e.getMessage());
			return null;
		}
		//else
		return hallOfFame.toArray(new Player[1]);
		
	}
	
	/************************************************GETTERS & SETTERS***********************************************/
		
	public GameModel getModel() {
		return model;
	}
	
	public MainWindow getView() {
		return view;
	}
	
	public int getGameMoves() {
		return this.getModel().getGameBoard().getMoves();
	}
	
	public void setModel(GameModel model) {
		this.model = model;
	}
			
}