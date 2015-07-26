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
package tools.descartes.librede.repository.rules;

import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.repository.IMetricAggregationHandler;
import tools.descartes.librede.repository.handlers.DefaultAggregationHandler;
import tools.descartes.librede.units.Dimension;

public class AggregationRule<D extends Dimension> extends AbstractRule<D> {
	
	private Aggregation sourceAggregation;
	private IMetricAggregationHandler<D> handler;
	
	protected AggregationRule(Metric<D> metric, Aggregation aggregation) {
		super(metric, aggregation);
	}
	
	public AggregationRule<D> from(Aggregation aggregation) {
		sourceAggregation = aggregation;
		addDependency(getMetric(), sourceAggregation);
		return this;
	}
	
	public AggregationRule<D> requiring(Metric<?> metric, Aggregation aggregation) {
		addDependency(getMetric(), aggregation);
		return this;
	}
	
	public AggregationRule<D> check(RulePrecondition precondition) {
		addPrecondition(precondition);
		return this;
	}
	
	public AggregationRule<D> build() {
		checkCompleteness();
		this.handler = new DefaultAggregationHandler<>(getMetric(), sourceAggregation);
		return this;
	}
	
	public AggregationRule<D> build(IMetricAggregationHandler<D> handler) {
		this.handler = handler;
		return this;
	}
	
	public static <D extends Dimension> AggregationRule<D> aggregate(Metric<D> metric, Aggregation aggregation) {
		return new AggregationRule<D>(metric, aggregation);
	}
	
	public IMetricAggregationHandler<D> getAggregationHandler() {
		return handler;
	}
	
	private void checkCompleteness() {
		if (sourceAggregation == null) {
			throw new IllegalStateException();
		}
	}

}
