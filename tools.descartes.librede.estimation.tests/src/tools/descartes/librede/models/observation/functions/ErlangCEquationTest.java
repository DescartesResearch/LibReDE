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

import org.junit.Test;

import tools.descartes.librede.testutils.LibredeTest;

public class ErlangCEquationTest extends LibredeTest {

	@Test
	public void testOneServer() {
		ErlangCEquation eqn = new ErlangCEquation();
		assertThat(eqn.calculateValue(1, 0)).isEqualTo(0.0, offset(1e-9));
		assertThat(eqn.calculateValue(1, 0.5)).isEqualTo(0.5, offset(1e-9));
		assertThat(eqn.calculateValue(1, 1)).isEqualTo(1.0, offset(1e-9));
	}
	
	@Test
	public void testTwoServers() {
		ErlangCEquation eqn = new ErlangCEquation();
		assertThat(eqn.calculateValue(2, 0)).isEqualTo(0.0, offset(1e-3));
		assertThat(eqn.calculateValue(2, 0.25)).isEqualTo(0.1, offset(1e-3));
		assertThat(eqn.calculateValue(2, 0.5)).isEqualTo(1 / 3.0, offset(1e-3));
		assertThat(eqn.calculateValue(2, 0.75)).isEqualTo(0.642857142857143, offset(1e-3));
		assertThat(eqn.calculateValue(2, 1)).isEqualTo(1.0, offset(1e-3));
		
		assertThat(eqn.calculateFirstDerivative(2, 0.25, 1)).isEqualTo(0.720195188843065, offset(1e-3));
		assertThat(eqn.calculateFirstDerivative(2, 0.5, 1)).isEqualTo(1.11096532942676, offset(1e-3));
		assertThat(eqn.calculateFirstDerivative(2, 0.75, 1)).isEqualTo(1.34704232177155, offset(1e-3));
	}
	
	@Test
	public void testFifteenServers() {
		ErlangCEquation eqn = new ErlangCEquation();
		assertThat(eqn.calculateValue(15, 0)).isEqualTo(0.0, offset(1e-3));
		assertThat(eqn.calculateValue(15, 0.25)).isEqualTo(9.77920896491434e-06, offset(1e-3));
		assertThat(eqn.calculateValue(15, 0.5)).isEqualTo(0.0112924361348896, offset(1e-3));
		assertThat(eqn.calculateValue(15, 0.75)).isEqualTo(0.217989042733931, offset(1e-3));
		assertThat(eqn.calculateValue(15, 1)).isEqualTo(1.0, offset(1e-2));
	}
	
}
