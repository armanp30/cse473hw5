package hw5;
/**
 * Alex Meng
 * CSE 473 Spring 2007
 * Homework 5, Othello, AIPlayer.java, this class is used to define the state and behavior of an AI Player.
 */

/**
 * This class is designed to represent an AI Player.
 * @author alexmeng
 */
public class AIPlayer implements Player {
	//an internal string to represent the Player on the board
	private String marker;
	//a flag to indicate whether or not use to Minimax algo.
	private boolean useMMax;
	
	/**
	 * Constructs an AIPlayer object based on its marker and if it should use minimax.
	 * 
	 * @param mark a String object, defining how the player should be represented on the board.
	 * @param minimax a boolean, indicating whether or not to use minimax to make moves.
	 */
	public AIPlayer (String mark, boolean minimax) {
		this.useMMax = minimax;
		this.marker = mark;
	}
	
	/**
	 * A method which determines if this player should be using MaxValue first, if it's black.
	 * 
	 * @return a boolean, true, if it moves first. false, otherwise.
	 */
	public boolean getMax() {
		return marker.equals("B");
	}
	
	/**
	 * This method returns the marker used to represent the player on the board.
	 * 
	 * @return a String object, representing the player's marker.
	 */
	public String getMarker() {
		return this.marker;
	}

	/**
	 * This method is used to calculate a move to make for the AI
	 * based on the state of the board passed.
	 * 
	 * @param state, a Board object, representing the current board state.
	 * @return void.
	 */
	public void makeMove(Board state) {
		Move bestMove;
		
		if (useMMax) 
			bestMove = GameHeuristic.SearchMiniMax(this, state);
		else 
			bestMove = GameHeuristic.SearchAlphaBetaPruning(this,state);
		
		state.makeMove(bestMove);
	}

}
