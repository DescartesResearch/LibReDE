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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import edu.kit.ipd.descartes.core.NamedElement;
import edu.kit.ipd.descartes.mm.applicationlevel.parameterdependencies.ComponentInstanceReference;
import edu.kit.ipd.descartes.mm.applicationlevel.repository.AssemblyContext;
import edu.kit.ipd.descartes.mm.applicationlevel.repository.InterfaceProvidingRole;
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
		public ProcessingResourceSpecification configuration;
		public Resource resource;
	}
	
	private static class ServiceMapping {
		public ComponentInstanceReference componentInstance;
		public InterfaceProvidingRole role;
		public Signature signature;
		public Service service;
	}
	
	private BiMap<edu.kit.ipd.descartes.mm.applicationlevel.servicebehavior.ResourceDemand, ResourceDemand> demands = HashBiMap.create();
	private BiMap<edu.kit.ipd.descartes.mm.applicationlevel.servicebehavior.ExternalCall, ExternalCall> calls = HashBiMap
			.create();
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
	
	public edu.kit.ipd.descartes.mm.applicationlevel.servicebehavior.ExternalCall getDmlCall(ExternalCall libredeCall) {
		return calls.inverse().get(libredeCall);
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
	
	public Service mapService(ComponentInstanceReference componentInstance, InterfaceProvidingRole role,
			Signature signature) {
		String serviceName = getServiceName(componentInstance, role, signature);
		unmappedServices.remove(serviceName);
		ServiceMapping mapping = services.get(serviceName);
		if (mapping == null) {
			mapping = createService(serviceName, componentInstance, role, signature);
		}
		return mapping.service;		
	}
	
	private ServiceMapping createService(String serviceName, ComponentInstanceReference componentInstance,
			InterfaceProvidingRole role, Signature signature) {
		Service service = ConfigurationFactory.eINSTANCE.createService();
		service.setName(serviceName);

		ServiceMapping mapping = new ServiceMapping();
		mapping.componentInstance = componentInstance;
		mapping.role = role;
		mapping.signature = signature;
		mapping.service = service;
		services.put(serviceName, mapping);
		workload.getServices().add(service);
		newEntities.add(service);
		return mapping;
	}

	private String getServiceName(ComponentInstanceReference componentInstance, InterfaceProvidingRole role,
			Signature signature) {
		StringBuilder name = new StringBuilder();
		for (AssemblyContext ctx : componentInstance.getAssemblies()) {
			name.append(ctx.getName()).append("/");
		}
		name.append(role.getName()).append("/");
		name.append(signature.getName());
		return name.toString();
	}

	public ResourceDemand mapResourceDemand(Resource resource, Service service, edu.kit.ipd.descartes.mm.applicationlevel.servicebehavior.ResourceDemand dmlDemand) {
		ResourceDemand demand = demands.get(dmlDemand);
		if (demand != null) {
			return demand;
		} else {
			demand = ConfigurationFactory.eINSTANCE.createResourceDemand();
			demand.setResource(resource);			
			// Set the name to the ID to allow for backward traceability (see DmlExport)
			demand.setName(dmlDemand.getId());
			service.getTasks().add(demand);
			demands.put(dmlDemand, demand);
			newEntities.add(demand);
			return demand;
		}
	}
	
	public boolean contains(edu.kit.ipd.descartes.mm.applicationlevel.servicebehavior.ResourceDemand dmlDemand) {
		return demands.containsValue(dmlDemand);
	}

	public List<Resource> mapResource(Container container, ResourceType resType) {
		List<Resource> childResources = new ArrayList<>();
		for (ConfigurationSpecification spec : container.getConfigSpec()) {
			if (spec instanceof ProcessingResourceSpecification) {
				ProcessingResourceSpecification procRes = (ProcessingResourceSpecification) spec;
				if (procRes.getProcessingResourceType().equals(resType)) {
					String resourceName = getResourceName(procRes);
					ResourceMapping rm = resources.get(resourceName);
					if (rm == null) {
						rm = createResource(resourceName, container, procRes);
					}
					// Update attribute settings
					rm.resource.setNumberOfServers(procRes.getNrOfParProcUnits().getNumber());
					rm.resource.setSchedulingStrategy(convertSchedulingStrategy(procRes.getSchedulingPolicy()));
					childResources.add(rm.resource);
					unmappedResources.remove(resourceName);
				}
			}
		}
		return childResources;
	}
	
	private ResourceMapping createResource(String resourceName, Container container,
			ProcessingResourceSpecification specification) {
		ResourceMapping rm = new ResourceMapping();
		rm.resource = ConfigurationFactory.eINSTANCE.createResource();
		rm.resource.setName(resourceName);
		rm.container = container;
		resources.put(resourceName, rm);
		workload.getResources().add(rm.resource);
		newEntities.add(rm.resource);
		return rm;
	}

	private String getResourceName(ProcessingResourceSpecification spec) {
		StringBuilder name = new StringBuilder();
		name.append(((NamedElement) spec.eContainer()).getName()).append("_");
		name.append(spec.getProcessingResourceType().getName()).append("_");
		name.append(spec.getId());
		return name.toString();
	}

	public ExternalCall mapExternalCall(Service service, Service calledService, edu.kit.ipd.descartes.mm.applicationlevel.servicebehavior.ExternalCall dmlCall) {
		ExternalCall call = calls.get(dmlCall);
		if (call != null) {
			return call;
		} else {
			call = ConfigurationFactory.eINSTANCE.createExternalCall();
			call.setCalledService(calledService);
			call.setName(calledService.getName());
			service.getTasks().add(call);
			calls.put(dmlCall, call);
			newEntities.add(call);
			return call;
		}
	}

	public Service getService(ComponentInstanceReference instance, InterfaceProvidingRole role, Signature signature) {
		ServiceMapping mapping = services.get(getServiceName(instance, role, signature));
		if (mapping == null) {
			return null;
		}
		return mapping.service;
	}

	public InterfaceProvidingRole getInterfaceProvidingRole(Service service) {
		ServiceMapping mapping = services.get(service.getName());
		if (mapping == null) {
			return null;
		}
		return mapping.role;
	}

	public Signature getSignature(Service service) {
		ServiceMapping mapping = services.get(service.getName());
		if (mapping == null) {
			return null;
		}
		return mapping.signature;
	}
	
	public ComponentInstanceReference getComponentInstance(Service service) {
		ServiceMapping mapping = services.get(service.getName());
		if (mapping == null) {
			return null;
		}
		return mapping.componentInstance;
	}
	
	public Container getContainer(Resource resource) {
		ResourceMapping mapping = resources.get(resource.getName());
		if (mapping == null) {
			return null;
		}
		return mapping.container;
	}
	
	public ProcessingResourceSpecification getResourceSpecification(Resource resource) {
		ResourceMapping mapping = resources.get(resource.getName());
		if (mapping == null) {
			return null;
		}
		return mapping.configuration;
	}

	public Resource getResource(ProcessingResourceSpecification spec) {
		ResourceMapping mapping = resources.get(getResourceName(spec));
		if (mapping == null) {
			return null;
		}
		return mapping.resource;
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
}
