function [estimates] = rde(data, services, resources)

load_classes();

import java.util.*;
import edu.kit.ipd.descartes.linalg.*;
import edu.kit.ipd.descartes.redeem.estimation.workload.*;
import edu.kit.ipd.descartes.redeem.estimation.repository.*;

if iscell(services)
    serviceNames = services;
else
    serviceNames = cell(services, 1);
    for i=1:length(serviceNames)
        serviceNames{i} = sprintf('WC%s', i);
    end
end

if iscell(resources)
    resourceNames = resources;
else
    resourceNames = cell(resources, 1);
    for i=1:length(resourceNames)
        resourceNames{i} = sprintf('R%s', i);
    end
end


%% Init model entities
serviceList = ArrayList(length(serviceNames));
for i=1:length(serviceNames)
    service = Service(serviceNames{i});
    serviceList.add(service);
end

resList = ArrayList(length(resourceNames));
for i=1:length(resourceNames)
    res = Resource(resourceNames{i});
    resList.add(res);
end

workload = WorkloadDescription(resList, serviceList);
repo = MemoryObservationRepository(workload);

%% CPU Utilization
i=2;
for j=length(serviceNames):size(data, 2)
    if ~isempty(data{i,j})
        util = TimeSeries(LinAlg.vector(data{1,j}), LinAlg.vector(data{i,j}));
        repo.setData(Metric.UTILIZATION, resList.get(j - length(serviceNames) - 1), util);
    end
end

i=3;
for j=1:length(serviceNames)
    if ~isempty(data{i,j})
        arriv = TimeSeries(LinAlg.vector(data{i,j}), LinAlg.ones(length(data{i,j}), 1));
        repo.setData(Metric.ARRIVAL_TIME, resList.get(j - 1), arriv);
    end
end

i=4;
for j=1:length(serviceNames)
    if ~isempty(data{i,j})
        resp = TimeSeries(LinAlg.vector(data{i,j}), LinAlg.ones(length(data{i,j}), 1));
        repo.setData(Metric.RESPONSE_TIME, resList.get(j - 1), resp);
    end
end

i=5;
for j=1:length(serviceNames)
    if ~isempty(data{i,j})
        respAvg = TimeSeries(LinAlg.vector(data{1,j}), LinAlg.vector(data{i,j}));
        repo.setData(Metric.AVERAGE_RESPONSE_TIME, resList.get(j - 1), respAvg);
    end
end

i=6;
for j=1:length(serviceNames)
    if ~isempty(data{i,j})
        tputAvg = TimeSeries(LinAlg.vector(data{1,j}), LinAlg.vector(data{i,j}));
        repo.setData(Metric.THROUGHPUT, resList.get(j - 1), tputAvg);
    end
end

end