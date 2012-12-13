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
package com.ibm.commons.runtime.naming;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

/**
 * ObjectFactory which will return a java.net.URL instance. Required for use with Tomcat
 * as it doesn't provide access for looking up URL's using JNDI.
 * 
 * @author Mark Wallace
 */
public class UrlFactory implements ObjectFactory {

	static final String sourceClass = UrlFactory.class.getName();
	static final Logger logger = Logger.getLogger(sourceClass);

	/* (non-Javadoc)
	 * @see javax.naming.spi.ObjectFactory#getObjectInstance(java.lang.Object, javax.naming.Name, javax.naming.Context, java.util.Hashtable)
	 */
	@Override
	public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getObjectInstance");
		}
		
		URL url = null;
		if (obj != null && obj instanceof Reference) {
			Reference reference = (Reference)obj;
			if ("java.net.URL".equals(reference.getClassName())) {
				RefAddr refAddr = reference.get("url");
				if (refAddr != null) {
					String urlStr = (String) refAddr.getContent();
					if (urlStr != null && urlStr.length() > 0) {
						url = createUrl(urlStr);
					}
				}				
			}
		}
		
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getObjectInstance", url);
		}
		return url;
	}

	/**
	 * Create URL from a string specification provide
	 * 
	 * @param urlStr
	 * @return
	 */
	protected URL createUrl(String urlStr) {
		if (logger.isLoggable(Level.FINER)) {
			logger.finer("Creating URL instance for: " + urlStr);
		}
		URL url = null;
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException mue) {
			// try using the config path as the context for the URL
			String catalinaBase = System.getProperty("catalina.base");
			if (catalinaBase != null && catalinaBase.length() > 0) {
				try {
					File contextDir = new File(catalinaBase, "conf");
					if (contextDir.exists()) {
						File theFile = new File(contextDir, urlStr);
						if (theFile.exists()) {
							url = theFile.toURI().toURL();
						}
					}
				}
				catch (Exception e) {
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error creating URL instance for: "+url, e);
		}
		return url;
	}

}
