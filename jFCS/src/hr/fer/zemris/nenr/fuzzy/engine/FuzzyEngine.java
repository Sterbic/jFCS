package hr.fer.zemris.nenr.fuzzy.engine;

import hr.fer.zemris.nenr.fuzzy.command.IFuzzyCommand;
import hr.fer.zemris.nenr.fuzzy.command.parser.FuzzyCommandParser;
import hr.fer.zemris.nenr.fuzzy.controller.FuzzyRule;
import hr.fer.zemris.nenr.fuzzy.decoder.CenterOfAreaDecoder;
import hr.fer.zemris.nenr.fuzzy.decoder.IFuzzyDecoder;
import hr.fer.zemris.nenr.fuzzy.domain.IDomain;
import hr.fer.zemris.nenr.fuzzy.set.FuzzySet;
import hr.fer.zemris.nenr.fuzzy.set.operator.IOperator;
import hr.fer.zemris.nenr.fuzzy.set.operator.MamdaniMin;
import hr.fer.zemris.nenr.fuzzy.set.operator.ZadehNot;
import hr.fer.zemris.nenr.fuzzy.set.operator.ZadehS;
import hr.fer.zemris.nenr.fuzzy.set.operator.ZadehT;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class modeling an engine capable of running commands
 * encapsulated in the IFuzzyCommand interface.
 * @author Luka Sterbic
 * @version 0.3
 */
public class FuzzyEngine {
	
	private Map<String, IDomain> domains;
	private Map<String, FuzzySet> sets;
	private Map<String, IOperator> operators;
	private List<Character> opPriority;
	private IFuzzyDecoder decoder;
	private Map<String, Object> singletons;
	private Map<FuzzySet, List<FuzzyRule>> rules;
	
	/**
	 * Constructor for FuzzyEngine
	 */
	public FuzzyEngine() {
		this.domains = new HashMap<>();
		this.sets = new HashMap<>();
		this.operators = new HashMap<>();
		this.opPriority = new ArrayList<>();
		this.decoder = new CenterOfAreaDecoder();
		this.singletons = new HashMap<>();
		this.rules = new HashMap<>();

		operators.put("!", new ZadehNot("!"));
		operators.put("*", new ZadehT("*"));
		operators.put("+", new ZadehS("+"));
		operators.put(">", new MamdaniMin(">"));
		
		opPriority.add('>');
		opPriority.add('+');
		opPriority.add('*');
		opPriority.add('!');
	}
	
	/**
	 * Run a fuzzy script with the given path
	 * @param scriptPath path to the script file
	 * @throws FuzzyEngineException in case of errors while executing the commands
	 */
	public void runScript(String scriptPath) throws FuzzyEngineException {
		int lineNumber = 0;
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(
				new BufferedInputStream(new FileInputStream(new File(scriptPath)))))) {
			while(true) {
				String line = br.readLine();
				lineNumber++;
				
				if(line == null) {
					break;
				}
				
				line = line.trim();
				
				if(line.isEmpty() || line.startsWith("#")) {
					continue;
				}
				
				if(!line.endsWith(";")) {
					throw new FuzzyEngineException("Illegal line ending!");
				}
				
				line = line.substring(0, line.length() - 1);
				
				IFuzzyCommand command = FuzzyCommandParser.parse(line);
				runCommand(command);
			}
		} catch(Exception ex) {
			throw new FuzzyEngineException(ex.getMessage() + " Line: " + lineNumber);
		}
	}
	
	public void runCommand(IFuzzyCommand command) throws FuzzyEngineException {
		command.run(this);
	}

	/**
	 * @return the domains
	 */
	public Map<String, IDomain> getDomains() {
		return domains;
	}
	
	/**
	 * @return the sets
	 */
	public Map<String, FuzzySet> getSets() {
		return sets;
	}
	
	/**
	 * @return the operators
	 */
	public Map<String, IOperator> getOperators() {
		return operators;
	}
	
	/**
	 * @return the opPriority
	 */
	public List<Character> getOpPriority() {
		return opPriority;
	}
	
	/**
	 * Register the given singleton
	 * @param name the name of the singleton
	 * @param object the value of the singleton
	 */
	public void registerSingleton(String name, Object object) {
		singletons.put(name, object);
	}
	
	/**
	 * Get the value of the singleton with the given name
	 * @param name the name of the singleton
	 * @return the value of the singleton
	 */
	public Object getSingleton(String name) {
		return singletons.get(name);
	}
	
	/**
	 * Decode the selected set
	 * @param fuzzySet the name of the fuzzy set
	 * @return the decoded integer value
	 */
	public double decode(String fuzzySet) {
		return decoder.decode(sets.get(fuzzySet));
	}
	
	/**
	 * Register the given rule to the rule database
	 * @param rule a fuzzy rule
	 */
	public void registerRule(FuzzyRule rule) {
		List<FuzzyRule> ruleList = rules.get(rule.getResultSet());
		
		if(ruleList == null) {
			ruleList = new ArrayList<>();
			rules.put(rule.getResultSet(), ruleList);
		}
		
		ruleList.add(rule);
	}

	/**
	 * Evaluate all stored rules
	 */
	public void evaluateRules() {
		for(FuzzySet consequent : rules.keySet()) {
			IDomain consDomain = consequent.getDomain();
			double[] consMembership = consequent.getMembership();
			
			List<FuzzyRule> ruleSet = rules.get(consequent);
			
			for(int i = 0; i < consDomain.getCardinality(); i++) {
				Object[] consObject = consDomain.elementAt(i);
				double m = 0.0;
				
				for(FuzzyRule rule : ruleSet) {
					m = Math.max(m, rule.evaluateFor(this, consObject));
				}
				
				consMembership[i] = m;
			}
		}
	}
	
}
