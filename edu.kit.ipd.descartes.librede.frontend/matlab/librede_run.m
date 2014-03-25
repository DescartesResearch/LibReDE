function [estimates, relErrUtil, relErrResp] = librede_run(repository, startTime, endTime, step, window)

% options = struct(...
%     'Util', [], ...
%     'UtilInterval', 0, ...
%     'Rt', [], ...
%     'RtInterval', 0, ...
%     'Tput', [], ...
%     'TputInterval', 0, ...
%     'StepSize', 1, ...
%     'Iterative', 0, ...
%     'Window', 1, ...
%     'ProcessorCount', ones(length(resources)));
% 
% optionNames = fieldnames(options);
% 
% nVarArgs = length(varargin);
% if round(nVarArgs/2)~=nVarArgs/2
%    error('Expected pairs of property/value.')
% end
% 
% for pair = reshape(varargin,2,[])
%    inpName = pair{1};
% 
%    if any(strcmpi(inpName,optionNames))
%        options.(inpName) = pair{2};
%    else
%       error('%s is not a recognized parameter name',inpName)
%    end
% end

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


repository.setCurrentTime(endTime);
result = EstimationHelper.runEstimationWithCrossValidation('ServiceDemandLaw', repository.getWorkload(), repository, startTime, step, window, 0);

for i=1:result.size()
    run = result.get(i - 1);
    curEst = run.estimates.getData().toArray1D();
    curErrUtil = run.relativeUtilizationError.toArray1D();
    curErrResp = run.relativeResponseTimeError.toArray1D();
end


end