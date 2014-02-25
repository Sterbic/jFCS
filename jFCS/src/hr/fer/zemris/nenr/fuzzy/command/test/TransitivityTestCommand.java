package hr.fer.zemris.nenr.fuzzy.command.test;

import hr.fer.zemris.nenr.fuzzy.domain.IDomain;
import hr.fer.zemris.nenr.fuzzy.set.FuzzySet;
import hr.fer.zemris.nenr.fuzzy.set.operator.IBinaryOperator;

public class TransitivityTestCommand extends AbstractBinaryTestCommand {
	
	private IBinaryOperator minOperator;

	public TransitivityTestCommand(String setName, IBinaryOperator minOperator) {
		super(setName);
		this.minOperator = minOperator;
	}

	@Override
	public boolean doTest(FuzzySet set, IDomain domain) {
		System.out.print("Is " + set.getName() + " transitive? ");
		
		for(int i = 0; i < domain.getCardinality(); i++) {
			Object x = domain.elementAt(i)[0];
			
			for(int j = 0; j < domain.getCardinality(); j++) {
				Object z = domain.elementAt(j)[0];
				
				double mxz = set.getMembershipFor(x, z);
				double mxyz = 0.0;
				
				for(int k = 0; k < domain.getCardinality(); k++) {
					Object y = domain.elementAt(k)[0];
					
					double mxy = set.getMembershipFor(x, y);
					double myz = set.getMembershipFor(y, z);
					
					mxyz = Math.max(mxyz, minOperator.operate(mxy, myz));
				}
				
				if(mxz < mxyz) {
					return false;
				}
			}
		}
		
		return true;
	}

}
