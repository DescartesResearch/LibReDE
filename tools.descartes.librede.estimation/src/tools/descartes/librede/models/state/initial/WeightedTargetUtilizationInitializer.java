package tools.descartes.librede.models.state.initial;

import static tools.descartes.librede.linalg.LinAlg.repmat;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.repository.StandardMetric;

/**
 * This class initializes a state model with demands so that a specified target
 * utilization is reached for the throughput values observed at the system so
 * far. The resulting utilization does not need to match the actual system
 * utilization, it should be chosen so that it is a good starting point for an
 * estimation algorithm. The demands are weighted according to the response
 * times observed so far. This assumes that the response time is proportional to
 * the resource demand.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public class WeightedTargetUtilizationInitializer implements IStateInitializer {

	private final double targetUtilization;
	private Query<Vector> respTime;
	private Query<Vector> throughput;
	private final int resourceCount;

	public WeightedTargetUtilizationInitializer(int resourceCount, double targetUtilization, IRepositoryCursor cursor) {
		this.targetUtilization = targetUtilization;

		respTime = QueryBuilder.select(StandardMetric.RESPONSE_TIME).forAllServices().average().using(cursor);
		throughput = QueryBuilder.select(StandardMetric.THROUGHPUT).forAllServices().average().using(cursor);

		this.resourceCount = resourceCount;
	}

	@Override
	public Vector getInitialValue() {
		Vector initialDemands = respTime.execute();
		if (resourceCount > 1) {
			// If we have several resources, then distribute the demands evenly
			// between the resources
			initialDemands = initialDemands.times(1.0 / resourceCount);
		}

		// utilizations close to 100% or above turned out to cause convergence
		// issues with many
		// approaches that depend on it as a starting point. Therefore, we scale
		// the demands to a value
		// so that the utilization at the beginning is at a configured initial
		// point (e.g., 50%).
		double util = initialDemands.dot(throughput.execute());
		initialDemands = initialDemands.times(targetUtilization / util);

		// assume each resource has the same initial demands
		return (Vector) repmat(initialDemands, resourceCount, 1);
	}
}
