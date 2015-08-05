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
import java.util.LinkedList;
import java.util.List;

import tools.descartes.librede.configuration.ExternalCall;
import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.Task;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.repository.IMetricAdapter;
import tools.descartes.librede.repository.TimeSeries.Interpolation;
import tools.descartes.librede.repository.handlers.AverageResponseTimeAggregationHandler;
import tools.descartes.librede.repository.handlers.DefaultAggregationHandler;
import tools.descartes.librede.repository.handlers.DeriveDiffHandler;
import tools.descartes.librede.repository.handlers.DeriveIdentityHandler;
import tools.descartes.librede.repository.handlers.DeriveResidenceTimeFromExternalCalls;
import tools.descartes.librede.repository.handlers.ThroughputWeightedAggregationHandler;
import tools.descartes.librede.repository.rules.Rule;
import tools.descartes.librede.repository.rules.RulePrecondition;
import tools.descartes.librede.repository.rules.RuleScope;
import tools.descartes.librede.units.Time;

public class ResidenceTimeAdapter implements IMetricAdapter<Time> {
	
	private static class NoExternalCallPrecondition implements RulePrecondition {
		@Override
		public boolean check(ModelEntity entity) {
			if (entity instanceof Service) {
				List<Task> tasks = ((Service)entity).getTasks();
				for (Task t : tasks) {
					if (t instanceof ExternalCall) {
						return false;
					}
				}
				return true;
			}
			return false;
		}		
	}
	
	private static class ExternalCallPrecondition implements RulePrecondition {
		@Override
		public boolean check(ModelEntity entity) {
			if (entity instanceof Service) {
				List<Task> tasks = ((Service)entity).getTasks();
				for (Task t : tasks) {
					if (t instanceof ExternalCall) {
						return true;
					}
				}
				return false;
			}
			return false;
		}		
	}
	
	private static class ExternalCallScope implements RuleScope {
		@Override
		public List<ModelEntity> getScopeSet(ModelEntity entity) {
			List<ModelEntity> entities = new LinkedList<>();
			entities.add(entity);

			List<Task> tasks = ((Service)entity).getTasks();
			for (Task t : tasks) {
				if (t instanceof ExternalCall) {
					ExternalCall call = (ExternalCall)t;
					if (call.getCalledService() != null) {
						entities.add(call);
					}
				}
			}
			return entities;
		}

		@Override
		public List<ModelEntity> getNotificationSet(ModelEntity base) {
			List<ModelEntity> entities = new LinkedList<>();
			entities.add(base);
			List<ExternalCall> callees = ((Service)base).getCallees();
			for (ExternalCall t : callees) {
				entities.add(t.getService());
			}
			return entities;
		}		
	}
	
	@Override
	public Interpolation getInterpolation() {
		return Interpolation.LINEAR;
	}

