package hw5;
/**
 * Alex Meng
 * CSE 473 Spring 2007
 * Homework 5, Othello, Board.java, Class used to define an Othello board.
 */

import java.util.ArrayList;
/**
 * This class is designed to represent the board for Othello.
 * 
 * @author alexmeng
 *
 */
public class Board {
	boolean noMovesP1;
	boolean noMovesP2;
	//final constant representing the size of the board
	private final int  SIZE;
	//a 2D array representing the board
	private String[][] board;
	//a field used to keep track of the number of moves
	private int moves; 
	
	//a string to indicate the winning marker, once a winner is found.
	private String winner;
	
	/**
	 * Constructs a Board object based on the size passed.
	 * 
	 * @param size an int, representing how large to construct the board.
	 */
	public Board( int size ) {
		this.SIZE = size;
		this.board = new String[SIZE][SIZE];
		winner = null;
		moves=0;
		setInitialState();
	}
	
	
	/**
	 * This method is designed to mark a specific location on the board
	 * given the marker passed in through the parameters.
	 * 
	 * @param move	A move to apply to the board
	 */
	public ArrayList<Move> makeMove(Move move) {
		int row = move.x;
		int col = move.y;
		ArrayList<Move> conversions;

		if (this.isGameOver())
			throw new IllegalStateException("The game has ended.");
		
		if (this.board[row-1][col-1]!=null)
			throw new IllegalStateException("Position is occupied.");
		
		conversions = getConversions(move);
		for (Move m: conversions) {
			convert(m);
		}
		board[move.x - 1][move.y - 1] = move.marker;
		
		moves++;
		return conversions;
	}
	
	private void convert(Move move) {
		if (board[move.x - 1][move.y - 1] == Markers.first) {
			board[move.x - 1][move.y - 1] = Markers.second;
		} else {
			board[move.x - 1][move.y - 1] = Markers.first;
		}
	}
	
	/**
	 * getConversions determines what cells should be converted based on
	 * applying the specified move.
	 * 
	 * @param move	The move to check conversion consequences for
	 * @return 	an ArrayList of affected cells
	 * 
	 */
	
	private ArrayList<Move> getConversions(Move move) {
		int checkX;
		int checkY;
		ArrayList<Move> conversions = new ArrayList<Move>();
		ArrayList<Move> temp = new ArrayList<Move>();
		for (int deltaX = -1; deltaX < 2; deltaX++) {
			for (int deltaY = -1; deltaY < 2; deltaY++) {
				checkX = move.x - 1 + deltaX;
				checkY = move.y - 1 + deltaY;
				if (checkX >= 0 && checkX < SIZE && checkY >= 0 && checkY < SIZE &&
						board[checkX][checkY] != null && board[checkX][checkY] != move.marker) {
					
					temp = findConversions(checkX, checkY, deltaX, deltaY, move.marker);
					if (temp != null) {
						conversions.addAll(temp);
					}
				}
			}
		}
		return conversions;
	}
	
	/**
	 * A helper function for getConversions which searches in a direction for
	 * cells which should be converted.
	 * @param x		x coordinate of cell currently being checked
	 * @param y		y coordinate of cell currently being checked
	 * @param deltaX	change in x coordinate for next cell
	 * @param deltaY	change in y coordinate for next cell
	 * @param marker	marker for player whose conversions we're finding
	 * @return 	an ArrayList of Moves indicating cells to be converted
	 */
	
	private ArrayList<Move> findConversions(int x, int y, int deltaX, int deltaY, String marker) {
		ArrayList<Move> conversions = new ArrayList<Move>();
		while (x < SIZE && x >= 0 && y < SIZE && y >= 0) {
			if (board[x][y] == marker) {
				return conversions;
			} else if (board[x][y] == null) {
				return null;
			} else {
				conversions.add(new Move(x + 1, y + 1, marker, 0));
				x += deltaX;
				y += deltaY;
			}
		}
		return null;
	}
	
	/**
	 * This method is designed to undo a move based on a coordinates
	 * passed.
	 * 
	 * @param x an int, representing the x coordinate. 
	 * @param y an int, representing the y coordinate.
	 */
	public void undoMove(Move move, ArrayList<Move> conversions) {
		winner=null;
		for (Move m: conversions) {
			convert(m);
		}
		board[move.x - 1][move.y - 1] = null;
		moves--;
	}

