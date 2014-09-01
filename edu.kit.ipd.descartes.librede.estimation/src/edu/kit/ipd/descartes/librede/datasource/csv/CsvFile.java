package edu.kit.ipd.descartes.librede.datasource.csv;

import java.io.File;

import edu.kit.ipd.descartes.librede.configuration.ParameterDefinition;
import edu.kit.ipd.descartes.librede.configuration.Component;
import edu.kit.ipd.descartes.librede.datasource.IDataItem;

@Component
public class CsvFile implements IDataItem {
	
	@ParameterDefinition(name="File", label="CSV File")
	private File csvFile;

}
