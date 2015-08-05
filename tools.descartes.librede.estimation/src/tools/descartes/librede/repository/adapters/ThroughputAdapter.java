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
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.repository.IMetricAdapter;
import tools.descartes.librede.repository.TimeSeries.Interpolation;
import tools.descartes.librede.repository.handlers.DeriveConstantRate;
import tools.descartes.librede.repository.handlers.RequestRateAggregationHandler;
import tools.descartes.librede.repository.handlers.TimeWeightedAggregationHandler;
import tools.descartes.librede.repository.rules.Rule;
import tools.descartes.librede.repository.rules.RulePrecondition;
import tools.descartes.librede.units.RequestRate;

public class ThroughputAdapter implements IMetricAdapter<RequestRate> {

	@Override
	public Interpolation getInterpolation() {
		return Interpolation.LINEAR;
	}

	@Override
	public List<Rule<RequestRate>> getDerivationRules() {
		return Arrays.asList(
				Rule.rule(StandardMetrics.THROUGHPUT, Aggregation.AVERAGE)
					.requiring(Aggregation.AVERAGE)
					.priority(10)
					.build(new TimeWeightedAggregationHandler<RequestRate>()),
				Rule.rule(StandardMetrics.THROUGHPUT, Aggregation.AVERAGE)
					.check(new RulePrecondition() {				
						@Override
						public boolean check(ModelEntity entity) {
							return (entity instanceof Service) && ((Service)entity).isBackgroundService();
						}
					})
					.priority(20) //IMPORTANT: Must be larger than everything else so that it is not overwritten
					.build(new DeriveConstantRate()),
				Rule.rule(StandardMetrics.THROUGHPUT, Aggregation.AVERAGE)
					.requiring(StandardMetrics.DEPARTURES, Aggregation.SUM)
					.priority(0)
					.build(new RequestRateAggregationHandler(StandardMetrics.DEPARTURES))
				);
	}

}