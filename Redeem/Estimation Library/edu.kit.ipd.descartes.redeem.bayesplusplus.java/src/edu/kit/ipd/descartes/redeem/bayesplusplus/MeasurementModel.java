package edu.kit.ipd.descartes.redeem.bayesplusplus;

import static edu.kit.ipd.descartes.linalg.Matrix.*;
import static edu.kit.ipd.descartes.linalg.Vector.*;

import com.sun.jna.Pointer;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.bayesplusplus.backend.BayesPlusPlusLibrary;
import edu.kit.ipd.descartes.redeem.bayesplusplus.backend.HCallback;
import edu.kit.ipd.descartes.redeem.bayesplusplus.util.NativeDoubleStorage;
import edu.kit.ipd.descartes.redeem.bayesplusplus.util.NativeHelper;

public abstract class MeasurementModel {
	
	private class HFunction implements HCallback {
		
		private Pointer vecBuffer = NativeHelper.allocateDoubleArray(observationSize);

		@Override
		public Pointer execute(Pointer x) {
			Vector currentState = vector(stateSize, new NativeDoubleStorage(x));
			
			Vector nextObservation = nextObservation(currentState, currentAdditionalInfo);
			calculateJacobi(currentState, currentAdditionalInfo);
			
			nextObservation.toDoubleStorage(new NativeDoubleStorage(vecBuffer));			
			return vecBuffer;
		}
		
	}

	private Pointer nativeModel = null;
	private int stateSize;
	private int observationSize;
	private Pointer matBuffer;
	private Vector[] currentAdditionalInfo;
	
	public MeasurementModel(int stateSize, int observationSize, Vector observeNoiseCovariance) {
		this.stateSize = stateSize;
		this.observationSize = observationSize;
		
		matBuffer = NativeHelper.allocateDoubleArray(observationSize * stateSize);
		
		nativeModel = BayesPlusPlusLibrary.INSTANCE.create_linrz_uncorrelated_observe_model(stateSize, observationSize, new HFunction());
		
		Pointer buffer = NativeHelper.allocateDoubleArray(observationSize);
		observeNoiseCovariance.toDoubleStorage(new NativeDoubleStorage(buffer));
		BayesPlusPlusLibrary.INSTANCE.set_Zv(nativeModel, buffer, observationSize);
	}	

	public int getStateSize() {
		return stateSize;
	}
	
	public int getObservationSize() {
		return observationSize;
	}

	public abstract Vector nextObservation(Vector currentState, Vector...additionalInfo);
	
	public abstract void calculateJacobi(Vector currentState, Vector...additionalInfo);
	
	public void setAdditionalInformation(Vector[] additionalInfo) {
		this.currentAdditionalInfo = additionalInfo;
	}
	
	public void setJacobi(Matrix jacobi) {
		jacobi.toDoubleStorage(new NativeDoubleStorage(matBuffer));
		BayesPlusPlusLibrary.INSTANCE.set_Hx(nativeModel, matBuffer, stateSize, observationSize);
	}
	
	public void dispose() {
		if (nativeModel != null) {
			BayesPlusPlusLibrary.INSTANCE.dispose_linrz_uncorrelated_observe_model(nativeModel);
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
