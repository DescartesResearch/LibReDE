package tools.descartes.librede.models.observation.queueingmodel;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.IStateConstraint;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.units.Ratio;

public class UtilizationValue extends FixedValue {

	private final Query<Scalar, Ratio> utilQuery;

	public UtilizationValue(IStateModel<? extends IStateConstraint> stateModel, IRepositoryCursor cursor,
			Resource resource, int historicInterval) {
		super(stateModel, historicInterval);
		this.utilQuery = QueryBuilder.select(StandardMetrics.UTILIZATION).in(Ratio.NONE).forResource(resource).average()
				.using(cursor);
		addDataDependency(utilQuery);		
	}
	
	@Override
	public double getConstantValue() {
		return utilQuery.get(historicInterval).getValue();
	}
	
	@Override
	public boolean hasData() {
		return utilQuery.hasData(historicInterval);
	}

}
