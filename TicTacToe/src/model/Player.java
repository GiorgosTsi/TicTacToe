package model;

import java.io.Serializable;


public abstract class Player implements Comparable<Player>,Serializable {
	
	private static final long serialVersionUID = 1L;
	
	protected String name;
	protected double score;
	protected int gameCounter;
	protected int winCounter;
	protected int loseCounter;
	protected GameRecord[] fiveLastGames;//five Last games
	protected GameRecord[] fiveBestGames;//The top five games of this player
	
	/*Constructors*/
	
	
	/**
	 * This constructor initializes the name
	 * by a String parameter
	 * @param name
	 */
	public Player(String name) {
		this.name = name;
		this.score = 0.0;
		this.gameCounter = 0;
		this.winCounter = 0;
		this.loseCounter = 0;
		this.fiveBestGames = new GameRecord[5];
		this.fiveLastGames = new GameRecord[5];
	}
	
	
	//default constructor
	public  Player() {
		this.name = null;
		this.score = 0.0;
		this.gameCounter = 0;
		this.winCounter = 0;
		this.loseCounter = 0;
		this.fiveBestGames = new GameRecord[5];
		this.fiveLastGames = new GameRecord[5];
	}
	
	/**
	 * This is a copy constructor
	 * Clones the input parameter player
	 * @param Player the player which will be cloned 
	 *  */
	public Player(Player player) {
		this.name = player.getName();
		this.score = player.getScore();
		this.gameCounter = player.getGameCounter();
		this.winCounter = player.getWinCounter();
		this.loseCounter = player.getLoseCounter();
		this.fiveBestGames = player.getFiveBestGames();
		this.fiveLastGames = player.getFiveLastGames();
	}
	
	
	/*Other methods */
	@Override
	public int compareTo(Player p) {
		//Compare the players, via their score in the game
		if(this.getScore() < p.getScore()){
			return 1;
		}
		else if(this.getScore() == p.getScore()){
			return 0;
		}	
		return -1;
	}

	//if a game is better than the five games in the list,add it.
	/**
	 * A game is better than another one if:
	 * a) The result is better : win>tie>lose
	 * b) The result is the same,but the opponent is a better player.
	 * 	  If the score of a player is bigger than another player's score,the first is better.
	 * c)The above are the same,but this game is more recent.
	 * @param GameRecord game
	 *  */
	public void addGoodGame(GameRecord game) {

		for (int i = 0; i < this.fiveBestGames.length; i++) {
			GameRecord current = this.fiveBestGames[i];
			if (current == null) {
				this.fiveBestGames[i] = game;
				return;	
			} 
			else{
				if (game.myCompareToGameRecords(current, this) < 0) { //if game > current
					// item should be placed here...
					
					// starting from the end, move each element one position down
					for (int j=this.fiveBestGames.length-1; j>i;j--) {
						fiveBestGames[j]=fiveBestGames[j-1];
					}
					//put the item in the i position;
					fiveBestGames[i] = game;
					return;
				}
			}		
		}
	}	
	
	
	/**
	 *Compare Local date time,and if the game is more recent than
	 *another one, replace it with the new.
	 *@param GameRecord game 
	 *  */
	public void addRecentGame(GameRecord game) {
		/*
		 * Traverse the array of the game records.If there is null in a position
		 * and all the above elements are 'bigger', place it in the first null position.
		 * Else compare the elements and place it in the correct position. 
		 *  */
		for(int i=0 ; i<this.fiveLastGames.length; i++) {
			
			if(fiveLastGames[i] == null) {
				fiveLastGames[i] = game;
				return;
			}else if(fiveLastGames[i].getGameTime().isBefore(game.getGameTime())){
				//move down all the other elements and place the parameter in the correct position
				for(int j=fiveLastGames.length -1; j>0 ; j--) {//shift 
					fiveLastGames[j] = fiveLastGames[j-1];
				}
				
				this.fiveLastGames[i] = game;
				return;
			}
		}
	}
	
	
	/*Selection sort */
	@SuppressWarnings("unused")
	private void reverseSortRecentGames() {
		GameRecord temp = null;
		
		for(int i=0;i< fiveLastGames.length ; i++) {
			int maxIndex=i;
			for(int j=i+1; j<fiveLastGames.length; j++) {
				if(fiveLastGames[maxIndex].getGameTime().isBefore(fiveLastGames[j].getGameTime())) {
					maxIndex = j;
				}
			}
			temp = fiveLastGames[i];
			fiveLastGames[i] = fiveLastGames[maxIndex];
			fiveLastGames[maxIndex] = temp;
			
		}
	}
	
