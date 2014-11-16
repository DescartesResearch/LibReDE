/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2014, by Simon Spinner and Contributors.
 *
 * Project Info:   http://www.descartes-research.net/
 *
 * All rights reserved. This software is made available under the terms of the
 * Eclipse Public License (EPL) v1.0 as published by the Eclipse Foundation
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse Public License (EPL)
 * for more details.
 *
 * You should have received a copy of the Eclipse Public License (EPL)
 * along with this software; if not visit http://www.eclipse.org or write to
 * Eclipse Foundation, Inc., 308 SW First Avenue, Suite 110, Portland, 97204 USA
 * Email: license (at) eclipse.org
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 */
package tools.descartes.librede.bayesplusplus;

import java.io.File;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.sun.jna.NativeLibrary;

public class BayesPlusPlusBundleActivator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {		
		if (Platform.getOS().equals(Platform.OS_WIN32)) {
			// Workaround: On windows the plugin contains all dependent libraries.
			// However, java cannot find them automatically. Therefore we have to load
			// them manually.
			System.loadLibrary("libwinpthread-1");
			if (Platform.getOSArch().equals(Platform.ARCH_X86)) {
				System.loadLibrary("libgcc_s_dw2-1");
			} else {
				System.loadLibrary("libgcc_s_seh-1");
			}
			System.loadLibrary("libstdc++-6");
		}
		// Check that native library part is available
		System.loadLibrary("BayesPlusPlus");
		
		// Add base location of plugin to the native library search path
		// of JNA so that it can find the library
		String pluginBasePath = new File(FileLocator.toFileURL(context.getBundle().getEntry("/os/" + Platform.getOS() + "/" + Platform.getOSArch())).getFile()).getAbsolutePath();
		NativeLibrary.addSearchPath("BayesPlusPlus", pluginBasePath);
	}

	@Override
	public void stop(BundleContext context) throws Exception {		
	}

}
