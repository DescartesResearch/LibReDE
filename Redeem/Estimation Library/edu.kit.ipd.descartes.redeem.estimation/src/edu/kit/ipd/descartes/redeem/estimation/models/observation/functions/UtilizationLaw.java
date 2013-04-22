package edu.kit.ipd.descartes.redeem.estimation.models.observation.functions;

import java.util.Arrays;

import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.repository.IMonitoringRepository;
import edu.kit.ipd.descartes.redeem.estimation.repository.Metric;
import edu.kit.ipd.descartes.redeem.estimation.repository.Query;
import edu.kit.ipd.descartes.redeem.estimation.repository.QueryBuilder;
import edu.kit.ipd.descartes.redeem.estimation.system.Resource;
import edu.kit.ipd.descartes.redeem.estimation.system.SystemModel;

/**
 * This output function implements the Utilization Law:
 * 
 * U_{i} = \sum_{r = 1}^{N} X_{r} * D_{i,r}
 * 
 * with
 * <ul>
 * 	<li>U_{i} is the utilization of resource i
 * 	<li>N is the number of services</li>
 *  <li>D_{i,r} is the resource demand of resource i and service r</li>
 *  <li>X_{r} is the throughput of service r</li>
 * </ul>
 * 
 * @author Simon Spinner (simon.spinner@kit.edu)
 * @version 1.0
 */
public class UtilizationLaw extends AbstractLinearOutputFunction {
	
	private Resource res_i;
	
	private IMonitoringRepository repository;
	
	private Query<Vector> throughputQuery;
	private Query<Scalar> utilizationQuery;
	
	public UtilizationLaw(SystemModel system, IMonitoringRepository repository,
			Resource resource) {
		super(system, Arrays.asList(resource), system.getServices());

		if (resource == null) {
			throw new IllegalArgumentException();
		}
		
		this.repository = repository;
		
		res_i = resource;
		
		throughputQuery = QueryBuilder.select(Metric.THROUGHPUT).forAllServices().average();
		utilizationQuery = QueryBuilder.select(Metric.UTILIZATION).forResource(res_i).average();
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.ILinearOutputFunction#getIndependentVariables()
	 */
	@Override
	public Vector getIndependentVariables() {
		return repository.execute(throughputQuery).getData();
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IOutputFunction#getObservedOutput()
	 */
	@Override
	public double getObservedOutput() {
		return repository.execute(utilizationQuery).getData().getValue();
	}

}
