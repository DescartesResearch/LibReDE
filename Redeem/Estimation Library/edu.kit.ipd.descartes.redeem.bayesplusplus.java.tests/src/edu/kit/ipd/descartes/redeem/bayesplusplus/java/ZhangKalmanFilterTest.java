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
	}
	
	@Test
	public void testFilter5WC1R() throws Exception {
		
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 5, 1);		
		Vector demands = vector(0.03, 0.04, 0.05, 0.06, 0.07);
		generator.setDemands(demands);
		generator.setUpperUtilizationBound(0.9);
		
		Vector initialEstimate = vector(0.1, 0.1, 0.1, 0.1, 0.1);
		stateModel = new ConstantStateModel<>(5, initialEstimate);
		
		observationModel = new VectorObservationModel<>();
		for (int i = 0; i < 5; i++) {
			observationModel.addOutputFunction(new ResponseTimeEquation(generator.getSystemModel(), generator, generator.getSystemModel().getServices().get(i), generator.getSystemModel().getResources()));
		}
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
	}

}