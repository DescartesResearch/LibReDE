package edu.kit.ipd.descartes.librede.estimation.models.observation.functions;

import static edu.kit.ipd.descartes.linalg.LinAlg.zeros;

import java.util.Arrays;

import edu.kit.ipd.descartes.librede.estimation.repository.Query;
import edu.kit.ipd.descartes.librede.estimation.repository.QueryBuilder;
import edu.kit.ipd.descartes.librede.estimation.repository.RepositoryCursor;
import edu.kit.ipd.descartes.librede.estimation.repository.StandardMetric;
import edu.kit.ipd.descartes.librede.estimation.workload.Resource;
import edu.kit.ipd.descartes.librede.estimation.workload.WorkloadDescription;
import edu.kit.ipd.descartes.linalg.Range;
import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;

/**
 * This output function implements the Utilization Law:
 * 
 * U_{i} = \sum_{r = 1}^{N} X_{r} * D_{i,r}
 * 
 * with
 * <ul>
 * 	<li>U_{i} is the utilization of resource i
 * 	<li>N is the number of services</li>
 *  <li>D_{i,r} is the resource demand of resource i and service r</li>
 *  <li>X_{r} is the throughput of service r</li>
 * </ul>
 * 
 * @author Simon Spinner (simon.spinner@kit.edu)
 * @version 1.0
 */
public class UtilizationLaw extends AbstractLinearOutputFunction {
	
	private Resource res_i;
	
	private final Query<Vector> throughputQuery;
	private final Query<Scalar> utilizationQuery;
	
	private final Vector variables; // vector of independent variables which is by default set to zero. The range varFocusedRange is updated later.
	private final Range varFocusedRange; // the range of the independent variables which is altered by this output function
	
	/**
	 * Creates a new instance.
	 * 
	 * @param system - the model of the system
	 * @param repository - the repository with current measurement data
	 * @param resource - the resource for which the utilization is calculated
	 * 
	 * @throws {@link NullPointerException} if any parameter is null
	 */
	public UtilizationLaw(WorkloadDescription system, RepositoryCursor repository,
			Resource resource) {
		super(system, Arrays.asList(resource), system.getServices());
		
		this.res_i = resource;
		
		variables = zeros(system.getState().getStateSize());
		varFocusedRange = system.getState().getRange(resource);
		
		throughputQuery = QueryBuilder.select(StandardMetric.THROUGHPUT).forAllServices().average().using(repository);
		utilizationQuery = QueryBuilder.select(StandardMetric.UTILIZATION).forResource(res_i).average().using(repository);
	}
	
	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.estimation.models.observation.functions.IOutputFunction#isApplicable()
	 */
	@Override
	public boolean isApplicable() {
		return throughputQuery.hasData() && utilizationQuery.hasData();
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.estimation.models.observation.functions.ILinearOutputFunction#getIndependentVariables()
	 */
	@Override
	public Vector getIndependentVariables() {
		Vector X = throughputQuery.execute();
		return variables.set(varFocusedRange, (Vector)X.times(1 / this.res_i.getNumberOfParallelServers()));
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.estimation.models.observation.functions.IOutputFunction#getObservedOutput()
	 */
	@Override
	public double getObservedOutput() {
		return utilizationQuery.execute().getValue();
	}

}
