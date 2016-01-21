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

import static tools.descartes.librede.linalg.LinAlg.indices;
import static tools.descartes.librede.linalg.LinAlg.scalar;
import static tools.descartes.librede.linalg.LinAlg.sum;
import static tools.descartes.librede.linalg.LinAlg.vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import tools.descartes.librede.configuration.ConfigurationFactory;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.linalg.Indices;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorFunction;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.State;
import tools.descartes.librede.models.observation.functions.ErlangCEquation;
import tools.descartes.librede.models.state.ConstantStateModel;
import tools.descartes.librede.models.state.ConstantStateModel.Builder;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.InvocationGraph;
import tools.descartes.librede.models.state.constraints.Unconstrained;
import tools.descartes.librede.repository.IMonitoringRepository;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.MemoryObservationRepository;
import tools.descartes.librede.repository.TimeSeries;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Ratio;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.UnitsFactory;

public class ObservationDataGenerator {
	
	private static final Quantity<Time> ONE_SECOND = UnitsFactory.eINSTANCE.createQuantity(1, Time.SECONDS);
	
	private Random randUtil;
	private Random randWheights;
	private Random randDemands;
	private double upperUtilizationBound = 1.0;
	private double lowerUtilizationBound = 0.0;
	private Vector demands;
	private double workloadMixVariation = 1.0;
	private ErlangCEquation erlangC;
	private int numServers;
	
	private Resource[] resources;
	private List<Indices> resToServ;
	private Map<Resource, Integer> resToIdx;
	private Service[] services;
	
	private double time = 1.0;
	
	private WorkloadDescription model;
	
	private IStateModel<Unconstrained> stateModel;
	
	private MemoryObservationRepository repository;
	
	private IRepositoryCursor cursor;
	
	public ObservationDataGenerator(long seed, int numWorkloadClasses, int numResources) {
		this(seed, numWorkloadClasses, numResources, 1);
	}
	
	public ObservationDataGenerator(long seed, int numWorkloadClasses, int numResources, boolean[][] mapping) {
		this(seed, numWorkloadClasses, numResources, 1, mapping);
	}
	
	public ObservationDataGenerator(long seed, int numWorkloadClasses, int numResources, int numServers) {
		boolean[][] mapping = new boolean[numWorkloadClasses][numResources];
		for (boolean[] m : mapping) {
			Arrays.fill(m, true);
		}
		init(seed, numWorkloadClasses, numResources, numServers, mapping);
	}
	
	public ObservationDataGenerator(long seed, int numWorkloadClasses, int numResources, int numServers, boolean[][] mapping) {
		init(seed, numWorkloadClasses, numResources, numServers, mapping);
	}
	
	private void init(long seed, int numWorkloadClasses, int numResources, int numServers, boolean[][] mapping) {
		Random randSeed = new Random(seed);
		randUtil = new Random(randSeed.nextLong());
		randWheights = new Random(randSeed.nextLong());
		randDemands = new Random(randSeed.nextLong());
		this.numServers = numServers;
		erlangC = new ErlangCEquation(numServers);
		
		model = ConfigurationFactory.eINSTANCE.createWorkloadDescription();
		
		services = new Service[numWorkloadClasses];
		resources = new Resource[numResources];
		for (int i = 0; i < numWorkloadClasses; i++) {
			services[i] = ConfigurationFactory.eINSTANCE.createService();
			services[i].setName("WC" + i);
		}
		model.getServices().addAll(Arrays.asList(services));
		
		resToServ = new ArrayList<>(numResources);
		resToIdx = new HashMap<>();
		for (int i = 0; i < numResources; i++) {
			resources[i] = ConfigurationFactory.eINSTANCE.createResource();
			resources[i].setName("R" + i);
			resources[i].setNumberOfServers(numServers);
			resToIdx.put(resources[i], i);
			
			final List<Integer> idx = new ArrayList<>(numWorkloadClasses);
			for (int j = 0; j < numWorkloadClasses; j++) {
				if (mapping[j][i]) {
					ResourceDemand d = ConfigurationFactory.eINSTANCE.createResourceDemand();
					d.setName(resources[i].getName());
					d.setResource(resources[i]);
					services[j].getTasks().add(d);
					idx.add(j);
				}
			}
			resToServ.add(indices(idx.size(), new VectorFunction() {				
				@Override
				public double cell(int row) {
					return idx.get(row);
				}
			}));
		}		
		model.getResources().addAll(Arrays.asList(resources));
		
		repository = new MemoryObservationRepository(model);
		cursor = repository.getCursor(UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS), ONE_SECOND);
		
