package edu.kit.ipd.descartes.redeem.ipopt.java;

import java.io.File;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.sun.jna.NativeLibrary;

public class IpoptActivator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		IpoptActivator.context = bundleContext;
		
		// Check that native library part is available
		System.loadLibrary("IpOpt");
		
		// Add base location of plugin to the native library search path
		// of JNA so that it can find the library
		String pluginBasePath = new File(FileLocator.toFileURL(context.getBundle().getEntry("/")).getFile()).getAbsolutePath();
		NativeLibrary.addSearchPath("IpOpt", pluginBasePath);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		IpoptActivator.context = null;
	}

}
