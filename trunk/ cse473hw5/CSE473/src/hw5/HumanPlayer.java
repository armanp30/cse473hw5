package hw5;
/**
 * Alex Meng
 * CSE 473 Spring 2007
 * Homework 5, Othello, Humanplayer.java, Class used to define the state and behavior of a Human Player.
 */
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is designed to represent the Human player
 * in a board game in Othello.
 * 
 * @author alexmeng
 *
 */
public class HumanPlayer implements Player {
	//represents as the marker for this player on the board
	private String marker; 
	
	//the interface in which this class interacts with the human user.
	private Scanner console;
	
	/**
	 * Constructs a Human player object based on the decided marker.
	 * 
	 * @param mark A string which indicates the player on board.
	 */
	public HumanPlayer(String mark){
		this.marker = mark;
		//Sets the console input as the interface which interacts with the user
		console= new Scanner( System.in);
	}
	
	/**
	 * This determines if the player makes the first move. This 
	 * method is specifically designed for the minimax/alpha beta pruning algorthim.
	 */
	public boolean getMax() {
		return marker.equals("B");
	}
	
	public String getMarker() {
		return this.marker;
	}
	
	public void makeMove(Board state) {
		ArrayList<Move> legalMoves = state.getLegalMoves(this);
		Move temp;
		do {
			System.out.println("Player " + getMarker());
			System.out.println("Please select a position [0,"+state.getBoardSize() + "] using the format: x,y");
			String[] grid = console.nextLine().split(",");
			temp = new Move(Integer.parseInt(grid[0]),Integer.parseInt(grid[1]),this.getMarker(),0);
			if (!legalMoves.contains(temp))
					System.out.println("**Invalid Move**");
		} while (!legalMoves.contains(temp));
		
		state.makeMove(temp.marker, temp.x, temp.y);
	}
}
