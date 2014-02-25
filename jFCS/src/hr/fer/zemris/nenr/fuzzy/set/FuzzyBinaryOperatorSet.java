package hr.fer.zemris.nenr.fuzzy.set;

import hr.fer.zemris.nenr.fuzzy.set.operator.IBinaryOperator;
import hr.fer.zemris.nenr.fuzzy.set.operator.IOperator;

public class FuzzyBinaryOperatorSet extends FuzzySet {
	
	private FuzzySet left;
	private FuzzySet right;
	private IBinaryOperator operator;
	
	public FuzzyBinaryOperatorSet(FuzzySet left, FuzzySet right, IOperator operator) {
		super(left.getName() + operator.getSign() + right.getName(), left.getDomain());		
		
		if(!left.getDomain().equals(right.getDomain())) {
			throw new IllegalArgumentException("Illegal domain combination!");
		}
		
		this.left = left;
		this.right = right;
		this.operator = (IBinaryOperator) operator;

		setMembership(null);
	}
	
	@Override
	public double getMembershipFor(Object... value) {
		double m1 = left.getMembershipFor(value);
		double m2 = right.getMembershipFor(value);
		
		return operator.operate(m1, m2);
	}
	
}
