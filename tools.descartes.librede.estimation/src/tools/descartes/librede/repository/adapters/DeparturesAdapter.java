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

import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.repository.IMetricAdapter;
import tools.descartes.librede.repository.TimeSeries.Interpolation;
import tools.descartes.librede.repository.handlers.DefaultAggregationHandler;
import tools.descartes.librede.repository.handlers.DeriveDeparturesHandler;
import tools.descartes.librede.repository.handlers.DeriveDiffHandler;
import tools.descartes.librede.repository.rules.DerivationRule;
import tools.descartes.librede.units.RequestCount;

public class DeparturesAdapter implements IMetricAdapter<RequestCount> {

	@Override
	public Interpolation getInterpolation() {
		return Interpolation.LINEAR;
	}

	@Override
	public List<DerivationRule<RequestCount>> getDerivationRules() {
		return Arrays.asList(
				DerivationRule.derive(StandardMetrics.DEPARTURES, Aggregation.SUM)
					.requiring(Aggregation.NONE)
					.priority(10)
					.build(new DefaultAggregationHandler<>(StandardMetrics.DEPARTURES, Aggregation.NONE)),
				DerivationRule.derive(StandardMetrics.DEPARTURES, Aggregation.SUM)
					.requiring(Aggregation.SUM)
					.priority(0)
					.build(new DefaultAggregationHandler<>(StandardMetrics.DEPARTURES, Aggregation.SUM)),
				DerivationRule.derive(StandardMetrics.DEPARTURES, Aggregation.MINIMUM)
					.requiring(Aggregation.NONE)
					.build(new DefaultAggregationHandler<>(StandardMetrics.DEPARTURES, Aggregation.NONE)),
				DerivationRule.derive(StandardMetrics.DEPARTURES, Aggregation.MAXIMUM)
					.requiring(Aggregation.NONE)
					.build(new DefaultAggregationHandler<>(StandardMetrics.DEPARTURES, Aggregation.NONE)),
				DerivationRule.derive(StandardMetrics.DEPARTURES, Aggregation.CUMULATIVE_SUM)
					.requiring(Aggregation.NONE)
					.build(new DefaultAggregationHandler<>(StandardMetrics.DEPARTURES, Aggregation.NONE)),
				DerivationRule.derive(StandardMetrics.DEPARTURES, Aggregation.CUMULATIVE_SUM)
					.requiring(Aggregation.SUM)
					.build(new DeriveDiffHandler<RequestCount>(StandardMetrics.DEPARTURES)),
				DerivationRule.derive(StandardMetrics.DEPARTURES)
					.requiring(StandardMetrics.RESPONSE_TIME)
					.build(new DeriveDeparturesHandler())
				);
	}

}