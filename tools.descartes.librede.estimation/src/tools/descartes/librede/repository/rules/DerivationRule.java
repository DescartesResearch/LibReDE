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
import tools.descartes.librede.repository.IMetricDerivationHandler;
import tools.descartes.librede.units.Dimension;

public class DerivationRule<D extends Dimension> extends AbstractRule<D> {
	
	private IMetricDerivationHandler<D> handler;
		
	private DerivationRule(Metric<D> metric, Aggregation aggregation) {
		super(metric, aggregation);
	}
	
	public DerivationRule<D> requiring(Aggregation aggregation) {
		addDependency(getMetric(), aggregation);
		return this;
	}
	
	public DerivationRule<D> requiring(Metric<?> metric) {
		addDependency(metric, Aggregation.NONE);
		return this;
	}
	
	public DerivationRule<D> requiring(Metric<?> metric, Aggregation aggregation) {
		addDependency(metric, aggregation);
		return this;
	}
	
	public DerivationRule<D> check(RulePrecondition precondition) {
		addPrecondition(precondition);
		return this;
	}
	
	public DerivationRule<D> scope(RuleScope resolver) {
		setScope(resolver);
		return this;
	}
	
	public DerivationRule<D> priority(int priority) {
		setPriority(priority);
		return this;
	}
	
	public DerivationRule<D> build(IMetricDerivationHandler<D> handler) {
		this.handler = handler;
		return this;
	}
		
	public static <D extends Dimension> DerivationRule<D> derive(Metric<D> metric) {
		return new DerivationRule<D>(metric, Aggregation.NONE);
	}
	
	public static <D extends Dimension> DerivationRule<D> derive(Metric<D> metric, Aggregation aggregation) {
		return new DerivationRule<D>(metric, aggregation);
	}
	
	public IMetricDerivationHandler<D> getDerivationHandler() {
		return handler;
	}
	

}
