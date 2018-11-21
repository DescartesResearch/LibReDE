/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2018, by Simon Spinner, Johannes Grohmann
 *  and Contributors.
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
package tools.descartes.librede.models.diff;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;

import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.MatrixBuilder;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorBuilder;

public class DifferentiationUtils {
	
	public static DerivativeStructure[] createDerivativeStructures(Vector x, int order) {
		DerivativeStructure[] dev = new DerivativeStructure[x.rows()];
		for (int i = 0; i < dev.length; i++) {
			dev[i] = new DerivativeStructure(dev.length, order, i, x.get(i));
		}
		return dev;
	}
	
	public static Vector getFirstDerivatives(DerivativeStructure dev) {
		int size = dev.getFreeParameters();
		VectorBuilder ret = VectorBuilder.create(size);
		int[] idx = new int[size];
		for (int i = 0; i < size; i++) {
			idx[i]++;
			ret.add(dev.getPartialDerivative(idx));
			idx[i]--;
		}
		return ret.toVector();
	}
	
	public static Matrix getSecondDerivatives(DerivativeStructure dev) {
		int size = dev.getFreeParameters();
		MatrixBuilder ret = MatrixBuilder.create(size);
		int[] idx = new int[size];
		double[] row = new double[size];
		for (int i = 0; i < size; i++) {
			idx[i]++;
			for (int j = 0; j < size; j++) {
				idx[j]++;
				row[j] = dev.getPartialDerivative(idx);
				idx[j]--;
			}
			ret.addRow(row);
			idx[i]--;
		}
		return ret.toMatrix();
	}
	
	public static Matrix getJacobiMatrix(DerivativeStructure[] dev) {
		int size = dev[0].getFreeParameters();
		MatrixBuilder jacobi = MatrixBuilder.create(dev.length, size);
		double[] row = new double[size];
		int[] idx = new int[size];
		for (int i = 0; i < dev.length; i++) {
			for (int j = 0; j < size; j++) {
				idx[j]++;
				row[j] = dev[i].getPartialDerivative(idx);
				idx[j]--;
			}
			jacobi.addRow(row);
		}
		return jacobi.toMatrix();
	}
}
