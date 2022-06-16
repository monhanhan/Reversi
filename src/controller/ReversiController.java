package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import customExceptions.ReversiCantPlaceException;
import model.ReversiModel;

/**
 * 
 * @author Ryan Munin
 * 
 *         purpose: This is the controller for the Reversi game. It takes input
 *         from the view and returns information to it. It also modifies the
 *         ReversiModel class using the getters and setters in that class.
 *
 */
public class ReversiController {
	private ReversiModel myModel;
	private boolean humanSkip;
	private boolean computerSkip;

	/**
	 * This is a constructor used for actually running the program. It
	 * constructs it's own ReversiModel object when it is constructed so that
	 * the view never has to interact with the model.
	 */
	public ReversiController() {
		this.myModel = new ReversiModel();
		this.humanSkip = false;
		this.computerSkip = false;

	}

	/**
	 * This is a constructor used only for testing.
	 * 
	 * This is useful because it allows us to create a ReversiModel separately
	 * from the controller and set up the board in ways that otherwise might not
	 * be possible and hand that setup to the controller.
	 * 
	 * @param myModel
	 *            is a ReversiModel object.
	 */
	public ReversiController(ReversiModel myModel) {
		this.myModel = myModel;
		this.humanSkip = false;
		this.computerSkip = false;
	}

	/**
	 * If the human and the computer both don't have a move, the game is over.
	 * 
	 * @return is a boolean representing whether the game has ended.
	 */
	public boolean isGameOver() {
		return (humanSkip && computerSkip);
	}

