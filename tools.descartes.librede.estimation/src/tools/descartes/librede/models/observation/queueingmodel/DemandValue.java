package tools.descartes.librede.models.observation.queueingmodel;

import static tools.descartes.librede.linalg.LinAlg.zeros;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.IStateConstraint;

public abstract class DemandValue extends FixedValue {

	private final Vector zerosBuffer;
	private final int variableIdx;
	
	public DemandValue(IStateModel<? extends IStateConstraint> stateModel, Resource resource, Service service,
			int historicInterval) {
		super(stateModel, historicInterval);
		this.zerosBuffer = zeros(stateModel.getStateSize());
		this.variableIdx = stateModel.getStateVariableIndex(resource, service);
	}
	
	@Override
	public Vector getFactors() {
		return zerosBuffer.set(variableIdx, getConstantValue());
	}
}
