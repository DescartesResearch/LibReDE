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
package edu.kit.ipd.descartes.librede.export.csv;

import java.io.File;
import java.io.PrintWriter;

import edu.kit.ipd.descartes.librede.estimation.repository.TimeSeries;
import edu.kit.ipd.descartes.librede.export.IExporter;
import edu.kit.ipd.descartes.librede.factory.Component;
import edu.kit.ipd.descartes.librede.factory.ParameterDefinition;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;

@Component(displayName = "CSV Export")
public class CsvExporter implements IExporter {

	@ParameterDefinition(name = "OutputDirectory", label = "Output Directory", required = true)
	private File outputPath;
	
	@ParameterDefinition(name = "FileName", label = "File Name", required = true)
	private String fileName;
	
	public void writeResults(String approach, int fold, TimeSeries estimates) throws Exception {
		File outputFile = new File(outputPath, fileName + "_" + approach + "_" + "fold_" + fold + ".csv");
		
		Vector time = estimates.getTime();
		Matrix demands = estimates.getData();

		PrintWriter out = new PrintWriter(outputFile);
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
