package edu.kit.ipd.descartes.librede.datasource.csv;

import java.io.File;

import edu.kit.ipd.descartes.librede.configuration.ParameterDefinition;
import edu.kit.ipd.descartes.librede.configuration.Component;
import edu.kit.ipd.descartes.librede.datasource.IDataSource;

@Component(displayName = "CSV Data Source")
public class CsvDataSource implements IDataSource {

	@ParameterDefinition(name = "BaseDirectory", label = "Base Directory")
	private File baseDirectory;

	@ParameterDefinition(name = "Separators", label = "Separators", required = false, defaultValue = ",")
	private String separators;

	@ParameterDefinition(name = "SkipFirstLine", label = "Skip First Line", required = false, defaultValue = "false")
	private boolean skipFirstLine;

}
