/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2018, by Simon Spinner, Johannes Grohmann
 *  and Contributors.
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

import java.util.List;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.repository.IMetricDerivationHandler;
import tools.descartes.librede.repository.IMonitoringRepository;
import tools.descartes.librede.units.Dimension;

public class DerivationRule<D extends Dimension> extends Rule implements Comparable<DerivationRule<D>>, IRuleActivationHandler {

	private IMetricDerivationHandler<D> derivationHandler;
	private final Metric<D> metric;
	private final Aggregation aggregation;
	private int priority;
		
	private DerivationRule(Metric<D> metric, Aggregation aggregation) {
		setActivationHandler(this);
		this.metric = metric;
		this.aggregation = aggregation;
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
	
	public DerivationRule<D> requiring(Metric<?> metric, Aggregation aggregation, DependencyScope scope) {
		addDependency(metric, aggregation, scope);
		return this;
	}
	
	public DerivationRule<D> check(RulePrecondition precondition) {
		addPrecondition(precondition);
		return this;
	}
	
	public DerivationRule<D> priority(int priority) {
		setPriority(priority);
		return this;
	}
	
	public DerivationRule<D> build(IMetricDerivationHandler<D> handler) {
		this.derivationHandler = handler;
		return this;
	}
	
	public IMetricDerivationHandler<D> getDerivationHandler() {
		return derivationHandler;
	}
		
	public static <D extends Dimension> DerivationRule<D> rule(Metric<D> metric) {
		return new DerivationRule<D>(metric, Aggregation.NONE);
	}
	
	public static <D extends Dimension> DerivationRule<D> rule(Metric<D> metric, Aggregation aggregation) {
		return new DerivationRule<D>(metric, aggregation);
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
		res.append("handler=").append(getDerivationHandler());
		res.append("}");
		return res.toString();
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

	@Override
	public int compareTo(DerivationRule<D> o) {
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

	@Override
	public void activateRule(IMonitoringRepository repository, Rule.Status rule, ModelEntity entity) {
		repository.insertDerivation(this, entity);		
	}

	@Override
	public void deactivateRule(IMonitoringRepository repository, Rule.Status rule, ModelEntity entity) {
		// EMPTY
		// If necessary, implement something for removal
	}
}
