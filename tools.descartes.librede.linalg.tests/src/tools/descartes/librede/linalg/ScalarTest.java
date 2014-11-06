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
package tools.descartes.librede.linalg;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static tools.descartes.librede.linalg.LinAlg.abs;
import static tools.descartes.librede.linalg.LinAlg.matrix;
import static tools.descartes.librede.linalg.LinAlg.norm1;
import static tools.descartes.librede.linalg.LinAlg.norm2;
import static tools.descartes.librede.linalg.LinAlg.ones;
import static tools.descartes.librede.linalg.LinAlg.row;
import static tools.descartes.librede.linalg.LinAlg.scalar;
import static tools.descartes.librede.linalg.LinAlg.sum;
import static tools.descartes.librede.linalg.LinAlg.vector;
import static tools.descartes.librede.linalg.testutil.MatrixAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.linalg.Vector;

public class ScalarTest {
	
	private static final double A = 1;
	private static final double B = -4;
	
	private Scalar a, b;

	@Before
	public void setUp() throws Exception {
		a = scalar(A);
		b = scalar(B);
	}
	
	@Test
	public void testCreate() {
		assertThat(a.rows()).isEqualTo(1);
		assertThat(b.rows()).isEqualTo(1);
		
		assertThat(a.columns()).isEqualTo(1);
		assertThat(b.columns()).isEqualTo(1);
		
		assertThat(a.getValue()).isEqualTo(A, offset(1e-9));
		assertThat(b.getValue()).isEqualTo(B, offset(1e-9));		
	}
	
	@Test
	public void testIsScalar() {
		Scalar s = scalar(1);
		Matrix m1 = matrix(row(A));
		Vector v = vector(A);
		Matrix m2 = matrix(row(1, 1), row(1, 1));
		
		assertThat(s.isScalar()).isTrue();
		assertThat(m1.isScalar()).isTrue();
		assertThat(v.isScalar()).isTrue();
		assertThat(m2.isScalar()).isFalse();
	}
	
	@Test
	public void testGetNormal() {
		assertThat(a.get(0)).isEqualTo(A, offset(1e-9));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetIllegal() {
		a.get(1);
	}
	
	@Test
	public void testTimes() {
		Scalar s = a.times(2);
		assertThat(s.getValue()).isEqualTo(A*2, offset(1e-9));
		assertThat(a.getValue()).isEqualTo(A, offset(1e-9));
	}
	
	@Test
	public void testScalarPlus() {
		Scalar s = a.plus(2);
		assertThat(s.getValue()).isEqualTo(A+2, offset(1e-9));
		assertThat(a.getValue()).isEqualTo(A, offset(1e-9));
	}
	
	@Test
	public void testScalarMinus() {
		Scalar s = a.minus(2);
		assertThat(s.getValue()).isEqualTo(A-2, offset(1e-9));
		assertThat(a.getValue()).isEqualTo(A, offset(1e-9));
	}
	
	@Test
	public void testSum() {
		assertThat(sum(a)).isEqualTo(A, offset(1e-9));
	}

	@Test
	public void testAbs() {
		assertThat(abs(a).getValue()).isEqualTo(A, offset(1e-9));
		assertThat(abs(b).getValue()).isEqualTo(-B, offset(1e-9));
	}
	
	@Test
	public void testNorm1() {
		assertThat(norm1(a)).isEqualTo(A, offset(1e-9));
		assertThat(norm1(b)).isEqualTo(-B, offset(1e-9));
	}
	
	@Test
	public void testNorm2() {
		assertThat(norm2(a)).isEqualTo(A*A, offset(1e-9));
		assertThat(norm2(b)).isEqualTo(B*B, offset(1e-9));
	}	

	@Test
	public void testPlusNormal() {
		assertThat(a.plus(scalar(2)).getValue()).isEqualTo(A + 2, offset(1e-9));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPlusIllegalVector() {
		a.plus(ones(3));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPlusIllegalMatrix() {
		a.plus(ones(3, 3));
	}
	
	@Test
	public void testMinusNormal() {
		assertThat(a.minus(scalar(2)).getValue()).isEqualTo(A - 2, offset(1e-9));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMinusIllegalVector() {
		a.minus(ones(3));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMinusIllegalMatrix() {
		a.minus(ones(3, 3));
	}
	
	@Test
	public void testDotNormal() {
		assertThat(a.dot(b)).isEqualTo(A * B, offset(1e-9));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testDotIllegal() {
		a.dot(ones(3));
	}
	
	@Test
	public void testToArray1D() {
		double[] arr = a.toArray1D();
		assertThat(arr.length).isEqualTo(1);
		assertThat(arr[0]).isEqualTo(A, offset(1e-9));
	}
	
	@Test
	public void testAppendColumns() {
		Matrix res = a.appendColumns(matrix(row(2, 3, 4)));
		assertThat(res).isEqualTo(matrix(row(1, 2, 3, 4)), offset(1e-9));
	}
	
	@Test
	public void testAppendRows() {
		Vector res = a.appendRows(vector(2, 3, 4));
		assertThat(res).isEqualTo(vector(1, 2, 3, 4), offset(1e-9));
	}

}