	@Override
	public List<Rule<Time>> getDerivationRules() {
		return Arrays.asList(
				/////////////////////////////////
				// residence time/NONE
				/////////////////////////////////
				Rule.rule(StandardMetrics.RESIDENCE_TIME, Aggregation.NONE)
					.requiring(StandardMetrics.RESPONSE_TIME, Aggregation.NONE)
					.check(new NoExternalCallPrecondition())
					.priority(100)
					.build(new DeriveIdentityHandler<>(StandardMetrics.RESPONSE_TIME)),
				/////////////////////////////////
				// residence time/AVERAGE
				/////////////////////////////////
				Rule.rule(StandardMetrics.RESIDENCE_TIME, Aggregation.AVERAGE)
					.requiring(Aggregation.NONE)
					.build(new DefaultAggregationHandler<Time>(Aggregation.NONE)),
				Rule.rule(StandardMetrics.RESIDENCE_TIME, Aggregation.AVERAGE)
					.requiring(Aggregation.AVERAGE)
					.requiring(StandardMetrics.THROUGHPUT, Aggregation.AVERAGE)
					.priority(20)
					.build(new ThroughputWeightedAggregationHandler<Time>()),
				Rule.rule(StandardMetrics.RESIDENCE_TIME, Aggregation.AVERAGE)
					.requiring(Aggregation.SUM)
					.requiring(StandardMetrics.DEPARTURES, Aggregation.SUM)
					.priority(10)
					.build(new AverageResponseTimeAggregationHandler()),
				Rule.rule(StandardMetrics.RESIDENCE_TIME, Aggregation.AVERAGE)
					.requiring(StandardMetrics.RESPONSE_TIME, Aggregation.AVERAGE)
					.requiring(StandardMetrics.VISITS, Aggregation.AVERAGE)
					.check(new ExternalCallPrecondition())
					.scope(new ExternalCallScope())
					.build(new DeriveResidenceTimeFromExternalCalls()),
				Rule.rule(StandardMetrics.RESIDENCE_TIME, Aggregation.AVERAGE)
					.requiring(StandardMetrics.RESPONSE_TIME, Aggregation.AVERAGE)
					.check(new NoExternalCallPrecondition())
					.priority(100)
					.build(new DeriveIdentityHandler<>(StandardMetrics.RESPONSE_TIME)),
				/////////////////////////////////
				// residence time/SUM
				/////////////////////////////////
				Rule.rule(StandardMetrics.RESIDENCE_TIME, Aggregation.SUM)
					.requiring(Aggregation.NONE)
					.priority(0)
					.build(new DefaultAggregationHandler<Time>(Aggregation.NONE)),
				Rule.rule(StandardMetrics.RESIDENCE_TIME, Aggregation.SUM)
					.requiring(Aggregation.SUM)
					.build(new DefaultAggregationHandler<Time>(Aggregation.SUM)),
				Rule.rule(StandardMetrics.RESIDENCE_TIME, Aggregation.SUM)
					.requiring(Aggregation.CUMULATIVE_SUM)
					.build(new DeriveDiffHandler<Time>()),
				Rule.rule(StandardMetrics.RESIDENCE_TIME, Aggregation.SUM)
					.requiring(StandardMetrics.RESPONSE_TIME, Aggregation.SUM)
					.check(new NoExternalCallPrecondition())
					.priority(100)
					.build(new DeriveIdentityHandler<>(StandardMetrics.RESPONSE_TIME)),
				/////////////////////////////////
				// residence time/MINIMUM
				/////////////////////////////////
				Rule.rule(StandardMetrics.RESIDENCE_TIME, Aggregation.MINIMUM)
					.requiring(Aggregation.NONE)
					.build(new DefaultAggregationHandler<Time>(Aggregation.NONE)),
				Rule.rule(StandardMetrics.RESIDENCE_TIME, Aggregation.MINIMUM)
					.requiring(StandardMetrics.RESPONSE_TIME, Aggregation.MINIMUM)
					.check(new NoExternalCallPrecondition())
					.priority(100)
					.build(new DeriveIdentityHandler<>(StandardMetrics.RESPONSE_TIME)),
				/////////////////////////////////
				// residence time/MAXIMUM
				/////////////////////////////////
				Rule.rule(StandardMetrics.RESIDENCE_TIME, Aggregation.MAXIMUM)
					.requiring(Aggregation.NONE)
					.build(new DefaultAggregationHandler<Time>(Aggregation.NONE)),
				Rule.rule(StandardMetrics.RESIDENCE_TIME, Aggregation.MAXIMUM)
					.requiring(StandardMetrics.RESPONSE_TIME, Aggregation.MINIMUM)
					.check(new NoExternalCallPrecondition())
					.priority(100)
					.build(new DeriveIdentityHandler<>(StandardMetrics.RESPONSE_TIME)),
				/////////////////////////////////
				// residence time/CUMULATIVE_SUM
				/////////////////////////////////
				Rule.rule(StandardMetrics.RESIDENCE_TIME, Aggregation.CUMULATIVE_SUM)
					.requiring(Aggregation.NONE)
					.build(new DefaultAggregationHandler<Time>(Aggregation.NONE)),
				Rule.rule(StandardMetrics.RESIDENCE_TIME, Aggregation.CUMULATIVE_SUM)
					.requiring(StandardMetrics.RESPONSE_TIME, Aggregation.CUMULATIVE_SUM)
					.check(new NoExternalCallPrecondition())
					.priority(100)
					.build(new DeriveIdentityHandler<>(StandardMetrics.RESPONSE_TIME))		
				);
	}
}
