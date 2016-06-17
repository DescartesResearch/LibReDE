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
package tools.descartes.librede.bayesplusplus;

import static tools.descartes.librede.linalg.LinAlg.matrix;
import static tools.descartes.librede.linalg.LinAlg.nanmean;
import static tools.descartes.librede.linalg.LinAlg.vector;
import static tools.descartes.librede.nativehelper.NativeHelper.nativeVector;
import static tools.descartes.librede.nativehelper.NativeHelper.toNative;

import com.sun.jna.Pointer;

import tools.descartes.librede.algorithm.AbstractEstimationAlgorithm;
import tools.descartes.librede.bayesplusplus.backend.BayesPlusPlusLibrary;
import tools.descartes.librede.bayesplusplus.backend.FCallback;
import tools.descartes.librede.bayesplusplus.backend.HCallback;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.exceptions.EstimationException;
import tools.descartes.librede.exceptions.InitializationException;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.MatrixFunction;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorFunction;
import tools.descartes.librede.models.EstimationProblem;
import tools.descartes.librede.models.State;
import tools.descartes.librede.models.state.constraints.IStateBoundsConstraint;
import tools.descartes.librede.models.state.constraints.IStateConstraint;
import tools.descartes.librede.models.variables.OutputVariable;
import tools.descartes.librede.nativehelper.NativeHelper;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.registry.ParameterDefinition;
import tools.descartes.librede.repository.IRepositoryCursor;

@Component(displayName="Extended Kalman Filter")
public class ExtendedKalmanFilter extends AbstractEstimationAlgorithm {

	// Callback function from native library for the observation model
	private class HFunction implements HCallback {

		private Pointer outputBuffer = NativeHelper.allocateDoubleArray(outputSize);
		private Pointer jacobiBuffer = NativeHelper.allocateDoubleArray(outputSize * stateSize);;

		@Override
		public Pointer execute(Pointer x) {
			State currentState = new State(getStateModel(), nativeVector(stateSize, x), 1);

			double[] value = new double[outputSize];
			double[][] jacobi = new double[outputSize][stateSize];
			int[] idx = new int[stateSize];
			for (int i = 0; i < outputSize; i++) {
				OutputVariable v = getObservationModel().getOutputFunction(i).getCalculatedOutput(currentState);
				value[i] = v.getDerivativeStructure().getValue();
				for (int j = 0; j < stateSize; j++) {
					idx[j] = 1;
					jacobi[i][j] = v.getDerivativeStructure().getPartialDerivative(idx);
					idx[j] = 0;
				}
			}
			toNative(jacobiBuffer, matrix(jacobi));
			BayesPlusPlusLibrary.set_Hx(nativeObservationModel, jacobiBuffer, stateSize, outputSize);

			toNative(outputBuffer, vector(value));
			return outputBuffer;
		}

	}

	// Callback function from native library for the state model
	private class FFunction implements FCallback {

		private Pointer stateBuffer = NativeHelper.allocateDoubleArray(stateSize);
		private Pointer jacobiBuffer = NativeHelper.allocateDoubleArray(stateSize * stateSize);

		@Override
		public Pointer execute(Pointer x) {
			State currentState = new State(getStateModel(), nativeVector(stateSize, x), 1);

			State nextState = getStateModel().step(currentState);
			toNative(jacobiBuffer, nextState.getStateJacobiMatrix());
			BayesPlusPlusLibrary.set_Fx(nativeStateModel, jacobiBuffer, stateSize);

			toNative(stateBuffer, nextState.getVector());
			return stateBuffer;
		}
	}
	
	@ParameterDefinition(name = "StateNoiseCovariance", label = "State Noise Covariance", defaultValue = "1.0")
	private double stateNoiseCovarianceConstant = 1.0;
	
	@ParameterDefinition(name = "StateNoiseCoupling", label = "State Noise Coupling", defaultValue = "1.0")
	private double stateNoiseCouplingConstant = 1.0;
	
