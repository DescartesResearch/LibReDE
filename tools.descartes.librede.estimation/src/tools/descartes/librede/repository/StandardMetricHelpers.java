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

import static tools.descartes.librede.linalg.LinAlg.ones;

import java.util.HashMap;
import java.util.Map;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.repository.TimeSeries.Interpolation;
import tools.descartes.librede.units.RequestCount;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;

public class StandardMetricHelpers {
	
	private static Map<Metric, IMetricHandler> handlers = null;
	
	public static IMetricHandler createHandler(Metric metric) {
		if (handlers == null) {
			handlers = new HashMap<Metric, IMetricHandler>();
			handlers.put(StandardMetrics.ARRIVAL_RATE, new ArrivalRateHelper());
			handlers.put(StandardMetrics.ARRIVALS, new ArrivalsHelper());
			handlers.put(StandardMetrics.BUSY_TIME, new BusyTimeHelper());
			handlers.put(StandardMetrics.DEPARTURES, new DeparturesHelper());
			handlers.put(StandardMetrics.IDLE_TIME, new IdleTimeHelper());
			handlers.put(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL, new QueueLengthSeenOnArrivalHelper());
			handlers.put(StandardMetrics.RESPONSE_TIME, new ResponseTimeHelper());			
			handlers.put(StandardMetrics.THROUGHPUT, new ThroughputHelper());
			handlers.put(StandardMetrics.UTILIZATION, new UtilizationHelper());
		}
		return handlers.get(metric);
	}
	
	private static class IdleTimeHelper extends AbstractMonitoringRepository.DefaultMetricHandler {
		@Override
		public double aggregate(IMonitoringRepository repository, Metric metric, Unit unit,
				ModelEntity entity, double start, double end, Aggregation func) {
			if (metric.isAggregationAllowed(func)) {
				TimeSeries series = select(repository, metric, unit, entity, start, end);
				series.setInterpolationMethod(Interpolation.LINEAR);
				return series.sum(0);
			} else {
				throw new IllegalArgumentException();
			}
		}
	}
	
	private static class BusyTimeHelper extends AbstractMonitoringRepository.DefaultMetricHandler {
		@Override
		public double aggregate(IMonitoringRepository repository, Metric metric, Unit unit,
				ModelEntity entity, double start, double end, Aggregation func) {
			if (metric.isAggregationAllowed(func)) {
				TimeSeries series = select(repository, metric, unit, entity, start, end);
				series.setInterpolationMethod(Interpolation.LINEAR);
				return series.sum(0);
			} else {
				throw new IllegalArgumentException();
			}
		}
	}

	private static class UtilizationHelper extends AbstractMonitoringRepository.DefaultMetricHandler {
		@Override
		public double aggregate(IMonitoringRepository repository, Metric metric, Unit unit, ModelEntity entity, double start, double end, Aggregation func) {
			if (metric.isAggregationAllowed(func)) {
				TimeSeries series = select(repository, metric, unit, entity, start, end);
				series.setInterpolationMethod(Interpolation.PIECEWISE_CONSTANT);
				return series.timeWeightedMean(0);
			} else {
				throw new IllegalArgumentException();
			}
		}

		@Override
		public boolean contains(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double aggregationInterval) {
			if (super.contains(repository, metric, entity, aggregationInterval)) {
				return true;
			}
			return repository.contains(StandardMetrics.BUSY_TIME, entity, aggregationInterval) && repository.contains(StandardMetrics.IDLE_TIME, entity, aggregationInterval);
		}
	}

	private static class ArrivalsHelper extends AbstractMonitoringRepository.DefaultMetricHandler {		
		@Override
		public TimeSeries select(IMonitoringRepository repository, Metric metric, Unit unit,
				ModelEntity entity, double start, double end) {
			TimeSeries ts = repository.select(StandardMetrics.ARRIVALS, unit, entity);
			if (ts.isEmpty()) {
				TimeSeries resp = repository.select(StandardMetrics.RESPONSE_TIME, Time.SECONDS, entity);
				double interval = repository.getAggregationInterval(StandardMetrics.RESPONSE_TIME, entity);
				if (!resp.isEmpty() && (interval == 0.0)) {
					ts = new TimeSeries(resp.getTime().minus(resp.getData(0)), ones(resp.getData(0).rows()));
					ts.setStartTime(resp.getStartTime());
					ts.setEndTime(ts.getEndTime());
					return UnitConverter.convertTo(ts.subset(start, end), RequestCount.REQUESTS, unit);
				} else {
					return TimeSeries.EMPTY;
				}
			}
			return ts.subset(start, end);
		}

