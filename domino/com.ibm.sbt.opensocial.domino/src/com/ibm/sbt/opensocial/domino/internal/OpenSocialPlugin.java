package com.ibm.sbt.opensocial.domino.internal;

import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class OpenSocialPlugin implements BundleActivator {
	
	public static final String ID = "com.ibm.sbt.opensocial.domino";
	private static Logger logger = Logger.getLogger(OpenSocialPlugin.class.getName());

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		OpenSocialPlugin.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		OpenSocialPlugin.context = null;
	}
	
	/**
	 * Gets the logger for this plugin.
	 * @return The logger for this plugin.
	 */
	public static Logger getLogger() {
		return logger;
	}

}
