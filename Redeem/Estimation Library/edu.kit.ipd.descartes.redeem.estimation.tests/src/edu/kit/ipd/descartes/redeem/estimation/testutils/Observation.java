package edu.kit.ipd.descartes.redeem.estimation.testutils;

import edu.kit.ipd.descartes.linalg.Vector;

public class Observation {
	
	private Vector meanUtilization;
	private Vector meanThroughput;
	private Vector meanResponseTime;
	
	public Observation(Vector meanUtilization, Vector meanThroughput,
			Vector meanResponseTime) {
		super();
		this.meanUtilization = meanUtilization;
		this.meanThroughput = meanThroughput;
		this.meanResponseTime = meanResponseTime;
	}

	public Vector getMeanUtilization() {
		return meanUtilization;
	}

	public Vector getMeanThroughput() {
		return meanThroughput;
	}

	public Vector getMeanResponseTime() {
		return meanResponseTime;
	}
}
