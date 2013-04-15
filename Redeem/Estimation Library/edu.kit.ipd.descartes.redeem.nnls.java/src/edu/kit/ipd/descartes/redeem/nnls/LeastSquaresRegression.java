package edu.kit.ipd.descartes.redeem.nnls;

import static edu.kit.ipd.descartes.linalg.LinAlg.matrix;
import static edu.kit.ipd.descartes.linalg.LinAlg.row;
import static edu.kit.ipd.descartes.linalg.LinAlg.vector;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;

import com.sun.jna.Memory;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.bayesplusplus.MeasurementModel;
import edu.kit.ipd.descartes.redeem.bayesplusplus.StateModel;
import edu.kit.ipd.descartes.redeem.estimation.models.algorithm.IEstimationAlgorithm;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.IObservationModel;
import edu.kit.ipd.descartes.redeem.estimation.models.state.ConstantStateModel;
import edu.kit.ipd.descartes.redeem.estimation.models.state.IStateModel;
import edu.kit.ipd.descartes.redeem.nnls.backend.NNLSLibrary;

/**
 * This class implements Least-Squares (NNLS) algorithm.
 * 
 * @author Mehran Saliminia
 * 
 */
public class LeastSquaresRegression<S extends ConstantStateModel & IJacobiCalculator, O extends LinearObservationModel & IJacobiCalculator>
		implements IEstimationAlgorithm<S, O> {

	private S stateModel;
	private O observationModel;

	private Vector stateNoiseCovariance = vector(1.0);
	private Matrix stateNoiseCoupling = matrix(row(1));

	private Vector observeNoise = vector(0.0001);

	private final int SIZE_OF_DOUBLE = 8;
	private final int SIZE_OF_INT = 8;
	private static final cern.colt.matrix.DoubleFactory2D Factory2D = cern.colt.matrix.DoubleFactory2D.dense;

	public LeastSquaresRegression() {

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
	public Matrix solve(Matrix A, Matrix B) {
		Algebra alg = new Algebra();
		DoubleMatrix2D a = Factory2D.make(A.toArray2D());
		DoubleMatrix2D b = Factory2D.make(B.toArray2D());
		DoubleMatrix2D x = alg.solve(a, b);
		Matrix X = matrix(x.toArray());
		return X;
	}

	/**
	 * This method implements the Non-Negative Least-Squares (NNLS) algorithm.
	 * We are initially given the m*n matrix E and the m-vector F. Minimize
	 * ||Ex-F|| subject to x >= 0.
	 * 
	 * @param E
	 *            m*n matrix
	 * @param F
	 *            m-vector
	 * @return the solution Vector
	 */
	public Vector nnls(Matrix E, Vector F) {
		try {
			System.loadLibrary("NNLS");
			// The solution vector
			Vector result;

			// Check inputs
			if (E == null || F == null || E.rows() != F.rows())
				throw new Exception("[NNLS]: Invalid inputs!");

			// Inputs
			int size_of_m_vector = F.rows();

			IntByReference mda = new IntByReference(size_of_m_vector);
			IntByReference m = new IntByReference(size_of_m_vector); // mda == m
			IntByReference n = new IntByReference(E.columns());
			Memory a = new Memory(SIZE_OF_DOUBLE * mda.getValue()
					* n.getValue()); // 8 ==
			// sizeof(double)
			double[] e = new double[size_of_m_vector * (E.columns())];
			double[] f = new double[size_of_m_vector];
			for (int i = 0; i < size_of_m_vector; ++i)
				f[i] = F.get(i);
			int count = 0;
			for (int i = 0; i < E.columns(); ++i)
				for (int j = 0; j < E.rows(); ++j) {
					e[count] = E.get(j, i); // column-order
					count++;
				}
			a.write(0, e, 0, mda.getValue() * n.getValue()); // Fortran expects
																// column-order
																// array!
			Memory b = new Memory(SIZE_OF_DOUBLE * mda.getValue());
			b.write(0, f, 0, mda.getValue());

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

		} catch (Exception ex) {
			System.out.println("[NNLS]: failed!");
			return null;
		}
	}

	public void runEstimation() {

		StateModelWrapper<S> stateModelWrapper = new StateModelWrapper<S>(
				stateModel, stateNoiseCovariance, stateNoiseCoupling);
		ObservationModelWrapper<O> obModelWrapper = new ObservationModelWrapper<O>(
				observationModel, stateModel.getStateSize(), observeNoise);

		while (true) {
			 Vector estimation = predict(observationModel); //TODO: save estimates

			if (!observationModel.nextObservation()) {
				break;
			}
		}
	}

	private Vector predict(LinearObservationModel model) {
		Vector result = this.nnls(model.getInputMatrix(),
				model.getObservedOutput());
		return result;
	}

	private static class StateModelWrapper<S extends IStateModel & IJacobiCalculator>
			extends StateModel {

		private S model;

		public StateModelWrapper(S model, Vector stateNoiseCovariance,
				Matrix stateNoiseCoupling) {
			super(model.getStateSize(), stateNoiseCovariance,
					stateNoiseCoupling);
			this.model = model;
		}

		@Override
		public Vector nextState(Vector currentState) {
			return model.getNextState(currentState);
		}

		@Override
		public void calculateJacobi(Vector currentState) {
			setJacobi(model.getJacobiMatrix(currentState));
		}
	}

	private static class ObservationModelWrapper<O extends IObservationModel & IJacobiCalculator>
			extends MeasurementModel {

		private O model;

		public ObservationModelWrapper(O model, int stateSize,
				Vector observeNoiseCovariance) {
			super(stateSize, model.getObservationSize(), observeNoiseCovariance);
			this.model = model;
		}

		@Override
		public Vector nextObservation(Vector currentState,
				Vector... additionalInfo) {
			return model.getCalculatedOutputVector(currentState);
		}

		@Override
		public void calculateJacobi(Vector currentState,
				Vector... additionalInfo) {
			setJacobi(model.getJacobiMatrix(currentState));
		}

	}
}
