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
package tools.descartes.librede.repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import tools.descartes.librede.configuration.ExternalCall;
import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.units.Dimension;
import tools.descartes.librede.units.Unit;

public class QueryBuilder<D extends Dimension> {
	
	private Metric<D> metric;
	private Unit<D> unit;
	private List<ModelEntity> entities = new LinkedList<>();
	private Aggregation aggregation;
	
	private QueryBuilder(Metric<D> metric) {
		this.metric = metric;
	}
	
	public static <D extends Dimension> QueryBuilder<D>.SelectClause select(Metric<D> metric) {
		QueryBuilder<D> builder = new QueryBuilder<D>(metric);
		return builder.new SelectClause();
	}
	
	public class SelectClause {
		public InClause in(Unit<D> unit) {
			QueryBuilder.this.unit = unit;
			return new InClause();
		}		
	}
	
	public class InClause {
	
		public ForClause forResource(Resource resource) {
			entities.add(resource);
			return new ForClause();
		}
		
		public ForClause forService(Service cls) {
			entities.add(cls);
			return new ForClause();
		}
		
		public ForAllClause forServices(Service...services) {
			return forServices(Arrays.asList(services));
		}
		
		public ForAllClause forServices(Collection<? extends Service> services) {
			entities.addAll(services);
			return new ForAllClause();
		}
		
		public ForAllClause forResources(Resource...resources) {
			return forResources(Arrays.asList(resources));
		}
		
		public ForAllClause forResources(Collection<? extends Resource> resources) {
			entities.addAll(resources);
			return new ForAllClause();
		}
		
		public ForClause forResourceDemand(ResourceDemand demand) {
			entities.add(demand);
			return new ForClause();
		}
		
		public ForAllClause forResourceDemands(ResourceDemand...demands) {
			return forResourceDemands(Arrays.asList(demands));
		}
		
		public ForAllClause forResourceDemands(Collection<? extends ResourceDemand> demands) {
			entities.addAll(demands);
			return new ForAllClause();
		}
		
		public ForClause forExternalCall(ExternalCall call) {
			entities.add(call);
			return new ForClause();
		}
		
		public ForAllClause forExternalCalls(ExternalCall...calls) {
			return forExternalCalls(Arrays.asList(calls));
		}
		
		public ForAllClause forExternalCalls(Collection<? extends ExternalCall> calls) {
			entities.addAll(calls);
			return new ForAllClause();
		}
		
	}
	
	public class ForAllClause {
		public UsingClause<Vector> sum() {
			aggregation = Aggregation.SUM;
			return new UsingClause<Vector>();
		}
		
		public UsingClause<Vector> min() {
			aggregation = Aggregation.MINIMUM;
			return new UsingClause<Vector>();
		}
		
		public UsingClause<Vector> max() {
			aggregation = Aggregation.MAXIMUM;
			return new UsingClause<Vector>();
		}
		
		public UsingClause<Vector> average() {
			aggregation = Aggregation.AVERAGE;
			return new UsingClause<Vector>();
		}
	}
	
	public class ForClause {		
		public UsingClause<Scalar> sum() {
			aggregation = Aggregation.SUM;
			return new UsingClause<Scalar>();
		}
		
		public UsingClause<Scalar> min() {
			aggregation = Aggregation.MINIMUM;
			return new UsingClause<Scalar>();
		}
		
		public UsingClause<Scalar> max() {
			aggregation = Aggregation.MAXIMUM;
			return new UsingClause<Scalar>();
		}
		
		public UsingClause<Scalar> average() {
			aggregation = Aggregation.AVERAGE;
			return new UsingClause<Scalar>();
		}
				
		public UsingClause<Vector> all() {
			aggregation = Aggregation.NONE;
			return new UsingClause<Vector>();
		}
	}
	
	public class UsingClause<T extends Vector> {
		public Query<T, D> using(IRepositoryCursor repository) {
			return new Query<T, D>(aggregation, metric, unit, entities, repository);
		}
	}
	
}
