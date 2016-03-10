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

/**
 * This class enables the calculation of the residence time for individual
 * resources.
 * 
 * The underlying is: R_{i,r} = D_{i,r} + W_{i,r}
 * 
 * with
 * <ul>
 * <li>R_{i,r} is the residence time of service r at resource i</li>
 * <li>D_{i,r} is the resource demand of resource i and service r</li>
 * <li>W_{i,r} is the waiting time of newly arriving jobs of service r at
 * resource i</li>
 * </ul>
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public class ResidenceTimeEquation extends AbstractDependencyTarget {

	private final int historicInterval;
	private final Service cls_r;
	private final Resource res_i;
	private final WaitingTimeEquation waitingTime;
	private final Query<Scalar, Ratio> contentionQuery;

	/**
	 * Constructor.
	 * 
	 * @param cursor
	 *            the position in the monitoring data
	 * @param service
	 *            service i
	 * @param resource
	 *            resource r
	 * @param historicInterval
	 *            the monitoring interval for which to calculate the residence
	 *            time (0 == current)
	 * @param waitingTime
	 *            a reference to a {@code WaitingTimeEquation} to calculate
	 *            W_{i,r}
	 */
	public ResidenceTimeEquation(IRepositoryCursor cursor, Service service, Resource resource, int historicInterval,
			WaitingTimeEquation waitingTime) {
		this.historicInterval = historicInterval;
		this.cls_r = service;
		this.res_i = resource;
		this.waitingTime = waitingTime;
		addDataDependencies(waitingTime);

		// The contention is the ratio of time a virtual CPU is waiting for a
		// physical CPU.
		contentionQuery = QueryBuilder.select(StandardMetrics.CONTENTION).in(Ratio.NONE).forResource(res_i).average()
				.using(cursor);
		addDataDependency(contentionQuery);
	}

	/**
	 * @param state
	 *            the current state of the system
	 * @return a {@code DerivativeStructure} containing the residence time
	 */
	public DerivativeStructure getResidenceTime(State state) {
		DerivativeStructure D_ir = state.getVariable(res_i, cls_r).getDerivativeStructure();
		DerivativeStructure T_q = waitingTime.getAverageWaitingTime(cls_r, state);
		double C_i = contentionQuery.get(historicInterval).getValue();
		return D_ir.add(T_q).multiply(1 + C_i);
	}
	
	public Vector getLinearResidenceTimeFactors(State state) {
		Vector factors = waitingTime.getLinearWaitingTimeFactors(cls_r, state);
		double C_i = contentionQuery.get(historicInterval).getValue();
		factors = factors.set(state.getStateModel().getStateVariableIndex(res_i, cls_r), state.getVariable(res_i, cls_r).getValue() + 1);
		return factors.times(1 + C_i);
	}
	
	public boolean isLinear() {
		return waitingTime.isLinear();
	}

}
