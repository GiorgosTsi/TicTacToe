package model;

import java.io.Serializable; 
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A class that represents a final game.
 * Holds the game players , the time of the game 
 * and the result of it. 
 *  */
public class GameRecord implements Serializable  {

	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private final Player[] gamePlayers;
	private LocalDateTime gameTime;
	private int result;
	// 1 if it is win for gamePlayers[0]==X, 0 tie , -1 win for gamePlayers[1]==O
	
	public GameRecord() {
		this.gamePlayers = new Player[2];
		this.gameTime= LocalDateTime.now();		
	}
	
	public GameRecord(Player[] gamePlayers) {
		this.gamePlayers = gamePlayers;
		this.gameTime = LocalDateTime.now();
	}

	public void selectPlayer(Player player, int pos) {
		if (pos<0 && pos>1)return;
		gamePlayers[pos]=player;		
	}
	
	public void swapPlayersPosition() {
		Player temp = this.gamePlayers[0];
		gamePlayers[0] = gamePlayers[1];
		gamePlayers[1] = temp;
	}
	
	
	/**
	 * Take as parameter the gameRecord,which we want
	 * to compare with the invoke gameRecord,and the 
	 * player whose we want to compare his games.
	 * A game is better than another one if:
	 * a) The result is better : win>tie>lose
	 * b) The result is the same,but the opponent is a better player.
	 * 	  If the score of a player is bigger than another player's score,the first is better.
	 * c)The above are the same,but this game is more recent.
	 *  @param GameRecord , Player p
	 *  @return int
	 *  */
	public int myCompareToGameRecords(GameRecord that,Player p) {
		/**
		 * We must compare the opponents in some games,
		 * so we must know for which player we are interested
		 * in , so we find his opponents and compare them.
		 * That's why we use a different compare To method.
		 * We need an extra input parameter.
		 *  */
		//first we want to find his opponent
		Player firstOpponent = this.gamePlayers[0].equals(p) ? this.gamePlayers[1] : this.gamePlayers[0];
		Player secondOpponent = that.getGamePlayers()[0].equals(p)? that.getGamePlayers()[1] : that.getGamePlayers()[0];
		
			
		if(p.equals(this.gamePlayers[0])) {
			//that > this
			//this block is for the input parameter player X
			//result==1 means that X won
			if(this.result < that.getResult() ||
					(this.result == that.getResult() && firstOpponent.compareTo(secondOpponent) == 1))//firstOpponent<secondOpponent
				return 1;
			if(this.result == that.getResult() && (firstOpponent.compareTo(secondOpponent) == 0))//firstOpponent==SecondOpponent
				if(this.gameTime.isBefore(that.getGameTime()))
					return 1;
			
			//this > that
			if(this.result > that.getResult() ||
					(this.result == that.getResult() && firstOpponent.compareTo(secondOpponent) < 0))//firstOpponent>secondOpponent
				return -1;
			if(this.result == that.getResult() && (firstOpponent.compareTo(secondOpponent) == 0))
				if(this.gameTime.isAfter(that.getGameTime()))
					return -1;								
			
			//finally this == that
			return 0;
		}
		else {
			//that > this
			//this block is  for the input parameter player O
			//result == -1 means that O won.So we must reverse the inequality
			if(this.result > that.getResult() ||
					(this.result == that.getResult() && firstOpponent.compareTo(secondOpponent) == 1))//firstOpponent<secondOpponent
				return 1;
			if(this.result == that.getResult() && (firstOpponent.compareTo(secondOpponent) == 0))//firstOpponent==SecondOpponent
				if(this.gameTime.isBefore(that.getGameTime()))
					return 1;
			
			//this > that
			if(this.result < that.getResult() ||
					(this.result == that.getResult() && firstOpponent.compareTo(secondOpponent) < 0))//firstOpponent>secondOpponent
				return -1;
			if(this.result == that.getResult() && (firstOpponent.compareTo(secondOpponent) == 0))
				if(this.gameTime.isAfter(that.getGameTime()))
					return -1;
			
			//finally this == that
			return 0;
		}
	}
	
	/**
	 * A method that returns this game record
	 * as a string representation.We didn't use
	 * the original toString() because we want also
	 * as parameter the player that we are interested in
	 * to print his games in player's panel.
	 * @param the player in which we want to show this string on his panel,
	 * @param  his opponent in this game.
	 * @return a string representation.
	 *  */
	public String myToString(Player p,Player opponent) {
		/*We can also find easily the opponent inside this method but it's okay */
		/*************************************************************/
		
		//the == operator will not work now.They are different references.
		String s = new String("");
		String time = this.gameTime.format(DateTimeFormatter.ofPattern("dd-MM-HH:mm"));///day,month,hour,minutes
		
		if((result == 1 && p.equals(this.gamePlayers[0]))//if result ==1 and p is the X player
				|| (result == -1 && p.equals(this.gamePlayers[1])))//if result ==-1 and p is the Ï player
			s+="Win ";
		else if((result ==1 && p.equals(this.gamePlayers[1]))
				|| (result == -1 && p.equals(this.gamePlayers[0])))
			s+= "Lose ";
		else if(result == 0)
			s+="Tie ";
		
		s+="vs " + opponent.getName();
		s+=" " + time;
		
		return s;
	}

	/***********************************************GETTERS & SETTERS*******************************************/
	
	public Player[] getGamePlayers() {
		return gamePlayers;
	}


	public LocalDateTime getGameTime() {
		return gameTime;
	}


	public void setGameTime(LocalDateTime gameTime) {
		this.gameTime = gameTime;
	}


	public int getResult() {
		return result;
	}


	public void setResult(int result) {
		this.result = result;
	}
	
}
