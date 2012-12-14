package edu.kit.ipd.descartes.redeem.estimation.menasceopt;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;

public class MenasceProblem {
	
	private Vector throughput;
	private Vector measuredRespTimes;
	
	
	private double f(Matrix x, int r) {
		double sum = 0.0;
		for (int i = 0; i < x.rowCount(); i++) {			
			sum += x.get(i, r) / x.rowVector(i).multiply(throughput);
		}		
		double diff = measuredRespTimes.get(r) - sum;
		return diff*diff;
	}
	
	private double dev(Matrix x, int r) {
		double[] sigma = new double[x.rowCount()];
		double[] sigmaSqr = new double[x.rowCount()];
		
		for (int i = 0; i < sigma.length; i++) {
			sigma[i] = x.rowVector(i).multiply(throughput);
			sigmaSqr[i] = sigma[i] * sigma[i];
		}
		
		double alpha = measuredRespTimes.get(r);
		for (int i = 0; i < x.rowCount(); i++) {
			alpha -= x.get(i, r) / sigma[i];
		}
		
		for (int i = 0; i < x.rowCount(); i++) {
			for (int j = 0; j < x.columnCount(); j++) {
				if (j == r) {
					double result = x.get(i, r)	* throughput.get(j)	/ sigmaSqr[i] + 1 / sigma[i];		
				} else {
					double result =  x.get(i, r) * throughput.get(j) / sigmaSqr[i];
				}				
			}
		}
		
		return 0.0;
	}
	
	private double dev2(Matrix x, int r) {
		double[] sigma = new double[x.rowCount()];
		double[] sigma2 = new double[x.rowCount()];
		double[] sigma3 = new double[x.rowCount()];
		
		for (int i = 0; i < x.rowCount(); i++) {
			for (int j = 0; j < x.columnCount(); j++) {
				for (int k = 0; k <= i; k++) {
					for (int l = 0; l <= j; l++) {
						if (i == k) {
							if (l == r) {
								double result = (2 * x.get(i,  r) * throughput.get(j) * throughput.get(l)) / sigma3[i] + 2 * throughput.get(j) / sigma2[i];
							} else {
								double result = (2 * x.get(i,  r) * throughput.get(j) * throughput.get(l)) / sigma3[i];
							}							
						} else {
							double result = 0;
						}
					}
				}				
			}
			sigma[i] = x.rowVector(i).multiply(throughput);
			sigma2[i] = sigma[i] * sigma[i];
			sigma3[i] = sigma2[i] * sigma[i];
		}
		
		return 0;
	}

}
