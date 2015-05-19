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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.repository.TimeSeries.Interpolation;
import tools.descartes.librede.units.Dimension;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Ratio;
import tools.descartes.librede.units.RequestCount;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;
import tools.descartes.librede.units.UnitsFactory;

public class StandardMetricHelpers {
	
	private static final Logger log = Logger.getLogger(StandardMetricHelpers.class);
	
	private static Map<Metric<?>, IMetricAdapter<?>> handlers = null;
	
	@SuppressWarnings("unchecked")
	public static <D extends Dimension> IMetricAdapter<D> createHandler(Metric<D> metric) {
		if (handlers == null) {
			handlers = new HashMap<Metric<?>, IMetricAdapter<?>>();
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
		return (IMetricAdapter<D>) handlers.get(metric);
	}
	
	public static abstract class BaseHandler<D extends Dimension> {

		private final List<? extends Aggregation> delegatedAggregations;
		private final List<? extends Metric<?>> delegatedMetrics;
		private final int length;
		
		public BaseHandler(Metric<?> delegatedMetric, Aggregation delegatedAggregation) {
			this.delegatedAggregations = Collections.singletonList(delegatedAggregation);
			this.delegatedMetrics = Collections.<Metric<?>>singletonList(delegatedMetric);
			this.length = 1;
		}
		
		public BaseHandler(List<? extends Metric<?>> delegatedMetrics, List<? extends Aggregation> delegatedAggregations) {
			if (delegatedMetrics.size() != delegatedAggregations.size()) {
				throw new IllegalArgumentException();
			}
			this.delegatedAggregations = delegatedAggregations;
			this.delegatedMetrics = delegatedMetrics;
			this.length = delegatedMetrics.size();
		}
		
		public Quantity<Time> getAggregationInterval(MemoryObservationRepository repository, Metric<D> metric,
				ModelEntity entity, Aggregation aggregation) {
			Quantity<Time> ret = repository.getAggregationInterval(delegatedMetrics.get(0), entity, delegatedAggregations.get(0));
			for (int i = 1; i < length; i++) {
				ret = max(ret, repository.getAggregationInterval(delegatedMetrics.get(i), entity, delegatedAggregations.get(i)));
			}
			return ret;
		}

		public Quantity<Time> getStartTime(MemoryObservationRepository repository, Metric<D> metric, ModelEntity entity,
				Aggregation aggregation) {
			Quantity<Time> ret = repository.getMonitoringStartTime(delegatedMetrics.get(0), entity, delegatedAggregations.get(0));
			for (int i = 1; i < length; i++) {
				ret = max(ret, repository.getMonitoringStartTime(delegatedMetrics.get(i), entity, delegatedAggregations.get(i)));
			}
			return ret;
		}

		public Quantity<Time> getEndTime(MemoryObservationRepository repository, Metric<D> metric, ModelEntity entity,
				Aggregation aggregation) {
			Quantity<Time> ret = repository.getMonitoringEndTime(delegatedMetrics.get(0), entity, delegatedAggregations.get(0));
			for (int i = 1; i < length; i++) {
				ret = min(ret, repository.getMonitoringEndTime(delegatedMetrics.get(i), entity, delegatedAggregations.get(i)));
			}
			return ret;
		}
		
		private Quantity<Time> min(Quantity<Time> t1, Quantity<Time> t2) {
			return (t1.getValue(Time.SECONDS) < t2.getValue(Time.SECONDS)) ? t1 : t2;
		}
		
		private Quantity<Time> max(Quantity<Time> t1, Quantity<Time> t2) {
			return (t1.getValue(Time.SECONDS) > t2.getValue(Time.SECONDS)) ? t1 : t2;
		}
	}
	
	public static abstract class BaseAggregationHandler<D extends Dimension> extends BaseHandler<D> implements IMetricAggregationHandler<D> {

		public BaseAggregationHandler(Metric<?> delegatedMetric, Aggregation delegatedAggregation) {
			super(delegatedMetric, delegatedAggregation);
		}

		public BaseAggregationHandler(List<? extends Metric<?>> delegatedMetrics, List<? extends Aggregation> delegatedAggregations) {
			super(delegatedMetrics, delegatedAggregations);
		}		
	}
	
	public static abstract class BaseDerivationHandler<D extends Dimension> extends BaseHandler<D> implements IMetricDerivationHandler<D> {

		public BaseDerivationHandler(Metric<?> delegatedMetric, Aggregation delegatedAggregation) {
			super(delegatedMetric, delegatedAggregation);
		}

		public BaseDerivationHandler(List<? extends Metric<?>> delegatedMetrics, List<? extends Aggregation> delegatedAggregations) {
			super(delegatedMetrics, delegatedAggregations);
		}		
	}
	
	public static class DefaultAggregationHandler<D extends Dimension> extends BaseAggregationHandler<D> {
		// the aggregation level from which we calculate the sum
		private final Aggregation baseAggregation;

		public DefaultAggregationHandler(Metric<D> metric, Aggregation baseAggregation) {
			super(metric, baseAggregation);
			this.baseAggregation = baseAggregation;
		}
		
		@Override
		public double aggregate(IMonitoringRepository repository, Metric<D> metric, Unit<D> unit,
				ModelEntity entity, Aggregation aggregation, Quantity<Time> start, Quantity<Time> end) {
			if (log.isTraceEnabled()) {
				log.trace("Calculate " + aggregation.getLiteral() + " of " + metric.getName() + " for entity " + entity + " from " + baseAggregation.getLiteral() + ".");
			}
			TimeSeries ts = repository.select(metric, unit, entity, baseAggregation, start, end);
			if (!ts.isEmpty()) {
				switch(aggregation) {
				case AVERAGE:
					return ts.mean(0);
				case MAXIMUM:
					return ts.max(0);
				case MINIMUM:
					return ts.min(0);
				case SUM:
					return ts.sum(0);
				case CUMULATIVE_SUM:
					// TODO: implement cumsum for time series
					return Double.NaN;
				case NONE:
					throw new IllegalArgumentException();
				}				
			}			
			return Double.NaN;
		}

		
	}
	
	public static class TimeWeightedAggregationHandler<D extends Dimension> extends BaseAggregationHandler<D> {
		
		public TimeWeightedAggregationHandler(Metric<?> delegatedMetric) {
			super(delegatedMetric, Aggregation.AVERAGE);
		}

		@Override
		public double aggregate(IMonitoringRepository repository, Metric<D> metric, Unit<D> unit,
				ModelEntity entity, Aggregation aggregation, Quantity<Time> start, Quantity<Time> end) {
			if (aggregation == Aggregation.AVERAGE) {
				if (log.isTraceEnabled()) {
					log.trace("Calculate time-weighted " + aggregation.getLiteral() + " of " + metric.getName() + " for entity " + entity + ".");
				}
				TimeSeries ts = repository.select(metric, unit, entity, Aggregation.AVERAGE, start, end);
				return ts.timeWeightedMean(0);
			}
			throw new IllegalArgumentException();
		}
	}
	
	public static class ThroughputWeightedAggregationHandler<D extends Dimension> extends BaseAggregationHandler<D> {

		public ThroughputWeightedAggregationHandler(Metric<?> delegatedMetric) {
			super(Arrays.asList(delegatedMetric, StandardMetrics.THROUGHPUT), Arrays.asList(Aggregation.AVERAGE, Aggregation.AVERAGE));
		}

		@Override
		public double aggregate(IMonitoringRepository repository, Metric<D> metric, Unit<D> unit,
				ModelEntity entity, Aggregation aggregation, Quantity<Time> start, Quantity<Time> end) {
			if (aggregation == Aggregation.AVERAGE) {
				if (log.isTraceEnabled()) {
					log.trace("Calculate throughput-weighted " + aggregation.getLiteral() + " of " + metric.getName() + " for entity " + entity + ".");
				}
				TimeSeries ts = repository.select(metric, unit, entity, Aggregation.AVERAGE, start, end);
				TimeSeries weights = repository.select(StandardMetrics.THROUGHPUT, RequestRate.REQ_PER_SECOND, entity, Aggregation.AVERAGE, start, end);
				if (weights.isEmpty()) {
					throw new IllegalStateException();
				}
				return ts.timeWeightedMean(0, weights);
			}
			throw new IllegalArgumentException();
		}
	}
	
	public static class AverageResponseTimeAggregationHandler extends BaseAggregationHandler<Time> {

		public AverageResponseTimeAggregationHandler() {
			super(Arrays.asList(StandardMetrics.RESPONSE_TIME, StandardMetrics.DEPARTURES), Arrays.asList(Aggregation.SUM, Aggregation.AVERAGE));
		}

		@Override
		public double aggregate(IMonitoringRepository repository, Metric<Time> metric, Unit<Time> unit,
				ModelEntity entity, Aggregation aggregation, Quantity<Time> start, Quantity<Time> end) {
			if (aggregation == Aggregation.AVERAGE) {
				if (log.isTraceEnabled()) {
					log.trace("Calculate " + aggregation.getLiteral() + " of " + metric.getName() + " for entity " + entity + " from " + Aggregation.SUM.getLiteral() + ".");
				}
				double sumRt = repository.aggregate(StandardMetrics.RESPONSE_TIME, Time.SECONDS, entity, Aggregation.SUM, start, end);
				double requests = repository.aggregate(StandardMetrics.DEPARTURES, RequestCount.REQUESTS, entity, Aggregation.SUM, start, end);
				return Time.SECONDS.convertTo(sumRt / requests, unit);
			}
			throw new IllegalArgumentException();
		}
		
	}
	
	public static class RequestRateAggregationHandler extends BaseAggregationHandler<RequestRate> {
		
		private Metric<RequestCount> countMetric;
		
		public RequestRateAggregationHandler(Metric<RequestCount> countMetric) {
			super(countMetric, Aggregation.SUM);
			this.countMetric = countMetric;
		}

		@Override
		public double aggregate(IMonitoringRepository repository, Metric<RequestRate> metric, Unit<RequestRate> unit,
				ModelEntity entity, Aggregation aggregation, Quantity<Time> start, Quantity<Time> end) {
			if (aggregation == Aggregation.AVERAGE) {
				double sum = repository.aggregate(countMetric, RequestCount.REQUESTS,
						entity, Aggregation.SUM, start, end);
				return unit.convertFrom(sum / (end.minus(start).getValue(Time.SECONDS)), RequestRate.REQ_PER_SECOND);
			}
			throw new IllegalArgumentException();
		}		
	}
	
	public static class DeriveResponeTimeHandler extends BaseDerivationHandler<Time> {

		public DeriveResponeTimeHandler() {
			super(Arrays.asList(StandardMetrics.ARRIVALS, StandardMetrics.DEPARTURES), Arrays.asList(Aggregation.NONE, Aggregation.NONE));
		}

		@Override
		public TimeSeries derive(IMonitoringRepository repository, Metric<Time> metric, Unit<Time> unit,
				ModelEntity entity, Aggregation aggregation, Quantity<Time> start, Quantity<Time> end) {
			log.trace("Derive non-aggregated response times from arrivals and departures");
			if (aggregation == Aggregation.NONE) {
				TimeSeries arriv = repository.select(StandardMetrics.ARRIVALS, RequestCount.REQUESTS, entity, Aggregation.NONE, start, end);
				TimeSeries departures = repository.select(StandardMetrics.DEPARTURES, RequestCount.REQUESTS, entity, Aggregation.NONE, start, end);
				if (!arriv.isEmpty() && !departures.isEmpty()) {
					TimeSeries ts = new TimeSeries(departures.getTime(), departures.getTime().minus(arriv.getTime()));
					return UnitConverter.convertTo(ts, Time.SECONDS, unit);
				} else {
					log.trace("Could not find required arrivals and/or departures traces. Skip derivation of response times.");
					return TimeSeries.EMPTY;
				}
			}
			throw new IllegalArgumentException("Unexpected aggregation: " + aggregation);
		}
	}
	
	public static class DeriveArrivalsHandler extends BaseDerivationHandler<RequestCount> {

		public DeriveArrivalsHandler() {
			super(StandardMetrics.RESPONSE_TIME, Aggregation.NONE);
		}

		@Override
		public TimeSeries derive(IMonitoringRepository repository, Metric<RequestCount> metric,
				Unit<RequestCount> unit, ModelEntity entity, Aggregation aggregation, Quantity<Time> start,
				Quantity<Time> end) {
			log.trace("Derive non-aggregated arrivals from response times");
			if (aggregation == Aggregation.NONE) {
				TimeSeries respTime = repository.select(StandardMetrics.RESPONSE_TIME, Time.SECONDS, entity, Aggregation.NONE, start, end);
				if (!respTime.isEmpty()) {
					TimeSeries arrivals = new TimeSeries(respTime.getTime().minus(respTime.getData(0)), ones(respTime.getData(0).rows()));
					arrivals.setStartTime(start.getValue(Time.SECONDS));
					arrivals.setEndTime(end.getValue(Time.SECONDS));
					return UnitConverter.convertTo(arrivals, RequestCount.REQUESTS, unit);
				} else {
					log.trace("Could not find response time traces. Skip derivation of arrivals.");
					return TimeSeries.EMPTY;
				}
			}
			throw new IllegalArgumentException("Unexpected aggregation: " + aggregation);
		}
	}
	
	public static class DeriveDeparturesHandler extends BaseDerivationHandler<RequestCount> {
		
		public DeriveDeparturesHandler() {
			super(StandardMetrics.RESPONSE_TIME, Aggregation.NONE);
		}
		
		@Override
		public TimeSeries derive(IMonitoringRepository repository, Metric<RequestCount> metric,
				Unit<RequestCount> unit, ModelEntity entity, Aggregation aggregation, Quantity<Time> start,
				Quantity<Time> end) {
			log.trace("Derive non-aggregated departures from response times");
			if (aggregation == Aggregation.NONE) {
				TimeSeries respTime = repository.select(StandardMetrics.RESPONSE_TIME, Time.SECONDS, entity, Aggregation.NONE, start, end);
				if (!respTime.isEmpty()) {
					TimeSeries departures = new TimeSeries(respTime.getTime(), ones(respTime.getData(0).rows()));
					departures.setStartTime(start.getValue(Time.SECONDS));
					departures.setEndTime(end.getValue(Time.SECONDS));
					return UnitConverter.convertTo(departures, RequestCount.REQUESTS, unit);
				} else {
					log.trace("Could not find required response time traces. Skip derivation of departures.");
					return TimeSeries.EMPTY;
				}
			}
			throw new IllegalArgumentException("Unexpected aggregation: " + aggregation);
		}
	}
	
	public static class DeriveDiffHandler<D extends Dimension> extends BaseDerivationHandler<D> {
		
		public DeriveDiffHandler(Metric<D> delegatedMetric) {
			super(delegatedMetric, Aggregation.CUMULATIVE_SUM);
		}

		@Override
		public TimeSeries derive(IMonitoringRepository repository, Metric<D> metric,
				Unit<D> unit, ModelEntity entity, Aggregation aggregation, Quantity<Time> start,
				Quantity<Time> end) {
			log.trace("Derive sum from cumulative sum of " + metric.getName());
			if (aggregation == Aggregation.SUM) {
				TimeSeries cumSum = repository.select(metric, unit, entity, Aggregation.CUMULATIVE_SUM, start, end);
				if (!cumSum.isEmpty()) {
					return cumSum.diff();
				} else {
					log.trace("Could not find required traces. Skip derivation of sum.");
					return TimeSeries.EMPTY;
				}
			}
			throw new IllegalArgumentException("Unexpected aggregation: " + aggregation);
		}
	}
	
	public static class DeriveConstantRate implements IMetricAggregationHandler<RequestRate> {
		
		private static final Quantity<Time> ZERO_SECONDS = UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS);
		
		@Override
		public double aggregate(IMonitoringRepository repository, Metric<RequestRate> metric, Unit<RequestRate> unit,
				ModelEntity entity, Aggregation aggregation, Quantity<Time> start, Quantity<Time> end) {
			log.trace("Derive constant rate " + metric.getName());
			if (aggregation == Aggregation.AVERAGE) {
				return 1.0;
			}
			throw new IllegalArgumentException("Unexpected aggregation: " + aggregation);
		}

		@Override
		public Quantity<Time> getAggregationInterval(MemoryObservationRepository repository, Metric<RequestRate> metric,
				ModelEntity entity, Aggregation aggregation) {
			return ZERO_SECONDS;
		}

		@Override
		public Quantity<Time> getStartTime(MemoryObservationRepository repository, Metric<RequestRate> metric,
				ModelEntity entity, Aggregation aggregation) {
			return ZERO_SECONDS;
		}

		@Override
		public Quantity<Time> getEndTime(MemoryObservationRepository repository, Metric<RequestRate> metric,
				ModelEntity entity, Aggregation aggregation) {
			return repository.getCurrentTime();
		}
	}
	