		@Override
		public double aggregate(IMonitoringRepository repository, Metric metric, Unit unit, ModelEntity entity, double start, double end, Aggregation func) {
			TimeSeries series = select(repository, metric, unit, entity, start, end);
			series.setInterpolationMethod(Interpolation.LINEAR);
			switch(func) {
			case SUM: {
				return series.sum(0);
			}
			case MINIMUM: {
				return series.min(0);
			}
			case MAXIMUM: {
				return series.max(0);
			}
			default:
				throw new IllegalArgumentException();
			}
		}
		
		@Override
		public boolean contains(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double aggregationInterval) {
			if (super.contains(repository, metric, entity, aggregationInterval)) {
				return true;
			}
			return repository.contains(StandardMetrics.RESPONSE_TIME, entity, 0.0);
		}
	}
	
	private static class DeparturesHelper extends AbstractMonitoringRepository.DefaultMetricHandler {
		@Override
		public TimeSeries select(IMonitoringRepository repository, Metric metric, Unit unit,
				ModelEntity entity, double start, double end) {
			TimeSeries ts = repository.select(StandardMetrics.DEPARTURES, unit, entity);
			if (ts.isEmpty()) {
				TimeSeries resp = repository.select(StandardMetrics.RESPONSE_TIME, Time.SECONDS, entity);
				double interval = repository.getAggregationInterval(StandardMetrics.RESPONSE_TIME, entity);
				if (!resp.isEmpty() && (interval == 0.0)) {
					ts = new TimeSeries(resp.getTime(), ones(resp.getData(0).rows()));
					ts.setStartTime(resp.getStartTime());
					ts.setEndTime(resp.getEndTime());
					return UnitConverter.convertTo(ts.subset(start, end), RequestCount.REQUESTS, unit);
				} else {
					return TimeSeries.EMPTY;
				}
			}		
			return ts.subset(start, end);
		}

		@Override
		public double aggregate(IMonitoringRepository repository, Metric metric, Unit unit, ModelEntity entity, double start, double end, Aggregation func) {
			TimeSeries series = select(repository, metric, unit, entity, start, end);
			series.setInterpolationMethod(Interpolation.LINEAR);
			switch(func) {
			case SUM: {
				return series.sum(0);
			}
			case MINIMUM: {
				return series.min(0);
			}
			case MAXIMUM: {
				return series.max(0);
			}
			default:
				throw new IllegalArgumentException();
			}
		}
		
		@Override
		public boolean contains(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double aggregationInterval) {
			if (super.contains(repository, metric, entity, aggregationInterval)) {
				return true;
			}
			return repository.contains(StandardMetrics.RESPONSE_TIME, entity, 0.0);
		}
	}

	private static class ArrivalRateHelper extends AbstractMonitoringRepository.DefaultMetricHandler {
		@Override
		public double aggregate(IMonitoringRepository repository, Metric metric, Unit unit, ModelEntity entity, double start, double end, Aggregation func) {
			if (metric.isAggregationAllowed(func)) {
				TimeSeries ts = select(repository, metric, unit, entity, start, end);
				if (ts.isEmpty()) {
					double arrivals = repository.select(StandardMetrics.ARRIVALS, RequestCount.REQUESTS,
							entity, start, end, Aggregation.SUM);
					return unit.convertFrom(arrivals / (end - start), RequestRate.REQ_PER_SECOND);
				}
				return ts.timeWeightedMean(0);
			} else {
				throw new IllegalArgumentException();
			}
		}
		
		@Override
		public boolean contains(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double aggregationInterval) {
			if (super.contains(repository, metric, entity, aggregationInterval)) {
				return true;
			}
			return repository.contains(StandardMetrics.ARRIVALS, entity, aggregationInterval);
		}
	}
	
