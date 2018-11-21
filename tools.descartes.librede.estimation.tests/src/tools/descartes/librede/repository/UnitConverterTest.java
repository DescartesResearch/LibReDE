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
package tools.descartes.librede.repository;

import static tools.descartes.librede.linalg.LinAlg.vector;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static tools.descartes.librede.linalg.testutil.VectorAssert.assertThat;

import org.junit.Test;

import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.testutils.LibredeTest;
import tools.descartes.librede.units.Time;

public class UnitConverterTest extends LibredeTest {

	@Test
	public void test() {
		Vector time = vector(0, 1, 2, 3, 4);
		Vector data = vector(60000, 2 * 60000, 3 * 60000, 4 * 60000, 5 * 60000);
		
		TimeSeries res1 = UnitConverter.convertTo(new TimeSeries(time, data), Time.MILLISECONDS, Time.MINUTES);
		assertThat(res1.getData(0)).isEqualTo(vector(1, 2, 3, 4, 5), offset(1e-9));
		
		TimeSeries res2 = UnitConverter.convertTo(res1, Time.MINUTES, Time.MILLISECONDS);
		assertThat(res2.getData(0)).isEqualTo(data, offset(1e-9));
	}
	
}
