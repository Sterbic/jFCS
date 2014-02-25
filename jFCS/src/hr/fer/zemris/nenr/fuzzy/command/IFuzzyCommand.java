package hr.fer.zemris.nenr.fuzzy.command;

import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngine;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngineException;

/**
 * Interface describing a command of the fuzzy engine
 * @author Luka Sterbic
 * @version 0.2
 */
public interface IFuzzyCommand {
	
	/**
	 * Run the command on the given fuzzy engine
	 * @param engine a fuzzy engine
	 * @throws FuzzyEngineException in case of errors
	 */
	public void run(FuzzyEngine engine) throws FuzzyEngineException;

}
