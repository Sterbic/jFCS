package hr.fer.zemris.nenr.fuzzy.command.test;

import hr.fer.zemris.nenr.fuzzy.domain.IDomain;
import hr.fer.zemris.nenr.fuzzy.set.FuzzySet;

public class SymmetryTestCommand extends AbstractBinaryTestCommand {

	private static final double EPSILON = 1E-6;

	public SymmetryTestCommand(String setName) {
		super(setName);
	}

	@Override
	public boolean doTest(FuzzySet set, IDomain domain) {
		System.out.print("Is " + set.getName() + " symmetric? ");
		
		for(int i = 0; i < domain.getCardinality(); i++) {
			Object firstObject = domain.elementAt(i)[0];
			
			for(int j = i + 1; j < domain.getCardinality(); j++) {
				Object secondObject = domain.elementAt(j)[0];
				
				double mxy = set.getMembershipFor(firstObject, secondObject);
				double myx = set.getMembershipFor(secondObject, firstObject);
				
				if(Math.abs(mxy - myx) > EPSILON) {
					return false;
				}
			}
		}
		
		return true;
	}

}
