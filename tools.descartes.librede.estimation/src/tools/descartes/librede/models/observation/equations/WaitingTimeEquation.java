/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2018, by Simon Spinner, Johannes Grohmann
 *  and Contributors.
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
package tools.descartes.librede.models.observation.equations;

import static tools.descartes.librede.linalg.LinAlg.vector;
import static tools.descartes.librede.linalg.LinAlg.zeros;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.configuration.SchedulingStrategy;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.State;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.IStateConstraint;
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
public abstract class WaitingTimeEquation extends ModelEquation {

	protected final Service cls_r;
	protected final Resource res_i;
	protected final int historicInterval;

	/**
	 * Use {@code WaitingTimeEquation#create(IRepositoryCursor, Resource, int)}
	 * instead.
	 * 
	 * @param cursor
	 * @param resource
	 * @param historicInterval
	 * @param utilization
	 */
	private WaitingTimeEquation(IStateModel<? extends IStateConstraint> stateModel, IRepositoryCursor cursor,
			Service service, Resource resource, int historicInterval) {
		super(stateModel, historicInterval);
		this.cls_r = service;
		this.res_i = resource;
		this.historicInterval = historicInterval;
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
	 *            a {@code LinearModelEquation} instance to calculate the
	 *            utilization of the resource.
	 * @return a new {@code WaitingTimeEquation} instance
	 */
	public static WaitingTimeEquation create(IStateModel<? extends IStateConstraint> stateModel,
			IRepositoryCursor cursor, Service service, Resource resource, int historicInterval, boolean useObservations) {
		if (isProductForm(resource)) {
			return new WaitingTimeEquationProductForm(stateModel, cursor, service, resource, historicInterval, useObservations);
		} else {
			if (resource.getSchedulingStrategy() == SchedulingStrategy.FCFS) {
				return new WaitingTimeEquationMultiClassFCFS(stateModel, cursor, service, resource, historicInterval,
						useObservations);
			}
			throw new IllegalArgumentException();
		}
	}
	
	@Override
	public boolean isConstant() {
		return false;
	}

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
		
		private final Vector zeroBuffer;
		private final ErlangCEquation erlangC;
		private final ModelEquation utilization;

		/**
		 * Constructor.
		 * 
		 * @param cursor
		 * @param resource
		 * @param historicInterval
		 * @param utilization
		 */
		public WaitingTimeEquationProductForm(IStateModel<? extends IStateConstraint> stateModel,
				IRepositoryCursor cursor, Service service, Resource resource, int historicInterval,
				boolean useObservations) {
			super(stateModel, cursor, service, resource, historicInterval);
			if (useObservations) {
				this.utilization = new UtilizationValue(getStateModel(), cursor, resource, historicInterval);
			} else {
				this.utilization = new UtilizationLawEquation(getStateModel(), cursor, resource, historicInterval);
			}
			this.erlangC = new ErlangCEquation(resource.getNumberOfServers());
			zeroBuffer = zeros(stateModel.getStateSize());
			addDataDependencies(this.erlangC);
			addDataDependencies(this.utilization);
		}
		
		@Override
		public boolean hasData() {
			return utilization.hasData();
		}
		
		@Override
		public boolean isLinear() {
			return utilization.isConstant();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see tools.descartes.librede.models.observation.equations.
		 * ModelEquation#getValue(tools.descartes.librede.models.State)
		 */
		@Override
		public DerivativeStructure getValue(State state) {
			switch (res_i.getSchedulingStrategy()) {
			case FCFS:
			case PS:
			case UNKOWN:
				DerivativeStructure D_ir = state.getVariable(res_i, cls_r).getDerivativeStructure();
				return D_ir.multiply(getWaitingTimeFactor(state));
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
		
		@Override
		public Vector getFactors() {
			if (!isLinear()) {
				throw new IllegalStateException();
			}
			
			int idx = getStateModel().getStateVariableIndex(res_i, cls_r);
			double U_i = utilization.getFactors().get(0);
			double P_q = erlangC.value(U_i);
			return zeroBuffer.set(idx, P_q / (1 - U_i));
		}

		private DerivativeStructure getWaitingTimeFactor(State state) {
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
				DerivativeStructure U_i = utilization.getValue(state);
				DerivativeStructure P_q = erlangC.value(U_i);
				return P_q.divide(U_i.multiply(-1).add(1));
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
	 * Since FCFS is known to result in non-product form queueing networks, we
	 * need to use an approximation. Franks [1] has evaluated different
	 * evaluation schemes and proposes to use the following equation (slightly
	 * adapted for our needs):
	 * 
	 * W_{i,r} = \frac{PB_{r}}{m_{r}} \sum_{s=1}^{N} D_{i,s} * Q_{i,s}
	 * 
	 * where
	 * <ul>
	 * <li>W_{i,r} is the waiting time of requests of service r at resource i
	 * </li>
	 * <li>PB_{r} is the probability that all servers or resource r are busy
	 * </li>
	 * <li>N is the number of workload classes</li>
	 * <li>D_{i,s} is the resource demand of service s at resource i</li>
	 * <li>Q_{i,s} is the queue length of service s at resource i</li>
	 * </ul>
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
		public WaitingTimeEquationMultiClassFCFS(IStateModel<? extends IStateConstraint> stateModel,
				IRepositoryCursor cursor, Service service, Resource resource, int historicInterval, boolean useObservations) {
			super(stateModel, cursor, service, resource, historicInterval);
			queueLengthQuery = QueryBuilder.select(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL)
					.in(RequestCount.REQUESTS).forResourceDemands(resource.getDemands()).average().using(cursor);
			addDataDependency(queueLengthQuery);
		}


		/* (non-Javadoc)
		 * @see tools.descartes.librede.models.observation.equations.ModelEquation#getValue(tools.descartes.librede.models.State)
		 */
		@Override
		public DerivativeStructure getValue(State state) {
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
				T_q = T_q.add(D_ir.multiply(Q_ir.get(i) / res_i.getNumberOfServers()));
			}
			return T_q;
		}


		/* (non-Javadoc)
		 * @see tools.descartes.librede.models.observation.equations.ModelEquation#getFactors()
		 */
		@Override
		public Vector getFactors() {
			if (!isLinear()) {
				throw new IllegalStateException();
			}
			
			Vector Q_ir = queueLengthQuery.get(historicInterval);
			double[] factorsBuffer = new double[getStateModel().getStateSize()];
			for (int i = 0; i < Q_ir.rows(); i++) {
				ResourceDemand curDemand = (ResourceDemand) queueLengthQuery.getEntity(i);
				int stateIdx = getStateModel().getStateVariableIndex(res_i, curDemand.getService());
				// We divide the queue length by the number of parallel servers. This
				// is an approximation proposed by Giuliano Casale et al (see ICPE tutorial 2016)
				factorsBuffer[stateIdx] = Q_ir.get(i) / res_i.getNumberOfServers();
			}
			return vector(factorsBuffer);
		}


		@Override
		public boolean isLinear() {
			return true;
		}


		@Override
		public boolean hasData() {
			return queueLengthQuery.hasData();
		}
	}
}
