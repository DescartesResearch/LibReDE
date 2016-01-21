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
package tools.descartes.librede.models.observation.functions;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static tools.descartes.librede.linalg.testutil.MatrixAssert.assertThat;
import static tools.descartes.librede.linalg.testutil.VectorAssert.assertThat;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.junit.Before;
import org.junit.Test;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.State;
import tools.descartes.librede.models.diff.DifferentiationUtils;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.testutils.Differentiation;
import tools.descartes.librede.testutils.LibredeTest;
import tools.descartes.librede.testutils.ObservationDataGenerator;
import tools.descartes.librede.units.Ratio;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.UnitsFactory;

public class ServiceDemandLawTest extends LibredeTest {
	
	private final static int STATE_IDX = 7;
	
	private ObservationDataGenerator generator;
	private ServiceDemandLaw law;
	private IRepositoryCursor cursor;
	private State state;
	
	private Resource resource;
	private Service service;

	@Before
	public void setUp() throws Exception {
		generator = new ObservationDataGenerator(42, 5, 4);
		generator.setRandomDemands();
		
		cursor = generator.getRepository().getCursor(UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS), UnitsFactory.eINSTANCE.createQuantity(1, Time.SECONDS));
		
		ResourceDemand demand = generator.getStateModel().getResourceDemand(STATE_IDX);
		resource = demand.getResource();
		service = demand.getService();
		
		law = new ServiceDemandLaw(generator.getStateModel(), cursor, resource, service);
		state = generator.getDemands();	
		
		generator.nextObservation();
		cursor.next();
	}

	@Test
	public void testGetObservedOutput() {
		Query<Vector, RequestRate> x = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forServices(generator.getStateModel().getUserServices()).average().using(cursor);
		Query<Vector, Time> r = QueryBuilder.select(StandardMetrics.RESPONSE_TIME).in(Time.SECONDS).forServices(generator.getStateModel().getUserServices()).average().using(cursor);
		double util = QueryBuilder.select(StandardMetrics.UTILIZATION).in(Ratio.NONE).forResource(resource).average().using(cursor).execute().getValue();
		
		Vector xVec = x.execute();
		Vector rVec = r.execute();
		assertThat(law.getObservedOutput()).isEqualTo(xVec.get(x.indexOf(service)) * rVec.get(r.indexOf(service)) * util / xVec.dot(rVec), offset(1e-9));
	}

	@Test
	public void testGetCalculatedOutput() {
		double x = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forService(service).average().using(cursor).execute().getValue();
		double expected = x * state.getVariable(resource, service).getValue();
		
		assertThat(law.getCalculatedOutput(state).getValue()).isEqualTo(expected, offset(1e-9));
	}
	
	@Test
	public void testGetFactor() {
		double x = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forService(service).average().using(cursor).execute().getValue();
		assertThat(law.getFactor()).isEqualTo(x, offset(1e-9));
	}
	
	@Test
	public void testGetDerivatives() {
		Vector diff1 = Differentiation.diff1(law, state);
		Matrix diff2 = Differentiation.diff2(law, state);
		
		DerivativeStructure s = law.getCalculatedOutput(new State(state.getStateModel(), state.getVector(), 2)).getDerivativeStructure();
		assertThat(DifferentiationUtils.getFirstDerivatives(s)).isEqualTo(diff1, offset(1e-4));
		assertThat(DifferentiationUtils.getSecondDerivatives(s)).isEqualTo(diff2, offset(1e-4));
	}

}
