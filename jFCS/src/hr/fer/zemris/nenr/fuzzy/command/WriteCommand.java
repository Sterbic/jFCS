package hr.fer.zemris.nenr.fuzzy.command;

import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngine;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngineException;
import hr.fer.zemris.nenr.fuzzy.set.FuzzySet;

public class WriteCommand extends AbstractFuzzyCommand {

	private String fuzzySet;
	
	public WriteCommand(String fuzzySet) {
		this.fuzzySet = fuzzySet;
	}
	
	@Override
	public void runOnEngine(FuzzyEngine engine) throws FuzzyEngineException {
		FuzzySet set = engine.getSets().get(fuzzySet);
		
		if(set == null) {
			throw new FuzzyEngineException(
					"The specified fuzzy set does not exist on this engine!");
		}
		
		System.out.println(set.toString());
	}

}
