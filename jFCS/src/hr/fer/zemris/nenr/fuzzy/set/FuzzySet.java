package hr.fer.zemris.nenr.fuzzy.set;

import hr.fer.zemris.nenr.fuzzy.domain.IDomain;

public class FuzzySet {
	
	private String name;
	private IDomain domain;
	private double[] membership;
	
	public FuzzySet(String name, IDomain domain) {
		this.name = name;
		this.domain = domain;
		this.membership = new double[domain.getCardinality()];
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public IDomain getDomain() {
		return domain;
	}
	
	public double[] getMembership() {
		return membership;
	}
	
	public void setMembership(double[] membership) {
		this.membership = membership;
	}
	
	public double getMembershipFor(Object... value) {
		return membership[domain.getIndexOfElement(value)];
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		FuzzySet other = (FuzzySet) obj;
		if(name == null) {
			if(other.name != null)
				return false;
		} else if(!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(name).append(": {");
		boolean empty = true;
		
		for(int i = 0; i < domain.getCardinality(); i++) {
			Object[] object = domain.elementAt(i);
			double m = getMembershipFor(object);
			
			if(m > 0) {
				empty = false;
				
				sb.append(m).append("/");
				sb.append(domain.toStringRepresentation(object));
				sb.append(", ");
			}
		}
		
		if(!empty) {
			sb.delete(sb.length() - 2, sb.length());
		}
		
		return sb.append("}").toString();
	}

}
