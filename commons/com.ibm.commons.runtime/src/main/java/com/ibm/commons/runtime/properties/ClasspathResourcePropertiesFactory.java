/*
 *  Copyright IBM Corp. 2012
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

import java.io.InputStream;
import java.util.Properties;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.StreamUtil;



/**
 * SBT Properties factory.
 *
 * @author Philippe Riand
 */
public class ClasspathResourcePropertiesFactory extends AbstractPropertiesFactory {

	public static final String DEFAULT_PROPERTIES = "sbt.properties";
		
	private Properties properties;
	
	public ClasspathResourcePropertiesFactory() {
		this(DEFAULT_PROPERTIES);
	}

	@Override
	public String getProperty(String name) {
		return properties!=null ? properties.getProperty(name) : null;
	}
	
	public ClasspathResourcePropertiesFactory(String fileName) {
		this.properties = readFactoriesFromClasspathResource(fileName);
	}
	
	public Properties readFactoriesFromClasspathResource(String resourceName) {
		if(StringUtil.isEmpty(resourceName)) {
			return null;
		}
		// We read the resource
		try {
			Properties properties = new Properties();
			InputStream is = getClass().getResourceAsStream(resourceName);
			if(is!=null) {
				try {
					properties.load(is);
					return properties;
				} finally {
					StreamUtil.close(is);
				}
			}
		} catch(Throwable t) {
			t.printStackTrace();
		}
		return null;
	}
}
