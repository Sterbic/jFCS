package hr.fer.zemris.nenr.fuzzy.command.persist;

import java.util.Arrays;

import hr.fer.zemris.nenr.fuzzy.command.AbstractFuzzyCommand;
import hr.fer.zemris.nenr.fuzzy.domain.IDomain;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngine;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngineException;
import hr.fer.zemris.nenr.fuzzy.set.FuzzySet;

public class PersistSingletonSetCommand extends AbstractFuzzyCommand {

	private String name;
	private String domainName;
	private Object object;

	public PersistSingletonSetCommand(String name, String domainName, Object object) {
		this.name = name;
		this.domainName = domainName;
		this.object = object;
	}

	@Override
	public void runOnEngine(FuzzyEngine engine) throws FuzzyEngineException {
		IDomain domain = engine.getDomains().get(domainName);
		
		FuzzySet old = engine.getSets().get(name);
		double[] membership;

		if(old == null) {
			FuzzySet set = new FuzzySet(name, domain);
			engine.getSets().put(name, set);
			membership = set.getMembership();
		} else {
			if(!domain.equals(old.getDomain())) {
				throw new FuzzyEngineException("The set is defined on a different domain!");
			}
			
			membership = old.getMembership();
		}
		
		int index = domain.getIndexOfElement(object);
		if(index == -1) {
			throw new FuzzyEngineException("The given object is not present in the domain!");
		}
		
		Arrays.fill(membership, 0.0);
		membership[index] = 1.0;
	}
	
}