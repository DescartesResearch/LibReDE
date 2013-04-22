package edu.kit.ipd.descartes.linalg;

import edu.kit.ipd.descartes.linalg.backend.MatrixImplementation;
import edu.kit.ipd.descartes.linalg.backend.SquareMatrixImplementation;

public class SquareMatrix extends Matrix {

	SquareMatrix(SquareMatrixImplementation delegate) {
		super(delegate);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	<M extends Matrix> M newInstance(MatrixImplementation delegate) {
		return (M) new SquareMatrix((SquareMatrixImplementation)delegate);
	}

}
