package hr.fer.zemris.nenr.fuzzy.domain;

import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngine;
import hr.fer.zemris.nenr.fuzzy.util.Utils;

public class IntegerDomain extends AbstractDomain {

	private int from;
	private int to;
	private int step;
	private int[] array;
	
	public IntegerDomain(String name, int from, int to, int step) {
		super(name);
		
		if(from > to || step < 0) {
			throw new IllegalArgumentException("Illegal parameters for an integer domain!");
		}
		
		this.from = from;
		this.to = to;
		this.step = step;
	}

	@Override
	public Object[] fromStringRepresentation(String... value) {
		return Utils.toObjectArray(Integer.parseInt(value[0]));
	}

	@Override
	public int getIndexOfElement(Object... value) {
		int val = (int) value[0];
		
		for(int i = 0; i < array.length; i++) {
			if(val == array[i]) {
				return i;
			}
		}
		
		return -1;
	}

	@Override
	public Object[] elementAt(int index) {
		return Utils.toObjectArray(array[index]);
	}

	@Override
	public int getCardinality() {
		return array.length;
	}

	@Override
	public void buildDomain(FuzzyEngine engine) {
		array = new int[to - from + 1];
		
		for(int i = 0; i < array.length; i++) {
			array[i] = from + step * i;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(getName()).append(": Integer[").append(from);
		sb.append(", ").append(to).append(", ").append(step).append("]");
		
		return sb.toString();
	}

	@Override
	public String toStringRepresentation(Object... value) {
		return value[0].toString();
	}
	
}
