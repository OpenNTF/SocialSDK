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
 * SBT ManagedBean Factory.
 *
 * This class creates the managed beans when necessary.
 *  
 * @author Philippe Riand
 */
public abstract class ManagedBeanFactory {

	/**
	 * Actual bean factory, also returning the scope.
	 * @author user
	 */
	public interface BeanFactory {
		public int getScope();
		public Object create(ClassLoader classLoader);
	}

	/**
	 * Create a bean giving a name.
	 * 
	 * @param name
	 * @return
	 */
	public abstract BeanFactory getBeanFactory(String name);
}
