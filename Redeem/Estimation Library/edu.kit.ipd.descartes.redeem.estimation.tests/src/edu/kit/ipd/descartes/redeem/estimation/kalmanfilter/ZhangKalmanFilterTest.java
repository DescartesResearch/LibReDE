package edu.kit.ipd.descartes.redeem.estimation.kalmanfilter;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cern.colt.list.DoubleArrayList;
import cern.jet.stat.Descriptive;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.bayesplusplus.ExtendedKalmanFilter;
import edu.kit.ipd.descartes.redeem.estimation.testutils.Observation;
import edu.kit.ipd.descartes.redeem.estimation.testutils.ObservationDataGenerator;

public class ZhangKalmanFilterTest {
	
	private static final int ITERATIONS = 10000;
	

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFilter1WC1R() {
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 1, 1);
		
		Matrix demands = Matrix.create(1, 1);
		demands.set(0, 0, 0.05);
		generator.setDemands(demands);
		
		Vector stateNoiseCovariance = Vector.create(1);
		stateNoiseCovariance.assign(1.0);
		Matrix stateNoiseCoupling = Matrix.create(1, 1);
		stateNoiseCoupling.assign(new double[][] {{1}});
		ConstantStateModel stateModel = new ConstantStateModel(1, stateNoiseCovariance, stateNoiseCoupling);
		
		
		Vector observeNoise = Vector.create(1);
		observeNoise.assign(0.0001);
		ZhangModel zhangModel = new ZhangModel(1, 1, new int[] { 1 }, observeNoise);
		
		Vector initialEstimate = Vector.create(1);
		initialEstimate.assign(0.1);
		Matrix initialCovariance = Matrix.create(1, 1);
		initialCovariance.assign(new double[][] {{0.01}});
		
		ExtendedKalmanFilter filter = new ExtendedKalmanFilter(1, initialEstimate, initialCovariance);
		
		double[] estimates = new double[ITERATIONS];
		
		for (int i = 0; i < ITERATIONS; i++) {
			Observation ob = generator.nextObservation();
			
			filter.predict(stateModel);
			
			Vector observation = Vector.create(2);
			observation.set(0, ob.getMeanResponseTime().get(0));
			observation.set(1, ob.getMeanUtilization().get(0));
			
			filter.observe(zhangModel, observation, ob.getMeanThroughput());
			
			filter.update();	
			
			Vector vec = filter.getCurrentEstimate();
			estimates[i] = vec.get(0);
		}
		
		assertEquals(0.05, Descriptive.mean(new DoubleArrayList(estimates)), 0.0001);
	}

}
