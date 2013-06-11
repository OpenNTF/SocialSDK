/*
 * � Copyright IBM Corp. 2012
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
import java.util.List;

import com.ibm.commons.runtime.Application;



/**
 * Playground extension.
 * 
 * @author priand
 */
public class PlaygroundExtensionFactory {

	public static final String PLAYGROUND_EXTENSION = "com.ibm.sbt.playground.extension";

	@SuppressWarnings("unchecked")
	public static List<PlaygroundExtensionFactory> getFactories() {
		Application app = Application.get();
		return (List<PlaygroundExtensionFactory>)(List)app.findServices(PLAYGROUND_EXTENSION);
	}

	public static List<Object> getExtensions(Class<?> clazz) {
		ArrayList<Object> extensions = new ArrayList<Object>();
		List<PlaygroundExtensionFactory> factories = getFactories();
		for(PlaygroundExtensionFactory f: factories) {
			extensions.add((Object)f.getExtension(clazz));
		}
		return extensions;
	}
	
	public PlaygroundExtensionFactory() {
	}
	
	public Object getExtension(Class<?> clazz) {
		return null;
	}
}
