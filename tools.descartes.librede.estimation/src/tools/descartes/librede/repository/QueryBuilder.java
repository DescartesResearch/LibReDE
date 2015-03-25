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
package tools.descartes.librede.repository;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.repository.Query.Type;
import tools.descartes.librede.units.Dimension;
import tools.descartes.librede.units.Unit;

public class QueryBuilder<D extends Dimension> {
	
	private Query.Type type;
	private Metric<D> metric;
	private Unit<D> unit;
	private ModelEntity entity;
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
			type = Type.RESOURCE;
			entity = resource;
			return new ForClause();
		}
		
		public ForClause forService(Service cls) {
			type = Type.SERVICE;
			entity = cls;
			return new ForClause();
		}
		
		public ForAllClause forAllServices() {
			type = Type.ALL_SERVICES;
			entity = null;
			return new ForAllClause();
		}
		
		public ForAllClause forAllResources() {
			type = Type.ALL_RESOURCES;
			entity = null;
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
			return new Query<T, D>(aggregation, type, metric, unit, entity, repository);
		}
	}
	
}
