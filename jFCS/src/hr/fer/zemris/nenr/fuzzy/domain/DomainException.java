package hr.fer.zemris.nenr.fuzzy.domain;

/**
 * Class representing a runtime exception which
 * may arise while working with domains.
 * @author Luka Sterbic
 * @version 0.1
 */
public class DomainException extends RuntimeException {

	private static final long serialVersionUID = 6135066898304406678L;

	/**
	 * Constructor for DomainException
	 * @param message the error message 
	 */
	public DomainException(String message) {
		super(message);
	}
	
}
