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
import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.repository.TimeSeries.Interpolation;

public enum StandardMetricHelpers implements IMetricHandler {
	
	IDLE_TIME {
		@Override
		public TimeSeries retrieve(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double start, double end) {
			TimeSeries series = repository.select(metric, entity);
			return series.subset(start, end);
		}

		@Override
		public double aggregate(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double start, double end, Aggregation func) {
			if (metric.isAggregationAllowed(func)) {
				TimeSeries series = retrieve(repository, metric, entity, start, end);
				series.setInterpolationMethod(Interpolation.LINEAR);
				return series.sum(0);
			} else {
				throw new IllegalArgumentException();
			}
		}

		@Override
		public boolean hasData(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double aggregationInterval) {
			return repository.contains(metric, entity, aggregationInterval);
		}
	},
	
	BUSY_TIME {

		@Override
		public TimeSeries retrieve(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double start, double end) {
			TimeSeries series = repository.select(metric, entity);
			return series.subset(start, end);
		}

		@Override
		public double aggregate(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double start, double end, Aggregation func) {
			if (metric.isAggregationAllowed(func)) {
				TimeSeries series = retrieve(repository, metric,entity, start, end);
				series.setInterpolationMethod(Interpolation.LINEAR);
				return series.sum(0);
			} else {
				throw new IllegalArgumentException();
			}
		}

		@Override
		public boolean hasData(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double aggregationInterval) {
			return repository.contains(metric, entity, aggregationInterval);
		}
	},

	UTILIZATION {	

		@Override
		public double aggregate(IMonitoringRepository repository, Metric metric, ModelEntity entity, double start, double end, Aggregation func) {
			if (metric.isAggregationAllowed(func)) {
				TimeSeries series = retrieve(repository, metric, entity, start, end);
				series.setInterpolationMethod(Interpolation.PIECEWISE_CONSTANT);
				return series.timeWeightedMean(0);
			} else {
				throw new IllegalArgumentException();
			}
		}

		@Override
		public TimeSeries retrieve(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double start, double end) {
			TimeSeries series = repository.select(metric, entity);
			return series.subset(start, end);
		}
		
		@Override
		public boolean hasData(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double aggregationInterval) {
			if (repository.contains(metric, entity, aggregationInterval)) {
				return true;
			}
			return repository.contains(StandardMetrics.BUSY_TIME, entity, aggregationInterval) && repository.contains(StandardMetrics.IDLE_TIME, entity, aggregationInterval);
		}
	},

