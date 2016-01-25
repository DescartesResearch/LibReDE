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
package tools.descartes.librede.ipopt.java;

import static org.fest.assertions.api.Assertions.offset;
import static tools.descartes.librede.linalg.LinAlg.vector;
import static tools.descartes.librede.linalg.testutil.VectorAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.EstimationProblem;
import tools.descartes.librede.models.observation.VectorObservationModel;
import tools.descartes.librede.models.observation.functions.IOutputFunction;
import tools.descartes.librede.models.observation.functions.ResponseTimeEquation;
import tools.descartes.librede.models.observation.functions.UtilizationLaw;
import tools.descartes.librede.models.state.ConstantStateModel;
import tools.descartes.librede.models.state.ConstantStateModel.Builder;
import tools.descartes.librede.models.state.InvocationGraph;
import tools.descartes.librede.models.state.constraints.IStateConstraint;
import tools.descartes.librede.models.state.constraints.NoRequestsBoundsConstraint;
import tools.descartes.librede.models.state.initial.WeightedTargetUtilizationInitializer;
import tools.descartes.librede.repository.CachingRepositoryCursor;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.testutils.LibredeTest;
import tools.descartes.librede.testutils.ObservationDataGenerator;

public class LiuOptimizationTest extends LibredeTest {

	private static final int ITERATIONS = 100;

	private VectorObservationModel<IOutputFunction> observationModel;
	private ConstantStateModel<IStateConstraint> stateModel;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testOneServiceOneResource() throws Exception {
		final ObservationDataGenerator generator = new ObservationDataGenerator(42, 1, 1);

		Vector demands = vector(0.05);
		generator.setDemands(demands);
		generator.setUpperUtilizationBound(0.9);
		
		WorkloadDescription workload = generator.getWorkloadDescription();
		IRepositoryCursor cursor = new CachingRepositoryCursor(generator.getCursor(), 5);

		Builder<IStateConstraint> builder = ConstantStateModel.constrainedModelBuilder();
		builder.addVariable(workload.getResources().get(0).getDemands().get(0));
		builder.setStateInitializer(new WeightedTargetUtilizationInitializer(0.5, cursor));
		builder.setInvocationGraph(new InvocationGraph(workload.getServices(), cursor, 5));
		builder.addConstraint(new NoRequestsBoundsConstraint(workload.getResources().get(0).getDemands().get(0), cursor, 0, 1));
		stateModel = builder.build();
		
		observationModel = new VectorObservationModel<>();
		for (int i = 0; i < 5; i++) {
			observationModel.addOutputFunction(new ResponseTimeEquation(stateModel, cursor, workload.getServices().get(0), true, i));
			observationModel.addOutputFunction(new UtilizationLaw(stateModel, cursor, workload.getResources().get(0), i));
		}
		
		for (int i = 0; i < 6; i++) {
			generator.nextObservation();
			
			cursor.next();
		}

		RecursiveOptimization optim = new RecursiveOptimization();
		optim.initialize(new EstimationProblem(stateModel, observationModel), cursor, 1);

		long start = System.nanoTime();

		for (int i = 0; i < ITERATIONS; i++) {
			generator.nextObservation();
			
			cursor.next();
			
			optim.update();

			Vector estimates = optim.estimate();

			assertThat(estimates).isEqualTo(demands, offset(0.001));
		}

		System.out.println("Duration: " + (System.nanoTime() - start) / 1000000);

		optim.destroy();

	}
	
	@Test
	public void testFiveServicesTwoResources() throws Exception {
		final ObservationDataGenerator generator = new ObservationDataGenerator(42, 5, 2);

		// IMPORTANT: test with zero demand!
//		Vector demands = vector(0.03, 0.04, 0.05, 0.06, 0.0);
//		generator.setDemands(demands);
		generator.setRandomDemands();
		generator.setUpperUtilizationBound(0.9);
		Vector demands = generator.getDemands();
		
		WorkloadDescription workload = generator.getWorkloadDescription();
		IRepositoryCursor cursor = new CachingRepositoryCursor(generator.getCursor(), 5);

		Builder<IStateConstraint> builder = ConstantStateModel.constrainedModelBuilder();
		for (Resource resource : workload.getResources()) {
			for (ResourceDemand demand : resource.getDemands()) {
				builder.addVariable(demand);
			}
		}
		builder.setStateInitializer(new WeightedTargetUtilizationInitializer(0.5, cursor));
		for (Resource resource : workload.getResources()) {
			for (ResourceDemand demand : resource.getDemands()) {
				builder.addConstraint(new NoRequestsBoundsConstraint(demand, cursor, 0, Double.POSITIVE_INFINITY));
			}	
		}
		builder.setInvocationGraph(new InvocationGraph(workload.getServices(), cursor, 5));
		stateModel = builder.build();
		

		observationModel = new VectorObservationModel<>();
		for (int i = 0; i < 3; i++) {
			for (Service service : workload.getServices()) {		
				observationModel.addOutputFunction(new ResponseTimeEquation(stateModel, cursor, service, true, i));
			}
			for (Resource res : workload.getResources()) {				
				observationModel.addOutputFunction(new UtilizationLaw(stateModel, cursor, res, i));
			}
		}
		
		for (int i = 0; i < 6; i++) {
			generator.nextObservation();
			
			cursor.next();
		}
		
		RecursiveOptimization optim = new RecursiveOptimization();
		optim.initialize(new EstimationProblem(stateModel, observationModel), cursor, 1);

		long start = System.nanoTime();

		for (int i = 0; i < ITERATIONS; i++) {
			generator.nextObservation();
			cursor.next();
			
			optim.update();

			Vector estimates = optim.estimate();
			
			assertThat(estimates).isEqualTo(demands, offset(0.001));
			
//			System.out.println(i + ": " + estimates);
		}

		System.out.println("Duration: " + (System.nanoTime() - start) / 1000000);

		optim.destroy();

	}
}
