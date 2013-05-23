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
package com.ibm.xsp.sbtsdk.servlets;

/**
 * JavaScript libraries for the SDK.
 *
 * @author priand
 */
public class JavaScriptLibraries {
	
	public enum LibType {
		DOJO(DominoDojoLibrary.NAME),
		JQUERY(DominoJQueryLibrary.NAME);
		
		private final String type;
		LibType(String type) {
			this.type = type;
		}
		public String toString() {
			return type;
		}
	}
	public static class JSLibrary {
		private String label;
		private LibType libType;
		private String libVersion;
		private String libUrl;
		private boolean async;
		public JSLibrary(String label, LibType libType, String libVersion, String libUrl, boolean async) {
			this.label = label;
			this.libType = libType;
			this.libVersion = libVersion;
			this.libUrl = libUrl;
			this.async = async;
		}
		public String getLabel() {
			return label;
		}
		public LibType getLibType() {
			return libType;
		}
		public String getLibVersion() {
			return libVersion;
		}
		public String getLibUrl() {
			return libUrl;
		}
		public boolean isAsync() {
			return async;
		}
	}
	// See: https://developers.google.com/speed/libraries/devguide
	public static JSLibrary[] LIBRARIES = {
		new JSLibrary("<default>",LibType.DOJO,"1.8.1","",false),
		//new JSLibrary("Dojo Toolkit 1.4.3","dojo","1.4.3","//ajax.googleapis.com/ajax/libs/dojo/1.4.3/dojo/dojo.js"),
		new JSLibrary("Dojo Toolkit 1.4.3",LibType.DOJO,"1.4.3","//ajax.googleapis.com/ajax/libs/dojo/1.4.3/",false),
		new JSLibrary("Dojo Toolkit 1.5.2",LibType.DOJO,"1.5.2","//ajax.googleapis.com/ajax/libs/dojo/1.5.2",false),
		new JSLibrary("Dojo Toolkit 1.6.1",LibType.DOJO,"1.6.1","//ajax.googleapis.com/ajax/libs/dojo/1.6.1",false),
		new JSLibrary("Dojo Toolkit 1.7.4",LibType.DOJO,"1.7.4","//ajax.googleapis.com/ajax/libs/dojo/1.7.4",false),
		new JSLibrary("Dojo Toolkit 1.8.4",LibType.DOJO,"1.8.4","//ajax.googleapis.com/ajax/libs/dojo/1.8.4",false),
		new JSLibrary("Dojo Toolkit 1.8.4 - Async",LibType.DOJO,"1.8.4","//ajax.googleapis.com/ajax/libs/dojo/1.8.4",true),
		new JSLibrary("Dojo Toolkit 1.9.0",LibType.DOJO,"1.9.0","//ajax.googleapis.com/ajax/libs/dojo/1.9.0",false),
		new JSLibrary("Dojo Toolkit 1.9.0 - Async",LibType.DOJO,"1.9.0","//ajax.googleapis.com/ajax/libs/dojo/1.9.0",true),
//		//new JSLibrary("JQuery 1.8.3","jquery","1.8.3","//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"),
		new JSLibrary("JQuery 1.8.3",LibType.JQUERY,"1.8.3","//ajax.googleapis.com/ajax/libs/jquery/1.8.3",false),
	};


}
