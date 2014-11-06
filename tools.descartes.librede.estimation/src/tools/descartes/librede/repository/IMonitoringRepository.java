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
package tools.descartes.librede.repository;

import java.util.List;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.workload.WorkloadDescription;


public interface IMonitoringRepository {

	public double getAggregationInterval(IMetric m, ModelEntity entity);
	public TimeSeries getData(IMetric metric, ModelEntity entity);
	public void setData(IMetric metric, ModelEntity entity, TimeSeries observations);
	public void setAggregatedData(IMetric m, ModelEntity entity, TimeSeries aggregatedObservations);
	public void setAggregatedData(IMetric m, ModelEntity entity, TimeSeries aggregatedObservations, double aggregationInterval);
	public boolean containsData(IMetric responseTime,
			ModelEntity entity, double maximumAggregationInterval);
	
	public List<Resource> listResources();
	public List<Service> listServices();
	
	public IRepositoryCursor getCursor(double startTime, double stepSize);
	
	public double getCurrentTime();
	public void setCurrentTime(double currentTime);
	public WorkloadDescription getWorkload();
}
