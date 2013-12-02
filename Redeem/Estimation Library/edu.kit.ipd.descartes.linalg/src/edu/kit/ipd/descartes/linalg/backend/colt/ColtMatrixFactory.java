package edu.kit.ipd.descartes.linalg.backend.colt;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.MatrixFunction;
import edu.kit.ipd.descartes.linalg.SquareMatrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorFunction;
import edu.kit.ipd.descartes.linalg.backend.MatrixFactory;

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
}
