package edu.kit.ipd.descartes.redeem.estimation.models;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;

public interface IHessianMatrices {
	
	Matrix[] getHessianMatrices(Vector state);

}
