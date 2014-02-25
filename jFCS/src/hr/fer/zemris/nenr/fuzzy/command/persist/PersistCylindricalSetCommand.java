package hr.fer.zemris.nenr.fuzzy.command.persist;

import hr.fer.zemris.nenr.fuzzy.command.AbstractFuzzyCommand;
import hr.fer.zemris.nenr.fuzzy.domain.CartesianDomain;
import hr.fer.zemris.nenr.fuzzy.domain.IDomain;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngine;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngineException;
import hr.fer.zemris.nenr.fuzzy.set.FuzzySet;

import java.util.Arrays;

public class PersistCylindricalSetCommand extends AbstractFuzzyCommand {

	private String name;
	private String extendFrom;
	private String extensionDomain;

	public PersistCylindricalSetCommand(String name, String extendFrom,
			String extensionDomain) {
		this.name = name;
		this.extendFrom = extendFrom;
		this.extensionDomain = extensionDomain;
	}
	
	@Override
	public void runOnEngine(FuzzyEngine engine) throws FuzzyEngineException {
		IDomain extDomain = engine.getDomains().get(extensionDomain);
		FuzzySet extSet = engine.getSets().get(extendFrom);
		
		IDomain cylDomain = new CartesianDomain(extSet.getDomain(), extDomain);
		
		FuzzySet set = new FuzzySet(name, cylDomain);
		engine.getSets().put(name, set);
		
		double[] extMembership = extSet.getMembership();
		double[] cylMembership = set.getMembership();
		
		int offest = 0;
		int increment = extDomain.getCardinality();
		
		for(int i = 0; i < extMembership.length; i++) {
			Arrays.fill(cylMembership, offest, offest + increment, extMembership[i]);
			offest += increment;
		}
	}
	
}