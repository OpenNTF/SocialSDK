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

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.ibm.commons.util.DoubleMap;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.web.SbtWebActivator;
import com.ibm.xsp.context.DojoLibrary;
import com.ibm.xsp.extlib.plugin.DominoPluginActivator;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.minifier.CSSResource;
import com.ibm.xsp.minifier.DojoResource;
import com.ibm.xsp.minifier.ResourceLoader;


/**
 * Resource Loader that loads the resource from core DWA.
 */
public class SbtLoader extends ResourceLoader {

    public static class SbtDojoLocaleResource extends UrlDojoLocaleResource {
        public SbtDojoLocaleResource(DojoLibrary dojoLibrary, String name, String baseUrl) {
            super(dojoLibrary, name, baseUrl);
        }
        @Override
        protected URL getResourceURL(String baseUrl, String name) throws IOException {
            String path = baseUrl+StringUtil.replace(name, '.', '/')+".js"; // $NON-NLS-1$
            URL url = ExtLibUtil.getResourceURL(DominoPluginActivator.instance.getBundle(), path);
            return url;
        }
//        // TEMP XPages bug
//        @Override
//        protected String getModulePath(String locale) {
//            String s = super.getModulePath(locale);
//            s = StringUtil.replace(s, "..", ".");
//            return s;
//        }
    }
    
    public static class SbtCSSResource extends UrlCSSResource {
        public SbtCSSResource(DojoLibrary dojoLibrary, String name, URL url) {
            super(dojoLibrary,name,url);
        }
        @Override
        protected String calculateUrlPrefix() {
            String s = super.calculateUrlPrefix();
            // If we try to access a resource through a servlet, add the prefix... 
            if(s.startsWith("/.ibmxspres/")) { // $NON-NLS-1$
                s = "/xsp"+s; // $NON-NLS-1$
            }
            return s;
        }
    }
    
    // Resources
    private HashMap<String,CSSResource> cssResources = new HashMap<String,CSSResource>();
    
    public SbtLoader() {
    }
    
    
    // ========================================================
    //  Handling Dojo
    // ========================================================
    
    @Override
    public DojoResource getDojoResource(String name, DojoLibrary dojoLibrary) {
        Map<String,DojoResource> dojoResources = (Map<String,DojoResource>)dojoLibrary.getDojoResources();
        
        DojoResource r = dojoResources.get(name);
        if(r==null) {
            synchronized(this) {
                r = dojoResources.get(name);
                if(r==null) {
                    r = loadDojoResource(name,dojoLibrary);
                    if(r!=null) {
                        dojoResources.put(name, r);
                    }
                }
            }
        }
        return r;
    }

    protected DojoResource loadDojoResource(String name, DojoLibrary dojoLibrary) {
        if(name.startsWith("sbt.")) { // $NON-NLS-1$
            String dojoName = name;
            String basePath;
            if(name.startsWith("sbt._bridge.")) {
            	dojoName = dojoName.substring(12);
            	basePath = "WebContent/js/sdk/_bridges/dojo-amd/"; // $NON-NLS-1$
            } else if(name.startsWith("sbt.dojo.")) {
            	dojoName = dojoName.substring(9);
            	basePath = "WebContent/js/sdk/dojo/"; // $NON-NLS-1$
            } else {
            	basePath = "WebContent/js/sdk/"; // $NON-NLS-1$
            }
            String path = basePath+StringUtil.replace(dojoName, '.', '/')+".js"; // $NON-NLS-1$
            URL u = ExtLibUtil.getResourceURL(SbtWebActivator.instance.getBundle(), path);
            if(u!=null) {
                return new UrlDojoResource(dojoLibrary,name,u);
            }
        }
        // Look for resources...
        if( name.startsWith("!sbt.")) { // $NON-NLS-1$
            return new SbtDojoLocaleResource(dojoLibrary,name,"WebContent/js/sdk");
        }
        return null;
    }

