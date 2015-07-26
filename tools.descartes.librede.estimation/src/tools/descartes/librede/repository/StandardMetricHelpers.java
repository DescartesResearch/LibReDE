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
import java.util.Map;

import org.apache.log4j.Logger;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.repository.TimeSeries.Interpolation;
import tools.descartes.librede.repository.handlers.AverageResponseTimeAggregationHandler;
import tools.descartes.librede.repository.handlers.DefaultAggregationHandler;
import tools.descartes.librede.repository.handlers.DeriveArrivalsHandler;
import tools.descartes.librede.repository.handlers.DeriveConstantRate;
import tools.descartes.librede.repository.handlers.DeriveDeparturesHandler;
import tools.descartes.librede.repository.handlers.DeriveDiffHandler;
import tools.descartes.librede.repository.handlers.DeriveResponeTimeHandler;
import tools.descartes.librede.repository.handlers.DeriveUtilizationHandler;
import tools.descartes.librede.repository.handlers.RequestRateAggregationHandler;
import tools.descartes.librede.repository.handlers.ThroughputWeightedAggregationHandler;
import tools.descartes.librede.repository.handlers.TimeWeightedAggregationHandler;
import tools.descartes.librede.units.Dimension;
import tools.descartes.librede.units.Ratio;
import tools.descartes.librede.units.RequestCount;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;

public class StandardMetricHelpers {
	
