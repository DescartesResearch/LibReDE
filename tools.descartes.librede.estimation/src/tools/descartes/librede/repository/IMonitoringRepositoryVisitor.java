package tools.descartes.librede.repository;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;

public interface IMonitoringRepositoryVisitor {

	void visitTimeSeries(ModelEntity entity, Metric<?> metric, Aggregation aggregation,
			Quantity<Time> aggregationInterval, TimeSeries data);

}
