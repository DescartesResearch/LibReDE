package edu.kit.ipd.descartes.linalg;

import static edu.kit.ipd.descartes.linalg.LinAlg.*;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

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
		assertThat(a.sum()).isEqualTo(A, offset(1e-9));
	}

	@Test
	public void testAbs() {
		assertThat(a.abs().getValue()).isEqualTo(A, offset(1e-9));
		assertThat(b.abs().getValue()).isEqualTo(-B, offset(1e-9));
	}
	
	@Test
	public void testToDoubleStorage() {
		DoubleStorage storageMock = mock(DoubleStorage.class);		
	
		a.toDoubleStorage(storageMock);
		
		verify(storageMock).write(aryEq(new double[] { A }));
	}
	
	@Test
	public void testNorm1() {
		assertThat(a.norm1()).isEqualTo(A, offset(1e-9));
		assertThat(b.norm1()).isEqualTo(-B, offset(1e-9));
	}
	
	@Test
	public void testNorm2() {
		assertThat(a.norm2()).isEqualTo(A*A, offset(1e-9));
		assertThat(b.norm2()).isEqualTo(B*B, offset(1e-9));
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

}
