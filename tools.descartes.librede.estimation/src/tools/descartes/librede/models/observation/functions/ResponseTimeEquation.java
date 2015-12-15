/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2014, by Simon Spinner and Contributors.
 *
 * Project Info:   http://www.descartes-research.net/
 *
 * All rights reserved. This software is made available under the terms of the
 * Eclipse Public License (EPL) v1.0 as published by the Eclipse Foundation
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse Public License (EPL)
 * for more details.
 *
 * You should have received a copy of the Eclipse Public License (EPL)
 * along with this software; if not visit http://www.eclipse.org or write to
 * Eclipse Foundation, Inc., 308 SW First Avenue, Suite 110, Portland, 97204 USA
 * Email: license (at) eclipse.org
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 */
package tools.descartes.librede.models.observation.functions;

import static tools.descartes.librede.linalg.LinAlg.matrix;
import static tools.descartes.librede.linalg.LinAlg.vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.MultivariateDifferentiableFunction;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.SchedulingStrategy;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.MatrixFunction;
import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorFunction;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.diff.IDifferentiableFunction;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.InvocationGraph;
import tools.descartes.librede.models.state.constraints.IStateConstraint;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.units.Ratio;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;

/**
 * This output function describes the relationship between the mean response
 * time and the resource demands. It implements the following equation:
 * 
 * R_{r} = \sum_{i = 1}{K} \frac{D_{i,r}}{1 - \sum_{v = 1}^{N} X_{v} * D{i,v}}
 * 
 * with
 * <ul>
 * <li>R_{r} is the response time of service r</li>
 * <li>K is the number of resources</li>
 * <li>N is the number of services</li>
 * <li>D_{i,r} is the resource demand of resource i and service r</li>
 * <li>X_{r} is the throughput of service r</li>
 * </ul>
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 * @version 1.0
 */
