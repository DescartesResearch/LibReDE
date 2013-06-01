package edu.kit.ipd.descartes.redeem.estimation.repository;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;


public interface IMonitoringRepository {
	
	public <T extends Matrix> Result<T> execute(Query<T> query);
	public boolean hasNext(Metric metric);
	public Vector next(Metric metric);
	
	
}
