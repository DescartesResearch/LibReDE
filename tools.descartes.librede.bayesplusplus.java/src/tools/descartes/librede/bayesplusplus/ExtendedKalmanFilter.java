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
import static tools.descartes.librede.linalg.LinAlg.mean;
import static tools.descartes.librede.linalg.LinAlg.transpose;
import static tools.descartes.librede.linalg.LinAlg.vector;
import static tools.descartes.librede.nativehelper.NativeHelper.nativeVector;
import static tools.descartes.librede.nativehelper.NativeHelper.nativeMatrix;
import static tools.descartes.librede.nativehelper.NativeHelper.toNative;
import tools.descartes.librede.algorithm.AbstractEstimationAlgorithm;
import tools.descartes.librede.bayesplusplus.backend.BayesPlusPlusLibrary;
import tools.descartes.librede.bayesplusplus.backend.FCallback;
import tools.descartes.librede.bayesplusplus.backend.HCallback;
import tools.descartes.librede.exceptions.EstimationException;
import tools.descartes.librede.exceptions.InitializationException;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.MatrixFunction;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorFunction;
import tools.descartes.librede.models.diff.JacobiMatrixBuilder;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.nativehelper.NativeHelper;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.registry.ParameterDefinition;
import tools.descartes.librede.repository.IRepositoryCursor;

import com.sun.jna.Pointer;

@Component(displayName="Extended Kalman Filter")
public class ExtendedKalmanFilter extends AbstractEstimationAlgorithm {

	// Callback function from native library for the observation model
	private class HFunction implements HCallback {

		private Pointer outputBuffer = NativeHelper.allocateDoubleArray(outputSize);
		private Pointer jacobiBuffer = NativeHelper.allocateDoubleArray(outputSize * stateSize);;

		@Override
		public Pointer execute(Pointer x) {
			Vector currentState = nativeVector(stateSize, x);

			Vector nextObservation = getObservationModel().getCalculatedOutput(currentState);

			Matrix jacobi = JacobiMatrixBuilder.calculateOfObservationModel(getObservationModel(), currentState);
			toNative(jacobiBuffer, jacobi);
			BayesPlusPlusLibrary.set_Hx(nativeObservationModel, jacobiBuffer, stateSize, outputSize);

			toNative(outputBuffer, nextObservation);
			return outputBuffer;
		}

	}

	// Callback function from native library for the state model
	private class FFunction implements FCallback {

		private Pointer stateBuffer = NativeHelper.allocateDoubleArray(stateSize);
		private Pointer jacobiBuffer = NativeHelper.allocateDoubleArray(stateSize * stateSize);

		@Override
		public Pointer execute(Pointer x) {
			Vector currentState = nativeVector(stateSize, x);

			Vector nextState = getStateModel().getNextState(currentState);
			Matrix jacobi = JacobiMatrixBuilder.calculateOfState(getStateModel(), currentState);
			toNative(jacobiBuffer, jacobi);
			BayesPlusPlusLibrary.set_Fx(nativeStateModel, jacobiBuffer, stateSize);

			toNative(stateBuffer, nextState);
			return stateBuffer;
		}
	}
	
	@ParameterDefinition(name = "StateNoiseCovariance", label = "State Noise Covariance", defaultValue = "1.0")
	private double stateNoiseCovarianceConstant = 1.0;
	
	@ParameterDefinition(name = "StateNoiseCoupling", label = "State Noise Coupling", defaultValue = "1.0")
	private double stateNoiseCouplingConstant = 1.0;
	
	@ParameterDefinition(name = "ObserveNoiseCovariance", label = "Observe Noise Covariance", defaultValue = "0.0001")
	private double observeNoiseCovarianceConstant = 0.0001;

	private int stateSize;
	private int outputSize;

	private Vector stateNoiseCovariance;
	private Matrix stateNoiseCoupling;

	private Vector observeNoise;
	
	private Matrix estimates;
	
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
		BayesPlusPlusLibrary.get_X(nativeScheme, stateCovarianceBuffer);
		Matrix P = nativeMatrix(stateSize, stateSize, stateCovarianceBuffer);
		Matrix symP = (P.plus(transpose(P))).times(0.5);
//		toNative(stateCovarianceBuffer, symP);
//		BayesPlusPlusLibrary.set_X(nativeScheme, stateCovarianceBuffer, stateSize);
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
	public void initialize(IStateModel<?> stateModel,
			IObservationModel<?, ?> observationModel, 
			IRepositoryCursor cursor,
			int estimationWindow) throws InitializationException {
		super.initialize(stateModel, observationModel, cursor, estimationWindow);
		
		this.stateSize = stateModel.getStateSize();
		this.outputSize = observationModel.getOutputSize();
		
		this.stateBuffer = NativeHelper.allocateDoubleArray(stateSize);
		this.stateCovarianceBuffer = NativeHelper.allocateDoubleArray(stateSize * stateSize);
		
		this.estimates = matrix(estimationWindow, stateSize, Double.NaN);
	}
	
	/* (non-Javadoc)
	 * @see tools.descartes.librede.algorithm.IEstimationAlgorithm#update()
	 */
	@Override
	public void update() throws EstimationException {
		if (!initialized) {
			// First we need to obtain some observations to be able to
			// determine a good initial state.
			Vector initialState = getStateModel().getInitialState();
			if (!initialState.isEmpty()) {
				// initial state could be determined
				initNativeStateModel();
				initNativeObservationModel();
				initNativeKalmanFilter(initialState);
				initialized = true;
			}
		} else {	
			predict();
	
			observe(getObservationModel().getObservedOutput());
	
			updateState();
			
			Vector cur = getCurrentEstimate();
			estimates = estimates.circshift(1).setRow(0, cur);
		}
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
		return mean(estimates);
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
