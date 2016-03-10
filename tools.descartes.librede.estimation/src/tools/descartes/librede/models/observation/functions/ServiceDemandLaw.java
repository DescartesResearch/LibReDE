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

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.models.observation.queueingmodel.ServiceDemandLawEquation;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.IStateConstraint;
import tools.descartes.librede.repository.IRepositoryCursor;

/**
 * This output function describes the relationship between the per-service utilization and the resource demands. 
 * It implements the Service Demand Law:
 * 
 * D_{i,r} = \frac{U_{i,r}}{X_{r}}
 * 
 * with
 * <ul>
 *  <li>D_{i,r} is the resource demand of resource i and service r</li>
 *  <li>U_{i,r} is the utilization at resource i due to service r</li>
 *  <li>X_{r} is the throughput of service r</li>
 * </ul>
 * 
 * The per-service utilization U_{i,r} is calculated from the aggregate utilization U_{i} according to the
 * following apportioning scheme:
 * 
 * U_{i,r} = \frac{U_{i} * R_{r} * X_{r}}{\sum_{v = 1}^{N} R_{v} * X_{v}}
 * 
 * with
 * <ul>
 *  <li>U_{i,r} is the utilization at resource i due to service r</li>
 *  <li>U_{i} is the aggregate utilization at resource i</li>
 *  <li>R_{r} is the average response time of service r</li>
 *  <li>X_{r} is the average throughput of service r</li>
 * </ul>
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 * @version 1.0
 */
public class ServiceDemandLaw extends AbstractDirectOutputFunction {
	
	private final ServiceDemandLawEquation sdl;
	
	/**
	 * Creates a new instance.
	 * 
	 * @param stateModel - the description of the state
	 * @param repository - the view of the repository with current measurement data
	 * @param service - the service for which the utilization is calculated
	 * @param resource - the resource for which the utilization is calculated
	 * 
	 * @throws {@link NullPointerException} if any parameter is null
	 */
	public ServiceDemandLaw(IStateModel<? extends IStateConstraint> stateModel, IRepositoryCursor repository,
			Resource resource,
			Service service) {
		this(stateModel, repository, resource, service, 0, true);
	}
	
	/**
	 * Creates a new instance.
	 * 
	 * @param stateModel - the description of the state
	 * @param repository - the view of the repository with current measurement data
	 * @param service - the service for which the utilization is calculated
	 * @param resource - the resource for which the utilization is calculated
	 * @param historicInterval - specifies the number of intervals this function is behind in the past.
	 * @param multiClass - flag indicating whether we have multiple workload classes
	 * 
	 * @throws {@link NullPointerException} if any parameter is null
	 */
	public ServiceDemandLaw(IStateModel<? extends IStateConstraint> stateModel, IRepositoryCursor repository,
			Resource resource,
			Service service, int historicInterval, boolean multiClass) {
		super(stateModel, resource, service, historicInterval);
		
		sdl = new ServiceDemandLawEquation(stateModel, repository, resource, service, historicInterval, multiClass);
	}
	
	/* (non-Javadoc)
	 * @see tools.descartes.librede.models.observation.functions.IOutputFunction#getObservedOutput()
	 */
	@Override
	public double getObservedOutput() {
		return sdl.getConstantValue();
	}

	/* (non-Javadoc)
	 * @see tools.descartes.librede.models.observation.functions.IDirectOutputFunction#getFactor()
	 */
	@Override
	public double getFactor() {
		return 1.0;
	}
	
	@Override
	public boolean hasData() {
		return sdl.hasData();
	}
	

}
