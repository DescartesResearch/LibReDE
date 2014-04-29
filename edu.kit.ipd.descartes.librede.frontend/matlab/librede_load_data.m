function [ ] = librede_load_data( repository, metric, entities, timestamps, values, interval )
%LIBREDE_LOAD_DATA Summary of this function goes here
%   Detailed explanation goes here

load_classes();

import edu.kit.ipd.descartes.linalg.*;
import edu.kit.ipd.descartes.librede.estimation.workload.*;
import edu.kit.ipd.descartes.librede.estimation.repository.*;
import edu.kit.ipd.descartes.librede.approaches.*;
import edu.kit.ipd.descartes.librede.frontend.*;

workload = repository.getWorkload();

if strcmpi(metric, 'utilization')
    m = StandardMetric.UTILIZATION;
    if iscell(entities)
        e = cell(length(entities));
        for i=1:length(entities)
            e{i} = workload.getResource(entities{i});
        end
    else
        e = {workload.getResource(entities)};
    end
elseif strcmpi(metric, 'response_time')
    m = StandardMetric.RESPONSE_TIME;
    if iscell(entities)
        e = cell(length(entities));
        for i=1:length(entities)
            e{i} = workload.getService(entities{i});
        end
    else
        e = {workload.getService(entities)};
    end
elseif strcmpi(metric, 'throughput')
    m = StandardMetric.THROUGHPUT;
    if iscell(entities)
        e = cell(length(entities));
        for i=1:length(entities)
            e{i} = workload.getService(entities{i});
        end
    else
        e = {workload.getService(entities)};
    end
end

for i=1:length(e)
    if iscell(values)
        trace = TimeSeries(LinAlg.vector(timestamps), LinAlg.vector(values{i}));
    else
        trace = TimeSeries(LinAlg.vector(timestamps), LinAlg.vector(values));
    end
    trace.setStartTime(timestamps(1) - interval);
    trace.setEndTime(timestamps(end));
    if interval > 0.0
       repository.setAggregatedData(m, e{i}, trace, interval);
    else
       repository.setData(m, e{i}, trace);
    end
end

end