	@ParameterDefinition(name = "ObserveNoiseCovariance", label = "Observe Noise Covariance", defaultValue = "0.0001")
	private double observeNoiseCovarianceConstant = 0.0001;
	
	@ParameterDefinition(name = "BoundsFactor", label = "Bounds factor", defaultValue = "0.9")
	private double boundsFactor = 0.9;
	
	@ParameterDefinition(name = "InitialBoundsDistance", label = "Initial bounds distance", defaultValue = "1e-4")
	private double initialBoundsDistance = 1e-4;

	private int stateSize;
	private int outputSize;

	private Vector stateNoiseCovariance;
	private Matrix stateNoiseCoupling;

	private Vector observeNoise;

	private Vector lastEstimate;
	private Matrix estimates;
	
	private Vector lowerStateBounds;
	private Vector upperStateBounds;
	
	private boolean initialized = false;

	/*
	 * Callback functions from native code. IMPORTANT: References to these
	 * objects need to be retained here, so that GC does not release these
	 * objects.
	 */
	private FFunction fcallback;
	private HFunction hcallback;

	private Pointer nativeObservationModel = null;
	private Pointer nativeStateModel = null;
	private Pointer nativeScheme = null;
	private Pointer stateBuffer;
	private Pointer stateCovarianceBuffer;
	
	private void initNativeKalmanFilter(Vector initialState) throws EstimationException {
		nativeScheme = BayesPlusPlusLibrary.create_covariance_scheme(stateSize);
		if (nativeScheme == null) {
			throw new EstimationException("Could not create kalman filter: "
					+ BayesPlusPlusLibrary.get_last_error());
		}

		toNative(stateBuffer, initialState);

		Matrix initialCovariance = getInitialStateCovariance(initialState);
		toNative(stateCovarianceBuffer, initialCovariance);

		if (BayesPlusPlusLibrary.init_kalman(nativeScheme, stateBuffer, stateCovarianceBuffer, stateSize) == BayesPlusPlusLibrary.ERROR) {
			throw new EstimationException("Could not initialize kalman filter: "
					+ BayesPlusPlusLibrary.get_last_error());
		}
	}

	private void initNativeStateModel() throws EstimationException {
		fcallback = new FFunction();

		nativeStateModel = BayesPlusPlusLibrary.create_linrz_predict_model(stateSize, stateSize, fcallback);
		if (nativeStateModel == Pointer.NULL) {
			throw new EstimationException("Error creating state model: " + BayesPlusPlusLibrary.get_last_error());
		}
		
		stateNoiseCovariance = vector(stateSize, new VectorFunction() {			
			@Override
			public double cell(int row) {
				return stateNoiseCovarianceConstant;
			}
		});
		toNative(stateBuffer, stateNoiseCovariance);
		BayesPlusPlusLibrary.set_q(nativeStateModel, stateBuffer, stateSize);

		Pointer temp = NativeHelper.allocateDoubleArray(stateSize * stateSize);		
		stateNoiseCoupling = matrix(stateSize, stateSize, new MatrixFunction() {
			@Override
			public double cell(int row, int column) {
				if (row == column) {
					return stateNoiseCouplingConstant;
				} else {
					return 0.0;
				}
			}
			
		});
		toNative(temp, stateNoiseCoupling);
		BayesPlusPlusLibrary.set_G(nativeStateModel, temp, stateSize);
	}

	private void initNativeObservationModel() throws EstimationException {
		hcallback = new HFunction();

		nativeObservationModel = BayesPlusPlusLibrary.create_linrz_uncorrelated_observe_model(stateSize, outputSize,
				hcallback);
		if (nativeObservationModel == null) {
			throw new EstimationException("Error creating observation model: "
					+ BayesPlusPlusLibrary.get_last_error());
		}

		Pointer buffer = NativeHelper.allocateDoubleArray(outputSize);
		
		observeNoise = vector(outputSize, new VectorFunction() {			
			@Override
			public double cell(int row) {
				return observeNoiseCovarianceConstant;
			}
		});
		toNative(buffer, observeNoise);
		BayesPlusPlusLibrary.set_Zv(nativeObservationModel, buffer, outputSize);
	}

