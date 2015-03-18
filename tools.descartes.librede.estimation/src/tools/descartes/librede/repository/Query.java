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
package tools.descartes.librede.repository;

import static tools.descartes.librede.linalg.LinAlg.vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorFunction;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;

public final class Query<T extends Vector> {
	
	public static enum Type {
		SERVICE, ALL_SERVICES, RESOURCE, ALL_RESOURCES
	}
	
	private Aggregation aggregation;
	private Query.Type type;
	private Metric metric;
	private List<ModelEntity> entities = new ArrayList<ModelEntity>();
	private IRepositoryCursor repositoryCursor;
	
	protected Query(Aggregation aggregation, Type type, Metric metric,
			ModelEntity entity, IRepositoryCursor repositoryCursor) {
		super();
		this.aggregation = aggregation;
		this.type = type;
		this.metric = metric;
		this.repositoryCursor = repositoryCursor;
		if (entity != null) {
			entities.add(entity);
		}
	}

	public Aggregation getAggregation() {
		return aggregation;
	}
	
	public Query.Type getType() {
		return type;
	}
	
	public Metric getMetric() {
		return metric;
	}
	
	public T execute() {		
		if (entities.isEmpty()) {
			load();
		}
		
		if (entities.size() > 1) {
			Vector result = vector(entities.size(), new VectorFunction() {				
				@Override
				public double cell(int row) {
					return repositoryCursor.getAggregatedValue(metric, entities.get(row), aggregation);
				}
			});
			return (T)result;
		} else {
			if (aggregation != Aggregation.NONE) {
				return (T)new Scalar(repositoryCursor.getAggregatedValue(metric, entities.get(0), aggregation));
			} else {
				return (T)repositoryCursor.getValues(metric, entities.get(0)).getData(0);
			}			
		}
	}
	
	public int indexOf(ModelEntity entity) {
		return entities.indexOf(entity);
	}
	
	public ModelEntity getEntity(int index) {
		return entities.get(index);
	}
	
	public List<? extends ModelEntity> getEntities() {
		return Collections.unmodifiableList(entities);
	}
	
	
	
	private void load() {
		if (type == Type.ALL_RESOURCES) {
			entities.addAll(repositoryCursor.getRepository().listResources());
		} else if (type == Type.ALL_SERVICES) {
			entities.addAll(repositoryCursor.getRepository().listServices());
		}		
	}

	public boolean hasData() {
		return repositoryCursor.hasData(metric, entities, aggregation);
	}
}
