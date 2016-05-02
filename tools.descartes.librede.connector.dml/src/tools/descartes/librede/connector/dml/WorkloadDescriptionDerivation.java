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

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.kit.ipd.descartes.mm.applicationlevel.parameterdependencies.ComponentInstanceReference;
import edu.kit.ipd.descartes.mm.applicationlevel.parameterdependencies.ModelVariableCharacterizationType;
import edu.kit.ipd.descartes.mm.applicationlevel.parameterdependencies.ParameterdependenciesFactory;
import edu.kit.ipd.descartes.mm.applicationlevel.repository.AssemblyConnector;
import edu.kit.ipd.descartes.mm.applicationlevel.repository.AssemblyContext;
import edu.kit.ipd.descartes.mm.applicationlevel.repository.BasicComponent;
import edu.kit.ipd.descartes.mm.applicationlevel.repository.ComposedStructure;
import edu.kit.ipd.descartes.mm.applicationlevel.repository.CompositeComponent;
import edu.kit.ipd.descartes.mm.applicationlevel.repository.InterfaceProvidingRole;
import edu.kit.ipd.descartes.mm.applicationlevel.repository.InterfaceRequiringRole;
import edu.kit.ipd.descartes.mm.applicationlevel.repository.ProvidingDelegationConnector;
import edu.kit.ipd.descartes.mm.applicationlevel.repository.RepositoryComponent;
import edu.kit.ipd.descartes.mm.applicationlevel.repository.RequiringDelegationConnector;
import edu.kit.ipd.descartes.mm.applicationlevel.repository.Signature;
import edu.kit.ipd.descartes.mm.applicationlevel.servicebehavior.AbstractAction;
import edu.kit.ipd.descartes.mm.applicationlevel.servicebehavior.BranchAction;
import edu.kit.ipd.descartes.mm.applicationlevel.servicebehavior.ComponentInternalBehavior;
import edu.kit.ipd.descartes.mm.applicationlevel.servicebehavior.ExternalCallAction;
import edu.kit.ipd.descartes.mm.applicationlevel.servicebehavior.FineGrainedBehavior;
import edu.kit.ipd.descartes.mm.applicationlevel.servicebehavior.ForkAction;
import edu.kit.ipd.descartes.mm.applicationlevel.servicebehavior.InternalAction;
import edu.kit.ipd.descartes.mm.applicationlevel.servicebehavior.LoopAction;
import edu.kit.ipd.descartes.mm.applicationlevel.servicebehavior.ResourceDemand;
import edu.kit.ipd.descartes.mm.applicationlevel.system.System;
import edu.kit.ipd.descartes.mm.deployment.Deployment;
import edu.kit.ipd.descartes.mm.deployment.DeploymentContext;
import edu.kit.ipd.descartes.mm.resourcelandscape.Container;
import edu.kit.ipd.descartes.mm.resourcetype.ProcessingResourceType;
import tools.descartes.librede.configuration.ConfigurationFactory;
import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.SchedulingStrategy;
import tools.descartes.librede.configuration.Service;

public class WorkloadDescriptionDerivation {
	
	private static final Logger log = Logger.getLogger(WorkloadDescriptionDerivation.class);

	// Set of all fully analyzed services
	private final Set<Service> completeServices = new HashSet<>();
	private DmlMapping mapping = new DmlMapping();
	private Map<AssemblyContext, Container> deploymentMapping = new HashMap<>();
	private Resource delayResource = null;

	public DmlMapping getMapping() {
		return mapping;
	}
	
	public void updateWorkloadDescription(Deployment deployment) {
		mapping.reset();
		completeServices.clear();
		deploymentMapping.clear();
		
		for (DeploymentContext depCtx : deployment.getDeploymentContexts()) {
			addDeployment(depCtx.getAssemblyContext(), depCtx.getResourceContainer());
		}

		Deque<AssemblyContext> callStack = new ArrayDeque<>();
		System system = deployment.getSystem();
		for (AssemblyContext ctx : system.getAssemblyContexts()) {
			callStack.push(ctx);
			Container deploymentTarget = deploymentMapping.get(ctx);
			visitAssemblyContext(callStack, deploymentTarget);
			callStack.pop();
		}
		
		// Remove all empty services
//		List<Service> nonemptyServices = new LinkedList<>();
//		for (Service curService : completeServices) {
//			if (curService.getTasks().isEmpty()) {
//				List<ExternalCall> calls = new ArrayList<>(curService.getIncomingCalls());
//				for (ExternalCall curCall : calls) {
//					curCall.getService().getTasks().remove(curCall);
//					mapping.unmapExternalCall(curCall);
//				}
//				mapping.unmapService(curService);
//			} else {
//				nonemptyServices.add(curService);
//			}
//		}
		
		addDelayResource();
		
		mapping.removeUnmappedEntites();
		
	}
	
