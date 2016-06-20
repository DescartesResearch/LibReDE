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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import tools.descartes.librede.ApproachResult;
import tools.descartes.librede.LibredeResults;
import tools.descartes.librede.ResultTable;
import tools.descartes.librede.approach.IEstimationApproach;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.export.IExporter;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.registry.ParameterDefinition;
import tools.descartes.librede.repository.TimeSeries;

@Component(displayName = "CSV Export")
public class CsvExporter implements IExporter {

	@ParameterDefinition(name = "OutputDirectory", label = "Output Directory", required = true)
	private File outputPath;
	
	@ParameterDefinition(name = "FileName", label = "File Name Prefix", required = true)
	private String fileName;
	
	public void writeResults(String approach, int fold, ResourceDemand[] variables, TimeSeries estimates) throws Exception {		
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
		for (ResourceDemand var : variables) {
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

	@Override
	public void writeResults(LibredeResults results) throws Exception {
		for (Class<? extends IEstimationApproach> approach : results.getApproaches()) {
			int i = 0;
			for (int f = 0; f < results.getNumberOfFolds(); f++) {
				ResultTable curFold = results.getEstimates(approach, f);
				writeResults(curFold.getApproach().getSimpleName(), i, curFold.getStateVariables(),
							curFold.getEstimates());
				i++;
			}
		}
		
		exportResultTimelineCSV(results);
	}
	
	private void exportResultTimelineCSV(LibredeResults results) {
		File outputFile = new File(outputPath, fileName + "_Selection.csv");
		
		try {
			List<String> saveResult = new LinkedList<String>();
			int idx = 0;
			for (Class<? extends IEstimationApproach> curApproach : results.getSelectedApproaches()) {
				ApproachResult approachResult = results.getApproachResults(curApproach);
				ResultTable[] result = approachResult.getResult();

				// get Timestamp and Approach Name
				// timestamp is startTime
				double startTimestamp = result[0].getEstimates().getStartTime();
				double endTimestamp = result[0].getEstimates().getEndTime();
				String approachName = approachResult.getApproach().getName();

				double utilizationError = approachResult.getUtilizationError();
				double responseTimeError = approachResult.getResponseTimeError();

				String[] line = new String[4];
				line[0] = Double.toString(startTimestamp);
				line[1] = Double.toString(endTimestamp);
				line[2] = approachName;
				line[3] = Double.toString(approachResult.getMeanValidationError());
				saveResult.add(implode(";", line));
				idx++;
			}

			// open buffered writer
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, false));
			// write header
			String[] head = new String[4];
			head[0] = "Start Timestamp";
			head[1] = "End Timestamp";
			head[2] = "Approach Name";
			head[3] = "Mean Error Validation";
			saveResult.add(0, implode(";", head));
			// write each line (timestamp + approach name + mean error
			// validation)
			for (String line : saveResult) {
				bw.write(line);
				bw.write("\n");
			}
			// close buffer
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String implode(String separator, String... line) {
		StringBuilder sb = new StringBuilder();
		for (String cell : line) {
			sb.append(cell);
			sb.append(separator);
		}
		return sb.toString();
	}

}
