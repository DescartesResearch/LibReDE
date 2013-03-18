package edu.kit.ipd.descartes.linalg;

import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public interface MatrixFactory {

	Matrix createMatrix(double[][] values);

	Matrix createMatrix(int rows, int columns);

	Matrix createMatrix(int rows, int columns, double fill);

	Matrix createMatrix(int rows, int columns, DoubleStorage storage);

	Matrix createMatrix(int rows, int columns, MatrixInitializer init);
	
	SquareMatrix createSquareMatrix(Matrix wrapped);
	
	Vector createVector(int rows);

	Vector createVector(double[] values);

	Vector createVector(int rows, double fill);

	Vector createVector(int rows, DoubleStorage storage);

	Vector createVector(int rows, VectorInitializer init);

	Vector createVector(Vector[] vectors);

}