	public static class DeriveUtilizationHandler extends BaseDerivationHandler<Ratio> {
		
		public DeriveUtilizationHandler() {
			super(Arrays.asList(StandardMetrics.BUSY_TIME, StandardMetrics.IDLE_TIME), Arrays.asList(Aggregation.SUM, Aggregation.SUM));
		}

		@Override
		public TimeSeries derive(IMonitoringRepository repository, Metric<Ratio> metric,
				Unit<Ratio> unit, ModelEntity entity, Aggregation aggregation, Quantity<Time> start,
				Quantity<Time> end) {
			log.trace("Derive average utilization from busy time and idle time.");
			if (aggregation == Aggregation.AVERAGE) {
				TimeSeries busy = repository.select(StandardMetrics.BUSY_TIME, Time.SECONDS, entity, Aggregation.SUM, start, end);
				TimeSeries idle = repository.select(StandardMetrics.IDLE_TIME, Time.SECONDS, entity, Aggregation.SUM, start, end);
				if (!busy.isEmpty() && !idle.isEmpty()) {
					// TODO: check that timestamps of idle and busy are equal
					TimeSeries util = new TimeSeries(idle.getTime(), busy.getData().arrayDividedBy(busy.getData().plus(idle.getData())));
					util.setStartTime(start.getValue(Time.SECONDS));
					util.setEndTime(end.getValue(Time.SECONDS));
					return UnitConverter.convertTo(util, Ratio.NONE, unit);
				} else {
					log.trace("Could not find required busy time and/or idle time traces. Skip derivation of average utilization.");
					return TimeSeries.EMPTY;
				}
			}
			throw new IllegalArgumentException("Unexpected aggregation: " + aggregation);
		}
	}
	
