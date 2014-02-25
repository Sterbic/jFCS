package hr.fer.zemris.nenr.fuzzy.domain;

import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngine;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngineException;

/**
 * Klasa koja modelira apstraktnu domenu
 * @author Luka Sterbic
 * @version 0.1
 */
public abstract class AbstractDomain implements IDomain {
	
	private String name;
	private boolean isBuilt;

	/**
	 * Konstruktor za AbstractDomain
	 * @param name ime domene
	 */
	public AbstractDomain(String name) {
		this.name = name;
		this.isBuilt = false;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public IDomain[] getDomainComponents() {
		IDomain[] domains = new IDomain[1];
		domains[0] = this;
		return domains;
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
		AbstractDomain other = (AbstractDomain) obj;
		if(name == null) {
			if(other.name != null)
				return false;
		} else if(!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public void build(FuzzyEngine engine) throws FuzzyEngineException {
		if(!isBuilt) {
			isBuilt = true;
			buildDomain(engine);
		}
		
	}

	/**
	 * Konstruiraj domenu na zadanom stroju, samo jednom
	 * @param engine fuzzy stroj
	 * @throws FuzzyEngineException 
	 */
	public abstract void buildDomain(FuzzyEngine engine) throws FuzzyEngineException;
	
	@Override
	public String toString() {
		return name;
	}
	
}
