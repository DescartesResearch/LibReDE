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
package tools.descartes.librede.connector.dml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import edu.kit.ipd.descartes.mm.applicationlevel.repository.AssemblyContext;
import edu.kit.ipd.descartes.mm.applicationlevel.repository.Signature;
import edu.kit.ipd.descartes.mm.resourceconfiguration.ConfigurationSpecification;
import edu.kit.ipd.descartes.mm.resourceconfiguration.ProcessingResourceSpecification;
import edu.kit.ipd.descartes.mm.resourceconfiguration.SchedulingPolicy;
import edu.kit.ipd.descartes.mm.resourcelandscape.Container;
import edu.kit.ipd.descartes.mm.resourcetype.ResourceType;
import tools.descartes.librede.configuration.ConfigurationFactory;
import tools.descartes.librede.configuration.ExternalCall;
import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.configuration.SchedulingStrategy;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.Task;
import tools.descartes.librede.configuration.WorkloadDescription;

public class DmlMapping {
	
	private static class ResourceMapping {
		public Container container;
		public Resource resource;
	}
	
	private static class ServiceMapping {
		public AssemblyContext context;
		public Signature signature;
		public Container container;
		public Service service;
	}
	
	private static class ExternalCallKey {
		public final Service service;
		public final Service calledService;
		public ExternalCallKey(Service service, Service calledService) {
			super();
			this.service = service;
			this.calledService = calledService;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((calledService == null) ? 0 : calledService.hashCode());
			result = prime * result + ((service == null) ? 0 : service.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ExternalCallKey other = (ExternalCallKey) obj;
			if (calledService == null) {
				if (other.calledService != null)
					return false;
			} else if (!calledService.equals(other.calledService))
				return false;
			if (service == null) {
				if (other.service != null)
					return false;
			} else if (!service.equals(other.service))
				return false;
			return true;
		}
	}
	
	private BiMap<edu.kit.ipd.descartes.mm.applicationlevel.servicebehavior.ResourceDemand, ResourceDemand> demands = HashBiMap.create();
	private BiMap<ExternalCallKey, ExternalCall> calls = HashBiMap.create();
	private Map<String, ServiceMapping> services = new HashMap<>();
	private Map<String, ResourceMapping> resources = new HashMap<>();
	
	private Set<ModelEntity> newEntities = new HashSet<>();
	private Set<String> unmappedServices = new HashSet<>();
	private Set<String> unmappedResources = new HashSet<>();
	
	private WorkloadDescription workload = ConfigurationFactory.eINSTANCE.createWorkloadDescription();
	
	public WorkloadDescription getWorkload() {
		return workload;
	}
	
	public void reset() {
		newEntities.clear();
		unmappedResources.clear();
		unmappedServices.clear();
		
		unmappedResources.addAll(resources.keySet());
		unmappedServices.addAll(services.keySet());
	}
	
	public Set<ModelEntity> getNewEntities() {
		return newEntities;
	}
	
	public ExternalCall getLibredeCall(edu.kit.ipd.descartes.mm.applicationlevel.servicebehavior.ExternalCall dmlCall) {
		return calls.get(dmlCall);
	}
	
	public edu.kit.ipd.descartes.mm.applicationlevel.servicebehavior.ResourceDemand getDmlDemand(ResourceDemand libredeDemand) {
		return demands.inverse().get(libredeDemand);
	}
	
	public void removeUnmappedEntites() {
		Set<String> servicesToUnmap = new HashSet<>(unmappedServices);
		for (String serviceName : servicesToUnmap) {
			ServiceMapping sm = services.get(serviceName);
			if (sm != null) {
				unmapService(sm.service);
			}
		}
		Set<String> resourcesToUnmap = new HashSet<>(unmappedResources);
		for (String resourceName : resourcesToUnmap) {
			ResourceMapping rm = resources.get(resourceName);
			if (rm != null) {
				unmapResource(rm.resource);
			}
		}
	}
	
	public void unmapService(Service service) {
		workload.getServices().remove(service);
		services.remove(service.getName());
		newEntities.remove(service);
		unmappedServices.remove(service.getName());
		for (Task task : service.getTasks()) {
			newEntities.remove(task);
			if (task instanceof ResourceDemand) {
				demands.inverse().remove(task);				
			} else if (task instanceof ExternalCall) {
				calls.inverse().remove(task);
			}			
		}		
	}
	
	public void unmapResource(Resource resource) {
		workload.getResources().remove(resource);
		resources.remove(resource);
		newEntities.remove(resource);
		unmappedResources.remove(resource);
	}
	
	public void unmapExternalCall(ExternalCall call) {
		newEntities.remove(call);
		calls.inverse().remove(call);
	}
	
