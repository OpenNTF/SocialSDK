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

import com.ibm.commons.runtime.impl.servlet.RuntimeFactoryServlet;

/**
 * Runtime factory. A runtime factory is provided to create the runtime artifacts required by the toolkit.
 * 
 * @author Philippe Riand
 */
public abstract class RuntimeFactory {

	private static RuntimeFactory	instance	= new RuntimeFactoryServlet();

	public static RuntimeFactory get() {
		return instance;
	}

	public static void set(RuntimeFactory runtimeAccessor) {
		instance = runtimeAccessor;
	}

	public RuntimeFactory() {
	}

	public abstract Application getApplicationUnchecked();

	public abstract Application initApplication(Object initContext);

	public abstract void destroyApplication(Application application);

	public abstract Application createApplication(Object context);

	public abstract Context getContextUnchecked();

	public abstract Context initContext(Application application, Object request, Object response);

	public abstract void destroyContext(Context context);

	public abstract Context createContext(Application application, Object request, Object response);
}
