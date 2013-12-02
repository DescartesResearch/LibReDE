package edu.kit.ipd.descartes.redeem.estimation.testutils;

import static edu.kit.ipd.descartes.linalg.LinAlg.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.repository.Metric;
import edu.kit.ipd.descartes.redeem.estimation.repository.ObservationRepositoryView;
import edu.kit.ipd.descartes.redeem.estimation.repository.Query;
import edu.kit.ipd.descartes.redeem.estimation.repository.QueryBuilder;

public class ObservationDataGeneratorTest {
	
	private static final double EPSILON = 1e-6;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNextObservation1WC1R() {
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 1, 1);
		ObservationRepositoryView view = generator.getRepository().createView(1);
		
		Query<Vector> utilData = QueryBuilder.select(Metric.UTILIZATION).forAllResources().average().using(view);
		Query<Vector> tputData = QueryBuilder.select(Metric.THROUGHPUT).forAllServices().average().using(view);
		Query<Vector> rtData = QueryBuilder.select(Metric.AVERAGE_RESPONSE_TIME).forAllServices().average().using(view);
		
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
		ObservationRepositoryView view = generator.getRepository().createView(1);
		
		Query<Vector> utilData = QueryBuilder.select(Metric.UTILIZATION).forAllResources().average().using(view);
		Query<Vector> tputData = QueryBuilder.select(Metric.THROUGHPUT).forAllServices().average().using(view);
		Query<Vector> rtData = QueryBuilder.select(Metric.AVERAGE_RESPONSE_TIME).forAllServices().average().using(view);
		
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
		ObservationRepositoryView view = generator.getRepository().createView(1);
		
		Query<Vector> utilData = QueryBuilder.select(Metric.UTILIZATION).forAllResources().average().using(view);
		Query<Vector> tputData = QueryBuilder.select(Metric.THROUGHPUT).forAllServices().average().using(view);
		Query<Vector> rtData = QueryBuilder.select(Metric.AVERAGE_RESPONSE_TIME).forAllServices().average().using(view);
		
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
		ObservationRepositoryView view = generator.getRepository().createView(1);
		
		Query<Vector> utilData = QueryBuilder.select(Metric.UTILIZATION).forAllResources().average().using(view);
		Query<Vector> tputData = QueryBuilder.select(Metric.THROUGHPUT).forAllServices().average().using(view);
		Query<Vector> rtData = QueryBuilder.select(Metric.AVERAGE_RESPONSE_TIME).forAllServices().average().using(view);
		
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
		ObservationRepositoryView view = generator.getRepository().createView(1);
		
		Query<Vector> utilData = QueryBuilder.select(Metric.UTILIZATION).forAllResources().average().using(view);
		
		Vector demands = vector(
				0.25,
				0.3,
				0.35,
				0.4);
		generator.setDemands(demands);
		
		for (int i = 0; i < 10000; i++) {
			generator.nextObservation();
			view.next();
			assertTrue(utilData.execute().get(0) >= 0.4);
			assertTrue(utilData.execute().get(0) <= 0.6);
		}
	}
	
	@Test
	public void testNextObservation4WC1RWithNoMix() {
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 4, 1);
		generator.setWorkloadMixVariation(0);
		ObservationRepositoryView view = generator.getRepository().createView(1);
		
		Query<Vector> tputData = QueryBuilder.select(Metric.THROUGHPUT).forAllServices().average().using(view);
		
		Vector demands = vector(
				0.25,
				0.3,
				0.35,
				0.4);
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
	
	private void assertObservation(Query<Vector> utilData, Query<Vector> tputData, Query<Vector> rtData, Matrix demands) {
		for (int r = 0; r < demands.columns(); r++) {
			double util = demands.column(r).dot(tputData.execute());
			assertEquals(util, utilData.execute().get(r), EPSILON);
		}
		
		for (int i = 0; i < demands.rows(); i++) {
			double sumRT = 0.0;
			for (int r = 0; r < demands.columns(); r++) {
				sumRT += demands.get(i, r) / (1 - demands.column(r).dot(tputData.execute()));
			}
			assertEquals(sumRT, rtData.execute().get(i), EPSILON);
		}
	}
	

}
