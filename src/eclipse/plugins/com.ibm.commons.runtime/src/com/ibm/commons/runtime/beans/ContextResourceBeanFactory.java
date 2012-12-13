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

package com.ibm.commons.runtime.beans;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.util.io.StreamUtil;

/**
 * This class creates the managed beans by reading a faces-config.xml from a URL
 * retrieved from a JNDI lookup.
 *  
 * @author Mark Wallace
 */
public class ContextResourceBeanFactory extends AbstractXmlConfigBeanFactory {

	static final String sourceClass = ContextResourceBeanFactory.class.getName();
	static final Logger logger = Logger.getLogger(sourceClass);
	
	/**
	 * Default constructor
	 */
	public ContextResourceBeanFactory() {
		setFactories(readFactories());
	}
	
	/*
	 * Read factories using location specified in application configuration e.g. web.xml context-param tag
	 */
	private Factory[] readFactories() {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "readFactories"); //$NON-NLS-1$
		}
		
		Factory[] factories = null;
		Application application = Application.getUnchecked();
		if (application != null) {
			try {
				InitialContext initCtx = new InitialContext();
				Context envCtx = (Context) initCtx.lookup("java:comp/env"); //$NON-NLS-1$
				if (envCtx != null) {
					factories = readFactoriesFromUrl(envCtx);
				}
			} catch (NoInitialContextException nice) {
				// ignoring this, only known to happen when running unit tests
			} catch (NamingException ne) {
				logger.log(Level.SEVERE, "Error reading context bean factories", ne);
			}
		}
		
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "readFactories", factories); //$NON-NLS-1$
		}
		return factories;
	}

	/*
	 * Lookup the URL which points to the faces-config.xml.
	 * 
	 * @param envCtx
	 * @throws NamingException
	 */
	private Factory[] readFactoriesFromUrl(Context envCtx) {
		try {
			URL url = (URL)envCtx.lookup("url/FacesConfigXml"); //$NON-NLS-1$
			if (url != null) {
				if (logger.isLoggable(Level.FINER)) {
					logger.finer("Reading context bean factories from: " + url);
				}
				InputStream inStream = null;
				try {
					inStream = url.openStream();
					return readFactories(inStream);
				} catch (IOException ioe) {
					logger.log(Level.SEVERE, "Error reading context bean factories from: "+url, ioe);
				} finally {
					StreamUtil.close(inStream);
				}
			}
		} catch (Exception e) {
			if (canIgnore(e)) {
				// can ignore this, just means the url/FacesConfigXml has not be defined
				logger.finer("Resource url/FacesConfigXml is not available");
			}
			else {
				logger.log(Level.SEVERE, "Error reading context bean factories from url/FacesConfigXml", e);
			}
		}
		return null;
	}
	
	/*
	 * Return true if the exception was caused by a naming exception
	 */
	private boolean canIgnore(Exception e) {
		if (e instanceof NamingException) {
			return true;
		}
		if (e.getCause() != null && e.getCause() instanceof NamingException) {
			return true;
		}
		return false;
	}

}
