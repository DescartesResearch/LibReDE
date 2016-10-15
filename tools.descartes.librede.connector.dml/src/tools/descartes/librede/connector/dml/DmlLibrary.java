package tools.descartes.librede.connector.dml;

import tools.descartes.librede.export.IExporter;
import tools.descartes.librede.registry.Registry;

public class DmlLibrary {
	
	public static void init() {
		Registry.INSTANCE.registerImplementationType(IExporter.class, DmlExport.class);
	}

}
