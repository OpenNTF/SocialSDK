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
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import com.ibm.commons.runtime.Application;
import com.ibm.commons.util.StringUtil;



/**
 * SBT ManagedBean Factory.
 *
 * This class creates the managed beans when necessary.
 * 
 * @author Mark Wallace
 */
public class MetaResourceBeanFactory extends AbstractXmlConfigBeanFactory {

	public static final String DEFAULT_FILENAME = "META-INF/"+ EnvironmentConfig.INSTANCE.getEnvironmentConfig();

	public MetaResourceBeanFactory() {
		this(DEFAULT_FILENAME);
	}

	public MetaResourceBeanFactory(String fileName) {
		setFactories(readFactoriesFromWebResource(fileName));
	}

	public Factory[] readFactoriesFromWebResource(String fileName) {
		if(StringUtil.isEmpty(fileName)) {
			return null;
		}
		// We read the resource
		Application app = Application.getUnchecked();
		if(app==null) {
			return null;
		}
		ClassLoader loader = app.getClassLoader();
		List<Factory> list = new ArrayList<Factory>();
		try {
			Enumeration<URL> resources = loader.getResources(fileName);
			while (resources.hasMoreElements()) {
				URL url = resources.nextElement();
				Factory[] factories = readFactories(url.openStream());
				for (Factory factory : factories) {
					list.add(factory);
				}
			}
		} catch (IOException ioe) {
		}

		return list.toArray(new Factory[list.size()]);
	}
}
