package edu.kit.ipd.descartes.redeem.bayesplusplus;

import static edu.kit.ipd.descartes.linalg.LinAlg.*;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.models.algorithm.IEstimationAlgorithm;
import edu.kit.ipd.descartes.redeem.estimation.models.diff.JacobiMatrixBuilder;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.IObservationModel;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IOutputFunction;
import edu.kit.ipd.descartes.redeem.estimation.models.state.IStateModel;
import edu.kit.ipd.descartes.redeem.estimation.models.state.constraints.Unconstrained;

public class KalmanFilter implements IEstimationAlgorithm<IStateModel<Unconstrained>, IObservationModel<IOutputFunction, Vector>> {
	
	private IStateModel<Unconstrained> stateModel;
	private IObservationModel<IOutputFunction, Vector> observationModel;
	
	private StateModelWrapper stateModelWrapper;
	private ObservationModelWrapper obModelWrapper;
	
	private ExtendedKalmanFilter filter;
	
	private int stateSize;
	private int outputSize;
	
	private Vector stateNoiseCovariance = vector(1.0);
	private Matrix stateNoiseCoupling = matrix(row(1));
	
	private Vector initialEstimate = vector(0.1);
	private Matrix initialCovariance = matrix(row(0.01));
	
	private Vector observeNoise = vector(0.0001);	
	
	public KalmanFilter() {
		
	}

	public Vector estimate() {
		
		filter.predict(stateModelWrapper);
			
		filter.observe(obModelWrapper, observationModel.getObservedOutput());
			
		filter.update();
			
		return filter.getCurrentEstimate();
	}
	
	private class StateModelWrapper extends StateModel {

		public StateModelWrapper() {
			super(stateSize, stateNoiseCovariance, stateNoiseCoupling);
		}

		@Override
		public Vector nextState(Vector currentState) {
			return stateModel.getNextState(currentState);
		}

		@Override
		public void calculateJacobi(Vector currentState) {
			Matrix jacobi = JacobiMatrixBuilder.calculateOfState(stateModel, currentState);
			setJacobi(jacobi);
		}		
	}
	
	private class ObservationModelWrapper extends MeasurementModel {

		public ObservationModelWrapper() {
			super(stateSize, outputSize, observeNoise);
		}

		@Override
		public Vector nextObservation(Vector currentState,
				Vector... additionalInfo) {
			return observationModel.getCalculatedOutput(currentState);
		}

		@Override
		public void calculateJacobi(Vector currentState,
				Vector... additionalInfo) {
			Matrix jacobi = JacobiMatrixBuilder.calculateOfObservationModel(observationModel, currentState);
			setJacobi(jacobi);
		}
		
	}

	@Override
	public void initialize(IStateModel<Unconstrained> stateModel,
			IObservationModel<IOutputFunction, Vector> observationModel) {
		this.observationModel = observationModel;
		this.stateModel = stateModel;
		
		this.stateSize = stateModel.getStateSize();
		this.outputSize = observationModel.getOutputSize();
		
		stateModelWrapper = new StateModelWrapper();
		obModelWrapper = new ObservationModelWrapper();

		filter = new ExtendedKalmanFilter(1, initialEstimate, initialCovariance);
	}

	@Override
	public void destroy() {
		
	}

}
