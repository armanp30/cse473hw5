package hw5;
/**
 * Alex Meng
 * CSE 473 Spring 2007
 * Homework 5, Othello, GameHeuristic.java -- AI class used to make moves for the AI using Minimax & Alpha Pruning.
 */
import java.util.ArrayList;

public class GameHeuristic {

	private static long ENDTIME;
	public static  Move iterativeAStar(Player player, Board board) {
		ENDTIME = System.currentTimeMillis() + 15 *125;
		Move result = null;
		try {
		for (int i=1;i<60;i++) {			
				if (board.getLegalMoves(player.getMarker()).size()==0)
					return null;
				else			
					result = SearchAlphaBetaPruning(player, board.clone(), i);
		}}
		catch (IllegalStateException ise )
		{}		 
		return result;
	}
	
	private static Move SearchAlphaBetaPruning(Player player,Board board, int depth) {
		if (player.getMax())
			return maxValue(board,Markers.first,Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
		else
			return minValue(board,Markers.second,Integer.MIN_VALUE, Integer.MAX_VALUE,depth);	
	}
	
	
	private static Move maxValue(Board board, String marker, int alpha, int beta, int depth) {
		
		if ( System.currentTimeMillis() > ENDTIME)
			throw new IllegalStateException();
		Move result = new Move(1,1,null,Integer.MIN_VALUE);
		ArrayList<Move> conversions;
		
		ArrayList<Move> childrens = board.getLegalMoves(marker); //getEmptySpots();
		for (int i=0; i<childrens.size(); i++ ) {
			Move temp = childrens.get(i);
			temp.marker = Markers.first;			
			conversions = board.makeMove(temp);	
			
			if (cutoffTest(board, depth))				
				temp.value = evaluateState(board, marker);		
			else
				temp.value = minValue(board, Markers.second, alpha, beta, --depth).value;
			
			if (result.value < temp.value)
				result = temp;
			board.undoMove(temp, conversions);
			if (result.value >= beta)
				return result;
			alpha = Math.max(alpha, result.value);
		}
	
		return result;
	}
	
	private static Move minValue(Board board, String marker, int alpha, int beta, int depth) {
		if ( System.currentTimeMillis() > ENDTIME)
			throw new IllegalStateException();
		Move result = new Move(1,1,null,Integer.MAX_VALUE);
		ArrayList<Move> conversions;
		
		ArrayList<Move> childrens = board.getLegalMoves(marker); //getEmptySpots();
		for (int i=0; i<childrens.size(); i++ ) {
			Move temp = childrens.get(i);
			temp.marker = Markers.second;
			conversions = board.makeMove(temp);
		
			if (cutoffTest(board, depth)) 
				temp.value = evaluateState(board, marker);						
			else
				temp.value = maxValue(board, Markers.first, alpha, beta, --depth).value;
			
			if (result.value > temp.value)
				result = temp;
			board.undoMove(temp, conversions);
			
			if (result.value <= alpha)
				return result;
			beta = Math.min( beta, result.value);
		}
	
		return result;
	}
	
	private static boolean cutoffTest(Board board, int depth) {			
		return depth==0 || staleMate(board);
	}
	
	
	private static boolean staleMate(Board board) {
		return board.getLegalMoves(Markers.first).size() == 0 && board.getLegalMoves(Markers.second).size() == 0; 
	}
	private static int evaluateState(Board board, String marker) {
		return utility(board, marker);		
	}
	
	/**
	 * An internal method used to determine the value of the board
	 * given its current state.
	 * 
	 * @param board a Board object, representing the current state of the board.
	 * @return an int, representing a numerical value of the state. Positive int indicates that the first player is winning. Negative reflects second player is winning.
	 * The value represents how many pieces they occupy.
	 */
	private static int utility(Board board, String marker) {
		int val = 5 * occupiedCorners(board, marker) + 7 * fullEdges(board, marker) + 2 * stableDiscs(board, marker);
		if (marker.equals(Markers.first)) {
			return val;
		} else {
			return -val;
		}
	}

	

	
	
	
	/*
	 * Heuristics to Implement
	 * ************************
	 * 
	 * Stable discs - Discs that can never be converted
	 * Wedges - Getting between opponent's discs on the edges so that he can't
	 * 			flip the newly-placed edge piece
	 * Mobility - Number and quality of possible moves.  Playing to a square
	 * 			a corner is bad as it allows your opponent to take the corner.
	 * 			Force your opponent to take a square next to the corner and you
	 * 			profit.
	 * Frontier -	Number of pieces you have next to an empty square.  The
	 * 				more you have, the more of your pieces opponent can
	 * 				possibly flip
	 * Center control - Controlling the center often affords your protection
	 * 					from conversions and gives lots of options for
	 * 					opponent's pieces to convert.
	 * 					Might be used in conjunction with frontier heuristic
	 */
	
	
	/**
	 * Computes the number of corners this marker occupies.
	 * 
	 * @param bArray	The board to examine
	 * @param marker Marker to count corner control for	
	 * @return number of corners this marker occupies
	 * 
	 */
	private static int occupiedCorners(Board board, String marker) {
		int val = 0;
		if (marker.equals(board.getMarker(0, 0))) {
			val++;
		}
		if (marker.equals(board.getMarker(0, 7))) {
			val++;
		}
		if (marker.equals(board.getMarker(7, 7))) {
			val++;
		}
		if (marker.equals(board.getMarker(7, 0))) {
			val++;
		}
		return val;
	}
	
	/**
	 * Computes the number of full edges this marker occupies
	 * @param board The board to examine
	 * @param marker To count edge control for
	 * @return number of edges this marker controls
	 */
	
	private static int fullEdges(Board board, String marker) {
		int edge = board.getBoardSize() - 1;
		boolean fullEdge = true;
		int val = 0;
		int x;
		int y;
		for (x = 0; x < 8; x += 7) {
			for (y = 0; y <= edge; y++) {
				if (!marker.equals(board.getMarker(x, y))) {
					fullEdge = false;
				}
			}
			if (fullEdge == true) {
				val++;
			}
			fullEdge = true;
		}
		for (y = 0; y < 8; y += 7) {
			for (x = 0; x <= edge; x++) {
				if (!marker.equals(board.getMarker(x, y))) {
					fullEdge = false;
				}
			}
			if (fullEdge == true) {
				val++;
			}
			fullEdge = true;
		}
		return val;
	}
	
	/*
	 * A disc is stable if it is in a corner formed either by the edge of the
	 * board or by another stable disc.
	 */
	

	public static int stableDiscs(Board board, String marker) {
		boolean[][] stable = new boolean[8][8];
		int val = 0;
		
		stable = findStableDiscs(board, stable, 0, 0, 1, 1, marker);
		stable = findStableDiscs(board, stable, 7, 7, -1, -1, marker);
		stable = findStableDiscs(board, stable, 0, 7, 1, -1, marker);
		stable = findStableDiscs(board, stable, 7, 0, -1, 1, marker);
		
		
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (stable[x][y]) {
					val++;
				}
			}
		}
		return val;
	}
	
	private static boolean[][] findStableDiscs(Board board, boolean[][] stable,
			int x, int y, int deltaX, int deltaY, String marker) {
		int i, j;
		while(x < 8 && x >= 0 && y < 8 && y >= 0) {
			if (!marker.equals(board.getMarker(x,y))) {
				return stable;
			}	
			for (i = x; i < 8 && i >= 0; i += deltaX) {
				if (marker.equals(board.getMarker(i, y))) {
					stable[i][y] = isStable(board, stable, i, y, deltaX, deltaY, marker);
				} else {
					break;
				}
			}
			for (j = y; j < 8 && j >= 0; j += deltaY) {
				if (marker.equals(board.getMarker(x, j))) {
					stable[x][j] = isStable(board, stable, x, j, deltaX, deltaY, marker);
				} else {
					break;
				}
			}
			x += deltaX;
			y += deltaY;
		}
		return stable;
		
	}
	
	private static boolean isStable(Board board, boolean[][] stable,
			int x, int y, int deltaX, int deltaY, String marker) {
		boolean safeX = true;
		boolean safeY = true;
		if (x - deltaX < 0 || x - deltaX > 7) {
			safeX = true;
		} else {
			safeX = stable[x - deltaX][y];
		}
		if (y - deltaY < 0 || y - deltaY > 7) {
			safeY = true;
		} else {
			safeY = stable[x][y - deltaY];
		}
		return safeX && safeY;
	}
	
	public static void printStable(boolean[][] stable) {
		System.out.println("  0 1 2 3 4 5 6 7");
		for (int i = 0; i < 8; i++) {
			System.out.print(i);
			for (int j = 0; j < 8; j++) {
				if (stable[i][j]) {
					System.out.print(" S");
				} else 
					System.out.print(" -");
			}
			System.out.println();
		}
	}
	
	
}
