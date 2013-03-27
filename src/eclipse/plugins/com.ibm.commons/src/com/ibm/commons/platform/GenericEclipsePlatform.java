/*
 * © Copyright IBM Corp. 2012-2013
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

package com.ibm.commons.platform;

import java.io.InputStream;
import java.io.PrintStream;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;

import com.ibm.commons.IPlatformService;
import com.ibm.commons.Platform;
import com.ibm.commons.log.LogMgrFactory;
import com.ibm.commons.log.eclipse.EclipseLogMgrFactory;


/**
 * Base implementation for a Eclipse based platform.
 * @ibm-not-published
 */
public class GenericEclipsePlatform extends Platform {

    public static final String ECLIPSE_PLATFORM = "Eclipse"; // $NON-NLS-1$
    
    public GenericEclipsePlatform() {
    }
    protected void initialize() {
    }

    public String getName() {
        return "Generic Eclipse"; // $NLS-GenericEclipsePlatform.GenericEclipse-1$
    }

    public boolean isPlatform(String name) {
        if(ECLIPSE_PLATFORM.equals(name)) {
            return true;
        }
        return super.isPlatform(name);
    }
    
    public PrintStream getOutputStream() {
        return java.lang.System.out;
    }
    
    public PrintStream getErrorStream() {
        return java.lang.System.err;
    }
    
    protected LogMgrFactory createLogMgrFactory() {
        return new EclipseLogMgrFactory();
    }
    
    public final boolean isEclipseBased() {
        return true;
    }

    public InputStream getGlobalResource(String resourceName) {
        InputStream is = findGlobalResource(resourceName);
        if(is!=null) {
            return is;
        }
        return super.getGlobalResource(resourceName);
    }
    
    private InputStream findGlobalResource(String resourceName) {
        try {
            // Look if someone implemented an extension point to retrive the platform
            IExtensionRegistry reg = org.eclipse.core.runtime.Platform.getExtensionRegistry();
            if (reg != null) {
                IConfigurationElement[] elt = reg.getConfigurationElementsFor("com.ibm.commons.GlobalResourceFactory"); // $NON-NLS-1$
                for( int i=0; i<elt.length; i++ ) {
                    if( "globalResourceFactory".equalsIgnoreCase(elt[i].getName()) ) { // $NON-NLS-1$
                        try {
                            Object o = elt[i].createExecutableExtension("class"); // $NON-NLS-1$
                            IGlobalResourceProvider factory = (IGlobalResourceProvider)o;
                            InputStream is = factory.getGlobalResource(resourceName);
                            if ( is != null ){
                            	return is;
                            }
                        } catch(Throwable ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        } catch( Throwable t ) {
            t.printStackTrace();
        }
        
        // Not found
        return null;
    }

    
    public IPlatformService getPlatformService( String serviceId ){
        IPlatformService srv = super.getPlatformService(serviceId);
        if(srv==null) {
        	// Look for a provider
        	srv = findService(serviceId);
        }
        return srv;
    }

    private IPlatformService findService(String serviceId) {
        try {
            // Look if someone implemented an extension point to retrive the platform
            IExtensionRegistry reg = org.eclipse.core.runtime.Platform.getExtensionRegistry();
            if (reg != null) {
                IConfigurationElement[] elt = reg.getConfigurationElementsFor("com.ibm.commons.ServiceFactory"); // $NON-NLS-1$
                for( int i=0; i<elt.length; i++ ) {
                    if( "serviceFactory".equalsIgnoreCase(elt[i].getName()) ) { // $NON-NLS-1$
                        try {
                            Object o = elt[i].createExecutableExtension("class"); // $NON-NLS-1$
                            IServiceFactory factory = (IServiceFactory)o;
                            IPlatformService srv = factory.getPlatformService(serviceId);
                            if ( srv != null ){
                            	return srv;
                            }
                        } catch(Throwable ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        } catch( Throwable t ) {
            t.printStackTrace();
        }
        
        // Not found
        return null;
    }
    
    public boolean isFeatureEnabled (String featureId){
        return false;
    }
}
