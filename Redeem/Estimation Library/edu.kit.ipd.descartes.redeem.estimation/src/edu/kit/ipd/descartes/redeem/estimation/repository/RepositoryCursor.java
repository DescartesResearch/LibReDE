package edu.kit.ipd.descartes.redeem.estimation.repository;


public class RepositoryCursor {
	
	private IMonitoringRepository repository;
	private double currentTime;
	private double interval;
	
	public RepositoryCursor(IMonitoringRepository repository, double interval) {
		this.repository = repository;
		this.interval = interval;
		this.currentTime = repository.getStartTimestamp();
	}
	
	public boolean next() {
		if (Double.isNaN(currentTime)) {
			currentTime = repository.getStartTimestamp();
		}
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
	
	public IMonitoringRepository getRepository() {
		return repository;
	}
}
