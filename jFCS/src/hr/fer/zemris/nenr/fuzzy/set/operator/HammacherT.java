package hr.fer.zemris.nenr.fuzzy.set.operator;

public class HammacherT implements IBinaryOperator {

	private String sign;
	private double v;
	
	public HammacherT(String sign, double v) {
		this.sign = sign;
		this.v = v;
	}
	
	@Override
	public String getSign() {
		return sign;
	}

	@Override
	public double operate(double firstValue, double secondValue) {
		double a = firstValue;
		double b = secondValue;
		return a * b / (v + (1 - v) * (a + b - a * b));
	}
	
}
