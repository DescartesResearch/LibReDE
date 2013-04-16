package edu.kit.ipd.descartes.redeem.estimation.repository;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.system.Resource;
import edu.kit.ipd.descartes.redeem.estimation.system.Service;

public class QueryBuilder {
	
	private Metric metric;
	
	private QueryBuilder(Metric metric) {
		this.metric = metric;
	}
	
	public static SelectClause select(Metric metric) {
		QueryBuilder builder = new QueryBuilder(metric);
		return builder.new SelectClause();
	}
	
	public class SelectClause {
	
		public ForClause<Scalar> forResource(Resource resource) {
			return null;
		}
		
		public ForClause<Scalar> forService(Service cls) {
			return null;
		}
		
		public ForClause<Vector> forAllServices() {
			return null;
		}
		
		public ForClause<Vector> forAllResources() {
			return null;
		}
		
	}
	
	public class ForClause<T extends Matrix> {
		
		public Query<T> sum() {
			return null;
		}
		
		public Query<T> average() {
			return null;
		}
				
		public Query<T> last() {			
			return null;
		}
	}
	
}
