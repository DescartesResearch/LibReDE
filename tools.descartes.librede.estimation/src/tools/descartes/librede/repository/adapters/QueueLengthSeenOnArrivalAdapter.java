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
package tools.descartes.librede.repository.adapters;

import java.util.Arrays;
import java.util.List;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.repository.IMetricAdapter;
import tools.descartes.librede.repository.TimeSeries.Interpolation;
import tools.descartes.librede.repository.handlers.DefaultAggregationHandler;
import tools.descartes.librede.repository.handlers.DeriveQueueLengthHandler;
import tools.descartes.librede.repository.handlers.ThroughputWeightedAggregationHandler;
import tools.descartes.librede.repository.rules.DerivationRule;
import tools.descartes.librede.repository.rules.RulePrecondition;
import tools.descartes.librede.units.RequestCount;

public class QueueLengthSeenOnArrivalAdapter implements IMetricAdapter<RequestCount> {	
		
	@Override
	public Interpolation getInterpolation() {
		return Interpolation.LINEAR;
	}

	@Override
	public List<DerivationRule<RequestCount>> getDerivationRules() {
		return Arrays.asList(
				DerivationRule.rule(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL, Aggregation.AVERAGE)
					.requiring(Aggregation.NONE)
					.priority(10)
					.build(new DefaultAggregationHandler<RequestCount>(Aggregation.NONE)),
				DerivationRule.rule(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL, Aggregation.MINIMUM)
					.requiring(Aggregation.NONE)
					.build(new DefaultAggregationHandler<RequestCount>(Aggregation.NONE)),
				DerivationRule.rule(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL, Aggregation.MAXIMUM)
					.requiring(Aggregation.NONE)
					.build(new DefaultAggregationHandler<RequestCount>(Aggregation.NONE)),
				DerivationRule.rule(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL, Aggregation.AVERAGE)
					.requiring(Aggregation.AVERAGE)
					.requiring(StandardMetrics.THROUGHPUT, Aggregation.AVERAGE)
					.priority(0)
					.build(new ThroughputWeightedAggregationHandler<RequestCount>()),
				DerivationRule.rule(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL, Aggregation.AVERAGE)
					.requiring(StandardMetrics.UTILIZATION, Aggregation.AVERAGE)
					.check(new RulePrecondition() {						
						@Override
						public boolean check(ModelEntity entity) {
							if (!(entity instanceof Resource)) {
								return false;
							}
							Resource res = (Resource)entity;
							switch (res.getSchedulingStrategy()) {
							case PS:
							case UNKOWN:
							case IS:
								return true;
							case FCFS:
								// The derivation is only valid for single-class queues
								return (res.getAccessingServices().size() == 1);
							}
							return false;
						}
					})
					.priority(-10)
					.build(new DeriveQueueLengthHandler())
				);
	}

}