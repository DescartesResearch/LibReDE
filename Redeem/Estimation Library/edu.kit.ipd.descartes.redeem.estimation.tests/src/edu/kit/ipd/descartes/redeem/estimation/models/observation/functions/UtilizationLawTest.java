package edu.kit.ipd.descartes.redeem.estimation.models.observation.functions;

import static edu.kit.ipd.descartes.linalg.LinAlg.zeros;
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
import edu.kit.ipd.descartes.redeem.estimation.workload.Resource;

public class UtilizationLawTest {
		
	private final static int RESOURCE_IDX = 2;
	
	private ObservationDataGenerator generator;
	private UtilizationLaw law;
	private Observation current;
	private Vector state;
	private Resource resource;

	@Before
	public void setUp() throws Exception {		
		generator = new ObservationDataGenerator(42, 5, 4);
		generator.setRandomDemands();
		
		resource = generator.getSystemModel().getResources().get(RESOURCE_IDX);
		law = new UtilizationLaw(generator.getSystemModel(), generator, resource);
		current = generator.nextObservation();
		state = generator.getDemands();
	}

	@Test
	public void testGetIndependentVariables() {
		Vector varVector = law.getIndependentVariables();		
		Vector expectedVarVector = zeros(state.rows()).set(generator.getSystemModel().getState().getRange(resource), current.getMeanThroughput());
		
		assertThat(varVector).isEqualTo(expectedVarVector, offset(1e-9));
	}

	@Test
	public void testGetObservedOutput() {
		assertThat(law.getObservedOutput()).isEqualTo(current.getMeanUtilization().get(RESOURCE_IDX), offset(1e-9));
	}

	@Test
	public void testGetCalculatedOutput() {
		assertThat(law.getCalculatedOutput(state)).isEqualTo(current.getMeanUtilization().get(RESOURCE_IDX), offset(1e-9));
	}

	@Test
	public void testGetFirstDerivatives() {
		Vector diff = Differentiation.diff1(law, state);
		assertThat(law.getFirstDerivatives(state)).isEqualTo(diff, offset(1e-4));
	}

	@Test
	public void testGetSecondDerivatives() {
		Matrix diff = Differentiation.diff2(law, state);
		assertThat(law.getSecondDerivatives(state)).isEqualTo(diff, offset(1e-4));
	}

}