	/**
	 * This generates a list of valid moves to make.
	 * 
	 * @param player, a Player object, representing which player to generate valid moves for.
	 * @return an ArrayList<Move>, represents a collection of Move objects that are valid for the player passed.
	 */
	public ArrayList<Move> getLegalMoves(Player player) {
		int checkX, checkY;
		Move move;
		ArrayList<Move> legalMoves = new ArrayList<Move>();
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (board[x][y] != null && board[x][y].equals(player.getMarker())) {
					for (int deltaX = -1; deltaX < 2; deltaX++) {
						for (int deltaY = -1; deltaY < 2; deltaY++) {
							checkX = x + deltaX;
							checkY = y + deltaY;
							if ( checkX >= 0 && checkX < SIZE && checkY >= 0 &&
									checkY < SIZE && board[checkX][checkY] != null) {
								move = findLegalMove(checkX, checkY, deltaX, deltaY, player.getMarker());
								if (move != null && !legalMoves.contains(move)) {
									legalMoves.add(move);
								}
							}
						}
					}
				}
			}
		}
		if (legalMoves.isEmpty()) {
			if (player.getMarker().equals(Markers.first)) {
				noMovesP1 = true;
			} else {
				noMovesP2 = true;
			}
		} else {
			noMovesP1 = false;
			noMovesP2 = false;
		}
		return legalMoves;
	}

	/**
	 * Explores a direction specified by deltaX and deltaY from an origin
	 * cell searching for a legal move.
	 * @param x		x coordinate of current cell to check
	 * @param y		y coordinate of current cell to check
	 * @param deltaX	change in x coordinate for next cell
	 * @param deltaY	change in y coordinate for next cell
	 * @param player	Player whose moves we're finding
	 * @return	A Move if a legal move is found, else null
	 */

	private Move findLegalMove(int x, int y, int deltaX, int deltaY, String marker) {
		if (board[x][y] == null) {
			return new Move(x + 1, y + 1, marker, 0);
		} else if (board[x][y].equals(marker)) {
			return null;
		} else {
			return findLegalMove(x + deltaX, y + deltaY, deltaX, deltaY, marker);
		}
	}
	
	/**
	 * This method is designed to determine if the last move made
	 * resulted as the winning move. 
	 * 
	 * @param x an int, representing the x coordinate.
	 * @param y an int, representing the y coordinate.
	 * 
	 * @return a boolean, true, if the last move was a winning move. false, otherwise.
	 */
	public boolean isWinner(int x, int y) {
		String marker = board[x][y];
		
		boolean rowCheck=true, colCheck=true, diagLeftCheck=true, diagRightCheck=true;

		//check vertical
		//check horizontal				
		for (int i=0; i<SIZE; i++) {
			if (board[x][i] == null || !board[x][i].equals(marker))
				rowCheck = false;
			if (board[i][y] == null || !board[i][y].equals(marker))
				colCheck = false;
		}
		
		int k=2;
		//check diagonal 
		for (int i=0; i<=SIZE*SIZE; i+=SIZE+1) {
			int tempX = i % SIZE;
			int tempY = i / SIZE;
			int tempOtherX = k % SIZE;
			int tempOtherY = k / SIZE;
			if (board[tempX][tempY] == null || !board[tempX][tempY].equals( marker))
				diagLeftCheck = false;
			if (board[tempOtherX][tempOtherY] == null || !board[tempOtherX][tempOtherY].equals(marker))
				diagRightCheck = false;
			k+=(SIZE-1);
		}
		return rowCheck || colCheck || diagLeftCheck || diagRightCheck;
	}
	
	/**
	 * This method is designed to return to the client
	 * the size of the board.
	 * 
	 * @return an int, representing the size of the board.
	 */
	public int getBoardSize() {
		return this.SIZE;
	}
	
	/**
	 * This method is used to determine who is currently leading the
	 * board with the most occupied cells. 
	 * 
	 * @return an int, representing how many cells are occupied. A positive result means B, a negative means W.
	 */
	public int getMajority() {
		int b=0, w=0;
		
		for (int i=0; i<this.SIZE;i++) {
			for (int j=0; j<this.SIZE; j++) {
				if ( this.board[i][j].equals(Markers.first) )
					b++;
				if ( this.board[i][j].equals(Markers.second) )
					w--;					
			}
		}
		
		if (Math.abs(w) > b)
			return w;
		else
			return b;
	}
	
	/**
	 * This checks if the board is in a state where the game 
	 * has ended.
	 * 
	 * @return a boolean: true, if the game has ended. false, otherwise.
	 */
	public boolean isGameOver(){
		return noMovesP1 && noMovesP2;
	}
	
	/**
	 * Returns the marker of the winning player.
	 * 
	 * @return a String object, representing the marker of the victorious player.
	 */
	public String getWinner() {
		return winner;
	}
	
	/**
	 * A method used to print to the console the current board state.
	 * 
	 * @return void
	 */
	public void printBoard() {
		System.out.println("*************************");
		System.out.println("  1 2 3 4 5 6 7 8");
		for (int i=0; i<SIZE;i++) {
			System.out.print((i + 1) + " ");
			for (int k=0; k<SIZE; k++) 
				if ( board[i][k] != null)
					System.out.print(board[i][k].toString() + " ");
				else
					System.out.print("_ ");
			System.out.println();
		}
		System.out.println("*************************");
	}
	
	/**
	 * An internal method which sets the initial state of the board.
	 * By calling the makeMove method.
	 * 
	 * @return void
	 */
	private void setInitialState() {
		makeMove(new Move(5,4,Markers.first,0));
		makeMove(new Move(4,5,Markers.first,0));
		makeMove(new Move(4,4,Markers.second,0));
		makeMove(new Move(5,5,Markers.second,0));
	}
	
}
