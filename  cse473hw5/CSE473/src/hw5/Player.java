package hw5;
/**
 * Alex Meng
 * CSE 473 Spring 2007
 * Homework 4, Tic Tac Toe, Minimax & Alpha Pruning
 */
/**
 * This is an interface used to define the expected behaviors
 * for players.
 */
public interface Player {
	public void makeMove(Board state);
	public String getMarker();
	public boolean getMax();
}
