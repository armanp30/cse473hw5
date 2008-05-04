package hw5;
/**
 * Alex Meng
 * CSE 473 Spring 2007
 * Homework 4, Tic Tac Toe, Minimax & Alpha Pruning
 */
public class AIPlayer implements Player {
	private String marker;
	private boolean useMMax;
	
	public AIPlayer (String mark, boolean minimax) {
		this.useMMax = minimax;
		this.marker = mark;
	}
	
	public boolean getMax() {
		return marker.equals("B");
	}
	
	public String getMarker() {
		return this.marker;
	}

	public void makeMove(Board state) {
		if (useMMax) {
			Move bestMove = GameHeuristic.SearchMiniMax(this, state);
			state.makeMove(marker, bestMove.x, bestMove.y);
			state.isGameOver();
		}
		else {
			Move bestMove = GameHeuristic.SearchAlphaBetaPruning(this,state);
			state.makeMove(marker, bestMove.x, bestMove.y);
			state.isGameOver();
		}
		
	}

}
