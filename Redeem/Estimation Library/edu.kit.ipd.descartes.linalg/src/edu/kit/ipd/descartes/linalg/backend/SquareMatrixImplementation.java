package edu.kit.ipd.descartes.linalg.backend;


public interface SquareMatrixImplementation extends MatrixImplementation {
	
	double det();

	SquareMatrixImplementation inverse();

	double rank();

	double trace();

	SquareMatrixImplementation pow(int p);

}
