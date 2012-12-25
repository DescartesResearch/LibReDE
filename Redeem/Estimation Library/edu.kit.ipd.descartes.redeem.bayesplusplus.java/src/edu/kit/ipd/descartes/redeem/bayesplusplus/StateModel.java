package edu.kit.ipd.descartes.redeem.bayesplusplus;

import static edu.kit.ipd.descartes.linalg.Matrix.*;
import static edu.kit.ipd.descartes.linalg.Vector.*;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.bayesplusplus.backend.BayesPlusPlusLibrary;
import edu.kit.ipd.descartes.redeem.bayesplusplus.backend.FCallback;
import edu.kit.ipd.descartes.redeem.bayesplusplus.util.NativeDoubleStorage;
import edu.kit.ipd.descartes.redeem.bayesplusplus.util.NativeHelper;

public abstract class StateModel {
	
	private class FFunction implements FCallback {
		
		private Pointer vecBuffer = NativeHelper.allocateDoubleArray(stateSize);

		@Override
		public Pointer execute(Pointer x) {
			Vector currentState = vector(stateSize, new NativeDoubleStorage(x));
			
			Vector nextState = nextState(currentState);
			calculateJacobi(currentState);
			
			nextState.toDoubleStorage(new NativeDoubleStorage(vecBuffer));			
			return vecBuffer;
		}		
	}
	
	private Pointer nativeModel = null;
	private int stateSize;
	private Pointer matBuffer;
	
	public StateModel(int stateSize, Vector stateNoiseCovariance, Matrix stateNoiseCoupling) {
		this.stateSize = stateSize;
		nativeModel = BayesPlusPlusLibrary.INSTANCE.create_linrz_predict_model(stateSize, stateSize, new FFunction());
		
		Pointer buffer = NativeHelper.allocateDoubleArray(stateSize);
		stateNoiseCovariance.toDoubleStorage(new NativeDoubleStorage(buffer));
		BayesPlusPlusLibrary.INSTANCE.set_q(nativeModel, buffer, stateSize);
		
		matBuffer = NativeHelper.allocateDoubleArray(stateSize*stateSize);
		stateNoiseCoupling.toDoubleStorage(new NativeDoubleStorage(buffer));
		BayesPlusPlusLibrary.INSTANCE.set_G(nativeModel, buffer, stateSize);
	}
	
	public abstract Vector nextState(Vector currentState);
	
	public abstract void calculateJacobi(Vector currentState);
	
	public void setJacobi(Matrix jacobi) {
		jacobi.toDoubleStorage(new NativeDoubleStorage(matBuffer));
		BayesPlusPlusLibrary.INSTANCE.set_Fx(nativeModel, matBuffer, stateSize);
	}
	
	public void dispose() {
		if (nativeModel != null) {
			BayesPlusPlusLibrary.INSTANCE.dispose_linrz_predict_model(nativeModel);
			nativeModel = null;
		}
	}
	
	Pointer getNativeModel() {
		return nativeModel;
	}
	
	 @Override
	protected void finalize() throws Throwable {
		dispose();
		super.finalize();
	}


}