	public void updateScore() {
		//update the score of the player after the end of the game
		this.score = Player.scoreFormula(this.winCounter, this.loseCounter, this.gameCounter);
	}
	
	private static double scoreFormula(int wins,int loses,int totalGames) {
		//A formula that finds the score of a player.
		int ties = totalGames -(wins + loses);
		return (50 * ((2 * wins + ties)/(double)totalGames));
	}
	
	
	@Override
	public boolean equals(Object that) {
		/*1. Check if the references are the same */
		if(this == that)
			return true;
		/*2.Check that their classes are the same, and that!=null */
		if(that == null ||
				this.getClass() != that.getClass())
			return false;
		/*3.Check if their members are the same */
		Player p = (Player)that;//casting
		/*There is no need to check all their members.
		 *For our purpose we need only to check their names.
		 *Every player has a different name!
		 * */
		return(this.name.equals(p.getName()));
		
	}
	
	@Override
	public String toString() {
		/*
		 * returns a representation of this Player as a String
		 *  */
		return this.name + " ,score: " + this.score
				+ ",Total Games: " + this.gameCounter;
	}
	
	public void playerWins() {
		//if player win a game, increase the win Counter
		this.winCounter++;
	}
	
	public void playerLoses() {
		//if a player lose a game, increase the lose Counter
		this.loseCounter++;
	}
	
	public void addGame() {
		this.gameCounter++;
	}
	
	public void randomMove() {
		
	}
	
	/*****************************************GETTERS & SETTERS*************************************************/
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public double getScore() {
		return score;
	}

	public int getGameCounter() {
		return gameCounter;
	}

	public void setGameCounter(int gameCounter) {
		this.gameCounter = gameCounter;
	}

	public String getGoodGames() {
		GameRecord[] games = this.fiveBestGames;
		StringBuilder stringBuilder = new StringBuilder("");
		Player opponent;
		
		for(var game : games) 
			if(game != null) {
				opponent = game.getGamePlayers()[0].equals(this) ? game.getGamePlayers()[1] : game.getGamePlayers()[0];
				stringBuilder.append(game.myToString(this,opponent)).append("\n");
			}
		if(stringBuilder.toString().equals(""))
			return null;
		
		return stringBuilder.toString();
	}
	
	public String getRecentGames() {
		StringBuilder stringBuilder = new StringBuilder("");
		Player opponent;
		
		for(var game : this.fiveLastGames)
			if(game != null) {
				opponent = game.getGamePlayers()[0].equals(this) ? game.getGamePlayers()[1] : game.getGamePlayers()[0];
				stringBuilder.append(game.myToString(this,opponent)).append("\n");
			}
		//if strinBuilder is empty return null
		if(stringBuilder.toString().equals(""))
			return null;
		//else
		return stringBuilder.toString();
	}
	
	public int getWinCounter() {
		return winCounter;
	}

	public void setWinCounter(int winCounter) {
		this.winCounter = winCounter;
	}

	public int getLoseCounter() {
		return loseCounter;
	}

	public void setLoseCounter(int loseCounter) {
		this.loseCounter = loseCounter;
	}
	
	
	public GameRecord[] getFiveBestGames() {
		return fiveBestGames;
	}

	
	public GameRecord[] getFiveLastGames() {
		return fiveLastGames;
	}


	public void setScore(double score) {
		this.score=score;
	}
}