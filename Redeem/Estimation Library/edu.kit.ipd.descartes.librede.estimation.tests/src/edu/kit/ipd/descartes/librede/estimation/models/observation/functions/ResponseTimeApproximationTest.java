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
package edu.kit.ipd.descartes.librede.estimation.models.observation.functions;

import static edu.kit.ipd.descartes.linalg.LinAlg.zeros;
import static edu.kit.ipd.descartes.linalg.testutil.MatrixAssert.assertThat;
import static edu.kit.ipd.descartes.linalg.testutil.VectorAssert.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.descartes.librede.estimation.models.observation.functions.ResponseTimeApproximation;
import edu.kit.ipd.descartes.librede.estimation.repository.Aggregation;
import edu.kit.ipd.descartes.librede.estimation.repository.Query;
import edu.kit.ipd.descartes.librede.estimation.repository.QueryBuilder;
import edu.kit.ipd.descartes.librede.estimation.repository.RepositoryCursor;
import edu.kit.ipd.descartes.librede.estimation.repository.StandardMetric;
import edu.kit.ipd.descartes.librede.estimation.testutils.Differentiation;
import edu.kit.ipd.descartes.librede.estimation.testutils.ObservationDataGenerator;
import edu.kit.ipd.descartes.librede.estimation.workload.Resource;
import edu.kit.ipd.descartes.librede.estimation.workload.Service;
import edu.kit.ipd.descartes.librede.estimation.workload.WorkloadDescription;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;

public class ResponseTimeApproximationTest {
	
	private final static int SERVICE_IDX = 2;
	private final static int RESOURCE_IDX = 1;
	
	private ObservationDataGenerator generator;
	private ResponseTimeApproximation law;
	private Vector state;
	
	private Resource resource;
	private Service service;
	private int stateIdx;
	private RepositoryCursor cursor;

	@Before
	public void setUp() throws Exception {
		generator = new ObservationDataGenerator(42, 5, 4);
		generator.setRandomDemands();
		
		WorkloadDescription workload = generator.getWorkloadDescription();
		cursor = generator.getRepository().getCursor(0, 1);
		cursor.setEndTime(1);
		
		resource = workload.getResources().get(RESOURCE_IDX);
		service = workload.getServices().get(SERVICE_IDX);
		stateIdx = workload.getState().getIndex(resource, service);
		
		law = new ResponseTimeApproximation(workload, cursor, resource, service, Aggregation.AVERAGE);
		state = generator.getDemands();
		
		generator.nextObservation();
		cursor.next();
	}

	@Test
	public void testGetFactor() {
		assertThat(law.getFactor()).isEqualTo(1.0, offset(1e-9));
	}

	@Test
	public void testGetObservedOutput() {
		Query<Scalar> resp = QueryBuilder.select(StandardMetric.RESPONSE_TIME).forService(service).average().using(cursor);
		assertThat(law.getObservedOutput()).isEqualTo(resp.execute().getValue(), offset(1e-9));
	}

	@Test
	public void testGetIndependentVariables() {
		assertThat(law.getIndependentVariables()).isEqualTo(zeros(state.rows()).set(stateIdx, 1.0), offset(1e-9));
	}

	@Test
	public void testGetCalculatedOutput() {
		assertThat(law.getCalculatedOutput(state)).isEqualTo(state.get(stateIdx), offset(1e-9));
	}

	@Test
	public void testGetFirstDerivatives() {
		Vector diff = Differentiation.diff1(law, state);
		assertThat(law.getFirstDerivatives(state)).isEqualTo(diff, offset(1e-4));
	}

	@Test
	public void testGetSecondDerivatives() {
		Matrix diff = Differentiation.diff2(law, state);
		assertThat(law.getSecondDerivatives(state)).isEqualTo(diff, offset(1e0));
	}

}
