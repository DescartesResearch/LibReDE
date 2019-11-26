/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2018, by Simon Spinner, Johannes Grohmann
 *  and Contributors.
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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;

public class RulesConfig {
	private static final Logger log = Logger.getLogger(RulesConfig.class);
	
	/**
	 * Map from metric to agreggation (both as id) to the rules that are applicable for this combination
	 */
	private final Map<Metric<?>, EnumMap<Aggregation, List<Rule>>> derivationRulesByDependency = new HashMap<>();
	/**
	 * rules with no dependencies
	 */
	private final Set<Rule> derivationRules = new HashSet<>();
	/**
	 * Rules with dependencies
	 */
	private final List<Rule> defaultRules = new LinkedList<>();
	
	
	public void addRule(Rule rule) {
		if (log.isDebugEnabled()) {
			log.debug("Add rule: " + rule);
		}
		
		if (rule.getDependencies().isEmpty()) {
			//add it to the rules without dependencies
			defaultRules.add(rule);
		} else {
			//extract the dependencies
			for (DataDependency<?> r : rule.getDependencies()) {
				EnumMap<Aggregation, List<Rule>> metricEntry = derivationRulesByDependency.get(r.getMetric());
				if (metricEntry == null) {
					metricEntry = new EnumMap<>(Aggregation.class);
					derivationRulesByDependency.put(r.getMetric(), metricEntry);
				}
				List<Rule> aggregationEntry = metricEntry.get(r.getAggregation());
				if (aggregationEntry == null) {
					aggregationEntry = new LinkedList<>();
					metricEntry.put(r.getAggregation(), aggregationEntry);
				}
				aggregationEntry.add(rule);				
			}
			//add it to the dependency list
			derivationRules.add(rule);
		}
	}
	
	public void removeRule(Rule rule) {
		//if the rule is part of the dependency rules
		if (derivationRules.contains(rule)) {
			//remove the dependencies
			for (DataDependency<?> r : rule.getDependencies()) {
				EnumMap<Aggregation, List<Rule>> metricEntry = derivationRulesByDependency.get(r.getMetric());
				if (metricEntry != null) {
					List<Rule> aggregationEntry = metricEntry.get(r.getAggregation());
					aggregationEntry.remove(rule);
				}
			}
			//remove the rule
			derivationRules.remove(rule);
		}
	}
	
	public List<Rule> getDerivationRules(Metric<?> metric, Aggregation aggregation) {
		//search for the metric in the mapping
		EnumMap<Aggregation, List<Rule>> metricEntry = derivationRulesByDependency.get(metric);
		if (metricEntry != null) {
			//search for the aggregation
			List<Rule> derivationEntry = metricEntry.get(aggregation);
			//return the list
			if (derivationEntry != null) {
				return derivationEntry;
			}
		}
		return Collections.emptyList();
	}
	/**
	 * Get the rules without dependencies
	 * @return
	 */
	public List<Rule> getDefaultDerivationRules() {
		return defaultRules;
	}
	/**
	 * log all the rules with and without dependencies
	 */
	public void logConfigDump() {
		for (Rule d : defaultRules) {
			log.info(d.toString());
		}
		
		for (Rule d : derivationRules) {
			log.info(d.toString());
		}
	}
}