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
			return maxValue(board,Markers.first,-200, 200, depth);
		else
			return minValue(board,Markers.second, -200, 200, depth);	
	}
	
	
	private static Move maxValue(Board board, String marker, int alpha, int beta, int depth) {
		
		if ( System.currentTimeMillis() > ENDTIME)
			throw new IllegalStateException();
		Move result = new Move(1,1,null,-200);
		ArrayList<Move> conversions;
		
		ArrayList<Move> childrens = board.getLegalMoves(marker); //getEmptySpots();
		for (int i=0; i<childrens.size(); i++ ) {
			Move temp = childrens.get(i);
			temp.marker = Markers.first;			
			conversions = board.makeMove(temp);	
			
			if (cutoffTest(board, depth))				
				temp.value = evaluateState(board);		
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
		Move result = new Move(1,1,null,200);
		ArrayList<Move> conversions;
		
		ArrayList<Move> childrens = board.getLegalMoves(marker); //getEmptySpots();
		for (int i=0; i<childrens.size(); i++ ) {
			Move temp = childrens.get(i);
			temp.marker = Markers.second;
			conversions = board.makeMove(temp);
		
			if (cutoffTest(board, depth)) 
				temp.value = evaluateState(board);						
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
	private static int evaluateState(Board board) {
		return utility(board);		
	}
	
	/**
	 * An internal method used to determine the value of the board
	 * given its current state.
	 * 
	 * @param board a Board object, representing the current state of the board.
	 * @return an int, representing a numerical value of the state. Positive int indicates that the first player is winning. Negative reflects second player is winning.
	 * The value represents how many pieces they occupy.
	 */
	private static int utility(Board board) {
		return board.getMajority();	
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
	 * @param board	The board to examine
	 * @param marker	
	 * @return
	 * 
	 */
//	private int occupiedCorners(Board board, String marker) {
//		int val = 0;
//		if (board[0][0].equals(marker)) {
//			val++;
//		}
//		if (board[0][board.getBoardSize() - 1].equals(marker)) {
//			val++;
//		}
//		if (board[board.getBoardSize() - 1][board.getBoardSize() - 1 ].equals(marker)) {
//			val++;
//		}
//		if (board[board.getBoardSize() - 1][0].equals(marker)) {
//			val++;
//		}
//		return val;
//	}
//	
//	private int fullEdges(Board board, String marker) {
//		boolean fullEdge = true;
//		int val = 0;
//		int x;
//		int y;
//		for (x = 0; x < 8; x += 7) {
//			for (y = 0; y < board.getBoardSize(); y++) {
//				if (!board[x][y].equals(marker)) {
//					fullEdge = false;
//				}
//			}
//			if (fullEdge == true) {
//				val++;
//			}
//			fullEdge = true;
//		}
//		for (y = 0; y < 8; y += 7) {
//			for (x = 0; x < board.getBoardSize(); x++) {
//				if (!board[x][y].equals(marker)) {
//					fullEdge = false;
//				}
//			}
//			if (fullEdge == true) {
//				val++;
//			}
//			fullEdge = true;
//		}
//		return val;
//	}
//	
//	/*
//	 * A disc is stable if it is in a corner formed either by the edge of the
//	 * board or by another stable disc.
//	 */
//	
//	private int stableDiscs(Board board, String marker) {
//		boolean[][] stable = new boolean[8][8];
//		int edge = board.getBoardSize() - 1;
//		int x = 0;
//		int y = 0;
//		int deltaX;
//		int deltaY;
//		if (board[x][y].equals(marker)) {
//			deltaX = 1;
//			deltaY = 1;
//			do {
//				for (i = x + deltaX; x <= edge; x += deltaX) {
//					if (board[i][y].equals(marker)) {
//						stable[i][y] = true;
//					} else {
//						break;
//					}
//				}
//				for (i = y + deltaY; y <= edge; y += deltaY) {
//					if (board[x][i].equals(marker)) {
//						stable[x][i] = true;
//					} else {
//						break;
//					}
//				}
//				x += 1;
//				y += 1;
//			} (while board[x][y].equals(marker) && board[x - 1][y].equals(marker) && board[x][y-1].equals(marker));
//		}
//		if (board[0][edge].equals(marker)) {
//			stable[0][edge] = true;
//		}
//		if (board[edge][edge].equals(marker)) {
//			stable[edge][edge] = true;
//		}
//		if (board[edge][0].equals(marker)) {
//			stable[edge][0] = true;
//		}
//		Find discs that are next to an edge and a stable disc
//	}
//	
//	private void findStableDiscs(boolean[][] stable, x, y, deltaX, deltaY) {
//		for (i = x + deltaX; x <= edge; x += deltaX) {
//			if (board[i][y].equals(marker)) {
//				stable[i][y] = true;
//			} else {
//				break;
//			}
//		}
//		for (i = y + deltaY; y <= edge; y += deltaY) {
//			if (board[x][i].equals(marker)) {
//				stable[x][i] = true;
//			} else {
//				break;
//			}
//		}
//	}
}