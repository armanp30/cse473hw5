package hw5;
/**
 * Alex Meng
 * CSE 473 Spring 2007
 * Homework 5, Othello, GameHeuristic.java -- AI class used to make moves for the AI using Minimax & Alpha Pruning.
 */
import java.util.ArrayList;

public class GameHeuristic {

	public static Move SearchMiniMax( Player player, Board board){ 		
		if (player.getMax())
			return maxValue(board);
		else
			return minValue(board);
	}
	
	private static Move maxValue(Board board) {
		Move result = new Move(0,0,null,-2);
		ArrayList<Move> conversions;
		
		ArrayList<Move> childrens = new ArrayList<Move>();//board.getEmptySpots();
		for (int i=0; i<childrens.size(); i++ ) {
			Move temp = childrens.get(i);
			temp.marker = "X";
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
	
	private static Move minValue(Board board) {
		Move result = new Move(0,0,null,2);
		ArrayList<Move> conversions;
		
		ArrayList<Move> childrens = board.getLegalMoves(null); //getEmptySpots();
		for (int i=0; i<childrens.size(); i++ ) {
			Move temp = childrens.get(i);
			temp.marker = "O";
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
	
	public static Move maxValue(Board board, int alpha, int beta) {
		Move result = new Move(0,0,null,-2);
		ArrayList<Move> conversions;
		
		ArrayList<Move> childrens = board.getLegalMoves(null); //getEmptySpots();
		for (int i=0; i<childrens.size(); i++ ) {
			Move temp = childrens.get(i);
			temp.marker = "X";
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
	
	public static Move minValue(Board board, int alpha, int beta) {
		Move result = new Move(0,0,null,2);
		ArrayList<Move> conversions;
		
		ArrayList<Move> childrens = board.getLegalMoves(null);//();
		for (int i=0; i<childrens.size(); i++ ) {
			Move temp = childrens.get(i);
			temp.marker = "O";
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
	
	
}
