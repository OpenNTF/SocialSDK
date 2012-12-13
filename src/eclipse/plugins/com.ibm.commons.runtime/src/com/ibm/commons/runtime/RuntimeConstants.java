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

import javax.servlet.http.HttpServletRequest;

import com.ibm.commons.Platform;
import com.ibm.commons.runtime.util.UrlUtil;



/**
 * Runtime constant.
 *
 * This class encapsulates the constants that are actually being used by the runtime platform.
 * 
 * @author Philippe Riand
 */
public abstract class RuntimeConstants {
	
	public static final int SERVICE_BASEURL		= 10;
	public static final int LIBRARY_BASEURL		= 11;

	private static RuntimeConstants instance;
	static {
		Platform p = Platform.getInstance();
		if(p.isPlatform("Domino")) {
			instance = new DominoConstants();
		} else if(p.isPlatform("Notes")) {
			instance = new NotesConstants();
		} else {
			instance = new J2EEConstants();
		}
	}

	public static RuntimeConstants get() {
		return instance;
	}
	public static void set(RuntimeConstants c) {
		instance = c;
	}
	
	protected RuntimeConstants() {
	}
	
	public String getConstant(int constant) {
		switch(constant) {
			case SERVICE_BASEURL:			return "/service";
			case LIBRARY_BASEURL:		return "/library";
		}
		return null;
	}
	
	public static class J2EEConstants extends RuntimeConstants {
		@Override
		public String getConstant(int constant) {
			return super.getConstant(constant);
		}
	}
	
	public static class AbstractNotesDominoConstants extends RuntimeConstants {
		
		public static final String SERVICE_PATHINFO 	= "/.sbtservice"; 
		public static final String LIBRARY_PATHINFO 	= "/.sbtlibrary"; 
		
		@Override
		public String getConstant(int constant) {
			switch(constant) {
				case SERVICE_BASEURL:		return "/xsp"+SERVICE_PATHINFO;
				case LIBRARY_BASEURL:		return "/xsp"+LIBRARY_PATHINFO;
			}
			return super.getConstant(constant);
		}
	}
	public static class DominoConstants extends AbstractNotesDominoConstants {
	}
	public static class NotesConstants extends AbstractNotesDominoConstants {
	}
	

    
    //
    // Utilities
    //
    /**
     * Get the base URL for the proxy.
     * 
     * @param context
     * @return
     */
    public String getBaseProxyUrl(Context context) {
    	StringBuilder b = new StringBuilder();
    	appendBaseProxyUrl(b, context);
    	return b.toString();
    }
    public StringBuilder appendBaseProxyUrl(StringBuilder b, Context context) {
        HttpServletRequest request = (HttpServletRequest)context.getHttpRequest();
        UrlUtil.appendBaseUrl(b, request);
        b.append(getConstant(RuntimeConstants.SERVICE_BASEURL));
        return b;
    }	
    public String getBaseProxyUrl(HttpServletRequest request) {
    	StringBuilder b = new StringBuilder();
    	appendBaseProxyUrl(b, request);
    	return b.toString();
    }
    public StringBuilder appendBaseProxyUrl(StringBuilder b, HttpServletRequest request) {
        UrlUtil.appendBaseUrl(b, request);
        b.append(getConstant(RuntimeConstants.SERVICE_BASEURL));
        return b;
    }	
}
