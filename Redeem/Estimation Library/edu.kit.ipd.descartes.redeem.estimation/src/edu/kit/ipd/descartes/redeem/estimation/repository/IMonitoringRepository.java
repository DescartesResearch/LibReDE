package edu.kit.ipd.descartes.redeem.estimation.repository;

import edu.kit.ipd.descartes.linalg.Matrix;


public interface IMonitoringRepository {
	
	public <T extends Matrix> Result<T> execute(Query<T> query);
	
}
