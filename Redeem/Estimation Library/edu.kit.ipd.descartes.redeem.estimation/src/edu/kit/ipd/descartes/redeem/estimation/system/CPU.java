package edu.kit.ipd.descartes.redeem.estimation.system;

import edu.kit.ipd.descartes.redeem.estimation.io.MeasurementStream;

public class CPU extends ProcessingResource {
	
	private int numCores;
	private MeasurementStream totalUtilization;

	public CPU(String identifier) {
		this(identifier, 1);
	}
	
	public CPU(String identifier, int numCores) {
		super(identifier);
		
		if (numCores < 1) {
			throw new IllegalArgumentException("Number of cores must be >= 1");
		}
		this.numCores = numCores;
	}

	public int getNumberOfCores() {
		return numCores;
	}
	
	public MeasurementStream getTotalUtilization() {
		return totalUtilization;
	}

	public void setTotalUtilization(MeasurementStream totalUtilization) {
		this.totalUtilization = totalUtilization;
	}	
}
