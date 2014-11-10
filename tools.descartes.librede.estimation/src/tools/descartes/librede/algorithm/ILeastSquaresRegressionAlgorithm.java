package tools.descartes.librede.algorithm;

import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.models.observation.functions.ILinearOutputFunction;
import tools.descartes.librede.models.state.ConstantStateModel;
import tools.descartes.librede.models.state.constraints.Unconstrained;

public interface ILeastSquaresRegressionAlgorithm extends IEstimationAlgorithm<ConstantStateModel<Unconstrained>, IObservationModel<ILinearOutputFunction, Scalar>> {

}
