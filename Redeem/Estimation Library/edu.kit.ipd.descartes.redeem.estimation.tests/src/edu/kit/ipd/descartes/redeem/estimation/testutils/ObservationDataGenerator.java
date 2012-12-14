package edu.kit.ipd.descartes.redeem.estimation.testutils;

import java.util.Random;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;

public class ObservationDataGenerator {
	
	private Random randUtil;
	private Random randWheights;
	private double upperUtilizationBound = 0.0;
	private double lowerUtilizationBound = 1.0;
	private Matrix demands;
	private int numWorkloadClasses;
	private int numResources;
	private double workloadMixVariation = 1.0;
	
	public ObservationDataGenerator(long seed, int numWorkloadClasses, int numResources) {
		Random randSeed = new Random(seed);
		randUtil = new Random(randSeed.nextLong());
		randWheights = new Random(randSeed.nextLong());
		this.numResources = numResources;
		this.numWorkloadClasses = numWorkloadClasses;
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

	public Matrix getDemands() {
		return demands;
	}

	public void setDemands(Matrix demands) {
		if (demands.rowCount() != numWorkloadClasses) {
			throw new IllegalArgumentException("Row count of demands does not match number of workload classes.");
		}
		if (demands.columnCount() != numResources) {
			throw new IllegalArgumentException("Column count of demands does not match number of resources.");
		}
		this.demands = demands;
	}
	
	public Observation nextObservation() {		
		Vector wheights = Vector.create(numWorkloadClasses);
		for (int i = 0; i < numWorkloadClasses; i++) {
			wheights.set(i, 1.0 - randWheights.nextDouble() * workloadMixVariation);
		}
		double totalWheight = wheights.sum();
		wheights.mapMultiplyToSelf(totalWheight);
		
		Vector weightedTotalDemand = Vector.create(numResources);
		int bottleneckResource = -1;
		double maxWeightedDemand = 0.0;
		
		for (int r = 0; r < numResources; r++) {
			double demand = wheights.multiply(demands.columnVector(r));
			weightedTotalDemand.set(r, demand);
			if (demand > maxWeightedDemand) {
				maxWeightedDemand = demand;
				bottleneckResource = r;
			}			
		}
		
		double maxUtil = randUtil.nextDouble() * (upperUtilizationBound - lowerUtilizationBound) + lowerUtilizationBound;
		
		double totalThroughput = maxUtil / weightedTotalDemand.get(bottleneckResource);		
		Vector throughput = Vector.create(numWorkloadClasses);		
		for (int i = 0; i < numWorkloadClasses; i++) {			
			throughput.set(i, totalThroughput * wheights.get(i));
		}		
		
		Vector utilization = Vector.create(numResources);
		utilization.set(bottleneckResource, maxUtil);
		for (int r = 0; r < numResources; r++) {
			if (r != bottleneckResource) {
				utilization.set(r, throughput.multiply(demands.columnVector(r)));
			}
		}
	
		Vector responsetime = Vector.create(numWorkloadClasses);
		for (int i = 0; i < numWorkloadClasses; i++) {
			double sumRT = 0.0;
			for (int r = 0; r < numResources; r++) {
				sumRT += demands.get(i, r) / (1 - utilization.get(r));
			}
			responsetime.set(i, sumRT);
		}
		return new Observation(utilization, throughput, responsetime);
	}

}
