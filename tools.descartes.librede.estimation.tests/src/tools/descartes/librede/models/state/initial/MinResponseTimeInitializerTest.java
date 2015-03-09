package tools.descartes.librede.models.state.initial;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static tools.descartes.librede.linalg.LinAlg.range;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.repository.StandardMetric;
import tools.descartes.librede.testutils.ObservationDataGenerator;

@RunWith(Parameterized.class)
public class MinResponseTimeInitializerTest {
	
	@Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {     
                 { 0.1, 0.2 }, { 0.4, 0.5 }, { 0.9, 1.0 }  
           });
    }
    
    private double lowerUtil;
    private double upperUtil;

	public MinResponseTimeInitializerTest(double lowerUtil, double upperUtil) {
		this.lowerUtil = lowerUtil;
		this.upperUtil = upperUtil;
	}

	@Test
	public void testInitializer() {
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 5, 4);
		generator.setRandomDemands();
		generator.setLowerUtilizationBound(lowerUtil);
		generator.setUpperUtilizationBound(upperUtil);
		
		IRepositoryCursor cursor = generator.getRepository().getCursor(0, 1);
		generator.nextObservation();
		cursor.next();
		
		MinResponseTimeInitializer initializer = new MinResponseTimeInitializer(cursor);
		Vector initialDemands = initializer.getInitialValue();
		
		Vector throughput = QueryBuilder.select(StandardMetric.THROUGHPUT).forAllServices().average().using(cursor).execute();
		
		for (int i = 0; i < 4; i++) {
			double util = initialDemands.slice(range(i * 5, (i + 1) * 5)).dot(throughput);
			assertThat(util).isEqualTo(0.5, offset(1e-9));
		}
	}

}
