package edu.kit.ipd.descartes.librede.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;


public class LibredeUIPlugin extends AbstractUIPlugin {
	
	public static final String PLUGIN_ID = "edu.kit.ipdate.descartes.librede.ui";

	// shared instance
	private static LibredeUIPlugin instance;
	
	public LibredeUIPlugin() {
		instance = this;
	}
	
	public static LibredeUIPlugin getDefault() {
		return instance;
	}

}
