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
import tools.descartes.librede.models.observation.VectorObservationModel;
import tools.descartes.librede.models.observation.functions.IOutputFunction;
import tools.descartes.librede.models.observation.functions.ResponseTimeEquation;
import tools.descartes.librede.models.observation.functions.UtilizationLaw;
import tools.descartes.librede.models.state.ConstantStateModel;
import tools.descartes.librede.models.state.ConstantStateModel.Builder;
import tools.descartes.librede.models.state.constraints.Unconstrained;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.testutils.LibredeTest;
import tools.descartes.librede.testutils.ObservationDataGenerator;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.UnitsFactory;

public class ZhangKalmanFilterTest extends LibredeTest {
	
	private static final int ITERATIONS = 1000;
	
	private VectorObservationModel<IOutputFunction> observationModel;
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
		
		IRepositoryCursor cursor = generator.getRepository().getCursor(UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS), UnitsFactory.eINSTANCE.createQuantity(1, Time.SECONDS));
		WorkloadDescription workload = generator.getWorkloadDescription();
		
		Builder<Unconstrained> builder = ConstantStateModel.unconstrainedModelBuilder();
		builder.addVariable(workload.getResources().get(0), workload.getServices().get(0));
		builder.setInitialState(vector(0.1));
		stateModel = builder.build();
		
		observationModel = new VectorObservationModel<>();		
		observationModel.addOutputFunction(new ResponseTimeEquation(stateModel, cursor, workload.getServices().get(0)));
		observationModel.addOutputFunction(new UtilizationLaw(stateModel, cursor, workload.getResources().get(0)));
		
		ExtendedKalmanFilter filter = new ExtendedKalmanFilter();
		filter.initialize(stateModel, observationModel, 1);
		
		long start = System.nanoTime();	
		
		for (int i = 0; i < ITERATIONS; i++) {
			generator.nextObservation();
			cursor.next();
			
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
		
		IRepositoryCursor cursor = generator.getRepository().getCursor(UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS), UnitsFactory.eINSTANCE.createQuantity(1, Time.SECONDS));
		WorkloadDescription workload = generator.getWorkloadDescription();
		
		Vector initialEstimate = vector(0.01, 0.01, 0.01, 0.01, 0.01);
		Builder<Unconstrained> builder = ConstantStateModel.unconstrainedModelBuilder();
		for (Service service : workload.getServices()) {
			builder.addVariable(workload.getResources().get(0), service);
		}
		builder.setInitialState(vector(0.01, 0.01, 0.01, 0.01, 0.01));
		stateModel = builder.build();
		
		observationModel = new VectorObservationModel<>();
		for (int i = 0; i < 5; i++) {
			observationModel.addOutputFunction(new ResponseTimeEquation(stateModel, cursor, workload.getServices().get(i)));
		}
		observationModel.addOutputFunction(new UtilizationLaw(stateModel, cursor, workload.getResources().get(0)));
		
		ExtendedKalmanFilter filter = new ExtendedKalmanFilter();
		filter.initialize(stateModel, observationModel, 1);
		
		long start = System.nanoTime();	
		
		for (int i = 0; i < ITERATIONS; i++) {
			generator.nextObservation();
			cursor.next();
			
			filter.update();
			
			Vector estimates = filter.estimate();
		}
		
		System.out.println("Duration: " + (System.nanoTime() - start));
		
		filter.destroy();
	}

}
