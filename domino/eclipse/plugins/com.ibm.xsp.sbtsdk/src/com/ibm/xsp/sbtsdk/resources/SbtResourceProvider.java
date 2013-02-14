/*
 * © Copyright IBM Corp. 2010
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

package com.ibm.xsp.sbtsdk.resources;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import com.ibm.sbt.web.SbtWebActivator;
import com.ibm.xsp.extlib.plugin.DominoPluginActivator;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.webapp.FacesResourceServlet;
import com.ibm.xsp.webapp.resources.BundleResourceProvider;

/**
 * SBT resource provider.
 * 
 * @author priand
 */
public class SbtResourceProvider extends BundleResourceProvider {

    public static final String SBT_PREFIX = ".sbtsdk"; // $NON-NLS-1$
    
    // Resource Path
    public static final String RESOURCE_PATH =    FacesResourceServlet.RESOURCE_PREFIX  // "/.ibmxspres/" 
                                                + SBT_PREFIX;      						// ".sbtsdk" 

    public SbtResourceProvider() {
        super(DominoPluginActivator.instance.getBundle(),SBT_PREFIX);
    }

    @Override
    protected URL getResourceURL(HttpServletRequest request, String name) {
    	// Access to bootstrap css
    	if(name.startsWith("bootstrap/")) {
    		String path = "WebContent/"+name;
    		return ExtLibUtil.getResourceURL(SbtWebActivator.instance.getBundle(), path);
    	}
    	// Access to the SDK
    	if(name.startsWith("sbt/_bridge/")) {
    	    if(ExtLibUtil.isXPages853()) {
                String path = "WebContent/js/sdk/_bridges/dojo/"+name.substring(12);
                return ExtLibUtil.getResourceURL(SbtWebActivator.instance.getBundle(), path);
    	    } else {
    	        String path = "WebContent/js/sdk/_bridges/dojo-amd/"+name.substring(12);
    	        return ExtLibUtil.getResourceURL(SbtWebActivator.instance.getBundle(), path);
    	    }
    	}
    	if(name.startsWith("sbt/dojo/")) {
    		String path = "WebContent/js/sdk/dojo/"+name.substring(9);
    		return ExtLibUtil.getResourceURL(SbtWebActivator.instance.getBundle(), path);
    	}
    	// Just regular sbt files
		String path = "WebContent/js/sdk/"+name;
		return ExtLibUtil.getResourceURL(SbtWebActivator.instance.getBundle(), path);
    }

}