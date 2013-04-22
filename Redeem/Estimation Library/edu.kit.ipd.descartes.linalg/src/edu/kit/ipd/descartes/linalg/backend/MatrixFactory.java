package edu.kit.ipd.descartes.linalg.backend;

import edu.kit.ipd.descartes.linalg.MatrixFunction;
import edu.kit.ipd.descartes.linalg.VectorFunction;
import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public interface MatrixFactory {

	MatrixImplementation createMatrix(double[][] values);

	MatrixImplementation createMatrix(int rows, int columns, double fill);

	MatrixImplementation createMatrix(int rows, int columns, DoubleStorage storage);

	MatrixImplementation createMatrix(int rows, int columns, MatrixFunction init);
	
	SquareMatrixImplementation createSquareMatrix(double[][] values);

	SquareMatrixImplementation createSquareMatrix(int size, double fill);
	
	SquareMatrixImplementation createSquareMatrix(int size, MatrixFunction init);
	
	SquareMatrixImplementation createSquareMatrix(int size, DoubleStorage storage);
	
	VectorImplementation createVector(double[] values);

	VectorImplementation createVector(int rows, double fill);

	VectorImplementation createVector(int rows, DoubleStorage storage);

	VectorImplementation createVector(int rows, VectorFunction init);

}
