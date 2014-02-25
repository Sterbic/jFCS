package hr.fer.zemris.nenr.fuzzy.command.persist;

import java.util.Arrays;

import hr.fer.zemris.nenr.fuzzy.command.AbstractFuzzyCommand;
import hr.fer.zemris.nenr.fuzzy.domain.IDomain;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngine;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngineException;
import hr.fer.zemris.nenr.fuzzy.set.FuzzySet;

public class PersistFunctionSetCommand extends AbstractFuzzyCommand {
	
	private String name;
	private String domainName;
	private String isWhat;

	public PersistFunctionSetCommand(String name, String domainName, String isWhat) {
		this.name = name;
		this.domainName = domainName;
		this.isWhat = isWhat;
	}

	@Override
	public void runOnEngine(FuzzyEngine engine) throws FuzzyEngineException {
		IDomain domain = engine.getDomains().get(domainName);
		
		FuzzySet old = engine.getSets().get(name);
		if(old != null && !domain.equals(old.getDomain())) {
			throw new FuzzyEngineException("The set is defined on a different domain!");
		}
		
		FuzzySet set = new FuzzySet(name, domain);
		double[] membership = set.getMembership();
		
		String[] parts = isWhat.substring(0, isWhat.length() - 1).split("\\(");
		String[] arguments = parts[1].split(",");
		
		int alpha = domain.getIndexOfElement(
				domain.fromStringRepresentation(arguments[0]));
		int beta = domain.getIndexOfElement(
				domain.fromStringRepresentation(arguments[1]));
		int gamma;
		int delta;
		
		double diff = beta - alpha;
		
		switch(parts[0]) {
		case "gamma":	
			for(int x = alpha; x < beta; x++) {
				membership[x] = (x - alpha) / diff;
			}
			
			Arrays.fill(membership, beta, membership.length, 1.0);
			break;
			
		case "lambda":
			gamma = domain.getIndexOfElement(
					domain.fromStringRepresentation(arguments[2]));
			
			double diffUp = beta - alpha;
			double diffDown = gamma - beta;
			
			for(int x = alpha; x < beta; x++) {
				membership[x] = (x - alpha) / diffUp;
			}
			
			for(int x = beta; x < gamma; x++) {
				membership[x] = (gamma - x) / diffDown;
			}
			
			break;
			
		case "l":
			Arrays.fill(membership, 0, alpha, 1.0);
			
			for(int x = alpha; x < beta; x++) {
				membership[x] = (beta - x) / diff;
			}
			
			break;
			
		case "pi":
			gamma = domain.getIndexOfElement(
					domain.fromStringRepresentation(arguments[2]));
			delta = domain.getIndexOfElement(
					domain.fromStringRepresentation(arguments[3]));
			
			double diffRise = beta - alpha;
			double diffFall = delta - gamma;
			
			for(int x = alpha; x < beta; x++) {
				membership[x] = (x - alpha) / diffRise;
			}
			
			Arrays.fill(membership, beta, gamma, 1.0);
			
			for(int x = gamma; x < delta; x++) {
				membership[x] = (delta - x) / diffFall;
			}
			
			break;

		default:
			throw new IllegalArgumentException("Unknown function!");
		}
		
		engine.getSets().put(name, set);
	}

}
