package edu.kit.ipd.descartes.linalg;

import edu.kit.ipd.descartes.linalg.backend.SharedBuffer;


public interface Matrix {
	
	double get(int row, int col);
	
	<M extends Matrix> M set(int row, int col, double value);
	
	<M extends Matrix> M appendColumns(Matrix a);
	
	<M extends Matrix> M appendRows(Matrix a);

	int rows();

	int columns();

	Vector row(int row);

	Vector column(int column);
	
	boolean isVector();
	
	boolean isScalar();
	
	boolean isEmpty();

	/*
	 * Algebra functions
	 */

	<M extends Matrix> M plus(M a);

	<M extends Matrix> M plus(double a);

	<M extends Matrix> M minus(M a);

	<M extends Matrix> M minus(double a);

	<M extends Matrix> M multipliedBy(M a);
	
	<M extends Matrix> M arrayMultipliedBy(M a);

	<M extends Matrix> M times(double a);	
	
	double norm1();
	
	double norm2();
	
	double sum();
	
	<M extends Matrix> M abs();
	
	<M extends Matrix> M transpose();
	
	public abstract int[] sort(int column);
	
	/*
	 * Conversion functions
	 */
	
	public abstract double[] toArray1D();

	public abstract double[][] toArray2D();
}
