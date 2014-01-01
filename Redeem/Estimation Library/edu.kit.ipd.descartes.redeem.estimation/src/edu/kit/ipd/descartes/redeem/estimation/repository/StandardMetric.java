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
		public double aggregate(TimeSeries series, Aggregation func) {
			if (isAggregationSupported(func)) {
				series.setInterpolationMethod(Interpolation.PIECEWISE_CONSTANT);
				return series.timeWeightedMean();
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
					ts = new TimeSeries(resp.getTime().minus(resp.getData()), ones(resp.getData().rows()));
					repository.setData(ARRIVALS, entity, ts);
					return ts.subset(start, end);
				}
			}
			return ts.subset(start, end);
		}

		@Override
		public double aggregate(TimeSeries series, Aggregation func) {
			series.setInterpolationMethod(Interpolation.LINEAR);
			switch(func) {
			case SUM: {
				return series.sum();
			}
			case MINIMUM: {
				return series.min();
			}
			case MAXIMUM: {
				return series.max();
			}
			default:
				throw new IllegalArgumentException();
			}
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
					ts = new TimeSeries(resp.getTime(), ones(resp.getData().rows()));
					repository.setData(DEPARTURES, entity, ts);
					return ts.subset(start, end);
				}
			}		
			return ts.subset(start, end);
		}

		@Override
		public double aggregate(TimeSeries series, Aggregation func) {
			series.setInterpolationMethod(Interpolation.LINEAR);
			switch(func) {
			case SUM: {
				return series.sum();
			}
			case MINIMUM: {
				return series.min();
			}
			case MAXIMUM: {
				return series.max();
			}
			default:
				throw new IllegalArgumentException();
			}
		}

	},
	ARRIVAL_RATE(AVERAGE) {

		@Override
		public double aggregate(TimeSeries series, Aggregation func) {
			if (isAggregationSupported(func)) {
				TimeSeries ts = repository.getData(this, entity);
				if (ts.isEmpty()) {
					double arrivals = StandardMetric.ARRIVALS.aggregate(repository,
							entity, start, end, SUM);
					return arrivals / (end - start);
				}
				return ts.timeWeightedMean(start, end);
			} else {
				throw new IllegalArgumentException();
			}
		}

		@Override
		public TimeSeries retrieve(IMonitoringRepository repository,
				IModelEntity entity, double start, double end) {
			throw new AssertionError();
		}
	},
	THROUGHPUT(AVERAGE) {

		@Override
		public double aggregate(TimeSeries series, Aggregation func) {
			if (isAggregationSupported(func)) {
				TimeSeries ts = repository.getData(this, entity);
				if (ts.isEmpty()) {
					double departures = StandardMetric.DEPARTURES.aggregate(repository,
							entity, start, end, SUM);
					return departures / (end - start);
				}
				return ts.timeWeightedMean(start, end);
			} else {
				throw new IllegalArgumentException();
			}
		}
		
		@Override
		public TimeSeries retrieve(IMonitoringRepository repository,
				IModelEntity entity, double start, double end) {
			throw new AssertionError();
		}

	},
	RESPONSE_TIME(NONE, AVERAGE, MINIMUM, MAXIMUM) {

		@Override
		public double aggregate(TimeSeries series, Aggregation func) {
			switch (func) {
			case AVERAGE: {
				TimeSeries ts = repository.getData(this, entity);
				double interval = repository.getAggregationInterval(this,
						entity);
				if (interval > 0) {
					TimeSeries weights = THROUGHPUT.retrieveAll(repository, entity);
					if (weights.isEmpty()) {
						weights = DEPARTURES.retrieveAll(repository, entity);
					}
					if (weights.isEmpty()) {
						throw new IllegalStateException();
					}
					return ts.timeWeightedMean(start, end, weights);
				} else {
					return ts.mean(start, end);
				}
			}
			case MAXIMUM: {
				TimeSeries ts = repository.getData(this, entity);
				return series.max();
			}
			case MINIMUM: {
				return series.min();
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
					repository.setData(this, entity, ts);
					return ts.subset(start, end);
				}				
			}
			return ts.subset(start, end);
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
