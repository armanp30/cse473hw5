package hw5;
/**
 * Alex Meng
 * CSE 473 Spring 2007
 * Homework 5, Othello, GameHeuristic.java -- AI class used to make moves for the AI using Minimax & Alpha Pruning.
 */
import java.util.ArrayList;

public class GameHeuristic {

	public static Move SearchMiniMax(Player player, Board board){ 		
		if (player.getMax())
			return maxValue(board);
		else
			return minValue(board);
	}
	
	private static Move maxValue(Board board, String marker) {
		Move result = new Move(0,0,null,-2);
		ArrayList<Move> conversions;
		
		ArrayList<Move> childrens = new ArrayList<Move>();//board.getEmptySpots();
		for (int i=0; i<childrens.size(); i++ ) {
			Move temp = childrens.get(i);
			temp.marker = Markers.first;
			conversions = board.makeMove(temp);
			if  ( terminalTest(board) )
				temp.value = utility(board);
			else
				temp.value = minValue(board).value;
			if (result.value < temp.value)
				result = temp;
			board.undoMove(temp, conversions);
		}
	
		return result;
	}
	
	private static Move minValue(Board board, String marker) {
		Move result = new Move(0,0,null,2);
		ArrayList<Move> conversions;
		
		ArrayList<Move> childrens = board.getLegalMoves(null); //getEmptySpots();
		for (int i=0; i<childrens.size(); i++ ) {
			Move temp = childrens.get(i);
			temp.marker = Markers.second;
			conversions = board.makeMove(temp);
			if  ( terminalTest(board) )
				temp.value = utility(board);
			else
				temp.value = maxValue(board).value;
			if (result.value >= temp.value)
				result = temp;
			board.undoMove(temp, conversions);
		}
		return result;
	}
	
	private static boolean terminalTest(Board board) {
		return board.isGameOver();
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
	
	public static Move SearchAlphaBetaPruning(Player player,Board board) {
		if (player.getMax())
			return maxValue(board, -2, 2);
		else
			return minValue(board, -2, 2);
		
	}
	
	public static Move maxValue(Board board, int alpha, int beta, String marker) {
		Move result = new Move(0,0,null,-2);
		ArrayList<Move> conversions;
		
		ArrayList<Move> childrens = board.getLegalMoves(null); //getEmptySpots();
		for (int i=0; i<childrens.size(); i++ ) {
			Move temp = childrens.get(i);
			temp.marker = Markers.first;
			conversions = board.makeMove(temp);
			if  ( terminalTest(board) )
				temp.value = utility(board);
			else
				temp.value = minValue(board).value;
			if (result.value < temp.value)
				result = temp;
			board.undoMove(temp, conversions);
			if (result.value >= beta)
				return result;
			alpha = Math.max(alpha, result.value);
		}
	
		return result;
	}
	
	public static Move minValue(Board board, int alpha, int beta, String marker) {
		Move result = new Move(0,0,null,2);
		ArrayList<Move> conversions;
		
		ArrayList<Move> childrens = board.getLegalMoves(null);//();
		for (int i=0; i<childrens.size(); i++ ) {
			Move temp = childrens.get(i);
			temp.marker = Markers.second;
			conversions = board.makeMove(temp);
			if  ( terminalTest(board) )
				temp.value = utility(board);
			else
				temp.value = maxValue(board).value;
			if (result.value >= temp.value)
				result = temp;
			board.undoMove(temp, conversions);
			if (result.value <= alpha)
				return result;
			beta = Math.min( beta, result.value);
		}
		return result;
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
	private int occupiedCorners(Board board, String marker) {
		int val = 0;
		if (board[0][0].equals(marker)) {
			val++;
		}
		if (board[0][board.getBoardSize() - 1].equals(marker)) {
			val++;
		}
		if (board[board.getBoardSize() - 1][board.getBoardSize() - 1 ].equals(marker)) {
			val++;
		}
		if (board[board.getBoardSize() - 1][0].equals(marker)) {
			val++;
		}
		return val;
	}
	
	private int fullEdges(Board board, String marker) {
		boolean fullEdge = true;
		int val = 0;
		int x;
		int y;
		for (x = 0; x < 8; x += 7) {
			for (y = 0; y < board.getBoardSize(); y++) {
				if (!board[x][y].equals(marker)) {
					fullEdge = false;
				}
			}
			if (fullEdge == true) {
				val++;
			}
			fullEdge = true;
		}
		for (y = 0; y < 8; y += 7) {
			for (x = 0; x < board.getBoardSize(); x++) {
				if (!board[x][y].equals(marker)) {
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
	
	private int stableDiscs(Board board, String marker) {
		boolean[][] stable = new boolean[8][8];
		int edge = board.getBoardSize() - 1;
		int x = 0;
		int y = 0;
		int deltaX;
		int deltaY;
		if (board[x][y].equals(marker)) {
			deltaX = 1;
			deltaY = 1;
			do {
				for (i = x + deltaX; x <= edge; x += deltaX) {
					if (board[i][y].equals(marker)) {
						stable[i][y] = true;
					} else {
						break;
					}
				}
				for (i = y + deltaY; y <= edge; y += deltaY) {
					if (board[x][i].equals(marker)) {
						stable[x][i] = true;
					} else {
						break;
					}
				}
				x += 1;
				y += 1;
			} (while board[x][y].equals(marker) && board[x - 1][y].equals(marker) && board[x][y-1].equals(marker));
		}
		if (board[0][edge].equals(marker)) {
			stable[0][edge] = true;
		}
		if (board[edge][edge].equals(marker)) {
			stable[edge][edge] = true;
		}
		if (board[edge][0].equals(marker)) {
			stable[edge][0] = true;
		}
		Find discs that are next to an edge and a stable disc
	}
	
	private void findStableDiscs(boolean[][] stable, x, y, deltaX, deltaY) {
		for (i = x + deltaX; x <= edge; x += deltaX) {
			if (board[i][y].equals(marker)) {
				stable[i][y] = true;
			} else {
				break;
			}
		}
		for (i = y + deltaY; y <= edge; y += deltaY) {
			if (board[x][i].equals(marker)) {
				stable[x][i] = true;
			} else {
				break;
			}
		}
	}
}
