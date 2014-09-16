package edu.kit.ipd.descartes.librede.estimation.repository;

import net.descartesresearch.librede.configuration.ConfigurationFactory;
import net.descartesresearch.librede.configuration.Resource;
import net.descartesresearch.librede.configuration.Service;

public class WorkloadBuilder {
	
	public static Resource newResource(String name) {
		Resource res = ConfigurationFactory.eINSTANCE.createResource();
		res.setName(name);
		return res;
	}
	
	public static Service newService(String name) {
		Service serv = ConfigurationFactory.eINSTANCE.createService();
		serv.setName(name);
		return serv;
	}

}
