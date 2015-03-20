/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2014, by Simon Spinner and Contributors.
 *
 * Project Info:   http://www.descartes-research.net/
 *
 * All rights reserved. This software is made available under the terms of the
 * Eclipse Public License (EPL) v1.0 as published by the Eclipse Foundation
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse Public License (EPL)
 * for more details.
 *
 * You should have received a copy of the Eclipse Public License (EPL)
 * along with this software; if not visit http://www.eclipse.org or write to
 * Eclipse Foundation, Inc., 308 SW First Avenue, Suite 110, Portland, 97204 USA
 * Email: license (at) eclipse.org
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 */
package tools.descartes.librede.testutils;

import static tools.descartes.librede.linalg.LinAlg.range;
import static tools.descartes.librede.linalg.LinAlg.scalar;
import static tools.descartes.librede.linalg.LinAlg.sum;
import static tools.descartes.librede.linalg.LinAlg.vector;

import java.util.Arrays;
import java.util.Random;

import tools.descartes.librede.configuration.ConfigurationFactory;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorFunction;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.state.ConstantStateModel;
import tools.descartes.librede.models.state.ConstantStateModel.Builder;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.Unconstrained;
import tools.descartes.librede.repository.IMonitoringRepository;
import tools.descartes.librede.repository.MemoryObservationRepository;
import tools.descartes.librede.repository.StandardMetricHelpers;
import tools.descartes.librede.repository.TimeSeries;
import tools.descartes.librede.units.Ratio;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.UnitsFactory;

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
	
	private IStateModel<Unconstrained> stateModel;
	
	private MemoryObservationRepository repository;
	
	public ObservationDataGenerator(long seed, int numWorkloadClasses, int numResources) {
		Random randSeed = new Random(seed);
		randUtil = new Random(randSeed.nextLong());
		randWheights = new Random(randSeed.nextLong());
		randDemands = new Random(randSeed.nextLong());
		
		services = new Service[numWorkloadClasses];
		resources = new Resource[numResources];
		for (int i = 0; i < numWorkloadClasses; i++) {
			services[i] = ConfigurationFactory.eINSTANCE.createService();
			services[i].setName("WC" + i);
		}
		
		for (int i = 0; i < numResources; i++) {
			resources[i] = ConfigurationFactory.eINSTANCE.createResource();
			resources[i].setName("R" + i);
		}
		
		model = ConfigurationFactory.eINSTANCE.createWorkloadDescription();
		model.getResources().addAll(Arrays.asList(resources));
		model.getServices().addAll(Arrays.asList(services));
		
		Builder<Unconstrained> builder = ConstantStateModel.unconstrainedModelBuilder();
		for (Resource res : resources) {
			for (Service serv : services) {
				builder.addVariable(res, serv);
			}
		}
		stateModel = builder.build();
		
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
			throw new IllegalArgumentException("Size of demands matrix does not match number of resources x services.");
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
		
		int maxIdx = -1;
		double max = Double.MIN_VALUE;
		for (int i = 0; i < weightedTotalDemand.rows(); i++) {
			if (maxIdx < 0) {
				maxIdx = i;
			}
			if (weightedTotalDemand.get(i) > max) {
				maxIdx = i;
			}
		}
		final int bottleneckResource = maxIdx;
		
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
					return throughput.dot(demands.slice(stateModel.getStateVariableIndexRange(resources[row])));
				} else {
					return maxUtil;
				}
			}
		});
		

		for (int i = 0; i < services.length; i++) {
			TimeSeries ts = repository.select(StandardMetrics.THROUGHPUT, RequestRate.REQ_PER_SECOND, services[i]);
			if (ts.isEmpty()) {
				ts = new TimeSeries(scalar(time), scalar(throughput.get(i)));
				ts.setStartTime(0);
			} else {
				ts = ts.addSample(time, throughput.get(i));
			}
			ts.setEndTime(time);
			repository.insert(StandardMetrics.THROUGHPUT, RequestRate.REQ_PER_SECOND, services[i], ts);	
		}
		
		

		for (int i = 0; i < resources.length; i++) {
			TimeSeries ts = repository.select(StandardMetrics.UTILIZATION, Ratio.NONE, resources[i]);
			if (ts.isEmpty()) {
				ts = new TimeSeries(scalar(time), scalar(utilization.get(i)));
				ts.setStartTime(0);
			} else {
				ts = ts.addSample(time, utilization.get(i));
			}
			ts.setEndTime(time);
			repository.insert(StandardMetrics.UTILIZATION, Ratio.NONE, resources[i], ts);	
		}
	
		for (int i = 0; i < services.length; i++) {
			double sumRT = 0.0;
			for (int r = 0; r < resources.length; r++) {
				sumRT += demands.get(r * services.length + i) / (1 - utilization.get(r));
			}
			
			TimeSeries ts = repository.select(StandardMetrics.RESPONSE_TIME, Time.SECONDS, services[i]);
			if (time == 1.0) {
				ts = new TimeSeries(scalar(time), scalar(sumRT));
				ts.setStartTime(0);
			} else {
				ts = ts.addSample(time, sumRT);
			}
			ts.setEndTime(time);
			repository.insert(StandardMetrics.RESPONSE_TIME, Time.SECONDS, services[i], ts);	
		}
		
		repository.setCurrentTime(UnitsFactory.eINSTANCE.createQuantity(time, Time.SECONDS));
		
		time++;
	}
	
	public IMonitoringRepository getRepository() {
		return repository;
	}
	
	public IStateModel<Unconstrained> getStateModel() {
		return stateModel;
	}
}
