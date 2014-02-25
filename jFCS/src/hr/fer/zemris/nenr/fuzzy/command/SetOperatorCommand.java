package hr.fer.zemris.nenr.fuzzy.command;

import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngine;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngineException;
import hr.fer.zemris.nenr.fuzzy.set.operator.IOperator;

public class SetOperatorCommand extends AbstractFuzzyCommand {

	private IOperator operator;
	
	public SetOperatorCommand(IOperator operator) {
		this.operator = operator;
	}

	@Override
	public void runOnEngine(FuzzyEngine engine) throws FuzzyEngineException {
		engine.getOperators().put(operator.getSign(), operator);
	}

}