	private void predict() throws EstimationException {
		if (BayesPlusPlusLibrary.predict(nativeScheme, nativeStateModel) == BayesPlusPlusLibrary.ERROR) {
			throw new EstimationException("Error in prediction phase: " + BayesPlusPlusLibrary.get_last_error());
		}
	}

	private void observe(Vector observation)
			throws EstimationException {
		Pointer buffer = NativeHelper.allocateDoubleArray(observation.rows());
		toNative(buffer, observation);
		if (BayesPlusPlusLibrary.observe(nativeScheme, nativeObservationModel, buffer, observation.rows()) == BayesPlusPlusLibrary.ERROR) {
			throw new EstimationException("Error in observation phase: " + BayesPlusPlusLibrary.get_last_error());
		}
	}

	private void updateState() throws EstimationException {
		if (BayesPlusPlusLibrary.update(nativeScheme) == BayesPlusPlusLibrary.ERROR) {
			throw new EstimationException("Error in update phase: " + BayesPlusPlusLibrary.get_last_error());
		}
		
		// Symmetrize the state covariance matrix to avoid numberical instabilities
		// See Optimal State Estimation by Dan Simon, page 140
		// Simon: Doing this seems to result in even higher instability (S not PD errors).
		// Therefore, I have commented it out.
//		BayesPlusPlusLibrary.get_X(nativeScheme, stateCovarianceBuffer);
//		Matrix P = nativeMatrix(stateSize, stateSize, stateCovarianceBuffer);
//		Matrix symP = (P.plus(transpose(P))).times(0.5);
//		toNative(stateCovarianceBuffer, symP);
//		BayesPlusPlusLibrary.set_X(nativeScheme, stateCovarianceBuffer, stateSize);
		
		/*
		 * Truncate estimate as described in: Tao Zheng; Woodside, M.; Litoiu, M.,
		 * "Performance Model Estimation and Tracking Using Optimal Filters,"
		 * Software Engineering, IEEE Transactions on , vol.34,
		 * no.3, pp.391,406, May-June 2008
		 */
		BayesPlusPlusLibrary.get_x(nativeScheme, stateBuffer);
		final Vector x = nativeVector(stateSize, stateBuffer);		
		toNative(stateBuffer, truncateState(x));
		BayesPlusPlusLibrary.set_x(nativeScheme, stateBuffer, stateSize);
	}
	
	private Vector truncateState(final Vector x) {
		final Vector x_lower = lowerStateBounds.times(boundsFactor).plus(lastEstimate.times(1 - boundsFactor));
		final Vector x_upper = upperStateBounds.times(boundsFactor).plus(lastEstimate.times(1 - boundsFactor));
		
		Vector x_truncated = vector(stateSize, new VectorFunction() {			
			@Override
			public double cell(int row) {
				return Math.min(x_upper.get(row), Math.max(x_lower.get(row), x.get(row)));
			}
		});
		return x_truncated;
	}

	private Vector getCurrentEstimate() {		
		BayesPlusPlusLibrary.get_x(nativeScheme, stateBuffer);
		return nativeVector(stateSize, stateBuffer);
	}
	
