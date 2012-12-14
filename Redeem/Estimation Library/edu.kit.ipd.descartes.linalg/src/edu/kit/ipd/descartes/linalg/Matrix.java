package edu.kit.ipd.descartes.linalg;

import edu.kit.ipd.descartes.linalg.impl.colt.ColtMatrix;
import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public abstract class Matrix {
	
	public static Matrix create(int rows, int columns) {
		return new ColtMatrix(rows, columns);
	}	
	
	public abstract void assign(double[][] values);
	
	public abstract void set(int row, int col, double value);
	
	public abstract double get(int row, int col);
	
	public abstract int rowCount();
	
	public abstract Vector rowVector(int row);
	
	public abstract Vector columnVector(int column);
	
	public abstract int columnCount();
	
	public abstract double[][] toArray();
	
	public abstract void readFrom(DoubleStorage storage);
	
	public abstract void writeTo(DoubleStorage storage);
	
	public abstract Vector multiply(Vector a);
	
	public abstract Matrix multiply(Matrix a);

}
