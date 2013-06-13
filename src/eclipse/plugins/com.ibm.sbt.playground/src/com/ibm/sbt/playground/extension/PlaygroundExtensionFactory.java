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
package com.ibm.sbt.playground.extension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ibm.commons.extension.ExtensionManager;
import com.ibm.commons.runtime.Application;



/**
 * Playground extension.
 * 
 * @author priand
 */
public abstract class PlaygroundExtensionFactory {

	public static final String PLAYGROUND_EXTENSION = "com.ibm.sbt.playground.extension";

	private static List<PlaygroundExtensionFactory> factories;
	static {
		factories = ExtensionManager.findServices(null,
    			PlaygroundExtensionFactory.class.getClassLoader(),
        		PlaygroundExtensionFactory.PLAYGROUND_EXTENSION, 
        		PlaygroundExtensionFactory.class);
	}
	
	@SuppressWarnings("unchecked")
	public static List<PlaygroundExtensionFactory> getFactories() {
		// We cannot use the application object here because of some ClassLoader issues and security
		// We only load plug-in based extension in the static initializer
		//Application app = Application.get();
		//return (List<PlaygroundExtensionFactory>)(List)app.findServices(PLAYGROUND_EXTENSION);
		return factories;
	}

	@SuppressWarnings("unchecked")
	public static<T> List<T> getExtensions(Class<T> clazz) {
		ArrayList<T> extensions = new ArrayList<T>();
		if(factories!=null) {
			for(PlaygroundExtensionFactory f: factories) {
				T ext = (T)f.getExtension(clazz);
				if(ext!=null) {
					extensions.add(ext);
				}
			}
			return extensions;
		}
		return Collections.emptyList();
	}
	
	public PlaygroundExtensionFactory() {
	}
	
	public Object getExtension(Class<?> clazz) {
		return null;
	}
}
