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

import static edu.kit.ipd.descartes.linalg.testutil.MatrixAssert.assertThat;
import static edu.kit.ipd.descartes.linalg.testutil.VectorAssert.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import net.descartesresearch.librede.configuration.Resource;
import net.descartesresearch.librede.configuration.Service;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.descartes.librede.models.observation.functions.ServiceDemandLaw;
import edu.kit.ipd.descartes.librede.repository.IRepositoryCursor;
import edu.kit.ipd.descartes.librede.repository.QueryBuilder;
import edu.kit.ipd.descartes.librede.repository.StandardMetric;
import edu.kit.ipd.descartes.librede.testutils.Differentiation;
import edu.kit.ipd.descartes.librede.testutils.ObservationDataGenerator;
import edu.kit.ipd.descartes.librede.workload.WorkloadDescription;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;

public class ServiceDemandLawTest {
	
	private final static int SERVICE_IDX = 2;
	private final static int RESOURCE_IDX = 1;
	
	private ObservationDataGenerator generator;
	private ServiceDemandLaw law;
	private IRepositoryCursor cursor;
	private Vector state;
	
	private Resource resource;
	private Service service;

	@Before
	public void setUp() throws Exception {
		generator = new ObservationDataGenerator(42, 5, 4);
		generator.setRandomDemands();
		
		WorkloadDescription workload = generator.getWorkloadDescription();
		cursor = generator.getRepository().getCursor(0, 1);
		
		resource = workload.getResources().get(RESOURCE_IDX);
		service = workload.getServices().get(SERVICE_IDX);
		
		law = new ServiceDemandLaw(workload, cursor, resource, service);
		state = generator.getDemands();	
		
		generator.nextObservation();
		cursor.next();
	}

	@Test
	public void testGetObservedOutput() {
		Vector x = QueryBuilder.select(StandardMetric.THROUGHPUT).forAllServices().average().using(cursor).execute();
		Vector r = QueryBuilder.select(StandardMetric.RESPONSE_TIME).forAllServices().average().using(cursor).execute();
		double util = QueryBuilder.select(StandardMetric.UTILIZATION).forResource(resource).average().using(cursor).execute().getValue();
		
		assertThat(law.getObservedOutput()).isEqualTo(x.get(SERVICE_IDX) * r.get(SERVICE_IDX) * util / x.dot(r), offset(1e-9));
	}

	@Test
	public void testGetCalculatedOutput() {
		double x = QueryBuilder.select(StandardMetric.THROUGHPUT).forService(service).average().using(cursor).execute().getValue();
		double expected = x * state.get(generator.getWorkloadDescription().getState().getIndex(resource, service));
		
		assertThat(law.getCalculatedOutput(state)).isEqualTo(expected, offset(1e-9));
	}
	
	@Test
	public void testGetFactor() {
		double x = QueryBuilder.select(StandardMetric.THROUGHPUT).forService(service).average().using(cursor).execute().getValue();
		assertThat(law.getFactor()).isEqualTo(x, offset(1e-9));
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
