package edu.kit.ipd.descartes.redeem.nnls.test;

public class Polynomial {
	private double[] coef;
	private int degree;
	
	public Polynomial(int degree) {
		this.degree = degree;
		coef = new double[degree + 1];
	}
	public boolean setCoef(double[] coef) {
		if (coef.length > degree + 1) {
			System.out.println("Invalid coefficients:"+
		    " #of coefficients > degree of polynom !");
			return false;
		}
		coef[0]=0;
		for (int i = 1; i < coef.length; ++i) {
			this.coef[i] = coef[i];
		}
		return true;
	}
	public int getDegree() {
		return degree;
	}
	public double[] getCoef() {
		return coef;
	}	
	public boolean equal(Object object)
	{
		return true;
	}
	
	public double getValue(int x)
	{
		double y=0;
		for(int i=0;i<this.degree+1;++i)
		{
			y+=coef[i] * Math.pow(x,i);
		}
		return y;
	}
	public String toString()
	{
		String str="y= "+coef[1]+" x";
		for(int i=2;i<this.degree+1;++i)
		{
			str+=" + "+coef[i]+" x ^ "+i;
		}
		return str;
	}
}
