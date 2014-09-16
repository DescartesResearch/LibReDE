package edu.kit.ipd.descartes.librede.export.csv;

import java.io.File;
import java.io.PrintWriter;

import edu.kit.ipd.descartes.librede.configuration.Component;
import edu.kit.ipd.descartes.librede.configuration.ParameterDefinition;
import edu.kit.ipd.descartes.librede.estimation.repository.TimeSeries;
import edu.kit.ipd.descartes.librede.export.IExporter;
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
