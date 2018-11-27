/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2018, by Simon Spinner, Johannes Grohmann
 *  and Contributors.
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
package tools.descartes.librede.datasource.kiekeramqp;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class CopyThread extends Thread {

	private String sourcedir;
	private String targetdir;
	
	public CopyThread(String sourcedir, String targetdir) {
		this.sourcedir =sourcedir;
		this.targetdir =targetdir;
	}
	
	
	@Override
	public void run() {
		File srcDir = new File(sourcedir);
		File destDir = new File(targetdir);
		try {
			FileUtils.copyDirectoryToDirectory(srcDir, destDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			FileUtils.deleteDirectory(srcDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
