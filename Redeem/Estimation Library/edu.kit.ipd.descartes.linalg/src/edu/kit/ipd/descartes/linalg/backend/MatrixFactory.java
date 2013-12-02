package edu.kit.ipd.descartes.linalg.backend;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.MatrixFunction;
import edu.kit.ipd.descartes.linalg.SquareMatrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorFunction;

public interface MatrixFactory {

	Matrix createMatrix(double[][] values);

	Matrix createMatrix(int rows, int columns, double fill);

	Matrix createMatrix(int rows, int columns, MatrixFunction init);
	
	SquareMatrix createSquareMatrix(double[][] values);

	SquareMatrix createSquareMatrix(int size, double fill);
	
	SquareMatrix createSquareMatrix(int size, MatrixFunction init);
	
	Vector createVector(double[] values);

	Vector createVector(int rows, double fill);

	Vector createVector(int rows, VectorFunction init);

}
