package edu.kit.ipd.descartes.redeem.estimation.models.observation.functions;

import java.util.Arrays;

import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.redeem.estimation.repository.IMonitoringRepository;
import edu.kit.ipd.descartes.redeem.estimation.repository.Metric;
import edu.kit.ipd.descartes.redeem.estimation.repository.Query;
import edu.kit.ipd.descartes.redeem.estimation.repository.QueryBuilder;
import edu.kit.ipd.descartes.redeem.estimation.system.Service;
import edu.kit.ipd.descartes.redeem.estimation.system.SystemModel;

public class ResponseTimeApproximation extends AbstractDirectOutputFunction {
	
	private Service cls_r;
	
	private IMonitoringRepository repository;
	
	private Query<Scalar> individualResponseTimesQuery;
	
	protected ResponseTimeApproximation(SystemModel system, IMonitoringRepository repository,
			Service workloadClass) {
		super(system, system.getResources(), Arrays.asList(workloadClass));
		
		if (workloadClass == null) {
			throw new IllegalArgumentException();
		}
		
		this.repository = repository;
		
		cls_r = workloadClass;
		
		individualResponseTimesQuery = QueryBuilder.select(Metric.RESPONSE_TIME).forService(cls_r).last();
	}

	@Override
	public double getFactor() {
		return 1.0;
	}

	@Override
	public double getObservedOutput() {
		return repository.execute(individualResponseTimesQuery).getData().getValue();
	}

}
