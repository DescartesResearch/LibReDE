package edu.kit.ipd.descartes.redeem.estimation.models.observation.functions;

import static edu.kit.ipd.descartes.linalg.LinAlg.vector;
import static edu.kit.ipd.descartes.linalg.testutil.MatrixAssert.assertThat;
import static edu.kit.ipd.descartes.linalg.testutil.VectorAssert.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.testutils.Differentiation;
import edu.kit.ipd.descartes.redeem.estimation.testutils.Observation;
import edu.kit.ipd.descartes.redeem.estimation.testutils.ObservationDataGenerator;

public class ResponseTimeEquationTest {
	
	private final static int SERVICE_IDX = 2;
	
	private ObservationDataGenerator generator;
	private ResponseTimeEquation law;
	private Observation current;
	private Vector state;

	@Before
	public void setUp() throws Exception {
		generator = new ObservationDataGenerator(42, 5, 4);
		generator.setRandomDemands();
		
		law = new ResponseTimeEquation(generator.getSystemModel(), generator, generator.getSystemModel().getServices().get(SERVICE_IDX), generator.getSystemModel().getResources());
		current = generator.nextObservation();
		state = generator.getDemands();
	}

	@Test
	public void testGetObservedOutput() {
		assertThat(law.getObservedOutput()).isEqualTo(current.getMeanResponseTime().get(SERVICE_IDX), offset(1e-9));
	}

	@Test
	public void testGetCalculatedOutput() {
		assertThat(law.getCalculatedOutput(state)).isEqualTo(current.getMeanResponseTime().get(SERVICE_IDX), offset(1e-9));
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
