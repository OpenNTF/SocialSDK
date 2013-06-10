package com.ibm.xsp.extlib.plugin;

import org.eclipse.core.runtime.Plugin;

import com.ibm.xsp.extlib.minifier.ExtLibLoaderExtension;
import com.ibm.xsp.extlib.minifier.SBTLoader;

public class SBTPluginActivator extends Plugin {

	public static SBTPluginActivator instance;
	
	public SBTPluginActivator() {
		instance = this;
		
        ExtLibLoaderExtension.getExtensions().add(new SBTLoader());
	}

}
