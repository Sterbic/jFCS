package hr.fer.zemris.nenr.fuzzy.set;

import hr.fer.zemris.nenr.fuzzy.set.operator.INegation;
import hr.fer.zemris.nenr.fuzzy.set.operator.IOperator;

public class FuzzyNegatedSet extends FuzzySet {
	
	private FuzzySet original;
	private INegation negation;
	
	public FuzzyNegatedSet(FuzzySet original, IOperator negation) {
		super(negation.getSign() + original.getName(), original.getDomain());		

		this.original = original;
		this.negation = (INegation) negation;
		
		setMembership(null);
	}
	
	@Override
	public double getMembershipFor(Object... value) {
		double m = original.getMembershipFor(value);
		return negation.negate(m);
	}
	
}