	private static class IdleTimeHelper implements IMetricAdapter<Time> {
		
		@Override
		public void registerHandlers(IMonitoringRepository repository, ModelEntity entity, Aggregation aggregation) {
			if (aggregation == Aggregation.CUMULATIVE_SUM) {
				if (!repository.exists(StandardMetrics.IDLE_TIME, entity, Aggregation.SUM)) {
					repository.addMetricDerivationHandler(StandardMetrics.IDLE_TIME, entity, Aggregation.SUM, new DeriveDiffHandler<Time>(StandardMetrics.IDLE_TIME));
				}
			}
			
			if (aggregation == Aggregation.SUM) {
				repository.addMetricAggregationHandler(StandardMetrics.BUSY_TIME, entity, Aggregation.SUM, new DefaultAggregationHandler<Time>(StandardMetrics.BUSY_TIME, Aggregation.SUM));				
				
				if (repository.exists(StandardMetrics.BUSY_TIME, entity, Aggregation.SUM) 
						&& !repository.exists(StandardMetrics.UTILIZATION, entity, Aggregation.AVERAGE)) {
					repository.addMetricDerivationHandler(StandardMetrics.UTILIZATION, entity, Aggregation.AVERAGE, new DeriveUtilizationHandler());
				}
			}
		}

		@Override
		public Interpolation getInterpolation() {
			return Interpolation.LINEAR;
		}

