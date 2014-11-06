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

import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.ipopt.java.RecursiveOptimization;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.observation.VectorObservationModel;
import tools.descartes.librede.models.observation.functions.IOutputFunction;
import tools.descartes.librede.models.observation.functions.ResponseTimeEquation;
import tools.descartes.librede.models.state.ConstantStateModel;
import tools.descartes.librede.models.state.constraints.ILinearStateConstraint;
import tools.descartes.librede.models.state.constraints.UtilizationConstraint;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.testutils.ObservationDataGenerator;
import tools.descartes.librede.workload.WorkloadDescription;

public class MenasceOptimizationTest {

	private static final int ITERATIONS = 100;

	private VectorObservationModel<IOutputFunction> observationModel;
	private ConstantStateModel<ILinearStateConstraint> stateModel;

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
		IRepositoryCursor cursor = generator.getRepository().getCursor(0, 1);

		Vector initialEstimate = vector(0.01);
		stateModel = new ConstantStateModel<>(1, initialEstimate);
		
		observationModel = new VectorObservationModel<>();
		observationModel.addOutputFunction(new ResponseTimeEquation(workload, cursor, workload.getServices().get(0), workload.getResources()));
		
		stateModel.addConstraint(new UtilizationConstraint(workload, cursor, workload.getResources().get(0)));

		RecursiveOptimization optim = new RecursiveOptimization();
		optim.initialize(stateModel, observationModel, 10);

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
	
	@Test
	public void testFiveServicesOneResource() throws Exception {
		final ObservationDataGenerator generator = new ObservationDataGenerator(42, 5, 1);

		Vector demands = vector(0.03, 0.04, 0.05, 0.06, 0.07);
		generator.setDemands(demands);
		generator.setUpperUtilizationBound(0.9);
		
		WorkloadDescription workload = generator.getWorkloadDescription();
		IRepositoryCursor cursor = generator.getRepository().getCursor(0, 1);

		Vector initialEstimate = vector(0.01, 0.01, 0.01, 0.01, 0.01);
		stateModel = new ConstantStateModel<>(5, initialEstimate);

		observationModel = new VectorObservationModel<>();
		for (Service service : workload.getServices()) {		
			observationModel.addOutputFunction(new ResponseTimeEquation(workload, cursor, service,
					workload.getResources()));
		}
		
		stateModel.addConstraint(new UtilizationConstraint(workload, cursor, workload.getResources().get(0)));

		RecursiveOptimization optim = new RecursiveOptimization();
		optim.initialize(stateModel, observationModel, 10);

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
