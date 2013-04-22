package edu.kit.ipd.descartes.redeem.estimation.repository;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.system.IModelEntity;
import edu.kit.ipd.descartes.redeem.estimation.system.Resource;
import edu.kit.ipd.descartes.redeem.estimation.system.Service;

public final class Query<T extends Matrix> {
	
	public static enum Type {
		SERVICE, ALL_SERVICES, RESOURCE, ALL_RESOURCES
	}
	
	public static enum Aggregation {
		LAST, AVERAGE, SUM
	}
	
	private Query.Aggregation aggregation;
	private Query.Type type;
	private Metric metric;
	private IModelEntity entity;	
	
	protected Query(Aggregation aggregation, Type type, Metric metric,
			IModelEntity entity) {
		super();
		this.aggregation = aggregation;
		this.type = type;
		this.metric = metric;
		this.entity = entity;
	}

	public Query.Aggregation getAggregation() {
		return aggregation;
	}
	
	public Query.Type getType() {
		return type;
	}
	
	public Metric getMetric() {
		return metric;
	}
	
	public IModelEntity getEntity() {
		return entity;
	}

}
