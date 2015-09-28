package tools.descartes.librede.repository.adapters;

import java.util.Arrays;
import java.util.List;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.repository.IMetricAdapter;
import tools.descartes.librede.repository.TimeSeries.Interpolation;
import tools.descartes.librede.repository.handlers.ConstantHandler;
import tools.descartes.librede.repository.handlers.DefaultAggregationHandler;
import tools.descartes.librede.repository.rules.Rule;
import tools.descartes.librede.repository.rules.RulePrecondition;
import tools.descartes.librede.units.Ratio;

public class StealTimeAdapter implements IMetricAdapter<Ratio> {

	@Override
	public Interpolation getInterpolation() {
		return Interpolation.PIECEWISE_CONSTANT;
	}

	@Override
	public List<Rule<Ratio>> getDerivationRules() {
		return Arrays.asList(
				Rule.rule(StandardMetrics.STEAL_TIME, Aggregation.AVERAGE)
					.requiring(Aggregation.AVERAGE)
					.build(new DefaultAggregationHandler<Ratio>(Aggregation.AVERAGE)),
				Rule.rule(StandardMetrics.STEAL_TIME, Aggregation.AVERAGE)
					.check(new RulePrecondition() {						
						@Override
						public boolean check(ModelEntity entity) {
							if (entity instanceof Resource) {
								return ((Resource) entity).getChildResources().isEmpty();
							}
							return false;
						}
					})
					.build(new ConstantHandler<Ratio>(0.0))
				);
	}

}
