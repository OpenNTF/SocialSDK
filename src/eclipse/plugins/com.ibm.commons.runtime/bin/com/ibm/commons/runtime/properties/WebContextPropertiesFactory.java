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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import com.ibm.commons.runtime.Application;

/**
 * Properties factory implementation which reads init parameters from the ServletContext.
 *
 * @author Mark Wallace
 */
public class WebContextPropertiesFactory extends AbstractPropertiesFactory {
	
	static final String sourceClass = WebContextPropertiesFactory.class.getName();
	static final Logger logger = Logger.getLogger(sourceClass);
	
	/**
	 * Default constructor
	 */
	public WebContextPropertiesFactory() {
	}

	/* (non-Javadoc)
	 * @see com.ibm.commons.runtime.impl.PropertiesFactory#getProperty(java.lang.String)
	 */
	@Override
	public String getProperty(String name) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getProperty", name);
		}
		
		String value = null;
		Application application = Application.getUnchecked();
		if (application != null) {
			Object context = application.getApplicationContext();
			if (context instanceof ServletContext) {
				ServletContext servletContext = (ServletContext)context;
				value = servletContext.getInitParameter(name);
			}
		}

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getProperty", value);
		}
		return value;
	}

}
