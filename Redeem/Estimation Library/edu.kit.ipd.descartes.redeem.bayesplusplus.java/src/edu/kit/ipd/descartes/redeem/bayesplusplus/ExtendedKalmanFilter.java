package edu.kit.ipd.descartes.redeem.bayesplusplus;

import static edu.kit.ipd.descartes.linalg.LinAlg.matrix;
import static edu.kit.ipd.descartes.linalg.LinAlg.row;
import static edu.kit.ipd.descartes.linalg.LinAlg.vector;
import static edu.kit.ipd.descartes.redeem.nativehelper.NativeHelper.nativeVector;
import static edu.kit.ipd.descartes.redeem.nativehelper.NativeHelper.toNative;

import com.sun.jna.Pointer;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.MatrixFunction;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.bayesplusplus.backend.BayesPlusPlusLibrary;
import edu.kit.ipd.descartes.redeem.bayesplusplus.backend.FCallback;
import edu.kit.ipd.descartes.redeem.bayesplusplus.backend.HCallback;
import edu.kit.ipd.descartes.redeem.estimation.algorithm.IEstimationAlgorithm;
import edu.kit.ipd.descartes.redeem.estimation.exceptions.EstimationException;
import edu.kit.ipd.descartes.redeem.estimation.exceptions.InitializationException;
import edu.kit.ipd.descartes.redeem.estimation.models.diff.JacobiMatrixBuilder;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.IObservationModel;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IOutputFunction;
import edu.kit.ipd.descartes.redeem.estimation.models.state.IStateModel;
import edu.kit.ipd.descartes.redeem.estimation.models.state.constraints.Unconstrained;
import edu.kit.ipd.descartes.redeem.nativehelper.NativeHelper;

public class ExtendedKalmanFilter implements
		IEstimationAlgorithm<IStateModel<Unconstrained>, IObservationModel<IOutputFunction, Vector>> {

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

	private Vector stateNoiseCovariance = vector(1.0);
	private Matrix stateNoiseCoupling = matrix(row(1));

	private Vector observeNoise = vector(0.0001, 0.0001);
	
	private double stepSize = 1.0;

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

		toNative(stateBuffer, stateNoiseCovariance);
		BayesPlusPlusLibrary.set_q(nativeStateModel, stateBuffer, stateSize);

		toNative(stateBuffer, stateNoiseCoupling);
		BayesPlusPlusLibrary.set_G(nativeStateModel, stateBuffer, stateSize);
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

	private void update() throws EstimationException {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.kit.ipd.descartes.redeem.estimation.models.algorithm.IEstimationAlgorithm
	 * #
	 * initialize(edu.kit.ipd.descartes.redeem.estimation.models.state.IStateModel
	 * ,
	 * edu.kit.ipd.descartes.redeem.estimation.models.observation.IObservationModel
	 * )
	 */
	@Override
	public void initialize(IStateModel<Unconstrained> stateModel,
			IObservationModel<IOutputFunction, Vector> observationModel) throws InitializationException {
		this.stateSize = stateModel.getStateSize();
		this.outputSize = observationModel.getOutputSize();

		this.observationModel = observationModel;
		this.stateModel = stateModel;
		
		this.stateBuffer = NativeHelper.allocateDoubleArray(stateSize);

		initNativeStateModel();
		initNativeObservationModel();
		initNativeKalmanFilter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.kit.ipd.descartes.redeem.estimation.models.algorithm.IEstimationAlgorithm
	 * #estimate()
	 */
	@Override
	public Vector estimate() throws EstimationException {
		predict();

		observe(observationModel.getObservedOutput());

		update();

		return getCurrentEstimate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.kit.ipd.descartes.redeem.estimation.models.algorithm.IEstimationAlgorithm
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
