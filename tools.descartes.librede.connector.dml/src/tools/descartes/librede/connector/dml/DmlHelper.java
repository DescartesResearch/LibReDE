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

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import tools.descartes.dml.mm.applicationlevel.repository.AssemblyConnector;
import tools.descartes.dml.mm.applicationlevel.repository.AssemblyContext;
import tools.descartes.dml.mm.applicationlevel.repository.BasicComponent;
import tools.descartes.dml.mm.applicationlevel.repository.ComposedStructure;
import tools.descartes.dml.mm.applicationlevel.repository.CompositeComponent;
import tools.descartes.dml.mm.applicationlevel.repository.InterfaceProvidingRole;
import tools.descartes.dml.mm.applicationlevel.repository.InterfaceRequiringRole;
import tools.descartes.dml.mm.applicationlevel.repository.ProvidingDelegationConnector;
import tools.descartes.dml.mm.applicationlevel.repository.RequiringDelegationConnector;
import tools.descartes.dml.mm.deployment.Deployment;
import tools.descartes.dml.mm.deployment.DeploymentContext;
import tools.descartes.dml.mm.resourcelandscape.ComputeNode;
import tools.descartes.dml.mm.resourcelandscape.Container;

public class DmlHelper {

	public static InterfaceProvidingRole getCalledInterfaceProvidingRole(Deque<AssemblyContext> callStack,
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
				// callStack.pop();
				return getCalledInterfaceProvidingRole(callStack, connector.getOuterInterfaceRequiringRole());
			}
		}
		return null;
	}

	public static InterfaceProvidingRole getInnerInterfaceProvidingRole(Deque<AssemblyContext> callStack,
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
		throw new IllegalStateException("Could not determine inner interface providing role for " + role.getName());
	}

	public static Map<? extends AssemblyContext, ? extends Container> determineDeploymentMapping(
			Deployment deployment) {
		Map<AssemblyContext, Container> deploymentMapping = new HashMap<>();
		// We determine the transitive deployment context for each assembly
		// context in a system.
		// For instance an assembly context within a composite component is
		// directly deployed, only
		// the assembly context of the outermost composite component is
		// referenced by a deployment context.
		for (DeploymentContext depCtx : deployment.getDeploymentContexts()) {
			addDeployment(depCtx.getAssemblyContext(), depCtx.getResourceContainer(), deploymentMapping);
		}
		return deploymentMapping;
	}

	public static boolean areOnSameComputeNode(Container container1, Container container2) {
		Container parent1 = container1;
		while ((parent1 != null) && (parent1.eContainer() instanceof Container)) {
			parent1 = (Container) parent1.eContainer();
		}

		Container parent2 = container2;
		while ((parent2 != null) && (parent2.eContainer() instanceof Container)) {
			parent2 = (Container) parent2.eContainer();
		}

		if ((parent1 instanceof ComputeNode) && (parent2 instanceof ComputeNode)) {
			return parent1.equals(parent2);
		}
		return false;
	}

	private static void addDeployment(AssemblyContext ctx, Container container,
			Map<AssemblyContext, Container> deploymentMapping) {
		deploymentMapping.put(ctx, container);
		if (ctx.getEncapsulatedComponent() instanceof CompositeComponent) {
			CompositeComponent composite = (CompositeComponent) ctx.getEncapsulatedComponent();
			for (AssemblyContext child : composite.getAssemblyContexts()) {
				addDeployment(child, container, deploymentMapping);
			}
		}
	}
}
