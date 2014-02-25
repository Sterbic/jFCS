package hr.fer.zemris.nenr.fuzzy.engine;

/**
 * Class modeling an exception that my arise
 * during the work of a fuzzy engine. 
 * @author Luka Sterbic
 * @version 0.1
 */
public class FuzzyEngineException extends Exception {

	private static final long serialVersionUID = -149223865830997244L;

	/**
	 * Constructor for FuzzyEngineException
	 * @param message the error message 
	 */
	public FuzzyEngineException(String message) {
		super(message);
	}
}
