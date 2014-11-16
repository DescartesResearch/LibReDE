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
package tools.descartes.librede.frontend;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import tools.descartes.librede.LibredeLibrary;
import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.LibredeConfiguration;

public class Console {

	private static final Logger log = Logger.getLogger(Console.class);

	@Option(name = "--configuration", required = true, aliases = { "-c" }, usage = "Path to a configuration file (*.librede)")
	private File configurationFile;

	private Path currentPath;

	public static void main(String[] args) {
		Console instance = new Console();
		instance.run(args);
	}

	public void run(String[] args) {
		BasicConfigurator.configure();

		CmdLineParser parser = new CmdLineParser(this);
		try {
			currentPath = Paths.get("").toAbsolutePath();

			parser.parseArgument(args);
			
			LibredeConfiguration config = loadConfiguration(configurationFile);
			
			LibredeLibrary.execute(config);
		} catch (CmdLineException ex) {
			System.err.println(ex.getMessage());
			parser.printUsage(System.err);
		} catch (Exception ex) {
			log.error("Error executing resource demand estimation.", ex);
		}
	}
	
	private LibredeConfiguration loadConfiguration(File path) {
		ResourceSet resourceSet = new ResourceSetImpl();
		
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
			    Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
		
		ConfigurationPackage pack = ConfigurationPackage.eINSTANCE;

		URI fileURI = URI.createFileURI(path.getAbsolutePath());

		Resource resource = resourceSet.getResource(fileURI, true);
				
		return (LibredeConfiguration) resource.getContents().get(0);
	}
}
