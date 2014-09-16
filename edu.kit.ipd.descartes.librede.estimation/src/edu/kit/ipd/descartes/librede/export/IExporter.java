package edu.kit.ipd.descartes.librede.export;

import edu.kit.ipd.descartes.librede.estimation.repository.TimeSeries;

public interface IExporter {
	
	public void writeResults(String approach, int fold, TimeSeries estimates) throws Exception;

}
