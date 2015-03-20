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
package tools.descartes.librede.nnls.tests;

import static tools.descartes.librede.linalg.LinAlg.vector;

import org.junit.Before;
import org.junit.Test;

import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.observation.ScalarObservationModel;
import tools.descartes.librede.models.observation.functions.ILinearOutputFunction;
import tools.descartes.librede.models.observation.functions.UtilizationLaw;
import tools.descartes.librede.models.state.ConstantStateModel;
import tools.descartes.librede.models.state.ConstantStateModel.Builder;
import tools.descartes.librede.models.state.constraints.Unconstrained;
import tools.descartes.librede.nnls.LeastSquaresRegression;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.testutils.LibredeTest;
import tools.descartes.librede.testutils.ObservationDataGenerator;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.UnitsFactory;

public class LeastSquaresRegressionTest extends LibredeTest {

	private static final int ITERATIONS = 100;

	private ScalarObservationModel<ILinearOutputFunction> observationModel;
	private ConstantStateModel<Unconstrained> stateModel;

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
		IRepositoryCursor cursor = generator.getRepository().getCursor(UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS), UnitsFactory.eINSTANCE.createQuantity(1, Time.SECONDS));

		Vector initialEstimate = vector(0.01);
		Builder<Unconstrained> builder = ConstantStateModel.unconstrainedModelBuilder();
		builder.addVariable(workload.getResources().get(0), workload.getServices().get(0));
		builder.setInitialState(initialEstimate);
		stateModel = builder.build();
		
		observationModel = new ScalarObservationModel<ILinearOutputFunction>(new UtilizationLaw(stateModel, cursor, stateModel.getResources().get(0)));

		LeastSquaresRegression optim = new LeastSquaresRegression();
		optim.initialize(stateModel, observationModel, 10);

		long start = System.nanoTime();

		for (int i = 0; i < ITERATIONS; i++) {
			generator.nextObservation();
			
			cursor.next();
			
			optim.update();

			Vector estimates = optim.estimate();

			//assertThat(estimates).isEqualTo(demands, offset(0.001));
			
			System.out.println(estimates);
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
		IRepositoryCursor cursor = generator.getRepository().getCursor(UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS), UnitsFactory.eINSTANCE.createQuantity(1, Time.SECONDS));

		Vector initialEstimate = vector(0.01, 0.01, 0.01, 0.01, 0.01);

		Builder<Unconstrained> builder = ConstantStateModel.unconstrainedModelBuilder();
		for (Service serv : workload.getServices()) {
			builder.addVariable(workload.getResources().get(0), serv);
		}
		builder.setInitialState(initialEstimate);
		stateModel = builder.build();

		observationModel = new ScalarObservationModel<ILinearOutputFunction>(new UtilizationLaw(stateModel, cursor, workload.getResources().get(0)));
		
		LeastSquaresRegression optim = new LeastSquaresRegression();
		optim.initialize(stateModel, observationModel, 10);

		long start = System.nanoTime();

		for (int i = 0; i < ITERATIONS; i++) {
			generator.nextObservation();
			cursor.next();
			
			optim.update();

			Vector estimates = optim.estimate();
			
//			assertThat(estimates).isEqualTo(demands, offset(0.001));
			
//			System.out.println(i + ": " + estimates);
		}

		System.out.println("Duration: " + (System.nanoTime() - start) / 1000000);

		optim.destroy();

	}
}
