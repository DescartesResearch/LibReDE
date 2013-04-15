package edu.kit.ipd.descartes.redeem.estimation.io;

import static edu.kit.ipd.descartes.linalg.LinAlg.*;
import static edu.kit.ipd.descartes.linalg.testutil.VectorAssert.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.redeem.estimation.repository.MatrixMonitoringRepository;

public class MatrixMeasurementStreamTest {
	
	private Matrix input;
	
	private MatrixMonitoringRepository stream;

	@Before
	public void setUp() throws Exception {		
//		input = matrix(row(1, 1, 1), 
//					row(2, 2, 2), 
//					row(3, 3, 3), 
//					row(4, 4, 4));
//		
//		stream = new MatrixMonitoringRepository(input);		
	}

	
	@Test(expected=IllegalArgumentException.class)
	public void testInitializerNull() {
//		new MatrixMonitoringRepository(null);
	}
	
	@Test
	public void testSize() {
//		assertThat(stream.size()).isEqualTo(input.columns());
	}
	
	@Test
	public void testNextSamples() {		
//		for (int i = 0; i < input.rows(); i++) {
//			
//			assertThat(stream.getCurrentSample()).isEqualTo(input.row(i), offset(1e-5));
//			assertThat(stream.getCurrentSample()).isEqualTo(input.row(i), offset(1e-5));	
//			
//			assertThat(stream.nextSample()).isEqualTo((i + 1) < input.rows());
//		}
//		
//		assertThat(stream.getCurrentSample()).isNull();
	}

}
