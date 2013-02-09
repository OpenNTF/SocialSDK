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

package com.ibm.commons.runtime;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.commons.Platform;



/**
 * SBT Application.
 *
 * This class encapsulate a an application (J2EE application).
 *  
 * @author Philippe Riand
 */
public abstract class Application {

	protected static Map<ClassLoader,Application> applications = new HashMap<ClassLoader, Application>();
	
	public static Application get() {
		Application app = getUnchecked();
		if(app==null) {
			throw new IllegalStateException("Application is not initialized");
		}
		return app;
	}
	public static Application getUnchecked() {
		return RuntimeFactory.get().getApplicationUnchecked();
	}

	public synchronized static Application init(Object servletContext) {
		return RuntimeFactory.get().initApplication(servletContext);
	}

	public synchronized static void destroy(Application application) {
		RuntimeFactory.get().destroyApplication(application);
	}

	private List<ApplicationListener> listeners;
	
	protected Application() {
	}
	
	public synchronized void addListener(ApplicationListener listener) {
		if(listeners==null) {
			listeners = new ArrayList<ApplicationListener>();
		}
		listeners.add(listener);
	}
	
	public synchronized void removeListener(ApplicationListener listener) {
		if(listeners!=null) {
			listeners.remove(listener);
			if(listeners.isEmpty()) {
				listeners = null;
			}
		}
	}

	public synchronized void close() {
		if(listeners!=null) {
			for(ApplicationListener l: listeners) {
				try {
					l.close(this);
				} catch(Exception ex) {
					Platform.getInstance().log(ex);
				}
			}
		}
	}

	public abstract ClassLoader getClassLoader();
	
	public abstract Object getApplicationContext();

	public abstract Map<String,Object> getScope();
		
	public abstract String getProperty(String name);
	
	public abstract void setProperty(String name, String value);
	
	public abstract InputStream getResourceAsStream(String name);
	
	public abstract List<Object> findServices(String serviceName);
}
