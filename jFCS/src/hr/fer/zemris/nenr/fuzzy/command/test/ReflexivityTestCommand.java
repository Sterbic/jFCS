package hr.fer.zemris.nenr.fuzzy.command.test;

import hr.fer.zemris.nenr.fuzzy.domain.IDomain;
import hr.fer.zemris.nenr.fuzzy.set.FuzzySet;

public class ReflexivityTestCommand extends AbstractBinaryTestCommand {

	private static final double EPSILON = 1E-9;

	public ReflexivityTestCommand(String setName) {
		super(setName);
	}

	@Override
	public boolean doTest(FuzzySet set, IDomain domain) {
		System.out.print("Is " + set.getName() + " reflexive? ");
		
		for(int i = 0; i < domain.getCardinality(); i++) {
			Object object = domain.elementAt(i)[0];
			
			if(Math.abs(set.getMembershipFor(object, object) - 1.0) > EPSILON) {
				return false;
			}
		}
		
		return true;
	}

}