    @Override
    public void loadDojoShortcuts(DoubleMap<String, String> aliases, DoubleMap<String, String> prefixes) {
        super.loadDojoShortcuts(aliases, prefixes);
        
        /// ALIASES
        if(aliases!=null) {
            aliases.put("@Ya","sbt"); // $NON-NLS-2$ $NON-NLS-1$
            aliases.put("@Yb","sbt.Endpoint"); // $NON-NLS-1$ $NON-NLS-2$
            aliases.put("@Yc","sbt.Proxy"); //  $NON-NLS-2$ $NON-NLS-1$
            aliases.put("@Yd","sbt.Transport"); //  $NON-NLS-2$ $NON-NLS-1$
            aliases.put("@Ye","sbt.dom"); //  $NON-NLS-2$ $NON-NLS-1$
            aliases.put("@Yf","sbt.xpath"); //  $NON-NLS-2$ $NON-NLS-1$
            aliases.put("@Yg","sbt.xml"); //  $NON-NLS-2$ $NON-NLS-1$
            aliases.put("@Yh","sbt.json"); //  $NON-NLS-2$ $NON-NLS-1$
            aliases.put("@Yi","sbt.lang"); //  $NON-NLS-2$ $NON-NLS-1$
            aliases.put("@Yj","sbt.stringutil"); //  $NON-NLS-2$ $NON-NLS-1$
        }
        
        /// PREFIXES
        if(prefixes!=null) {
            prefixes.put("Y","sbt."); // $NON-NLS-1$ $NON-NLS-2$
            prefixes.put("2Ya","sbt._bridges"); // $NON-NLS-2$ $NON-NLS-1$
            prefixes.put("2Yb","sbt.dojo"); // $NON-NLS-1$ $NON-NLS-2$
            prefixes.put("2Yc","sbt.authentication"); // $NON-NLS-2$ $NON-NLS-1$
            prefixes.put("2Yd","sbt.base"); // $NON-NLS-1$ $NON-NLS-2$
            prefixes.put("2Ye","sbt.connections"); // $NON-NLS-2$ $NON-NLS-1$
            prefixes.put("2Yf","sbt.smartcloud"); // $NON-NLS-1$ $NON-NLS-2$
            prefixes.put("2Yg","sbt.domino"); // $NON-NLS-1$ $NON-NLS-2$
        }
    }

    
    // ========================================================
    //  Handling CSS
    // ========================================================
    
    @Override
    public CSSResource getCSSResource(String name, DojoLibrary dojoLibrary) {
        CSSResource r = cssResources.get(name);
        if(r==null) {
            synchronized(this) {
                r = cssResources.get(name);
                if(r==null) {
                    r = loadCSSResource(name,dojoLibrary);
                    if(r!=null) {
                        cssResources.put(name, r);
                    }
                }
            }
        }
        return r;
    }
    public CSSResource loadCSSResource(String name, DojoLibrary dojoLibrary) {
        if(name.startsWith("/.ibmxspres/.sbtsdk/bootstrap/")) { // $NON-NLS-1$
            String path = "WebContent/bootstrap/"+name.substring(30); // $NON-NLS-1$
            URL u = ExtLibUtil.getResourceURL(SbtWebActivator.instance.getBundle(), path);
            if(u!=null) {
                return new SbtCSSResource(dojoLibrary,name,u);
            }
        }
        return null;
    }
    
    @Override
    public void loadCSSShortcuts(DoubleMap<String, String> aliases, DoubleMap<String, String> prefixes) {
        /// ALIASES
        if(aliases!=null) {
            aliases.put("@Ya","/.ibmxspres/.sbtsdk/bootstrap/css/bootstrap.css"); //$NON-NLS-1$ //$NON-NLS-2$
            aliases.put("@Yb","/.ibmxspres/.sbtsdk/bootstrap/css/bootstrap-responsive.css"); //$NON-NLS-1$ //$NON-NLS-2$
            aliases.put("@Yc","/.ibmxspres/.sbtsdk/bootstrap/css/bootstrap.min.css"); //$NON-NLS-1$ //$NON-NLS-2$
            aliases.put("@Yd","/.ibmxspres/.sbtsdk/bootstrap/css/bootstrap-responsive.min.css"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        /// PREFIXES
        if(prefixes!=null) {
            prefixes.put("2Ya","/.ibmxspres/.sbtsdk/"); //$NON-NLS-1$ //$NON-NLS-2$
            prefixes.put("2Yb","/.ibmxspres/.sbtsdk/bootstrap/"); //$NON-NLS-1$ //$NON-NLS-2$
            prefixes.put("2Yc","/.ibmxspres/.sbtsdk/bootstrap/css/"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }
}