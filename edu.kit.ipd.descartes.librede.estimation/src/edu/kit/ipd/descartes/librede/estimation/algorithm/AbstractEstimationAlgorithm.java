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
package edu.kit.ipd.descartes.librede.estimation.algorithm;

import edu.kit.ipd.descartes.librede.estimation.exceptions.InitializationException;
import edu.kit.ipd.descartes.librede.estimation.models.observation.IObservationModel;
import edu.kit.ipd.descartes.librede.estimation.models.state.IStateModel;

public abstract class AbstractEstimationAlgorithm<S extends IStateModel<?>, O extends IObservationModel<?, ?>> implements IEstimationAlgorithm<S, O> {
	
	private S stateModel;
	private O observationModel;
	
	@Override
	public void initialize(S stateModel, O observationModel,
			int estimationWindow) throws InitializationException {
		this.stateModel = stateModel;
		this.observationModel = observationModel;
	}

	@Override
	public S getStateModel() {
		return stateModel;
	}

	@Override
	public O getObservationModel() {
		return observationModel;
	}

}