		@Override
		public void registerDefaultHandlers(IMonitoringRepository repository, ModelEntity entity) {
		}
	}
	
	private static class BusyTimeHelper implements IMetricAdapter<Time> {
	
		@Override
		public void registerHandlers(IMonitoringRepository repository, ModelEntity entity,
				Aggregation aggregation) {
			if (aggregation == Aggregation.CUMULATIVE_SUM) {
				if (!repository.exists(StandardMetrics.BUSY_TIME, entity, Aggregation.SUM)) {
					repository.addMetricDerivationHandler(StandardMetrics.BUSY_TIME, entity, Aggregation.SUM, new DeriveDiffHandler<Time>(StandardMetrics.BUSY_TIME));
				}
			}
			
			if (aggregation == Aggregation.SUM) {
				repository.addMetricAggregationHandler(StandardMetrics.IDLE_TIME, entity, Aggregation.SUM, new DefaultAggregationHandler<Time>(StandardMetrics.IDLE_TIME, Aggregation.SUM));				

				if (repository.exists(StandardMetrics.IDLE_TIME, entity, Aggregation.SUM) 
						&& !repository.exists(StandardMetrics.UTILIZATION, entity, Aggregation.AVERAGE)) {
					repository.addMetricDerivationHandler(StandardMetrics.UTILIZATION, entity, Aggregation.AVERAGE, new DeriveUtilizationHandler());
				}
			}
		}

