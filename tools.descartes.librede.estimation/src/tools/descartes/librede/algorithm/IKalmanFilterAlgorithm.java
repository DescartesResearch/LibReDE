package tools.descartes.librede.algorithm;

import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.models.observation.functions.IOutputFunction;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.Unconstrained;

public interface IKalmanFilterAlgorithm extends IEstimationAlgorithm<IStateModel<Unconstrained>, IObservationModel<IOutputFunction, Vector>>{

}
