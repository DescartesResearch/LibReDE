package tools.descartes.librede.models.state.constraints;

import java.util.List;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.diff.IDifferentiableFunction;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.StateVariable;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.units.RequestRate;

/**
 * This bounds constraints check if throughput is larger than zero for the
 * specified state variable. If not, the demand is required to be zero. Otherwise
 * a predefined value is used.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public class NoRequestsBoundsConstraint implements IStateBoundsConstraint, IDifferentiableFunction {
	
	private Query<Scalar, RequestRate> throughputQuery;	
	private final double lowerBound;	
	private final double upperBound;	
	private final StateVariable variable;
	private IStateModel<? extends IStateConstraint> stateModel;
	
	public NoRequestsBoundsConstraint(Resource resource, Service service, IRepositoryCursor cursor, double lowerBound, double upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.variable = new StateVariable(resource, service);
		this.throughputQuery = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forService(service).average().using(cursor);
	}

	@Override
	public double getLowerBound() {
		return (throughputQuery.execute().get(0) == 0.0) ? 0.0 : lowerBound;
	}

	@Override
	public double getUpperBound() {
		return (throughputQuery.execute().get(0) == 0.0) ? 0.0 : upperBound;
	}

	@Override
	public double getValue(Vector state) {
		if (stateModel == null) {
			throw new IllegalStateException();
		}
		return state.get(stateModel.getStateVariableIndex(variable.getResource(), variable.getService()));
	}

	@Override
	public boolean isApplicable(List<String> messages) {
		if (!throughputQuery.hasData()) {
			StringBuilder msg = new StringBuilder("DATA PRECONDITION: ");
			msg.append("metric = ").append(throughputQuery.getMetric().toString()).append(" ");
			msg.append("entities = { ");
			for(ModelEntity entity : throughputQuery.getEntities()) {
				msg.append(entity.getName()).append(" ");
			}
			msg.append(" } ");
			messages.add(msg.toString());
			return false;
		}
		return true;
	}

	@Override
	public void setStateModel(IStateModel<? extends IStateConstraint> model) {
		this.stateModel = model;
	}

	@Override
	public Vector getFirstDerivatives(Vector x) {
		return null;
	}

	@Override
	public Matrix getSecondDerivatives(Vector x) {
		return null;
	}

	@Override
	public StateVariable getStateVariable() {
		return this.variable;
	}

}