		@Override
		public Interpolation getInterpolation() {
			return Interpolation.LINEAR;
		}

		@Override
		public void registerDefaultHandlers(IMonitoringRepository repository, ModelEntity entity) {
		}
	}

	private static class UtilizationHelper implements IMetricAdapter<Ratio> {

		@Override
		public void registerHandlers(IMonitoringRepository repository, ModelEntity entity,
				Aggregation aggregation) {
			if (aggregation == Aggregation.AVERAGE) {
				repository.addMetricAggregationHandler(StandardMetrics.UTILIZATION, entity, Aggregation.AVERAGE, new DefaultAggregationHandler<Ratio>(StandardMetrics.UTILIZATION, Aggregation.AVERAGE));
			}
		}

		@Override
		public Interpolation getInterpolation() {
			return Interpolation.PIECEWISE_CONSTANT;
		}

		@Override
		public void registerDefaultHandlers(IMonitoringRepository repository, ModelEntity entity) {
		}
	}

	private static class ArrivalsHelper implements IMetricAdapter<RequestCount> {
		@Override
		public void registerHandlers(IMonitoringRepository repository, ModelEntity entity,
				Aggregation aggregation) {
			if (aggregation == Aggregation.NONE) {
				repository.addMetricAggregationHandler(StandardMetrics.ARRIVALS, entity, Aggregation.SUM, new DefaultAggregationHandler<RequestCount>(StandardMetrics.ARRIVALS, Aggregation.NONE));
				repository.addMetricAggregationHandler(StandardMetrics.ARRIVALS, entity, Aggregation.MINIMUM, new DefaultAggregationHandler<RequestCount>(StandardMetrics.ARRIVALS, Aggregation.NONE));
				repository.addMetricAggregationHandler(StandardMetrics.ARRIVALS, entity, Aggregation.MAXIMUM, new DefaultAggregationHandler<RequestCount>(StandardMetrics.ARRIVALS, Aggregation.NONE));
				repository.addMetricAggregationHandler(StandardMetrics.ARRIVALS, entity, Aggregation.CUMULATIVE_SUM, new DefaultAggregationHandler<RequestCount>(StandardMetrics.ARRIVALS, Aggregation.NONE));
								
				if (repository.exists(StandardMetrics.DEPARTURES, entity, Aggregation.NONE) &&
						!repository.exists(StandardMetrics.RESPONSE_TIME, entity, Aggregation.NONE)) {
					repository.addMetricDerivationHandler(StandardMetrics.RESPONSE_TIME, entity, Aggregation.NONE, new DeriveResponeTimeHandler());
				}
			}
			
			if (aggregation == Aggregation.SUM) {
				repository.addMetricAggregationHandler(StandardMetrics.ARRIVALS, entity, Aggregation.SUM, new DefaultAggregationHandler<RequestCount>(StandardMetrics.ARRIVALS, Aggregation.SUM));
				repository.addMetricAggregationHandler(StandardMetrics.ARRIVAL_RATE, entity, Aggregation.AVERAGE, new RequestRateAggregationHandler(StandardMetrics.ARRIVALS));
			}
			
			if (aggregation == Aggregation.CUMULATIVE_SUM) {
				if (!repository.exists(StandardMetrics.ARRIVALS, entity, Aggregation.SUM)) {
					repository.addMetricDerivationHandler(StandardMetrics.ARRIVALS, entity, Aggregation.SUM, new DeriveDiffHandler<RequestCount>(StandardMetrics.ARRIVALS));
				}
			}
		}

