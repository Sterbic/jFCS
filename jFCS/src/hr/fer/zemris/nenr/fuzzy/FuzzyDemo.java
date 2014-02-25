package hr.fer.zemris.nenr.fuzzy;

import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngine;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngineException;

public class FuzzyDemo {
	
	public static void main(String[] args) {
		FuzzyEngine engine = new FuzzyEngine();
		try {
			engine.runScript("test.txt");
		} catch (FuzzyEngineException e) {
			e.printStackTrace();
		}
	}

}
