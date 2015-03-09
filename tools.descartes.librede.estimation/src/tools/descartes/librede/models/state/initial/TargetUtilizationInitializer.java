package tools.descartes.librede.models.state.initial;

import static tools.descartes.librede.linalg.LinAlg.sum;
import static tools.descartes.librede.linalg.LinAlg.vector;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorFunction;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.repository.StandardMetric;

/**
 * This class initializes a state model with demands so that a specified target
 * utilization is reached for the throughput values observed at the system so
 * far. The resulting utilization does not need to match the actual system
 * utilization, it should be chosen so that it is a good starting point for an
 * estimation algorithm.
 * 
 * NOTE: The initial demands for all services will be equal.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public class TargetUtilizationInitializer implements IStateInitializer {

	private Query<Vector> throughput;
	private final double targetUtilization;
	private final int stateSize;

	/**
	 * @param stateSize the state size of the state model
	 * @param targetUtilization the requested initial target utilization
	 * @param cursor handle to the monitoring data
	 */
	public TargetUtilizationInitializer(int stateSize, double targetUtilization, IRepositoryCursor cursor) {
		this.stateSize = stateSize;
		this.targetUtilization = targetUtilization;

		throughput = QueryBuilder.select(StandardMetric.THROUGHPUT).forAllServices().average().using(cursor);
	}

	@Override
	public Vector getInitialValue() {
		double totalThroughput = sum(throughput.execute());
		final double initialDemand = targetUtilization / totalThroughput;
		return vector(stateSize, new VectorFunction() {
			@Override
			public double cell(int row) {
				return initialDemand;
			}
		});
	}

}
