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
import com.ibm.commons.runtime.Application;
import com.ibm.commons.util.StringUtil;



/**
 * SBT ManagedBean Factory.
 *
 * This class creates the managed beans when necessary.
 * 
 * @author Philippe Riand
 */
public class WebResourceBeanFactory extends AbstractXmlConfigBeanFactory {

	public static final String DEFAULT_FILENAME = "/WEB-INF/faces-config.xml";

	public WebResourceBeanFactory() {
		this(DEFAULT_FILENAME);
	}

	public WebResourceBeanFactory(String fileName) {
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
		InputStream is = app.getResourceAsStream(fileName);
		return readFactories(is);
	}
}
