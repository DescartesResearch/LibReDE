package edu.kit.ipd.descartes.redeem.estimation.models.observation.functions;

import java.util.Arrays;

import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.repository.IMonitoringRepository;
import edu.kit.ipd.descartes.redeem.estimation.repository.Metric;
import edu.kit.ipd.descartes.redeem.estimation.repository.Query;
import edu.kit.ipd.descartes.redeem.estimation.system.Resource;
import edu.kit.ipd.descartes.redeem.estimation.system.SystemModel;

public class UtilizationLaw extends AbstractLinearOutputFunction {
	
	Resource res_i;
	
	Query<Vector> throughputQuery;
	Query<Scalar> utilizationQuery;
	
	protected UtilizationLaw(SystemModel system, IMonitoringRepository repository,
			Resource resource) {
		super(system, Arrays.asList(resource), system.getWorkloadClasses());

		if (resource == null) {
			throw new IllegalArgumentException();
		}
		
		res_i = resource;
		
		throughputQuery = repository.select(Metric.THROUGHPUT).forAllServices().average();
		utilizationQuery = repository.select(Metric.UTILIZATION).forResource(res_i).average();
	}

	@Override
	public Vector getIndependentVariables() {
		return throughputQuery.execute();
	}

	@Override
	public double getObservedOutput() {
		return utilizationQuery.execute().getValue();
	}

}
