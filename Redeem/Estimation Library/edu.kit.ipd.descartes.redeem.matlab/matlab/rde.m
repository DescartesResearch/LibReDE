function [estimates] = rde(data, services, resources)

load_classes();

import java.util.*;
import edu.kit.ipd.descartes.redeem.estimation.workload.*;
import edu.kit.ipd.descartes.redeem.estimation.repository.*;

repo = MatrixMonitoringRepository();

%% Init model entities
serviceList = ArrayList(length(services));
for i=1:length(services)
    service = Service(services{i});
    serviceList.add(service);
end

resList = ArrayList(length(resources));
for i=1:length(resources)
    res = Resource(resources{i});
    resList.add(res);
end


%% CPU Utilization
i=2;
for j=length(classes):size(data, 2)
    if ~isempty(data{i,j})
        
    end
end



end