		Builder<Unconstrained> builder = ConstantStateModel.unconstrainedModelBuilder();
		for (Resource res : resources) {
			for (ResourceDemand demand : res.getDemands()) {
				builder.addVariable(demand);
			}
		}
		builder.setInvocationGraph(new InvocationGraph(model.getServices(), cursor, 1));
		stateModel = builder.build();
		

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

	public State getDemands() {
		return new State(stateModel, demands);
	}
	
	public WorkloadDescription getWorkloadDescription() {
		return model;
	}

	public void setDemands(Vector demands) {
		if (demands.rows() != stateModel.getStateSize()) {
			throw new IllegalArgumentException("Size of demands matrix does not match the state size");
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
		final Vector totalDemands = vector(services.length, new VectorFunction() {
			@Override
			public double cell(int row) {
				Service s = services[row];
				double sumD = 0.0;
				for (Resource r : s.getAccessedResources()) {
					sumD += demands.get(stateModel.getStateVariableIndex(r, s));
				}				
				return sumD;
			}
		});		
		
		Vector absoluteWheights = vector(services.length, new VectorFunction() {			
			@Override 
			public double cell(int row) {
				if (totalDemands.get(row) == 0.0) {
					return 0.0;
				} else {
					return 1.0 - randWheights.nextDouble() * workloadMixVariation;
				}
			}
		});
				
		final Vector relativeWheights = absoluteWheights.times(1 / sum(absoluteWheights).get(0));
		
		Vector weightedTotalDemand = vector(resources.length, new VectorFunction() {			
			@Override
			public double cell(int row) {
				return relativeWheights.get(resToServ.get(row)).dot(demands.get(stateModel.getStateVariableIndices(resources[row])));
			}
		});	
		
		int maxIdx = -1;
		double max = Double.MIN_VALUE;
		for (int i = 0; i < weightedTotalDemand.rows(); i++) {
			if (weightedTotalDemand.get(i) > max) {
				maxIdx = i;
				max = weightedTotalDemand.get(i);
			}
		}
		final int bottleneckResource = maxIdx;
		
		final double maxUtil = randUtil.nextDouble() * (upperUtilizationBound - lowerUtilizationBound) + lowerUtilizationBound;
		
		final double totalThroughput = numServers * (maxUtil / weightedTotalDemand.get(bottleneckResource));		
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
					return throughput.get(resToServ.get(row)).dot(demands.get(stateModel.getStateVariableIndices(resources[row]))) / numServers;
				} else {
					return maxUtil;
				}
			}
		});
		

		for (int i = 0; i < services.length; i++) {
			TimeSeries ts;
			if (time == 1.0) {
				ts = new TimeSeries(scalar(time), scalar(throughput.get(i)));
				ts.setStartTime(0);
			} else {
				ts = repository.select(StandardMetrics.THROUGHPUT, RequestRate.REQ_PER_SECOND, services[i], Aggregation.AVERAGE);
				ts = ts.addSample(time, throughput.get(i));
			}
			ts.setEndTime(time);
			repository.insert(StandardMetrics.THROUGHPUT, RequestRate.REQ_PER_SECOND, services[i], ts, Aggregation.AVERAGE, ONE_SECOND);	
		}
		
		

		for (int i = 0; i < resources.length; i++) {
			TimeSeries ts;
			if (time == 1.0) {
				ts = new TimeSeries(scalar(time), scalar(utilization.get(i)));
				ts.setStartTime(0);
			} else {
				ts = repository.select(StandardMetrics.UTILIZATION, Ratio.NONE, resources[i], Aggregation.AVERAGE);
				ts = ts.addSample(time, utilization.get(i));
			}
			ts.setEndTime(time);
			repository.insert(StandardMetrics.UTILIZATION, Ratio.NONE, resources[i], ts, Aggregation.AVERAGE, ONE_SECOND);	
		}
	
		for (int i = 0; i < services.length; i++) {
			double sumRT = 0.0;
			for (Resource resource : services[i].getAccessedResources()) {
				int stateIdx = stateModel.getStateVariableIndex(resource, services[i]);
				double util = utilization.get(resToIdx.get(resource));
				sumRT += (demands.get(stateIdx) * (erlangC.calculateValue(util) + 1 - util)) / (1 - util);
			}
			
			TimeSeries ts;
			if (time == 1.0) {
				ts = new TimeSeries(scalar(time), scalar(sumRT));
				ts.setStartTime(0);
			} else {
				ts = repository.select(StandardMetrics.RESPONSE_TIME, Time.SECONDS, services[i], Aggregation.AVERAGE);
				ts = ts.addSample(time, sumRT);
			}
			ts.setEndTime(time);
			repository.insert(StandardMetrics.RESPONSE_TIME, Time.SECONDS, services[i], ts, Aggregation.AVERAGE, ONE_SECOND);	
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
	
	public IRepositoryCursor getCursor() {
		return cursor;
	}
}
