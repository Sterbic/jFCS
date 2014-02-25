package hr.fer.zemris.nenr.fuzzy.command.test;

import hr.fer.zemris.nenr.fuzzy.command.IFuzzyCommand;
import hr.fer.zemris.nenr.fuzzy.domain.IDomain;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngine;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngineException;
import hr.fer.zemris.nenr.fuzzy.set.FuzzySet;

public abstract class AbstractBinaryTestCommand implements IFuzzyCommand {
	
	private String setName;
	
	public AbstractBinaryTestCommand(String setName) {
		this.setName = setName;
	}
	
	@Override
	public void run(FuzzyEngine engine) throws FuzzyEngineException {
		FuzzySet set = engine.getSets().get(setName);
		
		if(set == null) {
			throw new FuzzyEngineException("The specified set does not exist in this engine!");
		}
		
		IDomain[] domains = set.getDomain().getDomainComponents();
		
		if(domains.length != 2 || !domains[0].equals(domains[1])) {
			throw new FuzzyEngineException(
					"The test command can only be run on domains of type (d X d)!");
		}
		
		boolean test = doTest(set, domains[0]);
		System.out.println(test ? "YES" : "NO");
	}

	public abstract boolean doTest(FuzzySet set, IDomain domain);

}
