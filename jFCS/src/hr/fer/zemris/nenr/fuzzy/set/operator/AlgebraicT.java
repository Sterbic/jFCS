package hr.fer.zemris.nenr.fuzzy.set.operator;

public class AlgebraicT implements IBinaryOperator {
	
	private String sign;
	
	public AlgebraicT(String sign) {
		this.sign = sign;
	}
	
	@Override
	public String getSign() {
		return sign;
	}

	@Override
	public double operate(double firstValue, double secondValue) {
		return firstValue * secondValue;
	}

}
