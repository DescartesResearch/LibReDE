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
package tools.descartes.librede.bayesplusplus.java;

import static tools.descartes.librede.linalg.LinAlg.vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tools.descartes.librede.bayesplusplus.ExtendedKalmanFilter;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.EstimationProblem;
import tools.descartes.librede.models.observation.OutputFunction;
import tools.descartes.librede.models.observation.VectorObservationModel;
import tools.descartes.librede.models.observation.equations.ResponseTimeEquation;
import tools.descartes.librede.models.observation.equations.ResponseTimeValue;
import tools.descartes.librede.models.observation.equations.UtilizationLawEquation;
import tools.descartes.librede.models.observation.equations.UtilizationValue;
import tools.descartes.librede.models.state.ConstantStateModel;
import tools.descartes.librede.models.state.ConstantStateModel.Builder;
import tools.descartes.librede.models.state.InvocationGraph;
import tools.descartes.librede.models.state.constraints.Unconstrained;
import tools.descartes.librede.models.state.initial.WeightedTargetUtilizationInitializer;
import tools.descartes.librede.testutils.LibredeTest;
import tools.descartes.librede.testutils.ObservationDataGenerator;

public class ZhangKalmanFilterTest extends LibredeTest {
	
	private static final int ITERATIONS = 1000;
	
	private VectorObservationModel observationModel;
	private ConstantStateModel<Unconstrained> stateModel;

	@Before
	public void setUp() throws Exception {
		
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFilter1WC1R() throws Exception {		
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 1, 1);		
		Vector demands = vector(0.05);
		generator.setDemands(demands);
		generator.setUpperUtilizationBound(0.9);
		
		WorkloadDescription workload = generator.getWorkloadDescription();
		
		Builder<Unconstrained> builder = ConstantStateModel.unconstrainedModelBuilder();
		builder.addVariable(workload.getResources().get(0).getDemands().get(0));
		builder.setStateInitializer(new WeightedTargetUtilizationInitializer(0.5, generator.getCursor()));
		builder.setInvocationGraph(new InvocationGraph(workload.getServices(), generator.getCursor(), 1));
		stateModel = builder.build();
		
		observationModel = new VectorObservationModel();
		ResponseTimeEquation funcRt = new ResponseTimeEquation(stateModel, generator.getCursor(), workload.getServices().get(0), false, 0);
		observationModel.addOutputFunction(new OutputFunction(new ResponseTimeValue(stateModel, generator.getCursor(), workload.getServices().get(0), 0), funcRt));
		UtilizationLawEquation funcUtil = new UtilizationLawEquation(stateModel, generator.getCursor(), workload.getResources().get(0), 0);
		observationModel.addOutputFunction(new OutputFunction(new UtilizationValue(stateModel, generator.getCursor(), workload.getResources().get(0), 0), funcUtil));
		
		ExtendedKalmanFilter filter = new ExtendedKalmanFilter();
		filter.initialize(new EstimationProblem(stateModel, observationModel), generator.getCursor(), 1);
		
		long start = System.nanoTime();	
		
		for (int i = 0; i < ITERATIONS; i++) {
			generator.nextObservation();
			generator.getCursor().next();
			
			filter.update();
			
			Vector estimates = filter.estimate();
		}
		
		System.out.println("Duration: " + (System.nanoTime() - start));
		
		filter.destroy();
	}
	
	@Test
	public void testFilter5WC1R() throws Exception {
		
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 5, 1);		
		Vector demands = vector(0.03, 0.04, 0.05, 0.06, 0.07);
		generator.setDemands(demands);
		generator.setUpperUtilizationBound(0.9);
		
		WorkloadDescription workload = generator.getWorkloadDescription();
		
		Vector initialEstimate = vector(0.01, 0.01, 0.01, 0.01, 0.01);
		Builder<Unconstrained> builder = ConstantStateModel.unconstrainedModelBuilder();
		for (Service service : workload.getServices()) {
			builder.addVariable(service.getResourceDemands().get(0));
		}
		builder.setStateInitializer(new WeightedTargetUtilizationInitializer(0.5, generator.getCursor()));
		builder.setInvocationGraph(new InvocationGraph(workload.getServices(), generator.getCursor(), 1));
		stateModel = builder.build();
		
		observationModel = new VectorObservationModel();
		for (int i = 0; i < 5; i++) {
			ResponseTimeEquation funcRt = new ResponseTimeEquation(stateModel, generator.getCursor(), workload.getServices().get(i), false, 0);
			observationModel.addOutputFunction(new OutputFunction(new ResponseTimeValue(stateModel, generator.getCursor(), workload.getServices().get(i), 0), funcRt));
		}
		UtilizationLawEquation funcUtil = new UtilizationLawEquation(stateModel, generator.getCursor(), workload.getResources().get(0), 0);
		observationModel.addOutputFunction(new OutputFunction(new UtilizationValue(stateModel, generator.getCursor(), workload.getResources().get(0), 0), funcUtil));
		
		ExtendedKalmanFilter filter = new ExtendedKalmanFilter();
		filter.initialize(new EstimationProblem(stateModel, observationModel), generator.getCursor(), 1);
		
		long start = System.nanoTime();	
		
		for (int i = 0; i < ITERATIONS; i++) {
			generator.nextObservation();
			generator.getCursor().next();
			
			filter.update();
			
			Vector estimates = filter.estimate();
		}
		
		System.out.println("Duration: " + (System.nanoTime() - start));
		
		filter.destroy();
	}

}
