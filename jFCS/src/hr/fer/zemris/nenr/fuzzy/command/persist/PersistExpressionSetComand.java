package hr.fer.zemris.nenr.fuzzy.command.persist;

import hr.fer.zemris.nenr.fuzzy.command.AbstractFuzzyCommand;
import hr.fer.zemris.nenr.fuzzy.command.parser.FuzzyParserException;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngine;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngineException;
import hr.fer.zemris.nenr.fuzzy.set.FuzzyBinaryOperatorSet;
import hr.fer.zemris.nenr.fuzzy.set.FuzzyImplicationSet;
import hr.fer.zemris.nenr.fuzzy.set.FuzzyNegatedSet;
import hr.fer.zemris.nenr.fuzzy.set.FuzzySet;
import hr.fer.zemris.nenr.fuzzy.set.operator.IOperator;

public class PersistExpressionSetComand extends AbstractFuzzyCommand {

	private String name;
	private String expression;
	private FuzzyEngine engine;
	
	public PersistExpressionSetComand(String name, String expression) {
		this.name = name;
		this.expression = expression;
	}
	
	@Override
	public void runOnEngine(FuzzyEngine engine) throws FuzzyEngineException {
		this.engine = engine;

		if(engine.getSets().containsKey(name)) {
			throw new FuzzyEngineException("A set with the given name already exists!");
		}
		
		FuzzySet set = parseFuzzyExpression(expression);
		set.setName(name);
		
		engine.getSets().put(name, set);
	}
	
	
	public FuzzySet parseFuzzyExpression(String expression) throws FuzzyParserException {
		if(expression == null) {
			throw new FuzzyParserException("Illegal expression, expression is null.");
		}
		
		expression = expression.replaceAll("\\s+", "");
		
		if(expression.isEmpty()) {
			throw new FuzzyParserException("Illegal expression, expression is empty.");
		}
		
		for(Character op : engine.getOpPriority()) {
			int depth = 0;
			
			for(int p = expression.length() - 1; p >= 0 ; p--) {
				char c = expression.charAt(p);
				if(c == ')') {
					depth++;
				} else if(c == '(') {
					depth--;
				} else if(depth == 0 && op.equals(c)) {
					IOperator operator = engine.getOperators().get(Character.toString(c));
					
					if(c == '!') {
						FuzzySet right = parseFuzzyExpression(expression.substring(p + 1));
						return new FuzzyNegatedSet(right, operator);
					} else {
						FuzzySet left = parseFuzzyExpression(expression.substring(0, p));
						FuzzySet right = parseFuzzyExpression(expression.substring(p + 1));
						
						switch(c) {
						case '>':
							return new FuzzyImplicationSet(left, right, operator);
						case '+':
							return new FuzzyBinaryOperatorSet(left, right, operator);
						case '*':
							return new FuzzyBinaryOperatorSet(left, right, operator);
						default:
							throw new FuzzyParserException("Unknown operator.");
						}
					}
				}
			}
		}
		
		if(expression.startsWith("(") && expression.endsWith(")")) {
			return parseFuzzyExpression(expression.substring(1, expression.length() - 1));
		}

		return engine.getSets().get(expression);
	}

}
