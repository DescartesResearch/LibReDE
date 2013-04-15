package edu.kit.ipd.descartes.linalg.impl.colt;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.MatrixFactory;
import edu.kit.ipd.descartes.linalg.MatrixInitializer;
import edu.kit.ipd.descartes.linalg.SquareMatrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorInitializer;
import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public class ColtMatrixFactory implements MatrixFactory {

	@Override
	public Matrix createMatrix(double[][] values) {
		return new ColtMatrix(values);
	}

	@Override
	public Matrix createMatrix(int rows, int columns) {
		return new ColtMatrix(rows, columns);
	}

	@Override
	public Matrix createMatrix(int rows, int columns, double fill) {
		return new ColtMatrix(rows, columns, fill);
	}

	@Override
	public Matrix createMatrix(int rows, int columns, DoubleStorage storage) {
		return new ColtMatrix(rows, columns, storage);
	}

	@Override
	public Matrix createMatrix(int rows, int columns, MatrixInitializer init) {
		return new ColtMatrix(rows, columns, init);
	}
	
	@Override
	public SquareMatrix createSquareMatrix(Matrix wrapped) {
		return new ColtSquareMatrix(wrapped);
	}
	
	@Override
	public Vector createVector(int rows) {
		return new ColtVector(rows);
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
	public Vector createVector(int rows, DoubleStorage storage) {
		return new ColtVector(rows, storage);
	}

	@Override
	public Vector createVector(int rows, VectorInitializer init) {
		return new ColtVector(rows, init);
	}

	@Override
	public Vector createVector(Vector[] vectors) {
		return new ColtVector(vectors);
	}
}
