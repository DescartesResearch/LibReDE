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
package net.descartesresearch.librede.configuration.actions;

import java.io.PrintStream;

import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.presentation.LibredeEditorPlugin;

import org.apache.log4j.BasicConfigurator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import edu.kit.ipd.descartes.librede.frontend.EstimationHelper;
import edu.kit.ipd.descartes.librede.frontend.LibredeEstimation;

public class RunEstimationAction extends Action {

	private static final String CONSOLE_NAME = "LibredeConsole";
	
	private LibredeConfiguration conf;

	public RunEstimationAction(LibredeConfiguration conf) {
		super();
		this.conf = conf;
		setToolTipText("Run Estimation...");
		setImageDescriptor(ExtendedImageRegistry.INSTANCE
				.getImageDescriptor(LibredeEditorPlugin.getPlugin().getImage(
						"full/obj16/run_exc")));
	}

	@Override
	public void run() {
		Job estimationJob = new Job("Estimate Resource Demands...") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				runEstimation(monitor);
				return Status.OK_STATUS;
			}

		};
		estimationJob.setUser(true);
		estimationJob.setPriority(Job.SHORT);
		estimationJob.schedule();
	}

	private void runEstimation(IProgressMonitor monitor) {
		MessageConsole console = findConsole(CONSOLE_NAME);
		revealConsole(console);
		MessageConsoleStream out = console.newMessageStream();
		out.setActivateOnWrite(true);
		PrintStream oldOut = System.out;
		PrintStream oldErr = System.err;
		System.setOut(new PrintStream(out));
		System.setErr(new PrintStream(out));

		BasicConfigurator.configure();
		
		LibredeEstimation.execute(conf);

		System.setOut(oldOut);
		System.setErr(oldErr);
	}

	private MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName()))
				return (MessageConsole) existing[i];
		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}

	private void revealConsole(final IConsole console) {
		// We are not on the event processing thread. 
		// All accesses to UI must be forked on the display thread.
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				String id = IConsoleConstants.ID_CONSOLE_VIEW;
				IConsoleView view;
				try {
					view = (IConsoleView) page.showView(id);
					view.display(console);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		});		
	}
}
