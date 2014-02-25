package hr.fer.zemris.nenr.fuzzy.set.operator;

public class ZadehS implements IBinaryOperator {

	private String sign;
	
	public ZadehS(String sign) {
		this.sign = sign;
	}
	
	@Override
	public String getSign() {
		return sign;
	}

	@Override
	public double operate(double firstValue, double secondValue) {
		return Math.max(firstValue, secondValue);
	}

}
