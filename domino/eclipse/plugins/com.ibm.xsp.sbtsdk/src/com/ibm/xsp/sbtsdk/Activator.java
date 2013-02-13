package com.ibm.xsp.sbtsdk;

import org.eclipse.core.runtime.Plugin;

import com.ibm.commons.runtime.RuntimeFactory;
import com.ibm.xsp.sbtsdk.runtime.XspRuntimeFactory;

/**
 * Plugin activator
 * @author priand
 */
public class Activator extends Plugin {

	public static Activator instance;
	
	public Activator() {
		instance = this;
		
        //ExtLibLoaderExtension.getExtensions().add(new SbtWebLoader());
        RuntimeFactory.set(new XspRuntimeFactory());
	}
}
