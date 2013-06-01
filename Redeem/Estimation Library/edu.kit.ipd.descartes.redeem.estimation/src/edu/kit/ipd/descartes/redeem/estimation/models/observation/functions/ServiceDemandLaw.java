package edu.kit.ipd.descartes.redeem.estimation.models.observation.functions;

import java.util.Arrays;

import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.repository.IMonitoringRepository;
import edu.kit.ipd.descartes.redeem.estimation.repository.Metric;
import edu.kit.ipd.descartes.redeem.estimation.repository.Query;
import edu.kit.ipd.descartes.redeem.estimation.repository.QueryBuilder;
import edu.kit.ipd.descartes.redeem.estimation.repository.Result;
import edu.kit.ipd.descartes.redeem.estimation.system.Resource;
import edu.kit.ipd.descartes.redeem.estimation.system.Service;
import edu.kit.ipd.descartes.redeem.estimation.system.SystemModel;

public class ServiceDemandLaw extends AbstractDirectOutputFunction {
	
	private Resource res_i;
	private Service cls_r;
	private int WINDOW_SIZE = 2;
	
	private IMonitoringRepository repository;
	
	private Query<Scalar> utilizationQuery;
	private Query<Vector> avgResponseTimeQuery;
	private Query<Vector> avgThroughputQuery;
	private Query<Scalar> avgThroughputQueryCurrentService;
	
	
	public ServiceDemandLaw(SystemModel system, IMonitoringRepository repository,
			Resource resource,
			Service workloadClass) {
		super(system, Arrays.asList(resource), Arrays.asList(workloadClass));
		
		if (resource == null || workloadClass == null) {
			throw new IllegalArgumentException();
		}
		
		this.repository = repository;
		
		res_i = resource;
		cls_r = workloadClass;
		
		utilizationQuery = QueryBuilder.select(Metric.UTILIZATION).forResource(res_i).average(WINDOW_SIZE);
		avgResponseTimeQuery = QueryBuilder.select(Metric.RESPONSE_TIME).forAllServices().average(WINDOW_SIZE);
		avgThroughputQuery = QueryBuilder.select(Metric.THROUGHPUT).forAllServices().average(WINDOW_SIZE);
		avgThroughputQueryCurrentService = QueryBuilder.select(Metric.THROUGHPUT).forService(workloadClass).average(WINDOW_SIZE);
	}

	@Override
	public double getObservedOutput() {
		Result<Vector> avgRespTimeResult = repository.execute(avgResponseTimeQuery);
		Result<Vector> avgThroughputResult = repository.execute(avgThroughputQuery);
		
		Vector R = avgRespTimeResult.getData();
		Vector X = avgThroughputResult.getData();
		double R_r = R.get(avgRespTimeResult.getIndex(cls_r));
		double X_r = X.get(avgThroughputResult.getIndex(cls_r));
		double U_i = repository.execute(utilizationQuery).getData().getValue();		
		
		return U_i * (R_r * X_r) / (R.dot(X));
	}

	@Override
	public double getFactor() {
		return repository.execute(avgThroughputQueryCurrentService).getData().getValue();
	}

}
