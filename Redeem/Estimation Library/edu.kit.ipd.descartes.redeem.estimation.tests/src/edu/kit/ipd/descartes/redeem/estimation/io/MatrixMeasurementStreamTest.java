package edu.kit.ipd.descartes.redeem.estimation.io;

import static edu.kit.ipd.descartes.linalg.Matrix.matrix;
import static edu.kit.ipd.descartes.linalg.Matrix.row;
import static edu.kit.ipd.descartes.linalg.testutil.VectorAssert.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.descartes.linalg.Matrix;

public class MatrixMeasurementStreamTest {
	
	private Matrix input;
	
	private MatrixMeasurementStream stream;

	@Before
	public void setUp() throws Exception {		
		input = matrix(row(1, 1, 1), 
					row(2, 2, 2), 
					row(3, 3, 3), 
					row(4, 4, 4));
		
		stream = new MatrixMeasurementStream(input);		
	}

	
	@Test(expected=IllegalArgumentException.class)
	public void testInitializerNull() {
		new MatrixMeasurementStream(null);
	}
	
	@Test
	public void testSize() {
		assertThat(stream.size()).isEqualTo(input.columns());
	}
	
	@Test
	public void testNextSamples() {		
		for (int i = 0; i < input.rows(); i++) {
			
			assertThat(stream.getCurrentSample()).isEqualTo(input.row(i), offset(1e-5));
			assertThat(stream.getCurrentSample()).isEqualTo(input.row(i), offset(1e-5));	
			
			assertThat(stream.nextSample()).isEqualTo((i + 1) < input.rows());
		}
		
		assertThat(stream.getCurrentSample()).isNull();
	}

}
