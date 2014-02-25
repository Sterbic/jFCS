package hr.fer.zemris.nenr.fuzzy.domain;

import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngine;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngineException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartesianDomain extends AbstractDomain {
	
	private int cardinality;
	private String[] domainIDs;
	private IDomain[] domains;
	private List<List<Object>> elements;

	public CartesianDomain(String name, String[] domainIDs) {
		super(name);
		this.domainIDs = domainIDs;
	}
	
	public CartesianDomain(IDomain... subDomains) {
		super(null);
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < subDomains.length; i++) {
			sb.append(subDomains[i]);
			
			if(i != subDomains.length - 1) {
				sb.append(" X ");
			}
		}
		
		init(subDomains);
	}
	
	private void init(IDomain... subDomains) {
		List<IDomain> unrolledDomains = new ArrayList<>();
		initSubDomains(unrolledDomains, subDomains);
		
		domains = new IDomain[unrolledDomains.size()];
		unrolledDomains.toArray(domains);
		
		domainIDs = new String[domains.length];
		cardinality = 1;
		
		for(int i = 0; i < domains.length; i++) {
			domainIDs[i] = domains[i].getName();
			cardinality *= domains[i].getCardinality();
		}
		
		elements = cartesianProduct(getSubDomainObjects());
	}
	
	private void initSubDomains(List<IDomain> unrolledDomains, IDomain... subDomains) {
		for(IDomain sd : subDomains) {
			IDomain[] components = sd.getDomainComponents();
			
			if(components.length == 1) {
				unrolledDomains.add(sd);
			} else {
				initSubDomains(unrolledDomains, components);
			}
		}
	}

	@Override
	public IDomain[] getDomainComponents() {
		return domains;
	}

	@Override
	public Object[] fromStringRepresentation(String... value) {
		Object[] object = new Object[domains.length];
		
		for(int i = 0; i < domains.length; i++) {
			object[i] = domains[i].fromStringRepresentation(value[i])[0];
		}
		
		return object;
	}

	@Override
	public int getIndexOfElement(Object... value) {
		int window = cardinality;
		int index = 0;
		
		for(int i = 0; i < domains.length; i++) {
			int localIndex = domains[i].getIndexOfElement(value[i]);
			
			if(localIndex == -1) {
				return -1;
			}
			
			window /= domains[i].getCardinality();
			index += window * localIndex;
		}
		
		return index;
	}

	@Override
	public Object[] elementAt(int index) {
		Object[] object = new Object[domains.length];
		elements.get(index).toArray(object);
		return object;
	}

	@Override
	public int getCardinality() {
		return cardinality;
	}

	@Override
	public void buildDomain(FuzzyEngine engine) throws FuzzyEngineException {
		Map<String, IDomain> domainProvider = engine.getDomains();
		
		IDomain[] subDomains = new IDomain[domainIDs.length];
		
		for(int i = 0; i < domainIDs.length; i++) {
			subDomains[i] = domainProvider.get(domainIDs[i]);
			
			if(subDomains[i] == null) {
				throw new FuzzyEngineException("The domain " + domainIDs[i] + " is undefined!");
			}
		} 
	
		init(subDomains);
	}
	
	private List<List<Object>> getSubDomainObjects() {
		List<List<Object>> domainObjects = new ArrayList<>();
		
		for(IDomain d : domains) {
			List<Object> objects = new ArrayList<>();
			
			for(int i = 0; i < d.getCardinality(); i++) {
				objects.add(d.elementAt(i)[0]);
			}
			
			domainObjects.add(objects);
		}
		
		return domainObjects;
	}

	private List<List<Object>> cartesianProduct(List<List<Object>> domainObjects) {
		List<List<Object>> resultLists = new ArrayList<>();
		
		if(domainObjects.size() == 0) {
			resultLists.add(new ArrayList<Object>());
			return resultLists;
		} else {
			List<Object> firstList = domainObjects.get(0);
			List<List<Object>> remainingLists = cartesianProduct(
					domainObjects.subList(1, domainObjects.size()));
			
			for(Object condition : firstList) {
				for(List<Object> remainingList : remainingLists) {
					List<Object> resultList = new ArrayList<>();
					
					resultList.add(condition);
					resultList.addAll(remainingList);
					resultLists.add(resultList);
				}
			}
		}
		
		return resultLists;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(getName()).append(": Cartesian[");
		
		for(int i = 0; i < domainIDs.length; i++) {
			sb.append(domainIDs[i]);
			
			if(i != domainIDs.length - 1) {
				sb.append(" X ");
			}
		}
		
		sb.append("]");
		
		return sb.toString();
	}

	@Override
	public String toStringRepresentation(Object... value) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("(");
		for(int i = 0; i < domains.length; i++) {
			sb.append(domains[i].toStringRepresentation(value[i]));
			
			if(i != domains.length - 1) {
				sb.append(", ");
			}
		}
		
		return sb.append(")").toString();
	}

}