	/**
	 * This checks each position on the board and adds up the score of each
	 * player.
	 * 
	 * @return is an int[] representation of the scores of each player.
	 */
	public int[] getScore() {
		int[] score = new int[2];
		score[0] = 0;
		score[1] = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (myModel.getAt(i, j) == 'W') {
					score[0]++;
				} else if (myModel.getAt(i, j) == 'B') {
					score[1]++;
				}
			}
		}
		return score;
	}

	/**
	 * This is a method for the controller to hand a representation of the board
	 * to the view so that the view never has to interact directly with the
	 * model.
	 * 
	 * @return is a char[][] representation of the board.
	 */
	public char[][] getBoard() {
		return myModel.getBoard();
	}

	/**
	 * This is a setter the view can use if the human has skipped its turn.
	 */
	public void humanSkipped() {
		humanSkip = true;
	}

	/**
	 * This is a setter that the view can use if the computer has skipped its
	 * turn.
	 */
	public void computerSkipped() {
		computerSkip = true;
	}

	/**
	 * This checks if the current player has a valid move to make.
	 * 
	 * @param playerChar
	 *            is the char representing the pieces belonging to the current
	 *            player.
	 * @return is a boolean representing whether a player has a valid move to
	 *         make.
	 */
	public boolean canMove(char playerChar) {
		boolean canMove = !(findValidMoves(playerChar).isEmpty());
		return canMove;

	}

	/**
	 * This takes user input from the view and uses it for the player to make a
	 * move.
	 * 
	 * The player's input is fed in from the ReversiView and checked against the
	 * HashMap of valid moves. If the chosen move is not on the HashMap the
	 * player will be asked to choose again in the view.
	 * 
	 * @param row
	 *            is the int representation of the Y position the player has
	 *            chosen.
	 * @param col
	 *            is the int representation of the X position the player has
	 *            chosen.
	 * @throws ReversiCantPlaceException
	 *             This exception is thrown when the human has chosen an invalid
	 *             move.
	 */
	public void humanTurn(int row, int col) throws ReversiCantPlaceException {
		HashMap<ArrayList<Integer>, ArrayList<int[]>> movesMap = findValidMoves(
				'W');

		ArrayList<Integer> playerMove = new ArrayList<Integer>();
		playerMove.add(col);
		playerMove.add(row);

		if (!(movesMap.containsKey(playerMove))) {
			throw new ReversiCantPlaceException();

		} else {
			makeMoves(movesMap.get(playerMove), 'W');
			humanSkip = false;
		}

	}

	/**
	 * This is where the computer makes its move.
	 * 
	 * @return is an int[] of length 2 that represents the move the computer has
	 *         chosen.
	 */
	public int[] computerTurn() {
		ArrayList<int[]> bestMoves = chooseBestMove();
		makeMoves(bestMoves, 'B');

		int[] chosenMove = new int[2];

		// Row position of the move set the computer has chosen.
		chosenMove[0] = bestMoves.get(0)[2];

		// col position of the move set the computer has chosen.
		chosenMove[1] = bestMoves.get(0)[3];

		computerSkip = false;
		return chosenMove;
	}

	/**
	 * This is a helper method that allows the computer to pick the best move.
	 * 
	 * This method checks each move in the HashMap and adds up how many pieces
	 * will be captured and picks the move that will capture the most pieces. If
	 * two moves are of equal value, a move is chosen at random.
	 * 
	 * @return is an ArrayList<int[]> that represents the best move.
	 */
	private ArrayList<int[]> chooseBestMove() {
		HashMap<ArrayList<Integer>, ArrayList<int[]>> movesMap = findValidMoves(
				'B');
		ArrayList<Integer> bestKey = new ArrayList<Integer>();
		int currMax = 0;

		for (ArrayList<Integer> key : movesMap.keySet()) {
			int currTotal = 0;
			ArrayList<int[]> currMoves = movesMap.get(key);
			for (int[] move : currMoves) {
				currTotal += move[4];
			}
			if (currTotal > currMax) {
				currMax = currTotal;
				bestKey = key;

			} else if (currTotal == currMax) {
				// Randomly chooses between two moves of the same quality.
				int coinFlip = ThreadLocalRandom.current().nextInt(0, 2);
				if (coinFlip == 0) {
					currMax = currTotal;
					bestKey = key;
				}

			}

		}

		return movesMap.get(bestKey);

	}

	/**
	 * This executes a valid move from either the player or the computer.
	 * 
	 * The game will have created a list of valid moves from which the player or
	 * computer will choose. the moves are actually stored as an array list what
	 * will keep track of all the pieces that will be captured if a move is
	 * carried out. This goes through and executes each of those actions.
	 * 
	 * @param moves
	 *            is a list of all directions where pieces will be captured.
	 * @param playerChar
	 *            is the character of the player who's turn it is.
	 */
	private void makeMoves(ArrayList<int[]> moves, char playerChar) {
		for (int[] currMove : moves) {
			int currX = currMove[0];
			int currY = currMove[1];
			int endX = currMove[2];
			int endY = currMove[3];

			if (currMove[5] == 0) {
				// upward move
				while (currY >= endY) {
					myModel.setAt(currX, currY, playerChar);
					currY--;
				}

			} else if (currMove[5] == 1) {
				// downward move
				while (currY <= endY) {
					myModel.setAt(currX, currY, playerChar);
					currY++;
				}

			} else if (currMove[5] == 2) {
				// right move
				while (currX <= endX) {
					myModel.setAt(currX, currY, playerChar);
					currX++;
				}

			} else if (currMove[5] == 3) {
				// left move
				while (currX >= endX) {
					myModel.setAt(currX, currY, playerChar);
					currX--;
				}

			} else if (currMove[5] == 4) {
				// down-right
				while (currX <= endX) {
					myModel.setAt(currX, currY, playerChar);
					currX++;
					currY++;
				}

			} else if (currMove[5] == 5) {
				// up-left
				while (currX >= endX) {
					myModel.setAt(currX, currY, playerChar);
					currX--;
					currY--;
				}

			} else if (currMove[5] == 6) {
				// up-right
				while (currX <= endX) {
					myModel.setAt(currX, currY, playerChar);
					currX++;
					currY--;
				}

			} else {
				// down-left
				while (currY <= endY) {
					myModel.setAt(currX, currY, playerChar);
					currX--;
					currY++;
				}

			}
		}
	}

	/**
	 * This creates a list of valid moves for the current player.
	 * 
	 * First, each position on the board is checked to see if there is a piece
	 * belonging to the player at that position. If so, each space in every
	 * direction is checked to see if a legal move can be made. All legal moves
	 * found are added to a HashMap with the placement position of the piece as
	 * the key.
	 * 
	 * @param playerChar
	 *            is the color piece belonging to the current player
	 * @return is a HashMap<ArrayList<Integer>, ArrayList<int[]>> that maps the
	 *         placement position of each valid move with the end point,
	 *         direction and value of all captured pieces starting from that
	 *         position.
	 */
	private HashMap<ArrayList<Integer>, ArrayList<int[]>> findValidMoves(
			char playerChar) {

		HashMap<ArrayList<Integer>, ArrayList<int[]>> moveMap =

				new HashMap<ArrayList<Integer>, ArrayList<int[]>>();

		// This checks for possible moves using a player's piece as a starting
		// point and adds the results to a list of moves.
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				ArrayList<int[]> tempList = new ArrayList<int[]>();
				if (myModel.getAt(i, j) == playerChar) {
					tempList.add(checkUp(i, j, playerChar));
					tempList.add(checkDown(i, j, playerChar));
					tempList.add(checkLeft(i, j, playerChar));
					tempList.add(checkRight(i, j, playerChar));
					tempList.add(checkDownRightDiag(i, j, playerChar));
					tempList.add(checkDownLeftDiag(i, j, playerChar));
					tempList.add(checkUpRightDiag(i, j, playerChar));
					tempList.add(checkUpLeftDiag(i, j, playerChar));

					// This takes every move on the move list and adds them to a
					// HashMap of possible moves based on the actual location a
					// player would place a piece. This is used later to check
					// if a player's moves are valid and for the AI to pick it's
					// moves from.
					for (int[] move : tempList) {
						if (move != null) {
							ArrayList<Integer> pos = new ArrayList<Integer>();
							pos.add(move[3]);
							pos.add(move[2]);
							if (moveMap.containsKey(pos)) {
								moveMap.get(pos).add(move);
							} else {
								ArrayList<int[]> innerList = new ArrayList<int[]>();
								moveMap.put(pos, innerList);
								moveMap.get(pos).add(move);
							}
						}
					}
				}

			}
		}

		return moveMap;

	}

	/**
	 * This method is a helper method that checks for a valid move in the up
	 * direction.
	 * 
	 * @param startX
	 *            is the x location where a player has a piece.
	 * @param startY
	 *            is the y location where a player has a piece.
	 * @param playerChar
	 *            is the character of the current player
	 * @return is an array of length 6 that consists of startX, startY, endX,
	 *         endY, score, and a flag to tell me which direction the move was
	 *         in in that order.
	 */
	private int[] checkUp(int startX, int startY, char playerChar) {
		if (startY < 2) {
			return null; // It is impossible to make a legal move if there is
							// only one blank space above one of your pieces.

		} else if ((myModel.getAt(startX, startY - 1) == '_')
				|| (myModel.getAt(startX, startY - 1) == playerChar)) {
			return null;// It is impossible to make a legal move if you can't
						// convert a piece.
		}

		int[] move = new int[6];
		int score = 1;
		int endX = startX;
		int endY = startY;

		// Start at startY - 2 since we have checked everything before it above.
		for (int i = startY - 2; i >= 0; i--) {
			char thisChar = myModel.getAt(startX, i);

			// return null if we hit a piece belonging to the current player
			// or we run out of board.
			if ((thisChar == playerChar) || ((i == 0) && (thisChar != '_'))) {
				return null;

			} else if (thisChar == '_') {
				endY = i;
				break;

			} else {
				score++;
			}

		}
		move[0] = startX;
		move[1] = startY;
		move[2] = endX;
		move[3] = endY;
		move[4] = score;

		// flag to let me know this was an upward move.
		move[5] = 0;

		return move;
	}

	/**
	 * This method is a helper method that checks for a valid move in the down
	 * direction.
	 * 
	 * @param startX
	 *            is the x location where a player has a piece.
	 * @param startY
	 *            is the y location where a player has a piece.
	 * @param playerChar
	 *            is the character of the current player
	 * @return is an array of length 6 that consists of startX, startY, endX,
	 *         endY, score, and a flag to tell me which direction the move was
	 *         in in that order.
	 */
	private int[] checkDown(int startX, int startY, char playerChar) {
		if (startY > 5) {
			return null; // It is impossible to make a legal move if there is
							// only one blank space below one of your pieces.

		} else if ((myModel.getAt(startX, startY + 1) == '_')
				|| (myModel.getAt(startX, startY + 1) == playerChar)) {
			return null;// It is impossible to make a legal move if you can't
						// convert a piece.
		}

		// Valid moves are found and returned as arrays with the elements being
		// 0: startX 1: startY 2:endX 3: endY 4: score
		int[] move = new int[6];
		int score = 1;
		int endX = startX;
		int endY = startY;

		// Start at startY + 2 since we have checked
		// everything before it above.
		for (int i = startY + 2; i < 8; i++) {
			char thisChar = myModel.getAt(startX, i);

			// return null if we hit a piece belonging to the current player
			// or we run out of board.
			if ((thisChar == playerChar) || ((i == 7) && (thisChar != '_'))) {
				return null;

			} else if (thisChar == '_') {
				endY = i;
				break;

			} else {
				score++;
			}

		}
		move[0] = startX;
		move[1] = startY;
		move[2] = endX;
		move[3] = endY;
		move[4] = score;

		// flag for downward move
		move[5] = 1;

		return move;
	}

	/**
	 * This method is a helper method that checks for a valid move in the right
	 * direction.
	 * 
	 * @param startX
	 *            is the x location where a player has a piece.
	 * @param startY
	 *            is the y location where a player has a piece.
	 * @param playerChar
	 *            is the character of the current player
	 * @return is an array of length 6 that consists of startX, startY, endX,
	 *         endY, score, and a flag to tell me which direction the move was
	 *         in in that order.
	 */
	private int[] checkRight(int startX, int startY, char playerChar) {
		if (startX > 5) {
			return null;

		} else if ((myModel.getAt(startX + 1, startY) == '_')
				|| (myModel.getAt(startX + 1, startY) == playerChar)) {
			return null;// It is impossible to make a legal move if you can't
						// convert a piece.
		}

		int[] move = new int[6];
		int score = 1;
		int endX = startX;
		int endY = startY;

		// Start at startY - 2 since we have checked everything before it above.
		int i = startX + 2;

		// for (int i = startX + 2; i <= 7; i++) {
		while (i <= 7) {
			char thisChar = myModel.getAt(i, startY);

			// return null if we hit a piece belonging to the current player
			// or we run out of board.
			if ((thisChar == playerChar) || ((i == 7) && (thisChar != '_'))) {
				return null;

			} else if (thisChar == '_') {
				endX = i;
				i = 8;
				// break;

			} else {
				score++;
				i++;
			}

		}
		move[0] = startX;
		move[1] = startY;
		move[2] = endX;
		move[3] = endY;
		move[4] = score;

		// flag for right move
		move[5] = 2;

		return move;
	}

	/**
	 * This method is a helper method that checks for a valid move in the left
	 * direction.
	 * 
	 * @param startX
	 *            is the x location where a player has a piece.
	 * @param startY
	 *            is the y location where a player has a piece.
	 * @param playerChar
	 *            is the character of the current player
	 * @return is an array of length 6 that consists of startX, startY, endX,
	 *         endY, score, and a flag to tell me which direction the move was
	 *         in in that order.
	 */
	private int[] checkLeft(int startX, int startY, char playerChar) {
		if (startX < 2) {
			return null;

		} else if ((myModel.getAt(startX - 1, startY) == '_')
				|| (myModel.getAt(startX - 1, startY) == playerChar)) {
			return null;// It is impossible to make a legal move if you can't
						// convert a piece.
		}

		int[] move = new int[6];
		int score = 1;
		int endX = startX;
		int endY = startY;

		// Start at startY - 2 since we have checked everything before it above
		int i = startX - 2;

		while (i >= 0) {
			char thisChar = myModel.getAt(i, startY);

			// return null if we hit a piece belonging to the current player
			// or we run out of board.
			if ((thisChar == playerChar) || ((i == 0) && (thisChar != '_'))) {
				return null;

			} else if (thisChar == '_') {
				endX = i;
				i = -1;

			} else {
				score++;
				i--;
			}

		}
		move[0] = startX;
		move[1] = startY;
		move[2] = endX;
		move[3] = endY;
		move[4] = score;

		// flag for left move
		move[5] = 3;

		return move;
	}

	/**
	 * This method is a helper method that checks for a valid move in the
	 * down-right diagonal direction.
	 * 
	 * @param startX
	 *            is the x location where a player has a piece.
	 * @param startY
	 *            is the y location where a player has a piece.
	 * @param playerChar
	 *            is the character of the current player
	 * @return is an array of length 6 that consists of startX, startY, endX,
	 *         endY, score, and a flag to tell me which direction the move was
	 *         in in that order.
	 */
	private int[] checkDownRightDiag(int startX, int startY, char playerChar) {
		int farthest = Math.max(startX, startY);

		if (farthest > 5) {
			return null;

		} else if ((myModel.getAt(startX + 1, startY + 1) == '_')
				|| (myModel.getAt(startX + 1, startY + 1) == playerChar)) {
			return null;// It is impossible to make a legal move if you can't
						// convert a piece.
		}

		int[] move = new int[6];
		int score = 1;
		int endX = startX;
		int endY = startY;

		for (int i = 2; i < 8 - farthest; i++) {
			char thisChar = myModel.getAt(startX + i, startY + i);

			if ((thisChar == playerChar)
					|| ((i == 7 - farthest) && (thisChar != '_'))) {
				return null;

			} else if (thisChar == '_') {
				endX = startX + i;
				endY = startY + i;
				break;

			} else {
				score++;
			}

		}
		move[0] = startX;
		move[1] = startY;
		move[2] = endX;
		move[3] = endY;
		move[4] = score;

		// flag for down right diagonal
		move[5] = 4;

		return move;
	}

	/**
	 * This method is a helper method that checks for a valid move in the
	 * up-left diagonal direction.
	 * 
	 * @param startX
	 *            is the x location where a player has a piece.
	 * @param startY
	 *            is the y location where a player has a piece.
	 * @param playerChar
	 *            is the character of the current player
	 * @return is an array of length 6 that consists of startX, startY, endX,
	 *         endY, score, and a flag to tell me which direction the move was
	 *         in in that order.
	 */
	private int[] checkUpLeftDiag(int startX, int startY, char playerChar) {
		if ((startY < 2) || (startX < 2)) {
			return null;

		} else if ((myModel.getAt(startX - 1, startY - 1) == '_')
				|| (myModel.getAt(startX - 1, startY - 1) == playerChar)) {
			return null;// It is impossible to make a legal move if you can't
						// convert a piece.
		}

		int[] move = new int[6];
		int score = 1;
		int endX = startX;
		int endY = startY;

		int i = 2;

		while ((startX - i >= 0) && (startY - i >= 0)) {
			char thisChar = myModel.getAt(startX - i, startY - i);

			// This if block checks to see if the player will hit it's own
			// piece or the edge of the board.
			if ((thisChar == playerChar)
					|| (((startX - i == 0) || (startY - i == 0))
							&& (thisChar != '_'))) {
				return null;

			} else if (thisChar == '_') {
				endX = startX - i;
				endY = startY - i;
				break;

			} else {
				score++;
				i++;
			}

		}
		move[0] = startX;
		move[1] = startY;
		move[2] = endX;
		move[3] = endY;
		move[4] = score;

		// flag for up left diagonal
		move[5] = 5;

		return move;
	}

	/**
	 * This method is a helper method that checks for a valid move in the
	 * up-right diagonal direction.
	 * 
	 * @param startX
	 *            is the x location where a player has a piece.
	 * @param startY
	 *            is the y location where a player has a piece.
	 * @param playerChar
	 *            is the character of the current player
	 * @return is an array of length 6 that consists of startX, startY, endX,
	 *         endY, score, and a flag to tell me which direction the move was
	 *         in in that order.
	 */
	private int[] checkUpRightDiag(int startX, int startY, char playerChar) {
		if ((startY < 2) || (startX > 5)) {
			return null;

		} else if ((myModel.getAt(startX + 1, startY - 1) == '_')
				|| (myModel.getAt(startX + 1, startY - 1) == playerChar)) {
			return null;// It is impossible to make a legal move if you can't
						// convert a piece.
		}

		int[] move = new int[6];
		int score = 1;
		int endX = startX;
		int endY = startY;

		int i = 2;

		while ((startX + i < 8) && (startY - i >= 0)) {
			char thisChar = myModel.getAt(startX + i, startY - i);

			// This if block checks to see if the player will hit it's own
			// piece or the edge of the board.
			if ((thisChar == playerChar)
					|| (((startX + i == 7) || (startY - i == 0))
							&& (thisChar != '_'))) {
				return null;

			} else if (thisChar == '_') {
				endX = startX + i;
				endY = startY - i;
				i = 100;

			} else {
				score++;
				i++;
			}

		}
		move[0] = startX;
		move[1] = startY;
		move[2] = endX;
		move[3] = endY;
		move[4] = score;

		// flag for up right diagonal
		move[5] = 6;

		return move;
	}

	/**
	 * This method is a helper method that checks for a valid move in the
	 * down-left diagonal direction.
	 * 
	 * @param startX
	 *            is the x location where a player has a piece.
	 * @param startY
	 *            is the y location where a player has a piece.
	 * @param playerChar
	 *            is the character of the current player
	 * @return is an array of length 6 that consists of startX, startY, endX,
	 *         endY, score, and a flag to tell me which direction the move was
	 *         in in that order.
	 */
	private int[] checkDownLeftDiag(int startX, int startY, char playerChar) {
		if ((startX < 2) || (startY > 5)) {
			return null;

		} else if ((myModel.getAt(startX - 1, startY + 1) == '_')
				|| (myModel.getAt(startX - 1, startY + 1) == playerChar)) {
			return null;// It is impossible to make a legal move if you can't
						// convert a piece.
		}

		int[] move = new int[6];
		int score = 1;
		int endX = startX;
		int endY = startY;

		int i = 2;

		while ((startY + i < 8) && (startX - i >= 0)) {
			char thisChar = myModel.getAt(startX - i, startY + i);

			// This if block checks to see if the player will hit it's own
			// piece or the edge of the board.
			if ((thisChar == playerChar)
					|| (((startY + i == 7) || (startX - i == 0))
							&& (thisChar != '_'))) {
				return null;

			} else if (thisChar == '_') {
				endX = startX - i;
				endY = startY + i;
				break;

			} else {
				score++;
				i++;
			}

		}
		move[0] = startX;
		move[1] = startY;
		move[2] = endX;
		move[3] = endY;
		move[4] = score;

		// flag for down left diagonal
		move[5] = 7;

		return move;
	}

}
