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
package tools.descartes.librede.linalg;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static tools.descartes.librede.linalg.LinAlg.det;
import static tools.descartes.librede.linalg.LinAlg.inverse;
import static tools.descartes.librede.linalg.LinAlg.matrix;
import static tools.descartes.librede.linalg.LinAlg.pow;
import static tools.descartes.librede.linalg.LinAlg.rank;
import static tools.descartes.librede.linalg.LinAlg.row;
import static tools.descartes.librede.linalg.LinAlg.trace;
import static tools.descartes.librede.linalg.testutil.MatrixAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.SquareMatrix;

public class SquareMatrixTest {
	
	private static final double[][] SQUARE = new double[][] {{1,2,0}, {2,3,0}, {3,4,1}};
	
	private SquareMatrix square;

	@Before
	public void setUp() throws Exception {
		square = (SquareMatrix)matrix(SQUARE);
	}

	@Test
	public void testDet() {
		double c = det(square);
		assertThat(c).isEqualTo(-1.0 /*Matlab: det(SQUARE)*/, offset(1e-9));
		assertThat(square).isEqualTo(matrix(SQUARE), offset(1e-9));
	}
	
	@Test
	public void testInverse() {
		Matrix c = inverse(square);
		assertThat(c).isEqualTo(matrix(
							row(-3.0, 2.0, 0),
						    row(2.0, -1.0, 0),
						    row(1.0, -2.0, 1.0)
			    		)/*Matlab: inv(SQUARE)*/, offset(1e6));
		assertThat(square).isEqualTo(matrix(SQUARE), offset(1e-9));
	}
	
	@Test
	public void testRank() {
		double c = rank(square);
		assertThat(c).isEqualTo(3 /*Matlab: rank(SQUARE)*/, offset(1e-9));
		assertThat(square).isEqualTo(matrix(SQUARE), offset(1e-9));
	}
	
	@Test
	public void testTrace() {
		double c = trace(square);
		assertThat(c).isEqualTo(5 /*Matlab: trace(SQUARE)*/, offset(1e-9));
		assertThat(square).isEqualTo(matrix(SQUARE), offset(1e-9));
	}
	
	@Test
	public void testPow() {
		Matrix c = pow(square, 3);
		assertThat(c).isEqualTo(matrix(
					 row(21, 34, 0),
					 row(34, 55, 0),
					 row(61, 98, 1)		
				) /*Matlab SQUARE^3*/, offset(1e-9));
		assertThat(square).isEqualTo(matrix(SQUARE), offset(1e-9));
	}

}
