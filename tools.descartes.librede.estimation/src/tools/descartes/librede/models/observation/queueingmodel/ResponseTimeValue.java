package tools.descartes.librede.models.observation.queueingmodel;

import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.IStateConstraint;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.units.Time;

public class ResponseTimeValue extends FixedValue {

	private final Query<Scalar, Time> respTimeQuery;
	
	public ResponseTimeValue(IStateModel<? extends IStateConstraint> stateModel, IRepositoryCursor cursor, Service service, int historicInterval) {
		super(stateModel, historicInterval);
		respTimeQuery = QueryBuilder.select(StandardMetrics.RESPONSE_TIME).in(Time.SECONDS).forService(service).average().using(cursor);
	}
	
	@Override
	public double getConstantValue() {
		return respTimeQuery.get(historicInterval).getValue();
	}

	@Override
	public boolean hasData() {
		return respTimeQuery.hasData();
	}

}
