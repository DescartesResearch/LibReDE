package edu.kit.ipd.descartes.librede.estimation.repository;

import java.util.UUID;

import edu.kit.ipd.descartes.librede.estimation.workload.IModelEntity;

public interface IMetric {
	
	public UUID getId();
	
	public TimeSeries retrieve(IMonitoringRepository repository, IModelEntity entity, double start, double end);

	public double aggregate(IMonitoringRepository repository, IModelEntity entity, double start, double end, Aggregation func);
	
	public boolean hasData(IMonitoringRepository repository, IModelEntity entity, double aggregationInterval);
	
	public boolean isAggregationSupported(Aggregation aggregation);
	
}
