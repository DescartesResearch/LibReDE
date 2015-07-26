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
package tools.descartes.librede.repository.handlers;

import java.util.List;

import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.repository.IMetricAggregationHandler;
import tools.descartes.librede.units.Dimension;

public abstract class BaseAggregationHandler<D extends Dimension> extends BaseHandler<D> implements IMetricAggregationHandler<D> {

	public BaseAggregationHandler(Metric<?> delegatedMetric, Aggregation delegatedAggregation) {
		super(delegatedMetric, delegatedAggregation);
	}

	public BaseAggregationHandler(List<? extends Metric<?>> delegatedMetrics, List<? extends Aggregation> delegatedAggregations) {
		super(delegatedMetrics, delegatedAggregations);
	}		
}