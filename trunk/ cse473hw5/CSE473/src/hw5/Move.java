package hw5;
/**
 * Alex Meng
 * CSE 473 Spring 2007
 * Homework 5, Othello, Move.java
 */

/**
 * This class is designed to encapsulate the following information for a Move object.
 * 
 * Coordinates on the board (x,y), the marker that occupies it, null if no marker occupies it
 * and the value of the move.
 */
public class Move {
	//fields used to represent the x and y coordinate.
	public int x,y;
	
	//the value associated with the move.
	public int value;
	
	//field indicating what marks the move.
	public String marker;
	
	/**
	 * Constructs a move object based on the coordinates, marker, and value passed.
	 * 
	 * @param x an int, the x coordinate of the move.
	 * @param y an int, the y coordinate of the move.
	 * @param marker a String object, representing the marker to occupy the move.
	 * @param value an int, representing the value of the move.
	 */
	public Move(int x, int y, String marker, int value) {
		this.x = x;
		this.y = y;
		this.marker = marker;
		this.value = value;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean xEq = true;
		boolean yEq = true;
		if ( ((Move)obj).x != this.x)
			xEq = false;
		if ( ((Move)obj).y != this.y)
			yEq = false;
		return xEq && yEq;
	}
	@Override
	public String toString() {
		String result = "X: "+ this.x + " Y: " + this.y + " Marked: " + ((this.marker==null) ? "_" : this.marker);
		return result;
	}
	
	
	
}
