package edu.kit.ipd.descartes.redeem.bayesplusplus.java;

import static edu.kit.ipd.descartes.linalg.LinAlg.vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.bayesplusplus.ExtendedKalmanFilter;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.VectorObservationModel;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IOutputFunction;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.ResponseTimeEquation;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.UtilizationLaw;
import edu.kit.ipd.descartes.redeem.estimation.models.state.ConstantStateModel;
import edu.kit.ipd.descartes.redeem.estimation.models.state.constraints.Unconstrained;
import edu.kit.ipd.descartes.redeem.estimation.testutils.Observation;
import edu.kit.ipd.descartes.redeem.estimation.testutils.ObservationDataGenerator;

public class ZhangKalmanFilterTest {
	
	private static final int ITERATIONS = 100000;
	
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
		
		Vector initialEstimate = vector(0.1);
		stateModel = new ConstantStateModel<>(1, initialEstimate);
		
		observationModel = new VectorObservationModel<>();		
		observationModel.addOutputFunction(new ResponseTimeEquation(generator.getSystemModel(), generator, generator.getSystemModel().getServices().get(0), generator.getSystemModel().getResources()));
		observationModel.addOutputFunction(new UtilizationLaw(generator.getSystemModel(), generator, generator.getSystemModel().getResources().get(0)));
		
		ExtendedKalmanFilter filter = new ExtendedKalmanFilter();
		filter.initialize(stateModel, observationModel);
		
		long start = System.nanoTime();		
		
		for (int i = 0; i < ITERATIONS; i++) {
			Observation ob = generator.nextObservation();
			
			Vector estimates = filter.estimate();
		}
		
		System.out.println("Duration: " + (System.nanoTime() - start));
		
		filter.destroy();
		
		
//		
//		
//		Matrix demands = matrix(row(0.05));
//		generator.setDemands(demands);
//		
//		Vector stateNoiseCovariance = vector(1.0);
//		Matrix stateNoiseCoupling = matrix(row(1));
//		ConstantStateModel stateModel = new ConstantStateModel(1, stateNoiseCovariance, stateNoiseCoupling);
//		
//		
//		Vector observeNoise = vector(0.0001);
//		ZhangModel zhangModel = new ZhangModel(1, 1, new int[] { 1 }, observeNoise);
//		
//		Vector initialEstimate = vector(0.1);
//		Matrix initialCovariance = matrix(row(0.01));
//		
//		ExtendedKalmanFilter filter = new ExtendedKalmanFilter(1, initialEstimate, initialCovariance);
//		
//		double[] estimates = new double[ITERATIONS];
//		
//		for (int i = 0; i < ITERATIONS; i++) {
//			Observation ob = generator.nextObservation();
//			
//			filter.predict(stateModel);
//			
//			Vector observation = vector(
//					ob.getMeanResponseTime().get(0), 
//					ob.getMeanUtilization().get(0));
//			
//			filter.observe(zhangModel, observation, ob.getMeanThroughput());
//			
//			filter.update();	
//			
//			Vector vec = filter.getCurrentEstimate();
//			estimates[i] = vec.get(0);
//		}
//		
//		assertEquals(0.05, Descriptive.mean(new DoubleArrayList(estimates)), 0.0001);
	}

}
