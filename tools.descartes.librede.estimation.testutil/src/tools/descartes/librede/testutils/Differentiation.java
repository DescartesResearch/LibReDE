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
package tools.descartes.librede.testutils;

import static tools.descartes.librede.linalg.LinAlg.matrix;
import static tools.descartes.librede.linalg.LinAlg.vector;

import java.util.Arrays;

import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.MatrixFunction;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorFunction;
import tools.descartes.librede.models.State;
import tools.descartes.librede.models.observation.equations.ModelEquation;
import tools.descartes.librede.models.state.IStateModel;

public class Differentiation {
	
	public static Vector diff1(ModelEquation equation, State state) {		
		final double sqrte = Math.sqrt(epsilon());
		final ModelEquation f = equation;
		final IStateModel<?> stateModel = state.getStateModel();
		final Vector x = state.getVector();
		final double[] h = new double[x.rows()];
		
		Vector dev = vector(x.rows(), new VectorFunction() {			
			@Override
			public double cell(int row) {
				Arrays.fill(h, 0);
				h[row] = sqrte * (Math.abs(x.get(row)) + 1.0);
				Vector hVec = vector(h);
				
				double x1 = f.getValue(new State(stateModel, x.plus(hVec))).getValue();
				double x2 = f.getValue(new State(stateModel, x.minus(hVec))).getValue();
				
				return (x1 - x2) / (2 * h[row]);
			}
		});		
		return dev;	
	}
	
	public static Matrix diff2(ModelEquation equation, State state) {		
		final double sqrtsqrte = Math.sqrt(Math.sqrt(epsilon()));
		final ModelEquation f = equation;
		final IStateModel<?> stateModel = state.getStateModel();
		final Vector x = state.getVector();
		final double[] h = new double[x.rows()];
		
		Matrix dev = matrix(x.rows(), x.rows(), new MatrixFunction() {			
			@Override
			public double cell(int row, int column) {
				/*
				 * Based on http://www.math.ohiou.edu/courses/math3600/lecture27.pdf
				 */
				
				if (row == column) {
					Arrays.fill(h, 0);
					h[row] = sqrtsqrte * (Math.abs(x.get(row)) + 1.0);
					Vector hVec = vector(h);
					
					double x1 = f.getValue(new State(stateModel, x.plus(hVec))).getValue();
					double x2 = f.getValue(new State(stateModel, x)).getValue();
					double x3 = f.getValue(new State(stateModel, x.minus(hVec))).getValue();
					
					return (x1 - 2 * x2 + x3) / (h[row]*h[row]);
				} else {
					Arrays.fill(h, 0);
					h[row] = sqrtsqrte * (Math.abs(x.get(row)) + 1.0);
					Vector h1 = vector(h);
					Arrays.fill(h, 0);
					h[column] = sqrtsqrte * (Math.abs(x.get(column)) + 1.0);
					Vector h2 = vector(h);
					
					double x1 = f.getValue(new State(stateModel, x.plus(h1.plus(h2)))).getValue();
					double x2 = f.getValue(new State(stateModel, x.plus(h1.minus(h2)))).getValue();
					double x3 = f.getValue(new State(stateModel, x.plus(h2.minus(h1)))).getValue();
					double x4 = f.getValue(new State(stateModel, x.minus(h1.plus(h2)))).getValue();
					
					return (x1 - x2 - x3 + x4) / (4 * h1.get(row) * h2.get(column));
				}
				
			}
		});		
		return dev;	
	}
	
	private static double epsilon() {
		/*
		 * Based on algorithm from Ronald Mak, "Java Number Cruncher: The Java Programmer's Guide to Numerical Computing", 
		 * Prentice Hall, Upper Saddle River, New Jersey
		 */
		double epsilon = 0.5;
		while (1 + epsilon > 1) {
			epsilon /= 2;
		}
		return epsilon;
	}

}
