package edu.kit.ipd.descartes.redeem.estimation.testutils;

import static edu.kit.ipd.descartes.linalg.LinAlg.*;

import java.util.Arrays;
import java.util.Random;

import edu.kit.ipd.descartes.linalg.LinAlg;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.MatrixFunction;
import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorFunction;
import edu.kit.ipd.descartes.redeem.estimation.repository.IMonitoringRepository;
import edu.kit.ipd.descartes.redeem.estimation.repository.Metric;
import edu.kit.ipd.descartes.redeem.estimation.repository.Query;
import edu.kit.ipd.descartes.redeem.estimation.repository.Query.Type;
import edu.kit.ipd.descartes.redeem.estimation.repository.Result;
import edu.kit.ipd.descartes.redeem.estimation.workload.IModelEntity;
import edu.kit.ipd.descartes.redeem.estimation.workload.Resource;
import edu.kit.ipd.descartes.redeem.estimation.workload.Service;
import edu.kit.ipd.descartes.redeem.estimation.workload.WorkloadDescription;

public class ObservationDataGenerator implements IMonitoringRepository {
	
	private Random randUtil;
	private Random randWheights;
	private Random randDemands;
	private double upperUtilizationBound = 1.0;
	private double lowerUtilizationBound = 0.0;
	private Vector demands;
	private double workloadMixVariation = 1.0;
	
	private Resource[] resources;
	private Service[] services;
	
	private WorkloadDescription model;
	
	private Observation currentObservation = null;
	
	public ObservationDataGenerator(long seed, int numWorkloadClasses, int numResources) {
		Random randSeed = new Random(seed);
		randUtil = new Random(randSeed.nextLong());
		randWheights = new Random(randSeed.nextLong());
		randDemands = new Random(randSeed.nextLong());
		
		services = new Service[numWorkloadClasses];
		resources = new Resource[numResources];
		for (int i = 0; i < numWorkloadClasses; i++) {
			services[i] = new Service("WC" + i);
		}
		
		for (int i = 0; i < numResources; i++) {
			resources[i] = new Resource("R" + i);
		}
		
		model = new WorkloadDescription(Arrays.asList(resources), Arrays.asList(services));
	}
	
	public double getUpperUtilizationBound() {
		return upperUtilizationBound;
	}
	
	public void setUpperUtilizationBound(double upperUtilizationBound) {
		this.upperUtilizationBound = upperUtilizationBound;
	}

	public double getLowerUtilizationBound() {
		return lowerUtilizationBound;
	}
	
	public void setLowerUtilizationBound(double lowerUtilizationBound) {
		this.lowerUtilizationBound = lowerUtilizationBound;
	}

	public double getWorkloadMixVariation() {
		return workloadMixVariation;
	}

	public void setWorkloadMixVariation(double workloadMixVariation) {
		this.workloadMixVariation = workloadMixVariation;
	}	

	public Vector getDemands() {
		return demands;
	}

	public void setDemands(Vector demands) {
		if (demands.rows() != services.length * resources.length) {
			throw new IllegalArgumentException("Row count of demands does not match number of resources * services.");
		}
		this.demands = demands;
	}
	
	public void setRandomDemands() {
		demands = vector(resources.length * services.length, new VectorFunction() {			
			@Override
			public double cell(int row) {
				return randDemands.nextDouble();
			}
		});
	}
	
	public Observation nextObservation() {		
		Vector absoluteWheights = vector(services.length, new VectorFunction() {			
			@Override 
			public double cell(int row) {
				return 1.0 - randWheights.nextDouble() * workloadMixVariation;
			}
		});
				
		final Vector relativeWheights = absoluteWheights.times(sum(absoluteWheights));
		
		Vector weightedTotalDemand = vector(resources.length, new VectorFunction() {			
			@Override
			public double cell(int row) {
				return relativeWheights.dot(demands.slice(model.getState().getRange(resources[row])));
			}
		});	
		
		final int bottleneckResource = max(weightedTotalDemand);
		
		final double maxUtil = randUtil.nextDouble() * (upperUtilizationBound - lowerUtilizationBound) + lowerUtilizationBound;
		
		final double totalThroughput = maxUtil / weightedTotalDemand.get(bottleneckResource);		
		final Vector throughput = vector(services.length, new VectorFunction() {
			@Override
			public double cell(int row) {
				return totalThroughput * relativeWheights.get(row);
			}
			
		});		
		
		final Vector utilization = vector(resources.length, new VectorFunction() {			
			@Override
			public double cell(int row) {
				if (row != bottleneckResource) {
					return throughput.dot(demands.slice(model.getState().getRange(resources[row])));
				} else {
					return maxUtil;
				}
			}
		});
	
		final Vector responsetime = vector(services.length, new VectorFunction() {
			@Override
			public double cell(int row) {
				double sumRT = 0.0;
				for (int r = 0; r < resources.length; r++) {
					sumRT += demands.get(model.getState().getIndex(resources[r], services[row])) / (1 - utilization.get(r));
				}
				return sumRT;
			}
			
		});
		currentObservation = new Observation(utilization, throughput, responsetime);
		
		return currentObservation;
	}
	
	public WorkloadDescription getSystemModel() {
		return model;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Matrix> Result<T> execute(Query<T> query) {
		switch(query.getMetric()) {
		case RESPONSE_TIME:
			if (query.getType() == Type.ALL_SERVICES) {
				return new Result<T>((T) currentObservation.getMeanResponseTime(), services);
			} else if (query.getType() == Type.SERVICE) {
				return new Result<T>((T) LinAlg.scalar(currentObservation.getMeanResponseTime().get(getIndexOfService((Service)query.getEntity()))), 
						new IModelEntity[] { query.getEntity() });
			}
			break;
		case UTILIZATION:
			if (query.getType() == Type.ALL_RESOURCES) {
				return new Result<T>((T) currentObservation.getMeanUtilization(), resources);
			} else if (query.getType() == Type.RESOURCE) {
				return new Result<T>((T) LinAlg.scalar(currentObservation.getMeanUtilization().get(getIndexOfResource((Resource)query.getEntity()))), 
						new IModelEntity[] { query.getEntity() });
			}
			break;
		case THROUGHPUT:
			if (query.getType() == Type.ALL_SERVICES) {
				return new Result<T>((T) currentObservation.getMeanThroughput(), services);
			} else if (query.getType() == Type.SERVICE) {
				return new Result<T>((T) LinAlg.scalar(currentObservation.getMeanThroughput().get(getIndexOfService((Service)query.getEntity()))), 
						new IModelEntity[] { query.getEntity() });
			}
			break;
		}
		return null;
	}
	
	private int getIndexOfResource(Resource resource) {
		for (int i = 0; i < resources.length; i++) {
			if (resources[i].equals(resource)) {
				return i;
			}
		}
		return -1;
	}
	
	private int getIndexOfService(Service service) {
		for (int i = 0; i < services.length; i++) {
			if (services[i].equals(service)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean hasNext(Metric metric) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Vector next(Metric metric) {
		// TODO Auto-generated method stub
		return null;
	}

}
