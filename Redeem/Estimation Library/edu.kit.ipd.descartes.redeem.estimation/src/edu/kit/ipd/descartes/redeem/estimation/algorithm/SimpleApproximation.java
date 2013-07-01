package edu.kit.ipd.descartes.redeem.estimation.algorithm;

import static edu.kit.ipd.descartes.linalg.LinAlg.vector;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorFunction;
import edu.kit.ipd.descartes.redeem.estimation.exceptions.EstimationException;
import edu.kit.ipd.descartes.redeem.estimation.exceptions.InitializationException;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.IObservationModel;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IDirectOutputFunction;
import edu.kit.ipd.descartes.redeem.estimation.models.state.ConstantStateModel;
import edu.kit.ipd.descartes.redeem.estimation.models.state.constraints.Unconstrained;

public class SimpleApproximation implements IEstimationAlgorithm<ConstantStateModel<Unconstrained>, IObservationModel<IDirectOutputFunction, Vector>> {
	
	private ConstantStateModel<Unconstrained> stateModel;
	private IObservationModel<IDirectOutputFunction, Vector> observationModel;

	@Override
	public void initialize(ConstantStateModel<Unconstrained> stateModel,
			IObservationModel<IDirectOutputFunction, Vector> observationModel) throws InitializationException {
		this.stateModel = stateModel;
		this.observationModel = observationModel;	
	}

	@Override
	public Vector estimate() throws EstimationException {
		final Vector output = observationModel.getObservedOutput();
		
		Vector estimate = vector(output.rows(), new VectorFunction() {			
			@Override
			public double cell(int row) {
				return output.get(row) / observationModel.getOutputFunction(row).getFactor();
			}
		});
		
		return estimate;		
	}

	@Override
	public void destroy() {
	}
	
	

}
