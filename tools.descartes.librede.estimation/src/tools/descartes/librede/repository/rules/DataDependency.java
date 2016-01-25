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
import java.util.HashSet;
import java.util.Set;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.repository.IMonitoringRepository;
import tools.descartes.librede.units.Dimension;

/**
 * A data dependency describes the data requirement of a specific metric and
 * aggregation type for one or several model entities (e.g., services or
 * resources). The scope defines the model entities for which data of a
 * certain metric and aggregation is required. 
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 * @version 1.0
 *
 * @param <D>
 *            the dimension of the metric
 */
public class DataDependency<D extends Dimension> {
	
	public class Status {
		private final Set<? extends ModelEntity> missingEntities;
		
		public Status(Set<? extends ModelEntity> missingEntities) {
			this.missingEntities = Collections.unmodifiableSet(missingEntities);
		}
		
		public DataDependency<D> getDependency() {
			return DataDependency.this;
		}
		
		public boolean isResolved() {
			return missingEntities.isEmpty();
		}
		
		public Set<? extends ModelEntity> getMissingEntities() {
			return missingEntities;
		}
	}

	private final DependencyScope scope;
	private final Metric<D> metric;
	private final Aggregation aggregation;
	
	public DataDependency(Metric<D> metric, Aggregation aggregation) {
		this(metric, aggregation, DependencyScope.dynamicScope());		
	}

	public DataDependency(Metric<D> metric, Aggregation aggregation, DependencyScope scope) {
		this.scope = scope;
		this.metric = metric;
		this.aggregation = aggregation;
	}
	
	public DependencyScope getScope() {
		return scope;
	}

	public Metric<D> getMetric() {
		return metric;
	}

	public Aggregation getAggregation() {
		return aggregation;
	}
	
	public Status checkStatus(IMonitoringRepository repository, ModelEntity target) {
		Set<? extends ModelEntity> scopeEntities = scope.getScopeSet(target);
		Set<ModelEntity> entitiesWithMissingData = new HashSet<>();
		for (ModelEntity e : scopeEntities) {				
			if (!repository.exists(metric, e, aggregation)) {
				entitiesWithMissingData.add(e);
			}
		}
		return new Status(entitiesWithMissingData);
	}
}
