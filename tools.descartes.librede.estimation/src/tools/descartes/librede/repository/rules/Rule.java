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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.repository.IMonitoringRepository;
import tools.descartes.librede.units.Dimension;

public class Rule {
	
	public class Status {
		private final boolean active;
		private final List<DataDependency<?>.Status> dependenciesStatus;
		
		public Status(boolean active, List<DataDependency<?>.Status> dependenciesStatus) {
			this.active = active;
			this.dependenciesStatus = Collections.unmodifiableList(dependenciesStatus);
		}
		
		public boolean isActive() {
			return active;
		}
		
		public List<DataDependency<?>.Status> getDependenciesStatus() {
			return dependenciesStatus;
		}
		
		public Rule getRule() {
			return Rule.this;
		}
	}
	
	private static final Logger log = Logger.getLogger(Rule.class);
	
	private IRuleActivationHandler handler;
	private final List<DataDependency<?>> dependencies = new LinkedList<>();
	private final List<RulePrecondition> preconditions = new LinkedList<>();
	
	public List<DataDependency<?>> getDependencies() {
		return dependencies;
	}

	protected void addPrecondition(RulePrecondition precondition) {
		preconditions.add(precondition);
	}
	
	public void addDependencies(Collection<DataDependency<?>> newDependencies) {
		dependencies.addAll(newDependencies);
	}

	public <B extends Dimension> void addDependency(Metric<B> metric, Aggregation aggregation) {
		dependencies.add(new DataDependency<B>(metric, aggregation));
	}
	
	public <B extends Dimension> void addDependency(Metric<B> metric, Aggregation aggregation, DependencyScope scope) {
		dependencies.add(new DataDependency<B>(metric, aggregation, scope));
	}
	
	public IRuleActivationHandler getActivationHandler() {
		return handler;
	}
	
	public void setActivationHandler(IRuleActivationHandler handler) {
		this.handler = handler;
	}
	
	public void checkStatus(IMonitoringRepository repository, ModelEntity target) {
		boolean active = true;
		if (applies(target)) {
			List<DataDependency<?>.Status> dependenciesStatus = new ArrayList<>(dependencies.size());
			for (DataDependency<?> dep : getDependencies()) {
				DataDependency<?>.Status depStatus = dep.checkStatus(repository, target);
				active = active && depStatus.isResolved();
				dependenciesStatus.add(depStatus);
			}
			
			Rule.Status status = new Rule.Status(active, dependenciesStatus);
			if (active) {
				handler.activateRule(repository, status, target);
			} else {
				handler.deactivateRule(repository, status, target);
			}
		}
	}

	public boolean applies(ModelEntity entity) {
		for (RulePrecondition cond : preconditions) {
			if (!cond.check(entity)) {
				return false;
			}
		}
		return true;
	}
}