	public Service mapService(AssemblyContext context, Signature signature, Container container) {
		return mapService(context.getName() + "#" + signature.getName(), context, signature, container); // TODO: cases with several providing roles with the same interfaces are there
	}
	
	public Service mapService(String serviceName, AssemblyContext context, Signature signature, Container container) {
		unmappedServices.remove(serviceName);
		ServiceMapping mapping = services.get(serviceName);
		if (mapping == null) {
			Service service = ConfigurationFactory.eINSTANCE.createService();
			service.setName(serviceName);
			
			mapping = new ServiceMapping();
			mapping.context = context;
			mapping.signature = signature;
			mapping.container = container;
			mapping.service = service;
			services.put(serviceName, mapping);
			workload.getServices().add(service);
			newEntities.add(service);
		}
		return mapping.service;		
	}
	
	public ResourceDemand mapResourceDemand(Resource resource, Service service, edu.kit.ipd.descartes.mm.applicationlevel.servicebehavior.ResourceDemand dmlDemand) {
		ResourceDemand demand = demands.get(dmlDemand);
		if (demand != null) {
			return demand;
		} else {
			demand = ConfigurationFactory.eINSTANCE.createResourceDemand();
			demand.setResource(resource);
			demand.setName(resource.getName());
			service.getTasks().add(demand);
			demands.put(dmlDemand, demand);
			newEntities.add(demand);
			return demand;
		}
	}
	
	public boolean contains(edu.kit.ipd.descartes.mm.applicationlevel.servicebehavior.ResourceDemand dmlDemand) {
		return demands.containsValue(dmlDemand);
	}

	public Resource mapResource(Container container, ResourceType resType) {
		String resourceName = container.getName() + "_" + resType.getName();
		ResourceMapping rm = resources.get(resourceName);
		ProcessingResourceSpecification procRes = getProcessingResource(container, resType);
		if (procRes != null) {
			if (rm == null) {
				rm = new ResourceMapping();
				rm.resource = ConfigurationFactory.eINSTANCE.createResource();
				rm.container = container;
				resources.put(resourceName, rm);
				workload.getResources().add(rm.resource);
				newEntities.add(rm.resource);
			}
			rm.resource.setName(resourceName);
			rm.resource.setNumberOfServers(procRes.getNrOfParProcUnits().getNumber());
			rm.resource.setSchedulingStrategy(convertSchedulingStrategy(procRes.getSchedulingPolicy()));							
		} else {
			return null;
		}
		unmappedResources.remove(resourceName);
		return rm.resource;
	}
	
	public ExternalCall mapExternalCall(Service service, Service calledService, edu.kit.ipd.descartes.mm.applicationlevel.servicebehavior.ExternalCall dmlCall) {
		ExternalCallKey key = new ExternalCallKey(service, calledService);
		ExternalCall call = calls.get(key);
		if (call != null) {
			return call;
		} else {
			call = ConfigurationFactory.eINSTANCE.createExternalCall();
			call.setCalledService(calledService);
			call.setName(calledService.getName());
			service.getTasks().add(call);
			calls.put(key, call);
			newEntities.add(call);
			return call;
		}
	}

	public Signature getSignature(Service service) {
		ServiceMapping mapping = services.get(service.getName());
		if (mapping == null) {
			return null;
		}
		return mapping.signature;
	}
	
	public AssemblyContext getAssemblyContext(Service service) {
		ServiceMapping mapping = services.get(service.getName());
		if (mapping == null) {
			return null;
		}
		return mapping.context;
	}

	public Container getContainer(Service service) {
		return services.get(service.getName()).container;
	}
	
	public Container getContainer(Resource resource) {
		return resources.get(resource.getName()).container;
	}
	
	private SchedulingStrategy convertSchedulingStrategy(SchedulingPolicy policy) {
		switch(policy) {
		case DELAY:
			return SchedulingStrategy.IS;
		case FCFS:
			return SchedulingStrategy.FCFS;
		case PROCESSOR_SHARING:
			return SchedulingStrategy.PS;
		case RANDOM:
		case NA:
		default:
			return SchedulingStrategy.UNKOWN;
		}
	}
	
	private ProcessingResourceSpecification getProcessingResource(Container container, ResourceType resType) {
		for (ConfigurationSpecification spec :  container.getConfigSpec()) {
			if (spec instanceof ProcessingResourceSpecification) {
				ProcessingResourceSpecification proc = (ProcessingResourceSpecification)spec;
				if (proc.getProcessingResourceType().equals(resType)) {
					return proc;
				}
			}
		}
		return null;
	}

}
