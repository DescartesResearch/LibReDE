package edu.kit.ipd.descartes.linalg;

import static edu.kit.ipd.descartes.linalg.LinAlg.*;
import static edu.kit.ipd.descartes.linalg.testutil.MatrixAssert.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

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
