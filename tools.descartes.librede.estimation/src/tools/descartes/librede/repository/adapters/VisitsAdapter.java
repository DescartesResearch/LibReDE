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
import java.util.Collections;
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
import tools.descartes.librede.repository.handlers.DefaultAggregationHandler;
import tools.descartes.librede.repository.handlers.DeriveVisitCountHandler;
import tools.descartes.librede.repository.handlers.TimeWeightedAggregationHandler;
import tools.descartes.librede.repository.rules.DerivationRule;
import tools.descartes.librede.repository.rules.RulePrecondition;
import tools.descartes.librede.repository.rules.RuleScope;
import tools.descartes.librede.units.RequestCount;

public class VisitsAdapter implements IMetricAdapter<RequestCount> {

	@Override
	public Interpolation getInterpolation() {
		return Interpolation.LINEAR;
	}

	@Override
	public List<DerivationRule<RequestCount>> getDerivationRules() {
		return Arrays.asList(
				DerivationRule.derive(StandardMetrics.VISITS, Aggregation.AVERAGE)
					.requiring(Aggregation.NONE)
					.priority(10)
					.build(new DefaultAggregationHandler<>(StandardMetrics.VISITS, Aggregation.NONE)),
				DerivationRule.derive(StandardMetrics.VISITS, Aggregation.AVERAGE)
					.requiring(Aggregation.AVERAGE)
					.priority(20)
					.build(new TimeWeightedAggregationHandler<RequestCount>(StandardMetrics.VISITS)),
				DerivationRule.derive(StandardMetrics.VISITS, Aggregation.AVERAGE)
					.requiring(StandardMetrics.THROUGHPUT, Aggregation.AVERAGE)
					.priority(0)
					.check(new RulePrecondition() {
						@Override
						public boolean check(ModelEntity entity) {
							return entity instanceof ExternalCall;
						}						
					})
					.scope(new RuleScope() {
						@Override
						public List<ModelEntity> getScopeSet(ModelEntity base) {
							return Arrays.asList(base, ((ExternalCall)base).getService());
						}
						@Override
						public List<ModelEntity> getNotificationSet(ModelEntity base) {
							if (base instanceof Service) {
								List<ModelEntity> ret = new LinkedList<ModelEntity>();
								ret.add(base);
								List<Task> tasks = ((Service)base).getTasks();
								for (Task t : tasks) {
									if (t instanceof ExternalCall) {
										ret.add(t);
									}
								}
								return ret;
							} else if (base instanceof ExternalCall) {
								return getScopeSet(base);
							}
							return Collections.emptyList();
						}						
					})
					.build(new DeriveVisitCountHandler())
				);
	}

}