public class ResponseTimeEquation extends AbstractOutputFunction
		implements IDifferentiableFunction, MultivariateDifferentiableFunction {

	private final ErlangCEquation[] erlangC;

	private final Service cls_r;
	private final List<Service> usedServices;
	private final Map<Resource, List<Service>> accessedResources;

	private boolean useObservedUtilization;

	private Query<Vector, Ratio> utilQuery;
	private Query<Vector, Ratio> contentionQuery;
	private Query<Scalar, Time> responseTimeQuery;
	private Query<Vector, RequestRate> throughputQuery;	
	
	private InvocationGraph invocations;

	/**
	 * Creates a new instance.
	 * 
	 * @param stateModel
	 *            - the description of the state
	 * @param repository
	 *            - a view of the repository with current measurement data
	 * @param service
	 *            - the service for which the response time is calculated
	 * @param useObservedUtilization
	 *            - a flag whether to use observed utilization values or
	 *            calculated
	 * 
	 * @throws NullPointerException
	 *             if any parameter is null
	 * @throws IllegalArgumentException
	 *             if the list of services or resources is empty
	 */
	public ResponseTimeEquation(IStateModel<? extends IStateConstraint> stateModel, IRepositoryCursor repository,
			Service service, boolean useObservedUtilization) {
		this(stateModel, repository, service, useObservedUtilization, 0);
	}

	/**
	 * Creates a new instance.
	 * 
	 * @param stateModel
	 *            - the description of the state
	 * @param repository
	 *            - a view of the repository with current measurement data
	 * @param service
	 *            - the service for which the response time is calculated
	 * @param useObservedUtilization
	 *            - a flag whether to use observed utilization values or
	 *            calculated
	 * @param historicInterval
	 *            - specifies the number of intervals this function is behind in
	 *            the past.
	 * 
	 * @throws NullPointerException
	 *             if any parameter is null
	 * @throws IllegalArgumentException
	 *             if the list of services or resources is empty
	 */
	public ResponseTimeEquation(IStateModel<? extends IStateConstraint> stateModel, IRepositoryCursor repository,
			Service service, boolean useObservedUtilization, int historicInterval) {
		super(stateModel, historicInterval);

		cls_r = service;
		this.invocations = stateModel.getInvocationGraph();
		this.useObservedUtilization = useObservedUtilization;

		// This equation is based on the end-to-end response time
		// therefore its scope includes all directly and indirectly
		// called services.
		usedServices = new ArrayList<>(invocations.getCalledServices(cls_r));
		accessedResources = getAccessedResources(usedServices);
		Set<Resource> finiteCapacityResources = new HashSet<>();
		for (Resource curResource : accessedResources.keySet()) {
			if (curResource.getSchedulingStrategy() != SchedulingStrategy.IS) {
				finiteCapacityResources.add(curResource);
			}
		}

		if (useObservedUtilization) {
			utilQuery = QueryBuilder.select(StandardMetrics.UTILIZATION).in(Ratio.NONE)
					.forResources(finiteCapacityResources).average().using(repository);
			// The contention is the ratio of time a virtual CPU is waiting for a physical
			// CPU.
			contentionQuery = QueryBuilder.select(StandardMetrics.CONTENTION).in(Ratio.NONE)
					.forResources(finiteCapacityResources).average().using(repository);
		}

		int maxParallel = 1;
		for (Resource res : finiteCapacityResources) {
			maxParallel = Math.max(maxParallel, res.getNumberOfServers());
		}
		erlangC = new ErlangCEquation[maxParallel + 1];
		for (Resource res : finiteCapacityResources) {
			if (erlangC[res.getNumberOfServers()] == null) {
				erlangC[res.getNumberOfServers()] = new ErlangCEquation(res.getNumberOfServers());
			}
		}

		responseTimeQuery = QueryBuilder.select(StandardMetrics.RESPONSE_TIME).in(Time.SECONDS).forService(service)
				.average().using(repository);
		if (useObservedUtilization) {
			throughputQuery = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND)
					.forServices(cls_r).average().using(repository);
		} else {
			/*
			 * IMPORTANT: Query throughput for all services accessing resources in scope.
			 * When calculating the utilization, we use the utilization law. 
			 */
			Set<Service> allServicesInScope = new HashSet<>();
			allServicesInScope.add(cls_r);
			for (Resource res : finiteCapacityResources) {
				allServicesInScope.addAll(res.getAccessingServices());
			}
			throughputQuery = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND)
					.forServices(allServicesInScope).average().using(repository);
		}
	}

	/* (non-Javadoc)
	 * @see tools.descartes.librede.models.observation.functions.AbstractOutputFunction#initDataDependencies()
	 */
	@Override
	protected void initDataDependencies() {
		addDataDependency(responseTimeQuery);
		addDataDependency(throughputQuery);
		if (useObservedUtilization) {
			addDataDependency(contentionQuery);
			addDataDependency(utilQuery);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tools.descartes.librede.models.observation.functions.IOutputFunction#
	 * isApplicable()
	 */
	@Override
	public boolean isApplicable(List<String> messages) {
		boolean result = true;
		result = result && checkQueryPrecondition(responseTimeQuery, messages);
		result = result && checkQueryPrecondition(throughputQuery, messages);
		if (useObservedUtilization) {
			result = result && checkQueryPrecondition(contentionQuery, messages);
			result = result && checkQueryPrecondition(utilQuery, messages);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tools.descartes.librede.models.observation.functions.IOutputFunction#
	 * getObservedOutput()
	 */
	@Override
	public double getObservedOutput() {
		double rt = responseTimeQuery.get(historicInterval).getValue();
		return (rt != rt) ? 0.0 : rt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tools.descartes.librede.models.observation.functions.IOutputFunction#
	 * getCalculatedOutput(tools.descartes.librede.linalg.Vector)
	 */
	@Override
	public double getCalculatedOutput(Vector state) {
		double rt = 0.0;
		Vector X = throughputQuery.get(historicInterval);
		if (X.get(throughputQuery.indexOf(cls_r)) == 0.0) {
			// no request observed in this interval
			return 0.0;
		}

		for (Resource res_i : accessedResources.keySet()) {
			double C_i = 0.0;
			if (useObservedUtilization) {
				C_i = getContention(historicInterval, res_i);
			}
			double U_i = getUtilization(historicInterval, res_i, state, X, C_i);
			double busyProp = 0.0;
			if (res_i.getSchedulingStrategy() != SchedulingStrategy.IS) {
				int p = res_i.getNumberOfServers();
				busyProp = erlangC[p].calculateValue(U_i);
			}
			
			List<Service> accessingServices = accessedResources.get(res_i);
			for (Service curService : accessingServices) {
				double D_ir = state.get(getStateModel().getStateVariableIndex(res_i, curService));
				double visits = 1;
				if (!curService.equals(cls_r)) {
					visits = invocations.getInvocationCount(cls_r, curService, historicInterval);
				}
				rt += visits * (1 + C_i) * (D_ir + calculateQueueingTime(D_ir, res_i, U_i, busyProp));
			}			
		}
		return rt;
	}

	/**
	 * @param state
	 * @param res_i
	 *            - the resource i
	 * @param U_i
	 *            - the utilization of resource i
	 * @param P_q
	 *            - the probability that all servers are occupied when a new job
	 *            arrives.
	 * @param S_i
	 *            a slow down factor for the processing (e.g., CPU contention in
	 *            hypervisor)
	 * @return
	 */
	private double calculateQueueingTime(double D_ir, Resource res_i, double U_i, double P_q) {
		switch (res_i.getSchedulingStrategy()) {
		case FCFS:
			// TODO: implement FCFS
		case PS:
		case UNKOWN:
			/*
			 * The mean queue length of a single-class, multi-server queue is
			 * 
			 * E[T_q] = \frac{1}{\lambda} * \frac{U_i}{1 - U_i} * P_q
			 * 
			 * (see Harchol-Balter,
			 * "Performance Modeling and Design of Computer Systems", p. 262)
			 * 
			 * For PS scheduling (in contrast to FCFS) this also holds for
			 * multi-class queues.
			 * 
			 * This formula can be reformulated to
			 * 
			 * E[T_q] = \frac{D_ir}{1 - U_i} * P_q
			 */
			return (D_ir * P_q) / (1 - U_i);
		case IS:
			/*
			 * Infinite server: a job will never be forced to wait for service
			 */
			return 0.0;
		default:
			throw new AssertionError("Unsupported scheduling strategy.");
		}
	}
	
	private double getContention(int historicInterval, Resource res_i) {
		if (res_i.getSchedulingStrategy() == SchedulingStrategy.IS) {
			return 0.0; // there is no slow-down
		} else {
			if (contentionQuery != null) {
				return contentionQuery.get(historicInterval).get(contentionQuery.indexOf(res_i));
			} else {
				// TODO: We need a complete MVA here since we do not have resource statistics here
				return 0.0;
			}
		}
	}

	private double getUtilization(int historicInterval, Resource res_i, Vector state, Vector X, double C_i) {
		if (res_i.getSchedulingStrategy() == SchedulingStrategy.IS) {
			// the resource has infinite capacity --> utilization is always zero
			return 0.0;
		} else {
			if (useObservedUtilization) {
				return utilQuery.get(historicInterval).get(utilQuery.indexOf(res_i));
			} else {
				/*
				 * Calculate the utilization using the utilization law.
				 */
				double U_i = 0;
				for (Service curService : res_i.getAccessingServices()) {
					int idx = throughputQuery.indexOf(curService);
					U_i += state.get(getStateModel().getStateVariableIndex(res_i, curService)) * X.get(idx);
				}
				return U_i / res_i.getNumberOfServers() + C_i;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tools.descartes.librede.models.diff.IDifferentiableFunction#
	 * getFirstDerivatives(tools.descartes.librede.linalg.Vector)
	 */
	@Override
	public Vector getFirstDerivatives(final Vector state) {
		return vector(state.rows(), new VectorFunction() {
			@Override
			public double cell(int row) {
				/*
				 * This function calculates the derivatives of the response time
				 * equation i.e. cell(row = \frac{df}{dx_{row}} where x is the
				 * vector of resource demands The following indexes are used: i
				 * - index of the resource corresponding to x_{row} s - index of
				 * the service corresponding to x_{row}
				 */
				Resource res_i = getStateModel().getResourceDemand(row).getResource();
				Service cls_s = getStateModel().getResourceDemand(row).getService();

				// get current throughput data
				Vector X = throughputQuery.get(historicInterval);
				double C_i = getContention(historicInterval, res_i);
				double U_i = getUtilization(historicInterval, res_i, state, X, C_i);

				// TODO: also include invocation counts?
				double dev = 0.0;
				if (cls_r.equals(cls_s)) {
					dev += 1.0;
				}
				return (1 + C_i) * (dev + getFirstDerivationOfQueueingTime(state, res_i, cls_s, X, U_i));
			}
		});
	}

	private double getFirstDerivationOfQueueingTime(final Vector state, Resource res_i, Service cls_s, Vector X,
			double U_i) throws AssertionError {
		switch (res_i.getSchedulingStrategy()) {
		case FCFS:
			// TODO: implement FCFS
		case PS:
		case UNKOWN:
			/*
			 * When calculating derivatives, we need to distinguish between two
			 * cases: 1. We use the observed utilization -> it is a linear
			 * function with simple derivation 2. We use the utilization law to
			 * calculate the utilization -> quotient rule needs to be applied
			 */
			int k = res_i.getNumberOfServers();
			double P_q = erlangC[k].calculateValue(U_i);
			double beta = 1 - U_i;
			double dev = 0.0;

			/*
			 * Calculate: (D_ir * \frac{P_q}{1 - U_i})'
			 */
			if (cls_s.equals(cls_r)) {
				dev += P_q / beta;
			}
			if (!useObservedUtilization) {
				// In case of non-rectangular state models we may hit this case
				// Then D_ir == 0 --> we can skip this part
				if (getStateModel().containsStateVariable(res_i, cls_r)) {
					double X_s = X.get(throughputQuery.indexOf(cls_s));
					/*
					 * Important: Consider the number of servers for calculating
					 * the derivation of U
					 */
					double devU = X_s / k;
					double devP_q = erlangC[k].calculateFirstDerivative(U_i, devU);
					/*
					 * U_i and P_q are also functions of the state. Therefore,
					 * we need to apply the quotient rule. The first addend of
					 * the quotient rule has already been calculated above.
					 * 
					 * u = D_ir v = \frac{P_q}{1 - U_i}
					 * 
					 */
					double D_ir = state.get(getStateModel().getStateVariableIndex(res_i, cls_r));
					dev += D_ir * deriveQueueingFactor(beta, devU, P_q, devP_q);
				}
			}
			return dev;
		case IS:
			return 1.0;
		default:
			throw new AssertionError("Unsupported scheduling strategy.");
		}
	}

	/*
	 * Calculates: F_q = (\frac{P_q}{beta})' with beta = 1 - U_i
	 */
	private double deriveQueueingFactor(double beta, double devU, double P_q, double devP_q) {
		return (devP_q / beta + (P_q * devU) / (beta * beta));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tools.descartes.librede.models.diff.IDifferentiableFunction#
	 * getSecondDerivatives(tools.descartes.librede.linalg.Vector)
	 */
	@Override
	public Matrix getSecondDerivatives(final Vector state) {
		return matrix(state.rows(), state.rows(), new MatrixFunction() {
			@Override
			public double cell(int row, int column) {
				/*
				 * This function calculates the second partial derivative of the
				 * response time equation i.e. cell(row, column) =
				 * \frac{df}{dx_{row}dx_{column}} where x is the vector of
				 * resource demands. The following indexes are used: i - index
				 * of the resource corresponding to x_{row} s - index of the
				 * service corresponding to x_{row} j - index of the resource
				 * corresponding to x_{column} t - index of the service
				 * corresponding to x_{column}
				 */
				Resource res_i = getStateModel().getResourceDemand(row).getResource();
				Service cls_s = getStateModel().getResourceDemand(row).getService();
				Resource res_j = getStateModel().getResourceDemand(column).getResource();
				Service cls_t = getStateModel().getResourceDemand(column).getService();

				// if resources of x_{row} and x_{column} do not match the
				// second
				// derivative is zero
				// if we use observed utilization it is only a linear function
				// -> second
				// derivative is zero
				if (!useObservedUtilization && res_i.equals(res_j)) {
					return getSecondDerivationOfWaitingTime(state, res_i, cls_s, cls_t);
				}
				return 0.0;
			}

			private double getSecondDerivationOfWaitingTime(final Vector state, Resource res_i, Service cls_s,
					Service cls_t) throws AssertionError {
				/*
				 * Calculates two step derivation: d_xis * d_xit (D_ir *
				 * \frac{P_q}{1 - U_i})
				 */

				// get current throughput data
				Vector X = throughputQuery.get(historicInterval);
				double X_s = X.get(throughputQuery.indexOf(cls_s));
				double X_t = X.get(throughputQuery.indexOf(cls_t));

				double C_i = getContention(historicInterval, res_i);
				double U_i = getUtilization(historicInterval, res_i, state, X, C_i);
				int k = res_i.getNumberOfServers();

				switch (res_i.getSchedulingStrategy()) {
				case FCFS:
					// TODO: implement FCFS scheduling
				case PS:
				case UNKOWN:
					double P_q = erlangC[k].calculateValue(U_i);
					double devsU = X_s / k;
					double devtU = X_t / k;
					double devsP_q = erlangC[k].calculateFirstDerivative(U_i, devsU);
					double devtP_q = erlangC[k].calculateFirstDerivative(U_i, devtU);
					double devsdevtP_q = erlangC[k].calculateSecondDerivative(U_i, devsU);

					double beta = 1 - U_i;
					double dev = 0.0;
					if (getStateModel().containsStateVariable(res_i, cls_r)) {
						double D_ir = state.get(getStateModel().getStateVariableIndex(res_i, cls_r));
						dev = (1 + C_i) * D_ir * (devsdevtP_q / beta + (devsP_q * devtU + devtP_q * devsU) / (beta * beta)
								+ (P_q * 2 * devsU * devtU) / (beta * beta * beta));
					}
					if (cls_r.equals(cls_s)) {
						dev += deriveQueueingFactor(beta, devtU, P_q, devtP_q);
					}
					if (cls_r.equals(cls_t)) {
						dev += deriveQueueingFactor(beta, devsU, P_q, devsP_q);
					}
					return (1 + C_i) * dev;
				case IS:
					return 0.0;
				default:
					throw new AssertionError("Unsupported scheduling strategy.");
				}
			}
		});
	}

	@Override
	public double value(double[] x) {
		return getCalculatedOutput(vector(x));
	}

	@Override
	public DerivativeStructure value(DerivativeStructure[] state) {
		Vector X = throughputQuery.get(historicInterval);
		if (X.get(throughputQuery.indexOf(cls_r)) == 0.0) {
			// no request observed in this interval
			return new DerivativeStructure(state[0].getFreeParameters(), state[0].getOrder(), 0.0);
		}

		DerivativeStructure rt = null;
		for (Resource res_i : accessedResources.keySet()) {
			double C_i = getContention(historicInterval, res_i);
			DerivativeStructure U_i = getUtilization(historicInterval, res_i, state, X, C_i);
			DerivativeStructure busyProp;
			if (res_i.getSchedulingStrategy() != SchedulingStrategy.IS) {
				int p = res_i.getNumberOfServers();
				busyProp = erlangC[p].value(U_i);
			} else {
				busyProp = new DerivativeStructure(state[0].getFreeParameters(), state[0].getOrder(), 0.0);
			}

			List<Service> accessingServices = accessedResources.get(res_i);
			for (Service curService : accessingServices) {
				DerivativeStructure D_ir = state[getStateModel().getStateVariableIndex(res_i, curService)];
				double visits = 1;
				if (!curService.equals(cls_r)) {
					visits = invocations.getInvocationCount(cls_r, curService, historicInterval);
				}
				DerivativeStructure curRt = D_ir.add(calculateQueueingTime(D_ir, res_i, U_i, busyProp)).multiply(1 + C_i).multiply(visits);
				if (rt == null) {
					rt = curRt;
				} else {
					rt = rt.add(curRt);
				}
			}
		}
		return rt;
	}

	private DerivativeStructure getUtilization(int historicInterval, Resource res_i, DerivativeStructure[] state,
			Vector X, double C_i) {
		if (res_i.getSchedulingStrategy() == SchedulingStrategy.IS) {
			// the resource has infinite capacity --> utilization is always zero
			return new DerivativeStructure(state[0].getFreeParameters(), state[0].getOrder(), 0.0);
		} else {
			if (useObservedUtilization) {
				return new DerivativeStructure(state[0].getFreeParameters(), state[0].getOrder(),
						utilQuery.get(historicInterval).get(utilQuery.indexOf(res_i)));
			} else {
				/*
				 * Calculate the utilization using the utilization law.
				 */
				// sorted according to state variable ordering
				double[] tput = new double[state.length];
				for (Service curService : res_i.getAccessingServices()) {
					int idx = throughputQuery.indexOf(curService);
					tput[getStateModel().getStateVariableIndex(res_i, curService)] = X.get(idx);
				}
				// Important: if tput.length == 1, commons math crashes with a
				// ArrayIndexOutOfBoundsException
				DerivativeStructure U_i;
				if (tput.length == 1) {
					U_i = state[0].multiply(tput[0]);
				} else {
					U_i = state[0].linearCombination(tput, state);
				}
				return U_i.divide(res_i.getNumberOfServers()).add(C_i);
			}
		}
	}

	/**
	 * @param state
	 * @param res_i
	 *            - the resource i
	 * @param U_i
	 *            - the utilization of resource i
	 * @param P_q
	 *            - the probability that all servers are occupied when a new job
	 *            arrives.
	 * @return
	 */
	private DerivativeStructure calculateQueueingTime(DerivativeStructure D_ir, Resource res_i,
			DerivativeStructure U_i, DerivativeStructure P_q) {
		switch (res_i.getSchedulingStrategy()) {
		case FCFS:
			// TODO: implement FCFS
		case PS:
		case UNKOWN:
			/*
			 * The mean queue length of a single-class, multi-server queue is
			 * 
			 * E[T_q] = \frac{1}{\lambda} * \frac{U_i}{1 - U_i} * P_q
			 * 
			 * (see Harchol-Balter,
			 * "Performance Modeling and Design of Computer Systems", p. 262)
			 * 
			 * For PS scheduling (in contrast to FCFS) this also holds for
			 * multi-class queues.
			 * 
			 * This formula can be reformulated to
			 * 
			 * E[T_q] = \frac{D_ir}{1 - U_i} * P_q
			 */
			return (D_ir.multiply(P_q)).divide(U_i.multiply(-1).add(1));
		case IS:
			/*
			 * Infinite server: a job will never be forced to wait for service
			 */
			return new DerivativeStructure(D_ir.getFreeParameters(), D_ir.getOrder(), 0.0);
		default:
			throw new AssertionError("Unsupported scheduling strategy.");
		}
	}
	
	/**
	 * Collects all accessed resources of the given services.
	 * 
	 * @param services a List of Service
	 * @return a Map containing the accessing services for each resource
	 */
	private Map<Resource, List<Service>> getAccessedResources(List<Service> services) {
		Map<Resource, List<Service>> resources = new HashMap<>();
		for (Service curService : services) {
			List<Resource> accessedResources = curService.getAccessedResources();
			for (Resource curResource : accessedResources) {
				List<Service> accessingServices = resources.get(curResource);
				if (accessingServices == null) {
					accessingServices = new LinkedList<Service>();
					resources.put(curResource, accessingServices);
				}
				accessingServices.add(curService);
			}
		}
		return resources;
	}

	@Override
	public boolean hasData() {
		boolean ret = responseTimeQuery.hasData(historicInterval) && throughputQuery.hasData(historicInterval);
		if (useObservedUtilization) {
			ret = ret && utilQuery.hasData(historicInterval);
		}
		return ret;
	}
}
