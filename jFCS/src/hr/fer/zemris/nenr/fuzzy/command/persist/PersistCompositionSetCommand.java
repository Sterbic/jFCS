package hr.fer.zemris.nenr.fuzzy.command.persist;

import hr.fer.zemris.nenr.fuzzy.command.AbstractFuzzyCommand;
import hr.fer.zemris.nenr.fuzzy.domain.CartesianDomain;
import hr.fer.zemris.nenr.fuzzy.domain.IDomain;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngine;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngineException;
import hr.fer.zemris.nenr.fuzzy.set.FuzzySet;
import hr.fer.zemris.nenr.fuzzy.set.operator.IBinaryOperator;

public class PersistCompositionSetCommand extends AbstractFuzzyCommand {

	private String name;
	private String firstSetName;
	private String secondSetName;
	private IBinaryOperator minOperator;
	
	public PersistCompositionSetCommand(String name, String firstSetName,
			String secondSetName, IBinaryOperator minOperator) {
		this.name = name;
		this.firstSetName = firstSetName;
		this.secondSetName = secondSetName;
		this.minOperator = minOperator;
	}
	
	@Override
	public void runOnEngine(FuzzyEngine engine) throws FuzzyEngineException {
		FuzzySet firstSet = engine.getSets().get(firstSetName);
		FuzzySet secondSet = engine.getSets().get(secondSetName);

		IDomain[] firstDomains = firstSet.getDomain().getDomainComponents();
		IDomain[] secondDomains = secondSet.getDomain().getDomainComponents();
		
		IDomain compDomain;
		IDomain innerDomain;
		
		int bestAlignment = 0;
		
		if(firstDomains.length == 2 && secondDomains.length == 2) {
			if(!firstDomains[1].equals(secondDomains[0])) {
				throw new FuzzyEngineException("Illegal binary domains for composition!");
			}
			
			bestAlignment = 1;
			
			compDomain = new CartesianDomain(firstDomains[0], secondDomains[1]);
			innerDomain = new CartesianDomain(firstDomains[1], secondDomains[0]);
		} else {
			int maxPossibleAlignment = Math.min(firstDomains.length, secondDomains.length);
			
			for(int i = 1; i <= maxPossibleAlignment; i++) {
				for(int j = 0; j < i; j++) {
					if(!firstDomains[firstDomains.length - j - 1].equals(secondDomains[j])) {
						break;
					} else if(j == i - 1) {
						bestAlignment = i;
					}
				}
			}
			
			if(firstDomains.length == secondDomains.length
					&& bestAlignment == firstDomains.length) {
				bestAlignment--;
			}
			
			if(bestAlignment == 0) {
				throw new FuzzyEngineException(
						"The alignemnt of the composition domains is empty!");
			}
			
			int nCompDomains = firstDomains.length + secondDomains.length - 2 * bestAlignment;
			IDomain[] compDomains = new IDomain[nCompDomains];
			IDomain[] innerDomains = new IDomain[bestAlignment];
			
			int fromFirst = firstDomains.length - bestAlignment;
			int fromSecond = secondDomains.length - bestAlignment;
			
			System.arraycopy(firstDomains, 0, compDomains, 0, fromFirst);
			System.arraycopy(secondDomains, bestAlignment, compDomains, fromFirst, fromSecond);
			System.arraycopy(secondDomains, 0, innerDomains, 0, bestAlignment);
			
			compDomain = new CartesianDomain(compDomains);
			innerDomain = new CartesianDomain(innerDomains);
		}
		
		FuzzySet set = new FuzzySet(name, compDomain);
		engine.getSets().put(name, set);
		
		double[] compMembership = set.getMembership();
		
		for(int i = 0; i < compDomain.getCardinality(); i++) {
			Object[] compObject = compDomain.elementAt(i);
			double m = 0.0;
			
			for(int j = 0; j < innerDomain.getCardinality(); j++) {
				Object[] innerObject = innerDomain.elementAt(j);
				
				Object[] firstSetObject = new Object[firstDomains.length];
				Object[] secondSetObject = new Object[secondDomains.length];
				
				int firstFromComp = firstSetObject.length - bestAlignment;
				int secondFromComp = secondSetObject.length - bestAlignment;
				
				System.arraycopy(compObject, 0, firstSetObject, 0, firstFromComp);
				System.arraycopy(innerObject, 0, firstSetObject, firstFromComp, bestAlignment);
				
				System.arraycopy(innerObject, 0, secondSetObject, 0, bestAlignment);
				System.arraycopy(compObject, firstFromComp, secondSetObject,
						bestAlignment, secondFromComp);
				
				double mFirst = firstSet.getMembershipFor(firstSetObject);
				double mSecond = secondSet.getMembershipFor(secondSetObject);
				
				m = Math.max(m, minOperator.operate(mFirst, mSecond));
			}
			
			compMembership[i] = m;
		}
	}

}
