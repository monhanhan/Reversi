import view.ReversiView;

/**
 * 
 * @author Ryan Garcia
 * 
 *         This is the Reversi program.
 * 
 *         Purpose: Fun is its own reward.
 * 
 *         Usage: when prompted, place a piece by typing the xy coordinates of
 *         where you would like to move. The computer will then take a turn.
 *         Play will continue until neither player can make any moves, at which
 *         time a winner will be determined.
 *
 */
public class Reversi {

	/**
	 * This is main. It makes the program go.
	 * 
	 * @param args
	 *            is a command line argument split on spaces. It is unused here.
	 */
	public static void main(String[] args) {
		ReversiView myView = new ReversiView();
		myView.playGame();

	}

}
