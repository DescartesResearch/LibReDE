package edu.kit.ipd.descartes.redeem.estimation.models.state.constraints;

import static edu.kit.ipd.descartes.linalg.LinAlg.zeros;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.models.diff.IDifferentiableFunction;
import edu.kit.ipd.descartes.redeem.estimation.repository.IMonitoringRepository;
import edu.kit.ipd.descartes.redeem.estimation.repository.Metric;
import edu.kit.ipd.descartes.redeem.estimation.repository.Query;
import edu.kit.ipd.descartes.redeem.estimation.repository.QueryBuilder;
import edu.kit.ipd.descartes.redeem.estimation.system.Resource;
import edu.kit.ipd.descartes.redeem.estimation.system.SystemModel;

public class UtilizationConstraint implements ILinearStateConstraint, IDifferentiableFunction {

	private int WINDOW_SIZE = 2;
	
	private Resource res_i;
	
	private SystemModel system;
	
	private IMonitoringRepository repository;
	
	private Query<Vector> throughputQuery;
	
	public UtilizationConstraint(SystemModel system, IMonitoringRepository repository, Resource resource) {
		this.system = system;
		this.repository = repository;
		this.res_i = resource;
		
		throughputQuery = QueryBuilder.select(Metric.THROUGHPUT).forAllServices().average(WINDOW_SIZE);
	}
	
	@Override
	public double getLowerBound() {
		return 0;
	}

	@Override
	public double getUpperBound() {
		return 1;
	}

	@Override
	public double getValue(Vector state) {
		Vector D_i = state.slice(system.getState().getRange(res_i));
		Vector X = repository.execute(throughputQuery).getData();
		return X.dot(D_i);
	}

	@Override
	public Vector getFirstDerivatives(Vector x) {
		return repository.execute(throughputQuery).getData();
	}

	@Override
	public Matrix getSecondDerivatives(Vector x) {
		return zeros(x.rows(), x.rows());
	}

}
