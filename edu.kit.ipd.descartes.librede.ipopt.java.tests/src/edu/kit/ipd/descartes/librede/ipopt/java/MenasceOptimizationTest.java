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
package edu.kit.ipd.descartes.librede.ipopt.java;

import static edu.kit.ipd.descartes.linalg.LinAlg.vector;
import static edu.kit.ipd.descartes.linalg.testutil.VectorAssert.assertThat;
import static org.fest.assertions.api.Assertions.offset;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.descartes.librede.estimation.models.observation.VectorObservationModel;
import edu.kit.ipd.descartes.librede.estimation.models.observation.functions.IOutputFunction;
import edu.kit.ipd.descartes.librede.estimation.models.observation.functions.ResponseTimeEquation;
import edu.kit.ipd.descartes.librede.estimation.models.state.ConstantStateModel;
import edu.kit.ipd.descartes.librede.estimation.models.state.constraints.ILinearStateConstraint;
import edu.kit.ipd.descartes.librede.estimation.models.state.constraints.UtilizationConstraint;
import edu.kit.ipd.descartes.librede.estimation.repository.RepositoryCursor;
import edu.kit.ipd.descartes.librede.estimation.testutils.ObservationDataGenerator;
import edu.kit.ipd.descartes.librede.estimation.workload.Service;
import edu.kit.ipd.descartes.librede.estimation.workload.WorkloadDescription;
import edu.kit.ipd.descartes.librede.ipopt.java.RecursiveOptimization;
import edu.kit.ipd.descartes.linalg.Vector;

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
		RepositoryCursor cursor = generator.getRepository().getCursor(0, 1);
		cursor.setEndTime(ITERATIONS);

		Vector initialEstimate = vector(0.01);
		stateModel = new ConstantStateModel<>(1, initialEstimate);
		
		observationModel = new VectorObservationModel<>();
		observationModel.addOutputFunction(new ResponseTimeEquation(workload, cursor, workload.getServices().get(0), workload.getResources()));
		
		stateModel.addConstraint(new UtilizationConstraint(workload, cursor, workload.getResources().get(0)));

		RecursiveOptimization optim = new RecursiveOptimization();
		optim.initialize(stateModel, observationModel, 10);

		long start = System.nanoTime();

		while(true) {
			generator.nextObservation();
			
			if (!cursor.next()) {
				break;
			}
			
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
		RepositoryCursor cursor = generator.getRepository().getCursor(0, 1);
		cursor.setEndTime(ITERATIONS);

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
