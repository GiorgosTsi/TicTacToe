package model;


public class RandomPlayer extends Player{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RandomPlayer(String name,GameModel gameModel) {
		super(name);
	}
	
	public RandomPlayer() {
		super();
		
	}
	
	public RandomPlayer(Player copyPlayer) {
		super(copyPlayer);
	}
	
}

