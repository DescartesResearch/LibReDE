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
package edu.kit.ipd.descartes.librede.approaches;

import static edu.kit.ipd.descartes.linalg.LinAlg.zeros;
import edu.kit.ipd.descartes.librede.estimation.exceptions.InitializationException;
import edu.kit.ipd.descartes.librede.estimation.models.observation.ScalarObservationModel;
import edu.kit.ipd.descartes.librede.estimation.models.observation.functions.ILinearOutputFunction;
import edu.kit.ipd.descartes.librede.estimation.models.observation.functions.UtilizationLaw;
import edu.kit.ipd.descartes.librede.estimation.models.state.ConstantStateModel;
import edu.kit.ipd.descartes.librede.estimation.models.state.constraints.Unconstrained;
import edu.kit.ipd.descartes.librede.estimation.repository.RepositoryCursor;
import edu.kit.ipd.descartes.librede.estimation.workload.WorkloadDescription;
import edu.kit.ipd.descartes.librede.nnls.LeastSquaresRegression;

public class RoliaRegressionApproach extends AbstractEstimationApproach {
	
	public static final String NAME = "RoliaRegression";

	@Override
	public void initialize(WorkloadDescription workload,
			RepositoryCursor cursor, int estimationWindow, boolean iterative) throws InitializationException {
		super.initialize(workload, cursor, estimationWindow, iterative);

		int stateSize = workload.getServices().size();
		
		if (workload.getResources().size() != 1) {
			throw new InitializationException("The rolia regression approach is only applicable on workload models with one resource.");
		}

		ConstantStateModel<Unconstrained> stateModel = new ConstantStateModel<Unconstrained>(
				stateSize, zeros(stateSize));

		
		UtilizationLaw func = new UtilizationLaw(workload, cursor, workload.getResources().get(0));		
		if (!func.isApplicable()) {
			throw new InitializationException("The utilization law output function is not applicable.");
		}
		ScalarObservationModel<ILinearOutputFunction> observationModel = new ScalarObservationModel<ILinearOutputFunction>(func);
	
		LeastSquaresRegression lsq = new LeastSquaresRegression();
		lsq.initialize(stateModel, observationModel, estimationWindow);
		setEstimationAlgorithm(lsq);
	}
}
