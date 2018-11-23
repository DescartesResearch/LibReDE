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
import static tools.descartes.librede.linalg.LinAlg.abs;
import static tools.descartes.librede.linalg.LinAlg.indices;
import static tools.descartes.librede.linalg.LinAlg.matrix;
import static tools.descartes.librede.linalg.LinAlg.norm1;
import static tools.descartes.librede.linalg.LinAlg.norm2;
import static tools.descartes.librede.linalg.LinAlg.ones;
import static tools.descartes.librede.linalg.LinAlg.range;
import static tools.descartes.librede.linalg.LinAlg.row;
import static tools.descartes.librede.linalg.LinAlg.scalar;
import static tools.descartes.librede.linalg.LinAlg.sum;
import static tools.descartes.librede.linalg.LinAlg.transpose;
import static tools.descartes.librede.linalg.LinAlg.vector;
import static tools.descartes.librede.linalg.LinAlg.zeros;
import static tools.descartes.librede.linalg.testutil.MatrixAssert.assertThat;
import static tools.descartes.librede.linalg.testutil.VectorAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class VectorTest {
	
	private static final double[] A = new double[] {1, 2, 3};
	private static final double[] B = new double[] {1, 4, 9};
	
	private Vector a, b;

	@Before
	public void setUp() throws Exception {
		a = vector(A);		
		b = vector(B);
	}

	@Test
	public void testCreate() {
		assertThat(a.rows()).isEqualTo(3);
		assertThat(b.rows()).isEqualTo(3);
		
		assertThat(a.columns()).isEqualTo(1);
		assertThat(b.columns()).isEqualTo(1);
		
		assertThat(a).isEqualTo(vector(A), offset(1e-9));
		assertThat(b).isEqualTo(vector(B), offset(1e-9));		
	}
	
	@Test
	public void testZerosCreate() {
		Vector c = zeros(1);
		assertThat(c).isInstanceOf(Scalar.class);
		assertThat(((Scalar)c).getValue()).isEqualTo(0, offset(1e-9));
		
		c = zeros(3);
		assertThat(c).isInstanceOf(Vector.class);
		assertThat(c).isEqualTo(vector(0, 0, 0), offset(1e-9));
	}
	
	@Test
	public void testOnesCreate() {
		Vector c = ones(1);
		assertThat(c).isInstanceOf(Scalar.class);
		assertThat(((Scalar)c).getValue()).isEqualTo(1, offset(1e-9));
		
		c = ones(3);
		assertThat(c).isInstanceOf(Vector.class);
		assertThat(c).isEqualTo(vector(1, 1, 1), offset(1e-9));
	}
	
	@Test
	public void testGetNormal() {
		for (int i = 0; i < a.rows(); i++) {
			assertThat(a.get(i)).isEqualTo(A[i], offset(1e-9));
			assertThat(a.get(i, 0)).isEqualTo(A[i], offset(1e-9));
		}		
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetIllegalIndex1() {
		a.get(3);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetIllegalIndex2() {
		a.get(0, 1);
	}
	
	@Test
	public void testSum() {
		assertThat(sum(a).get(0)).isEqualTo(A[0] + A[1] + A[2], offset(1e-9));
		assertThat(a).isEqualTo(vector(A), offset(1e-9));
	}
	
	@Test
	public void testAbs() {
		assertThat(abs(ones(3))).isEqualTo(ones(3), offset(1e-9));
		Vector c = ones(3).times(-1.0);
		Vector res = abs(c);
		assertThat(res).isEqualTo(ones(3), offset(1e-9));
		assertThat(c).isEqualTo(ones(3).times(-1.0), offset(1e-9));
	}
	
	@Test
	public void testNorm1() {
		assertThat(norm1(a)).isEqualTo(sum(abs(a)).get(0), offset(1e-9));
		assertThat(a).isEqualTo(vector(A), offset(1e-9));
	}
	
	@Test
	public void testNorm2() {
		assertThat(norm2(a)).isEqualTo(a.dot(a), offset(1e-9));
		assertThat(a).isEqualTo(vector(A), offset(1e-9));
	}
	
	@Test
	public void testPlus() {
		Vector c = a.plus(b);
		assertThat(c).isEqualTo(vector(A[0] + B[0], A[1] + B[1], A[2] + B[2]), offset(1e-9));
		assertThat(a).isEqualTo(vector(A), offset(1e-9));
		assertThat(b).isEqualTo(vector(B), offset(1e-9));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPlusWrongDimensions() {
		Vector d = ones(2);
		a.plus(d);
	}
	
	@Test
	public void testScalarPlus() {
		Vector c = a.plus(2);
		assertThat(c.get(0)).isEqualTo(A[0]+2, offset(1e-9));
		assertThat(c.get(1)).isEqualTo(A[1]+2, offset(1e-9));
		assertThat(c.get(2)).isEqualTo(A[2]+2, offset(1e-9));
	}
	
	@Test
	public void testMinus() {
		Vector c = a.minus(b);
		assertThat(c).isEqualTo(vector(A[0] - B[0], A[1] - B[1], A[2] - B[2]), offset(1e-9));
		assertThat(a).isEqualTo(vector(A), offset(1e-9));
		assertThat(b).isEqualTo(vector(B), offset(1e-9));
		assertThat(a).isEqualTo(vector(A), offset(1e-9));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMinusWrongDimensions() {
		Vector d = ones(2);
		a.minus(d);
	}
	
	@Test
	public void testScalarMinus() {
		Vector c = a.minus(2);
		assertThat(c.get(0)).isEqualTo(A[0]-2, offset(1e-9));
		assertThat(c.get(1)).isEqualTo(A[1]-2, offset(1e-9));
		assertThat(c.get(2)).isEqualTo(A[2]-2, offset(1e-9));
		assertThat(a).isEqualTo(vector(A), offset(1e-9));
	}

	@Test
	public void testMultiply() {
		double res = a.dot(b);
		assertThat(res).isEqualTo(A[0] * B[0] + A[1] * B[1] + A[2] * B[2], offset(1e-9));
		assertThat(a).isEqualTo(vector(A), offset(1e-9));
		assertThat(b).isEqualTo(vector(B), offset(1e-9));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMultiplyWrongDimensions() {
		Vector d = ones(2);
		a.minus(d);
	}
	
	@Test
	public void testTimes() {
		Vector c = a.times(2);
		assertThat(c.get(0)).isEqualTo(A[0]*2, offset(1e-9));
		assertThat(c.get(1)).isEqualTo(A[1]*2, offset(1e-9));
		assertThat(c.get(2)).isEqualTo(A[2]*2, offset(1e-9));
		
		assertThat(a).isEqualTo(vector(A), offset(1e-9));
	}
	
	@Test
	public void testColumnNormal() {
		Vector r = a.column(0);
		assertThat(r).isEqualTo(a, offset(1e-9));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testColumnIllegal() {
		a.column(1);
	}
	
	@Test
	public void testRowNormal() {
		assertThat(a.column(0)).isEqualTo(a, offset(1e-9));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testRowIllegal() {
		a.row(3);
	}
	
	@Test
	public void testMatrixPlus() {
		Matrix m = matrix(row(1), row(1), row(1));
		Matrix c = a.plus(m);		
		assertThat(c).isEqualTo(matrix(row(A[0] + 1), row(A[1] + 1), row(A[2] + 1)), offset(1e-9));
		assertThat(a).isEqualTo(vector(A), offset(1e-9));
		assertThat(m).isEqualTo(matrix(row(1), row(1), row(1)), offset(1e-9));
	}
	
	@Test
	public void testMatrixMinus() {
		Matrix m = matrix(row(1), row(1), row(1));
		Matrix c = a.minus(m);		
		assertThat(c).isEqualTo(matrix(row(A[0] - 1), row(A[1] - 1), row(A[2] - 1)), offset(1e-9));
		assertThat(a).isEqualTo(vector(A), offset(1e-9));
		assertThat(m).isEqualTo(matrix(row(1), row(1), row(1)), offset(1e-9));
	}
	
	@Test
	public void testRow() {
		assertThat(((Scalar)a.row(1)).getValue()).isEqualTo(A[1], offset(1e-9));
	}
	
	@Test
	public void testTranspose() {
		assertThat(transpose(a)).isEqualTo(matrix(row(A)), offset(1e-9));
	}
	
	@Test
	public void testToArray2D() {
		assertThat(a.toArray2D()).isEqualTo(new double[][] { {A[0]}, {A[1]}, {A[2]}});
	}
	
	@Test
	public void testGetRowsNormal() {
		assertThat(a.get(range(0, 2))).isEqualTo(vector(A[0], A[1]), offset(1e-9));
		assertThat(a.get(range(0, 1)).isScalar()).isTrue();
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetIllegal() {
		a.get(range(0, 4));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testDotIllegal() {
		a.dot(scalar(0));
	}
	
	@Test
	public void testGetIndices() {
		Vector v = a.get(indices(1));
		assertThat(v.isScalar()).isTrue();
		assertThat(((Scalar)v).getValue()).isEqualTo(A[1], offset(1e-9));
		v = a.get(indices(0, 1));
		assertThat(v).isEqualTo(vector(A[0],  A[1]), offset(1e-9));
	}

	@Test
	public void testGetRanges() {
		Vector v = a.get(range(1, 2));
		assertThat(v.isScalar()).isTrue();
		assertThat(((Scalar)v).getValue()).isEqualTo(A[1], offset(1e-9));
		v = a.get(range(0, 2));
		assertThat(v).isEqualTo(vector(A[0],  A[1]), offset(1e-9));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetIndicesIllegal() {
		a.get(indices(10, 11));
	}
	
	@Test
	public void testCircShift() {
		Matrix c = a.circshift(1);
		assertThat(c).isEqualTo(vector(A[2], A[0], A[1]), offset(1e-9));
		
		c = a.circshift(4);
		assertThat(c).isEqualTo(vector(A[2], A[0], A[1]), offset(1e-9));
		
		c = a.circshift(2);
		assertThat(c).isEqualTo(vector(A[1], A[2], A[0]), offset(1e-9));
		
		c = a.circshift(-2);
		assertThat(c).isEqualTo(vector(A[2], A[0], A[1]), offset(1e-9));
	}
}
