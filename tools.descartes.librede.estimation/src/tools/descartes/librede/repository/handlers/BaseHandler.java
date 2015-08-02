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

import java.util.Collections;
import java.util.List;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.repository.MemoryObservationRepository;
import tools.descartes.librede.units.Dimension;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;

public abstract class BaseHandler<D extends Dimension> {

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
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (i > 0) {
				builder.append(" + ");
			}
			builder.append(delegatedMetrics.get(i).getName()).append(":").append(delegatedAggregations.get(i).getLiteral());
		}
		builder.append("(").append(this.getClass().getName()).append(")");
		return builder.toString();
	}
}