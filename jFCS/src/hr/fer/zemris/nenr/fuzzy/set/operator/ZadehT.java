package hr.fer.zemris.nenr.fuzzy.set.operator;

public class ZadehT implements IBinaryOperator {

	private String sign;
	
	public ZadehT(String sign) {
		this.sign = sign;
	}
	
	@Override
	public String getSign() {
		return sign;
	}

	@Override
	public double operate(double firstValue, double secondValue) {
		return Math.min(firstValue, secondValue);
	}
	
}
