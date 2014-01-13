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
package edu.kit.ipd.descartes.librede.estimation.repository;

import java.util.List;

import edu.kit.ipd.descartes.librede.estimation.workload.IModelEntity;
import edu.kit.ipd.descartes.librede.estimation.workload.Resource;
import edu.kit.ipd.descartes.librede.estimation.workload.Service;


public interface IMonitoringRepository {

	public double getAggregationInterval(IMetric m, IModelEntity entity);
	public TimeSeries getData(IMetric metric, IModelEntity entity);
	public void setData(IMetric metric, IModelEntity entity, TimeSeries observations);
	public void setAggregatedData(IMetric m, IModelEntity entity, TimeSeries aggregatedObservations);
	public void setAggregatedData(IMetric m, IModelEntity entity, TimeSeries aggregatedObservations, double aggregationInterval);
	public boolean containsData(IMetric responseTime,
			IModelEntity entity, double maximumAggregationInterval);
	
	public List<Resource> listResources();
	public List<Service> listServices();
	
	public RepositoryCursor getCursor(double startTime, double stepSize);
}
