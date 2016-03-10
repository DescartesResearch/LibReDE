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
package tools.descartes.librede.models.observation.queueingmodel;

import static tools.descartes.librede.linalg.LinAlg.matrix;
import static tools.descartes.librede.linalg.LinAlg.vector;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;

import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.MatrixFunction;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.AbstractDependencyTarget;

public class ErlangCEquation extends AbstractDependencyTarget implements UnivariateDifferentiableFunction {
	
	// The degree of the polynomials used to approximate the Erlang-C function
	// A degree of 6 seems to be acceptable for k <= 15 for (tested with matlab)
	private static final int DEGREE = 6;
	
	// The parameters of the polynomial used to approximate the Erlang-C function
	private double[] factors = new double[DEGREE];
	
	private final int k;
	
	public ErlangCEquation(int k) {
		this.k = k;
		precalculateFactors();
	}
	
	public double calculateValue(double utilization) {
		if (k == 1) {
			// special case
			return utilization;
		} else {
			if (utilization >= 1.0) {
				return 1.0;
			} else {					
				double value = 0;
				double u = utilization;
				for (int i = 0; i < factors.length; i++) {
					value += factors[i] * u;
					u *= utilization;
				}
				return value;
			}
		}
	}

	public double calculateFirstDerivative(double utilization, double utilizationFirstDerivative) {
		if (k == 1) {
			return utilizationFirstDerivative;		
		} else {
			if (utilization >= 1.0) {
				return 0.0;
			} else {
				double u = utilization;
				double value = factors[0] * utilizationFirstDerivative;
				for (int i = 1; i < factors.length; i++) {
					value += (i + 1) * factors[i] * u * utilizationFirstDerivative;
					u *= utilization;
				}
				return value;
			}
		}
	}
	
	public double calculateSecondDerivative(double utilization, double utilizationFirstDerivative) {
		if (k == 1) {
			return 0.0;
		} else {
			if (utilization >= 1.0) {
				return 0.0;
			} else {
				double value = 0.0;
				double u = 1.0;
				for (int i = 1; i < factors.length; i++) {
					value += (i + 1) * factors[i] * (i * u * utilizationFirstDerivative);
					u *= utilization;
				}
				return value;
			}
		}
	}
	
	private void precalculateFactors() {
		// now we fit a n-degree polynomial function to the calculated values
		final double[] erlangValues = calculateErlangC(k);
		Matrix a = matrix(erlangValues.length, DEGREE, new MatrixFunction() {			
			@Override
			public double cell(int row, int column) {
				return Math.pow(row / 100.0, column + 1);
			}
		});
		Vector b = vector(erlangValues);
		Matrix x = a.mldivide(b);
		if (!x.isVector()) {
			throw new IllegalStateException();
		}
		factors = x.toArray1D();
	}
	
	private double[] calculateErlangC(int k) {
		/*
		 * See Mor Harchol-Balter, "Performance Modeling and Design of Computer Systems", Chapter 14, p. 260
		 */
		double[] func = new double[101];
		func[0] = 0;
		func[100] = 1;
		
		for (int u = 1; u < 100; u++) {
			double util = u / 100.0;
			double phi0 = 0.0;
			double factorial = 1.0;
			for (int i = 0; i < k; i++) {
				factorial = (i == 0) ? factorial : factorial * i;
			    phi0 += Math.pow(k * util, i) / factorial;
			}
			factorial = factorial * k;
			phi0 += Math.pow(k * util, k) / (factorial * (1 - util));
			phi0 = 1 / phi0;
			func[u] = (Math.pow(k * util, k) * phi0) / (factorial * (1 - util));
		}
		return func;
	}

	@Override
	public double value(double x) {
		return calculateValue(x);
	}

	@Override
	public DerivativeStructure value(DerivativeStructure x) {
		if (k == 1) {
			// special case
			return x;
		} else {
			if (x.getValue() >= 1.0) {
				return new DerivativeStructure(x.getFreeParameters(), x.getOrder(), 1.0);
			} else {					
				DerivativeStructure[] polynomials = new DerivativeStructure[DEGREE];
				for (int i = 0; i < polynomials.length; i++) {
					polynomials[i] = x.pow(i + 1);
				}
				return x.linearCombination(factors, polynomials);
			}
		}
	}

}
