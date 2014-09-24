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
package edu.kit.ipd.descartes.librede.algorithm;

import edu.kit.ipd.descartes.librede.exceptions.EstimationException;
import edu.kit.ipd.descartes.librede.exceptions.InitializationException;
import edu.kit.ipd.descartes.librede.models.observation.IObservationModel;
import edu.kit.ipd.descartes.librede.models.observation.functions.ILinearOutputFunction;
import edu.kit.ipd.descartes.librede.models.state.IStateModel;
import edu.kit.ipd.descartes.librede.models.state.constraints.ILinearStateConstraint;
import edu.kit.ipd.descartes.linalg.Vector;

public class LinearOptimizationAlgorithm extends AbstractEstimationAlgorithm<IStateModel<ILinearStateConstraint>, IObservationModel<ILinearOutputFunction, Vector>> {

	@Override
	public void initialize(IStateModel<ILinearStateConstraint> stateModel,
			IObservationModel<ILinearOutputFunction, Vector> observationModel,
			int estimationWindow) throws InitializationException {
		//SimplexSolver solver = new SimplexSolver();
		
		
		//solver.optimize(optData)
		
	}

	@Override
	public void update() throws EstimationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vector estimate() throws EstimationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
