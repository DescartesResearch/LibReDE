package edu.kit.ipd.descartes.redeem.nnls;

import static edu.kit.ipd.descartes.linalg.LinAlg.horzcat;
import static edu.kit.ipd.descartes.linalg.LinAlg.matrix;
import static edu.kit.ipd.descartes.linalg.LinAlg.vector;
import static edu.kit.ipd.descartes.linalg.LinAlg.vertcat;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;

import com.sun.jna.Memory;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.algorithm.IEstimationAlgorithm;
import edu.kit.ipd.descartes.redeem.estimation.exceptions.EstimationException;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.IObservationModel;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.ILinearOutputFunction;
import edu.kit.ipd.descartes.redeem.estimation.models.state.IStateModel;
import edu.kit.ipd.descartes.redeem.estimation.models.state.constraints.Unconstrained;
import edu.kit.ipd.descartes.redeem.nnls.backend.NNLSLibrary;

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

	// contains all measured throughputs
	private Matrix throughputs;
	// contains all measured utilizations
	private Vector utilizations;

	private final int SIZE_OF_DOUBLE = 8;
	private final int SIZE_OF_INT = 8;
	private final int MIN_SIZE_OF_ESTIMATION = 2;
	private static final DoubleFactory2D FACTORY2D = DoubleFactory2D.dense;

	public LeastSquaresRegression() {
	}

	@Override
	public void initialize(IStateModel<Unconstrained> stateModel,
			IObservationModel<ILinearOutputFunction, Scalar> observationModel) {
		this.observationModel = observationModel;
		this.stateModel = stateModel;
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
	public Vector estimate() throws EstimationException {
		if (observationModel.iterator().hasNext())
			outputFunction = observationModel.iterator().next();

		utilizations = (Vector) horzcat(utilizations,
				vector(outputFunction.getObservedOutput()));
		throughputs = vertcat(throughputs,
				outputFunction.getIndependentVariables());

		// when the sample size is small
		if (utilizations.columns() < MIN_SIZE_OF_ESTIMATION)
			return vector(0);
		else
			return nnls(throughputs, utilizations);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
}
