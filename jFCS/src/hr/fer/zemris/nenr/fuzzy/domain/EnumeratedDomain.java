package hr.fer.zemris.nenr.fuzzy.domain;

import java.util.Arrays;

import hr.fer.zemris.nenr.fuzzy.engine.FuzzyEngine;
import hr.fer.zemris.nenr.fuzzy.util.Utils;

public class EnumeratedDomain extends AbstractDomain {

	private String[] enums;
	
	public EnumeratedDomain(String name, String[] enums) {
		super(name);
		this.enums = enums;
	}

	@Override
	public Object[] fromStringRepresentation(String... value) {
		String val = value[0];
		
		for(String e : enums) {
			if(e.equals(val)) {
				return Utils.toObjectArray(e);
			}
		}
		
		throw new IllegalArgumentException(
				"The given value is not present in the enumeration!");
	}

	@Override
	public int getIndexOfElement(Object... value) {
		for(int i = 0; i < enums.length; i++) {
			if(enums[i].equals(value[0])) {
				return i;
			}
		}
		
		return -1;
	}

	@Override
	public Object[] elementAt(int index) {
		return Utils.toObjectArray(enums[index]);
	}

	@Override
	public int getCardinality() {
		return enums.length;
	}

	@Override
	public void buildDomain(FuzzyEngine engine) {
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getName()).append(": Enum").append(Arrays.toString(enums));
		return sb.toString();
	}

	@Override
	public String toStringRepresentation(Object... value) {
		return value[0].toString();
	}
	
}
