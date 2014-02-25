package hr.fer.zemris.nenr.fuzzy.command.persist;

import hr.fer.zemris.nenr.fuzzy.command.AbstractFuzzyCommand;
import hr.fer.zemris.nenr.fuzzy.domain.IDomain;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngine;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngineException;
import hr.fer.zemris.nenr.fuzzy.set.FuzzySet;

public class PersistSimpleSetCommand extends AbstractFuzzyCommand {

	private String name;
	private String domainName;
	private String isWhat;

	public PersistSimpleSetCommand(String name, String domainName, String isWhat) {
		this.name = name;
		this.domainName = domainName;
		this.isWhat = isWhat;
	}

	@Override
	public void runOnEngine(FuzzyEngine engine) throws FuzzyEngineException {
		IDomain domain = engine.getDomains().get(domainName);
		
		FuzzySet old = engine.getSets().get(name);
		double[] membership;

		if(old == null) {
			FuzzySet set = new FuzzySet(name, domain);
			engine.getSets().put(name, set);
			membership = set.getMembership();
		} else {
			if(!domain.equals(old.getDomain())) {
				throw new FuzzyEngineException("The set is defined on a different domain!");
			}
			
			membership = old.getMembership();
		}
		
		String[] parts;
		if(isWhat.contains("+")) {
			parts = isWhat.split("\\+");
		} else {
			parts = isWhat.split("\\),");
			
			if(parts.length == 1) {
				parts = isWhat.split(",");
			}
		}
		
		if(parts[0].isEmpty()) {
			return;
		}
		
		for(String entry : parts) {
			String[] entryParts = entry.split("/");
			String value = entryParts[1];
			Object[] object;
			
			if(value.startsWith("(")) {
				value = value.substring(1, value.length() - (value.endsWith(")") ? 1 : 0));
				object = domain.fromStringRepresentation(value.split(","));
			} else {
				object = domain.fromStringRepresentation(value);
			}
			
			int index = domain.getIndexOfElement(object);
			membership[index] = Double.parseDouble(entryParts[0]);
		}
	}
	
}