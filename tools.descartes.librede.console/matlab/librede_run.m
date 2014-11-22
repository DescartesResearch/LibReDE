function [approaches, estimates, relErrUtil, relErrResp] = librede_run(repository, startTime, endTime, varargin)

options = struct(...
    'Approaches', [], ...
    'StepSize', 60, ...
    'Iterative', 0, ...
    'Window', 15, ...
    'Folds', 0);

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
import tools.descartes.librede.linalg.*;
import tools.descartes.librede.estimation.workload.*;
import tools.descartes.librede.estimation.repository.*;
import tools.descartes.librede.approaches.*;
import tools.descartes.librede.frontend.*;


repository.setCurrentTime(endTime);
result = EstimationHelper.runEstimationWithCrossValidation(options.Approaches, repository, startTime, options.StepSize, options.Window, options.Iterative, options.Folds);

jApp = result.keySet().toArray();
nApproaches = length(jApp);
approaches = cell(nApproaches, 1);
estimates = cell(nApproaches, options.Folds);
relErrUtil = zeros(nApproaches, repository.getWorkload().getResources().size());
relErrResp = zeros(nApproaches, repository.getWorkload().getServices().size());

for i=1:nApproaches
    cur = result.get(jApp(i));
    approaches{i} = jApp(i);
    relErrUtil(i,:) = cur.getMeanRelativeUtilizationError().toArray1D();
    relErrResp(i,:) = cur.getMeanRelativeResponseTimeError().toArray1D();
end



end