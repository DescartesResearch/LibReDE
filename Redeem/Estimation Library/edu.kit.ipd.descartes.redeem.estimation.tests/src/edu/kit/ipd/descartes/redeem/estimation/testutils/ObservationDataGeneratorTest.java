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
		
		Matrix demands = matrix(row(0.25));
		generator.setDemands(demands);
		
		for (int i = 0; i < 10000; i++) {
			assertObservation(generator.nextObservation(), demands);
		}
	}
	
	@Test
	public void testNextObservation4WC1R() {
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 4, 1);
		
		Matrix demands = matrix(row(0.25), row(0.3), row(0.35), row(0.4));
		generator.setDemands(demands);
		
		for (int i = 0; i < 10000; i++) {
			assertObservation(generator.nextObservation(), demands);
		}
	}
	
	
	@Test
	public void testNextObservation1WC4R() {
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 1, 4);
		
		Matrix demands = matrix(row(0.25, 0.3, 0.35, 0.4));
		generator.setDemands(demands);
		
		for (int i = 0; i < 10000; i++) {
			assertObservation(generator.nextObservation(), demands);
		}
	}
	
	@Test
	public void testNextObservation4WC4R() {
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 4, 4);
		
		Matrix demands = matrix(
				row(0.25, 0.3, 0.35, 0.4),
				row(0.4, 0.25, 0.3, 0.35),
				row(0.35, 0.4, 0.25, 0.3),
				row(0.3, 0.35, 0.4, 0.25));
		generator.setDemands(demands);
		
		for (int i = 0; i < 10000; i++) {
			assertObservation(generator.nextObservation(), demands);
		}
	}
	
	@Test
	public void testNextObservation4WC1RWithBounds() {
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 4, 1);
		generator.setLowerUtilizationBound(0.4);
		generator.setUpperUtilizationBound(0.6);
		
		Matrix demands = matrix(
				row(0.25),
				row(0.3),
				row(0.35),
				row(0.4));
		generator.setDemands(demands);
		
		for (int i = 0; i < 10000; i++) {
			Observation ob = generator.nextObservation();
			assertTrue(ob.getMeanUtilization().get(0) >= 0.4);
			assertTrue(ob.getMeanUtilization().get(0) <= 0.6);
		}
	}
	
	@Test
	public void testNextObservation4WC1RWithNoMix() {
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 4, 1);
		generator.setWorkloadMixVariation(0);
		
		Matrix demands = matrix(
				row(0.25),
				row(0.3),
				row(0.35),
				row(0.4));
		generator.setDemands(demands);
		
		for (int i = 0; i < 10000; i++) {
			Observation ob = generator.nextObservation();
			for (int w = 1; w < 4; w++) {
				assertEquals(ob.getMeanThroughput().get(w-1), ob.getMeanThroughput().get(w), EPSILON);
			}
		}
	}
	
	private void assertObservation(Observation ob, Matrix demands) {
		for (int r = 0; r < demands.columns(); r++) {
			double util = demands.column(r).dot(ob.getMeanThroughput());
			assertEquals(util, ob.getMeanUtilization().get(r), EPSILON);
		}
		
		for (int i = 0; i < demands.rows(); i++) {
			double sumRT = 0.0;
			for (int r = 0; r < demands.columns(); r++) {
				sumRT += demands.get(i, r) / (1 - demands.column(r).dot(ob.getMeanThroughput()));
			}
			assertEquals(sumRT, ob.getMeanResponseTime().get(i), EPSILON);
		}
	}
	

}
