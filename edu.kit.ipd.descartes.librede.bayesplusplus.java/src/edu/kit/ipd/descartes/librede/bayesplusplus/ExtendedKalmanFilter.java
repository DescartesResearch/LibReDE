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
package edu.kit.ipd.descartes.librede.bayesplusplus;

import static edu.kit.ipd.descartes.librede.nativehelper.NativeHelper.nativeVector;
import static edu.kit.ipd.descartes.librede.nativehelper.NativeHelper.toNative;
import static edu.kit.ipd.descartes.linalg.LinAlg.matrix;
import static edu.kit.ipd.descartes.linalg.LinAlg.mean;
import static edu.kit.ipd.descartes.linalg.LinAlg.vector;

import com.sun.jna.Pointer;

import edu.kit.ipd.descartes.librede.algorithm.AbstractEstimationAlgorithm;
import edu.kit.ipd.descartes.librede.bayesplusplus.backend.BayesPlusPlusLibrary;
import edu.kit.ipd.descartes.librede.bayesplusplus.backend.FCallback;
import edu.kit.ipd.descartes.librede.bayesplusplus.backend.HCallback;
import edu.kit.ipd.descartes.librede.exceptions.EstimationException;
import edu.kit.ipd.descartes.librede.exceptions.InitializationException;
import edu.kit.ipd.descartes.librede.models.diff.JacobiMatrixBuilder;
import edu.kit.ipd.descartes.librede.models.observation.IObservationModel;
import edu.kit.ipd.descartes.librede.models.observation.functions.IOutputFunction;
import edu.kit.ipd.descartes.librede.models.state.IStateModel;
import edu.kit.ipd.descartes.librede.models.state.constraints.Unconstrained;
import edu.kit.ipd.descartes.librede.nativehelper.NativeHelper;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.MatrixFunction;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorFunction;

