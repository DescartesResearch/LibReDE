package edu.kit.ipd.descartes.redeem.estimation.repository;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.repository.Query.Aggregation;
import edu.kit.ipd.descartes.redeem.estimation.repository.Query.Type;
import edu.kit.ipd.descartes.redeem.estimation.system.IModelEntity;
import edu.kit.ipd.descartes.redeem.estimation.system.Resource;
import edu.kit.ipd.descartes.redeem.estimation.system.Service;

public class QueryBuilder {
	
	private Query.Type type;
	private Metric metric;
	private IModelEntity entity;
	
	private QueryBuilder(Metric metric) {
		this.metric = metric;
	}
	
	public static SelectClause select(Metric metric) {
		QueryBuilder builder = new QueryBuilder(metric);
		return builder.new SelectClause();
	}
	
	public class SelectClause {
	
		public ForClause<Scalar> forResource(Resource resource) {
			type = Type.RESOURCE;
			entity = resource;
			return new ForClause<>();
		}
		
		public ForClause<Scalar> forService(Service cls) {
			type = Type.SERVICE;
			entity = cls;
			return new ForClause<>();
		}
		
		public ForClause<Vector> forAllServices() {
			type = Type.ALL_SERVICES;
			entity = null;
			return new ForClause<>();
		}
		
		public ForClause<Vector> forAllResources() {
			type = Type.ALL_RESOURCES;
			entity = null;
			return new ForClause<>();
		}
		
	}
	
	public class ForClause<T extends Matrix> {
		
		//TODO window size
		public Query<T> sum(int windowSize) {
			return new Query<>(Aggregation.SUM, type, metric, entity, windowSize);
		}
		
		public Query<T> average(int windowSize) {
			return new Query<>(Aggregation.AVERAGE, type, metric, entity, windowSize);
		}
				
		public Query<T> last() {			
			return new Query<>(Aggregation.LAST, type, metric, entity,1);
		}
	}
	
}
