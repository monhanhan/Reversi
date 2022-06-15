package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import controller.ReversiController;
import customExceptions.ReversiCantPlaceException;
import model.ReversiModel;

//TODO block comment for class. 
public class ReversiTests {

	/**
	 * This tests the logic for the user placing a piece down and to the left of
	 * an existing piece.
	 */
	@Test
	void testDownLeft() {
		ReversiModel testModel = new ReversiModel();
		ReversiController testController = new ReversiController(testModel);

		testModel.setAt(3, 4, '_');
		testModel.setAt(4, 4, '_');
		testModel.setAt(3, 3, '_');
		testModel.setAt(5, 2, 'W');
		testModel.setAt(4, 3, 'B');

		try {
			testController.humanTurn(3, 4);
		} catch (ReversiCantPlaceException e) {
			System.out.println("Down left test made an invalid move");
			System.out.println();
		}

		assertEquals(testModel.getAt(4, 3), 'W');
	}

	/**
	 * This tests the logic for a user placing a new piece below an existing
	 * piece.
	 */
	@Test
	void testDown() {
		ReversiModel testModel = new ReversiModel();
		ReversiController testController = new ReversiController(testModel);

		testModel.setAt(3, 4, '_');
		testModel.setAt(4, 4, '_');
		testModel.setAt(4, 3, '_');
		testModel.setAt(3, 3, '_');
		testModel.setAt(5, 2, 'W');
		testModel.setAt(5, 3, 'B');
		testModel.setAt(0, 5, 'W');
		testModel.setAt(0, 6, 'W');
		testModel.setAt(0, 7, 'W');

		try {
			testController.humanTurn(5, 4);
		} catch (ReversiCantPlaceException e) {
			System.out.println("invalid move");
			System.out.println();
		}

	}

	/**
	 * This test checks coverage on the checkRight() helper function and also
	 * ensures placing a new piece to the right of an existing piece works
	 * correctly.
	 */
	@Test
	void testRight() {
		ReversiModel testModel = new ReversiModel();
		ReversiController testController = new ReversiController(testModel);

		// prepares the board so that the loop in ReversiController.checkRight()
		// runs.
		testModel.setAt(3, 4, '_');
		testModel.setAt(4, 4, '_');
		testModel.setAt(4, 3, '_');
		testModel.setAt(3, 3, '_');
		testModel.setAt(4, 3, 'W');
		testModel.setAt(5, 3, 'B');
		testModel.setAt(6, 3, 'B');

		// added to ensure we know when we hit the edge of the board.
		testModel.setAt(7, 7, 'B');
		testModel.setAt(6, 7, 'B');
		testModel.setAt(5, 7, 'W');

		// Added to ensure we know when we have the player's pieces already on
		// either side of the opponent's.
		testModel.setAt(7, 0, 'W');
		testModel.setAt(6, 0, 'B');
		testModel.setAt(5, 0, 'W');
		testModel.setAt(4, 0, 'W');

		try {
			testController.humanTurn(7, 3);
		} catch (ReversiCantPlaceException e) {
			System.out.println("Right made an invalid move");
			System.out.println();
		}

		assertEquals(testModel.getAt(6, 3), 'W');
		assertEquals(testModel.getAt(5, 3), 'W');
	}

	/**
	 * This test checks coverage on the checkLeft() helper function and also
	 * ensures placing a new piece to the left of an existing piece works
	 * correctly.
	 */
	@Test
	void testLeft() {
		ReversiModel testModel = new ReversiModel();
		ReversiController testController = new ReversiController(testModel);

		testModel.setAt(3, 4, '_');
		testModel.setAt(4, 4, '_');
		testModel.setAt(4, 3, '_');
		testModel.setAt(3, 3, '_');
		testModel.setAt(3, 3, 'B');
		testModel.setAt(4, 3, 'B');
		testModel.setAt(5, 3, 'W');

		// Coverage
		testModel.setAt(0, 0, 'W');
		testModel.setAt(1, 0, 'B');
		testModel.setAt(2, 0, 'W');

		testModel.setAt(1, 7, 'B');
		testModel.setAt(2, 7, 'W');

		try {
			testController.humanTurn(2, 3);
		} catch (ReversiCantPlaceException e) {
			System.out.println("Left made an invalid move");
			System.out.println();
		}

		assertEquals(testModel.getAt(4, 3), 'W');
		assertEquals(testModel.getAt(3, 3), 'W');
	}

	/**
	 * This tests the user placing a piece above another piece.
	 */
	@Test
	void testUp() {
		ReversiModel testModel = new ReversiModel();
		ReversiController testController = new ReversiController(testModel);

		testModel.setAt(3, 4, '_');
		testModel.setAt(4, 4, '_');
		testModel.setAt(4, 3, '_');
		testModel.setAt(3, 3, '_');
		testModel.setAt(5, 2, 'B');
		testModel.setAt(5, 3, 'W');

		try {
			testController.humanTurn(5, 1);
		} catch (ReversiCantPlaceException e) {
			System.out.println("Up made an invalid move");
			System.out.println();
		}

		assertEquals(testModel.getAt(5, 2), 'W');
	}

	/**
	 * This test the user placing a piece down and to the right of an existing
	 * piece.
	 */
	@Test
	void testDownRight() {
		ReversiModel testModel = new ReversiModel();
		ReversiController testController = new ReversiController(testModel);

		testModel.setAt(3, 4, '_');
		testModel.setAt(4, 4, 'B');
		testModel.setAt(4, 3, '_');
		testModel.setAt(3, 3, 'W');

		try {
			testController.humanTurn(5, 5);
		} catch (ReversiCantPlaceException e) {
			System.out.println("Up made an invalid move");
			System.out.println();
		}

		assertEquals(testModel.getAt(4, 4), 'W');
	}

