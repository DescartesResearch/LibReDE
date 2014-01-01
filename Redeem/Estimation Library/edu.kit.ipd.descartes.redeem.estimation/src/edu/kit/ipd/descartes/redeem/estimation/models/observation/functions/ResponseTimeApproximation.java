package edu.kit.ipd.descartes.redeem.estimation.models.observation.functions;

import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.redeem.estimation.repository.StandardMetric;
import edu.kit.ipd.descartes.redeem.estimation.repository.RepositoryCursor;
import edu.kit.ipd.descartes.redeem.estimation.repository.Query;
import edu.kit.ipd.descartes.redeem.estimation.repository.QueryBuilder;
import edu.kit.ipd.descartes.redeem.estimation.workload.Resource;
import edu.kit.ipd.descartes.redeem.estimation.workload.Service;
import edu.kit.ipd.descartes.redeem.estimation.workload.WorkloadDescription;

/**
 * This output function approximates the resource demands with the observed response times of a service.
 * 
 * @author Simon Spinner (simon.spinner@kit.edu)
 * @version 1.0
 */
public class ResponseTimeApproximation extends AbstractDirectOutputFunction {
	
	private final Service cls_r;
	
	private final Query<Scalar> individualResponseTimesQuery;
	
	/**
	 * Creates a new instance.
	 * 
	 * @param system - the model of the system
	 * @param repository - the repository with current measurement data
	 * @param service - the service for which the response time is calculated
	 * @param resource - the resource for which the response time is calculated
	 * 
	 * @throws {@link NullPointerException} if any parameter is null
	 */
	public ResponseTimeApproximation(WorkloadDescription system, RepositoryCursor repository, Resource resource,
			Service service) {
		super(system, resource, service);
		
		cls_r = service;
		
		individualResponseTimesQuery = QueryBuilder.select(StandardMetric.RESPONSE_TIME).forService(cls_r).average().using(repository);
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IDirectOutputFunction#getFactor()
	 */
	@Override
	public double getFactor() {
		// approximate response times directly with resource demands --> R = 1.0 * D
		return 1.0;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IOutputFunction#getObservedOutput()
	 */
	@Override
	public double getObservedOutput() {
		return individualResponseTimesQuery.execute().getValue();
	}

}
