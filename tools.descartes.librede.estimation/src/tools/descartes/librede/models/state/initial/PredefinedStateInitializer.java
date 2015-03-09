package tools.descartes.librede.models.state.initial;

import tools.descartes.librede.linalg.Vector;

/**
 * This is a simple implementation of IStateInitializer that simply returns a
 * pre-defined vector.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 */
public class PredefinedStateInitializer implements IStateInitializer {

	private final Vector initialState;

	/**
	 * @param initialState a nx1 vector with n equal to the state size
	 */
	public PredefinedStateInitializer(Vector initialState) {
		this.initialState = initialState;
	}

	@Override
	public Vector getInitialValue() {
		return initialState;
	}

}
