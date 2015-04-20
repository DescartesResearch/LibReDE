package tools.descartes.librede.models.state;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static tools.descartes.librede.linalg.LinAlg.indices;
import static tools.descartes.librede.linalg.LinAlg.vector;
import static tools.descartes.librede.linalg.testutil.VectorAssert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.state.ConstantStateModel.Builder;
import tools.descartes.librede.models.state.constraints.Unconstrained;
import tools.descartes.librede.models.state.initial.PredefinedStateInitializer;
import tools.descartes.librede.repository.WorkloadBuilder;
import tools.descartes.librede.testutils.LibredeTest;

public class ConstantStateModelTest extends LibredeTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNonRecStateModel() {
		List<Resource> resources = Arrays.asList(WorkloadBuilder.newResource("R1"), WorkloadBuilder.newResource("R2"), WorkloadBuilder.newResource("R3"));
		List<Service> services = Arrays.asList(WorkloadBuilder.newService("S1"), WorkloadBuilder.newService("S2"), WorkloadBuilder.newService("S3"));
		Vector initialState = vector(1, 2, 3, 4, 5);
		
		Builder<Unconstrained> builder = ConstantStateModel.unconstrainedModelBuilder();
		builder.addVariable(resources.get(0), services.get(0));
		builder.addVariable(resources.get(2), services.get(0));
		builder.addVariable(resources.get(1), services.get(1));
		builder.addVariable(resources.get(1), services.get(2));
		builder.addVariable(resources.get(2), services.get(2));
		builder.setStateInitializer(new PredefinedStateInitializer(initialState));
		
		ConstantStateModel<Unconstrained> stateModel = builder.build();
		assertThat(stateModel.getStateSize()).isEqualTo(5);
		assertThat(stateModel.getUserServices()).isEqualTo(services);
		assertThat(stateModel.getBackgroundServices()).isEmpty();
		assertThat(stateModel.getAllServices()).isEqualTo(services);
		assertThat(stateModel.getResources()).isEqualTo(resources);
		
		assertThat(stateModel.getResource(0)).isEqualTo(resources.get(0));
		assertThat(stateModel.getResource(1)).isEqualTo(resources.get(1));
		assertThat(stateModel.getResource(2)).isEqualTo(resources.get(1));
		assertThat(stateModel.getResource(3)).isEqualTo(resources.get(2));
		assertThat(stateModel.getResource(4)).isEqualTo(resources.get(2));
		
		assertThat(stateModel.getService(0)).isEqualTo(services.get(0));
		assertThat(stateModel.getService(1)).isEqualTo(services.get(1));
		assertThat(stateModel.getService(2)).isEqualTo(services.get(2));
		assertThat(stateModel.getService(3)).isEqualTo(services.get(0));
		assertThat(stateModel.getService(4)).isEqualTo(services.get(2));
		
		assertThat(stateModel.getInitialState()).isEqualTo(initialState, offset(1e-9));
		assertThat(stateModel.getStateVariableIndices(resources.get(0))).isEqualTo(indices(0));
		assertThat(stateModel.getStateVariableIndices(resources.get(1))).isEqualTo(indices(1, 2));
		assertThat(stateModel.getStateVariableIndices(resources.get(2))).isEqualTo(indices(3, 4));
	}

}
