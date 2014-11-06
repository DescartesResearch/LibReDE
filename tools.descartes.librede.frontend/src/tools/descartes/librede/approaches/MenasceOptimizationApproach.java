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
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.exceptions.InitializationException;
import tools.descartes.librede.ipopt.java.RecursiveOptimization;
import tools.descartes.librede.models.observation.VectorObservationModel;
import tools.descartes.librede.models.observation.functions.IOutputFunction;
import tools.descartes.librede.models.observation.functions.ResponseTimeEquation;
import tools.descartes.librede.models.state.ConstantStateModel;
import tools.descartes.librede.models.state.constraints.ILinearStateConstraint;
import tools.descartes.librede.models.state.constraints.UtilizationConstraint;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.registry.ParameterDefinition;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.workload.WorkloadDescription;

@Component(displayName = "Recursive Optimization using Response Times")
public class MenasceOptimizationApproach extends AbstractEstimationApproach {
	
	public static final String NAME = "MenasceOptimization";
	
	@ParameterDefinition(name = "SolutionTolerance", label = "Solution Tolerance", defaultValue = "1e-7")
	private double solutionTolerance;
	
	@ParameterDefinition(name = "UpperBoundsInfValue", label = "Upper Bounds Infinity Value", defaultValue = "1e19")
	private double upperBoundsInfValue;
	
	@ParameterDefinition(name = "LowerBoundsInfValue", label = "Lower Bounds Infinity Value", defaultValue = "-1e19")
	private double lowerBoundsInfValue;
	
	@Override
	public void initialize(WorkloadDescription workload,
			IRepositoryCursor cursor, int estimationWindow, boolean iterative)
			throws InitializationException {
		super.initialize(workload, cursor, estimationWindow, iterative);
		
		int stateSize = workload.getState().getStateSize();
		
		ConstantStateModel<ILinearStateConstraint> stateModel = new ConstantStateModel<ILinearStateConstraint>(stateSize, zeros(stateSize));
		for (Resource res : workload.getResources()) {
			stateModel.addConstraint(new UtilizationConstraint(workload, cursor, res));
		}
		
		VectorObservationModel<IOutputFunction> observationModel = new VectorObservationModel<IOutputFunction>();
		for (Service service : workload.getServices()) {
			ResponseTimeEquation func = new ResponseTimeEquation(workload, cursor, service, workload.getResources());
			observationModel.addOutputFunction(func);
		}

		RecursiveOptimization estimator = new RecursiveOptimization();
		estimator.setBoundsInfValue(lowerBoundsInfValue, upperBoundsInfValue);
		estimator.setSolutionTolerance(solutionTolerance);
		estimator.initialize(stateModel, observationModel, estimationWindow);
		setEstimationAlgorithm(estimator);
	}
	
}
