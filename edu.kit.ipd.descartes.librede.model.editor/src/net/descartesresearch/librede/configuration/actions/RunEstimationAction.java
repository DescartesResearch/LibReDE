package net.descartesresearch.librede.configuration.actions;

import java.io.PrintStream;

import net.descartesresearch.librede.configuration.presentation.LibredeEditorPlugin;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.action.Action;
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

public class RunEstimationAction extends Action {

	private static final String CONSOLE_NAME = "LibredeConsole";

	public RunEstimationAction() {
		super();
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
		MessageConsoleStream out = console.newMessageStream();
		out.setActivateOnWrite(true);
		PrintStream oldOut = System.out;
		System.setOut(new PrintStream(out));


		System.setOut(oldOut);
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

	private void revealConsole(IConsole console) {
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
}
