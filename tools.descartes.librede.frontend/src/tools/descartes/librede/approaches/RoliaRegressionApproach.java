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
package tools.descartes.librede.approaches;

import static tools.descartes.librede.linalg.LinAlg.zeros;
import tools.descartes.librede.approach.AbstractEstimationApproach;
import tools.descartes.librede.exceptions.InitializationException;
import tools.descartes.librede.models.observation.ScalarObservationModel;
import tools.descartes.librede.models.observation.functions.ILinearOutputFunction;
import tools.descartes.librede.models.observation.functions.UtilizationLaw;
import tools.descartes.librede.models.state.ConstantStateModel;
import tools.descartes.librede.models.state.constraints.Unconstrained;
import tools.descartes.librede.nnls.LeastSquaresRegression;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.workload.WorkloadDescription;

@Component(displayName = "Least-squares Regression using Utilization Law")
public class RoliaRegressionApproach extends AbstractEstimationApproach {
	
	public static final String NAME = "RoliaRegression";

	@Override
	public void initialize(WorkloadDescription workload,
			IRepositoryCursor cursor, int estimationWindow, boolean iterative) throws InitializationException {
		super.initialize(workload, cursor, estimationWindow, iterative);

		int stateSize = workload.getServices().size();
		
		if (workload.getResources().size() != 1) {
			throw new InitializationException("The rolia regression approach is only applicable on workload models with one resource.");
		}

		ConstantStateModel<Unconstrained> stateModel = new ConstantStateModel<Unconstrained>(
				stateSize, zeros(stateSize));

		
		UtilizationLaw func = new UtilizationLaw(workload, cursor, workload.getResources().get(0));		
		ScalarObservationModel<ILinearOutputFunction> observationModel = new ScalarObservationModel<ILinearOutputFunction>(func);
	
		LeastSquaresRegression lsq = new LeastSquaresRegression();
		lsq.initialize(stateModel, observationModel, estimationWindow);
		setEstimationAlgorithm(lsq);
	}
}