	private void addDeployment(AssemblyContext ctx, Container container) {
		deploymentMapping.put(ctx, container);
		if (ctx.getEncapsulatedComponent() instanceof CompositeComponent) {
			CompositeComponent composite = (CompositeComponent)ctx.getEncapsulatedComponent();
			for (AssemblyContext child : composite.getAssemblyContexts()) {
				addDeployment(child, container);
			}
		}
	}
	
	private void addDelayResource() {
		if (delayResource == null) {
			delayResource = ConfigurationFactory.eINSTANCE.createResource();
			delayResource.setName("Delay");
			delayResource.setSchedulingStrategy(SchedulingStrategy.IS);
			delayResource.setNumberOfServers(1);
			mapping.getWorkload().getResources().add(delayResource);
		}
		
		for (ModelEntity curEntity : mapping.getNewEntities()) {
			if (curEntity instanceof Service) {
				Service curService = (Service)curEntity;
				if (curService.getIncomingCalls().isEmpty()) {
					tools.descartes.librede.configuration.ResourceDemand delayDemand = ConfigurationFactory.eINSTANCE.createResourceDemand();
					delayDemand.setName("Delay");
					delayDemand.setResource(delayResource);
					curService.getTasks().add(delayDemand);
				}
			}
		}
	}
	
	private void visitAssemblyContext(Deque<AssemblyContext> callStack, Container deploymentTarget) {
		AssemblyContext curAssembly = callStack.peek();
		RepositoryComponent component = curAssembly.getEncapsulatedComponent();
		if (component instanceof BasicComponent) {
			if (deploymentTarget != null) {
				for (InterfaceProvidingRole role : curAssembly.getEncapsulatedComponent()
						.getInterfaceProvidingRoles()) {
					for (Signature sig : role.getInterface().getSignatures()) {
						determineTasks(callStack, deploymentTarget, component, role, sig);
					}
				}
			} else {
				log.warn("Assembly context " + curAssembly.getName() + " is not deployed on any container.");
			}
		} else {
			CompositeComponent composite = (CompositeComponent) curAssembly.getEncapsulatedComponent();
			for (AssemblyContext child : composite.getAssemblyContexts()) {
				callStack.push(child);
				if (deploymentTarget == null) {
					Container curTarget = deploymentMapping.get(curAssembly);
					visitAssemblyContext(callStack, curTarget);
				} else {
					visitAssemblyContext(callStack, deploymentTarget);
				}
				callStack.pop();
			}
		}
	}

	private void determineTasks(Deque<AssemblyContext> callStack, Container deploymentTarget,
			RepositoryComponent component, InterfaceProvidingRole role, Signature sig) {
		BasicComponent implementation = (BasicComponent) component;
		FineGrainedBehavior behavior = getImplementationBehavior(implementation, role, sig);
		if (behavior != null) {
			ComponentInstanceReference instance = ParameterdependenciesFactory.eINSTANCE
					.createComponentInstanceReference();
			instance.getAssemblies().addAll(callStack);
			Service service = mapping.mapService(instance, role, sig);
			if (!completeServices.contains(service)) {
				visitComponentInternalBehavior(service, callStack, role, sig, deploymentTarget, behavior.getBehavior(),
						"");
				completeServices.add(service);
			}
		} else {
			log.warn("No fine-grained behavior found for component " + component.getName());
		}
	}
	
	private FineGrainedBehavior getImplementationBehavior(BasicComponent component, InterfaceProvidingRole role, Signature sig)	{
		for (FineGrainedBehavior currentBehavior : component.getFineGrainedBehavior()) {
			if (currentBehavior.getInterfaceProvidingRole().equals(role)
					&& currentBehavior.getDescribedSignature().equals(sig)) {
				return currentBehavior;
			}
		}
		return null;
	}
	
	private void visitComponentInternalBehavior(Service service, Deque<AssemblyContext> callStack,
			InterfaceProvidingRole role, Signature signature, Container deploymentTarget,
			ComponentInternalBehavior behavior, String path) {
		int i = 0;
		path = path + "/actions.";
		for (AbstractAction action : behavior.getActions()) {
			if (action instanceof ForkAction) {
				// Only consider threads for which we will wait for completion
				// Other types of threads do not influence the response time behavior of the current service.
				if (((ForkAction)action).isWithSynchronizationBarrier()) {
					forEachComponentInternalBehavior(service, callStack, role, signature, deploymentTarget,
							((ForkAction) action).getForkedBehaviors(), path + i + "/forkedBehaviors");
				}
			} else if (action instanceof BranchAction) {
				forEachComponentInternalBehavior(service, callStack, role, signature, deploymentTarget,
						((BranchAction) action).getBranches(), path + i + "/branches");
			} else if (action instanceof LoopAction) {
				forEachComponentInternalBehavior(service, callStack, role, signature, deploymentTarget,
						Collections.singletonList(((LoopAction) action).getLoopBodyBehavior()),
						path + i + "/loopBodyBehavior");
			} else if (action instanceof InternalAction) {
				visitInternalAction(service, callStack, role, signature, deploymentTarget, (InternalAction) action,
						path + i);
			} else if (action instanceof ExternalCallAction) {
				visitExternalCallAction(service, callStack, (ExternalCallAction) action);
			}
			i++;
		}
	}
	