		@Override
		public Interpolation getInterpolation() {
			return Interpolation.LINEAR;
		}

		@Override
		public void registerDefaultHandlers(IMonitoringRepository repository, ModelEntity entity) {		
		}
	}
	
	private static class DeparturesHelper implements IMetricAdapter<RequestCount> {
		@Override
		public void registerHandlers(IMonitoringRepository repository, ModelEntity entity,
				Aggregation aggregation) {
			if (aggregation == Aggregation.NONE) {
				repository.addMetricAggregationHandler(StandardMetrics.DEPARTURES, entity, Aggregation.SUM, new DefaultAggregationHandler<RequestCount>(StandardMetrics.DEPARTURES, Aggregation.NONE));
				repository.addMetricAggregationHandler(StandardMetrics.DEPARTURES, entity, Aggregation.MINIMUM, new DefaultAggregationHandler<RequestCount>(StandardMetrics.DEPARTURES, Aggregation.NONE));
				repository.addMetricAggregationHandler(StandardMetrics.DEPARTURES, entity, Aggregation.MAXIMUM, new DefaultAggregationHandler<RequestCount>(StandardMetrics.DEPARTURES, Aggregation.NONE));
				repository.addMetricAggregationHandler(StandardMetrics.DEPARTURES, entity, Aggregation.CUMULATIVE_SUM, new DefaultAggregationHandler<RequestCount>(StandardMetrics.DEPARTURES, Aggregation.NONE));
				
				if (repository.exists(StandardMetrics.ARRIVALS, entity, Aggregation.NONE) &&
						!repository.exists(StandardMetrics.RESPONSE_TIME, entity, Aggregation.NONE)) {
					repository.addMetricDerivationHandler(StandardMetrics.RESPONSE_TIME, entity, Aggregation.NONE, new DeriveResponeTimeHandler());
				}
			}
			
			if (aggregation == Aggregation.SUM) {
				repository.addMetricAggregationHandler(StandardMetrics.DEPARTURES, entity, Aggregation.SUM, new DefaultAggregationHandler<RequestCount>(StandardMetrics.DEPARTURES, Aggregation.SUM));
				repository.addMetricAggregationHandler(StandardMetrics.THROUGHPUT, entity, Aggregation.AVERAGE, new RequestRateAggregationHandler(StandardMetrics.DEPARTURES));
				
				if (repository.exists(StandardMetrics.RESPONSE_TIME, entity, Aggregation.SUM)) {
					repository.addMetricAggregationHandler(StandardMetrics.RESPONSE_TIME, entity, Aggregation.AVERAGE, new AverageResponseTimeAggregationHandler());
				}
			}
			
			if (aggregation == Aggregation.CUMULATIVE_SUM) {
				if (!repository.exists(StandardMetrics.DEPARTURES, entity, Aggregation.SUM)) {
					repository.addMetricDerivationHandler(StandardMetrics.DEPARTURES, entity, Aggregation.SUM, new DeriveDiffHandler<RequestCount>(StandardMetrics.DEPARTURES));
				}
			}
		}

