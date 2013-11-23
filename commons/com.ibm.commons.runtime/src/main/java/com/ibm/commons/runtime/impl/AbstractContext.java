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

import java.util.Map;
import java.util.Properties;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.impl.ManagedBeanFactory.BeanFactory;


/**
 * SBT Context abstract implementation for J2EE servers.
 *
 * @author Philippe Riand
 */
public abstract class AbstractContext extends Context {
	
	private int references;

	private Object request;
	private Object response;
	private Properties properties;
	

	protected AbstractContext(Application application, Object request, Object response) {
		super(application);
		this.request = request;
		this.response = response;
	}

	public int _references() {
		return references;
	}

	public void _incReferences() {
		references++;
	}

	public void _decReferences() {
		references--;
	}
	
	@Override
	public Object getRequest() {
		return request;
	}

	@Override
	public Object getResponse() {
		return response;
	}

	@Override
	public AbstractApplication getApplication() {
		return (AbstractApplication)super.getApplication();
	}
	
	@Override
	public AbstractApplication getApplicationUnchecked() {
		return (AbstractApplication)super.getApplicationUnchecked();
	}
	
	@Override
	public String getProperty(String propertyName) {
		return getProperty(propertyName,null);
	}

	@Override
	public String getProperty(String propertyName, String defaultValue) {
		if(properties!=null) {
			String v = properties.getProperty(propertyName);
			if(v!=null) {
				return v;
			}
		}
		Application app = getApplicationUnchecked();
		if(app!=null) {
			String v = app.getProperty(propertyName);
			if(v!=null) {
				return v;
			}
		}
		return defaultValue;
	}

	@Override
	public void setProperty(String propertyName, String value) {
		if(properties==null) {
			properties = new Properties();
		}
		properties.setProperty(propertyName,value);
	}

	@Override
	public Object getBean(String beanName) {
		// Look for a bean in any of the available scope
		Object bean = getRequestMap().get(beanName);
		if(bean!=null) {
			return bean;
		}
		bean = getSessionMap().get(beanName);
		if(bean!=null) {
			return bean;
		}
		bean = getApplicationMap().get(beanName);
		if(bean!=null) {
			return bean;
		}
		bean = getGlobalMap().get(beanName);
		if(bean!=null) {
			return bean;
		}
		
		// Ok, we need to create it if possible
		AbstractApplication app = getApplicationUnchecked();
		if(app!=null) {
			ManagedBeanFactory[] factories = app.getManagedBeanFactories();
			if(factories!=null && factories.length>0) {
				for(int i=0; i<factories.length; i++) {
					BeanFactory factory = factories[i].getBeanFactory(beanName);
					if(factory!=null) {
						Map<String,Object> map = getScope(factory.getScope());
						synchronized(map) {
							Object o = map.get(beanName);
							if(o==null) {
								o = factory.create(getClassLoader());
								if(o!=null) {
									map.put(beanName, o);
								}
							}
							return o;
						}
					}
				}
			}
		}

		// Ok, there is no bean
		return null;
	}
}
