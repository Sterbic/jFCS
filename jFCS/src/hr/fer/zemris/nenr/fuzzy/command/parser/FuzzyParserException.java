package hr.fer.zemris.nenr.fuzzy.command.parser;

import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngineException;

/**
 * Class modeling an exception that my arise
 * while parsing a fuzzy script file. 
 * @author Luka Sterbic
 * @version 0.1
 */
public class FuzzyParserException extends FuzzyEngineException {
	
	private static final long serialVersionUID = -4905555809162609224L;

	/**
	 * Construktor for ParserException
	 * @param message the error message 
	 */
	public FuzzyParserException(String message) {
		super(message);
	}

}
