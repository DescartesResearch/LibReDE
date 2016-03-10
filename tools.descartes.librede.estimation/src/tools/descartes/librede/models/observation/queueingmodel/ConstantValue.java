package tools.descartes.librede.models.observation.queueingmodel;

import static tools.descartes.librede.linalg.LinAlg.scalar;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;

import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.State;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.IStateConstraint;

public abstract class ConstantValue extends ModelEquation {
	
	public ConstantValue(IStateModel<? extends IStateConstraint> stateModel, int historicInterval) {
		super(stateModel, historicInterval);
	}

	@Override
	public Vector getFactors() {
		return scalar(getConstantValue());
	}
	
	@Override
	public DerivativeStructure getValue(State state) {
		return new DerivativeStructure(state.getStateSize(), state.getDerivationOrder(), getConstantValue());
	}

	@Override
	public boolean isLinear() {
		return true;
	}
	
	@Override
	public boolean isConstant() {
		return true;
	}

}
