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
package tools.descartes.librede.models.observation.queueingmodel;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.AbstractDependencyTarget;
import tools.descartes.librede.models.State;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.units.Ratio;
import tools.descartes.librede.units.RequestRate;

/**
 * This class can be used to obtain the utilization of a resource.
 * 
 * It returns either the observed value or a calculated one using the
 * Utilization Law. An instance of this class can be created using the factory
 * method
 * {@code UtilizationFunction#create(IRepositoryCursor, Resource, int, boolean)}
 * .
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public abstract class UtilizationFunction extends AbstractDependencyTarget {

	// The resource of which the utilization is returned
	protected final Resource res_i;
	// Specifies the interval in the past (0 == current)
	protected final int historicInterval;

	/**
	 * Use
	 * {@code UtilizationFunction#create(IRepositoryCursor, Resource, int, boolean)}
	 * instead.
	 * 
	 * @param cursor
	 * @param resource
	 * @param historicInterval
	 */
	private UtilizationFunction(IRepositoryCursor cursor, Resource resource, int historicInterval) {
		this.res_i = resource;
		this.historicInterval = historicInterval;
	}

	/**
	 * Factory method for a new instance.
	 * 
	 * @param cursor
	 *            the position in the monitoring data
	 * @param resource
	 *            the resource of which utilization is requested
	 * @param historicInterval
	 *            the monitoring interval for which to calculate the utilization
	 *            (0 == current)
	 * @param calculated
	 *            A flag indicated whether to use observed or calculated
	 *            utilization values
	 * @return a new {@code UtilizationFunction} instance
	 */
	public static UtilizationFunction create(IRepositoryCursor cursor, Resource resource, int historicInterval,
			boolean calculated) {
		if (calculated) {
			return new CalculatedUtilizationFunction(cursor, resource, historicInterval);
		} else {
			return new ObservedUtilizationFunction(cursor, resource, historicInterval);
		}
	}

	/**
	 * Obtains the utilization of the resource for the specified historic
	 * interval.
	 * 
	 * @param state
	 *            the current state of the system
	 * @return a DerivativeStructure containing the utilization values.
	 */
	public abstract DerivativeStructure getUtilization(State state);

	/**
	 * @return a constant value of the current utilization.
	 */
	public abstract double getValue();

	/**
	 * @return a boolean indicating whether this function is constant or the
	 *         utilization depends on the current state vector.
	 */
	public abstract boolean isConstant();

	/**
	 * This is an implementation of {@code UtilizationFunction} that returns the
	 * currently observed utilization.
	 * 
	 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
	 *
	 */
	private static class ObservedUtilizationFunction extends UtilizationFunction {

		private final Query<Scalar, Ratio> utilQuery;

		/**
		 * Constructor.
		 * 
		 * @param cursor
		 * @param resource
		 * @param historicInterval
		 */
		public ObservedUtilizationFunction(IRepositoryCursor cursor, Resource resource, int historicInterval) {
			super(cursor, resource, historicInterval);
			this.utilQuery = QueryBuilder.select(StandardMetrics.UTILIZATION).in(Ratio.NONE).forResource(resource)
					.average().using(cursor);
			addDataDependency(utilQuery);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see tools.descartes.librede.models.observation.functions.helper.
		 * UtilizationFunction#getUtilization(tools.descartes.librede.models.
		 * State)
		 */
		@Override
		public DerivativeStructure getUtilization(State state) {
			return new DerivativeStructure(state.getStateSize(), state.getDerivationOrder(), getValue());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see tools.descartes.librede.models.observation.functions.helper.
		 * UtilizationFunction#getValue(tools.descartes.librede.models.State)
		 */
		@Override
		public double getValue() {
			return utilQuery.get(historicInterval).getValue();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see tools.descartes.librede.models.observation.functions.helper.
		 * UtilizationFunction#isConstant()
		 */
		@Override
		public boolean isConstant() {
			return true;
		}
	}

	/**
	 * This is an implementation of {@code UtilizationFunction} that returns a
	 * calculated utilization using the Utilization Law.
	 * 
	 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
	 *
	 */
	private static class CalculatedUtilizationFunction extends UtilizationFunction {

		private final Query<Scalar, Ratio> contentionQuery;
		private final Query<Vector, RequestRate> throughputQuery;

		/**
		 * Constructor
		 * 
		 * @param cursor
		 * @param resource
		 * @param historicInterval
		 */
		public CalculatedUtilizationFunction(IRepositoryCursor cursor, Resource resource, int historicInterval) {
			super(cursor, resource, historicInterval);
			this.throughputQuery = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND)
					.forServices(res_i.getAccessingServices()).average().using(cursor);
			addDataDependency(throughputQuery);
			this.contentionQuery = QueryBuilder.select(StandardMetrics.CONTENTION).in(Ratio.NONE).forResource(res_i)
					.average().using(cursor);
			addDataDependency(contentionQuery);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see tools.descartes.librede.models.observation.functions.helper.
		 * UtilizationFunction#getUtilization(tools.descartes.librede.models.
		 * State)
		 */
		@Override
		public DerivativeStructure getUtilization(State state) {
			/*
			 * Calculate the utilization using the utilization law.
			 */
			// sorted according to state variable ordering
			double C_i = contentionQuery.get(historicInterval).getValue();
			Vector X = throughputQuery.get(historicInterval);
			DerivativeStructure U_i = new DerivativeStructure(state.getStateSize(), state.getDerivationOrder(), 0.0);
			for (Service curService : res_i.getAccessingServices()) {
				int idx = throughputQuery.indexOf(curService);
				U_i = U_i.add(state.getVariable(res_i, curService).getDerivativeStructure().multiply(X.get(idx)));
			}
			return U_i.divide(res_i.getNumberOfServers()).add(C_i);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see tools.descartes.librede.models.observation.functions.helper.
		 * UtilizationFunction#getValue(tools.descartes.librede.models.State)
		 */
		@Override
		public double getValue() {
			// this function depends on the state and cannot provide a constant
			// value.
			throw new UnsupportedOperationException();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see tools.descartes.librede.models.observation.functions.helper.
		 * UtilizationFunction#isConstant()
		 */
		@Override
		public boolean isConstant() {
			return false;
		}
	}

}
