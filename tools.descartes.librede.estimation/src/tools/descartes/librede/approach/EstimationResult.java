package tools.descartes.librede.approach;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.linalg.MatrixBuilder;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.state.StateVariable;
import tools.descartes.librede.repository.TimeSeries;

public class EstimationResult {
	
	public static class Builder {
		private final Class<? extends IEstimationApproach> approach;
		private MatrixBuilder estimateBuilder;		
		private MatrixBuilder timestampBuilder;
		private double[] buffer;
		private Map<StateVariable, Integer> entryToColumn;
		
		private Builder(Class<? extends IEstimationApproach> approach, WorkloadDescription workload) {
			int stateSize = workload.getResources().size() * workload.getServices().size();
			this.approach = approach;
			estimateBuilder = new MatrixBuilder(stateSize);		
			timestampBuilder = new MatrixBuilder(1);
			entryToColumn = new HashMap<StateVariable, Integer>(stateSize);
			buffer = new double[stateSize];
			
			int i = 0;
			for (Resource res : workload.getResources()) {
				for (Service cls : workload.getServices()) {
					entryToColumn.put(new StateVariable(res, cls), i);
					i++;
				}
			}
		}
		
		public void next(double timestamp) {
			timestampBuilder.addRow(timestamp);
			Arrays.fill(buffer, 0);
		}
		
		public void set(Resource resource, Service service, double value) {
			buffer[entryToColumn.get(new StateVariable(resource, service))] = value;
		}
		
		public void save() {
			estimateBuilder.addRow(buffer);
		}
		
		public EstimationResult build() {
			StateVariable[] columnToEntry = new StateVariable[entryToColumn.size()];
			for (Map.Entry<StateVariable, Integer> e : entryToColumn.entrySet()) {
				columnToEntry[e.getValue()] = e.getKey();
			}
			TimeSeries estimates = new TimeSeries((Vector)timestampBuilder.toMatrix(), estimateBuilder.toMatrix());
			
			return new EstimationResult(approach, columnToEntry, estimates);
		}
	}
	
	private final Class<? extends IEstimationApproach> approach;
	private final StateVariable[] columnToEntry;
	private final TimeSeries estimates;
	
	private EstimationResult(Class<? extends IEstimationApproach> approach, StateVariable[] columnToEntry, TimeSeries estimates) {
		this.approach = approach;
		this.columnToEntry = columnToEntry;
		this.estimates = estimates;
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

}
