package edu.kit.ipd.descartes.redeem.bayesplusplus;

import java.io.File;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.sun.jna.NativeLibrary;

public class BayesPlusPlusBundleActivator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		// Check that native library part is available
		System.loadLibrary("BayesPlusPlus");
		
		// Add base location of plugin to the native library search path
		// of JNA so that it can find the library
		String pluginBasePath = new File(FileLocator.toFileURL(context.getBundle().getEntry("/")).getFile()).getAbsolutePath();
		NativeLibrary.addSearchPath("BayesPlusPlus", pluginBasePath);		
	}

	@Override
	public void stop(BundleContext context) throws Exception {		
	}

}
