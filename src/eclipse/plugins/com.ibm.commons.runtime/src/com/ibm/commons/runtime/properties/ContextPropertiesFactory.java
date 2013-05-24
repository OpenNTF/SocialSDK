/*
 * © Copyright IBM Corp. 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.commons.runtime.properties;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.util.io.StreamUtil;

/**
 * This class creates the properties by reading a sbt.properties from a URL
 * retrieved from a JNDI lookup.
 * 
 * @author mwallace
 *
 */
public class ContextPropertiesFactory extends AbstractPropertiesFactory {
	
	private Properties properties;
	
	static final String sourceClass = ContextPropertiesFactory.class.getName();
	static final Logger logger = Logger.getLogger(sourceClass);
	
	/**
	 * Constructor will load the properties from URL retrieved from a JNDI lookup
	 */
	public ContextPropertiesFactory() {
		readProperties();
	}

	/* (non-Javadoc)
	 * @see com.ibm.commons.runtime.impl.PropertiesFactory#getProperty(java.lang.String)
	 */
	@Override
	public String getProperty(String name) {
		return (properties != null) ? properties.getProperty(name) : null;
	}

	/*
	 * Read factories using location specified in application configuration e.g. web.xml context-param tag
	 */
	private Properties readProperties() {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "readProperties"); //$NON-NLS-1$
		}
		
		Properties properties = null;
		Application application = Application.getUnchecked();
		if (application != null) {
			try {
				InitialContext initCtx = new InitialContext();
				Context envCtx = (Context) initCtx.lookup("java:comp/env"); //$NON-NLS-1$
				if (envCtx != null) {
					properties = readPropertiesFromUrl(envCtx);
				}
			} catch (NoInitialContextException nice) {
				// ignoring this, only known to happen when running unit tests
			} catch (NamingException ne) {
				logger.log(Level.WARNING, ne.getMessage());
			}
		}
		
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "readProperties", properties); //$NON-NLS-1$
		}
		return properties;
	}

	/*
	 * Lookup the URL which points to the sbt.properties.
	 * 
	 * @param envCtx
	 * @throws NamingException
	 */
	private Properties readPropertiesFromUrl(Context envCtx) {
		try {
			URL url = (URL)envCtx.lookup(EnvironmentConfig.INSTANCE.getSbtPropertiesFileConfig()); //$NON-NLS-1$
			if (url != null) {
				if (logger.isLoggable(Level.FINER)) {
					logger.finer("Reading context properties from: " + url);
				}
				InputStream inStream = null;
				try {
					inStream = url.openStream();
					properties = new Properties();
					properties.load(inStream);
					return properties;
				} catch (IOException ioe) {
					logger.log(Level.SEVERE, "Error reading context properties from: "+url, ioe);
				} finally {
					StreamUtil.close(inStream);
				}
			}
		} catch (Exception e) {
			if (causeByNameNotFound(e)) {
				// can ignore this, just means the url/ibmsbt-sbtproperties has not be defined
				logger.log(Level.FINER, "Resource {0} is not available", EnvironmentConfig.INSTANCE.getSbtPropertiesFileConfig());
			}
			else {
				logger.log(Level.SEVERE, "Error reading context properties from " + EnvironmentConfig.INSTANCE.getSbtPropertiesFileConfig(), e);
			}
		}
		return null;
	}
	
	/*
	 * Return true if the exception was caused by a name not found error
	 */
	private boolean causeByNameNotFound(Exception e) {
		if (e instanceof NameNotFoundException) {
			return true;
		}
		if (e.getCause() != null && e.getCause() instanceof NameNotFoundException) {
			return true;
		}
		return false;
	}

	
}
