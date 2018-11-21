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
package tools.descartes.librede.linalg.backend.colt;

import static tools.descartes.librede.linalg.LinAlg.empty;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.MatrixBuilder;
import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.linalg.Vector;

public class ColtMatrixBuilder extends MatrixBuilder {
	
	private DoubleMatrix2D matrix;
	private int last;
	private int capacity;
	
	public ColtMatrixBuilder(int capacity, int columns) {
		matrix = new DenseDoubleMatrix2D(capacity, columns);
		last = 0;
		this.capacity = capacity;
	}

	@Override
	public void addRow(Vector values) {
		if (values.isEmpty()) {
			return;
		}
		
		if (values.rows() != matrix.columns()) {
			throw new ArrayIndexOutOfBoundsException();
		}
		if (last == capacity) {
			grow();
		}
		for (int i = 0; i < values.rows(); i++) {
			matrix.setQuick(last, i, values.get(i));
		}
		last++;
	}

	@Override
	public void addRow(double... values) {
		if (values.length != matrix.columns()) {
			throw new ArrayIndexOutOfBoundsException();
		}
		if (last == capacity) {
			grow();
		}
		for (int i = 0; i < values.length; i++) {
			matrix.setQuick(last, i, values[i]);
		}
		last++;		
	}

	@Override
	public Matrix toMatrix() {
		if (last == 0 || matrix.columns() == 0) {
			return empty();
		}		
		if (matrix.columns() > 1) {
			if (last == capacity) {
				return new ColtMatrix(matrix);
			} else {
				return new ColtMatrix(matrix.viewPart(0, 0, last, matrix.columns()));
			}
		} else {
			if (last > 1) {
				return new ColtVector(matrix.viewColumn(0).viewPart(0, last));
			} else {
				return new Scalar(matrix.get(0, 0));
			}
		}
		
	}
	
	private void grow() {
		capacity = capacity * 2;
		DoubleMatrix2D newValues = new DenseDoubleMatrix2D(capacity, matrix.columns());
		newValues.viewPart(0, 0, matrix.rows(), matrix.columns()).assign(matrix);
		matrix = newValues;
	}
	
	

}
