package edu.kit.ipd.descartes.librede.nnls.tests;

public class Polynomial {
	private double[] coef;
	private int degree;

	public Polynomial(int degree) {
		this.degree = degree;
		coef = new double[degree];
	}

	public boolean setCoef(double[] coef) {
		if (coef.length > degree) {
			System.out.println("Invalid coefficients:"
					+ " #of coefficients > degree of polynom !");
			return false;
		}
		for (int i = 0; i < coef.length; ++i) {
			this.coef[i] = coef[i];
		}
		return true;
	}

	public double[] getValues(int count) {
		double[] values = new double[count];
		for (int i = 0; i < count; ++i)
			values[i] = this.getValue(i + 1);
		return values;
	}

	// generates random coefficients
	public void generateRandomCoeffs() {
		coef = new double[degree];
		for (int i = 0; i < degree; ++i) {
			coef[i] = Math.round(Math.random() * 100.0) / 100.0;
		}
	}

	public int getDegree() {
		return degree;
	}

	public double[] getCoef() {
		return coef;
	}

	public boolean equal(Object object) {
		return true;
	}

	public double getValue(int x) {
		double y = 0;
		for (int i = 0; i < this.degree; ++i) {
			y += coef[i] * Math.pow(x, i);
		}
		return y;
	}

	public String toString() {
		String str = "y= " + coef[0] + " x";
		for (int i = 1; i < this.degree; ++i) {
			str += " + " + coef[i] + " x ^ " + i;
		}
		return str;
	}
}
