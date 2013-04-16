package edu.kit.ipd.descartes.redeem.estimation.repository;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.redeem.estimation.system.IModelEntity;

public class Result<T extends Matrix> {

	private T data;
	
	public Result(T data) {
		this.data = data;
	}
	
	public int getIndex(IModelEntity entity) {
		return 0;
	}
	
	public IModelEntity getEntity(int index) {
		return null;
	}
	
	public T getData() {
		return data;
	}
	
}
