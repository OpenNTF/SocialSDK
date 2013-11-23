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
package com.ibm.sbt.jslibrary;

import java.util.List;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;



/**
 * SBT Environment Factory.
 * 
 * @author Philippe Riand
 */
public abstract class SBTEnvironmentFactory {

	public static final String ENVIRONMENT_FACTORY = "com.ibm.sbt.core.environmentfactory";
	
	/**
	 * Get an environment by its name.
	 * @param name
	 * @return the corresponding environment, or null if not found
	 */
	public static SBTEnvironment get(String name) {
		Application app = Application.getUnchecked();
		if (app != null) {
			List<Object> factories = app.findServices(ENVIRONMENT_FACTORY);
			for(int i=0; i<factories.size(); i++) {
				SBTEnvironmentFactory factory = (SBTEnvironmentFactory)factories.get(i);
				SBTEnvironment env = factory.getEnvironment(name);
				if(env!=null) {
					return env;
				}
			}
		}
		return null;
	}
	
	/**
	 * Get the a environment from its name.
	 * 
	 * @param name
	 * @return
	 */
	public abstract SBTEnvironment getEnvironment(String name);
}
