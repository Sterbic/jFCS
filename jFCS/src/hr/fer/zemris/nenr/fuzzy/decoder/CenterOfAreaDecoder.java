package hr.fer.zemris.nenr.fuzzy.decoder;

import hr.fer.zemris.nenr.fuzzy.domain.IntegerDomain;
import hr.fer.zemris.nenr.fuzzy.set.FuzzySet;

public class CenterOfAreaDecoder implements IFuzzyDecoder {

	@Override
	public double decode(FuzzySet set) {
		if(!(set.getDomain() instanceof IntegerDomain)) {
			throw new IllegalArgumentException(
					"The COA decoder can work only on integer domains!");
		}
		
		double sumMix = 0.0;
		double sumMi = 0.0;
		
		double[] membership = set.getMembership();
		
		for(int i = 0; i < membership.length; i++) {
			if(membership[i] > 0) {
				Integer x = (Integer) set.getDomain().elementAt(i)[0];
				
				sumMix += membership[i] * x;
				sumMi += membership[i];
			}
		}
		
		return sumMix / sumMi;
	}

}
