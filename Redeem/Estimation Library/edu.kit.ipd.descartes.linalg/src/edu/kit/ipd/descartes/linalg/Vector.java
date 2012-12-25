package edu.kit.ipd.descartes.linalg;

import edu.kit.ipd.descartes.linalg.impl.colt.ColtVectorFactory;
import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public abstract class Vector {

	private static VectorFactory factory = new ColtVectorFactory();

	public static Vector zeros(int rows) {
		return factory.create(rows);
	}

	public static Vector ones(int rows) {
		return factory.create(rows, 1);
	}

	public static Vector vector(double... values) {
		return factory.create(values);
	}

	public static Vector vector(int rows, VectorInitializer init) {
		return factory.create(rows, init);
	}

	public static Vector vector(int rows, DoubleStorage storage) {
		return factory.create(rows, storage);
	}

	public static Vector vector(Vector... vectors) {
		return factory.create(vectors);
	}

	public static Vector abs(Vector v) {
		return v.abs();
	}

	public static double sum(Vector v) {
		return v.sum();
	}

	public static double norm1(Vector v) {
		return v.norm1();
	}

	public static double norm2(Vector v) {
		return v.norm2();
	}

	public static int min(Vector v) {
		double min = Double.MAX_VALUE;
		int idx = -1;
		for (int i = 0; i < v.rows(); i++) {
			if (v.get(i) < min) {
				min = v.get(i);
				idx = i;
			}
		}
		return idx;
	}

	public static int max(Vector v) {
		double max = Double.MIN_VALUE;
		int idx = -1;
		for (int i = 0; i < v.rows(); i++) {
			if (v.get(i) > max) {
				max = v.get(i);
				idx = i;
			}
		}
		return idx;
	}

	public abstract double get(int row);

	public abstract int rows();

	/*
	 * Algebra functions
	 */

	public abstract double multipliedBy(Vector b);

	public abstract Vector times(double d);

	public abstract Vector plus(Vector a);

	public abstract Vector plus(double d);

	public abstract Vector minus(Vector a);

	public abstract Vector minus(double d);

	/*
	 * Conversion functions
	 */

	public abstract double[] toArray();

	public abstract void toDoubleStorage(DoubleStorage storage);

	/*
	 * Internal functions
	 */

	protected abstract Vector abs();

	protected abstract double sum();

	protected abstract double norm1();

	protected abstract double norm2();
}
