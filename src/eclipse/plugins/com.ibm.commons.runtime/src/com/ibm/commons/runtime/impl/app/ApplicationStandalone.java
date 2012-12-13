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
package com.ibm.commons.runtime.impl.app;

import java.io.InputStream;
import com.ibm.commons.runtime.impl.AbstractApplication;

/**
 * @author Mark Wallace
 * @date 6 Dec 2012
 * TODO Why is AbstractJ2eeApplication J2EE specific?
 */
public class ApplicationStandalone extends AbstractApplication {

	/**
	 * @param applicationContext
	 */
	protected ApplicationStandalone(Object applicationContext) {
		super(applicationContext);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.commons.runtime.Application#getResourceAsStream(java.lang.String)
	 */
	@Override
	public InputStream getResourceAsStream(String name) {
		return this.getClassLoader().getResourceAsStream(name);
	}

}
