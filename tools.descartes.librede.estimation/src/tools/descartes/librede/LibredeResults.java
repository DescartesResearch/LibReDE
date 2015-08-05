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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import tools.descartes.librede.approach.IEstimationApproach;

public class LibredeResults {
	
	private final Map<Class<? extends IEstimationApproach>, Integer> approaches = new HashMap<>(); 
	private final ResultTable[][] results;
	private final int numFolds;
	private int currentNumApproaches = 0;
	
	public LibredeResults(int numApproaches, int numFolds) {
		this.numFolds = numFolds;
		results = new ResultTable[numApproaches][];
		for (int i = 0; i < numApproaches; i++) {
			results[i] = new ResultTable[numFolds];
		}
	}
	
	public void addEstimates(Class<? extends IEstimationApproach> approach, int fold, ResultTable estimates) {
		Integer idx = approaches.get(approach);
		if (idx == null) {
			if (currentNumApproaches < results.length) {
				idx = currentNumApproaches;
				approaches.put(approach, currentNumApproaches);
				currentNumApproaches++;
			} else {
				throw new IllegalStateException();
			}
		}
		results[idx][fold] = estimates;
	}
	
	public ResultTable getEstimates(Class<? extends IEstimationApproach> approach, int fold) {
		Integer idx = approaches.get(approach);
		if (idx == null) {
			throw new IllegalArgumentException();
		}
		return results[idx][fold];
	}	
	
	public int getNumberOfFolds() {
		return numFolds;
	}
	
	public Set<Class<? extends IEstimationApproach>> getApproaches() {
		return approaches.keySet();
	}

}
