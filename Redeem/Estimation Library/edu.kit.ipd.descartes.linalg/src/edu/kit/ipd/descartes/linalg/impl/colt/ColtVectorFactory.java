package edu.kit.ipd.descartes.linalg.impl.colt;

import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorFactory;
import edu.kit.ipd.descartes.linalg.VectorInitializer;
import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public class ColtVectorFactory implements VectorFactory {

	@Override
	public Vector create(int rows) {
		return new ColtVector(rows);
	}

	@Override
	public Vector create(double[] values) {
		return new ColtVector(values);
	}

	@Override
	public Vector create(int rows, double fill) {
		return new ColtVector(rows, fill);
	}

	@Override
	public Vector create(int rows, DoubleStorage storage) {
		return new ColtVector(rows, storage);
	}

	@Override
	public Vector create(int rows, VectorInitializer init) {
		return new ColtVector(rows, init);
	}

	@Override
	public Vector create(Vector[] vectors) {
		return new ColtVector(vectors);
	}

}
