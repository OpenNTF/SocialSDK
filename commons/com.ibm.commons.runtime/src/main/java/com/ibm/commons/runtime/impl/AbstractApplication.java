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

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.ibm.commons.extension.ExtensionManager;
import com.ibm.commons.runtime.Application;
import com.ibm.commons.util.QuickSort;



/**
 * SBT Application abstract implementation for J2EE servers.
 * 
 * This class implements the managed beans and properties factories. 
 *  
 * @author Philippe Riand
 */
public abstract class AbstractApplication extends Application {

    private static final String MANAGEDBEAN_FACTORY = "com.ibm.commons.runtime.managedbeansfactory";
    private static final String PROPERTIES_FACTORY = "com.ibm.commons.runtime.propertiesfactory";
	
	private int references;
	private Object applicationContext;
	private Properties properties;
	private HashMap<String,Object> scope = new HashMap<String,Object>(); 

	private ManagedBeanFactory[] managedBeanFactories;
	private PropertiesFactory[] propertiesFactories;
	
	private Map<String,List<Object>> services;
	
	protected AbstractApplication(Object applicationContext) {
		this.applicationContext = applicationContext;
		this.properties = new Properties();
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
	public ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
	
	@Override
	public Object getApplicationContext() {
		return applicationContext;
	}

	@Override
	public Map<String,Object> getScope() {
		return scope;
	}

	@Override
	public String getProperty(String name) {
		String s = properties.getProperty(name);
		if(s!=null) {
			return s;
		}
		PropertiesFactory[] f = getPropertiesFactories();
		for(int i=0; i<f.length; i++) {
			s = f[i].getProperty(name);
			if(s!=null) {
				return s;
			}
		}
		return null;
	}
	
	@Override
	public void setProperty(String name, String value) {
		properties.setProperty(name,value);
	}
	
	public ManagedBeanFactory[] getManagedBeanFactories() {
		if(managedBeanFactories==null) {
			synchronized(this) {
				if(managedBeanFactories==null) {
		            List<ManagedBeanFactory> l = AccessController.doPrivileged(new PrivilegedAction<List<ManagedBeanFactory>>() {
		                @Override
						public List<ManagedBeanFactory> run() {
		                    List<ManagedBeanFactory> l = (List)ExtensionManager.findApplicationServices(getClassLoader(),MANAGEDBEAN_FACTORY); 
		                    return l;
		                }
		            });
		            managedBeanFactories = l.toArray(new ManagedBeanFactory[l.size()]);
				}
			}
		}
		return managedBeanFactories;
	}
	
	public PropertiesFactory[] getPropertiesFactories() {
		if(propertiesFactories==null) {
			synchronized(this) {
				if(propertiesFactories==null) {
		            List<PropertiesFactory> l = AccessController.doPrivileged(new PrivilegedAction<List<PropertiesFactory>>() {
		                @Override
						public List<PropertiesFactory> run() {
		                    List<PropertiesFactory> l = (List)ExtensionManager.findApplicationServices(getClassLoader(),PROPERTIES_FACTORY); 
		                    return l;
		                }
		            });
		            propertiesFactories = (PropertiesFactory[])l.toArray(new PropertiesFactory[l.size()]);
		            // Make sure they are accessed in order
		            (new QuickSort.ObjectArray(propertiesFactories) {
						@Override
						public int compare(Object o1, Object o2) {
							PropertiesFactory p1 = (PropertiesFactory)o1;
							PropertiesFactory p2 = (PropertiesFactory)o2;
							return p1.getPriority()-p2.getPriority();
						}
		            	
		            }).sort();
				}
			}
		}
		return propertiesFactories;
	}
		
	@Override
	public List<Object> findServices(String serviceName) {
		if(services==null) {
			services = new HashMap<String, List<Object>>();
		}
		return ExtensionManager.findApplicationServices(services, getClassLoader(), serviceName);
	}	
}
