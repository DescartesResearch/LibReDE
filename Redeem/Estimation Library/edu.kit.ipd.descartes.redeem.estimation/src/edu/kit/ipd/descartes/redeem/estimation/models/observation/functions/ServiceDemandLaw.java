package edu.kit.ipd.descartes.redeem.estimation.models.observation.functions;

import java.util.Arrays;

import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.repository.IMonitoringRepository;
import edu.kit.ipd.descartes.redeem.estimation.repository.Metric;
import edu.kit.ipd.descartes.redeem.estimation.repository.Query;
import edu.kit.ipd.descartes.redeem.estimation.system.Resource;
import edu.kit.ipd.descartes.redeem.estimation.system.Service;
import edu.kit.ipd.descartes.redeem.estimation.system.SystemModel;

public class ServiceDemandLaw extends AbstractDirectOutputFunction {
	
	private Resource res_i;
	private Service cls_r;
	
	private Query<Scalar> utilizationQuery;
	private Query<Vector> avgResponseTimeQuery;
	private Query<Vector> avgThroughputQuery;
	
	public ServiceDemandLaw(SystemModel system, IMonitoringRepository repository,
			Resource resource,
			Service workloadClass) {
		super(system, Arrays.asList(resource), Arrays.asList(workloadClass));
		
		if (resource == null || workloadClass == null) {
			throw new IllegalArgumentException();
		}
		
		res_i = resource;
		cls_r = workloadClass;
		
		utilizationQuery = repository.select(Metric.UTILIZATION).forResource(res_i).average();
		avgResponseTimeQuery = repository.select(Metric.RESPONSE_TIME).forAllServices().average();
		avgThroughputQuery = repository.select(Metric.THROUGHPUT).forAllServices().average();
	}

	@Override
	public double getObservedOutput() {
		Vector R = avgResponseTimeQuery.execute();
		Vector X = avgThroughputQuery.execute();
		double R_r = R.get(avgResponseTimeQuery.getIndex(cls_r));
		double X_r = X.get(avgThroughputQuery.getIndex(cls_r));
		double U_i = utilizationQuery.execute().getValue();		
		
		return U_i * (R_r * X_r) / (R.dot(X));
	}

	@Override
	public double getFactor() {
		return avgThroughputQuery.execute().get(avgThroughputQuery.getIndex(cls_r));
	}

}
