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

import java.util.HashMap;
import java.util.Map;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.RuntimeFactory;



/**
 * Abstract Runtime factory for J2EE servers.
 * 
 * Implementation of a runtime factory to be used in a regular J2EE application. 
 * 
 * @author Philippe Riand
 */
public abstract class AbstractRuntimeFactory extends RuntimeFactory {

	private Map<ClassLoader,AbstractApplication> applications = new HashMap<ClassLoader, AbstractApplication>();
	private ThreadLocal<AbstractContext> contexts = new ThreadLocal<AbstractContext>();
	
	
	//
	//
	// Application
	//
	//
	
	public Map<ClassLoader,AbstractApplication> getApplicationMap() {
		return applications;
	}

	@Override
	public Application getApplicationUnchecked() {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		return applications.get(cl);
	}

	@Override
	public synchronized Application initApplication(Object servletContext) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		AbstractApplication app = applications.get(cl);
		if(app==null) {
			RuntimeFactory rtFactory = RuntimeFactory.get();
			app = (AbstractApplication)rtFactory.createApplication(servletContext);
			applications.put(cl,app);
		}
		app._incReferences();
		return app;
	}

	@Override
	public synchronized void destroyApplication(Application application) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		AbstractApplication app = applications.get(cl);
		if(app==null || app!=application) { // should not happen
			throw new IllegalStateException("Cannot destroy an application that doesn't exist");
		}
		app._decReferences();
		if(app._references()==0) {
			app.close();
			applications.remove(cl);
		}
	}	
	
	
	//
	//
	// Context
	//
	//
	
	public ThreadLocal<AbstractContext> getContextThreadLocal() {
		return contexts;
	}

	@Override
	public Context getContextUnchecked() {
		return contexts.get();
	}
	@Override
	public Context initContext(Application application, Object request, Object response) {
		AbstractContext ctx = contexts.get();
		if(ctx==null) {
			RuntimeFactory rtFactory = RuntimeFactory.get();
			ctx = (AbstractContext)rtFactory.createContext(application, request, response);
			contexts.set(ctx);
		}
		ctx._incReferences();
		return ctx;
	}
	@Override
	public Context initContext(Context context) {
		AbstractContext ctx = contexts.get();
		if(ctx!=null) {
			throw new IllegalStateException("Context is already initialized for the thread");
		}
		ctx = (AbstractContext)context;
		contexts.set(ctx);
		ctx._incReferences();
		return ctx;
	}
	@Override
	public void destroyContext(Context context) {
		AbstractContext ctx = contexts.get();
		if(ctx==null || ctx!=context) { // should not happen
			throw new IllegalStateException("Cannot destroy a context that doesn't exist");
		}
		ctx._decReferences();
		if(ctx._references()==0) {
			ctx.close();
			contexts.remove();
		}
	}

}
