package edu.kit.ipd.descartes.redeem.estimation.repository;

import java.util.List;

import edu.kit.ipd.descartes.redeem.estimation.workload.IModelEntity;
import edu.kit.ipd.descartes.redeem.estimation.workload.Resource;
import edu.kit.ipd.descartes.redeem.estimation.workload.Service;


public interface IMonitoringRepository {

	public double getStartTimestamp();
	public double getEndTimestamp();
	public double getAggregationInterval(IMetric m, IModelEntity entity);
	public TimeSeries getData(IMetric metric, IModelEntity entity);
	public void setData(IMetric metric, IModelEntity entity, TimeSeries observations);
	public void setAggregatedData(IMetric m, IModelEntity entity, TimeSeries aggregatedObservations);
	public void setAggregatedData(IMetric m, IModelEntity entity, TimeSeries aggregatedObservations, double aggregationInterval);
	
	public List<Resource> listResources();
	public List<Service> listServices();
	
	public RepositoryCursor getCursor(int periodLength);
}
