/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2014, by Simon Spinner and Contributors.
 *
 * Project Info:   http://www.descartes-research.net/
 *
 * All rights reserved. This software is made available under the terms of the
 * Eclipse Public License (EPL) v1.0 as published by the Eclipse Foundation
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse Public License (EPL)
 * for more details.
 *
 * You should have received a copy of the Eclipse Public License (EPL)
 * along with this software; if not visit http://www.eclipse.org or write to
 * Eclipse Foundation, Inc., 308 SW First Avenue, Suite 110, Portland, 97204 USA
 * Email: license (at) eclipse.org
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 */
package edu.kit.ipd.descartes.librede.nnls;

import static edu.kit.ipd.descartes.linalg.LinAlg.matrix;
import static edu.kit.ipd.descartes.linalg.LinAlg.scalar;
import static edu.kit.ipd.descartes.linalg.LinAlg.vector;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;

import com.sun.jna.Memory;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;

import edu.kit.ipd.descartes.librede.estimation.algorithm.IEstimationAlgorithm;
import edu.kit.ipd.descartes.librede.estimation.exceptions.EstimationException;
import edu.kit.ipd.descartes.librede.estimation.models.observation.IObservationModel;
import edu.kit.ipd.descartes.librede.estimation.models.observation.functions.ILinearOutputFunction;
import edu.kit.ipd.descartes.librede.estimation.models.state.IStateModel;
import edu.kit.ipd.descartes.librede.estimation.models.state.constraints.Unconstrained;
import edu.kit.ipd.descartes.librede.nnls.backend.NNLSLibrary;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;

/**
 * This class implements Least-Squares (NNLS) algorithm.
 * 
 * @author Mehran Saliminia
 * 
 */
public class LeastSquaresRegression
		implements
		IEstimationAlgorithm<IStateModel<Unconstrained>, IObservationModel<ILinearOutputFunction, Scalar>> {

	private IObservationModel<ILinearOutputFunction, Scalar> observationModel;
	private IStateModel<Unconstrained> stateModel;
	private ILinearOutputFunction outputFunction;

	// contains current measurements
	private Matrix independentVariables;
	private Vector dependentVariables;
	private int numObservations;

	private final int SIZE_OF_DOUBLE = 8;
	private final int SIZE_OF_INT = 8;
	private final int MIN_SIZE_OF_ESTIMATION = 2;
	private static final DoubleFactory2D FACTORY2D = DoubleFactory2D.dense;

	public LeastSquaresRegression() {
	}

	@Override
	public void initialize(IStateModel<Unconstrained> stateModel,
			IObservationModel<ILinearOutputFunction, Scalar> observationModel, int estimationWindow) {
		this.observationModel = observationModel;
		this.stateModel = stateModel;
		
		independentVariables = matrix(estimationWindow, stateModel.getStateSize(), Double.NaN);
		dependentVariables = (Vector)matrix(estimationWindow, 1, Double.NaN);
		numObservations = 0;
	}

	/**
	 * This method uses colt library to solves A * X = B.
	 * 
	 * @param A
	 * @param B
	 * @return X; a new independent matrix; solution if A is square, least
	 *         squares solution otherwise.
	 * 
	 */
	private Matrix solve(Matrix A, Matrix B) {
		Algebra alg = new Algebra();
		DoubleMatrix2D a = FACTORY2D.make(A.toArray2D());
		DoubleMatrix2D b = FACTORY2D.make(B.toArray2D());
		DoubleMatrix2D x = alg.solve(a, b);
		return matrix(x.toArray());
	}

	/**
	 * This method implements the Non-Negative Least-Squares (NNLS) algorithm.
	 * We are initially given the m*n matrix E and the m-vector F. Minimize
	 * ||Ex-F|| subject to x >= 0.
	 * 
	 * @param e
	 *            m*n matrix
	 * @param f
	 *            m-vector
	 * @return the solution Vector
	 * @throws EstimationException 
	 */
	//public for testing
	public Vector nnls(Matrix e, Vector f){
		
			// The solution vector
			Vector result;

			// Check inputs
			if (e == null || f == null || e.rows() != f.rows())
				throw new IllegalArgumentException("[NNLS]: Invalid inputs!");

			// Inputs
			int size_of_m_vector = f.rows();

			IntByReference mda = new IntByReference(size_of_m_vector);
			IntByReference m = new IntByReference(size_of_m_vector); // mda == m
			IntByReference n = new IntByReference(e.columns());
			Memory a = new Memory(SIZE_OF_DOUBLE * mda.getValue()
					* n.getValue()); // 8 ==
			// sizeof(double)
			double[] eArray = new double[size_of_m_vector * (e.columns())];
			double[] fArray = new double[size_of_m_vector];
			for (int i = 0; i < size_of_m_vector; ++i)
				fArray[i] = f.get(i);
			int count = 0;
			for (int i = 0; i < e.columns(); ++i)
				for (int j = 0; j < e.rows(); ++j) {
					eArray[count] = e.get(j, i); // column-order
					count++;
				}
			a.write(0, eArray, 0, mda.getValue() * n.getValue()); // Fortran
																	// expects
																	// column-order
																	// array!
			Memory b = new Memory(SIZE_OF_DOUBLE * mda.getValue());
			b.write(0, fArray, 0, mda.getValue());

			// Outputs
			Memory x = new Memory(SIZE_OF_DOUBLE * n.getValue());
			DoubleByReference rnorm = new DoubleByReference();
			IntByReference mode = new IntByReference();

			// Temporary working arrays
			Memory w = new Memory(SIZE_OF_DOUBLE * n.getValue());
			Memory zz = new Memory(SIZE_OF_DOUBLE * m.getValue());
			Memory index = new Memory(SIZE_OF_INT * n.getValue());

			NNLSLibrary.INSTANCE.nnls_(a, mda, m, n, b, x, rnorm, w, zz, index,
					mode);

			double[] res = new double[n.getValue()];
			x.read(0, res, 0, res.length);
			result = vector(res);
			return result;
	}
	
	@Override
	public void update() throws EstimationException {
		outputFunction = observationModel.getOutputFunction(0);
		
		numObservations++;
		
		dependentVariables = dependentVariables.circshift(1).set(0, outputFunction.getObservedOutput());
		independentVariables = independentVariables.circshift(1).setRow(0, outputFunction.getIndependentVariables());
	}

	@Override
	public Vector estimate() throws EstimationException {
		// when the sample size is small
		if (numObservations < MIN_SIZE_OF_ESTIMATION) {
			return vector(0);
		} else if (numObservations < dependentVariables.rows()) {
			return nnls(independentVariables.rows(0, numObservations - 1), dependentVariables.rows(0, numObservations - 1));
		} else {
			return nnls(independentVariables, dependentVariables);
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
}
