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
package com.ibm.sbt.config;

import java.util.HashMap;
import java.util.Map;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.impl.ManagedBeanFactory;

/**
 * @author mwallace
 *
 */
public class MockManagedBeansFactory extends ManagedBeanFactory {
	
	private Map<String, BeanFactory> endpoints = new HashMap<String, BeanFactory>();
	
	/**
	 * Constructor
	 */
	public MockManagedBeansFactory() {
		endpoints.put("connections", new MockBeanFactory(Context.SCOPE_GLOBAL, new MockBasicEndpoint()));
		endpoints.put("smartcloud", new MockBeanFactory(Context.SCOPE_GLOBAL, new MockOAuthEndpoint()));
	}

	/* (non-Javadoc)
	 * @see com.ibm.commons.runtime.impl.ManagedBeanFactory#getBeanFactory(java.lang.String)
	 */
	@Override
	public BeanFactory getBeanFactory(String name) {
		return endpoints.get(name);
	}
	
	/*
	 * Mock BeanFactory implementation
	 */
	class MockBeanFactory implements BeanFactory {
		
		private int scope;
		private Object object;
		
		MockBeanFactory(int scope, Object object) {
			this.scope = scope;
			this.object = object;
		}

		/* (non-Javadoc)
		 * @see com.ibm.commons.runtime.impl.ManagedBeanFactory.BeanFactory#getScope()
		 */
		@Override
		public int getScope() {
			return scope;
		}

		/* (non-Javadoc)
		 * @see com.ibm.commons.runtime.impl.ManagedBeanFactory.BeanFactory#create(java.lang.ClassLoader)
		 */
		@Override
		public Object create(ClassLoader classLoader) {
			return object;
		}
		
	}

}
