package tools.descartes.librede.models.state.constraints;

import tools.descartes.librede.models.state.StateVariable;

public interface IStateBoundsConstraint extends ILinearStateConstraint {

	public StateVariable getStateVariable();

}
