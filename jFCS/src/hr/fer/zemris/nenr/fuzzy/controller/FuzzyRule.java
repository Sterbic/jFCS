package hr.fer.zemris.nenr.fuzzy.controller;

import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngine;
import hr.fer.zemris.nenr.fuzzy.set.FuzzySet;
import hr.fer.zemris.nenr.fuzzy.set.operator.IBinaryOperator;

import java.util.Map;
import java.util.Map.Entry;

public class FuzzyRule {
	
	private Map<String, FuzzySet> antecedents;
	private FuzzySet consequent;
	private FuzzySet resultSet;
	
	public FuzzyRule(Map<String, FuzzySet> antecedens, FuzzySet consecvens,
			FuzzySet resultSet) {
		this.antecedents = antecedens;
		this.consequent = consecvens;
		this.resultSet = resultSet;
	}
	
	public FuzzySet getResultSet() {
		return resultSet;
	}

	public double evaluateFor(FuzzyEngine engine, Object[] value) {
		IBinaryOperator operator = (IBinaryOperator) engine.getOperators().get(">");
		double result = 1.0;
		
		for(Entry<String, FuzzySet> entry : antecedents.entrySet()) {
			Object singleton = engine.getSingleton(entry.getKey());
			double mAntecednes = entry.getValue().getMembershipFor(singleton);
			result = operator.operate(result, mAntecednes);
		}

		result = operator.operate(result, consequent.getMembershipFor(value));
		
		return result;
	}
	
}
