/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2014, by Simon Spinner and Contributors.
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
package tools.descartes.librede.algorithm;

import tools.descartes.librede.exceptions.EstimationException;
import tools.descartes.librede.exceptions.InitializationException;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.repository.IRepositoryCursor;

/**
 * This interface must be implemented by all estimation algorithms. It provides
 * the common interface to execute the estimation algorithm given a concrete
 * state and observation model.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public interface IEstimationAlgorithm {

	/**
	 * Initializes the estimation algorithm.
	 * 
	 * This function needs to be called before any other method in this
	 * interface.
	 * 
	 * @param stateModel
	 *            an instance of IStateModel
	 * @param observationModel
	 *            an instance of IObservationModel
	 * @param cursor
	 *            an instance of IRepositoryCursor
	 * @param estimationWindow
	 *            the size of the sliding window to be used on the input
	 *            measurement data. The size of the sliding window is the number
	 *            of interval which are considered during one run of the
	 *            estimation algorithm. (estimationWindow >= 1)
	 * @throws InitializationException
	 */
	void initialize(IStateModel<?> stateModel, IObservationModel<?, ?> observationModel, IRepositoryCursor cursor,
			int estimationWindow) throws InitializationException;

	/**
	 * This method is called to update the internal state of the estimator after
	 * new measurement data points are available.
	 * 
	 * This method must be called before estimate in order to update the
	 * estimated resource demands.
	 * 
	 * @throws EstimationException
	 */
	void update() throws EstimationException;

	/**
	 * Returns the current estimated resource demands.
	 * 
	 * @return a state vector with estimated demands for all resources and
	 *         services defined in the state model.
	 * @throws EstimationException
	 */
	Vector estimate() throws EstimationException;

	/**
	 * Releases any retained resources.
	 * 
	 * This method should be implemented if native resources are retained. The
	 * method should be executed only once, when the estimation algorithm is not
	 * needed any more.
	 */
	void destroy();

	/**
	 * @return the state model associated with this instance of the estimation
	 *         algorithm.
	 */
	IStateModel<?> getStateModel();

	/**
	 * @return the observation model associated with this instance of the
	 *         estimation algorithm.
	 */
	IObservationModel<?, ?> getObservationModel();

}
