package model;

/**
 * 
 * @author Ryan Munin
 * 
 *         This is the model for my reversi board. It is only ever called by the
 *         controller, or by testers. The board is represented as an 8x8
 *         char[][] array.
 *
 */
public class ReversiModel {
	private char[][] board;

	/**
	 * This is a constructor for the model
	 */
	public ReversiModel() {
		this.board = new char[8][8];

		// These loops create the board. It is initially blank.
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				this.board[i][j] = '_';
			}
		}
		// This sets the initial positions of the pieces
		this.board[3][3] = 'W';
		this.board[3][4] = 'B';
		this.board[4][3] = 'B';
		this.board[4][4] = 'W';

	}

	/**
	 * This gets the character at a given position on the board.
	 * 
	 * @param x
	 *            is the x coordinate of a position.
	 * @param y
	 *            is the y coordinate of a position
	 * @return is the char at the requested position.
	 */
	public char getAt(int x, int y) {
		return board[y][x];
	}

	/**
	 * This places a piece at a given position on the board.
	 * 
	 * @param x
	 *            is the x position
	 * @param y
	 *            is the y position
	 * @param piece
	 *            is the char to be placed.
	 */
	public void setAt(int x, int y, char piece) {
		board[y][x] = piece;
	}

	/**
	 * This returns the char[][] representation of the board as it currently
	 * stands.
	 * 
	 * @return is the char[][] representation of the board as it currently
	 *         stands.
	 */
	public char[][] getBoard() {
		return board;
	}

}