	ARRIVALS {
		
		@Override
		public TimeSeries retrieve(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double start, double end) {
			TimeSeries ts = repository.select(StandardMetrics.ARRIVALS, entity);
			if (ts.isEmpty()) {
				TimeSeries resp = repository.select(StandardMetrics.RESPONSE_TIME, entity);
				double interval = repository.getAggregationInterval(StandardMetrics.RESPONSE_TIME, entity);
				if (!resp.isEmpty() && (interval == 0.0)) {
					ts = new TimeSeries(resp.getTime().minus(resp.getData(0)), ones(resp.getData(0).rows()));
					ts.setStartTime(resp.getStartTime());
					ts.setEndTime(ts.getEndTime());
					return ts.subset(start, end);
				} else {
					return TimeSeries.EMPTY;
				}
			}
			return ts.subset(start, end);
		}

		@Override
		public double aggregate(IMonitoringRepository repository, Metric metric, ModelEntity entity, double start, double end, Aggregation func) {
			TimeSeries series = retrieve(repository, metric, entity, start, end);
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
		public boolean hasData(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double aggregationInterval) {
			if (repository.contains(metric, entity, aggregationInterval)) {
				return true;
			}
			return repository.contains(StandardMetrics.RESPONSE_TIME, entity, 0.0);
		}
	},
	DEPARTURES {
		
		@Override
		public TimeSeries retrieve(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double start, double end) {
			TimeSeries ts = repository.select(StandardMetrics.DEPARTURES, entity);
			if (ts.isEmpty()) {
				TimeSeries resp = repository.select(StandardMetrics.RESPONSE_TIME, entity);
				double interval = repository.getAggregationInterval(StandardMetrics.RESPONSE_TIME, entity);
				if (!resp.isEmpty() && (interval == 0.0)) {
					ts = new TimeSeries(resp.getTime(), ones(resp.getData(0).rows()));
					ts.setStartTime(resp.getStartTime());
					ts.setEndTime(resp.getEndTime());
					return ts.subset(start, end);
				} else {
					return TimeSeries.EMPTY;
				}
			}		
			return ts.subset(start, end);
		}

		@Override
		public double aggregate(IMonitoringRepository repository, Metric metric, ModelEntity entity, double start, double end, Aggregation func) {
			TimeSeries series = retrieve(repository, metric, entity, start, end);
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
		public boolean hasData(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double aggregationInterval) {
			if (repository.contains(metric, entity, aggregationInterval)) {
				return true;
			}
			return repository.contains(StandardMetrics.RESPONSE_TIME, entity, 0.0);
		}
	},
	ARRIVAL_RATE {

		@Override
		public double aggregate(IMonitoringRepository repository, Metric metric, ModelEntity entity, double start, double end, Aggregation func) {
			if (metric.isAggregationAllowed(func)) {
				TimeSeries ts = retrieve(repository, metric, entity, start, end);
				if (ts.isEmpty()) {
					double arrivals = StandardMetricHelpers.ARRIVALS.aggregate(repository, StandardMetrics.ARRIVALS,
							entity, start, end, Aggregation.SUM);
					return arrivals / (end - start);
				}
				return ts.timeWeightedMean(0);
			} else {
				throw new IllegalArgumentException();
			}
		}

		@Override
		public TimeSeries retrieve(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double start, double end) {
			TimeSeries series = repository.select(metric, entity);
			return series.subset(start, end);
		}
		
		@Override
		public boolean hasData(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double aggregationInterval) {
			if (repository.contains(metric, entity, aggregationInterval)) {
				return true;
			}
			return ARRIVALS.hasData(repository, StandardMetrics.ARRIVALS, entity, aggregationInterval);
		}
	},
	THROUGHPUT {

		@Override
		public double aggregate(IMonitoringRepository repository, Metric metric, ModelEntity entity, double start, double end, Aggregation func) {
			if (metric.isAggregationAllowed(func)) {
				TimeSeries ts = retrieve(repository, metric, entity, start, end);
				if (ts.isEmpty()) {
					double departures = StandardMetricHelpers.DEPARTURES.aggregate(repository, StandardMetrics.DEPARTURES,
							entity, start, end, Aggregation.SUM);
					return departures / (end - start);
				}
				return ts.timeWeightedMean(0);
			} else {
				throw new IllegalArgumentException();
			}
		}
		
		@Override
		public TimeSeries retrieve(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double start, double end) {
			TimeSeries series = repository.select(metric, entity);
			return series.subset(start, end);
		}
		
		@Override
		public boolean hasData(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double aggregationInterval) {
			if (repository.contains(metric, entity, aggregationInterval)) {
				return true;
			}
			return DEPARTURES.hasData(repository, StandardMetrics.DEPARTURES, entity, aggregationInterval);
		}
	},
	RESPONSE_TIME {

		@Override
		public double aggregate(IMonitoringRepository repository, Metric metric, ModelEntity entity, double start, double end, Aggregation func) {
			TimeSeries series = retrieve(repository, metric, entity, start, end);
			switch (func) {
			case AVERAGE: {
				double interval = repository.getAggregationInterval(metric,
						entity);
				if (interval > 0) {
					TimeSeries weights = THROUGHPUT.retrieve(repository, StandardMetrics.THROUGHPUT, entity, start, end);
					if (weights.isEmpty()) {
						weights = DEPARTURES.retrieve(repository, StandardMetrics.DEPARTURES, entity, start, end);
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
		public TimeSeries retrieve(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double start, double end) {
			TimeSeries ts = repository.select(metric, entity);
			if (ts.isEmpty()) {
				TimeSeries arriv = repository.select(StandardMetrics.ARRIVALS, entity);
				TimeSeries departures = repository.select(StandardMetrics.DEPARTURES, entity);
				if (!arriv.isEmpty() && !departures.isEmpty()) {
					ts = new TimeSeries(departures.getTime(), departures.getTime().minus(arriv.getTime()));
					return ts.subset(start, end);
				}				
			}
			return ts.subset(start, end);
		}
		
		@Override
		public boolean hasData(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double aggregationInterval) {
			if (repository.contains(metric, entity, aggregationInterval)) {
				return true;
			}
			return DEPARTURES.hasData(repository, StandardMetrics.DEPARTURES, entity, 0.0) && ARRIVALS.hasData(repository, StandardMetrics.ARRIVALS, entity, 0.0);
		}
	},
	QUEUE_LENGTH_SEEN_ON_ARRIVAL {

		@Override
		public TimeSeries retrieve(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double start, double end) {
			return repository.select(metric, entity).subset(start, end);
		}

		@Override
		public double aggregate(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double start, double end, Aggregation func) {
			TimeSeries series = retrieve(repository, metric, entity, start, end);
			switch(func) {
			case AVERAGE: {
				double interval = repository.getAggregationInterval(metric,
						entity);
				if (interval > 0) {
					TimeSeries weights = THROUGHPUT.retrieve(repository, StandardMetrics.THROUGHPUT, entity, start, end);
					if (weights.isEmpty()) {
						weights = DEPARTURES.retrieve(repository, StandardMetrics.DEPARTURES, entity, start, end);
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

		@Override
		public boolean hasData(IMonitoringRepository repository, Metric metric,
				ModelEntity entity, double aggregationInterval) {
			return repository.contains(metric, entity, aggregationInterval);
		}
	};

	private StandardMetricHelpers() {
	}
}