	private void forEachComponentInternalBehavior(Service service, Deque<AssemblyContext> callStack,
			InterfaceProvidingRole role, Signature signature, Container deploymentTarget,
			List<ComponentInternalBehavior> behaviors, String path) {
		int j = 0;
		for (ComponentInternalBehavior b : behaviors) {
			visitComponentInternalBehavior(service, callStack, role, signature, deploymentTarget, b,
					path + path + "." + j);
			j++;
		}
	}
	
	private void visitExternalCallAction(Service service, Deque<AssemblyContext> callStack, ExternalCallAction action) {
		InterfaceRequiringRole requiringRole = action.getExternalCall().getInterfaceRequiringRole();
		Deque<AssemblyContext> calledStack = new ArrayDeque<>(callStack);
		InterfaceProvidingRole calledProvidingRole = getCalledInterfaceProvidingRole(calledStack, requiringRole);
		
		if (calledProvidingRole != null) {
			ComponentInstanceReference instance = ParameterdependenciesFactory.eINSTANCE.createComponentInstanceReference();
			instance.getAssemblies().addAll(calledStack);
			Service calledService = mapping.mapService(instance, calledProvidingRole,
					action.getExternalCall().getSignature());
			// Recursive calls are currently not supported.			
			if (!calledService.equals(service)) {
				mapping.mapExternalCall(service, calledService, action.getExternalCall());
			}
		} else {
			log.warn("No providing assembly context found for requiring role " + requiringRole.getName());
		}
	}
	
	private InterfaceProvidingRole getCalledInterfaceProvidingRole(Deque<AssemblyContext> callStack,
			InterfaceRequiringRole requiringRole) {
		AssemblyContext curAssembly = callStack.pop();
		ComposedStructure parent = (ComposedStructure) curAssembly.eContainer();
		for (AssemblyConnector connector : parent.getAssemblyConnectors()) {
			if (connector.getInterfaceRequiringRole().equals(requiringRole)) {
				callStack.push(connector.getProvidingAssemblyContext());
				return getInnerInterfaceProvidingRole(callStack, connector.getInterfaceProvidingRole());
			}
		}
		for (RequiringDelegationConnector connector : parent.getRequiringDelegationConnectors()) {
			if (connector.getInnerInterfaceRequiringRole().equals(requiringRole)) {
				// We have to go up one level in the callStack to determine the
				// called component
				callStack.pop();
				return getCalledInterfaceProvidingRole(callStack, connector.getOuterInterfaceRequiringRole());
			}
		}
		return null;
	}
	
	private InterfaceProvidingRole getInnerInterfaceProvidingRole(Deque<AssemblyContext> callStack,
			InterfaceProvidingRole role) {
		AssemblyContext ctx = callStack.peek();
		if (ctx.getEncapsulatedComponent() instanceof BasicComponent) {
			return role;
		} else {
			ComposedStructure composite = (ComposedStructure) ctx.getEncapsulatedComponent();
			for (ProvidingDelegationConnector del : composite.getProvidingDelegationConnectors()) {
				if (del.getOuterInterfaceProvidingRole().equals(role)) {
					callStack.push(del.getAssemblyContext());
					return getInnerInterfaceProvidingRole(callStack, del.getInnerInterfaceProvidingRole());
				}
			}
		}
		throw new IllegalStateException();
	}
	
	private void visitInternalAction(Service parent, Deque<AssemblyContext> callStack, InterfaceProvidingRole role,
			Signature signature, Container deploymentTarget, InternalAction action, String path) {
		for (ResourceDemand demand : action.getResourceDemand()) {
			if (demand.getCharacterization() == ModelVariableCharacterizationType.EMPIRICAL) {
				if (demand.getResourceType() instanceof ProcessingResourceType) {
					List<Resource> curResources = mapping.mapResource(deploymentTarget, demand.getResourceType());
					if (curResources.isEmpty()) {
						log.warn("No processing resource of type " + demand.getResourceType() + " found in container " + deploymentTarget.getName() + ".");
						continue;
					}
					ComponentInstanceReference instance = ParameterdependenciesFactory.eINSTANCE
							.createComponentInstanceReference();
					instance.getAssemblies().addAll(callStack);
					Service curService = mapping.mapService(instance, role, signature);
					for (Resource res : curResources) {
						mapping.mapResourceDemand(res, curService, demand);
					}
				}
			}
		}
	}	
		
}
 