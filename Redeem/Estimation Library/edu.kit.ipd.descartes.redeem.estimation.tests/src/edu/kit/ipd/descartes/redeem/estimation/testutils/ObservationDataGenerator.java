package edu.kit.ipd.descartes.redeem.estimation.testutils;

import static edu.kit.ipd.descartes.linalg.LinAlg.*;

import java.util.Random;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorInitializer;

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
		if (demands.rows() != numWorkloadClasses) {
			throw new IllegalArgumentException("Row count of demands does not match number of workload classes.");
		}
		if (demands.columns() != numResources) {
			throw new IllegalArgumentException("Column count of demands does not match number of resources.");
		}
		this.demands = demands;
	}
	
	public Observation nextObservation() {		
		Vector absoluteWheights = vector(numWorkloadClasses, new VectorInitializer() {			
			@Override 
			public double cell(int row) {
				return 1.0 - randWheights.nextDouble() * workloadMixVariation;
			}
		});
				
		final Vector relativeWheights = absoluteWheights.times(sum(absoluteWheights));
		
		Vector weightedTotalDemand = vector(numResources, new VectorInitializer() {			
			@Override
			public double cell(int row) {
				return relativeWheights.dot(demands.column(row));
			}
		});	
		
		final int bottleneckResource = max(weightedTotalDemand);
		
		final double maxUtil = randUtil.nextDouble() * (upperUtilizationBound - lowerUtilizationBound) + lowerUtilizationBound;
		
		final double totalThroughput = maxUtil / weightedTotalDemand.get(bottleneckResource);		
		final Vector throughput = vector(numWorkloadClasses, new VectorInitializer() {
			@Override
			public double cell(int row) {
				return totalThroughput * relativeWheights.get(row);
			}
			
		});		
		
		final Vector utilization = vector(numResources, new VectorInitializer() {			
			@Override
			public double cell(int row) {
				if (row != bottleneckResource) {
					return throughput.dot(demands.column(row));
				} else {
					return maxUtil;
				}
			}
		});
	
		final Vector responsetime = vector(numWorkloadClasses, new VectorInitializer() {
			@Override
			public double cell(int row) {
				double sumRT = 0.0;
				for (int r = 0; r < numResources; r++) {
					sumRT += demands.get(row, r) / (1 - utilization.get(r));
				}
				return sumRT;
			}
			
		});
		return new Observation(utilization, throughput, responsetime);
	}

}
