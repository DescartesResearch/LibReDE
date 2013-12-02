package edu.kit.ipd.descartes.redeem.estimation.testutils;

import static edu.kit.ipd.descartes.linalg.LinAlg.max;
import static edu.kit.ipd.descartes.linalg.LinAlg.scalar;
import static edu.kit.ipd.descartes.linalg.LinAlg.sum;
import static edu.kit.ipd.descartes.linalg.LinAlg.vector;

import java.util.Arrays;
import java.util.Random;

import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorFunction;
import edu.kit.ipd.descartes.redeem.estimation.repository.IMonitoringRepository;
import edu.kit.ipd.descartes.redeem.estimation.repository.MatrixMonitoringRepository;
import edu.kit.ipd.descartes.redeem.estimation.repository.Metric;
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
	
	private double time = 0.0;
	
	private WorkloadDescription model;
	
	private MatrixMonitoringRepository repository;
	
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
		repository = new MatrixMonitoringRepository(model);
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
		

		for (int i = 0; i < services.length; i++) {
			TimeSeries ts;
			if (time == 0.0) {
				ts = new TimeSeries(scalar(0.0), scalar(throughput.get(i)));
			} else {
				time += 1.0;
				ts = repository.getData(Metric.THROUGHPUT, services[i]);
				ts = ts.addSample(time, throughput.get(i));
			}
			repository.setData(Metric.THROUGHPUT, services[i], ts);	
		}
		
		

		for (int i = 0; i < resources.length; i++) {
			TimeSeries ts;
			if (time == 0.0) {
				ts = new TimeSeries(scalar(0.0), scalar(utilization.get(i)));
			} else {
				time += 1.0;
				ts = repository.getData(Metric.UTILIZATION, resources[i]);
				ts = ts.addSample(time, utilization.get(i));
			}
			repository.setData(Metric.UTILIZATION, resources[i], ts);	
		}
	
		for (Service service : services) {
			double sumRT = 0.0;
			for (int r = 0; r < resources.length; r++) {
				sumRT += demands.get(model.getState().getIndex(resources[r], service)) / (1 - utilization.get(r));
			}
			
			TimeSeries ts;
			if (time == 0.0) {
				ts = new TimeSeries(scalar(0.0), scalar(sumRT));
			} else {
				time += 1.0;
				ts = repository.getData(Metric.AVERAGE_RESPONSE_TIME, service);
				ts = ts.addSample(time, sumRT);
			}
			repository.setData(Metric.AVERAGE_RESPONSE_TIME, service, ts);	
		}
	}
	
	public IMonitoringRepository getRepository() {
		return repository;
	}
}
