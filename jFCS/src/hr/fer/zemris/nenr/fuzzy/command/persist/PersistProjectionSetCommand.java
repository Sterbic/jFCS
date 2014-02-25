package hr.fer.zemris.nenr.fuzzy.command.persist;

import hr.fer.zemris.nenr.fuzzy.command.AbstractFuzzyCommand;
import hr.fer.zemris.nenr.fuzzy.domain.CartesianDomain;
import hr.fer.zemris.nenr.fuzzy.domain.IDomain;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngine;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngineException;
import hr.fer.zemris.nenr.fuzzy.set.FuzzySet;

import java.util.HashSet;
import java.util.Set;

public class PersistProjectionSetCommand extends AbstractFuzzyCommand {

	private String name;
	private String originalSetName;
	private String projectionDomain;

	
	public PersistProjectionSetCommand(String name, String originalSetName,
			String projectionDomain) {
		this.name = name;
		this.originalSetName = originalSetName;
		this.projectionDomain = projectionDomain;
	}

	@Override
	public void runOnEngine(FuzzyEngine engine) throws FuzzyEngineException {
		IDomain projDomain = engine.getDomains().get(projectionDomain);
		FuzzySet originalSet = engine.getSets().get(originalSetName);
		
		IDomain originalDomain = originalSet.getDomain();
		IDomain[] originalDomains = originalDomain.getDomainComponents();
		IDomain[] projDomains = projDomain.getDomainComponents();
		
		if(originalDomains.length == projDomains.length) {
			throw new FuzzyEngineException("Illegal projection domain!");
		}
		
		int originalIndex = originalDomains.length - 1;
		Set<Integer> projectedIndexes = new HashSet<>();
		
		for(int i = projDomains.length - 1; i >= 0; i--) {
			IDomain domain = projDomains[i];
			
			while(!originalDomains[originalIndex].equals(domain)) {
				originalIndex--;
			}
			
			if(originalIndex == -1) {
				throw new FuzzyEngineException("Illegal projection domain!");
			} else {
				projectedIndexes.add(originalIndex);
				originalIndex--;
			}
		}
		
		IDomain[] newDomains = new IDomain[originalDomains.length - projDomains.length];
		
		int index = 0;
		for(int i = 0; i < originalDomains.length; i++) {
			if(!projectedIndexes.contains(i)) {
				newDomains[index++] = originalDomains[i];
			}
		}
		
		IDomain newDomain;
		if(newDomains.length == 1) {
			newDomain = newDomains[0];
		} else {
			newDomain = new CartesianDomain(newDomains);;
		}
		
		FuzzySet set = new FuzzySet(name, newDomain);
		engine.getSets().put(name, set);
		
		double[] originalMembership = originalSet.getMembership();
		double[] newMembership = set.getMembership();
		
		for(int i = 0; i < originalDomain.getCardinality(); i++) {
			double m = originalMembership[i];
			
			if(m > 0) {
				Object[] object = originalDomain.elementAt(i);
				Object[] projectedObject = new Object[newDomains.length];
				
				int k = 0;
				for(int j = 0; j < object.length; j++) {
					if(!projectedIndexes.contains(j)) {
						projectedObject[k] = object[j];
						k++;
					}
				}
				
				int elementIndex = newDomain.getIndexOfElement(projectedObject);
				
				newMembership[elementIndex] = Math.max(newMembership[elementIndex], m);
			}
		}
	}
}