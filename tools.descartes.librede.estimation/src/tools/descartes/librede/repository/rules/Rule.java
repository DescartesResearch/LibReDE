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
import java.util.Set;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.units.Dimension;

public class Rule<D extends Dimension> implements Comparable<Rule<D>> {
	
	static class DefaultScope implements RuleScope {

		@Override
		public Set<? extends ModelEntity> getScopeSet(ModelEntity base) {
			return Collections.singleton(base);
		}

		@Override
		public Set<? extends ModelEntity> getNotificationSet(ModelEntity changed) {
			return Collections.singleton(changed);
		}
		
	}
	
	private IRuleActivationHandler<D> handler;
	private final Metric<D> metric;
	private final Aggregation aggregation;
	private final List<DataDependency<?>> dependencies = new LinkedList<>();
	private final List<RulePrecondition> preconditions = new LinkedList<>();
	private RuleScope resolver = new DefaultScope();
	private int priority;
		
	private Rule(Metric<D> metric, Aggregation aggregation) {
		this.metric = metric;
		this.aggregation = aggregation;
	}
	
	public Rule<D> requiring(Aggregation aggregation) {
		addDependency(getMetric(), aggregation);
		return this;
	}
	
	public Rule<D> requiring(Metric<?> metric) {
		addDependency(metric, Aggregation.NONE);
		return this;
	}
	
	public Rule<D> requiring(Metric<?> metric, Aggregation aggregation) {
		addDependency(metric, aggregation);
		return this;
	}
	
	public Rule<D> check(RulePrecondition precondition) {
		addPrecondition(precondition);
		return this;
	}
	
	public Rule<D> scope(RuleScope resolver) {
		setScope(resolver);
		return this;
	}
	
	public Rule<D> priority(int priority) {
		setPriority(priority);
		return this;
	}
	
	public Rule<D> build(IRuleActivationHandler<D> handler) {
		this.handler = handler;
		return this;
	}
		
	public static <D extends Dimension> Rule<D> rule(Metric<D> metric) {
		return new Rule<D>(metric, Aggregation.NONE);
	}
	
	public static <D extends Dimension> Rule<D> rule(Metric<D> metric, Aggregation aggregation) {
		return new Rule<D>(metric, aggregation);
	}
	
	public IRuleActivationHandler<D> getDerivationHandler() {
		return handler;
	}
	
	@Override
	public String toString() {
		StringBuilder res = new StringBuilder("Derivation rule ");
		res.append("*/").append(this.getMetric()).append("/").append(this.getAggregation());
		List<DataDependency<?>> dependencies = getDependencies();
		if (!dependencies.isEmpty()) {
			res.append(" <- (");
			boolean first = true;
			for (DataDependency<?> dep : dependencies) {
				if (!first) {
					res.append(",");
				}
				res.append("*/").append(dep.getMetric()).append("/").append(dep.getAggregation());
				first = false;
			}
			res.append(")");
		}
		res.append(" {");
		res.append("priority=").append(getPriority()).append(", ");
		res.append("handler=").append(handler);
		res.append("}");
		return res.toString();
	}

	public List<DataDependency<?>> getDependencies() {
		return dependencies;
	}

	protected void addPrecondition(RulePrecondition precondition) {
		preconditions.add(precondition);
	}

	protected void addDependency(Metric<? extends Dimension> metric, Aggregation aggregation) {
		dependencies.add(new DataDependency<>(metric, aggregation));
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

	public Set<? extends ModelEntity> getScopeSet(ModelEntity base) {
		return resolver.getScopeSet(base);
	}

	public Set<? extends ModelEntity> getNotificationSet(ModelEntity changed) {
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
	public int compareTo(Rule<D> o) {
		if (this == o) {
			// rules are only equal if they point to the same instance.
			return 0;
		}
		
		int c = this.getMetric().getName().compareTo(o.getMetric().getName());
		if (c == 0) {
			c = this.getAggregation().compareTo(o.getAggregation());
			if (c == 0) {
				c = Integer.compare(this.getPriority(), o.getPriority());
				if (c == 0) {
					// rules with the same priority are shown in consecutive order
					c = 1;
				}
			}
		}
		return c;
	}
	

}
