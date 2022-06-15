package view;

import java.util.HashMap;
import java.util.Scanner;

import controller.ReversiController;
import customExceptions.ReversiCantPlaceException;

/**
 * 
 * @author Ryan
 * 
 *         Purpose: This class functions as the view for my Reversi program.
 * 
 *         This class is constructed by the Reversi.java class and then runs
 *         here. This is where input is taken and given to the controller. This
 *         is also where all information is printed to the user.
 *
 */
public class ReversiView {
	private ReversiController myController;

	// These exist to translate the user input and computer output into int
	// coordinates.
	private HashMap<Character, Integer> colMap;
	private char[] charCols;

	/**
	 * This is the constructor for the view. It constructs the controller and
	 * also creates a map and an array that keeps track of the letter
	 * representation of the x values of the board that the user will input.
	 */
	public ReversiView() {
		this.myController = new ReversiController();
		this.colMap = new HashMap<Character, Integer>();
		colMap.put('a', 0);
		colMap.put('b', 1);
		colMap.put('c', 2);
		colMap.put('d', 3);
		colMap.put('e', 4);
		colMap.put('f', 5);
		colMap.put('g', 6);
		colMap.put('h', 7);

		this.charCols = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

	}

	/**
	 * This function contains the loop that allows a player to play a full game
	 * against the computer. This is where input is taken and output is printed.
	 */
	public void playGame() {
		Scanner myInput = new Scanner(System.in);

		System.out.println("Welcome to Reversi");
		System.out.println();
		System.out.println("You are W");
		System.out.println();

		displayBoard();
		displayScore();

		while (!(myController.isGameOver())) {

			if (myController.canMove('W')) {
				takeHumanMove(myInput);
				displayBoard();
				displayScore();

			} else {
				myController.humanSkipped();
				System.out.println("No move possible. You have been skipped.");
			}

			if (myController.canMove('B')) {
				makeComputerMove();
				displayBoard();
				displayScore();

			} else {
				myController.computerSkipped();
				System.out.println("No move possible. Computer has skipped.");
			}

		}
		finalScore();
		myInput.close();

	}

	/**
	 * This gets the char[][] representation of the board from the controller,
	 * which gets it from the model. It then prints the board as it currently
	 * stands and adds grid coordinates to the left side and bottom.
	 */
	private void displayBoard() {
		char[][] myBoard = myController.getBoard();
		for (int i = 0; i < 8; i++) {
			System.out.print(i + 1);
			for (int j = 0; j < 8; j++) {
				System.out.print(myBoard[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println(" a b c d e f g h");
		System.out.println();

	}

	/**
	 * This gets the score from the controller and prints it for the player.
	 */
	private void displayScore() {
		int[] score = myController.getScore();
		System.out.print("The score is ");
		System.out.print(score[0]);
		System.out.print('-');
		System.out.print(score[1]);
		System.out.print('.');
		System.out.println();

	}

	/**
	 * This takes input from the user and passes it to the humanTurn(col, row)
	 * method in the controller.
	 * 
	 * Before the data is passed to the controller it is error checked to ensure
	 * it is the right length, with a valid letter as the first item passed and
	 * a valid number as the second item. This also ensures that the input is of
	 * the correct length.
	 * 
	 * @param myInput
	 *            is a scanner instance that takes input from the user.
	 */
	private void takeHumanMove(Scanner myInput) {
		boolean badInput = true;

		while (badInput) {

			System.out.println("Where would you like to place your token? ");
			String move = myInput.next();

			// makes input case insensitive and allows for space on either side
			// of the input.
			move = move.toLowerCase();
			move = move.strip();

			if (move.length() != 2) {
				System.out.println(
						"Invalid move. Ensure your move is one letter and one "
								+ "number, in that order, "
								+ "with no space between.");
				continue;
			}

			char colChar = move.charAt(0);

			// Checks that the first character is a letter that is in the valid
			// input.
			if (!(colMap.containsKey(colChar))) {
				System.out.println("Invalid move. Please ensure your move "
						+ "is in column a-h");
				continue;
			}
			int col = colMap.get(colChar);

			// Checks that the second character is a number in the valid range.
			int row = Character.getNumericValue(move.charAt(1)) - 1;
			if ((row < 0) || (row > 7)) {
				System.out.println("Invalid input. Please ensure your "
						+ "move is in a row from 1-8");
				continue;
			}

			// Feeds good input to the controller. If the input is valid, but is
			// not a legal move a message is printed and the player is allowed
			// to try again.
			try {
				myController.humanTurn(col, row);

			} catch (ReversiCantPlaceException e) {
				System.out.println("That is an invalid move. Try harder.");
				continue;
			}

			badInput = false;
		}

	}

	/**
	 * This makes the computer take its turn using the computerTurn() method in
	 * the controller. It also prints the computer's move for the user to see.
	 */
	private void makeComputerMove() {
		int[] computerMove = myController.computerTurn();
		char col = charCols[computerMove[0]];
		System.out.println();
		System.out.print("The computer places a piece at ");
		System.out.print(col);
		System.out.print(computerMove[1] + 1);
		System.out.println();
	}

	/**
	 * This looks at the final score of the program and determines a winner.
	 */
	private void finalScore() {
		int[] score = myController.getScore();

		if (score[0] > score[1]) {
			System.out.println("You Win!");

		} else if (score[0] < score[1]) {
			System.out.println("You lose. Your father was right about you.");

		} else {
			System.out.println("It's a tie. I guess that's as "
					+ "good as we could hope for you.");
		}

	}

}
