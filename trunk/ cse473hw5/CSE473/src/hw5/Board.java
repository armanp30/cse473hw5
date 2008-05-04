package hw5;
/**
 * Alex Meng
 * CSE 473 Spring 2007
 * Homework 5, Othello, Board.java, Class used to define an Othello board.
 */
import java.util.ArrayList;

public class Board {
	
	private final int  SIZE;
	private String[][] board;
	private int moves; 
	
	private String winner;
	
	public Board( int size ) {
		this.SIZE = size;
		this.board = new String[SIZE][SIZE];
		winner = null;
		moves=0;
		setInitialState();
	}
	
	
	private void setInitialState() {
		makeMove("B",5,4);
		makeMove("B",4,5);
		makeMove("W",4,4);
		makeMove("W",5,5);
		
	}
	/**
	 * This method is designed to mark a specific location on the board
	 * given the marker passed in through the parameters.
	 * 
	 * @param marker, A indicator representing the player to occupy the location
	 * @param row	The X coordinate on the board of the location to occupy.
	 * @param col	The Y coordinate on the board of the location to occupy.
	 */
	public void makeMove(String marker, int row, int col) {

		if (this.isGameOver())
			throw new IllegalStateException("The game has ended.");
		
		if (this.board[row-1][col-1]!=null)
			throw new IllegalStateException("Position is occupied.");
		
		//sets the move on the board
		this.board[row-1][col-1] = marker;
		
		//check for winner after move		
		if ( isWinner(row-1, col-1) )
			winner = marker;
		
		moves++;
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
	 * This method is designed to undo a move based on a coordinates
	 * passed.
	 * 
	 * @param x an int, representing the x coordinate. 
	 * @param y an int, representing the y coordinate.
	 */
	public void undoMove(int x, int y) {
		if (isGameOver())
			winner=null;

		this.board[x-1][y-1] = null;
		moves--;
	}

	/**
	 * This generates a list of valid moves to make.
	 * @return
	 */
	public ArrayList<Move> getLegalMoves(Player player) {		
		ArrayList<Move> result = new ArrayList<Move>(); 
		for (int i=0; i<SIZE;i++) {
			for (int k=0; k<SIZE; k++)
				if (board[i][k]==null)
					result.add( new Move(i+1, k+1, null,0) );
		}		
		return result;
	}
	
	public boolean isWinner(int x, int y) {
		String marker = board[x][y];
		
		boolean rowCheck=true, colCheck=true, diagLeftCheck=true, diagRightCheck=true;

		//check vertical
		//check horizontal
		//check diagonal
		
		for (int i=0; i<SIZE; i++) {
			if (board[x][i] == null || !board[x][i].equals(marker))
				rowCheck = false;
			if (board[i][y] == null || !board[i][y].equals(marker))
				colCheck = false;
		}
		
		int k=2;
		//diagonal
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
	
	
	public boolean isGameOver(){
		return (moves>= SIZE*SIZE) || winner != null;
	}
	
	public String getWinner() {
		return winner;
	}
	
	public void printBoard() {
		System.out.println("*************************");
		for (int i=0; i<SIZE;i++) {
			for (int k=0; k<SIZE; k++) 
				if ( board[i][k] != null)
					System.out.print(board[i][k].toString() + " ");
				else
					System.out.print("_ ");
			System.out.println();
		}
		System.out.println("*************************");
	}
}
