/*
 *  Copyright IBM Corp. 2012
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

import javax.faces.context.FacesContext;

/**
 * JavaScript libraries for the SDK.
 *
 * @author priand
 */
public class JavaScriptLibraries {
	
	public static JSLibrary getCurrentJavaScriptLibrary() {
		return getCurrentJavaScriptLibrary(null);
	}
	public static JSLibrary getCurrentJavaScriptLibrary(String libName) {
		JSLibrary[] libs = JavaScriptLibraries.LIBRARIES;
		FacesContext ctx = FacesContext.getCurrentInstance();
		if(ctx!=null) {
			String lib = (String)ctx.getExternalContext().getSessionMap().get("javaScriptLibrary");
			if(lib!=null) {
				for(int i=0; i<libs.length; i++) {
					if(libs[i].getLabel().equals(lib)) {
						return libs[i];
					}
				}
			}
		}
		return libs[0];
	}

	public static String getCurrentJavaScriptLibraryName() {
		return getCurrentJavaScriptLibraryName(null);
	}
	public static String getCurrentJavaScriptLibraryName(String libName) {
		return getCurrentJavaScriptLibrary(libName).getLabel();
	}

	
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
		private String tag;
		private LibType libType;
		private String libVersion;
		private String libUrl;
		private boolean async;
		public JSLibrary(String label, String tag, LibType libType, String libVersion, String libUrl, boolean async) {
			this.label = label;
			this.tag = tag;
			this.libType = libType;
			this.libVersion = libVersion;
			this.libUrl = libUrl;
			this.async = async;
		}
		public String getLabel() {
			return label;
		}
		public String getTag() {
			return tag;
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
		new JSLibrary("<default>","dojo181",LibType.DOJO,"1.8.1","",false),
		//new JSLibrary("Dojo Toolkit 1.4.3","dojo","1.4.3","//ajax.googleapis.com/ajax/libs/dojo/1.4.3/dojo/dojo.js"),
		new JSLibrary("Dojo Toolkit 1.4.3","dojo143",LibType.DOJO,"1.4.3","//ajax.googleapis.com/ajax/libs/dojo/1.4.3/",false),
		new JSLibrary("Dojo Toolkit 1.5.2","dojo152",LibType.DOJO,"1.5.2","//ajax.googleapis.com/ajax/libs/dojo/1.5.2",false),
		new JSLibrary("Dojo Toolkit 1.6.1","dojo161",LibType.DOJO,"1.6.1","//ajax.googleapis.com/ajax/libs/dojo/1.6.1",false),
		new JSLibrary("Dojo Toolkit 1.7.4","dojo174",LibType.DOJO,"1.7.4","//ajax.googleapis.com/ajax/libs/dojo/1.7.4",false),
		new JSLibrary("Dojo Toolkit 1.8.4","dojo184",LibType.DOJO,"1.8.4","//ajax.googleapis.com/ajax/libs/dojo/1.8.4",false),
		new JSLibrary("Dojo Toolkit 1.8.4 - Async","dojo184",LibType.DOJO,"1.8.4","//ajax.googleapis.com/ajax/libs/dojo/1.8.4",true),
		new JSLibrary("Dojo Toolkit 1.9.0","dojo190",LibType.DOJO,"1.9.0","//ajax.googleapis.com/ajax/libs/dojo/1.9.0",false),
		new JSLibrary("Dojo Toolkit 1.9.0 - Async","dojo190",LibType.DOJO,"1.9.0","//ajax.googleapis.com/ajax/libs/dojo/1.9.0",true),
//		//new JSLibrary("JQuery 1.8.3","jquery","1.8.3","//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"),
		new JSLibrary("JQuery 1.8.3","jquery183",LibType.JQUERY,"1.8.3","//ajax.googleapis.com/ajax/libs/jquery/1.8.3",false),
	};
}
