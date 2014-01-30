function [estimates, relErrUtil, relErrResp] = rde(approach, services, resources, startTime, endTime, varargin)

options = struct(...
    'Util', [], ...
    'UtilInterval', 0, ...
    'Rt', [], ...
    'RtInterval', 0, ...
    'Tput', [], ...
    'TputInterval', 0, ...
    'StepSize', 1, ...
    'Iterative', 0, ...
    'Window', 1, ...
    'ProcessorCount', ones(length(resources)));

optionNames = fieldnames(options);

nVarArgs = length(varargin);
if round(nVarArgs/2)~=nVarArgs/2
   error('Expected pairs of property/value.')
end

for pair = reshape(varargin,2,[])
   inpName = pair{1};

   if any(strcmpi(inpName,optionNames))
       options.(inpName) = pair{2};
   else
      error('%s is not a recognized parameter name',inpName)
   end
end

if isempty(startTime) || ~isnumeric(startTime)
    error('Parameter startTime is not a number.');
end

if isempty(endTime) || ~isnumeric(endTime)
    error('Parameter endTime is not a number.');
end

if endTime < startTime
    error('Parameter startTime must be less or equal to endTime');
end

load_classes();

import java.util.*;
import edu.kit.ipd.descartes.linalg.*;
import edu.kit.ipd.descartes.librede.estimation.workload.*;
import edu.kit.ipd.descartes.librede.estimation.repository.*;
import edu.kit.ipd.descartes.librede.approaches.*;
import edu.kit.ipd.descartes.librede.frontend.*;

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
    res = Resource(resourceNames{i}, options.ProcessorCount(i));
    resList.add(res);
end

workload = WorkloadDescription(resList, serviceList);
repo = MemoryObservationRepository(workload);
repo.setCurrentTime(endTime);
cursor = repo.getCursor(startTime, options.StepSize);

if ~isempty(options.Util)
    util = options.Util;
    if length(util) ~= length(resourceNames)
        error('The number of elements in the utilization cell array must be equal to the number of resources.');
    end
    for i=1:length(util)
       if size(util{i}, 2) ~= 2
           error('Wrong format');
       end
       cur_data = util{i};
       trace = TimeSeries(LinAlg.vector(cur_data(:,1)), LinAlg.vector(cur_data(:,2)));
       trace.setStartTime(startTime);
       trace.setEndTime(endTime);
       if options.UtilInterval > 0.0
           repo.setAggregatedData(StandardMetric.UTILIZATION, resList.get(i - 1), trace, options.UtilInterval);
       else
           repo.setData(StandardMetric.UTILIZATION, resList.get(i - 1), trace);
       end
    end
end

if ~isempty(options.Rt)
    rt = options.Rt;
    if length(rt) ~= length(serviceNames)
        error('The number of elements in the response time cell array must be equal to the number of services.');
    end
    for i=1:length(rt)
       if size(rt{i}, 2) ~= 2
           error('Wrong format');
       end
       cur_data = rt{i};
       trace = TimeSeries(LinAlg.vector(cur_data(:,1)), LinAlg.vector(cur_data(:,2)));
       trace.setStartTime(startTime);
       trace.setEndTime(endTime);
       if options.RtInterval > 0.0
           repo.setAggregatedData(StandardMetric.RESPONSE_TIME, serviceList.get(i - 1), trace, options.RtInterval);
       else
           repo.setData(StandardMetric.RESPONSE_TIME, serviceList.get(i - 1), trace);
       end
    end
end

if ~isempty(options.Tput)
    tput = options.Tput;
    if length(tput) ~= length(serviceNames)
        error('The number of elements in the throughput cell array must be equal to the number of resources.');
    end
    for i=1:length(tput)
       if size(tput{i}, 2) ~= 2
           error('Wrong format');
       end
       cur_data = tput{i};
       trace = TimeSeries(LinAlg.vector(cur_data(:,1)), LinAlg.vector(cur_data(:,2)));
       trace.setStartTime(startTime);
       trace.setEndTime(endTime);
       if options.TputInterval > 0.0
           repo.setAggregatedData(StandardMetric.THROUGHPUT, serviceList.get(i - 1), trace, options.TputInterval);
       else
           repo.setData(StandardMetric.THROUGHPUT, serviceList.get(i - 1), trace);
       end
    end
end

result = EstimationHelper.runEstimationWithCrossValidation(approach, workload, repo, startTime, options.StepSize, options.Window, options.Iterative);

for i=1:result.size()
    run = result.get(i - 1);
    curEst = result.estimates.getData().toArray1D();
    curErrUtil = result.relativeUtilizationError.toArray1D();
    curErrResp = result.relativeResponseTimeError.toArray1D();
end


end