package edu.kit.ipd.descartes.librede.estimation.models.state.constraints;

import static edu.kit.ipd.descartes.linalg.LinAlg.zeros;
import edu.kit.ipd.descartes.librede.estimation.models.diff.IDifferentiableFunction;
import edu.kit.ipd.descartes.librede.estimation.repository.Query;
import edu.kit.ipd.descartes.librede.estimation.repository.QueryBuilder;
import edu.kit.ipd.descartes.librede.estimation.repository.RepositoryCursor;
import edu.kit.ipd.descartes.librede.estimation.repository.StandardMetric;
import edu.kit.ipd.descartes.librede.estimation.workload.Resource;
import edu.kit.ipd.descartes.librede.estimation.workload.WorkloadDescription;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;

public class UtilizationConstraint implements ILinearStateConstraint, IDifferentiableFunction {

	private Resource res_i;
	
	private WorkloadDescription system;
	
	private Query<Vector> throughputQuery;
	
	public UtilizationConstraint(WorkloadDescription system, RepositoryCursor repository, Resource resource) {
		this.system = system;
		this.res_i = resource;
		
		throughputQuery = QueryBuilder.select(StandardMetric.THROUGHPUT).forAllServices().average().using(repository);
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
		Vector X = throughputQuery.execute();
		return X.dot(D_i);
	}

	@Override
	public Vector getFirstDerivatives(Vector x) {
		return throughputQuery.execute();
	}

	@Override
	public Matrix getSecondDerivatives(Vector x) {
		return zeros(x.rows(), x.rows());
	}

}
