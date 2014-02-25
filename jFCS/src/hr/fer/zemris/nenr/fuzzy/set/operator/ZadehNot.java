package hr.fer.zemris.nenr.fuzzy.set.operator;

public class ZadehNot implements INegation {

	private String sign;
	
	public ZadehNot(String sign) {
		this.sign = sign;
	}
	
	@Override
	public String getSign() {
		return sign;
	}

	@Override
	public double negate(double value) {
		return 1.0 - value;
	}

}
