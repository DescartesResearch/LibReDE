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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.log4j.Logger;

import edu.kit.ipd.descartes.mm.applicationlevel.parameterdependencies.ModelVariableCharacterizationType;
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

		Stack<ComposedStructure> callStack = new Stack<ComposedStructure>();
		System system = deployment.getSystem();
		callStack.push(system);
		for (AssemblyContext ctx : system.getAssemblyContexts()) {
			Container deploymentTarget = deploymentMapping.get(ctx);
			if (deploymentTarget != null) {
				visitAssemblyContext(callStack, deploymentTarget, ctx);
			} else {
				log.warn("Assembly context " + ctx.getName() + " is not deployed on any container.");
			}			
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
	
	private void visitAssemblyContext(Stack<ComposedStructure> callStack, Container deploymentTarget, AssemblyContext assembly) {
		RepositoryComponent component = assembly.getEncapsulatedComponent();
		if (component instanceof BasicComponent) {
			for (InterfaceProvidingRole role : assembly.getEncapsulatedComponent().getInterfaceProvidingRoles()) {
				for (Signature sig : role.getInterface().getSignatures()) {
					determineTasks(callStack, deploymentTarget, assembly, component, role, sig);
				}
			}
		} else {
			CompositeComponent composite = (CompositeComponent)assembly.getEncapsulatedComponent();
			callStack.push(composite);
			for (AssemblyContext child : composite.getAssemblyContexts()) {
				visitAssemblyContext(callStack, deploymentTarget, child);
			}
			callStack.pop();
		}
	}

	private void determineTasks(Stack<ComposedStructure> callStack, Container deploymentTarget, AssemblyContext assembly, RepositoryComponent component, InterfaceProvidingRole role, Signature sig) {
			BasicComponent implementation = (BasicComponent)component;
			FineGrainedBehavior behavior = getImplementationBehavior(implementation, role, sig);
			if (behavior != null) {
				Service service = mapping.mapService(assembly, sig, deploymentTarget);
				if (!completeServices.contains(service)) {
					visitComponentInternalBehavior(service, callStack, deploymentTarget, assembly, sig, behavior.getBehavior(), "");
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
	
	private ProvidingDelegationConnector getDelegationConnector(CompositeComponent composite, InterfaceProvidingRole role) {		
		for (ProvidingDelegationConnector connector : composite.getProvidingDelegationConnectors()) {
			if (connector.getOuterInterfaceProvidingRole().equals(role)) {
				return connector;
			}
		}
		return null;
	}

	private void visitComponentInternalBehavior(Service service, Stack<ComposedStructure> callStack, Container deploymentTarget, AssemblyContext assembly, Signature signature, ComponentInternalBehavior behavior, String path) {
		int i = 0;
		path = path + "/actions.";
		for (AbstractAction action : behavior.getActions()) {
			if (action instanceof ForkAction) {
				// Only consider threads for which we will wait for completion
				// Other types of threads do not influence the response time behavior of the current service.
				if (((ForkAction)action).isWithSynchronizationBarrier()) {
					forEachComponentInternalBehavior(service, callStack, deploymentTarget, assembly, signature, ((ForkAction) action).getForkedBehaviors(), path + i +"/forkedBehaviors");
				}
			} else if (action instanceof BranchAction) {
				forEachComponentInternalBehavior(service, callStack, deploymentTarget, assembly, signature, ((BranchAction) action).getBranches(), path + i + "/branches");
			} else if (action instanceof LoopAction) {
				forEachComponentInternalBehavior(service, callStack, deploymentTarget, assembly, signature, Collections.singletonList(((LoopAction) action).getLoopBodyBehavior()), path + i + "/loopBodyBehavior");
			} else if (action instanceof InternalAction) {
				visitInternalAction(service, deploymentTarget, assembly, signature, (InternalAction)action, path + i);
			} else if (action instanceof ExternalCallAction) {
				visitExternalCallAction(service, callStack, (ExternalCallAction) action);
			}
			i++;
		}
	}
	
	private void forEachComponentInternalBehavior(Service service, Stack<ComposedStructure> callStack, Container deploymentTarget, AssemblyContext assembly, Signature signature, List<ComponentInternalBehavior> behaviors, String path) {
		int j = 0;
		for (ComponentInternalBehavior b : behaviors) {
			visitComponentInternalBehavior(service, callStack, deploymentTarget, assembly, signature, b, path + path + "." + j);
			j++;
		}
	}
	
	private void visitExternalCallAction(Service service, Stack<ComposedStructure> callStack, ExternalCallAction action) {
		InterfaceRequiringRole requiringRole = action.getExternalCall().getInterfaceRequiringRole();
		AssemblyContext providingCtx = getCalledAssemblyContext(callStack, requiringRole);
		Container targetContainer = deploymentMapping.get(providingCtx);
		
		if (providingCtx != null) {	
			Service calledService = mapping.mapService(providingCtx, action.getExternalCall().getSignature(), targetContainer);
			// Recursive calls are currently not supported.			
			if (!calledService.equals(service)) {
				mapping.mapExternalCall(service, calledService, action.getExternalCall());
			}
		} else {
			log.warn("No providing assembly context found for requiring role " + requiringRole.getName());
		}
	}
	
	private AssemblyContext getCalledAssemblyContext(Stack<ComposedStructure> callStack, InterfaceRequiringRole requiringRole) {
		ComposedStructure parent = callStack.peek();
		for (AssemblyConnector connector : parent.getAssemblyConnectors()) {
			if (connector.getInterfaceRequiringRole().equals(requiringRole)) {
				return getInnerAssemblyContext(connector.getProvidingAssemblyContext(), connector.getInterfaceProvidingRole());
			}
		}
		for (RequiringDelegationConnector connector : parent.getRequiringDelegationConnectors()) {
			if (connector.getInnerInterfaceRequiringRole().equals(requiringRole)) {
				// We have to go up one level in the callstack to determine the called component
				callStack.pop();
				AssemblyContext providingCtx = getCalledAssemblyContext(callStack, connector.getOuterInterfaceRequiringRole());
				// Restore call stack
				callStack.push(parent);
				return providingCtx;
			}
		}
		
		return null;
	}
	
	private AssemblyContext getInnerAssemblyContext(AssemblyContext ctx, InterfaceProvidingRole role) {
		if (ctx.getEncapsulatedComponent() instanceof BasicComponent) {
			return ctx;
		} else {
			CompositeComponent composite = (CompositeComponent)ctx.getEncapsulatedComponent();
			for (ProvidingDelegationConnector del : composite.getProvidingDelegationConnectors()) {
				if (del.getOuterInterfaceProvidingRole().equals(role)) {
					return getInnerAssemblyContext(del.getAssemblyContext(), del.getInnerInterfaceProvidingRole());
				}
			}
		}
		return ctx;
	}
	
	private void visitInternalAction(Service parent, Container deploymentTarget, AssemblyContext assembly, Signature signature, InternalAction action, String path) {
		for (ResourceDemand demand : action.getResourceDemand()) {
			if (demand.getCharacterization() == ModelVariableCharacterizationType.EMPIRICAL) {
				if (demand.getResourceType() instanceof ProcessingResourceType) {
					Resource curResource = mapping.mapResource(deploymentTarget, demand.getResourceType());
					if (curResource == null) {
						log.warn("No processing resource of type " + demand.getResourceType() + " found in container " + deploymentTarget.getName() + ".");
						continue;
					}
					Service curService = mapping.mapService(assembly, signature, deploymentTarget);
					mapping.mapResourceDemand(curResource, curService, demand);
				}
			}
		}
	}	
		
}
 