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
package edu.kit.ipd.descartes.linalg;

import static edu.kit.ipd.descartes.linalg.LinAlg.abs;
import static edu.kit.ipd.descartes.linalg.LinAlg.horzcat;
import static edu.kit.ipd.descartes.linalg.LinAlg.matrix;
import static edu.kit.ipd.descartes.linalg.LinAlg.norm1;
import static edu.kit.ipd.descartes.linalg.LinAlg.norm2;
import static edu.kit.ipd.descartes.linalg.LinAlg.ones;
import static edu.kit.ipd.descartes.linalg.LinAlg.range;
import static edu.kit.ipd.descartes.linalg.LinAlg.row;
import static edu.kit.ipd.descartes.linalg.LinAlg.scalar;
import static edu.kit.ipd.descartes.linalg.LinAlg.sum;
import static edu.kit.ipd.descartes.linalg.LinAlg.transpose;
import static edu.kit.ipd.descartes.linalg.LinAlg.vector;
import static edu.kit.ipd.descartes.linalg.LinAlg.vertcat;
import static edu.kit.ipd.descartes.linalg.LinAlg.zeros;
import static edu.kit.ipd.descartes.linalg.testutil.MatrixAssert.assertThat;
import static edu.kit.ipd.descartes.linalg.testutil.VectorAssert.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;

import org.junit.Before;
import org.junit.Test;

public class MatrixTest {
	
	private static final double[][] A = new double[][] {{1, 2, 3}, {4, 5, 6}};
	private static final double[][] B = new double[][] {{1, 4,  9}, {16, 25, 36}};
	private static final double[][] B_TRANS = new double[][] {{1, 16}, {4, 25}, {9, 36}};

	private static final double[] V = new double[] {1, 4, 9};
	
	private Matrix a, b, b_trans;
	private Vector v;

	@Before
	public void setUp() throws Exception {
		a = matrix(A);		
		b = matrix(B);		
		b_trans = matrix(B_TRANS);	
		v = vector(V);
	}

	@Test
	public void testCreate() {
		assertThat(a.rows()).isEqualTo(2);
		assertThat(b.rows()).isEqualTo(2);
		
		assertThat(a.columns()).isEqualTo(3);
		assertThat(b.columns()).isEqualTo(3);
		
		assertThat(a).isEqualTo(matrix(A), offset(1e-9));
		assertThat(b).isEqualTo(matrix(B), offset(1e-9));	
	}
	
	@Test
	public void testToArray() {
		double[] arr = a.toArray1D();
		assertThat(arr).isNotNull();
		assertThat(arr).isEqualTo(new double[] { A[0][0], A[0][1], A[0][2], A[1][0], A[1][1], A[1][2] });
	}
	
	@Test
	public void testZerosCreate() {
		Matrix c = zeros(2 ,3);
		assertThat(c).isEqualTo(matrix(row(0, 0, 0), row(0, 0, 0)), offset(1e-9));
	}
	
	@Test
	public void testOnesCreate() {
		Matrix c = ones(2 ,3);
		assertThat(c).isEqualTo(matrix(row(1, 1, 1), row(1, 1, 1)), offset(1e-9));
	}

