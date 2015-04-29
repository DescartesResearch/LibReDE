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
package tools.descartes.librede.export.csv;

import java.io.File;
import java.io.PrintWriter;

import tools.descartes.librede.export.IExporter;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.state.StateVariable;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.registry.ParameterDefinition;
import tools.descartes.librede.repository.TimeSeries;

@Component(displayName = "CSV Export")
public class CsvExporter implements IExporter {

	@ParameterDefinition(name = "OutputDirectory", label = "Output Directory", required = true)
	private File outputPath;
	
	@ParameterDefinition(name = "FileName", label = "File Name Prefix", required = true)
	private String fileName;
	
	public void writeResults(String approach, int fold, StateVariable[] variables, TimeSeries estimates) throws Exception {		
		File outputFile;
		if (fileName != null && !fileName.isEmpty()) {
			outputFile = new File(outputPath, fileName + "_" + approach + "_" + "fold_" + fold + ".csv");
		} else {
			outputFile = new File(outputPath, approach + "_" + "fold_" + fold + ".csv");
		}
		
		Vector time = estimates.getTime();
		Matrix demands = estimates.getData();

		PrintWriter out = new PrintWriter(outputFile);
		out.print("#timestamp");
		for (StateVariable var : variables) {
			out.print(", ");
			out.print(var.getResource().getName() + "/" + var.getService().getName());			
		}
		out.println();
		
		try {
			for (int j = 0; j < time.rows(); j++) {
				out.print(time.get(j));
				for (int k = 0; k < demands.columns(); k++) {
					out.print(",");
					out.print(demands.get(j, k));
				}
				out.println();
			}
		} finally {
			out.close();
		}
	}

}
