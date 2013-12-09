package edu.kit.ipd.descartes.redeem.estimation.repository;

import java.util.List;

import edu.kit.ipd.descartes.redeem.estimation.workload.IModelEntity;
import edu.kit.ipd.descartes.redeem.estimation.workload.Resource;
import edu.kit.ipd.descartes.redeem.estimation.workload.Service;


public interface IMonitoringRepository {

	public double getStartTimestamp();
	public double getEndTimestamp();
	public TimeSeries getData(Metric metric, IModelEntity entity);
	public void setData(Metric metric, IModelEntity entity, TimeSeries observations);	
	
	public List<Resource> listResources();
	public List<Service> listServices();
	
	public RepositoryCursor getCursor(int periodLength);
}
