package hr.fer.zemris.nenr.fuzzy.set;

import hr.fer.zemris.nenr.fuzzy.domain.CartesianDomain;
import hr.fer.zemris.nenr.fuzzy.set.operator.IBinaryOperator;
import hr.fer.zemris.nenr.fuzzy.set.operator.IOperator;

public class FuzzyImplicationSet extends FuzzySet {
	
	private FuzzySet left;
	private FuzzySet right;
	private IBinaryOperator operator;
	private int delimiter;
	
	public FuzzyImplicationSet(FuzzySet left, FuzzySet right, IOperator operator) {
		super(
				left.getName() + operator.getSign() + right.getName(),
				new CartesianDomain(left.getDomain(), right.getDomain())
				);
		
		this.left = left;
		this.right = right;
		this.operator = (IBinaryOperator) operator;
		this.delimiter = left.getDomain().getDomainComponents().length;

		setMembership(null);
	}
	
	@Override
	public double getMembershipFor(Object... value) {
		Object[] firstSetObject = new Object[delimiter];
		Object[] secondSetObject = new Object[value.length - delimiter];
		
		System.arraycopy(value, 0, firstSetObject, 0, delimiter);
		System.arraycopy(value, delimiter, secondSetObject, 0, value.length - delimiter);
		
		double m1 = left.getMembershipFor(firstSetObject);
		double m2 = right.getMembershipFor(secondSetObject);
		
		return operator.operate(m1, m2);
	}
	
}
