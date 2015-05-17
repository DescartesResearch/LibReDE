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
package tools.descartes.librede.datasource;

import java.util.Collections;
import java.util.List;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.TraceFilter;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;

/**
 * This class is a data structure to identify a trace from a data source
 * uniquely by its metric, unit, interval, model entity (resource or
 * service) and aggregation.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public class TraceKey {
	private final Metric<?> metric;
	private final Unit<?> unit;
	private final Quantity<Time> interval;
	private final ModelEntity entity;
	private final Aggregation aggregation;
	private final List<TraceFilter> filters;
	
	public TraceKey(Metric<?> metric, Unit<?> unit, Quantity<Time> interval, ModelEntity entity, Aggregation aggregation) {
		this(metric, unit, interval, entity, aggregation, Collections.<TraceFilter>emptyList());
	}

	public TraceKey(Metric<?> metric, Unit<?> unit, Quantity<Time> interval, ModelEntity entity, Aggregation aggregation, List<TraceFilter> filters) {
		super();
		this.metric = metric;
		this.unit = unit;
		this.interval = interval;
		this.entity = entity;
		this.aggregation = aggregation;
		this.filters = filters;
	}

	public Metric<?> getMetric() {
		return metric;
	}

	public Unit<?> getUnit() {
		return unit;
	}

	public Quantity<Time> getInterval() {
		return interval;
	}

	public ModelEntity getEntity() {
		return entity;
	}
	
	public Aggregation getAggregation() {
		return aggregation;
	}
	
	public List<TraceFilter> getFilters() {
		return filters;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aggregation == null) ? 0 : aggregation.hashCode());
		result = prime * result + ((entity == null) ? 0 : entity.hashCode());
		result = prime * result + ((interval == null) ? 0 : interval.hashCode());
		result = prime * result + ((metric == null) ? 0 : metric.hashCode());
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TraceKey other = (TraceKey) obj;
		if (aggregation != other.aggregation)
			return false;
		if (entity == null) {
			if (other.entity != null)
				return false;
		} else if (!entity.equals(other.entity))
			return false;
		if (interval == null) {
			if (other.interval != null)
				return false;
		} else if (!interval.equals(other.interval))
			return false;
		if (metric == null) {
			if (other.metric != null)
				return false;
		} else if (!metric.equals(other.metric))
			return false;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		return true;
	}



}
