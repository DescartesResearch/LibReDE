/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2014, by Simon Spinner and Contributors.
 *
 * Project Info:   http://www.descartes-research.net/
 *
 * All rights reserved. This software is made available under the terms of the
 * Eclipse Public License (EPL) v1.0 as published by the Eclipse Foundation
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse Public License (EPL)
 * for more details.
 *
 * You should have received a copy of the Eclipse Public License (EPL)
 * along with this software; if not visit http://www.eclipse.org or write to
 * Eclipse Foundation, Inc., 308 SW First Avenue, Suite 110, Portland, 97204 USA
 * Email: license (at) eclipse.org
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 */
package tools.descartes.librede;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import tools.descartes.librede.approach.IEstimationApproach;

public class ApproachSelector {

	private static final Logger log = Logger.getLogger(Librede.class);
	private LibredeVariables var;
	private List<Class<? extends IEstimationApproach>> approaches;
	private double[] errorTable;
	private Map<Integer, Class<? extends IEstimationApproach>> approachMap = new HashMap<>();

	public ApproachSelector(LibredeVariables var) {
		this.var = var;
		this.approaches = new ArrayList<>(var.getResults().getApproaches());
		this.errorTable = new double[approaches.size()];
	}

	public void loadErrorTable() {
		log.info("Start approach selection.");

		// backup the selected Approaches from LibredeVariables to reset the
		// selected Approaches to all Approaches possible from Configuration to
		// get validation results from all Approaches
		// afterwards reset selected Approaches of LibredeVariables to the
		// previous selected approaches
		// List<EstimationApproachConfiguration> selectedApproachesBackup =
		// var.getSelectedApproaches();
//		if ((var.getSelectedApproaches()).isEmpty()) {
//			var.setSelectedApproaches(var.getConf().getEstimation().getApproaches());
//		}
		// run execute with all approaches to get validation results of all
		// approaches
		//
		// Librede.executeContinuous(var, Collections.<String, IDataSource>
		// emptyMap());
		// reset selected Approaches to the previous selected ones
		// var.setSelectedApproaches(selectedApproachesBackup);

		if (!var.getConf().getValidation().isValidateEstimates()) {
			log.info("No validation is defined for approach selection ");
		} else {
			// walk through all approaches, fetch all Approach validation
			// errors,
			// save the errors to the errorTable and the index to approachMap
			int i = 0;
			for (Class<? extends IEstimationApproach> approach : approaches) {
				errorTable[i] = var.getResults().getApproachResults(approach).getMeanValidationError();
				approachMap.put(i, approach);
				i++;
			}
		}

		selectApproach();
	}

	public void selectApproach() {

		double minError = Double.MAX_VALUE;
		Class<? extends IEstimationApproach> selectedApproach = null;

		for (int i = 0; i < errorTable.length; i++) {
//			System.err.println("Approach Name: " + approachMap.get(i));
//			System.err.println("Mean Error " + i + " : " + errorTable[i]);
			if (errorTable[i] != 0 && minError > errorTable[i]) {
				minError = errorTable[i];
				selectedApproach = approachMap.get(i);
			}
		}

		if (selectedApproach != null) {
			log.info("Selected approach: " + selectedApproach.getName() + " Mean Validation Error: " + minError);
			var.getResults().setSelectedApproaches(Arrays.<Class<? extends IEstimationApproach>>asList(selectedApproach));
		}
	}
	
	public static void selectApproach(LibredeVariables var) {
		ApproachSelector s = new ApproachSelector(var);
		//loads error table and calls intern method select approach
		s.loadErrorTable();
	}

}
