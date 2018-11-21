/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2018, by Simon Spinner, Johannes Grohmann
 *  and Contributors.
 *
 * Project Info:   http://www.descartes-research.net/
 *
 * All rights reserved. This software is made available under the terms of the
 * Eclipse Public License (EPL) v1.0 as published by the Eclipse Foundation
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse Public License (EPL)
 * for more details.
 *
 * You should have received a copy of the Eclipse Public License (EPL)
 * along with this software; if not visit http://www.eclipse.org or write to
 * Eclipse Foundation, Inc., 308 SW First Avenue, Suite 110, Portland, 97204 USA
 * Email: license (at) eclipse.org
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 */
package tools.descartes.librede.models.state.initial;

import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.state.IStateModel;

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
	 * demands.
	 * 
	 * @param stateModel
	 *            the state model to be initialized
	 * 
	 * @return a Vector containing the initial value for all state variables or
	 *         an empty vector if the initial value could not be determined
	 *         (e.g., due to missing observations)
	 */
	Vector getInitialValue(IStateModel<?> stateModel);

}