		@Override
		public Interpolation getInterpolation() {
			return Interpolation.LINEAR;
		}

		@Override
		public void registerDefaultHandlers(IMonitoringRepository repository, ModelEntity entity) {	
		}
	}

	private static class ArrivalRateHelper implements IMetricAdapter<RequestRate> {
		@Override
		public void registerHandlers(IMonitoringRepository repository, ModelEntity entity,
				Aggregation aggregation) {
			repository.addMetricAggregationHandler(StandardMetrics.ARRIVAL_RATE, entity, Aggregation.AVERAGE, new TimeWeightedAggregationHandler<RequestRate>(StandardMetrics.ARRIVAL_RATE));	
		}

		@Override
		public Interpolation getInterpolation() {
			return Interpolation.LINEAR;
		}

		@Override
		public void registerDefaultHandlers(IMonitoringRepository repository, ModelEntity entity) {
			if ((entity instanceof Service) && ((Service)entity).isBackgroundService()) {
				repository.addMetricAggregationHandler(StandardMetrics.ARRIVAL_RATE, entity, Aggregation.AVERAGE, new DeriveConstantRate());
			}
		}
	}
	
	private static class ThroughputHelper implements IMetricAdapter<RequestRate> {		
		@Override
		public void registerHandlers(IMonitoringRepository repository, ModelEntity entity,
				Aggregation aggregation) {
			repository.addMetricAggregationHandler(StandardMetrics.THROUGHPUT, entity, Aggregation.AVERAGE, new TimeWeightedAggregationHandler<RequestRate>(StandardMetrics.THROUGHPUT));
			
			if (repository.exists(StandardMetrics.RESPONSE_TIME, entity, Aggregation.AVERAGE)) {
				repository.addMetricAggregationHandler(StandardMetrics.RESPONSE_TIME, entity, Aggregation.AVERAGE, new ThroughputWeightedAggregationHandler<Time>(StandardMetrics.RESPONSE_TIME));
			}
		}

		@Override
		public Interpolation getInterpolation() {
			return Interpolation.LINEAR;
		}

		@Override
		public void registerDefaultHandlers(IMonitoringRepository repository, ModelEntity entity) {
			if ((entity instanceof Service) && ((Service)entity).isBackgroundService()) {
				repository.addMetricAggregationHandler(StandardMetrics.THROUGHPUT, entity, Aggregation.AVERAGE, new DeriveConstantRate());
			}
		}
	}
	
	private static class ResponseTimeHelper implements IMetricAdapter<Time> {		
	
