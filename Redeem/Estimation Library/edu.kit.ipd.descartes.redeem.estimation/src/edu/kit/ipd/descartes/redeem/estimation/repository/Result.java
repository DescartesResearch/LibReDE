package edu.kit.ipd.descartes.redeem.estimation.repository;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.redeem.estimation.workload.IModelEntity;

public class Result<T extends Matrix> {

	private T data;
	private IModelEntity[] entities;
	
	public Result(T data, IModelEntity[] entities) {
		this.data = data;
		this.entities = entities;
	}
	
	public int getIndex(IModelEntity entity) {
		for (int i = 0; i < entities.length; i++) {
			if (entity.equals(entities[i])) {
				return i;
			}
		}
		return 0;
	}
	
	public IModelEntity getEntity(int index) {
		return entities[index];
	}
	
	public T getData() {
		return data;
	}
	
}
