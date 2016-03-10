package tools.descartes.librede.models.observation.queueingmodel;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;

import tools.descartes.librede.models.AbstractDependencyTarget;
import tools.descartes.librede.models.State;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.IStateConstraint;

public abstract class ModelEquation extends AbstractDependencyTarget {
	
	private final IStateModel<? extends IStateConstraint> stateModel;
	protected final int historicInterval;

	protected ModelEquation(IStateModel<? extends IStateConstraint> stateModel, int historicInterval) {		
		if (stateModel == null) {
			throw new NullPointerException();
		}
		this.historicInterval = historicInterval;
		this.stateModel = stateModel;
	}

	public IStateModel<? extends IStateConstraint> getStateModel() {
		return stateModel;
	}
	
	/**
	 * Obtains the value of this model equation for the specified historic
	 * interval.
	 * 
	 * @param state
	 *            the current state of the system
	 * @return a DerivativeStructure containing the current values.
	 */
	public abstract DerivativeStructure getValue(State state);
	
	public abstract boolean hasData();

}
