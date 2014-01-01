package edu.kit.ipd.descartes.linalg;

public interface Vector extends Matrix {
	
	double get(int row);
	
	Vector set(int row, double value);
	
	Vector set(Range rows, Vector values);
	
	Vector slice(Range range);
	
	Vector subset(int...indeces);
	
	Vector subset(int start, int end);
	
	Vector plus(Matrix a);

	Vector plus(double a);

	Vector minus(Matrix a);

	Vector minus(double a);

	Matrix multipliedBy(Matrix a);
	
	Matrix arrayMultipliedBy(Matrix a);

	Vector times(double a);
	
	Vector abs();
	
	double dot(Vector b);
	
	double mean();	
}
