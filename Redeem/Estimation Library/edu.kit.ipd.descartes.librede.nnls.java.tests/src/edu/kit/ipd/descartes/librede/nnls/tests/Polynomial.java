/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2014, by Simon Spinner and Contributors.
 *
 * Project Info:   http://www.descartes-research.net/
 *
 * All rights reserved. This software is made available under the terms of the
 * Eclipse Public License (EPL) v1.0 as published by the Eclipse Foundation
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse Public License (EPL)
 * for more details.
 *
 * You should have received a copy of the Eclipse Public License (EPL)
 * along with this software; if not visit http://www.eclipse.org or write to
 * Eclipse Foundation, Inc., 308 SW First Avenue, Suite 110, Portland, 97204 USA
 * Email: license (at) eclipse.org
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 */
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
