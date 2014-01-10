package edu.kit.ipd.descartes.librede.nnls;

import java.io.File;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.sun.jna.NativeLibrary;


public class NNLSActivator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		NNLSActivator.context = bundleContext;
		
		// Check that native library part is available
		System.loadLibrary("NNLS");
		
		// Add base location of plugin to the native library search path
		// of JNA so that it can find the library
		String pluginBasePath = new File(FileLocator.toFileURL(context.getBundle().getEntry("/")).getFile()).getAbsolutePath();
		NativeLibrary.addSearchPath("NNLS", pluginBasePath);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		NNLSActivator.context = null;
	}

}
