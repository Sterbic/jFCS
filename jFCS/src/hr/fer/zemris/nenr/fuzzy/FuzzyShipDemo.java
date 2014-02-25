package hr.fer.zemris.nenr.fuzzy;

import hr.fer.zemris.nenr.fuzzy.controller.FuzzyRule;
import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngine;
import hr.fer.zemris.nenr.fuzzy.set.FuzzyNegatedSet;
import hr.fer.zemris.nenr.fuzzy.set.FuzzySet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FuzzyShipDemo {

	public static void main(String[] args) throws Exception {
		//System.setIn(new FileInputStream("in.txt"));
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

		int L;
		int D;
		int LK;
		int DK;
		int V;
		int S;
		
		FuzzyEngine engine = new FuzzyEngine();
		engine.runScript("init.fscript");
		
		initRules(engine);
		
		String line = null;
		
		while(true) {
			line = input.readLine();
			
			if(line == null) {
				break;
			}
				
			line = line.trim();
				
			if(line.equals("KRAJ")) {
				break;
			}
				
			Scanner s = new Scanner(line);
			
			L = s.nextInt();
			D = s.nextInt();
			LK = s.nextInt();
			DK = s.nextInt();
			V = s.nextInt();
			S = s.nextInt();
			
			s.close();

			engine.registerSingleton("L", L);
			engine.registerSingleton("D", D);
			engine.registerSingleton("LK", LK);
			engine.registerSingleton("DK", DK);
			engine.registerSingleton("V", V);
			engine.registerSingleton("S", S);

			engine.evaluateRules();
			
			int acc = (int) Math.round(engine.decode("acc"));
			int rudder = (int) Math.round(engine.decode("kormilo"));
					
	        System.out.println(acc + " " + rudder);
	        System.out.flush();
	    }
	}

	private static void initRules(FuzzyEngine engine) {
		Map<String, FuzzySet> sets = engine.getSets();
		
		Map<String, FuzzySet> r1a = new HashMap<>();
		r1a.put("S", sets.get("S0"));
		r1a.put("DK", sets.get("DKunsafe"));
		
		FuzzyRule r1 = new FuzzyRule(r1a, sets.get("anglePBig"), sets.get("kormilo"));
		engine.registerRule(r1);
		
		Map<String, FuzzySet> r2a = new HashMap<>();
		r2a.put("S", sets.get("S0"));
		r2a.put("LK", sets.get("LKunsafe"));
		
		FuzzyRule r2 = new FuzzyRule(r2a, sets.get("angleNBig"), sets.get("kormilo"));
		engine.registerRule(r2);
		
		Map<String, FuzzySet> r3a = new HashMap<>();
		r3a.put("S", sets.get("S0"));
		r3a.put("LK", sets.get("LKsafe"));
		
		FuzzyRule r3 = new FuzzyRule(r3a, sets.get("anglePBig"), sets.get("kormilo"));
		engine.registerRule(r3);
		
		Map<String, FuzzySet> r4a = new HashMap<>();
		r4a.put("LK", sets.get("LKunsafe"));
		
		FuzzyRule r4 = new FuzzyRule(r4a, sets.get("angleNBig"), sets.get("kormilo"));
		engine.registerRule(r4);
		
		Map<String, FuzzySet> r5a = new HashMap<>();
		r5a.put("DK", sets.get("DKunsafe"));
		
		FuzzyRule r5 = new FuzzyRule(r5a, sets.get("anglePBig"), sets.get("kormilo"));
		engine.registerRule(r5);
		
		Map<String, FuzzySet> r6a = new HashMap<>();
		r6a.put("V", sets.get("speedSlow"));
		
		FuzzyRule r6 = new FuzzyRule(r6a, sets.get("accFSmall"), sets.get("acc"));
		engine.registerRule(r6);
		
		/*Map<String, FuzzySet> r7a = new HashMap<>();
		r7a.put("D", sets.get("Dfar"));
		
		FuzzyRule r7 = new FuzzyRule(r7a, sets.get("angleNSmall"), sets.get("kormilo"));
		engine.registerRule(r7);
		
		Map<String, FuzzySet> r8a = new HashMap<>();
		r8a.put("L", sets.get("Lfar"));
		
		FuzzyRule r8 = new FuzzyRule(r8a, sets.get("anglePSmall"), sets.get("kormilo"));
		engine.registerRule(r8);*/
		
		Map<String, FuzzySet> r9a = new HashMap<>();
		r9a.put("DK", sets.get("DKsafe"));
		r9a.put("LK", sets.get("LKsafe"));
		r9a.put("V", new FuzzyNegatedSet(sets.get("speedFast"), engine.getOperators().get("!")));
		
		FuzzyRule r9 = new FuzzyRule(r9a, sets.get("accFSmall"), sets.get("acc"));
		engine.registerRule(r9);
		/*
		Map<String, FuzzySet> r10a = new HashMap<>();
		r10a.put("DK", sets.get("DKunsafe"));
		r10a.put("V", new FuzzyNegatedSet(sets.get("speedMedium"), engine.getOperators().get("!")));
		
		FuzzyRule r10 = new FuzzyRule(r10a, sets.get("accBSmall"), sets.get("acc"));
		engine.registerRule(r10);
		
		Map<String, FuzzySet> r11a = new HashMap<>();
		r11a.put("LK", sets.get("LKunsafe"));
		r11a.put("V", new FuzzyNegatedSet(sets.get("speedMedium"), engine.getOperators().get("!")));
		
		FuzzyRule r11 = new FuzzyRule(r11a, sets.get("accBSmall"), sets.get("acc"));
		engine.registerRule(r11);*/
		
		Map<String, FuzzySet> r12a = new HashMap<>();
		r12a.put("LK", sets.get("LKunsafe"));
		r12a.put("DK", sets.get("DKunsafe"));
		
		FuzzyRule r12 = new FuzzyRule(r12a, sets.get("accBSmall"), sets.get("acc"));
		engine.registerRule(r12);
		engine.registerRule(new FuzzyRule(r12a, sets.get("angleNBig"), sets.get("kormilo")));
	}

}
