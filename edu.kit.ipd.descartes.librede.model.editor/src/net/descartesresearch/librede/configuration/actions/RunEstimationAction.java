package net.descartesresearch.librede.configuration.actions;

import net.descartesresearch.librede.configuration.presentation.LibredeEditorPlugin;

import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.action.Action;

public class RunEstimationAction extends Action {
	
	public RunEstimationAction() {
		super();
		setToolTipText("Run Estimation...");		
		setImageDescriptor(ExtendedImageRegistry.INSTANCE.getImageDescriptor(LibredeEditorPlugin.getPlugin().getImage("full/obj16/run_exc")));
	}
	
	@Override
	public void run() {
		super.run();
	}

}
