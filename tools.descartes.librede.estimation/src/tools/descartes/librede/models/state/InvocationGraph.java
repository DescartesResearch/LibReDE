/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2018, by Simon Spinner, Johannes Grohmann
 *  and Contributors.
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
package tools.descartes.librede.models.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tools.descartes.librede.configuration.ExternalCall;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.repository.rules.DataDependency;
import tools.descartes.librede.repository.rules.DependencyScope;
import tools.descartes.librede.repository.rules.IDependencyTarget;
import tools.descartes.librede.units.RequestCount;
import tools.descartes.librede.units.Time;

public class InvocationGraph implements IDependencyTarget {
	private final int historySize;
	private final Query<Vector, RequestCount> visitCountQuery;
	private final Query<Vector, Time> delayQuery;
	private final double[][][] invocations;
	private final double[][][] delays;
	private final boolean[][] reachability;
	private final List<Service> services;
	private final Map<Service, Integer> servicesToIdx = new HashMap<>();
	private final int serviceCount;
	private int last = 0;
	private final boolean externalCalls;
	
	public InvocationGraph(List<Service> services, IRepositoryCursor cursor, int historySize) {
		if (historySize < 1) {
			throw new IllegalArgumentException();
		}		
		this.historySize = historySize;
		this.services = services;

		serviceCount = services.size();
		for (int i = 0; i < serviceCount; i++) {
			servicesToIdx.put(services.get(i), i);
		}
		invocations = new double[historySize][serviceCount][serviceCount];
		delays = new double[historySize][serviceCount][serviceCount];
		List<ExternalCall> calls = getExternalCalls(services);
		this.externalCalls = !calls.isEmpty();
		if (!externalCalls) {
			reachability = new boolean[serviceCount][serviceCount];
			visitCountQuery = null;
			delayQuery = null;
		} else {
			reachability = calculateReachabilities(serviceCount, calls);			
			visitCountQuery = QueryBuilder.select(StandardMetrics.VISITS).in(RequestCount.REQUESTS)
					.forExternalCalls(calls).average().using(cursor);
			delayQuery = QueryBuilder.select(StandardMetrics.DELAY).in(Time.SECONDS)
					.forExternalCalls(calls).average().using(cursor);
		}
	}
	
	public double getInvocationDelay(Service a, Service b) {
		return getInvocationDelay(a, b, 0);
	}
	
	public double getInvocationDelay(Service a, Service b, int historicInterval) {
		int idxA = servicesToIdx.get(a);
		int idxB = servicesToIdx.get(b);
		int interval = last - historicInterval;
		return delays[(interval < 0) ? (historySize + interval) : interval][idxA][idxB];
	}
	
	public double getInvocationCount(Service a, Service b) {
		return getInvocationCount(a, b, 0);
	}
	
	public double getInvocationCount(Service a, Service b, int historicInterval) {
		int idxA = servicesToIdx.get(a);
		int idxB = servicesToIdx.get(b);
		int interval = last - historicInterval;
		return invocations[(interval < 0) ? (historySize + interval) : interval][idxA][idxB];
	}
	
	public boolean isCycleFree() {
		boolean cycleFree = true;
		for (int i = 0; (i < serviceCount) && cycleFree; i++) {
			cycleFree = cycleFree && reachability[i][i];
		}
		return cycleFree;
	}
	
	public void step() {
		if (externalCalls) {
			last = (last + 1) % historySize;
			updateInvocationMatrix();
			calculateTransitiveClosureOfInvocationCounts();
		}
	}
	
	private void updateInvocationMatrix() {
		for (int i = 0; i < serviceCount; i++) {
			Arrays.fill(invocations[last][i], 0);
		}	
		
		Vector visits = visitCountQuery.get(0);
		Vector delayVector = delayQuery.get(0);
		for (int i = 0; i < visits.rows(); i++) {
			ExternalCall call = (ExternalCall)visitCountQuery.getEntity(i);
			int idx1 = services.indexOf(call.getService());
			int idx2 = services.indexOf(call.getCalledService());
			invocations[last][idx1][idx2] += visits.get(i);
			delays[last][idx1][idx2] += delayVector.get(i);
		}
	}
	
	private void calculateTransitiveClosureOfInvocationCounts() {
		// This is an adapted version of the Floyd-Warshall algorithm
		// The original algorithm is used for determining the shortest
		// paths between all nodes. In each step it updates the
		// weights with the minimum between the existing and the 
		// current path.
		// In our case, the weights of subsequent edges in a path are
		// not added but multiplied (if A calls B 2 times and B calls
		// C 3 times, then A calls C 6 times) and we sum up the call
		// counts for all possible paths, instead of taking the mininum)
		for (int k = 0; k < serviceCount; k++) {
			for (int i = 0; i < serviceCount; i++) {
				for (int j = 0; j < serviceCount; j++) {
					invocations[last][i][j] += invocations[last][i][k] * invocations[last][k][j];
				}
			}
		}
	}
	
	private boolean[][] calculateReachabilities(int serviceCount, List<ExternalCall> externalCalls) {
		// Use Warshall algorithm on a unweighted graph
		boolean[][] reachability = new boolean[serviceCount][serviceCount];
		for (ExternalCall c: externalCalls) {
			int idx1 = servicesToIdx.get(c.getService());
			int idx2 = servicesToIdx.get(c.getCalledService());
			reachability[idx1][idx2] = true;
		}		
		
		for (int k = 0; k < serviceCount; k++) {
			for (int i = 0; i < serviceCount; i++) {
				for (int j = 0; j < serviceCount; j++) {
					reachability[i][j] = reachability[i][j] || (reachability[i][k] && reachability[k][j]);
				}
			}
		}
		return reachability;
	}

	/**
	 * This method returns all directly or indirectly called services
	 * 
	 * @param service
	 *            the start point of the search.
	 * @return a Set of all called services.
	 */
	public Set<Service> getCalledServices(Service base) {
		int idx = servicesToIdx.get(base);
		Set<Service> calledServices = new HashSet<>();
		calledServices.add(base);
		for (int i = 0; i < serviceCount; i++) {
			if (reachability[idx][i]) {
				calledServices.add(services.get(i));
			}
		}
		return calledServices;
	}
	
	private static List<ExternalCall> getExternalCalls(List<Service> services) {
		List<ExternalCall> calls = new ArrayList<>();
		for (Service curService : services) {
			calls.addAll(curService.getOutgoingCalls());
		}
		return calls;
	}

	@Override
	public List<DataDependency<?>> getDataDependencies() {
		if (!externalCalls) {
			return Collections.emptyList();
		} else {
			return Arrays.<DataDependency<?>>asList(
					new DataDependency<>(visitCountQuery.getMetric(), visitCountQuery.getAggregation(), DependencyScope.fixedScope(visitCountQuery.getEntities())),
					new DataDependency<>(delayQuery.getMetric(), delayQuery.getAggregation(), DependencyScope.fixedScope(delayQuery.getEntities()))
					);
		}
	}

}
