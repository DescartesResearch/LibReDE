package edu.kit.ipd.descartes.redeem.estimation.repository;

import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.repository.Query.Type;
import edu.kit.ipd.descartes.redeem.estimation.workload.IModelEntity;
import edu.kit.ipd.descartes.redeem.estimation.workload.Resource;
import edu.kit.ipd.descartes.redeem.estimation.workload.Service;

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
