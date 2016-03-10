package tools.descartes.librede.models.observation.queueingmodel;

import static tools.descartes.librede.linalg.LinAlg.zeros;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.IStateConstraint;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.units.Time;

/**
 * This output function approximates the resource demands with the observed
 * response times (min, max, or mean) of a service.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 * @version 1.0
 */
public class ResponseTimeApproximationEquation extends ModelEquation {

	private final Service cls_r;

	private final Query<Scalar, Time> individualResidenceTimesQuery;

	private final Vector zerosBuffer;
	private final int variableIdx;

	/**
	 * Creates a new instance.
	 * 
	 * @param stateModel
	 *            the description of the state
	 * @param repository
	 *            the repository with current measurement data
	 * @param service
	 *            the service for which the response time is calculated
	 * @param resource
	 *            the resource for which the response time is calculated
	 * @param aggregation
	 *            specifies whether average, minimum or maximum of the observed
	 *            response time is used as approximation for the resource
	 *            demand.
	 * 
	 * @throws {@link
	 *             NullPointerException} if any parameter is null
	 */
	public ResponseTimeApproximationEquation(IStateModel<? extends IStateConstraint> stateModel,
			IRepositoryCursor repository, Resource resource, Service service, Aggregation aggregation) {
		this(stateModel, repository, resource, service, aggregation, 0);
	}

	/**
	 * Creates a new instance.
	 * 
	 * @param stateModel
	 *            the description of the state
	 * @param repository
	 *            the repository with current measurement data
	 * @param service
	 *            the service for which the response time is calculated
	 * @param resource
	 *            the resource for which the response time is calculated
	 * @param aggregation
	 *            specifies whether average, minimum or maximum of the observed
	 *            response time is used as approximation for the resource
	 *            demand.
	 * @param historicInterval
	 *            specifies the number of intervals this function is behind in
	 *            the past.
	 * 
	 * @throws {@link
	 *             NullPointerException} if any parameter is null
	 */
	public ResponseTimeApproximationEquation(IStateModel<? extends IStateConstraint> stateModel,
			IRepositoryCursor repository, Resource resource, Service service, Aggregation aggregation,
			int historicInterval) {
		super(stateModel, historicInterval);

		cls_r = service;

		this.zerosBuffer = zeros(stateModel.getStateSize());
		this.variableIdx = stateModel.getStateVariableIndex(resource, cls_r);

		switch (aggregation) {
		case AVERAGE:
			individualResidenceTimesQuery = QueryBuilder.select(StandardMetrics.RESIDENCE_TIME).in(Time.SECONDS)
					.forService(cls_r).average().using(repository);
			addDataDependency(individualResidenceTimesQuery);
			break;
		case MAXIMUM:
			individualResidenceTimesQuery = QueryBuilder.select(StandardMetrics.RESIDENCE_TIME).in(Time.SECONDS)
					.forService(cls_r).max().using(repository);
			addDataDependency(individualResidenceTimesQuery);
			break;
		case MINIMUM:
			individualResidenceTimesQuery = QueryBuilder.select(StandardMetrics.RESIDENCE_TIME).in(Time.SECONDS)
					.forService(cls_r).min().using(repository);
			addDataDependency(individualResidenceTimesQuery);
			break;
		default:
			throw new IllegalArgumentException();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tools.descartes.librede.models.observation.queueingmodel.ModelEquation#
	 * getConstantValue()
	 */
	@Override
	public double getConstantValue() {
		double rt = individualResidenceTimesQuery.get(historicInterval).getValue();
		// We did not observe a request in this interval
		// therefore, we approximate the demand with zero
		return (rt != rt) ? 0.0 : rt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tools.descartes.librede.models.observation.queueingmodel.ModelEquation#
	 * getFactors()
	 */
	@Override
	public Vector getFactors() {
		return zerosBuffer.set(variableIdx, getConstantValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tools.descartes.librede.models.observation.queueingmodel.ModelEquation#
	 * hasData()
	 */
	@Override
	public boolean hasData() {
		return individualResidenceTimesQuery.hasData(historicInterval);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tools.descartes.librede.models.observation.queueingmodel.ModelEquation#
	 * isConstant()
	 */
	@Override
	public boolean isConstant() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tools.descartes.librede.models.observation.queueingmodel.ModelEquation#
	 * isLinear()
	 */
	@Override
	public boolean isLinear() {
		return true;
	}
}
