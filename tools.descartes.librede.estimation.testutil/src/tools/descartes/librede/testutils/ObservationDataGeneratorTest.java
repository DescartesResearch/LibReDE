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
package tools.descartes.librede.testutils;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static tools.descartes.librede.linalg.LinAlg.vector;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.observation.functions.ErlangCEquation;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.units.Ratio;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.UnitsFactory;

public class ObservationDataGeneratorTest extends LibredeTest {
	
	private static final double EPSILON = 1e-4;
	
	private ErlangCEquation[] erlangC = new ErlangCEquation[3];
	
	@Before
	public void setUp() throws Exception {
		erlangC[1] = new ErlangCEquation(1);
		erlangC[2] = new ErlangCEquation(2);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testNextObservation2WC1RZeroDemand() {
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 2, 1);
		IRepositoryCursor view = generator.getRepository().getCursor(UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS), UnitsFactory.eINSTANCE.createQuantity(1, Time.SECONDS));
		
		Query<Vector, Ratio> utilData = QueryBuilder.select(StandardMetrics.UTILIZATION).in(Ratio.NONE).forResources(generator.getStateModel().getResources()).average().using(view);
		Query<Vector, RequestRate> tputData = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forServices(generator.getStateModel().getUserServices()).average().using(view);
		Query<Vector, Time> rtData = QueryBuilder.select(StandardMetrics.RESPONSE_TIME).in(Time.SECONDS).forServices(generator.getStateModel().getUserServices()).average().using(view);
		
		Vector demands = vector(0.25, 0.0);
		generator.setDemands(demands);
		
