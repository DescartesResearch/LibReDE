package tools.descartes.librede.models.state.initial;

import tools.descartes.librede.linalg.Vector;

/**
 * This interface allows to implement the logic for determining the initial
 * demands of a state model as a strategy pattern. This information is needed by
 * estimation algorithms assuming a starting point for estimtaion.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public interface IStateInitializer {

	/**
	 * Called to initialized the state model with initial values of the resource
	 * demnads.
	 * 
	 * @return a Vector containing the initial value for all state variables
	 */
	Vector getInitialValue();

}
