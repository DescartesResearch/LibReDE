/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2014, by Simon Spinner and Contributors.
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
package tools.descartes.librede.models.observation.functions;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static tools.descartes.librede.linalg.LinAlg.zeros;
import static tools.descartes.librede.linalg.testutil.MatrixAssert.assertThat;
import static tools.descartes.librede.linalg.testutil.VectorAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.repository.Aggregation;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.repository.StandardMetric;
import tools.descartes.librede.testutils.Differentiation;
import tools.descartes.librede.testutils.ObservationDataGenerator;

public class ResponseTimeApproximationTest {
	
	private final static int SERVICE_IDX = 2;
	private final static int RESOURCE_IDX = 1;
	
	private ObservationDataGenerator generator;
	private ResponseTimeApproximation law;
	private Vector state;
	
	private Resource resource;
	private Service service;
	private int stateIdx;
	private IRepositoryCursor cursor;

	@Before
	public void setUp() throws Exception {
		generator = new ObservationDataGenerator(42, 5, 4);
		generator.setRandomDemands();

		cursor = generator.getRepository().getCursor(0, 1);
		
		resource = generator.getStateModel().getResources().get(RESOURCE_IDX);
		service = generator.getStateModel().getServices().get(SERVICE_IDX);
		stateIdx = generator.getStateModel().getStateVariableIndex(resource, service);
		
		law = new ResponseTimeApproximation(generator.getStateModel(), cursor, resource, service, Aggregation.AVERAGE);
		state = generator.getDemands();
		
		generator.nextObservation();
		cursor.next();
	}

	@Test
	public void testGetFactor() {
		assertThat(law.getFactor()).isEqualTo(1.0, offset(1e-9));
	}

	@Test
	public void testGetObservedOutput() {
		Query<Scalar> resp = QueryBuilder.select(StandardMetric.RESPONSE_TIME).forService(service).average().using(cursor);
		assertThat(law.getObservedOutput()).isEqualTo(resp.execute().getValue(), offset(1e-9));
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
