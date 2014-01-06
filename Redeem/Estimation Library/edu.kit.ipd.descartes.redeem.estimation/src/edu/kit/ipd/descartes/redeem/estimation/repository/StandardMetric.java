package edu.kit.ipd.descartes.redeem.estimation.repository;

import static edu.kit.ipd.descartes.linalg.LinAlg.ones;
import static edu.kit.ipd.descartes.redeem.estimation.repository.Aggregation.AVERAGE;
import static edu.kit.ipd.descartes.redeem.estimation.repository.Aggregation.MAXIMUM;
import static edu.kit.ipd.descartes.redeem.estimation.repository.Aggregation.MINIMUM;
import static edu.kit.ipd.descartes.redeem.estimation.repository.Aggregation.NONE;
import static edu.kit.ipd.descartes.redeem.estimation.repository.Aggregation.SUM;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.UUID;

import edu.kit.ipd.descartes.redeem.estimation.repository.TimeSeries.Interpolation;
import edu.kit.ipd.descartes.redeem.estimation.workload.IModelEntity;

public enum StandardMetric implements IMetric {

	UTILIZATION(AVERAGE) {	

		@Override
		public double aggregate(IMonitoringRepository repository, IModelEntity entity, double start, double end, Aggregation func) {
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
				IModelEntity entity, double start, double end) {
			TimeSeries series = repository.getData(this, entity);
			return series.subset(start, end);
		}
		
		@Override
		public boolean hasData(IMonitoringRepository repository,
				IModelEntity entity, double aggregationInterval) {
			return repository.containsData(this, entity, aggregationInterval);
		}

	},

	ARRIVALS(NONE, SUM, MINIMUM, MAXIMUM) {
		
		@Override
		public TimeSeries retrieve(IMonitoringRepository repository,
				IModelEntity entity, double start, double end) {
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
		public double aggregate(IMonitoringRepository repository, IModelEntity entity, double start, double end, Aggregation func) {
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
				IModelEntity entity, double aggregationInterval) {
			if (repository.containsData(this, entity, aggregationInterval)) {
				return true;
			}
			return repository.containsData(RESPONSE_TIME, entity, 0.0);
		}

	},
	DEPARTURES(NONE, SUM, MINIMUM, MAXIMUM) {
		
		@Override
		public TimeSeries retrieve(IMonitoringRepository repository,
				IModelEntity entity, double start, double end) {
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
		public double aggregate(IMonitoringRepository repository, IModelEntity entity, double start, double end, Aggregation func) {
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
				IModelEntity entity, double aggregationInterval) {
			if (repository.containsData(this, entity, aggregationInterval)) {
				return true;
			}
			return repository.containsData(RESPONSE_TIME, entity, 0.0);
		}

	},
	ARRIVAL_RATE(AVERAGE) {

		@Override
		public double aggregate(IMonitoringRepository repository, IModelEntity entity, double start, double end, Aggregation func) {
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
				IModelEntity entity, double start, double end) {
			TimeSeries series = repository.getData(this, entity);
			return series.subset(start, end);
		}
		
		@Override
		public boolean hasData(IMonitoringRepository repository,
				IModelEntity entity, double aggregationInterval) {
			if (repository.containsData(this, entity, aggregationInterval)) {
				return true;
			}
			return ARRIVALS.hasData(repository, entity, aggregationInterval);
		}
	},
	THROUGHPUT(AVERAGE) {

		@Override
		public double aggregate(IMonitoringRepository repository, IModelEntity entity, double start, double end, Aggregation func) {
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
				IModelEntity entity, double start, double end) {
			TimeSeries series = repository.getData(this, entity);
			return series.subset(start, end);
		}
		
		@Override
		public boolean hasData(IMonitoringRepository repository,
				IModelEntity entity, double aggregationInterval) {
			if (repository.containsData(this, entity, aggregationInterval)) {
				return true;
			}
			return DEPARTURES.hasData(repository, entity, aggregationInterval);
		}

	},
	RESPONSE_TIME(NONE, AVERAGE, MINIMUM, MAXIMUM) {

		@Override
		public double aggregate(IMonitoringRepository repository, IModelEntity entity, double start, double end, Aggregation func) {
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
				IModelEntity entity, double start, double end) {
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
				IModelEntity entity, double aggregationInterval) {
			if (repository.containsData(this, entity, aggregationInterval)) {
				return true;
			}
			return DEPARTURES.hasData(repository, entity, 0.0) && ARRIVALS.hasData(repository, entity, 0.0);
		}

	};

	private final UUID id;
	private final EnumSet<Aggregation> supportedAggregations;

	private StandardMetric(Aggregation... aggregations) {
		id = UUID.randomUUID();
		supportedAggregations = EnumSet.copyOf(Arrays.asList(aggregations));
	}
	
	@Override
	public UUID getId() {
		return id;
	}

	public boolean isAggregationSupported(Aggregation aggregation) {
		return supportedAggregations.contains(aggregation);
	}
}