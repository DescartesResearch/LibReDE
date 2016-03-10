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

import org.apache.log4j.Logger;

import tools.descartes.librede.exceptions.InitializationException;
import tools.descartes.librede.models.EstimationProblem;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.repository.IRepositoryCursor;

/**
 * This abstract class provides standard implementations for some of the methods in {@link IEstimationAlgorithm}.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public abstract class AbstractEstimationAlgorithm implements IEstimationAlgorithm {
	
	protected final Logger log = Logger.getLogger(getClass());
	
	private IStateModel<?> stateModel;
	private IObservationModel<?> observationModel;
	private IRepositoryCursor cursor;
	
	@Override
	public void initialize(EstimationProblem problem,
			IRepositoryCursor cursor, int estimationWindow) throws InitializationException {
		this.stateModel = problem.getStateModel();
		this.observationModel = problem.getObservationModel();
		this.cursor = cursor;
	}

	@Override
	public IStateModel<?> getStateModel() {
		return stateModel;
	}

	@Override
	public IObservationModel<?> getObservationModel() {
		return observationModel;
	}

	@Override
	public void destroy() {
	}	
}
