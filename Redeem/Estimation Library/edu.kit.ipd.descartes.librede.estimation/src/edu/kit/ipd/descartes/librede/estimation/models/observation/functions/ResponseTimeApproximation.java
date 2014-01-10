package edu.kit.ipd.descartes.librede.estimation.models.observation.functions;

import edu.kit.ipd.descartes.librede.estimation.repository.Aggregation;
import edu.kit.ipd.descartes.librede.estimation.repository.Query;
import edu.kit.ipd.descartes.librede.estimation.repository.QueryBuilder;
import edu.kit.ipd.descartes.librede.estimation.repository.RepositoryCursor;
import edu.kit.ipd.descartes.librede.estimation.repository.StandardMetric;
import edu.kit.ipd.descartes.librede.estimation.workload.Resource;
import edu.kit.ipd.descartes.librede.estimation.workload.Service;
import edu.kit.ipd.descartes.librede.estimation.workload.WorkloadDescription;
import edu.kit.ipd.descartes.linalg.Scalar;

/**
 * This output function approximates the resource demands with the observed response times (min, max, or mean) of a service.
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
	 * @param system the model of the system
	 * @param repository the repository with current measurement data
	 * @param service the service for which the response time is calculated
	 * @param resource the resource for which the response time is calculated
	 * @param aggregation specifies whether average, minimum or maximum of the observed response time is used as approximation for the resource demand.
	 * 
	 * @throws {@link NullPointerException} if any parameter is null
	 */
	public ResponseTimeApproximation(WorkloadDescription system, RepositoryCursor repository, Resource resource,
			Service service, Aggregation aggregation) {
		super(system, resource, service);
		
		cls_r = service;
		
		switch(aggregation) {
		case AVERAGE:
			individualResponseTimesQuery = QueryBuilder.select(StandardMetric.RESPONSE_TIME).forService(cls_r).average().using(repository);
			break;
		case MAXIMUM:
			individualResponseTimesQuery = QueryBuilder.select(StandardMetric.RESPONSE_TIME).forService(cls_r).max().using(repository);
			break;
		case MINIMUM:
			individualResponseTimesQuery = QueryBuilder.select(StandardMetric.RESPONSE_TIME).forService(cls_r).min().using(repository);
			break;
		default:
			throw new IllegalArgumentException();
		}		
	}
	
	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.estimation.models.observation.functions.IOutputFunction#isApplicable()
	 */
	@Override
	public boolean isApplicable() {
		return individualResponseTimesQuery.hasData();
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.estimation.models.observation.functions.IDirectOutputFunction#getFactor()
	 */
	@Override
	public double getFactor() {
		// approximate response times directly with resource demands --> R = 1.0 * D
		return 1.0;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.estimation.models.observation.functions.IOutputFunction#getObservedOutput()
	 */
	@Override
	public double getObservedOutput() {
		return individualResponseTimesQuery.execute().getValue();
	}

}
