package edu.kit.ipd.descartes.librede.export.csv;

import java.io.File;

import edu.kit.ipd.descartes.librede.configuration.Component;
import edu.kit.ipd.descartes.librede.configuration.ParameterDefinition;
import edu.kit.ipd.descartes.librede.export.IExporter;

@Component
public class CsvExporter implements IExporter {

	@ParameterDefinition(name = "File", label = "CSV File", required = true)
	private File targetFile;
}