	private static class ThroughputHelper extends AbstractMonitoringRepository.DefaultMetricHandler {
		@Override
		public double aggregate(IMonitoringRepository repository, Metric metric, Unit unit, ModelEntity entity, double start, double end, Aggregation func) {
			if (metric.isAggregationAllowed(func)) {
				TimeSeries ts = select(repository, metric, unit, entity, start, end);
				if (ts.isEmpty()) {
					double departures = repository.select(StandardMetrics.DEPARTURES, RequestCount.REQUESTS,
							entity, start, end, Aggregation.SUM);
					return unit.convertFrom(departures / (end - start), RequestRate.REQ_PER_SECOND);
				}
				return ts.timeWeightedMean(0);
			} else {
				throw new IllegalArgumentException();
			}
		}
		
		@Override
		public boolean contains(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double aggregationInterval) {
			if (super.contains(repository, metric, entity, aggregationInterval)) {
				return true;
			}
			return repository.contains(StandardMetrics.DEPARTURES, entity, aggregationInterval);
		}
	}
	
	private static class ResponseTimeHelper extends AbstractMonitoringRepository.DefaultMetricHandler {
		@Override
		public double aggregate(IMonitoringRepository repository, Metric metric, Unit unit, ModelEntity entity, double start, double end, Aggregation func) {
			TimeSeries series = select(repository, metric, unit, entity, start, end);
			switch (func) {
			case AVERAGE: {
				double interval = repository.getAggregationInterval(metric,
						entity);
				if (interval > 0) {
					TimeSeries weights = repository.select(StandardMetrics.THROUGHPUT, RequestRate.REQ_PER_SECOND, entity, start, end);
					if (weights.isEmpty()) {
						weights = repository.select(StandardMetrics.DEPARTURES, RequestCount.REQUESTS, entity, start, end);
					}
					if (weights.isEmpty()) {
						throw new IllegalStateException();
					}
					return series.timeWeightedMean(0, weights);
				} else {
					return series.mean(0);
				}
			}
			case MAXIMUM: {
				return series.max(0);
			}
			case MINIMUM: {
				return series.min(0);
			}
			default:
				throw new IllegalArgumentException();
			}
		}

		@Override
		public TimeSeries select(IMonitoringRepository repository, Metric metric, Unit unit,
				ModelEntity entity, double start, double end) {
			TimeSeries ts = repository.select(metric, unit, entity);
			if (ts.isEmpty()) {
				TimeSeries arriv = repository.select(StandardMetrics.ARRIVALS, RequestCount.REQUESTS, entity);
				TimeSeries departures = repository.select(StandardMetrics.DEPARTURES, RequestCount.REQUESTS, entity);
				if (!arriv.isEmpty() && !departures.isEmpty()) {
					ts = new TimeSeries(departures.getTime(), departures.getTime().minus(arriv.getTime()));
					return UnitConverter.convertTo(ts.subset(start, end), Time.SECONDS, unit);
				}				
			}
			return ts.subset(start, end);
		}
		
		@Override
		public boolean contains(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double aggregationInterval) {
			if (super.contains(repository, metric, entity, aggregationInterval)) {
				return true;
			}
			return repository.contains(StandardMetrics.DEPARTURES, entity, 0.0) && repository.contains(StandardMetrics.ARRIVALS, entity, 0.0);
		}
	}
	
	private static class QueueLengthSeenOnArrivalHelper extends AbstractMonitoringRepository.DefaultMetricHandler {
		@Override
		public double aggregate(IMonitoringRepository repository, Metric metric, Unit unit,
				ModelEntity entity, double start, double end, Aggregation func) {
			TimeSeries series = select(repository, metric, unit, entity, start, end);
			switch(func) {
			case AVERAGE: {
				double interval = repository.getAggregationInterval(metric,
						entity);
				if (interval > 0) {
					TimeSeries weights = repository.select(StandardMetrics.THROUGHPUT, RequestRate.REQ_PER_SECOND, entity, start, end);
					if (weights.isEmpty()) {
						weights = repository.select(StandardMetrics.DEPARTURES, RequestCount.REQUESTS, entity, start, end);
					}
					if (weights.isEmpty()) {
						throw new IllegalStateException();
					}
					return series.timeWeightedMean(0, weights);
				} else {
					return series.mean(0);
				}
			}
			case MINIMUM:
				return series.min(0);
			case MAXIMUM:
				return series.max(0);
			default:
				throw new IllegalArgumentException();
			}
		}
	};

	private StandardMetricHelpers() {
	}
}
