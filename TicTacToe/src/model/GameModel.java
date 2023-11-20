package model;


import java.io.Serializable;
import java.text.DecimalFormat;

import controller.GameController;

public class GameModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PlayersCatalogue  playersCatalogue;
	private Player[] gamePlayers;		
	private GameBoard gameBoard;
	private GameController gc;
	
	public GameModel(GameController gc) {
		this.gc=gc;
		gamePlayers = new Player[2];
		gameBoard= null;
		playersCatalogue= new PlayersCatalogue();
	}
	
	
	public void selectPlayer(Player player, int pos) {
		if (pos<0 && pos>1)
			return;
		gamePlayers[pos]=player;		
	}
	
	
	public boolean ready() {
		return (gamePlayers[0] != null && gamePlayers[1] !=null);
	}
	
	
	public void startGame(int pos) {
		gameBoard= new GameBoard(pos);
	}
	
	
	public boolean inPlay() {
		return (gameBoard !=null && gameBoard.getMoves() < GameBoard.MAX_MOVES && gameBoard.getEnd()==false);
	}
	
	public boolean noPlay() {
		return !inPlay();
	}
	
	public void placeMove(int row,int col) {
		if(inPlay())
			this.gameBoard.makeMove(row, col);
	}
	
	/*Updates the win counter of the winner
	 * and the lose counter of the loser
	 */
	public void updateWinner() {

		GameRecord game = new GameRecord();
		/*
		 * The gamePlayers array will change when the next game
		 * take place.But we need to hold a final player array
		 * in every game record.Else the references to the players
		 * will change in the next game.So instead 
		 * we will make new players with the copy constructor.
		 * They will not change when the new game will take place. 
		 *  */
		//for game Player[0] :
		if(this.gamePlayers[0] instanceof GodPlayer)
			game.selectPlayer(new GodPlayer(this.gamePlayers[0]), 0);
		else if (this.getGamePlayers()[0] instanceof RandomPlayer) {
			game.selectPlayer(new RandomPlayer(this.gamePlayers[0]), 0);
		}else
			game.selectPlayer(new HumanPlayer(this.gamePlayers[0]), 0);
		
		//for game Player[1] :
		if(this.gamePlayers[1] instanceof GodPlayer)
			game.selectPlayer(new GodPlayer(this.gamePlayers[1]), 1);
		else if (this.getGamePlayers()[1] instanceof RandomPlayer) {
			game.selectPlayer(new RandomPlayer(this.gamePlayers[1]), 1);
		}else
			game.selectPlayer(new HumanPlayer(this.gamePlayers[1]), 1);
		
		game.setResult(0);
		
		
		//you should update here the result in the game record, and at the ends of this method add it to the players
		if(this.getGameBoard().getWinner() == GameBoard.PLAYER_X) {
			this.gamePlayers[0].playerWins();//1 corresponds to player X 
			this.gamePlayers[0].updateScore();//update the total score using the new data

			game.setResult(GameBoard.PLAYER_X);//X won
			
			this.gamePlayers[1].playerLoses();//When X wins O loses
			this.gamePlayers[1].updateScore();//update the total score using the new data
			
			System.out.println("Data updated");
		}
		else if(this.gameBoard.getWinner() == GameBoard.PLAYER_O){
			this.gamePlayers[1].playerWins();//-1 corresponds to player O
			this.gamePlayers[1].updateScore();//update the total score using the new data
			
			game.setResult(GameBoard.PLAYER_O);//O won
			
			this.gamePlayers[0].playerLoses();//When O wins X loses
			this.gamePlayers[0].updateScore();//update the total score using the new data
			
			System.out.println("Data updated");


		}
		
		//update their game records;
		/*
		 * This game will be ignored if it's not better
		 * than the other games in the best games list.
		 * Else it will replace another one.
		 * 
		 *  */
		this.getGamePlayers()[0].addGoodGame(game);
		this.getGamePlayers()[0].addRecentGame(game);
		this.getGamePlayers()[1].addGoodGame(game);
		this.getGamePlayers()[1].addRecentGame(game);
	}
	
	/**************************************************GETTERS & SETTERS******************************************/
	
	public Player[] getGamePlayers() {
		return this.gamePlayers;
	}
	
	public int getMover() {
		boolean bool = this.gameBoard.getMover();
		return bool == true? GameBoard.PLAYER_X : GameBoard.PLAYER_O;
	}
	

	public GameBoard getGameBoard() {
		return gameBoard;
	}

	public void setGameBoard(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
	}

	public PlayersCatalogue getPlayerCatalogue() {
		//playerCatalogue.load?
		return playersCatalogue;
	}

	public String getPlayerStats(String name) {
		//wants more to add here
		Player player = this.getPlayerCatalogue().findPlayerByName(name);
		StringBuilder stringBuilder = new StringBuilder("");
		DecimalFormat df = new DecimalFormat("##.##");//format to get only 2 decimal digits of the double
		double winRate = 0.0;
		
		try {
			//if game Counter ==0 exception will be thrown
			winRate =((player.getGameCounter() - player.getLoseCounter())/(double)(player.getGameCounter()));
			
			if(Double.valueOf(winRate).isNaN()) {
				winRate=0.0;
				//in order not to show NaN
			}
		} catch (ArithmeticException e) {}
		
		
		stringBuilder.append("Total Games:\t" + player.getGameCounter()).append("\n");//total games
		stringBuilder.append("Total Score:\t" + df.format(player.getScore())).append("\n");//Total score
		stringBuilder.append("Win Rate:\t" + (df.format(winRate*100)) + "%").append("\n\n");//Win rate
		//best games
		if(player.getGoodGames() != null) {
			stringBuilder.append("Best Games:\n");
			stringBuilder.append(player.getGoodGames()).append("\n");
		}
		
		
		if(player.getRecentGames() != null) {
			stringBuilder.append("Recent Games:\n");
			stringBuilder.append(player.getRecentGames());
		}
		
		return stringBuilder.toString();
	}
	public void setPlayerCatalogue(PlayersCatalogue playerCatalogue) {
		this.playersCatalogue = playerCatalogue;
	}
	
	
	public GameController getGameController() {
		return this.gc;
	}
}