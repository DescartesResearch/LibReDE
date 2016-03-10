package tools.descartes.librede.models.observation;

import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.AbstractDependencyTarget;
import tools.descartes.librede.models.State;
import tools.descartes.librede.models.observation.queueingmodel.FixedValue;
import tools.descartes.librede.models.observation.queueingmodel.ModelEquation;
import tools.descartes.librede.models.variables.OutputVariable;

public class OutputFunction extends AbstractDependencyTarget {
	
	private final FixedValue observedOutput;
	private final ModelEquation calculatedOutput;
	
	public OutputFunction(FixedValue observedOutput, ModelEquation calculatedOutput) {
		this.observedOutput = observedOutput;
		this.calculatedOutput = calculatedOutput;
		addDataDependencies(this.observedOutput);
		addDataDependencies(this.calculatedOutput);
	}
	
	public boolean hasData() {
		return observedOutput.hasData() && calculatedOutput.hasData();
	}
	
	public double getObservedOutput() {
		return observedOutput.getConstantValue();
	}
	
	public OutputVariable getCalculatedOutput(State state) {
		return new OutputVariable(state, calculatedOutput.getValue(state));
	}
	
	public double getFactor() {
		return calculatedOutput.getConstantValue();
	}
	
	public Vector getIndependentVariables() {
		return calculatedOutput.getFactors();
	}
	
	public boolean isLinear() {
		return calculatedOutput.isLinear();
	}
	
	public boolean isConstant() {
		return calculatedOutput.isConstant();
	}

}
