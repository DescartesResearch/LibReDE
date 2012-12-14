package edu.kit.ipd.descartes.linalg;

import edu.kit.ipd.descartes.linalg.impl.colt.ColtVector;
import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;


public abstract class Vector {
	
	public static Vector create(int rows) {
		return new ColtVector(rows);
	}
	
	public abstract void assign(double...d);
	
	public abstract void set(int row, double value);
	
	public abstract double get(int row);
	
	public abstract int rowCount();
	
	public abstract double sum();
	
	public abstract double multiply(Vector b);
	
	public abstract void mapMultiplyToSelf(double d);
	
	public abstract double[] toArray();

	public abstract void readFrom(DoubleStorage storage);

	public abstract void writeTo(DoubleStorage storage);

}
