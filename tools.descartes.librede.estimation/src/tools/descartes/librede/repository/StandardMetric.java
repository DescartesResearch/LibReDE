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
import static tools.descartes.librede.repository.Aggregation.AVERAGE;
import static tools.descartes.librede.repository.Aggregation.MAXIMUM;
import static tools.descartes.librede.repository.Aggregation.MINIMUM;
import static tools.descartes.librede.repository.Aggregation.NONE;
import static tools.descartes.librede.repository.Aggregation.SUM;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.UUID;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.repository.TimeSeries.Interpolation;

public enum StandardMetric implements IMetric {
	
	IDLE_TIME("idle time", SUM) {
		@Override
		public TimeSeries retrieve(IMonitoringRepository repository,
				ModelEntity entity, double start, double end) {
			TimeSeries series = repository.getData(this, entity);
			return series.subset(start, end);
		}

		@Override
		public double aggregate(IMonitoringRepository repository,
				ModelEntity entity, double start, double end, Aggregation func) {
			if (isAggregationSupported(func)) {
				TimeSeries series = retrieve(repository, entity, start, end);
				series.setInterpolationMethod(Interpolation.LINEAR);
				return series.sum(0);
			} else {
				throw new IllegalArgumentException();
			}
		}

		@Override
		public boolean hasData(IMonitoringRepository repository,
				ModelEntity entity, double aggregationInterval) {
			return repository.containsData(this, entity, aggregationInterval);
		}
	},
	
	BUSY_TIME("busy time", SUM) {

		@Override
		public TimeSeries retrieve(IMonitoringRepository repository,
				ModelEntity entity, double start, double end) {
			TimeSeries series = repository.getData(this, entity);
			return series.subset(start, end);
		}

		@Override
		public double aggregate(IMonitoringRepository repository,
				ModelEntity entity, double start, double end, Aggregation func) {
			if (isAggregationSupported(func)) {
				TimeSeries series = retrieve(repository, entity, start, end);
				series.setInterpolationMethod(Interpolation.LINEAR);
				return series.sum(0);
			} else {
				throw new IllegalArgumentException();
			}
		}

		@Override
		public boolean hasData(IMonitoringRepository repository,
				ModelEntity entity, double aggregationInterval) {
			return repository.containsData(this, entity, aggregationInterval);
		}		
	},

	UTILIZATION("utilization", AVERAGE) {	

		@Override
		public double aggregate(IMonitoringRepository repository, ModelEntity entity, double start, double end, Aggregation func) {
			if (isAggregationSupported(func)) {
				TimeSeries series = retrieve(repository, entity, start, end);
				series.setInterpolationMethod(Interpolation.PIECEWISE_CONSTANT);
				return series.timeWeightedMean(0);
			} else {
				throw new IllegalArgumentException();
			}
		}

		@Override
		public TimeSeries retrieve(IMonitoringRepository repository,
				ModelEntity entity, double start, double end) {
			TimeSeries series = repository.getData(this, entity);
			return series.subset(start, end);
		}
		
		@Override
		public boolean hasData(IMonitoringRepository repository,
				ModelEntity entity, double aggregationInterval) {
			if (repository.containsData(this, entity, aggregationInterval)) {
				return true;
			}
			return repository.containsData(BUSY_TIME, entity, aggregationInterval) && repository.containsData(IDLE_TIME, entity, aggregationInterval);
		}

	},

