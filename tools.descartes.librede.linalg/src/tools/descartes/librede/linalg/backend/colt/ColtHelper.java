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
package tools.descartes.librede.linalg.backend.colt;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.linalg.Vector;

public class ColtHelper {
	
	private static final Algebra ALG = new Algebra();
	
	private static Matrix wrap(DoubleMatrix2D matrix) {
		int rows = matrix.rows();
		int columns = matrix.columns();
		if (columns == 1) {
			if (rows == 1) {
				return new Scalar(matrix.getQuick(0, 0));
			} else {
				return new ColtVector(matrix.viewColumn(0));
			}
		} else {
			if (rows == columns) {
				return new ColtSquareMatrix(matrix);
			} else {
				return new ColtMatrix(matrix);
			}
		}		
	}
	
	public static ColtMatrix toColtMatrix(Matrix a) {
		if (a instanceof ColtMatrix) {
			return (ColtMatrix) a;
		} else {
			return new ColtMatrix(a.toArray2D());
		}
	}

	public static ColtVector toColtVector(Vector a) {
		if (a instanceof ColtVector) {
			return (ColtVector) a;
		} else {
			return new ColtVector(a.toArray1D());
		}
	}
	
	public static Matrix solve(Matrix a, Matrix b) {
		if (a.rows() != b.rows()) {
			throw new IllegalArgumentException("A and B must have the same number of rows.");
		}
		ColtMatrix aMat = toColtMatrix(a);
		ColtMatrix bMat = toColtMatrix(b);

		DoubleMatrix2D x = ALG.solve(aMat.delegate, bMat.delegate);		
		return wrap(x);
	}

}
