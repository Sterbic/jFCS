package hr.fer.zemris.nenr.fuzzy.set.operator;

public class HammacherS implements IBinaryOperator {

	private String sign;
	private double v;
	
	public HammacherS(String sign, double v) {
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
		return (a + b - (2 - v) * a * b) / (1 - (1 - v) * a * b);
	}
	
}
