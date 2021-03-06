/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2018, by Simon Spinner, Johannes Grohmann
 *  and Contributors.
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
import tools.descartes.librede.configuration.ResourceDemand;
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
		List<Service> services = Arrays.asList(WorkloadBuilder.newService("S1", resources.get(0), resources.get(2)), 
				WorkloadBuilder.newService("S2", resources.get(1)), WorkloadBuilder.newService("S3", resources.get(1), resources.get(2)));
		Vector initialState = vector(1, 2, 3, 4, 5);
		
		Builder<Unconstrained> builder = ConstantStateModel.unconstrainedModelBuilder();
		for (Resource res : resources) {
			for (ResourceDemand demand : res.getDemands()) {
				builder.addVariable(demand);
			}
		}
		builder.setStateInitializer(new PredefinedStateInitializer(initialState));
		
		ConstantStateModel<Unconstrained> stateModel = builder.build();
		assertThat(stateModel.getStateSize()).isEqualTo(5);
		assertThat(stateModel.getUserServices()).isEqualTo(services);
		assertThat(stateModel.getBackgroundServices()).isEmpty();
		assertThat(stateModel.getAllServices()).isEqualTo(services);
		assertThat(stateModel.getResources()).isEqualTo(resources);
		
		assertThat(stateModel.getResourceDemand(0).getResource()).isEqualTo(resources.get(0));
		assertThat(stateModel.getResourceDemand(1).getResource()).isEqualTo(resources.get(1));
		assertThat(stateModel.getResourceDemand(2).getResource()).isEqualTo(resources.get(1));
		assertThat(stateModel.getResourceDemand(3).getResource()).isEqualTo(resources.get(2));
		assertThat(stateModel.getResourceDemand(4).getResource()).isEqualTo(resources.get(2));
		
		assertThat(stateModel.getResourceDemand(0).getService()).isEqualTo(services.get(0));
		assertThat(stateModel.getResourceDemand(1).getService()).isEqualTo(services.get(1));
		assertThat(stateModel.getResourceDemand(2).getService()).isEqualTo(services.get(2));
		assertThat(stateModel.getResourceDemand(3).getService()).isEqualTo(services.get(0));
		assertThat(stateModel.getResourceDemand(4).getService()).isEqualTo(services.get(2));
		
		assertThat(stateModel.getInitialState()).isEqualTo(initialState, offset(1e-9));
		assertThat(stateModel.getStateVariableIndices(resources.get(0))).isEqualTo(indices(0));
		assertThat(stateModel.getStateVariableIndices(resources.get(1))).isEqualTo(indices(1, 2));
		assertThat(stateModel.getStateVariableIndices(resources.get(2))).isEqualTo(indices(3, 4));
	}

}
