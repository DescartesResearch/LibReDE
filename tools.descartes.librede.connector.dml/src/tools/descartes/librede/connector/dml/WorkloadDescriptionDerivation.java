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
package tools.descartes.librede.connector.dml;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import tools.descartes.dml.mm.applicationlevel.parameterdependencies.ComponentInstanceReference;
import tools.descartes.dml.mm.applicationlevel.parameterdependencies.ModelVariableCharacterizationType;
import tools.descartes.dml.mm.applicationlevel.parameterdependencies.ParameterdependenciesFactory;
import tools.descartes.dml.mm.applicationlevel.repository.AssemblyContext;
import tools.descartes.dml.mm.applicationlevel.repository.BasicComponent;
import tools.descartes.dml.mm.applicationlevel.repository.ComposedStructure;
import tools.descartes.dml.mm.applicationlevel.repository.InterfaceProvidingRole;
import tools.descartes.dml.mm.applicationlevel.repository.InterfaceRequiringRole;
import tools.descartes.dml.mm.applicationlevel.repository.RepositoryComponent;
import tools.descartes.dml.mm.applicationlevel.repository.Signature;
import tools.descartes.dml.mm.applicationlevel.servicebehavior.AbstractAction;
import tools.descartes.dml.mm.applicationlevel.servicebehavior.BranchAction;
import tools.descartes.dml.mm.applicationlevel.servicebehavior.ComponentInternalBehavior;
import tools.descartes.dml.mm.applicationlevel.servicebehavior.ExternalCallAction;
import tools.descartes.dml.mm.applicationlevel.servicebehavior.FineGrainedBehavior;
import tools.descartes.dml.mm.applicationlevel.servicebehavior.ForkAction;
import tools.descartes.dml.mm.applicationlevel.servicebehavior.InternalAction;
import tools.descartes.dml.mm.applicationlevel.servicebehavior.LoopAction;
import tools.descartes.dml.mm.applicationlevel.servicebehavior.ResourceDemand;
import tools.descartes.dml.mm.resourcelandscape.Container;
import tools.descartes.dml.mm.resourcetype.ProcessingResourceType;
import tools.descartes.librede.configuration.Resource;
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
	
//	public void updateWorkloadDescription(Deployment deployment) {
//		mapping.reset();
//		completeServices.clear();
//		deploymentMapping.clear();
//		
//		// Search for all resources in the data center and create a mapping	
//		TreeIterator<EObject> dcIterator = deployment.getTargetResourceLandscape().eAllContents();
//		while (dcIterator.hasNext()) {
//			EObject curObject = dcIterator.next();
//			if (curObject instanceof Container) {
//				Container curContainer = (Container) curObject;
//				for (ConfigurationSpecification curSpec : curContainer.getConfigSpec()) {
//					if (curSpec instanceof ProcessingResourceSpecification) {
//						mapping.mapResource(curContainer, ((ProcessingResourceSpecification) curSpec).getProcessingResourceType());
//					}
//				}
//			}
//		}
//		
//		deploymentMapping.putAll(DmlHelper.determineDeploymentMapping(deployment));
//
//		Deque<AssemblyContext> callStack = new ArrayDeque<>();
//		tools.descartes.dml.mm.applicationlevel.system.System system = deployment.getSystem();
//		for (AssemblyContext ctx : system.getAssemblyContexts()) {
//			callStack.push(ctx);
//			Container deploymentTarget = deploymentMapping.get(ctx);
//			visitAssemblyContext(callStack, deploymentTarget);
//			callStack.pop();
//		}
//		
//		mapping.removeUnmappedEntites();
//	}
	

	
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
			ComposedStructure composite = (ComposedStructure) curAssembly.getEncapsulatedComponent();
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
			ComponentInstanceReference instance = getInstance(callStack);
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
	
	private ComponentInstanceReference getInstance(Deque<AssemblyContext> callStack) {
		ComponentInstanceReference instance = ParameterdependenciesFactory.eINSTANCE.createComponentInstanceReference();
		// IMPORTANT: Skip the last assembly context which is the assembly
		// context of application. Sensors in the repository are always relative
		// to the application.
		List<AssemblyContext> contexts = new ArrayList<>(callStack);
		for (int i = callStack.size() - 2; i >= 0; i--) {
			instance.getAssemblies().add(contexts.get(i));
		}
		return instance;
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
		InterfaceProvidingRole calledProvidingRole = DmlHelper.getCalledInterfaceProvidingRole(calledStack,
				requiringRole);
		
		if (calledProvidingRole != null) {
			ComponentInstanceReference instance = getComponentInstance(calledStack);
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
					ComponentInstanceReference instance = getComponentInstance(callStack);
					Service curService = mapping.mapService(instance, role, signature);
					for (Resource res : curResources) {
						mapping.mapResourceDemand(res, curService, demand);
					}
				}
			}
		}
	}

	private ComponentInstanceReference getComponentInstance(Deque<AssemblyContext> callStack) {
		ComponentInstanceReference instance = ParameterdependenciesFactory.eINSTANCE.createComponentInstanceReference();
		ArrayList<AssemblyContext> path = new ArrayList<>(callStack);
		for (int i = path.size() - 2; i >= 0; i--) {
			instance.getAssemblies().add(path.get(i));
		}
		return instance;
	}
		
}
 