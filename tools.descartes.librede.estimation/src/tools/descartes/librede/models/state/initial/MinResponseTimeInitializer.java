package tools.descartes.librede.models.state.initial;

import static tools.descartes.librede.linalg.LinAlg.repmat;

import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.repository.StandardMetric;

public class MinResponseTimeInitializer implements IStateInitializer {
	
	private static final double TARGET_INITIAL_UTILIZATION = 0.5;
	
	private Query<Vector> respTime;
	private Query<Vector> throughput;
	private int resourceCount;
	
	public MinResponseTimeInitializer(IRepositoryCursor cursor) {	
		respTime = QueryBuilder.select(StandardMetric.RESPONSE_TIME).forAllServices().min().using(cursor);
		throughput = QueryBuilder.select(StandardMetric.THROUGHPUT).forAllServices().average().using(cursor);
		
		resourceCount = cursor.getRepository().listResources().size();
	}

	@Override
	public Vector getInitialValue() {
		Vector initialDemands = respTime.execute();
		if (resourceCount > 1) {
			// If we have several resources, then distribute the demands evenly between the resources
			initialDemands = initialDemands.times(1.0 / resourceCount);
		}
		
		// utilizations close to 100% or above turned out to cause convergence issues with many
		// approaches that depend on it as a starting point. Therefore, we scale the demands to a value
		// so that the utilization at the beginning is at a configured initial point (e.g., 50%).
		double util = initialDemands.dot(throughput.execute());
		initialDemands = initialDemands.times(TARGET_INITIAL_UTILIZATION / util);
		
		// assume each resource has the same initial demands
		return (Vector) repmat(initialDemands, resourceCount, 1);
	}
}
