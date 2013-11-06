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
package com.ibm.commons.runtime.impl;


/**
 * SBT Properties Factory.
 *
 * @author Philippe Riand
 */
public abstract class PropertiesFactory {

	public static final int DEFAULT_PRIORITY	= 100;
	
	public int getPriority() {
		return DEFAULT_PRIORITY;
	}
	
	/**
	 * Get the value of a property.
	 * 
	 * @param name
	 * @return
	 */
	public abstract String getProperty(String name);
}
