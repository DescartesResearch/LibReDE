package edu.kit.ipd.descartes.librede.datasource;

import java.io.InputStream;

import edu.kit.ipd.descartes.librede.estimation.repository.TimeSeries;

public interface IDataSource {
	
	public TimeSeries load(InputStream in, int column) throws Exception;

}
