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
package tools.descartes.librede.models.observation.functions.helper;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.configuration.SchedulingStrategy;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.AbstractDependencyTarget;
import tools.descartes.librede.models.State;
import tools.descartes.librede.models.observation.functions.ErlangCEquation;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.units.RequestCount;

/**
 * This is a helper function for calculating the waiting time of a newly
 * arriving request at a specific resource.
 * 
 * This is an abstract class since there are different variants depending on the
 * number of workload classes and the scheduling strategy. Use the function
 * {@code WaitingTimeEquation#create(IRepositoryCursor, Resource, int)} to
 * create a new instance.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public abstract class WaitingTimeEquation extends AbstractDependencyTarget {

	protected final Resource res_i;
	protected final ErlangCEquation erlangC;
	protected final int historicInterval;
	protected final UtilizationFunction utilization;

	/**
	 * Use {@code WaitingTimeEquation#create(IRepositoryCursor, Resource, int)}
	 * instead.
	 * 
	 * @param cursor
	 * @param resource
	 * @param historicInterval
	 * @param utilization
	 */
	private WaitingTimeEquation(IRepositoryCursor cursor, Resource resource, int historicInterval,
			UtilizationFunction utilization) {
		this.res_i = resource;
		this.historicInterval = historicInterval;
		this.erlangC = new ErlangCEquation(resource.getNumberOfServers());
		this.utilization = utilization;

		addDataDependencies(this.erlangC);
		addDataDependencies(this.utilization);
	}

	/**
	 * Creates a new instance
	 * 
	 * @param cursor
	 *            the position in the monitoring data
	 * @param resource
	 *            the resource at which the waiting takes place
	 * @param historicInterval
	 *            the monitoring interval for which to calculate the waiting
	 *            time (0 == current interval)
	 * @param utilization
	 *            a {@code UtilizationFunction} instance to calculate the
	 *            utilization of the resource.
	 * @return a new {@code WaitingTimeEquation} instance
	 */
	public static WaitingTimeEquation create(IRepositoryCursor cursor, Resource resource, int historicInterval,
			UtilizationFunction utilization) {
		if (isProductForm(resource)) {
			return new WaitingTimeEquationProductForm(cursor, resource, historicInterval, utilization);
		} else {
			if (resource.getSchedulingStrategy() == SchedulingStrategy.FCFS) {
				return new WaitingTimeEquationMultiClassFCFS(cursor, resource, historicInterval, utilization);
			}
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Calculates the average waiting time of a service at the resource.
	 * 
	 * @param service
	 *            the service for which to determine the waiting time
	 * @param state
	 *            the current state of the system
	 * @return a {@code DerivativeStructure} containing the waiting time
	 */
	public abstract DerivativeStructure getAverageWaitingTime(Service service, State state);

	/**
	 * 
	 * @param resource
	 * @return a boolean indicating whether the resource fulfills the
	 *         product-form assumption.
	 */
	private static boolean isProductForm(Resource resource) {
		return (resource.getAccessingServices().size() == 1)
				|| (resource.getSchedulingStrategy() != SchedulingStrategy.FCFS);
	}

	/**
	 * This implementation assumes that the residence time can be calculated
	 * without information on the queue lengths of the individual workload
	 * classes. This assumption holds only for the scheduling strategies
	 * specified in the BCMP theorem.
	 * 
	 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
	 *
	 */
	private static class WaitingTimeEquationProductForm extends WaitingTimeEquation {

		/**
		 * Constructor.
		 * 
		 * @param cursor
		 * @param resource
		 * @param historicInterval
		 * @param utilization
		 */
		public WaitingTimeEquationProductForm(IRepositoryCursor cursor, Resource resource, int historicInterval,
				UtilizationFunction utilization) {
			super(cursor, resource, historicInterval, utilization);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see tools.descartes.librede.models.observation.functions.helper.
		 * WaitingTimeEquation#getAverageWaitingTime(tools.descartes.librede.
		 * configuration.Service, tools.descartes.librede.models.State)
		 */
		@Override
		public DerivativeStructure getAverageWaitingTime(Service service, State state) {
			switch (res_i.getSchedulingStrategy()) {
			case FCFS:
			case PS:
			case UNKOWN:
				/*
				 * The mean queue length of a single-class, multi-server queue
				 * is
				 * 
				 * E[T_q] = \frac{1}{\lambda} * \frac{U_i}{1 - U_i} * P_q
				 * 
				 * (see Harchol-Balter,
				 * "Performance Modeling and Design of Computer Systems", p.
				 * 262)
				 * 
				 * For PS scheduling (in contrast to FCFS) this also holds for
				 * multi-class queues.
				 * 
				 * This formula can be reformulated to
				 * 
				 * E[T_q] = \frac{D_ir}{1 - U_i} * P_q
				 */
				DerivativeStructure D_ir = state.getVariable(res_i, service).getDerivativeStructure();
				DerivativeStructure U_i = utilization.getUtilization(state);
				DerivativeStructure P_q = erlangC.value(U_i);
				return (D_ir.multiply(P_q)).divide(U_i.multiply(-1).add(1));
			case IS:
				/*
				 * Infinite server: a job will never be forced to wait for
				 * service
				 */
				return new DerivativeStructure(state.getStateSize(), state.getDerivationOrder(), 0.0);
			default:
				throw new AssertionError("Unsupported scheduling strategy.");
			}
		}
	}

	/**
	 * This implementation use information on the queue length of individual
	 * workload classes to calculate the waiting time for multi-class FCFS
	 * queues.
	 * 
	 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
	 *
	 */
	private static class WaitingTimeEquationMultiClassFCFS extends WaitingTimeEquation {

		private Query<Vector, RequestCount> queueLengthQuery;

		/**
		 * Constructor.
		 * 
		 * @param cursor
		 * @param resource
		 * @param historicInterval
		 * @param utilization
		 */
		public WaitingTimeEquationMultiClassFCFS(IRepositoryCursor cursor, Resource resource, int historicInterval,
				UtilizationFunction utilization) {
			super(cursor, resource, historicInterval, utilization);
			queueLengthQuery = QueryBuilder.select(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL)
					.in(RequestCount.REQUESTS).forResourceDemands(resource.getDemands()).average().using(cursor);
			addDataDependency(queueLengthQuery);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see tools.descartes.librede.models.observation.functions.helper.
		 * WaitingTimeEquation#getAverageWaitingTime(tools.descartes.librede.
		 * configuration.Service, tools.descartes.librede.models.State)
		 */
		@Override
		public DerivativeStructure getAverageWaitingTime(Service service, State state) {
			Vector Q_ir = queueLengthQuery.get(historicInterval);
			/*
			 * The mean queue length depends on the current queue length of all
			 * classes. Therefore, we calculate the queueing time of all
			 * requests before the newly arrived request.
			 */
			DerivativeStructure T_q = new DerivativeStructure(state.getStateSize(), state.getDerivationOrder(), 0.0);
			for (int i = 0; i < Q_ir.rows(); i++) {
				ResourceDemand curDemand = (ResourceDemand) queueLengthQuery.getEntity(i);
				DerivativeStructure D_ir = state.getVariable(res_i, curDemand.getService()).getDerivativeStructure();
				T_q = T_q.add(D_ir.multiply(Q_ir.get(i)));
			}
			DerivativeStructure U_i = utilization.getUtilization(state);
			DerivativeStructure P_q = erlangC.value(U_i);
			/*
			 * We need to wait only in cases where all servers are busy.
			 * Therefore we multiply the waiting time with the busy probability
			 * of the resource.
			 */
			return T_q.multiply(P_q);
		}
	}
}
