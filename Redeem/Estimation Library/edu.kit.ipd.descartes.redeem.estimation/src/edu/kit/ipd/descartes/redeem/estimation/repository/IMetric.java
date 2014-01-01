package edu.kit.ipd.descartes.redeem.estimation.repository;

import java.util.UUID;

import edu.kit.ipd.descartes.redeem.estimation.workload.IModelEntity;

public interface IMetric {
	
	public UUID getId();
	
	public TimeSeries retrieve(IMonitoringRepository repository, IModelEntity entity, double start, double end);

	public double aggregate(TimeSeries series, Aggregation func);

}
