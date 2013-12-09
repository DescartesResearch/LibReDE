package edu.kit.ipd.descartes.redeem.estimation.repository;

import static edu.kit.ipd.descartes.linalg.LinAlg.vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorFunction;
import edu.kit.ipd.descartes.redeem.estimation.workload.IModelEntity;

public final class Query<T extends Vector> {
	
	public static enum Type {
		SERVICE, ALL_SERVICES, RESOURCE, ALL_RESOURCES
	}
	
	public static enum Aggregation {
		ALL, MINIMUM, MAXIMUM, AVERAGE, SUM
	}
	
	private Query.Aggregation aggregation;
	private Query.Type type;
	private Metric metric;
	private List<IModelEntity> entities = new ArrayList<IModelEntity>();
	private RepositoryCursor repositoryCursor;
	
	protected Query(Aggregation aggregation, Type type, Metric metric,
			IModelEntity entity, RepositoryCursor repositoryCursor) {
		super();
		this.aggregation = aggregation;
		this.type = type;
		this.metric = metric;
		this.repositoryCursor = repositoryCursor;
		if (entity != null) {
			entities.add(entity);
		}
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
	
	public T execute() {		
		if (entities.isEmpty()) {
			load();
		}
		
		final List<TimeSeries> timeSeries = new ArrayList<TimeSeries>(entities.size());
		for (IModelEntity entity : entities) {
			TimeSeries current = null;
			if (metric == Metric.AVERAGE_RESPONSE_TIME) {
				current = repositoryCursor.getRepository().getData(Metric.RESPONSE_TIME, entity);
				if (current.isEmpty()) {
					current = repositoryCursor.getRepository().getData(Metric.AVERAGE_RESPONSE_TIME, entity);
				}
			} else {
				current = repositoryCursor.getRepository().getData(metric, entity);
			}
			if (current.isEmpty()) {
				throw new IllegalStateException(); //TODO: introduce dedicated exception
			}
			timeSeries.add(current);			
		}
		
		if (timeSeries.size() > 1) {
			Vector result = vector(timeSeries.size(), new VectorFunction() {				
				@Override
				public double cell(int row) {
					return aggregate(entities.get(row), timeSeries.get(row), repositoryCursor.getCurrentIntervalStart(), repositoryCursor.getCurrentIntervalEnd());
				}
			});
			return (T)result;
		} else {
			if (aggregation != Aggregation.ALL) {
				return (T)new Scalar(aggregate(entities.get(0), timeSeries.get(0), repositoryCursor.getCurrentIntervalStart(), repositoryCursor.getCurrentIntervalEnd()));
			} else {
				TimeSeries result = timeSeries.get(0).subset(repositoryCursor.getCurrentIntervalStart(), repositoryCursor.getCurrentIntervalEnd());
				return (T)result.getData();
			}			
		}
	}
	
	public int indexOf(IModelEntity entity) {
		return entities.indexOf(entity);
	}
	
	public IModelEntity getEntity(int index) {
		return entities.get(index);
	}
	
	public List<? extends IModelEntity> getEntities() {
		return Collections.unmodifiableList(entities);
	}
	
	private double aggregate(IModelEntity entity, TimeSeries ts, double startTime, double endTime) {
		switch(aggregation) {
		case AVERAGE:
			switch(metric) {
			case RESPONSE_TIME:
				return ts.mean(startTime, endTime);
			case UTILIZATION:
			case THROUGHPUT:
				return ts.timeWeightedMean(startTime, endTime);
			case AVERAGE_RESPONSE_TIME:
				TimeSeries tputSeries = repositoryCursor.getRepository().getData(Metric.THROUGHPUT, entity);
				if (tputSeries.isEmpty()) {
					return ts.timeWeightedMean(startTime, endTime);
				} else {
					return ts.timeWeightedMean(startTime, endTime, tputSeries);
				}
			}			
		case MINIMUM:
			return ts.min(startTime, endTime);
		case MAXIMUM:
			return ts.max(startTime, endTime);
		case SUM:
			return ts.subset(startTime, endTime).getData().sum();
		default:
			break;
		}
		return Double.NaN;
	}
	
	private void load() {
		if (type == Type.ALL_RESOURCES) {
			entities.addAll(repositoryCursor.getRepository().listResources());
		} else if (type == Type.ALL_SERVICES) {
			entities.addAll(repositoryCursor.getRepository().listServices());
		}		
	}
}