public class ExtendedKalmanFilter extends
		AbstractEstimationAlgorithm<IStateModel<Unconstrained>, IObservationModel<IOutputFunction, Vector>> {

	// Callback function from native library for the observation model
	private class HFunction implements HCallback {

		private Pointer outputBuffer = NativeHelper.allocateDoubleArray(outputSize);
		private Pointer jacobiBuffer = NativeHelper.allocateDoubleArray(outputSize * stateSize);;

		@Override
		public Pointer execute(Pointer x) {
			Vector currentState = nativeVector(stateSize, x);

			Vector nextObservation = observationModel.getCalculatedOutput(currentState);

			Matrix jacobi = JacobiMatrixBuilder.calculateOfObservationModel(observationModel, currentState);
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

			Vector nextState = stateModel.getNextState(currentState);
			Matrix jacobi = JacobiMatrixBuilder.calculateOfState(stateModel, currentState);
			toNative(jacobiBuffer, jacobi);
			BayesPlusPlusLibrary.set_Fx(nativeStateModel, jacobiBuffer, stateSize);

			toNative(stateBuffer, nextState);
			return stateBuffer;
		}
	}

	private IStateModel<Unconstrained> stateModel;
	private IObservationModel<IOutputFunction, Vector> observationModel;

	private int stateSize;
	private int outputSize;

	private Vector stateNoiseCovariance;
	private Matrix stateNoiseCoupling;

	private Vector observeNoise;
	
	private Matrix estimates;

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
	
	private double stateNoiseCovarianceConstant = 1.0;
	private double stateNoiseCouplingConstant = 1.0;
	private double observeNoiseConstant = 0.0001;
	
	/**
	 * Sets a constant value used for all entries of the state noise vector.
	 * 
	 * @param stateNoiseCovarianceConstant
	 */
	public void setStateNoiseCovarianceConstant(
			double stateNoiseCovarianceConstant) {
		this.stateNoiseCovarianceConstant = stateNoiseCovarianceConstant;
	}
	
	/**
	 * Sets a constant value used to initialize the diagonal of the corresponding matrix.
	 * 
	 * @param stateNoiseCouplingConstant
	 */
	public void setStateNoiseCouplingConstant(double stateNoiseCouplingConstant) {
		this.stateNoiseCouplingConstant = stateNoiseCouplingConstant;
	}
	
	/**
	 * Sets a constant value used to initialize the elements of the observe noise vector
	 * 
	 * @param observeNoiseConstant
	 */
	public void setObserveNoiseConstant(double observeNoiseConstant) {
		this.observeNoiseConstant = observeNoiseConstant;
	}

	private void initNativeKalmanFilter() throws InitializationException {
		nativeScheme = BayesPlusPlusLibrary.create_covariance_scheme(stateSize);
		if (nativeScheme == null) {
			throw new InitializationException("Could not create kalman filter: "
					+ BayesPlusPlusLibrary.get_last_error());
		}

		Vector initialState = stateModel.getInitialState();
		toNative(stateBuffer, initialState);

		Pointer covBuffer = NativeHelper.allocateDoubleArray(stateSize * stateSize);
		Matrix initialCovariance = getInitialStateCovariance(initialState);
		toNative(covBuffer, initialCovariance);

		if (BayesPlusPlusLibrary.init_kalman(nativeScheme, stateBuffer, covBuffer, stateSize) == BayesPlusPlusLibrary.ERROR) {
			throw new InitializationException("Could not initialize kalman filter: "
					+ BayesPlusPlusLibrary.get_last_error());
		}
	}

	private void initNativeStateModel() throws InitializationException {
		fcallback = new FFunction();

		nativeStateModel = BayesPlusPlusLibrary.create_linrz_predict_model(stateSize, stateSize, fcallback);
		if (nativeStateModel == Pointer.NULL) {
			throw new InitializationException("Error creating state model: " + BayesPlusPlusLibrary.get_last_error());
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

	private void initNativeObservationModel() throws InitializationException {
		hcallback = new HFunction();

		nativeObservationModel = BayesPlusPlusLibrary.create_linrz_uncorrelated_observe_model(stateSize, outputSize,
				hcallback);
		if (nativeObservationModel == null) {
			throw new InitializationException("Error creating observation model: "
					+ BayesPlusPlusLibrary.get_last_error());
		}

		Pointer buffer = NativeHelper.allocateDoubleArray(outputSize);
		
		observeNoise = vector(outputSize, new VectorFunction() {			
			@Override
			public double cell(int row) {
				return observeNoiseConstant;
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
	 * @see edu.kit.ipd.descartes.librede.algorithm.IEstimationAlgorithm#initialize(edu.kit.ipd.descartes.librede.models.state.IStateModel, edu.kit.ipd.descartes.librede.models.observation.IObservationModel, int)
	 */
	@Override
	public void initialize(IStateModel<Unconstrained> stateModel,
			IObservationModel<IOutputFunction, Vector> observationModel, int estimationWindow) throws InitializationException {
		super.initialize(stateModel, observationModel, estimationWindow);
		
		this.stateSize = stateModel.getStateSize();
		this.outputSize = observationModel.getOutputSize();

		this.observationModel = observationModel;
		this.stateModel = stateModel;
		
		this.stateBuffer = NativeHelper.allocateDoubleArray(stateSize);
		
		this.estimates = matrix(estimationWindow, stateSize, Double.NaN);

		initNativeStateModel();
		initNativeObservationModel();
		initNativeKalmanFilter();
	}
	
	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.algorithm.IEstimationAlgorithm#update()
	 */
	@Override
	public void update() throws EstimationException {
		predict();

		observe(observationModel.getObservedOutput());

		updateState();
		
		Vector cur = getCurrentEstimate();
		estimates = estimates.circshift(1).setRow(0, cur);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.kit.ipd.descartes.librede.models.algorithm.IEstimationAlgorithm
	 * #estimate()
	 */
	@Override
	public Vector estimate() throws EstimationException {
		return mean(estimates, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.kit.ipd.descartes.librede.models.algorithm.IEstimationAlgorithm
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
