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
package edu.kit.ipd.descartes.librede.bayesplusplus.java;

import static edu.kit.ipd.descartes.linalg.LinAlg.vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.descartes.librede.bayesplusplus.ExtendedKalmanFilter;
import edu.kit.ipd.descartes.librede.estimation.models.observation.VectorObservationModel;
import edu.kit.ipd.descartes.librede.estimation.models.observation.functions.IOutputFunction;
import edu.kit.ipd.descartes.librede.estimation.models.observation.functions.ResponseTimeEquation;
import edu.kit.ipd.descartes.librede.estimation.models.observation.functions.UtilizationLaw;
import edu.kit.ipd.descartes.librede.estimation.models.state.ConstantStateModel;
import edu.kit.ipd.descartes.librede.estimation.models.state.constraints.Unconstrained;
import edu.kit.ipd.descartes.librede.estimation.repository.RepositoryCursor;
import edu.kit.ipd.descartes.librede.estimation.testutils.ObservationDataGenerator;
import edu.kit.ipd.descartes.librede.estimation.workload.WorkloadDescription;
import edu.kit.ipd.descartes.linalg.Vector;

public class ZhangKalmanFilterTest {
	
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
		
		RepositoryCursor cursor = generator.getRepository().getCursor(0, 1);
		cursor.setEndTime(ITERATIONS);
		WorkloadDescription workload = generator.getWorkloadDescription();
		
		Vector initialEstimate = vector(0.1);
		stateModel = new ConstantStateModel<>(1, initialEstimate);
		
		observationModel = new VectorObservationModel<>();		
		observationModel.addOutputFunction(new ResponseTimeEquation(workload, cursor, workload.getServices().get(0), workload.getResources()));
		observationModel.addOutputFunction(new UtilizationLaw(workload, cursor, workload.getResources().get(0)));
		
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
		
		RepositoryCursor cursor = generator.getRepository().getCursor(0, 1);
		cursor.setEndTime(ITERATIONS);
		WorkloadDescription workload = generator.getWorkloadDescription();
		
		Vector initialEstimate = vector(0.01, 0.01, 0.01, 0.01, 0.01);
		stateModel = new ConstantStateModel<>(5, initialEstimate);
		
		observationModel = new VectorObservationModel<>();
		for (int i = 0; i < 5; i++) {
			observationModel.addOutputFunction(new ResponseTimeEquation(workload, cursor, workload.getServices().get(i), workload.getResources()));
		}
		observationModel.addOutputFunction(new UtilizationLaw(workload, cursor, workload.getResources().get(0)));
		
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
