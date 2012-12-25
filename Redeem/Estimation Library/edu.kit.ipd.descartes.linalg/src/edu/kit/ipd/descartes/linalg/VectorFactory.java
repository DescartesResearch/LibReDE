package edu.kit.ipd.descartes.linalg;

import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public interface VectorFactory {

	Vector create(int rows);

	Vector create(double[] values);

	Vector create(int rows, double fill);

	Vector create(int rows, DoubleStorage storage);

	Vector create(int rows, VectorInitializer init);

	Vector create(Vector[] vectors);

}
