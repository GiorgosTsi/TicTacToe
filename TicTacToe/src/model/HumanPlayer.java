package model;


/**
 * This class represents a human Player.Every player which user gonna
 * add will be a human-player.
 *  */
public class HumanPlayer extends Player{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HumanPlayer() {
		super();
	}
	
	public HumanPlayer(String name) {
		super(name);
	}
	
	public HumanPlayer(double score) {
		this.score = score;
	}
	
	public HumanPlayer(Player player) {
		super(player);
	}
	

}
