package tools.descartes.librede;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class LibredePlugin implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		LibredeLibrary.init();
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		
	}

}
