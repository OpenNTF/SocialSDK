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


package com.ibm.commons.platform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.naming.NamingException;

import com.ibm.commons.platform.GenericWebAppServerPlatform;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.TDiag;
import com.ibm.commons.util.io.ReaderInputStream;

/**
 * Platform class for WebSphere Server.
 */
public abstract class WebAppServerPlatform extends GenericWebAppServerPlatform {

    public static final String PROP_INSTALLDIR   = "webserver.installdir"; // $NON-NLS-1$
    public static final String PROP_RESOURCESDIR = "webserver.resourcesdir"; // $NON-NLS-1$
        
    public static final String RESNAME_RESOURCESXML = "resources.xml"; // $NON-NLS-1$
    public static final String RESNAME_SQLDDLPROPS = "sqlddl.properties"; // $NON-NLS-1$
    
    public static final String ENABLE_ADVSTATEMGR = "enableAdvStateMgr"; //$NON-NLS-1$
    
    public WebAppServerPlatform() {
    }
   
    public String getProperty(String key) {
        if(PROP_INSTALLDIR.equals(key)) {
            return getInstallationDirectory().getAbsolutePath();
        }
        if(PROP_RESOURCESDIR.equals(key)) {
            return getResourcesDirectory().getAbsolutePath();
        }
        return super.getProperty(key);
    }
    
    public abstract File getInstallationDirectory();
    public abstract File getResourcesDirectory();
    
    public InputStream getGlobalResource(String resourceName) {
        // Look for a JNDI entry
        InputStream jndiStream = getGlobalResourceFromJNDI(resourceName);
        if(jndiStream!=null) {
            return jndiStream;
        }
        
        // Look for a file
        InputStream fileStream = getGlobalResourceFromFile(resourceName);
        if(fileStream!=null) {
            return fileStream;
        }
        
        // Ok, nothing found..
        return null;
    }
        
    protected InputStream getGlobalResourceFromJNDI(String resourceName) {
        String name = getResourceJNDIName(resourceName);
        if(StringUtil.isNotEmpty(name)) {
            ArrayList<String> logMsgs = new ArrayList<String>();
            

            // Get the value stored in the repository, as a String
            // PHIL: this can be a URL object!!
            String resourcesXmlVal = null;
            try {
                resourcesXmlVal = getGlobalResJNDIProperty(resourceName);
            } catch (NamingException e) {
                // log problem with JNDI initial context?
                logMsgs.add("NamingException while reading the JNDI property for global resources descriptor. Exception: "+ e.getMessage()); // $NLE-WebAppServerPlatform.NamingExceptionwhilereadingtheJND-1$
            }

            // if we got a property value, determine if it is a URL or content
            if (StringUtil.isNotEmpty(resourcesXmlVal)) {
                // check if we have a URL. If so, record the fact
                URL url = null;
                try {
                    url = new URL(resourcesXmlVal);
                    // log the fact that we picked up an URL?
                    logMsgs.add("Found a valid URL to global resources descriptor. Accessing URL now..."); // $NLI-WebAppServerPlatform.FoundavalidURLtoglobalresourcesde-1$
                    return url.openStream();
                } catch( MalformedURLException ex) {
                    return new ReaderInputStream(new StringReader(resourcesXmlVal));
                } catch (IOException e) {
                    // log that URL access failed
                    logMsgs.add("IOException while accessing the URL to global resources descriptor."); // $NLE-WebAppServerPlatform.IOExceptionwhileaccessingtheURLto-1$
                }
                
                //log here if JNDI property is non-empty, and both attempts fail
                for (String msg : logMsgs) {
                    TDiag.trace(msg);
                }
                logMsgs.clear();
            }
        }
        return null;
    }
    
    protected String getResourceJNDIName(String resourceName) {
        // PHIL: disabled for now until it is tested...
        return null;
        //return resourceName;
    }

    protected String getGlobalResJNDIProperty(String resourceName) throws NamingException {
        return null;
    }
    

    protected InputStream getGlobalResourceFromFile(String resourceName) {
        File file = getGlobalResourceFile(resourceName);
        if(file!=null) {
            try {
                return new FileInputStream(file);
            } 
            catch(FileNotFoundException ex) {
            }
        }
        return null;
    }

    public File getGlobalResourceFile(String resourceName) {
        File file = new File(getResourcesDirectory(),resourceName);
        return file;
    }
    
    public boolean isFeatureEnabled (String featureId){
    	if(StringUtil.equals(featureId, ENABLE_ADVSTATEMGR)) {
    		return true;
    	}
    	return super.isFeatureEnabled(featureId);
    }
    
}
