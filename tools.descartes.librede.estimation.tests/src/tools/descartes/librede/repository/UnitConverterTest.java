package tools.descartes.librede.repository;

import static tools.descartes.librede.linalg.LinAlg.vector;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static tools.descartes.librede.linalg.testutil.VectorAssert.assertThat;

import org.junit.Test;

import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.testutils.LibredeTest;
import tools.descartes.librede.units.Time;

public class UnitConverterTest extends LibredeTest {

	@Test
	public void test() {
		Vector time = vector(0, 1, 2, 3, 4);
		Vector data = vector(60000, 2 * 60000, 3 * 60000, 4 * 60000, 5 * 60000);
		
		TimeSeries res1 = UnitConverter.convertTo(new TimeSeries(time, data), Time.MILLISECONDS, Time.MINUTES);
		assertThat(res1.getData(0)).isEqualTo(vector(1, 2, 3, 4, 5), offset(1e-9));
		
		TimeSeries res2 = UnitConverter.convertTo(res1, Time.MINUTES, Time.MILLISECONDS);
		assertThat(res2.getData(0)).isEqualTo(data, offset(1e-9));
	}
	
}
