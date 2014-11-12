package tools.descartes.librede.approach;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.MatrixBuilder;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.repository.TimeSeries;
import tools.descartes.librede.workload.WorkloadDescription;

public class EstimationResult {
	
	private static class EntitiesPair {
		public final Resource resource;
		public final Service service;
		
		public EntitiesPair(Resource resource, Service service) {
			super();
			this.resource = resource;
			this.service = service;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((resource == null) ? 0 : resource.hashCode());
			result = prime * result
					+ ((service == null) ? 0 : service.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			EntitiesPair other = (EntitiesPair) obj;
			if (resource == null) {
				if (other.resource != null)
					return false;
			} else if (!resource.equals(other.resource))
				return false;
			if (service == null) {
				if (other.service != null)
					return false;
			} else if (!service.equals(other.service))
				return false;
			return true;
		}	
	}
	
	public static class Builder {
		private final Class<? extends IEstimationApproach> approach;
		private MatrixBuilder estimateBuilder;		
		private MatrixBuilder timestampBuilder;
		private double[] buffer;
		private Map<EntitiesPair, Integer> entryToColumn;
		
		private Builder(Class<? extends IEstimationApproach> approach, WorkloadDescription workload) {
			int stateSize = workload.getResources().size() * workload.getServices().size();
			this.approach = approach;
			estimateBuilder = new MatrixBuilder(stateSize);		
			timestampBuilder = new MatrixBuilder(1);
			entryToColumn = new HashMap<EntitiesPair, Integer>(stateSize);
			buffer = new double[stateSize];
			
			int i = 0;
			for (Resource res : workload.getResources()) {
				for (Service cls : workload.getServices()) {
					entryToColumn.put(new EntitiesPair(res, cls), i);
					i++;
				}
			}
		}
		
		public void next(double timestamp) {
			timestampBuilder.addRow(timestamp);
			Arrays.fill(buffer, 0);
		}
		
		public void set(Resource resource, Service service, double value) {
			buffer[entryToColumn.get(new EntitiesPair(resource, service))] = value;
		}
		
		public void save() {
			estimateBuilder.addRow(buffer);
		}
		
		public EstimationResult build() {
			EntitiesPair[] columnToEntry = new EntitiesPair[entryToColumn.size()];
			for (Map.Entry<EntitiesPair, Integer> e : entryToColumn.entrySet()) {
				columnToEntry[e.getValue()] = e.getKey();
			}
			TimeSeries estimates = new TimeSeries((Vector)timestampBuilder.toMatrix(), estimateBuilder.toMatrix());
			
			return new EstimationResult(approach, columnToEntry, estimates);
		}
	}
	
	private final Class<? extends IEstimationApproach> approach;
	private final EntitiesPair[] columnToEntry;
	private final TimeSeries estimates;
	
	private EstimationResult(Class<? extends IEstimationApproach> approach, EntitiesPair[] columnToEntry, TimeSeries estimates) {
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
		return columnToEntry[column].resource;
	}
	
	public Service getService(int column) {
		return columnToEntry[column].service;
	}
	
	public Class<? extends IEstimationApproach> getApproach() {
		return approach;
	}

}
