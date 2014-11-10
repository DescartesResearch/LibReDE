package tools.descartes.librede.algorithm;

import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.models.observation.functions.IOutputFunction;
import tools.descartes.librede.models.state.ConstantStateModel;
import tools.descartes.librede.models.state.constraints.IStateConstraint;

public interface IConstrainedNonLinearOptimizationAlgorithm extends IEstimationAlgorithm<ConstantStateModel<? extends IStateConstraint>, IObservationModel<IOutputFunction, Vector>>  {

}
