package edu.kit.ipd.descartes.redeem.estimation.testutils;

import static edu.kit.ipd.descartes.linalg.LinAlg.matrix;
import static edu.kit.ipd.descartes.linalg.LinAlg.vector;

import java.util.Arrays;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.MatrixFunction;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorFunction;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IOutputFunction;

public class Differentiation {
	
	public static Vector diff1(IOutputFunction equation, Vector state) {		
		final double sqrte = Math.sqrt(epsilon());
		final IOutputFunction f = equation;		
		final Vector x = state;
		final double[] h = new double[x.rows()];
		
		Vector dev = vector(x.rows(), new VectorFunction() {			
			@Override
			public double cell(int row) {
				Arrays.fill(h, 0);
				h[row] = sqrte * (Math.abs(x.get(row)) + 1.0);
				Vector hVec = vector(h);
				
				double x1 = f.getCalculatedOutput(x.plus(hVec));
				double x2 = f.getCalculatedOutput(x.minus(hVec));
				
				return (x1 - x2) / (2 * h[row]);
			}
		});		
		return dev;	
	}
	
	public static Matrix diff2(IOutputFunction equation, Vector state) {		
		final double sqrtsqrte = Math.sqrt(Math.sqrt(epsilon()));
		final IOutputFunction f = equation;		
		final Vector x = state;
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
					
					double x1 = f.getCalculatedOutput(x.plus(hVec));
					double x2 = f.getCalculatedOutput(x);
					double x3 = f.getCalculatedOutput(x.minus(hVec));
					
					return (x1 - 2 * x2 + x3) / (h[row]*h[row]);
				} else {
					Arrays.fill(h, 0);
					h[row] = sqrtsqrte * (Math.abs(x.get(row)) + 1.0);
					Vector h1 = vector(h);
					Arrays.fill(h, 0);
					h[column] = sqrtsqrte * (Math.abs(x.get(column)) + 1.0);
					Vector h2 = vector(h);
					
					double x1 = f.getCalculatedOutput(x.plus(h1.plus(h2)));
					double x2 = f.getCalculatedOutput(x.plus(h1.minus(h2)));
					double x3 = f.getCalculatedOutput(x.plus(h2.minus(h1)));
					double x4 = f.getCalculatedOutput(x.minus(h1.plus(h2)));
					
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
