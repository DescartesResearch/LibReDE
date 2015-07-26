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
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.units.Dimension;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;


public interface IMonitoringRepository {
	
	public <D extends Dimension> TimeSeries select(Metric<D> metric, Unit<D> unit, ModelEntity entity, Aggregation aggregation);
	
	public <D extends Dimension> TimeSeries select(Metric<D> metric, Unit<D> unit, ModelEntity entity, Aggregation aggregation, Quantity<Time> start, Quantity<Time> end);

	public <D extends Dimension> double aggregate(Metric<D> metric, Unit<D> unit, ModelEntity entity, Aggregation aggregation, Quantity<Time> start, Quantity<Time> end);
	
	public <D extends Dimension> boolean exists(Metric<D> metric, ModelEntity entity, Aggregation aggregation);
	
	public <D extends Dimension> Quantity<Time> getAggregationInterval(Metric<D> metric, ModelEntity entity, Aggregation aggregation);
	
	public <D extends Dimension> Quantity<Time> getMonitoringStartTime(Metric<D> metric, ModelEntity entity, Aggregation aggregation);
	
	public <D extends Dimension> Quantity<Time> getMonitoringEndTime(Metric<D> metric, ModelEntity entity, Aggregation aggregation);
	
	public List<Resource> listResources();
	public List<Service> listServices();
	
	public IRepositoryCursor getCursor(Quantity<Time> startTime, Quantity<Time> stepSize);
	
	public Quantity<Time> getCurrentTime();
	public void setCurrentTime(Quantity<Time> currentTime);
	public WorkloadDescription getWorkload();
}
