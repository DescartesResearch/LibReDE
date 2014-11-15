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
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.repository.StandardMetric;
import tools.descartes.librede.testutils.Differentiation;
import tools.descartes.librede.testutils.ObservationDataGenerator;

public class UtilizationLawTest {
		
	private final static int RESOURCE_IDX = 2;
	
	private ObservationDataGenerator generator;
	private UtilizationLaw law;
	private IRepositoryCursor cursor;
	private Vector state;
	private Resource resource;

	@Before
	public void setUp() throws Exception {		
		generator = new ObservationDataGenerator(42, 5, 4);
		generator.setRandomDemands();

		cursor = generator.getRepository().getCursor(0, 1);
		
		resource = generator.getStateModel().getResources().get(RESOURCE_IDX);
		law = new UtilizationLaw(generator.getStateModel(), cursor, resource);
		state = generator.getDemands();		
		
		generator.nextObservation();
		cursor.next();
	}

	@Test
	public void testGetIndependentVariables() {
		Vector x = QueryBuilder.select(StandardMetric.THROUGHPUT).forAllServices().average().using(cursor).execute();
		Vector varVector = law.getIndependentVariables();		
		Vector expectedVarVector = zeros(state.rows()).set(generator.getStateModel().getStateVariableIndexRange(resource), x);
		
		assertThat(varVector).isEqualTo(expectedVarVector, offset(1e-9));
	}

	@Test
	public void testGetObservedOutput() {
		double util = QueryBuilder.select(StandardMetric.UTILIZATION).forResource(resource).average().using(cursor).execute().getValue();
		assertThat(law.getObservedOutput()).isEqualTo(util, offset(1e-9));
	}

	@Test
	public void testGetCalculatedOutput() {
		double util = QueryBuilder.select(StandardMetric.UTILIZATION).forResource(resource).average().using(cursor).execute().getValue();
		assertThat(law.getCalculatedOutput(state)).isEqualTo(util, offset(1e-9));
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
