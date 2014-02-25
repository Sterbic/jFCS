package hr.fer.zemris.nenr.fuzzy.command;

import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngine;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngineException;

/**
 * Abstract implementation of the IFuzzyCommand
 * interface with a check for null engine.
 * @author Luka Sterbic
 * @version 0.2
 */
public abstract class AbstractFuzzyCommand implements IFuzzyCommand {

	@Override
	public void run(FuzzyEngine engine) throws FuzzyEngineException {
		if(engine == null) {
			throw new FuzzyEngineException("This command can't be run on a null engine!");
		}
		
		runOnEngine(engine);
	}

	/**
	 * Run the command on the given fuzzy engine, null safe
	 * @param engine a fuzzy engine
	 * @throws FuzzyEngineException in case of errors
	 */
	public abstract void runOnEngine(FuzzyEngine engine) throws FuzzyEngineException;
	
}
