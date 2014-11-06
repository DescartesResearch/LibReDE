function [ repository ] = librede_init( services, resources )
%RDE_INIT Summary of this function goes here
%   Detailed explanation goes here

load_classes();

import java.util.*;
import tools.descartes.librede.linalg.*;
import tools.descartes.librede.estimation.workload.*;
import tools.descartes.librede.estimation.repository.*;
import tools.descartes.librede.approaches.*;
import tools.descartes.librede.frontend.*;

org.apache.log4j.BasicConfigurator.configure();

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
    service = Service(java.lang.String(serviceNames{i}));
    serviceList.add(service);
end

resList = ArrayList(length(resourceNames));
for i=1:length(resourceNames)
    res = Resource(resourceNames{i}, 1);
    resList.add(res);
end

workload = WorkloadDescription(resList, serviceList);
repository = MemoryObservationRepository(workload);


end

