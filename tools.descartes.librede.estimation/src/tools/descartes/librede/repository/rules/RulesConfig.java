/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2014, by Simon Spinner and Contributors.
 *
 * Project Info:   http://www.descartes-research.net/
 *
 * All rights reserved. This software is made available under the terms of the
 * Eclipse Public License (EPL) v1.0 as published by the Eclipse Foundation
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse Public License (EPL)
 * for more details.
 *
 * You should have received a copy of the Eclipse Public License (EPL)
 * along with this software; if not visit http://www.eclipse.org or write to
 * Eclipse Foundation, Inc., 308 SW First Avenue, Suite 110, Portland, 97204 USA
 * Email: license (at) eclipse.org
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 */
package tools.descartes.librede.repository.rules;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;

public class RulesConfig {
	private final Map<Metric<?>, EnumMap<Aggregation, List<DerivationRule<?>>>> derivationRules = new HashMap<>();
	private final List<DerivationRule<?>> defaultRules = new LinkedList<>();
	
	public void addDerivationRule(DerivationRule<?> rule) {
		if (rule.getDependencies().isEmpty()) {
			defaultRules.add(rule);
		} else {
			for (RuleDependency<?> r : rule.getDependencies()) {
				EnumMap<Aggregation, List<DerivationRule<?>>> metricEntry = derivationRules.get(r.getMetric());
				if (metricEntry == null) {
					metricEntry = new EnumMap<>(Aggregation.class);
					derivationRules.put(r.getMetric(), metricEntry);
				}
				List<DerivationRule<?>> aggregationEntry = metricEntry.get(r.getAggregation());
				if (aggregationEntry == null) {
					aggregationEntry = new LinkedList<>();
					metricEntry.put(r.getAggregation(), aggregationEntry);
				}
				aggregationEntry.add(rule);
			}
		}
	}
	
	public List<DerivationRule<?>> getDerivationRules(Metric<?> metric, Aggregation aggregation) {
		EnumMap<Aggregation, List<DerivationRule<?>>> metricEntry = derivationRules.get(metric);
		if (metricEntry != null) {
			List<DerivationRule<?>> derivationEntry = metricEntry.get(aggregation);
			if (derivationEntry != null) {
				return derivationEntry;
			}
		}
		return Collections.emptyList();
	}
	
	public List<DerivationRule<?>> getDefaultDerivationRules() {
		return defaultRules;
	}
}