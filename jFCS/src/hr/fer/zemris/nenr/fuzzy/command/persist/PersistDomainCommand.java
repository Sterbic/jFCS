package hr.fer.zemris.nenr.fuzzy.command.persist;

import hr.fer.zemris.nenr.fuzzy.command.AbstractFuzzyCommand;
import hr.fer.zemris.nenr.fuzzy.domain.IDomain;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngine;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngineException;

import java.util.Map;

/**
 * Class modeling a fuzzy engine command to
 * persist the given domain in the domain map.
 * @author Luka Sterbic
 * @version 0.3
 */
public class PersistDomainCommand extends AbstractFuzzyCommand {

	private IDomain domain;

	/**
	 * Constructor for PersistDomainCommand
	 * @param domain the domain to be persisted
	 */
	public PersistDomainCommand(IDomain domain) {
		if(domain == null) {
			throw new NullPointerException("The domain must not be null!");
		}
		
		this.domain = domain;
	}

	@Override
	public void runOnEngine(FuzzyEngine engine) throws FuzzyEngineException {
		Map<String, IDomain> domains = engine.getDomains();
		
		if(domains.containsKey(domain.getName())) {
			throw new FuzzyEngineException(
					"A domain with the given name is already present in the engine!");
		} else {
			domain.build(engine);
			domains.put(domain.getName(), domain);
		}
	}
	
}
