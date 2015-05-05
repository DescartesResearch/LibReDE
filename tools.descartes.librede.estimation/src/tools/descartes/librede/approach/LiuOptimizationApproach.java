package tools.descartes.librede.approach;

import java.util.Arrays;
import java.util.List;

import tools.descartes.librede.algorithm.EstimationAlgorithmFactory;
import tools.descartes.librede.algorithm.IConstrainedNonLinearOptimizationAlgorithm;
import tools.descartes.librede.algorithm.IEstimationAlgorithm;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.SchedulingStrategy;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.models.observation.VectorObservationModel;
import tools.descartes.librede.models.observation.functions.IOutputFunction;
import tools.descartes.librede.models.observation.functions.ResponseTimeEquation;
import tools.descartes.librede.models.observation.functions.UtilizationLaw;
import tools.descartes.librede.models.state.ConstantStateModel;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.ConstantStateModel.Builder;
import tools.descartes.librede.models.state.constraints.IStateConstraint;
import tools.descartes.librede.models.state.constraints.StateBoundsConstraint;
import tools.descartes.librede.models.state.constraints.UtilizationConstraint;
import tools.descartes.librede.models.state.initial.WeightedTargetUtilizationInitializer;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.repository.IRepositoryCursor;

@Component(displayName = "Recursive Optimization using Response Times and Utilization")
public class LiuOptimizationApproach extends AbstractEstimationApproach {
	
	/**
	 * The initial demand is scaled to this utilization level, to avoid bad
	 * starting points (e.g., demands that would result in a utilization value
	 * above 100%)
	 */
	private static final double INITIAL_UTILIZATION = 0.5;

	@Override
	protected List<IStateModel<?>> deriveStateModels(WorkloadDescription workload, IRepositoryCursor cursor) {
		Builder<IStateConstraint> builder = ConstantStateModel.constrainedModelBuilder();
		for (Resource res : workload.getResources()) {
			for (Service service : res.getServices()) {
				builder.addConstraint(new StateBoundsConstraint(res, service, 0, Double.POSITIVE_INFINITY));
				builder.addVariable(res, service);
			}
		}
		builder.setStateInitializer(new WeightedTargetUtilizationInitializer(INITIAL_UTILIZATION, cursor));
		return Arrays.<IStateModel<?>>asList(builder.build());
	}

	@Override
	protected IObservationModel<?, ?> deriveObservationModel(IStateModel<?> stateModel, IRepositoryCursor cursor) {
		VectorObservationModel<IOutputFunction> observationModel = new VectorObservationModel<IOutputFunction>();
		for (int i = 0; i < getEstimationWindow(); i++) {
			for (Service service : stateModel.getUserServices()) {
				ResponseTimeEquation func = new ResponseTimeEquation(stateModel, cursor, service, true, i);
				observationModel.addOutputFunction(func);
			}
			for (Resource resource : stateModel.getResources()) {
				if (resource.getSchedulingStrategy() != SchedulingStrategy.IS) {
					UtilizationLaw func = new UtilizationLaw(stateModel, cursor, resource, i);
					observationModel.addOutputFunction(func);
				}
			}
		}
		return observationModel;
	}

	@Override
	protected IEstimationAlgorithm getEstimationAlgorithm(EstimationAlgorithmFactory factory) {
		return factory.createInstance(IConstrainedNonLinearOptimizationAlgorithm.class);
	}

}
