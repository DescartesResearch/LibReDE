package edu.kit.ipd.descartes.linalg;

public interface Vector extends Matrix {
	
	double get(int row);
	
	<V extends Vector> V set(int row, double value);
	
	<V extends Vector> V set(Range rows, Vector values);
	
	<V extends Vector> V slice(Range range);
	
	<V extends Vector> V subset(int[] indeces);
	
	double dot(Vector b);
	
	double mean();	
}
