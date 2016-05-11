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

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import edu.kit.ipd.descartes.mm.applicationlevel.functions.ExponentialDistribution;
import edu.kit.ipd.descartes.mm.applicationlevel.functions.FunctionsFactory;
import edu.kit.ipd.descartes.mm.applicationlevel.functions.RandomVariable;
import edu.kit.ipd.descartes.mm.applicationlevel.parameterdependencies.ModelVariableCharacterizationType;
import edu.kit.ipd.descartes.mm.applicationlevel.repository.Repository;
import edu.kit.ipd.descartes.mm.applicationlevel.system.System;
import edu.kit.ipd.descartes.mm.deployment.Deployment;
import edu.kit.ipd.descartes.mm.resourcelandscape.DistributedDataCenter;
import edu.kit.ipd.descartes.mm.resourcetype.ResourcetypePackage;
import edu.kit.ipd.descartes.mm.usageprofile.UsageProfile;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.export.IExporter;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.registry.ParameterDefinition;
import tools.descartes.librede.repository.TimeSeries;

/**
 * This class provides an exporter that saves the resulting estimates in a DML
 * model. The assumption is that the resource demands in the workload
 * description have the same name like the id of the corresponding model
 * variable in the DML model.
 * 
 * @author Simon Spinner (mailto: simon.spinner@uni-wuerzburg.de)
 *
 */
@Component(displayName = "DML Export")
public class DmlExport implements IExporter {
	
	private static final Logger log = Logger.getLogger(DmlExport.class);

	@ParameterDefinition(name = "ModelPath", label = "Path to DML model", required = true)
	private Path dmlModelPath;
	
	@ParameterDefinition(name = "ModelName", label = "Name of DML model (excluding file extension)", required = true)
	private String dmlModelName;
	
	private Repository repository;
	private System system;
	private DistributedDataCenter ddc;
	private Deployment deployment;
	private UsageProfile usage;

	@Override
	public void writeResults(String approach, int fold, ResourceDemand[] variables, TimeSeries estimates)
			throws Exception {
		// Load and create a copey of the DML model
		loadModel();
		if (repository == null || system == null || ddc == null || deployment == null || usage == null) {
			log.error("Could not load DML model " + dmlModelName + " in directory " + dmlModelPath);
			return;
		}
		
		// maps demand names to corresponding variable indexes
		Map<String, Integer> demandsToIdx = new HashMap<>();
		for (int i = 0; i < variables.length; i++) {
			demandsToIdx.put(variables[i].getName(), i);
		}		
		
		// Iterate over all resource demands in the DML model
		TreeIterator<EObject> elementsIter = repository.eAllContents();
		while (elementsIter.hasNext()) {
			EObject curObj = elementsIter.next();
			if (curObj instanceof edu.kit.ipd.descartes.mm.applicationlevel.servicebehavior.ResourceDemand) {
				edu.kit.ipd.descartes.mm.applicationlevel.servicebehavior.ResourceDemand dmlDemand = (edu.kit.ipd.descartes.mm.applicationlevel.servicebehavior.ResourceDemand) curObj;
				Integer idx = demandsToIdx.get(dmlDemand.getId());
				if (idx != null) {
					double lastEstimate = estimates.getData(idx).get(estimates.samples() - 1);
					dmlDemand.setCharacterization(ModelVariableCharacterizationType.EXPLICIT);
					RandomVariable var = FunctionsFactory.eINSTANCE.createRandomVariable();
					dmlDemand.setExplicitDescription(var);
					ExponentialDistribution expDist = FunctionsFactory.eINSTANCE.createExponentialDistribution();
					if (lastEstimate != 0) {
						expDist.setRate(BigDecimal.valueOf(1 / lastEstimate));
					} else {
						log.warn("Resource demand is empty.");
						expDist.setRate(BigDecimal.valueOf(1e30));
					}
					var.setProbFunction(expDist);
				} else {
					log.warn("Could not find a corresponding resource demand in the workload description (DML resource demand " + dmlDemand.getId() + ")");
				}
			}
		}
		
		// Store the copy with the parameter values in the file system.
		storeModel(approach, fold);
	}
	
	private void storeModel(String approach, int fold) {
		ResourceSet rset = createResourceSet();
		try {
			serialize(rset, repository, dmlModelPath.resolve(dmlModelName + "-" + approach + "-" + fold + ".repository"));
			serialize(rset, ddc, dmlModelPath.resolve(dmlModelName + "-" + approach + "-" + fold + ".resourcelandscape"));
			serialize(rset, system, dmlModelPath.resolve(dmlModelName + "-" + approach + "-" + fold + ".system"));
			serialize(rset, deployment, dmlModelPath.resolve(dmlModelName + "-" + approach + "-" + fold + ".deployment"));
			serialize(rset, usage, dmlModelPath.resolve(dmlModelName + "-" + approach + "-" + fold + ".usageprofile"));
		} catch (IOException e) {
			log.error("Error saving DML model " + dmlModelName + "-" + approach + "-" + fold + ".", e);
		}
	}
	
	private void loadModel() {
		ResourceSet rset = createResourceSet();
		EcoreUtil.Copier copier = new EcoreUtil.Copier();
		repository = (Repository) copier.copy(deserialize(rset, dmlModelPath.resolve(dmlModelName + ".repository")));
		ddc = (DistributedDataCenter) copier.copy(deserialize(rset, dmlModelPath.resolve(dmlModelName + ".resourcelandscape")));
		system = (System) copier.copy(deserialize(rset, dmlModelPath.resolve(dmlModelName + ".system")));
		deployment = (Deployment) copier.copy(deserialize(rset, dmlModelPath.resolve(dmlModelName + ".deployment")));
		usage = (UsageProfile) copier.copy(deserialize(rset, dmlModelPath.resolve(dmlModelName + ".usageprofile")));
		copier.copyReferences();
	}
	
	private EObject deserialize(ResourceSet resSet, Path path) {
		Resource res = resSet.getResource(URI.createFileURI(path.toString()), true);
		if (res != null && !res.getContents().isEmpty()) {
			return res.getContents().get(0);
		}
		return null;
	}
	
	private void serialize(ResourceSet resSet, EObject root, Path path) throws IOException {
		Resource res = resSet.createResource(URI.createFileURI(path.toString()));
		res.getContents().add(root);
		res.save(Collections.EMPTY_MAP);
	}
	
	private ResourceSet createResourceSet() {
		ResourcetypePackage.eINSTANCE.eClass();

		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("repository", new XMIResourceFactoryImpl());
		m.put("system", new XMIResourceFactoryImpl());
		m.put("resourcelandscape", new XMIResourceFactoryImpl());
		m.put("resourcetype", new XMIResourceFactoryImpl());
		m.put("deployment", new XMIResourceFactoryImpl());
		m.put("usageprofile", new XMIResourceFactoryImpl());

		ResourceSet resSet = new ResourceSetImpl();
		return resSet;
	}

}