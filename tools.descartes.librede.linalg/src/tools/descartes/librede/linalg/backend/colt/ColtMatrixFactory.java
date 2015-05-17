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

import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.MatrixBuilder;
import tools.descartes.librede.linalg.MatrixFunction;
import tools.descartes.librede.linalg.SquareMatrix;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorBuilder;
import tools.descartes.librede.linalg.VectorFunction;
import tools.descartes.librede.linalg.backend.MatrixFactory;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;

public class ColtMatrixFactory implements MatrixFactory {
	
	public Matrix createMatrix(DoubleMatrix2D wrapped) {
		return new ColtMatrix(wrapped);
	}

	@Override
	public Matrix createMatrix(double[][] values) {
		return new ColtMatrix(values);
	}

	@Override
	public Matrix createMatrix(int rows, int columns, double fill) {
		return new ColtMatrix(rows, columns, fill);
	}

	@Override
	public Matrix createMatrix(int rows, int columns, MatrixFunction init) {
		return new ColtMatrix(rows, columns, init);
	}
	
	public Vector createVector(DoubleMatrix1D wrapped) {
		return new ColtVector(wrapped);
	}
	
	@Override
	public Vector createVector(double[] values) {
		return new ColtVector(values);
	}

	@Override
	public Vector createVector(int rows, double fill) {
		return new ColtVector(rows, fill);
	}

	@Override
	public Vector createVector(int rows, VectorFunction init) {
		return new ColtVector(rows, init);
	}
	
	public SquareMatrix createSquareMatrix(DoubleMatrix2D wrapped) {
		return new ColtSquareMatrix(wrapped);
	}
	
	@Override
	public SquareMatrix createSquareMatrix(double[][] values) {
		return new ColtSquareMatrix(values);
	}

	@Override
	public SquareMatrix createSquareMatrix(int size, double fill) {
		return new ColtSquareMatrix(size, fill);
	}

	@Override
	public SquareMatrix createSquareMatrix(int size, MatrixFunction init) {
		return new ColtSquareMatrix(size, init);
	}
	
	@Override
	public VectorBuilder createVectorBuilder(int maxRows) {
		return new ColtVectorBuilder(maxRows);
	}
	
	@Override
	public MatrixBuilder createMatrixBuilder(int maxRows, int columns) {
		return new ColtMatrixBuilder(maxRows, columns);
	}
}
