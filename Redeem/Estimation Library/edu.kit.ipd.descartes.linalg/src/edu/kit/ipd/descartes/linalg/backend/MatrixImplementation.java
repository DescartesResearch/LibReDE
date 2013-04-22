package edu.kit.ipd.descartes.linalg.backend;

import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public interface MatrixImplementation {
	
	MatrixImplementation abs();

	double sum();

	double norm1();

	double norm2();

	MatrixImplementation transpose();
	
	MatrixImplementation appendRows(MatrixImplementation a);
	
	MatrixImplementation appendColumns(MatrixImplementation a);
	
	double get(int row, int col);

	int rows();

	int columns();

	VectorImplementation row(int row);

	VectorImplementation column(int column);
	
	MatrixImplementation plus(double a);
	
	MatrixImplementation minus(double a);
	
	MatrixImplementation times(double a);
	
	MatrixImplementation plus(MatrixImplementation a);
	
	MatrixImplementation minus(MatrixImplementation a);
	
	MatrixImplementation arrayMultipliedBy(MatrixImplementation a);
	
	MatrixImplementation multipliedBy(MatrixImplementation a);
	
	double[] toArray1D();

	double[][] toArray2D();

	void toDoubleStorage(DoubleStorage storage);

}
