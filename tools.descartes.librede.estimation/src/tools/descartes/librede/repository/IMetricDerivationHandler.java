package tools.descartes.librede.repository;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.units.Dimension;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;

public interface IMetricDerivationHandler<D extends Dimension> {
	
	TimeSeries derive(IMonitoringRepository repository, Metric<D> metric, Unit<D> unit, ModelEntity entity, Aggregation aggregation, Quantity<Time> start, Quantity<Time> end);
	
	Quantity<Time> getAggregationInterval(MemoryObservationRepository repository, Metric<D> metric, ModelEntity entity, Aggregation aggregation);
	
	Quantity<Time> getStartTime(MemoryObservationRepository repository, Metric<D> metric, ModelEntity entity, Aggregation aggregation);
	
	Quantity<Time> getEndTime(MemoryObservationRepository repository, Metric<D> metric, ModelEntity entity, Aggregation aggregation);
}