	/**
	 * this tests the user placing a piece up and to the right of another piece.
	 */
	@Test
	void testUpRight() {
		ReversiModel testModel = new ReversiModel();
		ReversiController testController = new ReversiController(testModel);

		testModel.setAt(3, 4, 'W');
		testModel.setAt(4, 4, '_');
		testModel.setAt(4, 3, 'B');
		testModel.setAt(3, 3, '_');

		// Coverage
		testModel.setAt(5, 2, 'B');
		testModel.setAt(6, 1, 'B');

		testModel.setAt(6, 0, 'W');
		testModel.setAt(5, 1, 'B');
		testModel.setAt(4, 2, 'W');

		try {
			testController.humanTurn(7, 0);
		} catch (ReversiCantPlaceException e) {
			System.out.println("Up-Right made an invalid move");
			System.out.println();
		}

		assertEquals(testModel.getAt(4, 3), 'W');
		assertEquals(testModel.getAt(5, 2), 'W');
		assertEquals(testModel.getAt(6, 1), 'W');
	}

	/**
	 * This checks the logic for a user placing a piece up and to the left of an
	 * existing piece.
	 */
	@Test
	void testUpLeft() {
		ReversiModel testModel = new ReversiModel();
		ReversiController testController = new ReversiController(testModel);

		testModel.setAt(3, 4, '_');
		testModel.setAt(4, 4, 'W');
		testModel.setAt(4, 3, '_');
		testModel.setAt(3, 3, 'B');

		try {
			testController.humanTurn(2, 2);
		} catch (ReversiCantPlaceException e) {
			System.out.println("Up-Left made an invalid move");
			System.out.println();
		}

		assertEquals(testModel.getAt(3, 3), 'W');
	}

	/**
	 * This checks to ensure the humanTurn() function throws a
	 * ReversiCantPlaceException for bad input.
	 */
	@Test
	void testHumanTurnThrows() {
		ReversiModel testModel = new ReversiModel();
		ReversiController testController = new ReversiController(testModel);

		testModel.setAt(3, 4, 'W');
		testModel.setAt(4, 4, 'B');
		testModel.setAt(4, 3, '_');
		testModel.setAt(3, 3, '_');

		Assertions.assertThrows(ReversiCantPlaceException.class, () -> {
			testController.humanTurn(0, 0);
		});
	}

	/**
	 * This tests the canMove() function.
	 */
	@Test
	void testCanMove() {
		ReversiModel testModel = new ReversiModel();
		ReversiController testController = new ReversiController(testModel);

		for (int i = 1; i < 7; i++) {
			for (int j = 1; j < 7; j++) {
				testModel.setAt(j, i, 'B');
			}
		}

		for (int i = 3; i < 5; i++) {
			for (int j = 3; j < 5; j++) {
				testModel.setAt(j, i, 'W');

			}
		}

		assertFalse(testController.canMove('B'));
		assertTrue(testController.canMove('W'));
	}

	/**
	 * This tests the isGameOver method.
	 */
	@Test
	void testGameOver() {
		ReversiController testController = new ReversiController();

		assertFalse(testController.isGameOver());

		testController.humanSkipped();
		assertFalse(testController.isGameOver());

		testController.computerSkipped();
		assertTrue(testController.isGameOver());

	}

	/**
	 * This tests the logic for the computer taking a turn.
	 */
	@Test
	void testComputerTurn() {
		ReversiModel testModel = new ReversiModel();
		ReversiController testController = new ReversiController(testModel);

		// Set up an empty board.
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				testModel.setAt(j, i, '_');
			}
		}

		// This should set up the computer's best move.
		testModel.setAt(1, 1, 'B');
		testModel.setAt(2, 1, 'W');
		testModel.setAt(3, 1, 'W');

		// This will give the computer a worse move to pick from.
		testModel.setAt(2, 0, 'B');

		// Check the scoring method here, because why not?
		int[] score = testController.getScore();
		assertEquals(score[0], 2);
		assertEquals(score[1], 2);

		int[] chosenMove = testController.computerTurn();

		// ensure the computer has chosen the better move.
		assertEquals(chosenMove[0], 4);
		assertEquals(chosenMove[1], 1);

		// The score should be 0-5 now.
		int[] newScore = testController.getScore();
		assertEquals(newScore[0], 0);
		assertEquals(newScore[1], 5);

		// Set up a new board for branch coverage to test when moves are worth
		// the same.
		testModel.setAt(2, 0, '_');

		for (int i = 1; i < 7; i++) {
			for (int j = 1; j < 7; j++) {
				testModel.setAt(j, i, 'W');
			}
		}

		for (int i = 3; i < 5; i++) {
			for (int j = 3; j < 5; j++) {
				testModel.setAt(j, i, 'B');

			}
		}
		testController.computerTurn();

	}

	/**
	 * This ensures the getBoard method returns an 8x8 board.
	 */
	@Test
	void testGetBoard() {
		ReversiController testController = new ReversiController();
		char[][] myBoard = testController.getBoard();

		// Checks that the board is 8x8.
		assertEquals(myBoard.length, 8);
		for (int i = 0; i < 7; i++) {
			assertEquals(myBoard[i].length, 8);
		}

	}

}
