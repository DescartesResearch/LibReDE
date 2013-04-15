package edu.kit.ipd.descartes.redeem.estimation.repository;


public interface IMonitoringRepository {
	
	public Query.SelectClause select(Metric metric);
	
}
