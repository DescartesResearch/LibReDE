package edu.kit.ipd.descartes.linalg;


public interface SquareMatrix extends Matrix {

	double det();
	
	SquareMatrix inverse();
	
	SquareMatrix pow(int a);
	
	double rank();
	
	double trace();

}
