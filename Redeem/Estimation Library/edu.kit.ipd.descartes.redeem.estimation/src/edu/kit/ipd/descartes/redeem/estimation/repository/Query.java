package edu.kit.ipd.descartes.redeem.estimation.repository;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.system.IModelEntity;
import edu.kit.ipd.descartes.redeem.estimation.system.Resource;
import edu.kit.ipd.descartes.redeem.estimation.system.Service;

public class Query<T extends Matrix> {
	
	public static class SelectClause {
		
		private Metric metric;
		
		protected SelectClause(Metric metric) {
			this.metric = metric;
		}
		
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
	
	public static class ForClause<T extends Matrix> {
		
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
	
	public int getIndex(IModelEntity entity) {
		return 0;
	}
	
	public IModelEntity getEntity(int index) {
		return null;
	}
	
	public T execute() {
		return null;
	}
}
