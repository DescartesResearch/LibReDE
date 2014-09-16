package edu.kit.ipd.descartes.librede.export.csv;

import java.io.File;

import edu.kit.ipd.descartes.librede.configuration.Component;
import edu.kit.ipd.descartes.librede.configuration.ParameterDefinition;
import edu.kit.ipd.descartes.librede.export.IExporter;

@Component(displayName = "CSV Export")
public class CsvExporter implements IExporter {

	@ParameterDefinition(name = "OutputDirectory", label = "Output Directory", required = true)
	private File outputPath;
	
	@ParameterDefinition(name = "FileName", label = "File Name", required = true)
	private String fileName;

}
