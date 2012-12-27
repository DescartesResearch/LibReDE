package edu.kit.ipd.descartes.linalg;

import static edu.kit.ipd.descartes.linalg.Vector.*;
import static edu.kit.ipd.descartes.linalg.VectorAssert.assertThat;
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
		
		assertThat(a).isEqualTo(vector(A), offset(1e-9));
		assertThat(b).isEqualTo(vector(B), offset(1e-9));		
	}
	
	@Test
	public void testCreateFromStorage() {		
		DoubleStorage storageMock = mock(DoubleStorage.class);
		
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				double[] elements = (double[])invocation.getArguments()[0];
				System.arraycopy(B, 0, elements, 0, 3);
				return null;
			}			
		}).when(storageMock).read(any(double[].class));
		
		Vector c = vector(3, storageMock);
		
		assertThat(c).isEqualTo(vector(B), offset(1e-9));
	}
	
	@Test
	public void testZerosCreate() {
		Vector c = zeros(3);
		assertThat(c).isEqualTo(vector(0, 0, 0), offset(1e-9));
	}
	
	@Test
	public void testOnesCreate() {
		Vector c = ones(3);
		assertThat(c).isEqualTo(vector(1, 1, 1), offset(1e-9));
	}
	
	@Test
	public void testGetNormal() {
		for (int i = 0; i < a.rows(); i++) {
			assertThat(a.get(i)).isEqualTo(A[i], offset(1e-9));
		}		
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetIllegalIndex() {
		a.get(3);	
	}
	
	@Test
	public void testSum() {
		assertThat(a.sum()).isEqualTo(A[0] + A[1] + A[2], offset(1e-9));
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
		assertThat(norm1(a)).isEqualTo(sum(abs(a)), offset(1e-9));
		assertThat(a).isEqualTo(vector(A), offset(1e-9));
	}
	
	@Test
	public void testNorm2() {
		assertThat(norm2(a)).isEqualTo(a.multipliedBy(a), offset(1e-9));
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
		double res = a.multipliedBy(b);
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
	public void testToDoubleStorage() {
		DoubleStorage storageMock = mock(DoubleStorage.class);		
	
		a.toDoubleStorage(storageMock);
		
		verify(storageMock).write(aryEq(A));
	}
	
	

}
