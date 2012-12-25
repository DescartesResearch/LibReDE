package edu.kit.ipd.descartes.linalg;

import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public interface MatrixFactory {

	Matrix create(double[][] values);

	Matrix create(int rows, int columns);

	Matrix create(int rows, int columns, double fill);

	Matrix create(int rows, int columns, DoubleStorage storage);

	Matrix create(int rows, int columns, MatrixInitializer init);

}
