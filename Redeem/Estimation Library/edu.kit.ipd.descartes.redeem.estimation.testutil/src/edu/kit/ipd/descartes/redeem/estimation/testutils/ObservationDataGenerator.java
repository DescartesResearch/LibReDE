package edu.kit.ipd.descartes.redeem.estimation.testutils;

import static edu.kit.ipd.descartes.linalg.LinAlg.max;
import static edu.kit.ipd.descartes.linalg.LinAlg.range;
import static edu.kit.ipd.descartes.linalg.LinAlg.scalar;
import static edu.kit.ipd.descartes.linalg.LinAlg.sum;
import static edu.kit.ipd.descartes.linalg.LinAlg.vector;

import java.util.Arrays;
import java.util.Random;

import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorFunction;
import edu.kit.ipd.descartes.redeem.estimation.repository.IMonitoringRepository;
import edu.kit.ipd.descartes.redeem.estimation.repository.MemoryObservationRepository;
import edu.kit.ipd.descartes.redeem.estimation.repository.StandardMetric;
import edu.kit.ipd.descartes.redeem.estimation.repository.TimeSeries;
import edu.kit.ipd.descartes.redeem.estimation.workload.Resource;
import edu.kit.ipd.descartes.redeem.estimation.workload.Service;
import edu.kit.ipd.descartes.redeem.estimation.workload.WorkloadDescription;

public class ObservationDataGenerator {
	
	private Random randUtil;
	private Random randWheights;
	private Random randDemands;
	private double upperUtilizationBound = 1.0;
	private double lowerUtilizationBound = 0.0;
	private Vector demands;
	private double workloadMixVariation = 1.0;
	
	private Resource[] resources;
	private Service[] services;
	
	private double time = 1.0;
	
	private WorkloadDescription model;
	
	private MemoryObservationRepository repository;
	
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
		repository = new MemoryObservationRepository(model);
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
	
	public WorkloadDescription getWorkloadDescription() {
		return model;
	}

	public void setDemands(Vector demands) {
		if (demands.rows() != services.length * resources.length) {
			throw new IllegalArgumentException("Size of demands amtrix does not match number of resources x services.");
		}
		this.demands = demands;
	}
	
	public void setRandomDemands() {
		demands = vector(resources.length *services.length, new VectorFunction() {			
			@Override
			public double cell(int row) {
				return randDemands.nextDouble();
			}
		});
	}
	
	public void nextObservation() {		
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
				return relativeWheights.dot(demands.slice(range(row * services.length, (row + 1) * services.length)));
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
		

		for (int i = 0; i < services.length; i++) {
			TimeSeries ts;
			if (time == 1.0) {
				ts = new TimeSeries(scalar(1.0), scalar(throughput.get(i)));
			} else {
				time += 1.0;
				ts = repository.getData(StandardMetric.THROUGHPUT, services[i]);
				ts = ts.addSample(time, throughput.get(i));
			}
			repository.setData(StandardMetric.THROUGHPUT, services[i], ts);	
		}
		
		

		for (int i = 0; i < resources.length; i++) {
			TimeSeries ts;
			if (time == 1.0) {
				ts = new TimeSeries(scalar(1.0), scalar(utilization.get(i)));
			} else {
				time += 1.0;
				ts = repository.getData(StandardMetric.UTILIZATION, resources[i]);
				ts = ts.addSample(time, utilization.get(i));
			}
			repository.setData(StandardMetric.UTILIZATION, resources[i], ts);	
		}
	
		for (int i = 0; i < services.length; i++) {
			double sumRT = 0.0;
			for (int r = 0; r < resources.length; r++) {
				sumRT += demands.get(r * services.length + i) / (1 - utilization.get(r));
			}
			
			TimeSeries ts;
			if (time == 1.0) {
				ts = new TimeSeries(scalar(1.0), scalar(sumRT));
			} else {
				time += 1.0;
				ts = repository.getData(StandardMetric.RESPONSE_TIME, services[i]);
				ts = ts.addSample(time, sumRT);
			}
			repository.setData(StandardMetric.RESPONSE_TIME, services[i], ts);	
		}
	}
	
	public IMonitoringRepository getRepository() {
		return repository;
	}
}
