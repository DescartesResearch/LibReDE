package edu.kit.ipd.descartes.redeem.estimation.models.observation.functions;

import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.repository.IMonitoringRepository;
import edu.kit.ipd.descartes.redeem.estimation.repository.Metric;
import edu.kit.ipd.descartes.redeem.estimation.repository.Query;
import edu.kit.ipd.descartes.redeem.estimation.repository.QueryBuilder;
import edu.kit.ipd.descartes.redeem.estimation.repository.Result;
import edu.kit.ipd.descartes.redeem.estimation.workload.Resource;
import edu.kit.ipd.descartes.redeem.estimation.workload.Service;
import edu.kit.ipd.descartes.redeem.estimation.workload.WorkloadDescription;

/**
 * This output function describes the relationship between the per-service utilization and the resource demands. 
 * It implements the Service Demand Law:
 * 
 * D_{i,r} = \frac{U_{i,r}}{X_{r}}
 * 
 * with
 * <ul>
 *  <li>D_{i,r} is the resource demand of resource i and service r</li>
 *  <li>U_{i,r} is the utilization at resource i due to service r</li>
 *  <li>X_{r} is the throughput of service r</li>
 * </ul>
 * 
 * The per-service utilization U_{i,r} is calculated from the aggregate utilization U_{i} according to the
 * following apportioning scheme:
 * 
 * U_{i,r} = \frac{U_{i} * R_{r} * X_{r}}{\sum_{v = 1}^{N} R_{v} * X_{v}}
 * 
 * with
 * <ul>
 *  <li>U_{i,r} is the utilization at resource i due to service r</li>
 *  <li>U_{i} is the aggregate utilization at resource i</li>
 *  <li>R_{r} is the average response time of service r</li>
 *  <li>X_{r} is the average throughput of service r</li>
 * </ul>
 * 
 * @author Simon Spinner (simon.spinner@kit.edu)
 * @version 1.0
 */
public class ServiceDemandLaw extends AbstractDirectOutputFunction {
	
	private Resource res_i;
	private Service cls_r;
	private int WINDOW_SIZE = 2;
	
	private IMonitoringRepository repository;
	
	private Query<Scalar> utilizationQuery;
	private Query<Vector> avgResponseTimeQuery;
	private Query<Vector> avgThroughputQuery;
	private Query<Scalar> avgThroughputQueryCurrentService;
	
	
	/**
	 * Creates a new instance.
	 * 
	 * @param system - the model of the system
	 * @param repository - the repository with current measurement data
	 * @param service - the service for which the utilization is calculated
	 * @param resource - the resource for which the utilization is calculated
	 * 
	 * @throws {@link NullPointerException} if any parameter is null
	 */
	public ServiceDemandLaw(WorkloadDescription system, IMonitoringRepository repository,
			Resource resource,
			Service service) {
		super(system, resource, service);
		
		this.repository = repository;
		
		res_i = resource;
		cls_r = service;
		
		utilizationQuery = QueryBuilder.select(Metric.UTILIZATION).forResource(res_i).average(WINDOW_SIZE);
		avgResponseTimeQuery = QueryBuilder.select(Metric.RESPONSE_TIME).forAllServices().average(WINDOW_SIZE);
		avgThroughputQuery = QueryBuilder.select(Metric.THROUGHPUT).forAllServices().average(WINDOW_SIZE);
		avgThroughputQueryCurrentService = QueryBuilder.select(Metric.THROUGHPUT).forService(service).average(WINDOW_SIZE);
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IOutputFunction#getObservedOutput()
	 */
	@Override
	public double getObservedOutput() {
		Result<Vector> avgRespTimeResult = repository.execute(avgResponseTimeQuery);
		Result<Vector> avgThroughputResult = repository.execute(avgThroughputQuery);
		
		/*
		 * We only get the aggregate utilization of a resource. In order to apportion this utilization between
		 * services, we assume R ~ D.
		 */
		Vector R = avgRespTimeResult.getData();
		Vector X = avgThroughputResult.getData();
		double R_r = R.get(avgRespTimeResult.getIndex(cls_r));
		double X_r = X.get(avgThroughputResult.getIndex(cls_r));
		double U_i = repository.execute(utilizationQuery).getData().getValue();		
		
		return U_i * (R_r * X_r) / (R.dot(X));
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IDirectOutputFunction#getFactor()
	 */
	@Override
	public double getFactor() {
		return repository.execute(avgThroughputQueryCurrentService).getData().getValue();
	}

}
