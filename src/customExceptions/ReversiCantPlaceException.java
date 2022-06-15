package customExceptions;

/**
 * 
 * @author Ryan Garcia
 * 
 *         This is my custom exception for the Reversi game. It doesn't really
 *         do anything on it's own except to have a fancy name. When it gets
 *         caught in the program it tells the user that they are kinda dumb and
 *         then gives them another chance to make a move.
 *
 */
@SuppressWarnings("serial")
public class ReversiCantPlaceException extends Exception {

	/**
	 * This empty constructor exists mostly for the sake of existing. We have
	 * that in common.
	 */
	public ReversiCantPlaceException() {

	}

}