		@Override
		public void registerHandlers(IMonitoringRepository repository, ModelEntity entity,
				Aggregation aggregation) {
			if (aggregation == Aggregation.NONE) {
				repository.addMetricAggregationHandler(StandardMetrics.RESPONSE_TIME, entity, Aggregation.AVERAGE, new DefaultAggregationHandler<Time>(StandardMetrics.RESPONSE_TIME, Aggregation.NONE));
				repository.addMetricAggregationHandler(StandardMetrics.RESPONSE_TIME, entity, Aggregation.SUM, new DefaultAggregationHandler<Time>(StandardMetrics.RESPONSE_TIME, Aggregation.NONE));
				repository.addMetricAggregationHandler(StandardMetrics.RESPONSE_TIME, entity, Aggregation.MINIMUM, new DefaultAggregationHandler<Time>(StandardMetrics.RESPONSE_TIME, Aggregation.NONE));
				repository.addMetricAggregationHandler(StandardMetrics.RESPONSE_TIME, entity, Aggregation.MAXIMUM, new DefaultAggregationHandler<Time>(StandardMetrics.RESPONSE_TIME, Aggregation.NONE));
				repository.addMetricAggregationHandler(StandardMetrics.RESPONSE_TIME, entity, Aggregation.CUMULATIVE_SUM, new DefaultAggregationHandler<Time>(StandardMetrics.RESPONSE_TIME, Aggregation.NONE));				
				
				if (!repository.exists(StandardMetrics.ARRIVALS, entity, Aggregation.NONE)) {
					repository.addMetricDerivationHandler(StandardMetrics.ARRIVALS, entity, Aggregation.NONE, new DeriveArrivalsHandler());
				}				
				if (!repository.exists(StandardMetrics.DEPARTURES, entity, Aggregation.NONE)) {
					repository.addMetricDerivationHandler(StandardMetrics.DEPARTURES, entity, Aggregation.NONE, new DeriveDeparturesHandler());
				}
			}
			
			if (aggregation == Aggregation.AVERAGE) {
				if (repository.exists(StandardMetrics.THROUGHPUT, entity, Aggregation.AVERAGE)) {
					repository.addMetricAggregationHandler(StandardMetrics.RESPONSE_TIME, entity, Aggregation.AVERAGE, new ThroughputWeightedAggregationHandler<Time>(StandardMetrics.RESPONSE_TIME));
				}
			}
			
			if (aggregation == Aggregation.CUMULATIVE_SUM) {
				if (!repository.exists(StandardMetrics.RESPONSE_TIME, entity, Aggregation.SUM)) {
					repository.addMetricDerivationHandler(StandardMetrics.RESPONSE_TIME, entity, Aggregation.SUM, new DeriveDiffHandler<Time>(StandardMetrics.RESPONSE_TIME));
				}
			}
			
			if (aggregation == Aggregation.SUM) {
				repository.addMetricAggregationHandler(StandardMetrics.RESPONSE_TIME, entity, Aggregation.SUM, new DefaultAggregationHandler<Time>(StandardMetrics.RESPONSE_TIME, Aggregation.SUM) );
				if (repository.exists(StandardMetrics.DEPARTURES, entity, Aggregation.SUM)) {
					repository.addMetricAggregationHandler(StandardMetrics.RESPONSE_TIME, entity, Aggregation.AVERAGE, new AverageResponseTimeAggregationHandler());
				}
			}
		}

		@Override
		public Interpolation getInterpolation() {
			return Interpolation.LINEAR;
		}

		@Override
		public void registerDefaultHandlers(IMonitoringRepository repository, ModelEntity entity) {
		}
	}
	
	private static class QueueLengthSeenOnArrivalHelper implements IMetricAdapter<RequestCount> {
		@Override
		public void registerHandlers(IMonitoringRepository repository, ModelEntity entity,
				Aggregation aggregation) {
			if (aggregation == Aggregation.NONE) {
				repository.addMetricAggregationHandler(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL, entity, Aggregation.AVERAGE, new DefaultAggregationHandler<RequestCount>(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL, Aggregation.NONE));
				repository.addMetricAggregationHandler(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL, entity, Aggregation.MINIMUM, new DefaultAggregationHandler<RequestCount>(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL, Aggregation.NONE));
				repository.addMetricAggregationHandler(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL, entity, Aggregation.MAXIMUM, new DefaultAggregationHandler<RequestCount>(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL, Aggregation.NONE));
			}
			
			if (aggregation == Aggregation.AVERAGE) {
				if (repository.exists(StandardMetrics.THROUGHPUT, entity, Aggregation.AVERAGE)) {
					repository.addMetricAggregationHandler(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL, entity, Aggregation.AVERAGE, new ThroughputWeightedAggregationHandler<RequestCount>(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL));
				}
			}			
		}

		@Override
		public Interpolation getInterpolation() {
			return Interpolation.LINEAR;
		}

		@Override
		public void registerDefaultHandlers(IMonitoringRepository repository, ModelEntity entity) {
		}
	};

	private StandardMetricHelpers() {
	}
}
