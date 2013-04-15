package edu.kit.ipd.descartes.redeem.bayesplusplus;

import static edu.kit.ipd.descartes.linalg.LinAlg.vector;

import com.sun.jna.Pointer;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.bayesplusplus.backend.BayesPlusPlusLibrary;
import edu.kit.ipd.descartes.redeem.nativehelper.NativeDoubleStorage;
import edu.kit.ipd.descartes.redeem.nativehelper.NativeHelper;

public class ExtendedKalmanFilter {
	
	private Pointer nativeScheme = null;
	private Pointer stateBuffer;
	private int stateSize;
	
	public ExtendedKalmanFilter(int stateSize, Vector initialState, Matrix initialCovariance) {
		this.stateSize = stateSize;
		nativeScheme = BayesPlusPlusLibrary.INSTANCE.create_covariance_scheme(stateSize);
		
		stateBuffer = NativeHelper.allocateDoubleArray(stateSize);
		initialState.toDoubleStorage(new NativeDoubleStorage(stateBuffer));
		Pointer matBuffer = NativeHelper.allocateDoubleArray(stateSize*stateSize);
		initialCovariance.toDoubleStorage(new NativeDoubleStorage(matBuffer));
		
		BayesPlusPlusLibrary.INSTANCE.init_kalman(nativeScheme, stateBuffer, matBuffer, stateSize);		
	}
	
	public void predict(StateModel stateModel) {
		BayesPlusPlusLibrary.INSTANCE.predict(nativeScheme, stateModel.getNativeModel());
	}
	
	public void observe(MeasurementModel measurementModel, Vector observation, Vector...additionalInfo) {
		measurementModel.setAdditionalInformation(additionalInfo);
		
		Pointer buffer = NativeHelper.allocateDoubleArray(observation.rows());
		observation.toDoubleStorage(new NativeDoubleStorage(buffer));
		BayesPlusPlusLibrary.INSTANCE.observe(nativeScheme, measurementModel.getNativeModel(), buffer, observation.rows());
	}
	
	public void update() {
		BayesPlusPlusLibrary.INSTANCE.update(nativeScheme);
	}
	
	public Vector getCurrentEstimate() {		
		BayesPlusPlusLibrary.INSTANCE.get_x(nativeScheme, stateBuffer);
		return vector(stateSize, new NativeDoubleStorage(stateBuffer));
	}
	
	public void dispose() {
		if (nativeScheme != null) {
			BayesPlusPlusLibrary.INSTANCE.dispose_covariance_scheme(nativeScheme);
			nativeScheme = null;
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		dispose();
		super.finalize();
	}

}
