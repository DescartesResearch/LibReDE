package edu.kit.ipd.descartes.redeem.estimation.models.algorithm;

import static edu.kit.ipd.descartes.linalg.Matrix.matrix;
import static edu.kit.ipd.descartes.linalg.Matrix.row;
import static edu.kit.ipd.descartes.linalg.Vector.vector;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.bayesplusplus.ExtendedKalmanFilter;
import edu.kit.ipd.descartes.redeem.bayesplusplus.MeasurementModel;
import edu.kit.ipd.descartes.redeem.bayesplusplus.StateModel;
import edu.kit.ipd.descartes.redeem.estimation.models.IJacobiMatrix;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.IObservationModel;
import edu.kit.ipd.descartes.redeem.estimation.models.state.IStateModel;

public class KalmanFilter<S extends IStateModel & IJacobiMatrix, 
							O extends IObservationModel & IJacobiMatrix> implements IEstimationAlgorithm<S, O> {
	
	private S stateModel;
	private O observationModel;
	
	private Vector stateNoiseCovariance = vector(1.0);
	private Matrix stateNoiseCoupling = matrix(row(1));
	
	private Vector initialEstimate = vector(0.1);
	private Matrix initialCovariance = matrix(row(0.01));
	
	private Vector observeNoise = vector(0.0001);	
	
	public KalmanFilter() {
		
	}

	public void runEstimation() {
		
		StateModelWrapper<S> stateModelWrapper = new StateModelWrapper<S>(stateModel, stateNoiseCovariance, stateNoiseCoupling);
		ObservationModelWrapper<O> obModelWrapper = new ObservationModelWrapper<O>(observationModel, stateModel.getStateSize(), observeNoise);

		ExtendedKalmanFilter filter = new ExtendedKalmanFilter(1, initialEstimate, initialCovariance);
		
		while(true) {
			filter.predict(stateModelWrapper);
			
			filter.observe(obModelWrapper, observationModel.getObservedOutputVector());
			
			filter.update();
			
			Vector vec = filter.getCurrentEstimate(); //TODO: save estimates
			
			if (!observationModel.nextObservation()) {
				break;
			}
		}
	}
	
	private static class StateModelWrapper<S extends IStateModel & IJacobiMatrix> extends StateModel {
		
		private S model;

		public StateModelWrapper(S model, Vector stateNoiseCovariance,
				Matrix stateNoiseCoupling) {
			super(model.getStateSize(), stateNoiseCovariance, stateNoiseCoupling);
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
	
	private static class ObservationModelWrapper<O extends IObservationModel & IJacobiMatrix> extends MeasurementModel {
		
		private O model;

		public ObservationModelWrapper(O model, int stateSize, Vector observeNoiseCovariance) {
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
