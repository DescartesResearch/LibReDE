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
package edu.kit.ipd.descartes.librede.testutils;

import static edu.kit.ipd.descartes.linalg.LinAlg.range;
import static edu.kit.ipd.descartes.linalg.LinAlg.vector;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.descartes.librede.repository.IRepositoryCursor;
import edu.kit.ipd.descartes.librede.repository.Query;
import edu.kit.ipd.descartes.librede.repository.QueryBuilder;
import edu.kit.ipd.descartes.librede.repository.StandardMetric;
import edu.kit.ipd.descartes.linalg.Vector;

public class ObservationDataGeneratorTest {
	
	private static final double EPSILON = 1e-4;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNextObservation1WC1R() {
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 1, 1);
		IRepositoryCursor view = generator.getRepository().getCursor(0, 1);
		
		Query<Vector> utilData = QueryBuilder.select(StandardMetric.UTILIZATION).forAllResources().average().using(view);
		Query<Vector> tputData = QueryBuilder.select(StandardMetric.THROUGHPUT).forAllServices().average().using(view);
		Query<Vector> rtData = QueryBuilder.select(StandardMetric.RESPONSE_TIME).forAllServices().average().using(view);
		
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
		IRepositoryCursor view = generator.getRepository().getCursor(0, 1);
		
		Query<Vector> utilData = QueryBuilder.select(StandardMetric.UTILIZATION).forAllResources().average().using(view);
		Query<Vector> tputData = QueryBuilder.select(StandardMetric.THROUGHPUT).forAllServices().average().using(view);
		Query<Vector> rtData = QueryBuilder.select(StandardMetric.RESPONSE_TIME).forAllServices().average().using(view);
		
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
		IRepositoryCursor view = generator.getRepository().getCursor(0, 1);
		
		Query<Vector> utilData = QueryBuilder.select(StandardMetric.UTILIZATION).forAllResources().average().using(view);
		Query<Vector> tputData = QueryBuilder.select(StandardMetric.THROUGHPUT).forAllServices().average().using(view);
		Query<Vector> rtData = QueryBuilder.select(StandardMetric.RESPONSE_TIME).forAllServices().average().using(view);
		
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
		IRepositoryCursor view = generator.getRepository().getCursor(0, 1);
		
		Query<Vector> utilData = QueryBuilder.select(StandardMetric.UTILIZATION).forAllResources().average().using(view);
		Query<Vector> tputData = QueryBuilder.select(StandardMetric.THROUGHPUT).forAllServices().average().using(view);
		Query<Vector> rtData = QueryBuilder.select(StandardMetric.RESPONSE_TIME).forAllServices().average().using(view);
		
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
	public void testNextObservation4WC1RWithBounds() {
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 4, 1);
		generator.setLowerUtilizationBound(0.4);
		generator.setUpperUtilizationBound(0.6);
		IRepositoryCursor view = generator.getRepository().getCursor(0, 1);
		
		Query<Vector> utilData = QueryBuilder.select(StandardMetric.UTILIZATION).forAllResources().average().using(view);
		
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
		IRepositoryCursor view = generator.getRepository().getCursor(0, 1);
		
		Query<Vector> tputData = QueryBuilder.select(StandardMetric.THROUGHPUT).forAllServices().average().using(view);
		
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
	
	private void assertObservation(Query<Vector> utilData, Query<Vector> tputData, Query<Vector> rtData, Vector demands) {		
		int wclCnt = tputData.getEntities().size();
		int resCnt =  utilData.getEntities().size();
		
		for (int r = 0; r < resCnt; r++) {
			double util = demands.slice(range(r * wclCnt, (r + 1) * wclCnt)).dot(tputData.execute());
			assertEquals(util, utilData.execute().get(r), EPSILON);
		}
		
		for (int i = 0; i < wclCnt; i++) {
			double sumRT = 0.0;
			for (int r = 0; r < resCnt; r++) {
				sumRT += demands.get(r * wclCnt + i) / (1 - demands.slice(range(wclCnt * r, (r + 1) * wclCnt)).dot(tputData.execute()));
			}
			assertEquals(sumRT, rtData.execute().get(i), EPSILON);
		}
	}
	

}
