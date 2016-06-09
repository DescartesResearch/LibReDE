package tools.descartes.librede.connector.dml;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import edu.kit.ipd.descartes.mm.applicationlevel.repository.AssemblyConnector;
import edu.kit.ipd.descartes.mm.applicationlevel.repository.AssemblyContext;
import edu.kit.ipd.descartes.mm.applicationlevel.repository.BasicComponent;
import edu.kit.ipd.descartes.mm.applicationlevel.repository.ComposedStructure;
import edu.kit.ipd.descartes.mm.applicationlevel.repository.CompositeComponent;
import edu.kit.ipd.descartes.mm.applicationlevel.repository.InterfaceProvidingRole;
import edu.kit.ipd.descartes.mm.applicationlevel.repository.InterfaceRequiringRole;
import edu.kit.ipd.descartes.mm.applicationlevel.repository.ProvidingDelegationConnector;
import edu.kit.ipd.descartes.mm.applicationlevel.repository.RequiringDelegationConnector;
import edu.kit.ipd.descartes.mm.deployment.Deployment;
import edu.kit.ipd.descartes.mm.deployment.DeploymentContext;
import edu.kit.ipd.descartes.mm.resourcelandscape.ComputeNode;
import edu.kit.ipd.descartes.mm.resourcelandscape.Container;

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
