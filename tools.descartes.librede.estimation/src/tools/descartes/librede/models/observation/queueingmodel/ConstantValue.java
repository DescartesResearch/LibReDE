package tools.descartes.librede.models.observation.queueingmodel;

import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.IStateConstraint;

public class ConstantValue extends FixedValue {
	
	public final double constant;
	
	public ConstantValue(IStateModel<? extends IStateConstraint> stateModel, double constant) {
		super(stateModel, -1);
		this.constant = constant;
	}
	
	@Override
	public double getConstantValue() {
		return constant;
	}

	@Override
	public boolean hasData() {
		return true;
	}
	
}
