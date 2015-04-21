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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.units.Dimension;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;
import tools.descartes.librede.units.UnitsFactory;


public class AggregationRepositoryCursor implements IRepositoryCursor {
	
	private static final Quantity<Time> ZERO_SECONDS = UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS);
	
	private IMonitoringRepository repository;
	private Quantity<Time> currentTime;
	private Quantity<Time> startTime;
	private Quantity<Time> stepSize;
	private int lastInterval;
	
	public AggregationRepositoryCursor(IMonitoringRepository repository, Quantity<Time> startTime, Quantity<Time> stepSize) {
		this.repository = repository;
		this.stepSize = stepSize;
		this.startTime = startTime;
		this.currentTime = startTime;
		this.lastInterval = -1;
	}
	
	/* (non-Javadoc)
	 * @see tools.descartes.librede.repository.IRepositoryCursor#next()
	 */
	@Override
	public boolean next() {
		Quantity<Time> newTime = currentTime.plus(stepSize);
		if (newTime.compareTo(repository.getCurrentTime()) <= 0) {
			currentTime = newTime;
			lastInterval++;
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see tools.descartes.librede.repository.IRepositoryCursor#reset()
	 */
	@Override
	public void reset() {
		this.currentTime = startTime;
		this.lastInterval = 0;
	}
	
	/* (non-Javadoc)
	 * @see tools.descartes.librede.repository.IRepositoryCursor#getLastInterval()
	 */
	public int getLastInterval() {
		return lastInterval;
	}

	/* (non-Javadoc)
	 * @see tools.descartes.librede.repository.IRepositoryCursor#getCurrentIntervalStart(int)
	 */
	@Override
	public Quantity<Time> getIntervalStart(int interval) {
		return startTime.plus(stepSize.times(interval));
	}
	
	/* (non-Javadoc)
	 * @see tools.descartes.librede.repository.IRepositoryCursor#getCurrentIntervalEnd(int)
	 */
	@Override
	public Quantity<Time> getIntervalEnd(int interval) {
		return startTime.plus(stepSize.times(interval + 1));
	}
	
	/* (non-Javadoc)
	 * @see tools.descartes.librede.repository.IRepositoryCursor#getValues(int, tools.descartes.librede.metrics.Metric, tools.descartes.librede.units.Unit, tools.descartes.librede.configuration.ModelEntity)
	 */
	@Override
	public <D extends Dimension> TimeSeries getValues(int interval, Metric<D> metric, Unit<D> unit, ModelEntity entity) {
		return repository.select(metric, unit, entity, getIntervalStart(interval), getIntervalEnd(interval));
	}
	
	/* (non-Javadoc)
	 * @see tools.descartes.librede.repository.IRepositoryCursor#getAggregatedValue(int, tools.descartes.librede.metrics.Metric, tools.descartes.librede.units.Unit, tools.descartes.librede.configuration.ModelEntity, tools.descartes.librede.metrics.Aggregation)
	 */
	@Override
	public <D extends Dimension> double getAggregatedValue(int interval, Metric<D> metric, Unit<D> unit, ModelEntity entity, Aggregation func) {
		return repository.select(metric, unit, entity, getIntervalStart(interval), getIntervalEnd(interval), func);
	}
	
	/* (non-Javadoc)
	 * @see tools.descartes.librede.repository.IRepositoryCursor#getRepository()
	 */
	@Override
	public IMonitoringRepository getRepository() {
		return repository;
	}

	/* (non-Javadoc)
	 * @see tools.descartes.librede.repository.IRepositoryCursor#hasData(int, tools.descartes.librede.metrics.Metric, java.util.List, tools.descartes.librede.metrics.Aggregation)
	 */
	@Override
	public <D extends Dimension> boolean hasData(int interval, Metric<D> metric, List<ModelEntity> entities,
			Aggregation aggregation) {
		if (metric.isAggregationAllowed(aggregation)) {
			boolean data = true;
			for (ModelEntity e : entities) {
				if (aggregation == Aggregation.NONE) {
					data = data && repository.contains(metric, e, ZERO_SECONDS);
				} else {
					data = data && repository.contains(metric, e, stepSize);
				}
			}
			return data;
		}
		return false;
	}
}
