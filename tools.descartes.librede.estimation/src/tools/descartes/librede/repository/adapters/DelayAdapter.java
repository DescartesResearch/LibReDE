package tools.descartes.librede.repository.adapters;

import java.util.Arrays;
import java.util.List;

import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.repository.IMetricAdapter;
import tools.descartes.librede.repository.TimeSeries.Interpolation;
import tools.descartes.librede.repository.handlers.ConstantHandler;
import tools.descartes.librede.repository.handlers.DefaultAggregationHandler;
import tools.descartes.librede.repository.handlers.DeriveDiffHandler;
import tools.descartes.librede.repository.rules.DerivationRule;
import tools.descartes.librede.units.Time;

public class DelayAdapter implements IMetricAdapter<Time> {

	@Override
	public Interpolation getInterpolation() {
		return Interpolation.LINEAR;
	}

	@Override
	public List<DerivationRule<Time>> getDerivationRules() {
		return Arrays.asList(
				DerivationRule.rule(StandardMetrics.DELAY, Aggregation.AVERAGE).requiring(Aggregation.NONE)
						.build(new DefaultAggregationHandler<Time>(Aggregation.NONE)),
				DerivationRule.rule(StandardMetrics.DELAY, Aggregation.SUM).requiring(Aggregation.NONE)
						.priority(0).build(new DefaultAggregationHandler<Time>(Aggregation.NONE)),
				DerivationRule.rule(StandardMetrics.DELAY, Aggregation.SUM).requiring(Aggregation.SUM)
						.priority(10).build(new DefaultAggregationHandler<Time>(Aggregation.SUM)),
				DerivationRule.rule(StandardMetrics.DELAY, Aggregation.MINIMUM).requiring(Aggregation.NONE)
						.build(new DefaultAggregationHandler<Time>(Aggregation.NONE)),
				DerivationRule.rule(StandardMetrics.DELAY, Aggregation.MAXIMUM).requiring(Aggregation.NONE)
						.build(new DefaultAggregationHandler<Time>(Aggregation.NONE)),
				DerivationRule.rule(StandardMetrics.DELAY, Aggregation.CUMULATIVE_SUM)
						.requiring(Aggregation.NONE).build(new DefaultAggregationHandler<Time>(Aggregation.NONE)),
				DerivationRule.rule(StandardMetrics.DELAY, Aggregation.SUM)
						.requiring(Aggregation.CUMULATIVE_SUM).build(new DeriveDiffHandler<Time>()),
				DerivationRule.rule(StandardMetrics.DELAY, Aggregation.AVERAGE)
						.priority(-100) //IMPORTANT: Use this only if nothing else is available
						.build(new ConstantHandler<Time>(0.0))
						);
	}

}
