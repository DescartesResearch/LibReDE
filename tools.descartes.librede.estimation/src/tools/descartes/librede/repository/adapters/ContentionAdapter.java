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
import tools.descartes.librede.repository.handlers.ConstantHandler;
import tools.descartes.librede.repository.handlers.DefaultAggregationHandler;
import tools.descartes.librede.repository.handlers.DeriveContentionHandler;
import tools.descartes.librede.repository.rules.DerivationRule;
import tools.descartes.librede.repository.rules.RulePrecondition;
import tools.descartes.librede.units.Ratio;
import tools.descartes.librede.units.Time;

public class ContentionAdapter implements IMetricAdapter<Ratio> {

	@Override
	public Interpolation getInterpolation() {
		return Interpolation.PIECEWISE_CONSTANT;
	}

	@Override
	public List<DerivationRule<Ratio>> getDerivationRules() {
		return Arrays.asList(
				DerivationRule.rule(StandardMetrics.CONTENTION, Aggregation.AVERAGE)
					.requiring(Aggregation.AVERAGE)
					.build(new DefaultAggregationHandler<Ratio>(Aggregation.AVERAGE)),
				DerivationRule.rule(StandardMetrics.CONTENTION, Aggregation.AVERAGE)
					.check(new RulePrecondition() {						
						@Override
						public boolean check(ModelEntity entity) {
							if (entity instanceof Resource) {
								return ((Resource) entity).getChildResources().isEmpty();
							}
							return false;
						}
					})
					.build(new ConstantHandler<Ratio>(0.0)),
				DerivationRule.rule(StandardMetrics.CONTENTION, Aggregation.AVERAGE)
					.requiring(StandardMetrics.STEAL_TIME, Aggregation.SUM)
					.build(new DeriveContentionHandler())
				);
	}

}