	private Matrix getInitialStateCovariance(final Vector initialState) {
		return matrix(stateSize, stateSize, new MatrixFunction() {
			@Override
			public double cell(int row, int column) {
				if (row == column) {
					/*
					 * Based on: Tao Zheng; Woodside, M.; Litoiu, M.,
					 * "Performance Model Estimation and Tracking Using Optimal Filters,"
					 * Software Engineering, IEEE Transactions on , vol.34,
					 * no.3, pp.391,406, May-June 2008
					 */
					double x = initialState.get(row);
					return x * x;
				}
				return 0;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		destroy(); // safety net if someone forgets to call destroy explicitly
		super.finalize();
	}

	/* (non-Javadoc)
	 * @see tools.descartes.librede.algorithm.AbstractEstimationAlgorithm#initialize(tools.descartes.librede.models.state.IStateModel, tools.descartes.librede.models.observation.IObservationModel, tools.descartes.librede.repository.IRepositoryCursor, int)
	 */
	@Override
	public void initialize(EstimationProblem problem, 
			IRepositoryCursor cursor,
			int estimationWindow) throws InitializationException {
		super.initialize(problem, cursor, estimationWindow);
		
		this.stateSize = problem.getStateModel().getStateSize();
		this.outputSize = problem.getObservationModel().getOutputSize();
		
		this.stateBuffer = NativeHelper.allocateDoubleArray(stateSize);
		this.stateCovarianceBuffer = NativeHelper.allocateDoubleArray(stateSize * stateSize);
		
		this.estimates = matrix(estimationWindow, stateSize, Double.NaN);
		
		// Initialized the state bounds
		upperStateBounds = (Vector) matrix(stateSize, 1, Double.POSITIVE_INFINITY);
		lowerStateBounds = (Vector) matrix(stateSize, 1, 0);
		for (IStateConstraint curConstraint : problem.getStateModel().getConstraints()) {
			if (curConstraint instanceof IStateBoundsConstraint) {
				ResourceDemand demand = ((IStateBoundsConstraint) curConstraint).getStateVariable();
				int idx = getStateModel().getStateVariableIndex(demand.getResource(), demand.getService());
				upperStateBounds = upperStateBounds.set(idx, curConstraint.getUpperBound());
				lowerStateBounds = lowerStateBounds.set(idx, curConstraint.getLowerBound());
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see tools.descartes.librede.algorithm.IEstimationAlgorithm#update()
	 */
	@Override
	public void update() throws EstimationException {
		if (!initialized) {
			// IMPORTANT: Call step so that invocation graph is correctly initialized.
			getStateModel().step(null);
			
			// First we need to obtain some observations to be able to
			// determine a good initial state.
			Vector initialState = truncateIntialState(getStateModel().getInitialState());
			if (!initialState.isEmpty()) {
				// initial state could be determined
				initNativeStateModel();
				initNativeObservationModel();
				initNativeKalmanFilter(initialState);
				lastEstimate = initialState;
				initialized = true;
			}
		}

		predict();

		observe(getObservationModel().getObservedOutput());

		updateState();
		
		Vector cur = getCurrentEstimate();
		estimates = estimates.circshift(1).setRow(0, cur);
	}
	
	/*
	 * Truncates the initial state so that it is within the bounds, 
	 * including a certain buffer distance (configurable).
	 */
	private Vector truncateIntialState(final Vector initialState) {
		Vector truncatedInitialState = vector(initialState.rows(), new VectorFunction() {			
			@Override
			public double cell(int row) {
				double value = initialState.get(row);
				
				if ((upperStateBounds.get(row) - initialBoundsDistance) < value) {
					return upperStateBounds.get(row) - initialBoundsDistance;
				} else if ((lowerStateBounds.get(row) + initialBoundsDistance) > value) {
					return (lowerStateBounds.get(row) + initialBoundsDistance);
				}
				return value;
			}
		});
		return truncatedInitialState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tools.descartes.librede.models.algorithm.IEstimationAlgorithm
	 * #estimate()
	 */
	@Override
	public Vector estimate() throws EstimationException {
		return nanmean(estimates);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tools.descartes.librede.models.algorithm.IEstimationAlgorithm
	 * #destroy()
	 */
	@Override
	public void destroy() {
		if (nativeScheme != null) {
			BayesPlusPlusLibrary.dispose_covariance_scheme(nativeScheme);
			nativeScheme = null;
			BayesPlusPlusLibrary.dispose_linrz_uncorrelated_observe_model(nativeObservationModel);
			nativeObservationModel = null;
			BayesPlusPlusLibrary.dispose_linrz_predict_model(nativeStateModel);
			nativeStateModel = null;
		}
	}
}
