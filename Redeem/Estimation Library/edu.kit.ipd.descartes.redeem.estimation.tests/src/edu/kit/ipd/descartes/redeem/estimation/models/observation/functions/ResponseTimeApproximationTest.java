package edu.kit.ipd.descartes.redeem.estimation.models.observation.functions;

import static edu.kit.ipd.descartes.linalg.LinAlg.zeros;
import static edu.kit.ipd.descartes.linalg.testutil.MatrixAssert.assertThat;
import static edu.kit.ipd.descartes.linalg.testutil.VectorAssert.assertThat;
import static org.junit.Assert.*;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.testutils.Differentiation;
import edu.kit.ipd.descartes.redeem.estimation.testutils.Observation;
import edu.kit.ipd.descartes.redeem.estimation.testutils.ObservationDataGenerator;
import edu.kit.ipd.descartes.redeem.estimation.workload.Resource;
import edu.kit.ipd.descartes.redeem.estimation.workload.Service;

public class ResponseTimeApproximationTest {
	
	private final static int SERVICE_IDX = 2;
	private final static int RESOURCE_IDX = 1;
	
	private ObservationDataGenerator generator;
	private ResponseTimeApproximation law;
	private Observation current;
	private Vector state;
	
	private Resource resource;
	private Service service;
	private int stateIdx;

	@Before
	public void setUp() throws Exception {
		generator = new ObservationDataGenerator(42, 5, 4);
		generator.setRandomDemands();
		
		resource = generator.getSystemModel().getResources().get(RESOURCE_IDX);
		service = generator.getSystemModel().getServices().get(SERVICE_IDX);
		stateIdx = generator.getSystemModel().getState().getIndex(resource, service);
		
		law = new ResponseTimeApproximation(generator.getSystemModel(), generator, resource, service);
		current = generator.nextObservation();
		state = generator.getDemands();
	}

	@Test
	public void testGetFactor() {
		assertThat(law.getFactor()).isEqualTo(1.0, offset(1e-9));
	}

	@Test
	public void testGetObservedOutput() {
		assertThat(law.getObservedOutput()).isEqualTo(current.getMeanResponseTime().get(SERVICE_IDX), offset(1e-9));
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
