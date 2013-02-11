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

import java.io.InputStream;

import com.ibm.commons.util.StringUtil;


/**
 * SBT ManagedBean Factory.
 *
 * This class reads a managed-beans file from the classpath resources, within the same
 * package than the factory class.
 *  
 * @author Philippe Riand
 */
public abstract class ClasspathResourceBeanFactory extends AbstractXmlConfigBeanFactory {

	public static final String DEFAULT_RESOURCENAME = EnvironmentConfig.INSTANCE.getEnvironmentConfig();
	
	
	public ClasspathResourceBeanFactory() {
		this(DEFAULT_RESOURCENAME);
	}

	public ClasspathResourceBeanFactory(String fileName) {
		setFactories(readFactoriesFromClasspathResource(fileName));
	}
	
	public Factory[] readFactoriesFromClasspathResource(String resourceName) {
		if(StringUtil.isEmpty(resourceName)) {
			return null;
		}
		// We read the resource
		InputStream is = getClass().getResourceAsStream(resourceName);
		return readFactories(is);
	}
}
