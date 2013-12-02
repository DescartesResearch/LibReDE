package edu.kit.ipd.descartes.redeem.estimation.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.kit.ipd.descartes.redeem.estimation.workload.IModelEntity;
import edu.kit.ipd.descartes.redeem.estimation.workload.Resource;
import edu.kit.ipd.descartes.redeem.estimation.workload.Service;
import edu.kit.ipd.descartes.redeem.estimation.workload.WorkloadDescription;

/**
 * This class implements the IMonitoringRepository
 * 
 * @author Mehran Saliminia
 * 
 */
public class MatrixMonitoringRepository implements IMonitoringRepository {

	private Map<Metric, Map<IModelEntity, TimeSeries>> data = new HashMap<Metric, Map<IModelEntity, TimeSeries>>();
	private double minimumTimestamp;
	private double maximumTimestamp;
	private WorkloadDescription workload;
	
	public MatrixMonitoringRepository(WorkloadDescription workload) {
		this.workload = workload;
	}
	
	public TimeSeries getData(Metric m, IModelEntity entity) {
		Map<IModelEntity, TimeSeries> temp = data.get(m);
		if (temp == null) {
			return null;
		}
		return temp.get(entity);
	}
	
	public void setData(Metric m, IModelEntity entity, TimeSeries observations) {
		Map<IModelEntity, TimeSeries> temp = data.get(m);
		if (temp == null) {
			temp = new HashMap<IModelEntity, TimeSeries>();
			data.put(m, temp);
		}
		temp.put(entity, observations);
		updateMaxMinTimestamps();
	}
	
	@Override
	public List<Resource> listResources() {
		return workload.getResources();
	}
	
	@Override
	public List<Service> listServices() {
		return workload.getServices();
	}

	@Override
	public double getStartTimestamp() {
		return minimumTimestamp;
	}

	@Override
	public double getEndTimestamp() {
		return maximumTimestamp;
	}
	
	@Override
	public ObservationRepositoryView createView(int periodLength) {
		return null;
	}
	
	private void updateMaxMinTimestamps() {
		minimumTimestamp = Double.NEGATIVE_INFINITY;
		maximumTimestamp = Double.POSITIVE_INFINITY;
		for (Map<IModelEntity, TimeSeries> m : data.values()) {
			for (TimeSeries t : m.values()) {
				if (t.getStartTime() > minimumTimestamp) {
					minimumTimestamp = t.getStartTime();
				}
				if (t.getEndTime() < maximumTimestamp) {
					maximumTimestamp = t.getEndTime();
				}
			}
		}
	}

}
