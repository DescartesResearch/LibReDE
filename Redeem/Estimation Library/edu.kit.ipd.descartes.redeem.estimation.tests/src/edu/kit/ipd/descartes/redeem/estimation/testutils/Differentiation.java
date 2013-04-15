package edu.kit.ipd.descartes.redeem.estimation.testutils;

import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IOutputFunction;
import edu.kit.ipd.descartes.redeem.estimation.system.Resource;
import edu.kit.ipd.descartes.redeem.estimation.system.Service;
import edu.kit.ipd.descartes.redeem.estimation.system.State;
import edu.kit.ipd.descartes.redeem.estimation.system.SystemModel;

public class Differentiation {
	
	public boolean testDiff(IOutputFunction equation, State state, SystemModel model) {
		
		double e = epsilon();
		
		for (Resource resource : model.getResources()) {
			for (Service cls : model.getWorkloadClasses()) {
//				Vector s = state.getStateVector();
//				equation.nextObservation(state)				
			}
		}
		
		return false;	
	}
	
	private double epsilon() {
		/*
		 * Based on algorithm from Ronald Mak, "Java Number Cruncher: The Java Programmer's Guide to Numerical Computing", 
		 * Prentice Hall, Upper Saddle River, New Jersey
		 */
		double epsilon = 0.5;
		while (1 + epsilon > 1) {
			epsilon /= 2;
		}
		return epsilon;
	}

}