	@Test
	public void testGetNormal() {
		for (int i = 0; i < a.rows(); i++) {
			for (int j = 0; j < a.columns(); j++) {
				assertThat(a.get(i, j)).isEqualTo(A[i][j], offset(1e-9));
			}
		}		
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetIllegalIndex() {
		a.get(3, 1);	
	}
	
	@Test
	public void testRowVector() {
		Vector r1 = a.row(0);
		Vector r2 = a.row(1);
		
		assertThat(r1).isEqualTo(vector(A[0]), offset(1e-9));
		assertThat(r2).isEqualTo(vector(A[1]), offset(1e-9));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testRowVectorIllegalIndex() {
		a.row(3);
	}

	@Test
	public void testColumnVector() {
		Vector c1 = a.column(0);
		Vector c2 = a.column(1);
		Vector c3 = a.column(2);
		
		assertThat(c1).isEqualTo(vector(A[0][0], A[1][0]), offset(1e-9));
		assertThat(c2).isEqualTo(vector(A[0][1], A[1][1]), offset(1e-9));
		assertThat(c3).isEqualTo(vector(A[0][2], A[1][2]), offset(1e-9));
		
		Vector scalar1 = matrix(row(1, 2, 3)).column(0);
		assertThat(scalar1.isScalar()).isTrue();
		assertThat(scalar1).isEqualTo(scalar(1), offset(1e-9));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testColumnVectorIllegalIndex() {
		a.column(4);
	}

	@Test
	public void testPlus() {
		Matrix c = a.plus(b);		
		assertThat(c).isEqualTo(matrix(
				row(A[0][0] + B[0][0], A[0][1] + B[0][1], A[0][2] + B[0][2]),
				row(A[1][0] + B[1][0], A[1][1] + B[1][1], A[1][2] + B[1][2])
				), offset(1e-9));
		assertThat(a).isEqualTo(matrix(A), offset(1e-9));
		assertThat(b).isEqualTo(matrix(B), offset(1e-9));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPlusWrongDimensions() {
		Matrix d = ones(3, 3);
		a.plus(d);
	}
	
	@Test
	public void testScalarPlus() {
		Matrix c = a.plus(2);		
		assertThat(c).isEqualTo(matrix(
				row(A[0][0] + 2, A[0][1] + 2, A[0][2] + 2),
				row(A[1][0] + 2, A[1][1] + 2, A[1][2] + 2)
				), offset(1e-9));
		assertThat(a).isEqualTo(matrix(A), offset(1e-9));
	}

	@Test
	public void testMinus() {
		Matrix c = a.minus(b);		
		assertThat(c).isEqualTo(matrix(
				row(A[0][0] - B[0][0], A[0][1] - B[0][1], A[0][2] - B[0][2]),
				row(A[1][0] - B[1][0], A[1][1] - B[1][1], A[1][2] - B[1][2])
				), offset(1e-9));
		assertThat(a).isEqualTo(matrix(A), offset(1e-9));
		assertThat(b).isEqualTo(matrix(B), offset(1e-9));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMinusWrongDimensions() {
		Matrix d = ones(3, 3);
		a.minus(d);
	}
	
	@Test
	public void testScalarMinus() {
		Matrix c = a.minus(2);		
		assertThat(c).isEqualTo(matrix(
				row(A[0][0] - 2, A[0][1] - 2, A[0][2] - 2),
				row(A[1][0] - 2, A[1][1] - 2, A[1][2] - 2)
				), offset(1e-9));
		assertThat(a).isEqualTo(matrix(A), offset(1e-9));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testMultiplyVectorWrongDimensions() {
		Vector d = ones(2);
		a.multipliedBy(d);
	}

	@Test
	public void testMultiplyMatrix() {
		Matrix c = a.multipliedBy(b_trans);		
		assertThat(c).isEqualTo(matrix(
				row(A[0][0] * B_TRANS[0][0] + A[0][1] * B_TRANS[1][0] + A[0][2] * B_TRANS[2][0], A[0][0] * B_TRANS[0][1] + A[0][1] * B_TRANS[1][1] + A[0][2] * B_TRANS[2][1]),
				row(A[1][0] * B_TRANS[0][0] + A[1][1] * B_TRANS[1][0] + A[1][2] * B_TRANS[2][0], A[1][0] * B_TRANS[0][1] + A[1][1] * B_TRANS[1][1] + A[1][2] * B_TRANS[2][1])
				), offset(1e-9));
		assertThat(a).isEqualTo(matrix(A), offset(1e-9));
		assertThat(b_trans).isEqualTo(matrix(B_TRANS), offset(1e-9));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMultiplyMatrixWrongDimensions() {
		Matrix d = ones(2, 3);
		a.multipliedBy(d);
	}
	
	@Test
	public void testTimes() {
		Matrix c = a.times(2);		
		assertThat(c).isEqualTo(matrix(
				row(A[0][0] * 2, A[0][1] * 2, A[0][2] * 2),
				row(A[1][0] * 2, A[1][1] * 2, A[1][2] * 2)
				), offset(1e-9));
		assertThat(a).isEqualTo(matrix(A), offset(1e-9));
	}
	
	@Test
	public void testAbs() {
		assertThat(abs(ones(3,3))).isEqualTo(ones(3,3), offset(1e-9));
		
		Matrix c = ones(3,3).times(-1.0);
		Matrix res = abs(c);
		assertThat(res).isEqualTo(ones(3, 3), offset(1e-9));
		assertThat(c).isEqualTo(ones(3, 3).times(-1.0), offset(1e-9));
	}
	
	@Test
	public void testSum() {
		double c = sum(a);
		assertThat(c).isEqualTo(A[0][0] + A[0][1] + A[0][2] + A[1][0] + A[1][1] + A[1][2], offset(1e-9));
		assertThat(a).isEqualTo(matrix(A), offset(1e-9));
	}
	
	@Test
	public void testNorm1() {
		double c = norm1(a);
		assertThat(c).isEqualTo(9 /*Matlab: norm(A,1)*/, offset(1e-9));
		assertThat(a).isEqualTo(matrix(A), offset(1e-9));
	}
	
	@Test
	public void testNorm2() {
		double c = norm2(b_trans);
		assertThat(c).isEqualTo(47.6055361304348 /*Matlab: norm(B_TRANS,2)*/, offset(1e-9));
		assertThat(b_trans).isEqualTo(matrix(B_TRANS), offset(1e-9));
	}
	
	
	@Test
	public void testTranspose() {
		Matrix c = transpose(b);
		assertThat(c).isEqualTo(b_trans, offset(1e-9));
		assertThat(b).isEqualTo(matrix(B), offset(1e-9));
	}
	
	@Test
	public void testHorzCat() {
		Matrix c = horzcat(a, b, v.slice(range(0,2)));
		assertThat(c).isEqualTo(
						matrix(row(A[0][0], A[0][1], A[0][2], B[0][0], B[0][1], B[0][2], V[0]), 
						row(A[1][0], A[1][1], A[1][2], B[1][0], B[1][1], B[1][2], V[1])), 
						offset(1e-9)
								);
		
		c = horzcat(v.slice(range(0,2)), a, b);
		assertThat(c).isEqualTo(
						matrix(
								row(V[0], A[0][0], A[0][1], A[0][2], B[0][0], B[0][1], B[0][2]), 
								row(V[1], A[1][0], A[1][1], A[1][2], B[1][0], B[1][1], B[1][2])), 
								offset(1e-9)
								);
		
	}
	
	@Test
	public void testVertCat() {
		Matrix c = vertcat(a, b, transpose(v));
		assertThat(c).isEqualTo(
						matrix(
							row(A[0][0], A[0][1], A[0][2]), 
							row(A[1][0], A[1][1], A[1][2]),
							row(B[0][0], B[0][1], B[0][2]), 
							row(B[1][0], B[1][1], B[1][2]),
							row(V[0], V[1], V[2])
						), offset(1e-9));
		
		c = vertcat(transpose(v), a, b);
		assertThat(c).isEqualTo(
						matrix(
							row(V[0], V[1], V[2]),
							row(A[0][0], A[0][1], A[0][2]), 
							row(A[1][0], A[1][1], A[1][2]),
							row(B[0][0], B[0][1], B[0][2]), 
							row(B[1][0], B[1][1], B[1][2])
						), offset(1e-9));
	}
	
	@Test
	public void testCircShift() {
		Matrix c = a.circshift(1);
		assertThat(c).isEqualTo(
						matrix(
							row(A[1][0], A[1][1], A[1][2]),
							row(A[0][0], A[0][1], A[0][2])
						), offset(1e-9));
		
		c = a.circshift(2);
		assertThat(c).isEqualTo(
						matrix(
							row(A[0][0], A[0][1], A[0][2]),
							row(A[1][0], A[1][1], A[1][2])
						), offset(1e-9));
		c = transpose(a).circshift(2);
		assertThat(c).isEqualTo(
						matrix(
							row(A[0][1], A[1][1]), 
							row(A[0][2], A[1][2]),
							row(A[0][0], A[1][0])
						), offset(1e-9));		
		c = transpose(a).circshift(-2);
		assertThat(c).isEqualTo(
						matrix(							 
							row(A[0][2], A[1][2]),
							row(A[0][0], A[1][0]),
							row(A[0][1], A[1][1])
						), offset(1e-9));
	}
}