	static final Logger log = Logger.getLogger(StandardMetricHelpers.class);
	
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
	
//	public static void init() {
//		
//		////////////////////////////////////////////////////////////////
//		// Arrival rate
//		////////////////////////////////////////////////////////////////		
//		AggregationRule.aggregate(StandardMetrics.ARRIVAL_RATE, Aggregation.AVERAGE).from(Aggregation.AVERAGE)
//			.build(new TimeWeightedAggregationHandler<RequestRate>(StandardMetrics.ARRIVAL_RATE));
//		// TODO: convert to derivation rule?
//		//AggregationRule.aggregate(StandardMetrics.ARRIVAL_RATE, Aggregation.AVERAGE).from(StandardMetrics.ARRIVALS, Aggregation.SUM).build( new RequestRateAggregationHandler(StandardMetrics.ARRIVALS));
//				
//		////////////////////////////////////////////////////////////////
//		// Arrivals
//		////////////////////////////////////////////////////////////////
//		AggregationRule.aggregate(StandardMetrics.ARRIVALS, Aggregation.SUM).from(Aggregation.NONE).build();
//		AggregationRule.aggregate(StandardMetrics.ARRIVALS, Aggregation.SUM).from(Aggregation.SUM).build();
//		AggregationRule.aggregate(StandardMetrics.ARRIVALS, Aggregation.MINIMUM).from(Aggregation.NONE).build();
//		AggregationRule.aggregate(StandardMetrics.ARRIVALS, Aggregation.MAXIMUM).from(Aggregation.NONE).build();
//		AggregationRule.aggregate(StandardMetrics.ARRIVALS, Aggregation.CUMULATIVE_SUM).from(Aggregation.NONE).build();
//		DerivationRule.derive(StandardMetrics.ARRIVALS, Aggregation.CUMULATIVE_SUM).from(Aggregation.SUM).build(new DeriveDiffHandler<RequestCount>(StandardMetrics.ARRIVALS));
//		DerivationRule.derive(StandardMetrics.ARRIVALS).from(StandardMetrics.RESPONSE_TIME).build(new DeriveArrivalsHandler());
//		
//		////////////////////////////////////////////////////////////////
//		// Busy time
//		////////////////////////////////////////////////////////////////
//		AggregationRule.aggregate(StandardMetrics.BUSY_TIME, Aggregation.SUM).from(Aggregation.SUM).build();
//		DerivationRule.derive(StandardMetrics.BUSY_TIME, Aggregation.CUMULATIVE_SUM).from(Aggregation.SUM).build(new DeriveDiffHandler<Time>(StandardMetrics.BUSY_TIME));
//
//		////////////////////////////////////////////////////////////////
//		// Departures
//		////////////////////////////////////////////////////////////////
//		AggregationRule.aggregate(StandardMetrics.DEPARTURES, Aggregation.SUM).from(Aggregation.NONE).build();
//		AggregationRule.aggregate(StandardMetrics.DEPARTURES, Aggregation.SUM).from(Aggregation.SUM).build();
//		AggregationRule.aggregate(StandardMetrics.DEPARTURES, Aggregation.MINIMUM).from(Aggregation.NONE).build();
//		AggregationRule.aggregate(StandardMetrics.DEPARTURES, Aggregation.MAXIMUM).from(Aggregation.NONE).build();
//		AggregationRule.aggregate(StandardMetrics.DEPARTURES, Aggregation.CUMULATIVE_SUM).from(Aggregation.NONE).build();
//		DerivationRule.derive(StandardMetrics.DEPARTURES, Aggregation.CUMULATIVE_SUM).from(Aggregation.SUM).build(new DeriveDiffHandler<RequestCount>(StandardMetrics.DEPARTURES));
//		DerivationRule.derive(StandardMetrics.DEPARTURES).from(StandardMetrics.RESPONSE_TIME).build(new DeriveDeparturesHandler());
//
//		////////////////////////////////////////////////////////////////
//		// Idle time
//		////////////////////////////////////////////////////////////////
//		AggregationRule.aggregate(StandardMetrics.IDLE_TIME, Aggregation.SUM).from(Aggregation.SUM).build();
//		DerivationRule.derive(StandardMetrics.IDLE_TIME, Aggregation.CUMULATIVE_SUM).from(Aggregation.SUM).build(new DeriveDiffHandler<Time>(StandardMetrics.IDLE_TIME));
//		
//		////////////////////////////////////////////////////////////////
//		// Queue length seen on arrival
//		////////////////////////////////////////////////////////////////
//		AggregationRule.aggregate(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL, Aggregation.AVERAGE).from(Aggregation.NONE).build();
//		AggregationRule.aggregate(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL, Aggregation.MINIMUM).from(Aggregation.NONE).build();
//		AggregationRule.aggregate(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL, Aggregation.MAXIMUM).from(Aggregation.NONE).build();
//		AggregationRule.aggregate(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL, Aggregation.AVERAGE)
//			.from(Aggregation.AVERAGE)
//			.with(StandardMetrics.THROUGHPUT, Aggregation.AVERAGE)
//			.build(new ThroughputWeightedAggregationHandler<RequestCount>(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL));
//		
//		////////////////////////////////////////////////////////////////
//		// Response time
//		////////////////////////////////////////////////////////////////
//		AggregationRule.aggregate(StandardMetrics.RESPONSE_TIME, Aggregation.AVERAGE).from(Aggregation.NONE).build();
//		AggregationRule.aggregate(StandardMetrics.RESPONSE_TIME, Aggregation.SUM).from(Aggregation.NONE).build();
//		AggregationRule.aggregate(StandardMetrics.RESPONSE_TIME, Aggregation.SUM).from(Aggregation.SUM).build();
//		AggregationRule.aggregate(StandardMetrics.RESPONSE_TIME, Aggregation.MINIMUM).from(Aggregation.NONE).build();
//		AggregationRule.aggregate(StandardMetrics.RESPONSE_TIME, Aggregation.MAXIMUM).from(Aggregation.NONE).build();
//		AggregationRule.aggregate(StandardMetrics.RESPONSE_TIME, Aggregation.CUMULATIVE_SUM).from(Aggregation.NONE).build();
//		AggregationRule.aggregate(StandardMetrics.RESPONSE_TIME, Aggregation.AVERAGE)
//			.from(Aggregation.AVERAGE)
//			.with(StandardMetrics.THROUGHPUT, Aggregation.AVERAGE)
//			.build(new ThroughputWeightedAggregationHandler<Time>(StandardMetrics.RESPONSE_TIME));
//		AggregationRule.aggregate(StandardMetrics.RESPONSE_TIME, Aggregation.AVERAGE)
//			.from(Aggregation.SUM)
//			.with(StandardMetrics.DEPARTURES, Aggregation.SUM)
//			.build(new AverageResponseTimeAggregationHandler());
//		DerivationRule.derive(StandardMetrics.RESPONSE_TIME, Aggregation.CUMULATIVE_SUM).from(Aggregation.SUM).build(new DeriveDiffHandler<Time>(StandardMetrics.RESPONSE_TIME));
//		DerivationRule.derive(StandardMetrics.RESPONSE_TIME, Aggregation.NONE)
//			.from(StandardMetrics.ARRIVALS)
//			.from(StandardMetrics.DEPARTURES)
//			.build(new DeriveResponeTimeHandler());
//		
//		////////////////////////////////////////////////////////////////
//		// Throughput
//		////////////////////////////////////////////////////////////////
//		AggregationRule.aggregate(StandardMetrics.THROUGHPUT, Aggregation.AVERAGE)
//			.from(Aggregation.AVERAGE)
//			.build(new TimeWeightedAggregationHandler<RequestRate>(StandardMetrics.ARRIVAL_RATE));			
//		// TODO: convert to derivation rule?
//		//AggregationRule.aggregate(StandardMetrics.THROUGHPUT, Aggregation.AVERAGE).from(StandardMetrics.ARRIVALS, Aggregation.SUM).build( new RequestRateAggregationHandler(StandardMetrics.THROUGHPUT));
//				
//		////////////////////////////////////////////////////////////////
//		// Utilization
//		////////////////////////////////////////////////////////////////
//		AggregationRule.aggregate(StandardMetrics.UTILIZATION, Aggregation.AVERAGE)
//			.from(Aggregation.AVERAGE)
//			.build();
//		DerivationRule.derive(StandardMetrics.UTILIZATION, Aggregation.AVERAGE)
//			.from(StandardMetrics.BUSY_TIME, Aggregation.SUM)
//			.from(StandardMetrics.IDLE_TIME, Aggregation.SUM)
//			.build(new DeriveUtilizationHandler());
//	}
	
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
