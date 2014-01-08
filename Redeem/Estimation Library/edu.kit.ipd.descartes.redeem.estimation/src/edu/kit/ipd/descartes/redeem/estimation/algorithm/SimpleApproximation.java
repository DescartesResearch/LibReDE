package edu.kit.ipd.descartes.redeem.estimation.algorithm;

import static edu.kit.ipd.descartes.linalg.LinAlg.empty;
import static edu.kit.ipd.descartes.linalg.LinAlg.matrix;
import static edu.kit.ipd.descartes.linalg.LinAlg.max;
import static edu.kit.ipd.descartes.linalg.LinAlg.mean;
import static edu.kit.ipd.descartes.linalg.LinAlg.min;
import static edu.kit.ipd.descartes.linalg.LinAlg.sum;
import static edu.kit.ipd.descartes.linalg.LinAlg.vector;
import edu.kit.ipd.descartes.linalg.AggregationFunction;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorFunction;
import edu.kit.ipd.descartes.redeem.estimation.exceptions.EstimationException;
import edu.kit.ipd.descartes.redeem.estimation.exceptions.InitializationException;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.IObservationModel;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IDirectOutputFunction;
import edu.kit.ipd.descartes.redeem.estimation.models.state.ConstantStateModel;
import edu.kit.ipd.descartes.redeem.estimation.models.state.constraints.Unconstrained;
import edu.kit.ipd.descartes.redeem.estimation.repository.Aggregation;

public class SimpleApproximation implements IEstimationAlgorithm<ConstantStateModel<Unconstrained>, IObservationModel<IDirectOutputFunction, Vector>> {
	
	private ConstantStateModel<Unconstrained> stateModel;
	private IObservationModel<IDirectOutputFunction, Vector> observationModel;
	private Aggregation aggregation;
	private Matrix buffer;
	
	public SimpleApproximation(Aggregation aggregation) {
		this.aggregation = aggregation;
	}

	@Override
	public void initialize(ConstantStateModel<Unconstrained> stateModel,
			IObservationModel<IDirectOutputFunction, Vector> observationModel, int estimationWindow) throws InitializationException {
		this.stateModel = stateModel;
		this.observationModel = observationModel;
		this.buffer = matrix(estimationWindow, stateModel.getStateSize(), Double.NaN);
	}
	
	@Override
	public void update() throws EstimationException {
		final Vector output = observationModel.getObservedOutput();		
		
		Vector currentEstimate = vector(output.rows(), new VectorFunction() {			
			@Override
			public double cell(int row) {
				return output.get(row) / observationModel.getOutputFunction(row).getFactor();
			}
		});
		buffer = buffer.circshift(1).setRow(0, currentEstimate);		
	}

	@Override
	public Vector estimate() throws EstimationException {
		switch(aggregation) {
		case AVERAGE:
			return mean(buffer, 0);
		case MAXIMUM:
			return max(buffer, 0);
		case MINIMUM:
			return min(buffer, 0);
		case SUM:
			return sum(buffer, 0);
		case NONE:
			return buffer.row(0);				
		}
		return empty();
	}

	@Override
	public void destroy() {
	}
}
