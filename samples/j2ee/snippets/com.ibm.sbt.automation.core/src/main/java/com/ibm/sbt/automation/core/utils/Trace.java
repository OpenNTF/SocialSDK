package com.ibm.sbt.automation.core.utils;

import com.ibm.sbt.automation.core.environment.TestEnvironment;
import com.ibm.sbt.automation.core.environment.TestEnvironmentFactory;

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

/**
 * @author mkataria
 * @date Feb 6, 2013
 */
public class Trace {

	public static void log(String msg) {
		TestEnvironment environment = TestEnvironmentFactory.getEnvironment();
		if (environment != null && "true".equalsIgnoreCase(environment.getProperty(TestEnvironment.PROP_ENABLED_TRACE))) {
			System.out.println(msg);
		}
	}

}
