package edu.kit.ipd.descartes.redeem.estimation.repository;

import static edu.kit.ipd.descartes.linalg.LinAlg.max;
import static edu.kit.ipd.descartes.linalg.LinAlg.mean;
import static edu.kit.ipd.descartes.linalg.LinAlg.min;
import static edu.kit.ipd.descartes.linalg.LinAlg.sum;
import static edu.kit.ipd.descartes.linalg.LinAlg.vector;

import java.util.ArrayList;
import java.util.List;

import edu.kit.ipd.descartes.linalg.AggregateFunction;
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
	private ObservationRepositoryView repositoryView;
	private AggregateFunction func;
	
	protected Query(Aggregation aggregation, Type type, Metric metric,
			IModelEntity entity) {
		super();
		this.aggregation = aggregation;
		this.type = type;
		this.metric = metric;
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
				current = repositoryView.getData(entity, Metric.RESPONSE_TIME);
				if (current == null) {
					current = repositoryView.getData(entity, Metric.AVERAGE_RESPONSE_TIME);
				}
			} else {
				current = repositoryView.getData(entity, metric);
			}
			if (current == null) {
				throw new IllegalStateException(); //TODO: introduce dedicated exception
			}
			timeSeries.add(current);			
		}
		
		if (timeSeries.size() > 1) {
			Vector result = vector(timeSeries.size(), new VectorFunction() {				
				@Override
				public double cell(int row) {
					TimeSeries result = timeSeries.get(row).subset(repositoryView.getCurrentIntervalStart(), repositoryView.getCurrentIntervalEnd());
					return result.aggregate(func);
				}
			});
			return (T)result;
		} else {
			if (aggregation != Aggregation.ALL) {
				TimeSeries result = timeSeries.get(0).subset(repositoryView.getCurrentIntervalStart(), repositoryView.getCurrentIntervalEnd());
				return (T)new Scalar(result.aggregate(func));
			} else {
				TimeSeries result = timeSeries.get(0).subset(repositoryView.getCurrentIntervalStart(), repositoryView.getCurrentIntervalEnd());
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
	
	private void load() {
		switch(aggregation) {
		case AVERAGE:
			func = new AggregateFunction() {				
				@Override
				public double aggregate(Vector values) {
					return mean(values);
				}
			};
			break;
		case SUM:
			func = new AggregateFunction() {				
				@Override
				public double aggregate(Vector values) {
					return sum(values);
				}
			};
			break;
		case MINIMUM:
			func = new AggregateFunction() {				
				@Override
				public double aggregate(Vector values) {
					return min(values);
				}
			};
			break;
		case MAXIMUM:
			func = new AggregateFunction() {				
				@Override
				public double aggregate(Vector values) {
					return max(values);
				}
			};
			break;
		default:
			func = null;
			break;
		}
		
		if (type == Type.ALL_RESOURCES) {
			entities.addAll(repositoryView.listResources());
		} else if (type == Type.ALL_SERVICES) {
			entities.addAll(repositoryView.listServices());
		}		
	}
}
