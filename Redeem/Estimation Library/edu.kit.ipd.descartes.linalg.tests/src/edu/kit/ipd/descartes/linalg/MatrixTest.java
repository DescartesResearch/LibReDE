package edu.kit.ipd.descartes.linalg;

import static edu.kit.ipd.descartes.linalg.Matrix.*;
import static edu.kit.ipd.descartes.linalg.Vector.ones;
import static edu.kit.ipd.descartes.linalg.Vector.vector;
import static edu.kit.ipd.descartes.linalg.Vector.zeros;
import static edu.kit.ipd.descartes.linalg.testutil.MatrixAssert.assertThat;
import static edu.kit.ipd.descartes.linalg.testutil.VectorAssert.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public class MatrixTest {
	
	private static final double[][] A = new double[][] {{1, 2, 3}, {4, 5, 6}};
	private static final double[][] B = new double[][] {{1, 4,  9}, {16, 25, 36}};
	private static final double[][] B_TRANS = new double[][] {{1, 16}, {4, 25}, {9, 36}};
	private static final double[][] SQUARE = new double[][] {{1,2,0}, {2,3,0}, {3,4,1}};
	private static final double[] V = new double[] {1, 4, 9};
	
	private Matrix a, b, b_trans, square;
	private Vector v;

	@Before
	public void setUp() throws Exception {
		a = matrix(A);		
		b = matrix(B);		
		b_trans = matrix(B_TRANS);	
		square = matrix(SQUARE);
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
	public void testZerosCreate() {
		Matrix c = zeros(2 ,3);
		assertThat(c).isEqualTo(matrix(row(0, 0, 0), row(0, 0, 0)), offset(1e-9));
	}
	
	@Test
	public void testAppendRow() {
		Matrix a=Matrix.matrix(A);
		int rows=a.rows();
		double[] row={9,23,4};
		Vector v=Vector.vector(row);
		a.appendRow(v);
		assertThat(v).isEqualTo(vector(a.row(rows)), offset(1e-9));
	}
	
	@Test
	public void testOnesCreate() {
		Matrix c = ones(2 ,3);
		assertThat(c).isEqualTo(matrix(row(1, 1, 1), row(1, 1, 1)), offset(1e-9));
	}
	
	@Test
	public void testCreateFromStorage() {
		DoubleStorage storageMock = mock(DoubleStorage.class);
		
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				double[] elements = (double[])invocation.getArguments()[0];
				System.arraycopy(B[0], 0, elements, 0, 3);
				System.arraycopy(B[1], 0, elements, 3, 3);
				return null;
			}			
		}).when(storageMock).read(any(double[].class));
		
		Matrix c = matrix(2, 3, storageMock);
		
		assertThat(c).isEqualTo(matrix(B), offset(1e-9));
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
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testColumnVectorIllegalIndex() {
		a.column(4);
	}

	@Test
	public void testWriteTo() {
		DoubleStorage storageMock = mock(DoubleStorage.class);		
		
		a.toDoubleStorage(storageMock);
		
		verify(storageMock).write(aryEq(new double[] {A[0][0], A[0][1], A[0][2], A[1][0], A[1][1], A[1][2]}));
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

	@Test
	public void testMultiplyVector() {
		Vector c = a.multipliedBy(v);
		assertThat(c).isEqualTo(vector(A[0][0] * V[0] + A[0][1] * V[1] + A[0][2] * V[2], 
									A[1][0] * V[0] + A[1][1] * V[1] + A[1][2] * V[2]
								), offset(1e-9));
		assertThat(a).isEqualTo(matrix(A), offset(1e-9));
		assertThat(v).isEqualTo(vector(V), offset(1e-9));
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
	public void testTranspose() {
		Matrix c = transpose(b);
		assertThat(c).isEqualTo(b_trans, offset(1e-9));
		assertThat(b).isEqualTo(matrix(B), offset(1e-9));
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
