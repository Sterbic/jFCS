package hr.fer.zemris.nenr.fuzzy.domain;

import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngine;
import hr.fer.zemris.nenr.fuzzy.util.Utils;

public class RealDomain extends AbstractDomain {
	
	private static final double EPSILON = 1E-6;
	
	private double from;
	private double to;
	private double step;
	private double[] array;
	
	public RealDomain(String name, double from, double to, double step) {
		super(name);
		
		if(from > to || step < 0) {
			throw new IllegalArgumentException("Illegal parameters for a real domain!");
		}
		
		this.from = from;
		this.to = to;
		this.step = step;
	}

	@Override
	public Object[] fromStringRepresentation(String... value) {
		return Utils.toObjectArray(Double.parseDouble(value[0]));
	}

	@Override
	public int getIndexOfElement(Object... value) {
		double val = (double) value[0];
		
		for(int i = 0; i < array.length; i++) {
			if(Math.abs(array[i] - val) < EPSILON) {
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
		int len = 0;
		double val = from;
		
		while(val <= to) {
			val += step;
			len++;
		}
		
		array = new double[len];
		
		for(int i = 0; i < array.length; i++) {
			array[i] = from + step * i;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(getName()).append(": Real[").append(from);
		sb.append(", ").append(to).append(", ").append(step).append("]");
		
		return sb.toString();
	}

	@Override
	public String toStringRepresentation(Object... value) {
		return value[0].toString();
	}

}
