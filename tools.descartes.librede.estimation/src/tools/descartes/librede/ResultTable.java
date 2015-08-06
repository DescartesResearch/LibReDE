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

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import tools.descartes.librede.approach.IEstimationApproach;
import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.linalg.MatrixBuilder;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorBuilder;
import tools.descartes.librede.repository.TimeSeries;
import tools.descartes.librede.validation.IValidator;

public class ResultTable {
	
	public static class Builder {
		private final Class<? extends IEstimationApproach> approach;
		private MatrixBuilder estimateBuilder;		
		private VectorBuilder timestampBuilder;
		private double[] buffer;
		private Map<ResourceDemand, Integer> entryToColumn;
		
		private Builder(Class<? extends IEstimationApproach> approach, WorkloadDescription workload) {
			this.approach = approach;
			TreeSet<ResourceDemand> variables = new TreeSet<ResourceDemand>();			
			for (Resource res : workload.getResources()) {
				for (ResourceDemand demand : res.getDemands()) {
					variables.add(demand);					
				}
			}
			int stateSize = variables.size();			
			estimateBuilder = MatrixBuilder.create(stateSize);		
			timestampBuilder = VectorBuilder.create();
			entryToColumn = new HashMap<ResourceDemand, Integer>(stateSize);
			buffer = new double[stateSize];
			
			int i = 0;
			for (ResourceDemand var : variables) {
				entryToColumn.put(var, i);
				i++;
			}
		}
		
		public void next(double timestamp) {
			timestampBuilder.add(timestamp);
			Arrays.fill(buffer, 0);
		}
		
		public void set(ResourceDemand demand, double value) {
			buffer[entryToColumn.get(demand)] = value;
		}
		
		public void save() {
			estimateBuilder.addRow(buffer);
		}
		
		public ResultTable build() {
			ResourceDemand[] columnToEntry = new ResourceDemand[entryToColumn.size()];
			for (Map.Entry<ResourceDemand, Integer> e : entryToColumn.entrySet()) {
				columnToEntry[e.getValue()] = e.getKey();
			}
			TimeSeries estimates = new TimeSeries(timestampBuilder.toVector(), estimateBuilder.toMatrix());
			
			return new ResultTable(approach, columnToEntry, estimates);
		}
	}
	
	private final Class<? extends IEstimationApproach> approach;
	private final ResourceDemand[] columnToEntry;
	private final TimeSeries estimates;
	private Map<Class<? extends IValidator>, List<ModelEntity>> validationEntities;
	private Map<Class<? extends IValidator>, Vector> validationResults;
	
	private ResultTable(Class<? extends IEstimationApproach> approach, ResourceDemand[] columnToEntry, TimeSeries estimates) {
		this.approach = approach;
		this.columnToEntry = columnToEntry;
		this.estimates = estimates;
		this.validationEntities = new HashMap<Class<? extends IValidator>, List<ModelEntity>>();
		this.validationResults = new HashMap<Class <? extends IValidator>, Vector>();
	}
	
	public static Builder builder(Class<? extends IEstimationApproach> approach, WorkloadDescription workload) {
		return new Builder(approach, workload);
	}
	
	public TimeSeries getEstimates() {
		return estimates;
	}
	
	public Resource getResource(int column) {
		return columnToEntry[column].getResource();
	}
	
	public Service getService(int column) {
		return columnToEntry[column].getService();
	}
	
	public Class<? extends IEstimationApproach> getApproach() {
		return approach;
	}
	
	public void addValidationResults(Class <? extends IValidator> validator, Vector errors) {
		validationResults.put(validator, errors);
	}
	
	public Vector getLastEstimates() {
		return estimates.getData().row(estimates.samples() - 1);
	}
	
	public ResourceDemand[] getStateVariables() {
		return columnToEntry;
	}
	
	public Set<Class <? extends IValidator>> getValidators() {
		return validationResults.keySet();
	}
	
	public void setValidatedEntities(Class<? extends IValidator> validator, List<ModelEntity> entities) {
		validationEntities.put(validator, entities);
	}
	
	public List<ModelEntity> getValidatedEntities(Class<? extends IValidator> validator) {
		return validationEntities.get(validator);
	}

	public Vector getValidationErrors(Class<? extends IValidator> validator) {
		return validationResults.get(validator);
	}
}
