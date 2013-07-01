package edu.kit.ipd.descartes.redeem.estimation.repository;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.redeem.estimation.workload.IModelEntity;

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
	private int windowSize; 
	
	protected Query(Aggregation aggregation, Type type, Metric metric,
			IModelEntity entity, int windowSize) {
		super();
		this.aggregation = aggregation;
		this.type = type;
		this.metric = metric;
		this.entity = entity;
		this.windowSize = windowSize;
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

	public int getWindowSize() {
		return windowSize;
	}

}
