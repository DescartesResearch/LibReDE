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
package edu.kit.ipd.descartes.librede.estimation.repository;

import edu.kit.ipd.descartes.librede.estimation.repository.Query.Type;
import edu.kit.ipd.descartes.librede.estimation.workload.IModelEntity;
import edu.kit.ipd.descartes.librede.estimation.workload.Resource;
import edu.kit.ipd.descartes.librede.estimation.workload.Service;
import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;

public class QueryBuilder {
	
	private Query.Type type;
	private StandardMetric metric;
	private IModelEntity entity;
	private Aggregation aggregation;
	
	private QueryBuilder(StandardMetric metric) {
		this.metric = metric;
	}
	
	public static SelectClause select(StandardMetric metric) {
		QueryBuilder builder = new QueryBuilder(metric);
		return builder.new SelectClause();
	}
	
	public class SelectClause {
	
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
		public Query<T> using(RepositoryCursor repository) {
			return new Query<T>(aggregation, type, metric, entity, repository);
		}
	}
	
}
