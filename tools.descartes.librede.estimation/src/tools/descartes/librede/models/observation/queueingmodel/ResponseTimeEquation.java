package tools.descartes.librede.models.observation.queueingmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.State;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.InvocationGraph;
import tools.descartes.librede.models.state.constraints.IStateConstraint;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.units.RequestRate;

public class ResponseTimeEquation extends ModelEquation {
	
	private final Service cls_r;
	private final List<Service> usedServices;
	private final Map<Resource, List<Service>> accessedResources;
	private final Map<Resource, Map<Service, ResidenceTimeEquation>> residenceTimeEquations;
	
	private final Query<Scalar, RequestRate> throughputQuery;

	private InvocationGraph invocations;

	public ResponseTimeEquation(IStateModel<? extends IStateConstraint> stateModel, IRepositoryCursor cursor,
			Service service, boolean useObservedUtilization, int historicInterval) {
		super(stateModel, historicInterval);
		
		cls_r = service;
		this.invocations = stateModel.getInvocationGraph();

		// This equation is based on the end-to-end response time
		// therefore its scope includes all directly and indirectly
		// called services.
		usedServices = new ArrayList<>(invocations.getCalledServices(cls_r));
		accessedResources = getAccessedResources(usedServices);
		residenceTimeEquations = new HashMap<>();
		for (Resource res : accessedResources.keySet()) {
			Map<Service, ResidenceTimeEquation> currentMap = residenceTimeEquations.get(res);
			if (currentMap == null) {
				currentMap = new HashMap<>();
				residenceTimeEquations.put(res, currentMap);
			}

			ModelEquation utilFunction;
			if (useObservedUtilization) {
				utilFunction = new UtilizationValue(getStateModel(), cursor, res, historicInterval);
			} else {
				utilFunction = new UtilizationLawEquation(getStateModel(), cursor, res, historicInterval);
			}
			
			for (Service serv : res.getAccessingServices()) {
				WaitingTimeEquation waitingTime = WaitingTimeEquation.create(getStateModel(), cursor, serv, res, historicInterval,
						utilFunction);
				currentMap.put(serv, new ResidenceTimeEquation(stateModel, cursor, serv, res, historicInterval, waitingTime));
			}
		}

		throughputQuery = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND)
				.forService(cls_r).average().using(cursor);
		addDataDependency(throughputQuery);

	}
	
	@Override
	public DerivativeStructure getValue(State state) {
		Vector X = throughputQuery.get(historicInterval);
		if (X.get(throughputQuery.indexOf(cls_r)) == 0.0) {
			// no request observed in this interval
			return new DerivativeStructure(state.getStateSize(), state.getDerivationOrder(), 0.0);
		}

		DerivativeStructure rt = null;
		for (Resource res_i : accessedResources.keySet()) {
			Map<Service, ResidenceTimeEquation> accessingServices = residenceTimeEquations.get(res_i);
			for (Service curService : accessingServices.keySet()) {
				double visits = 1;
				if (!curService.equals(cls_r)) {
					visits = invocations.getInvocationCount(cls_r, curService, historicInterval);
				}
				ResidenceTimeEquation R_ir = accessingServices.get(curService);
				DerivativeStructure curRt = R_ir.getValue(state).multiply(visits);
				if (rt == null) {
					rt = curRt;
				} else {
					rt = rt.add(curRt);
				}
			}
		}
		return rt;
	}
	
	@Override
	public boolean hasData() {
		boolean ret = throughputQuery.hasData(historicInterval);
		for (Map<Service, ResidenceTimeEquation> res : residenceTimeEquations.values()) {
			for (ResidenceTimeEquation eq : res.values()) {
				ret = ret && eq.hasData();
			}
		}
		return ret;
	}
	
	@Override
	public boolean isLinear() {
		return false;
	}
	
	
	@Override
	public boolean isConstant() {
		return false;
	}
	
	
	/**
	 * Collects all accessed resources of the given services.
	 * 
	 * @param services
	 *            a List of Service
	 * @return a Map containing the accessing services for each resource
	 */
	private Map<Resource, List<Service>> getAccessedResources(List<Service> services) {
		Map<Resource, List<Service>> resources = new HashMap<>();
		for (Service curService : services) {
			List<Resource> accessedResources = curService.getAccessedResources();
			for (Resource curResource : accessedResources) {
				List<Service> accessingServices = resources.get(curResource);
				if (accessingServices == null) {
					accessingServices = new LinkedList<Service>();
					resources.put(curResource, accessingServices);
				}
				accessingServices.add(curService);
			}
		}
		return resources;
	}

}
