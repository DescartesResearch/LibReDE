package tools.descartes.librede.models.observation.queueingmodel;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;

import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.State;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.IStateConstraint;
import tools.descartes.librede.repository.Query;

public class ConstantValue extends ModelEquation {
	
	private final Query<Scalar, ?> query;
	
	public ConstantValue(IStateModel<? extends IStateConstraint> stateModel, int historicInterval, Query<Scalar, ?> query) {
		super(stateModel, historicInterval);
		this.query = query;
		addDataDependency(query);
	}

	@Override
	public Vector getFactors() {
		return query.get(historicInterval);
	}
	
	@Override
	public DerivativeStructure getValue(State state) {
		return new DerivativeStructure(state.getStateSize(), state.getDerivationOrder(), query.get(historicInterval).getValue());
	}
	
	@Override
	public boolean hasData() {
		return query.hasData();
	}
	
	@Override
	public boolean isLinear() {
		return true;
	}

}
