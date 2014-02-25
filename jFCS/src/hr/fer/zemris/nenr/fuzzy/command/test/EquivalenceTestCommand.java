package hr.fer.zemris.nenr.fuzzy.command.test;

import hr.fer.zemris.nenr.fuzzy.domain.IDomain;
import hr.fer.zemris.nenr.fuzzy.set.FuzzySet;
import hr.fer.zemris.nenr.fuzzy.set.operator.IBinaryOperator;

public class EquivalenceTestCommand extends AbstractBinaryTestCommand {
	
	private IBinaryOperator minOperator;

	public EquivalenceTestCommand(String setName, IBinaryOperator minOperator) {
		super(setName);
		this.minOperator = minOperator;
	}

	@Override
	public boolean doTest(FuzzySet set, IDomain domain) {
		boolean isSymmetric = new SymmetryTestCommand(
				set.getName()).doTest(set, domain);
		
		System.out.println(isSymmetric ? "YES" : "NO");
		
		boolean isReflexive = new ReflexivityTestCommand(
				set.getName()).doTest(set, domain);
		
		System.out.println(isReflexive ? "YES" : "NO");
		
		boolean isTransitive = new TransitivityTestCommand(
				set.getName(), minOperator).doTest(set, domain);
		
		System.out.println(isTransitive ? "YES" : "NO");
		System.out.print("Is " + set.getName() + " an equivalence relation? ");
		
		return isSymmetric && isReflexive && isTransitive;
	}

}