		for (int i = 0; i < 10000; i++) {
			generator.nextObservation();
			view.next();
			assertObservation(utilData, tputData, rtData, demands);
		}
	}

	@Test
	public void testNextObservation1WC1R() {
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 1, 1);
		IRepositoryCursor view = generator.getRepository().getCursor(UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS), UnitsFactory.eINSTANCE.createQuantity(1, Time.SECONDS));
		
		Query<Vector, Ratio> utilData = QueryBuilder.select(StandardMetrics.UTILIZATION).in(Ratio.NONE).forResources(generator.getStateModel().getResources()).average().using(view);
		Query<Vector, RequestRate> tputData = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forServices(generator.getStateModel().getUserServices()).average().using(view);
		Query<Vector, Time> rtData = QueryBuilder.select(StandardMetrics.RESPONSE_TIME).in(Time.SECONDS).forServices(generator.getStateModel().getUserServices()).average().using(view);
		
		Vector demands = vector(0.25);
		generator.setDemands(demands);
		
		for (int i = 0; i < 10000; i++) {
			generator.nextObservation();
			view.next();
			assertObservation(utilData, tputData, rtData, demands);
		}
	}
	
	@Test
	public void testNextObservation4WC1R() {
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 4, 1);
		IRepositoryCursor view = generator.getRepository().getCursor(UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS), UnitsFactory.eINSTANCE.createQuantity(1, Time.SECONDS));
		
		Query<Vector,Ratio> utilData = QueryBuilder.select(StandardMetrics.UTILIZATION).in(Ratio.NONE).forResources(generator.getStateModel().getResources()).average().using(view);
		Query<Vector, RequestRate> tputData = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forServices(generator.getStateModel().getUserServices()).average().using(view);
		Query<Vector, Time> rtData = QueryBuilder.select(StandardMetrics.RESPONSE_TIME).in(Time.SECONDS).forServices(generator.getStateModel().getUserServices()).average().using(view);
		
		Vector demands = vector(0.25, 0.3, 0.35, 0.4);
		generator.setDemands(demands);
		
		for (int i = 0; i < 10000; i++) {
			generator.nextObservation();
			view.next();
			assertObservation(utilData, tputData, rtData, demands);
		}
	}
	
	@Test
	public void testNextObservation4WC1RParallel() {
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 4, 1, 2);
		IRepositoryCursor view = generator.getRepository().getCursor(UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS), UnitsFactory.eINSTANCE.createQuantity(1, Time.SECONDS));
		
		Query<Vector,Ratio> utilData = QueryBuilder.select(StandardMetrics.UTILIZATION).in(Ratio.NONE).forResources(generator.getStateModel().getResources()).average().using(view);
		Query<Vector, RequestRate> tputData = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forServices(generator.getStateModel().getUserServices()).average().using(view);
		Query<Vector, Time> rtData = QueryBuilder.select(StandardMetrics.RESPONSE_TIME).in(Time.SECONDS).forServices(generator.getStateModel().getUserServices()).average().using(view);
		
		Vector demands = vector(0.25, 0.3, 0.35, 0.4);
		generator.setDemands(demands);
		
		for (int i = 0; i < 10000; i++) {
			generator.nextObservation();
			view.next();
			assertObservation(utilData, tputData, rtData, demands);
		}
	}
	
	
	@Test
	public void testNextObservation1WC4R() {
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 1, 4);
		IRepositoryCursor view = generator.getRepository().getCursor(UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS), UnitsFactory.eINSTANCE.createQuantity(1, Time.SECONDS));
		
		Query<Vector, Ratio> utilData = QueryBuilder.select(StandardMetrics.UTILIZATION).in(Ratio.NONE).forResources(generator.getStateModel().getResources()).average().using(view);
		Query<Vector, RequestRate> tputData = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forServices(generator.getStateModel().getUserServices()).average().using(view);
		Query<Vector, Time> rtData = QueryBuilder.select(StandardMetrics.RESPONSE_TIME).in(Time.SECONDS).forServices(generator.getStateModel().getUserServices()).average().using(view);
		
		Vector demands = vector(0.25, 0.3, 0.35, 0.4);
		generator.setDemands(demands);
		
		for (int i = 0; i < 10000; i++) {
			generator.nextObservation();
			view.next();
			assertObservation(utilData, tputData, rtData, demands);
		}
	}
	
	@Test
	public void testNextObservation4WC4R() {
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 4, 4);
		IRepositoryCursor view = generator.getRepository().getCursor(UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS), UnitsFactory.eINSTANCE.createQuantity(1, Time.SECONDS));
		
		Query<Vector, Ratio> utilData = QueryBuilder.select(StandardMetrics.UTILIZATION).in(Ratio.NONE).forResources(generator.getStateModel().getResources()).average().using(view);
		Query<Vector, RequestRate> tputData = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forServices(generator.getStateModel().getUserServices()).average().using(view);
		Query<Vector, Time> rtData = QueryBuilder.select(StandardMetrics.RESPONSE_TIME).in(Time.SECONDS).forServices(generator.getStateModel().getUserServices()).average().using(view);
		
		Vector demands = vector(
				0.25, 0.3, 0.35, 0.4,
				0.4, 0.25, 0.3, 0.35,
				0.35, 0.4, 0.25, 0.3,
				0.3, 0.35, 0.4, 0.25);
		generator.setDemands(demands);
		
		for (int i = 0; i < 10000; i++) {
			generator.nextObservation();
			view.next();
			assertObservation(utilData, tputData, rtData, demands);
		}
	}
	
	@Test
	public void testNextObservation4WC4RNonRec() {
		boolean[][] mapping = new boolean[][] { {true, false, false, false}, {true, true, false, false}, {true, false, true, false}, {false, false, true, true} };
		
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 4, 4, mapping);
		
		assertThat(generator.getStateModel().getStateSize()).isEqualTo(7);

		IRepositoryCursor view = generator.getRepository().getCursor(UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS), UnitsFactory.eINSTANCE.createQuantity(1, Time.SECONDS));
		
		Query<Vector, Ratio> utilData = QueryBuilder.select(StandardMetrics.UTILIZATION).in(Ratio.NONE).forResources(generator.getStateModel().getResources()).average().using(view);
		Query<Vector, RequestRate> tputData = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forServices(generator.getStateModel().getUserServices()).average().using(view);
		Query<Vector, Time> rtData = QueryBuilder.select(StandardMetrics.RESPONSE_TIME).in(Time.SECONDS).forServices(generator.getStateModel().getUserServices()).average().using(view);
		
		Vector demands = vector(
				0.25, 
				0.3, 0.35, 
				0.4, 0.25, 
				0.3, 0.35);
		generator.setDemands(demands);
		
		for (int i = 0; i < 10000; i++) {
			generator.nextObservation();
			view.next();
			assertObservation(utilData, tputData, rtData, demands);
		}
	}
	
	@Test
	public void testNextObservation4WC1RWithBounds() {
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 4, 1);
		generator.setLowerUtilizationBound(0.4);
		generator.setUpperUtilizationBound(0.6);
		IRepositoryCursor view = generator.getRepository().getCursor(UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS), UnitsFactory.eINSTANCE.createQuantity(1, Time.SECONDS));
		
		Query<Vector, Ratio> utilData = QueryBuilder.select(StandardMetrics.UTILIZATION).in(Ratio.NONE).forResources(generator.getStateModel().getResources()).average().using(view);
		
		Vector demands = vector(0.25, 0.3, 0.35, 0.4);
		generator.setDemands(demands);
		
		for (int i = 0; i < 10000; i++) {
			generator.nextObservation();
			view.next();
			if (i >= 1) {
				assertTrue(utilData.execute().get(0) >= 0.4);
				assertTrue(utilData.execute().get(0) <= 0.6);
			}
		}
	}
	
	@Test
	public void testNextObservation4WC1RWithNoMix() {
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 4, 1);
		generator.setWorkloadMixVariation(0);
		IRepositoryCursor view = generator.getRepository().getCursor(UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS), UnitsFactory.eINSTANCE.createQuantity(1, Time.SECONDS));
		
		Query<Vector, RequestRate> tputData = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forServices(generator.getStateModel().getUserServices()).average().using(view);
		
		Vector demands = vector(0.25, 0.3, 0.35, 0.4);
		generator.setDemands(demands);
		
		for (int i = 0; i < 10000; i++) {
			generator.nextObservation();
			view.next();
			Vector tput = tputData.execute();
			for (int w = 1; w < 4; w++) {
				assertEquals(tput.get(w-1), tput.get(w), EPSILON);
			}
		}
	}
	
	private void assertObservation(Query<Vector, Ratio> utilData, Query<Vector, RequestRate> tputData, Query<Vector, Time> rtData, Vector demands) {		
		@SuppressWarnings("unchecked")
		List<Service> services = (List<Service>) tputData.getEntities();
		@SuppressWarnings("unchecked")
		List<Resource> resources = (List<Resource>) utilData.getEntities();
		
		int stateVar = 0;
		double[] sumD = new double[services.size()]; // sum of all demands of service
		double[] sumRT = new double[services.size()];
		for (Resource res : resources) {
			double util = 0;
			int temp = stateVar;			
			for (Service serv : res.getServices()) {
				int idx = tputData.indexOf(serv);
				sumD[idx] += demands.get(temp);
				util += demands.get(temp) * tputData.execute().get(idx) / res.getNumberOfServers();
				temp++;
			}
			assertThat(util).isEqualTo(utilData.execute().get(utilData.indexOf(res)), offset(EPSILON));
			
			for (Service serv : res.getServices()) {
				int idx = tputData.indexOf(serv);
				sumRT[idx] += (demands.get(stateVar) * (erlangC[res.getNumberOfServers()].calculateValue(util) + 1 - util)) / (1 - util);
				stateVar++;
			}
		}
		Vector act = rtData.execute();
		Vector tput = tputData.execute();
		for (int i = 0; i < act.rows(); i++) {
			if (sumD[i] > 0.0) {
				assertThat(act.get(i)).isEqualTo(sumRT[i], offset(EPSILON));
			} else {
				// if no request are observed for a service, the *average* response time should be NaN
				// indicating that no value was observed.
				assertThat(act.get(i)).isNaN();
				assertThat(tput.get(i)).isZero();
			}
			
		}		
	}
}
