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

public class UtilizationLaw extends AbstractLinearOutputFunction {
	
	private Resource res_i;
	
	private IMonitoringRepository repository;
	
	Query<Vector> throughputQuery;
	Query<Scalar> utilizationQuery;
	
	protected UtilizationLaw(SystemModel system, IMonitoringRepository repository,
			Resource resource) {
		super(system, Arrays.asList(resource), system.getWorkloadClasses());

		if (resource == null) {
			throw new IllegalArgumentException();
		}
		
		this.repository = repository;
		
		res_i = resource;
		
		throughputQuery = QueryBuilder.select(Metric.THROUGHPUT).forAllServices().average();
		utilizationQuery = QueryBuilder.select(Metric.UTILIZATION).forResource(res_i).average();
	}

	@Override
	public Vector getIndependentVariables() {
		return repository.execute(throughputQuery).getData();
	}

	@Override
	public double getObservedOutput() {
		return repository.execute(utilizationQuery).getData().getValue();
	}

}
