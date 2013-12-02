package edu.kit.ipd.descartes.redeem.estimation.repository;

import java.util.List;

import edu.kit.ipd.descartes.redeem.estimation.workload.IModelEntity;
import edu.kit.ipd.descartes.redeem.estimation.workload.Resource;
import edu.kit.ipd.descartes.redeem.estimation.workload.Service;

public class ObservationRepositoryView {
	
	private IMonitoringRepository repository;
	private double currentTime;
	private double interval;
	
	public ObservationRepositoryView(IMonitoringRepository repository, double interval) {
		this.repository = repository;
		this.interval = interval;
		this.currentTime = repository.getStartTimestamp();
	}
	
	public boolean next() {
		if (currentTime + interval <= repository.getEndTimestamp()) {
			currentTime += interval;
			return true;
		}
		return false;
	}
	
	public double getCurrentIntervalStart() {
		return currentTime - interval;
	}
	
	public double getCurrentIntervalEnd() {
		return currentTime;
	}
	
	public TimeSeries getData(IModelEntity entity, Metric metric) {
		return repository.getData(metric, entity);
	}
	
	public List<Resource> listResources() {
		return repository.listResources();
	}
	
	public List<Service> listServices() {
		return repository.listServices();
	}
}
