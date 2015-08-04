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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.units.Dimension;

public abstract class AbstractRule<D extends Dimension> implements Comparable<AbstractRule<D>> {
	
	private static class DefaultScope implements RuleScope {

		@Override
		public List<ModelEntity> getScopeSet(ModelEntity base) {
			return Collections.singletonList(base);
		}

		@Override
		public List<ModelEntity> getNotificationSet(ModelEntity changed) {
			return Collections.singletonList(changed);
		}
		
	}

	private final Metric<D> metric;
	private final Aggregation aggregation;
	private final List<RuleDependency<?>> dependencies = new LinkedList<>();
	private final List<RulePrecondition> preconditions = new LinkedList<>();
	private RuleScope resolver = new DefaultScope();
	private int priority;
	
	protected AbstractRule(Metric<D> metric, Aggregation aggregation) {
		this.metric = metric;
		this.aggregation = aggregation;
	}

	public List<RuleDependency<?>> getDependencies() {
		return dependencies;
	}
	
	protected void addPrecondition(RulePrecondition precondition) {
		preconditions.add(precondition);
	}
	
	protected void addDependency(Metric<? extends Dimension> metric, Aggregation aggregation) {
		dependencies.add(new RuleDependency<>(metric, aggregation));
	}
	
	public Metric<D> getMetric() {
		return metric;
	}
	
	public Aggregation getAggregation() {
		return aggregation;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public void setScope(RuleScope resolver) {
		this.resolver = resolver;
	}
	
	public List<ModelEntity> getScopeSet(ModelEntity base) {
		return resolver.getScopeSet(base);
	}
	
	public List<ModelEntity> getNotificationSet(ModelEntity changed) {
		return resolver.getNotificationSet(changed);
	}
	
	public boolean applies(ModelEntity entity) {
		for (RulePrecondition cond : preconditions) {
			if (!cond.check(entity)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int compareTo(AbstractRule<D> o) {
		int c = this.getMetric().getName().compareTo(o.getMetric().getName());
		if (c == 0) {
			c = this.getAggregation().compareTo(o.getAggregation());
			if (c == 0) {
				c = Integer.compare(this.getPriority(), o.getPriority());
			}
		}
		return c;
	}
}
 