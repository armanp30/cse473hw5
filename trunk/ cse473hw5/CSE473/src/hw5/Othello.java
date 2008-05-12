package hw5;
/**
 * Alex Meng
 * CSE 473 Spring 2007
 * Homework 5, Othello, Othello.java
 */
import java.util.ArrayList;
/**
 * This class is designed to be the driver for the Othello game.
 * This constructs the board and players that are necessary in order to play an Othello game.
 * 
 * @author alexmeng
 *
 */
public class Othello {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		ArrayList<Player> players = new ArrayList<Player>();
		
		players.add( new HumanPlayer(Markers.first));
		//players.add( new HumanPlayer(Markers.second));
		players.add(new AIPlayer("W",false));
		
		Board gameBoard = new Board(8);
		
		int turn = 0;
		System.out.println("***********************");
		System.out.println("Let's Play Othello!");
		System.out.println("***********************");
		gameBoard.printBoard();
		
		while ( !gameBoard.isGameOver() ) {
			players.get(turn).makeMove(gameBoard);
			gameBoard.printBoard();
			turn++;
			turn = turn % players.size();
		}
		
		System.out.println("********************");
		System.out.println("Game over!");
		
		String winner = gameBoard.getWinner();
		if ( winner!=null) 			
			System.out.println(winner + " is the winner!");
		else
			System.out.println("The game is a draw!");

	}

}
