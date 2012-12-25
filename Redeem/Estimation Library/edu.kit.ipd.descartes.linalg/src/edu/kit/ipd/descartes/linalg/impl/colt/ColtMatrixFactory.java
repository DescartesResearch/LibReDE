package edu.kit.ipd.descartes.linalg.impl.colt;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.MatrixFactory;
import edu.kit.ipd.descartes.linalg.MatrixInitializer;
import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public class ColtMatrixFactory implements MatrixFactory {

	@Override
	public Matrix create(double[][] values) {
		return new ColtMatrix(values);
	}

	@Override
	public Matrix create(int rows, int columns) {
		return new ColtMatrix(rows, columns);
	}

	@Override
	public Matrix create(int rows, int columns, double fill) {
		return new ColtMatrix(rows, columns, 1);
	}

	@Override
	public Matrix create(int rows, int columns, DoubleStorage storage) {
		return new ColtMatrix(rows, columns, storage);
	}

	@Override
	public Matrix create(int rows, int columns, MatrixInitializer init) {
		return new ColtMatrix(rows, columns, init);
	}
}