	ARRIVALS("arrivals", NONE, SUM, MINIMUM, MAXIMUM) {
		
		@Override
		public TimeSeries retrieve(IMonitoringRepository repository,
				ModelEntity entity, double start, double end) {
			TimeSeries ts = repository.getData(ARRIVALS, entity);
			if (ts.isEmpty()) {
				TimeSeries resp = repository.getData(RESPONSE_TIME, entity);
				double interval = repository.getAggregationInterval(RESPONSE_TIME, entity);
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
		public double aggregate(IMonitoringRepository repository, ModelEntity entity, double start, double end, Aggregation func) {
			TimeSeries series = retrieve(repository, entity, start, end);
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
		public boolean hasData(IMonitoringRepository repository,
				ModelEntity entity, double aggregationInterval) {
			if (repository.containsData(this, entity, aggregationInterval)) {
				return true;
			}
			return repository.containsData(RESPONSE_TIME, entity, 0.0);
		}

	},
	DEPARTURES("departures", NONE, SUM, MINIMUM, MAXIMUM) {
		
		@Override
		public TimeSeries retrieve(IMonitoringRepository repository,
				ModelEntity entity, double start, double end) {
			TimeSeries ts = repository.getData(DEPARTURES, entity);
			if (ts.isEmpty()) {
				TimeSeries resp = repository.getData(RESPONSE_TIME, entity);
				double interval = repository.getAggregationInterval(RESPONSE_TIME, entity);
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
		public double aggregate(IMonitoringRepository repository, ModelEntity entity, double start, double end, Aggregation func) {
			TimeSeries series = retrieve(repository, entity, start, end);
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
		public boolean hasData(IMonitoringRepository repository,
				ModelEntity entity, double aggregationInterval) {
			if (repository.containsData(this, entity, aggregationInterval)) {
				return true;
			}
			return repository.containsData(RESPONSE_TIME, entity, 0.0);
		}

	},
	ARRIVAL_RATE("arrival rate", AVERAGE) {

		@Override
		public double aggregate(IMonitoringRepository repository, ModelEntity entity, double start, double end, Aggregation func) {
			if (isAggregationSupported(func)) {
				TimeSeries ts = retrieve(repository, entity, start, end);
				if (ts.isEmpty()) {
					double arrivals = StandardMetric.ARRIVALS.aggregate(repository,
							entity, start, end, SUM);
					return arrivals / (end - start);
				}
				return ts.timeWeightedMean(0);
			} else {
				throw new IllegalArgumentException();
			}
		}

		@Override
		public TimeSeries retrieve(IMonitoringRepository repository,
				ModelEntity entity, double start, double end) {
			TimeSeries series = repository.getData(this, entity);
			return series.subset(start, end);
		}
		
		@Override
		public boolean hasData(IMonitoringRepository repository,
				ModelEntity entity, double aggregationInterval) {
			if (repository.containsData(this, entity, aggregationInterval)) {
				return true;
			}
			return ARRIVALS.hasData(repository, entity, aggregationInterval);
		}
	},
	THROUGHPUT("throughput", AVERAGE) {

		@Override
		public double aggregate(IMonitoringRepository repository, ModelEntity entity, double start, double end, Aggregation func) {
			if (isAggregationSupported(func)) {
				TimeSeries ts = retrieve(repository, entity, start, end);
				if (ts.isEmpty()) {
					double departures = StandardMetric.DEPARTURES.aggregate(repository,
							entity, start, end, SUM);
					return departures / (end - start);
				}
				return ts.timeWeightedMean(0);
			} else {
				throw new IllegalArgumentException();
			}
		}
		
		@Override
		public TimeSeries retrieve(IMonitoringRepository repository,
				ModelEntity entity, double start, double end) {
			TimeSeries series = repository.getData(this, entity);
			return series.subset(start, end);
		}
		
		@Override
		public boolean hasData(IMonitoringRepository repository,
				ModelEntity entity, double aggregationInterval) {
			if (repository.containsData(this, entity, aggregationInterval)) {
				return true;
			}
			return DEPARTURES.hasData(repository, entity, aggregationInterval);
		}

	},
	RESPONSE_TIME("reponse time", NONE, AVERAGE, MINIMUM, MAXIMUM) {

		@Override
		public double aggregate(IMonitoringRepository repository, ModelEntity entity, double start, double end, Aggregation func) {
			TimeSeries series = retrieve(repository, entity, start, end);
			switch (func) {
			case AVERAGE: {
				double interval = repository.getAggregationInterval(this,
						entity);
				if (interval > 0) {
					TimeSeries weights = THROUGHPUT.retrieve(repository, entity, start, end);
					if (weights.isEmpty()) {
						weights = DEPARTURES.retrieve(repository, entity, start, end);
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
		public TimeSeries retrieve(IMonitoringRepository repository,
				ModelEntity entity, double start, double end) {
			TimeSeries ts = repository.getData(this, entity);
			if (ts.isEmpty()) {
				TimeSeries arriv = repository.getData(ARRIVALS, entity);
				TimeSeries departures = repository.getData(DEPARTURES, entity);
				if (!arriv.isEmpty() && !departures.isEmpty()) {
					ts = new TimeSeries(departures.getTime(), departures.getTime().minus(arriv.getTime()));
					return ts.subset(start, end);
				}				
			}
			return ts.subset(start, end);
		}
		
		@Override
		public boolean hasData(IMonitoringRepository repository,
				ModelEntity entity, double aggregationInterval) {
			if (repository.containsData(this, entity, aggregationInterval)) {
				return true;
			}
			return DEPARTURES.hasData(repository, entity, 0.0) && ARRIVALS.hasData(repository, entity, 0.0);
		}

	},
	QUEUE_LENGTH_SEEN_ON_ARRIVAL("queue length seen on arrival", NONE, AVERAGE, MINIMUM, MAXIMUM) {

		@Override
		public TimeSeries retrieve(IMonitoringRepository repository,
				ModelEntity entity, double start, double end) {
			return repository.getData(this, entity).subset(start, end);
		}

		@Override
		public double aggregate(IMonitoringRepository repository,
				ModelEntity entity, double start, double end, Aggregation func) {
			TimeSeries series = retrieve(repository, entity, start, end);
			switch(func) {
			case AVERAGE: {
				double interval = repository.getAggregationInterval(this,
						entity);
				if (interval > 0) {
					TimeSeries weights = THROUGHPUT.retrieve(repository, entity, start, end);
					if (weights.isEmpty()) {
						weights = DEPARTURES.retrieve(repository, entity, start, end);
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
		public boolean hasData(IMonitoringRepository repository,
				ModelEntity entity, double aggregationInterval) {
			return repository.containsData(this, entity, aggregationInterval);
		}
		
	};

	private final UUID id;
	private String displayName;
	private final EnumSet<Aggregation> supportedAggregations;

	private StandardMetric(String displayName, Aggregation... aggregations) {
		id = UUID.randomUUID();
		this.displayName = displayName;
		supportedAggregations = EnumSet.copyOf(Arrays.asList(aggregations));
	}
	
	@Override
	public String getDisplayName() {
		return displayName;
	}
	
	@Override
	public UUID getId() {
		return id;
	}

	public boolean isAggregationSupported(Aggregation aggregation) {
		return supportedAggregations.contains(aggregation);
	}
}
