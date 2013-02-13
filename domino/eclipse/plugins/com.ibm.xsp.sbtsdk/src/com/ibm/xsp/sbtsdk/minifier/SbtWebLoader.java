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

package com.ibm.xsp.sbtsdk.minifier;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.osgi.framework.Bundle;

import com.ibm.commons.util.DoubleMap;
import com.ibm.sbt.web.SbtWebActivator;
import com.ibm.xsp.extlib.minifier.ExtLibLoaderExtension;
import com.ibm.xsp.extlib.util.ExtLibUtil;


/**
 * Resource Loader that loads the resource from sbt plug-in.
 */
public class SbtWebLoader extends ExtLibLoaderExtension {

	public SbtWebLoader() {
	}
    
    @Override
    public Bundle getOSGiBundle() {
        return SbtWebActivator.instance.getBundle();
    }
	
	
	// ========================================================
	//	Handling Dojo
	// ========================================================
    
    @Override
    public void loadDojoShortcuts(DoubleMap<String, String> aliases, DoubleMap<String, String> prefixes) {
        /// ALIASES
        if(aliases!=null) {
            //aliases.put("XEa","extlib.dijit.Accordion"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        /// PREFIXES
        if(prefixes!=null) {
            //prefixes.put("3Z0a","extlib.codemirror"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }
    
    // ========================================================
    //  Handling CSS
    // ========================================================
    
    @Override
    public void loadCSSShortcuts(DoubleMap<String, String> aliases, DoubleMap<String, String> prefixes) {
        /// ALIASES
        if(aliases!=null) {
            //aliases.put("@Ea","/.ibmxspres/.extlib/css/tagcloud.css"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        /// PREFIXES
        if(prefixes!=null) {
            //prefixes.put("3Z0a","/.ibmxspres/.extlib/codemirror"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    
    // ========================================================
    // Serving resources
    // ========================================================
    
    @Override
    public URL getResourceURL(HttpServletRequest request, String name) {
    	// Access to bootstrap css
    	if(name.startsWith("bootstrap/")) {
    		String path = "WebContent/"+name;
    		return ExtLibUtil.getResourceURL(SbtWebActivator.instance.getBundle(), path);
    	}
    	// Access to the SDK
    	if(name.startsWith("sbt/_bridge")) {
    		String path = "WebContent/js/sdk/_bridges/dojo-amd"+name.substring(11);
    		return ExtLibUtil.getResourceURL(SbtWebActivator.instance.getBundle(), path);
    	}
    	if(name.startsWith("sbt/dojo")) {
    		String path = "WebContent/js/sdk/dojo"+name.substring(8);
    		return ExtLibUtil.getResourceURL(SbtWebActivator.instance.getBundle(), path);
    	}
		String path = "WebContent/js/sdk/"+name;
		return ExtLibUtil.getResourceURL(SbtWebActivator.instance.getBundle(), path);
    }
}
