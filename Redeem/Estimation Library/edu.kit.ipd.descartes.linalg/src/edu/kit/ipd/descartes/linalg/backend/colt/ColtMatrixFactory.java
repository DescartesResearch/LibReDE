package edu.kit.ipd.descartes.linalg.backend.colt;

import edu.kit.ipd.descartes.linalg.MatrixFunction;
import edu.kit.ipd.descartes.linalg.VectorFunction;
import edu.kit.ipd.descartes.linalg.backend.MatrixFactory;
import edu.kit.ipd.descartes.linalg.backend.MatrixImplementation;
import edu.kit.ipd.descartes.linalg.backend.SquareMatrixImplementation;
import edu.kit.ipd.descartes.linalg.backend.VectorImplementation;
import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public class ColtMatrixFactory implements MatrixFactory {

	@Override
	public MatrixImplementation createMatrix(double[][] values) {
		return new ColtMatrix(values);
	}

	@Override
	public MatrixImplementation createMatrix(int rows, int columns, double fill) {
		ColtMatrix matrix = new ColtMatrix(rows, columns);
		matrix.assign(fill);
		return matrix;
	}

	@Override
	public MatrixImplementation createMatrix(int rows, int columns, DoubleStorage storage) {
		return new ColtMatrix(rows, columns, storage);
	}

	@Override
	public MatrixImplementation createMatrix(int rows, int columns, MatrixFunction init) {
		ColtMatrix matrix = new ColtMatrix(rows, columns);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				matrix.setQuick(i, j, init.cell(i, j));
			}
		}
		return matrix;
	}
	
	@Override
	public VectorImplementation createVector(double[] values) {
		return new ColtVector(values);
	}

	@Override
	public VectorImplementation createVector(int rows, double fill) {
		ColtVector vector = new ColtVector(rows);
		vector.assign(fill);
		return vector;
	}

	@Override
	public VectorImplementation createVector(int rows, DoubleStorage storage) {
		return new ColtVector(rows, storage);
	}

	@Override
	public VectorImplementation createVector(int rows, VectorFunction init) {
		ColtVector vector = new ColtVector(rows);
		for (int i = 0; i < rows; i++) {
			vector.setQuick(i, init.cell(i));
		}
		return vector;
	}
	
	@Override
	public SquareMatrixImplementation createSquareMatrix(double[][] values) {
		return new ColtSquareMatrix(values);
	}

	@Override
	public SquareMatrixImplementation createSquareMatrix(int size, double fill) {
		ColtSquareMatrix matrix = new ColtSquareMatrix(size);
		matrix.assign(fill);
		return matrix;
	}

	@Override
	public SquareMatrixImplementation createSquareMatrix(int size, MatrixFunction init) {
		ColtSquareMatrix matrix = new ColtSquareMatrix(size);
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				matrix.setQuick(i, j, init.cell(i, j));
			}
		}
		return matrix;
	}

	@Override
	public SquareMatrixImplementation createSquareMatrix(int size, DoubleStorage storage) {
		return new ColtSquareMatrix(size, storage);
	}
}
