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


package com.ibm.commons;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.ibm.commons.extension.ExtensionManager;
import com.ibm.commons.log.LogMgrFactory;
import com.ibm.commons.platform.GenericEclipsePlatform;
import com.ibm.commons.platform.GenericPlatform;
import com.ibm.commons.platform.GenericWebAppServerPlatform;
import com.ibm.commons.platform.IPlatformFactory;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.StreamUtil;


/**
 * Access to platform specific services.
 * <p>
 * There is one platform object (singleton) for the JVM. It encapsulate the services for the
 * underlying platform.
 * This platform object is automatically instantiated when necessary.
 * </p>
 * @ibm-api
 */
public abstract class Platform {
    
    private static Platform instance;  //singleton
//    static {
//        instance = createPlatform();
//    	instance.initialize();
//    	//System.out.println("Platform: '" + instance.getName() + "'"); // $NON-NLS-1$
//    }
    
    /**
     * Return the platform singleton. 
     * @return the Platform singleton
     * @ibm-api
     */
    public static Platform getInstance() {
    	  if(instance == null)  {
              synchronized (Platform.class) {
                  if(instance == null)  {
                      instance = createPlatform();
          	    	instance.initialize();
                      //System.out.println("Platform: '" + instance.getName() + "'"); // $NON-NLS-1$
                  }
              }
          }
        return instance;
    }
    
    // --------------------------------------------------------------------
    public static final String FACTORY_CLASS   = "com.ibm.commons.platform.Factory"; // $NON-NLS-1$
    
    // Server side platform classes
    public static final String PORTAL6_PLATFORM_CLASS = "com.ibm.xsp.portlet.platform.PortalPlatform"; // $NON-NLS-1$
//    public static final String TOMCAT_PLATFORM_CLASS  = "com.ibm.xfaces.platform.TomcatPlatform"; // $NON-NLS-1$
//    public static final String WASCE_PLATFORM_CLASS   = "com.ibm.xfaces.platform.WasCEPlatform"; // $NON-NLS-1$

    // Entry if needed is the System properties
    public static final String PLATFORM_PROPERTY_KEY = "com.ibm.commons.platform"; // $NON-NLS-1$
    
    // Properties file storing meta info
    private static final String PLATFORM_PROPERTIES = "META-INF/com.ibm.commons.Platform.properties"; // $NON-NLS-1$

    // In order to force the platform in some specific cases
    // Must be called before the first access to the platform
    public static void initPlatform(Platform platform) {
        synchronized (Platform.class) {
	    	if(instance!=null) {
	    		throw new IllegalStateException("Platform Object has already been set"); // $NON-NLS-1$
	    	}
	    	instance = platform;
	    	instance.initialize();
            //System.out.println("Platform forced to: '" + instance.getName() + "'"); // $NLS-Platform.Platform-1$
        }
    }
    
    private static Platform createPlatform() {
        // 1- Use the property to find the Platform
        String prop = System.getProperty(PLATFORM_PROPERTY_KEY);
        if(StringUtil.isNotEmpty(prop)) {
            Platform platform = loadPlatform(prop);
            if(platform!=null) {
                return platform;
            }
        }
        
        // 2- Use an extension point
        try {
            List<Object> l = ExtensionManager.findServices(null, Platform.class, FACTORY_CLASS);
            if(l.size()>0) {
                // Only get the first one...
    	        try {
    	            IPlatformFactory factory = (IPlatformFactory)l.get(0);
    	            return factory.createPlatform();
    	        } catch(Throwable e) {
    	            e.printStackTrace();
    	        }
            }
        } catch(Throwable e) {
            // Not found...
        }
        // 2+- Try to find a factory with fixed name
        // This is left temporally as it won't be in use when the Domino server will be restructured 
        try {
            Class<?> factoryClass = Class.forName(FACTORY_CLASS); // $NON-NLS-1$
            try {
                IPlatformFactory factory = (IPlatformFactory)factoryClass.newInstance();
                return factory.createPlatform();
            } catch(Throwable e) {
                e.printStackTrace();
            }
        } catch(Throwable e) {
            // Not found...
        }
        
        
        // 3- Try to discover it
        Platform platform = findPlatform();
        if(platform!=null) {
            return platform;
        }

        // 3- Default to a generic one
        return new GenericPlatform();
    }

    private LogMgrFactory logMgrFactory;
    private HashMap<String,Object> _objects;
    private HashMap<String,IPlatformService> _services;


    /**
     * 
     */
    protected Platform() {
        instance = this; // Ensure this is defined as the current one
        this._objects = new HashMap<String, Object>();
        this._services = new HashMap<String, IPlatformService>();
        logMgrFactory = createLogMgrFactory();
    }
    protected void initialize() {
    }
    
//    protected void initLog() {
//		BasicConsoleHandler handler = new BasicConsoleHandler();
//		handler.setLevel(Level.INFO);
//		Logger.getLogger("").addHandler(handler);
//    }
    
    /**
     * Return the name of the platform.
     * @return the platform name
     * @ibm-api
     */
    public abstract String getName();
    
    /**
     * Get the Log manager factory used for this platform.
     * @return
     * @ibm-api
     */
    public final LogMgrFactory getLogMgrFactory() {
        return logMgrFactory;
    }
    protected abstract LogMgrFactory createLogMgrFactory();
    
    /**
     * Get the output stream for this platform.
     * @return
     * @ibm-api
     */
    public abstract PrintStream getOutputStream();
    
    /**
     * Get the error stream for this platform.
     * @return
     * @ibm-api
     */
    public abstract PrintStream getErrorStream();
    
    /**
     * Check if the runtime platform is Eclipse based.
     * @return true if the host is Eclipse
     * @ibm-api
     */
    public abstract boolean isEclipseBased();

    /**
     * Check if it is a particular platform.
     * @return true if the platform matches the name
     * @ibm-api
     */
    public boolean isPlatform(String name) {
        return false;
    }
    
    /**
     * Return a sevice implementation giving its name.
     * @param serviceId : unique id representing the platform service
     * @return the service implementation
     * @ibm-not-published
     */
    public IPlatformService getPlatformService( String serviceId ){
        return (IPlatformService)_services.get(serviceId);
    }
    
    /**
     * Register a platform service.
     * @param serviceId unique id 
     * @param platformService
     * @return
     * @ibm-not-published
     */
    public void registerPlatformService( String serviceId, IPlatformService platformService ){
        _services.put( serviceId, platformService );
    }
    
    /**
     * Get a platform property.
     * @param key the property name
     * @return the property value
     * @ibm-api
     */
    public String getProperty(String key) {
        String s = (String)_objects.get(key);
        if(s!=null) {
        	return s;
        }
//        try {
//        	return System.getProperty(key);
//        } catch(Throwable t) {
//        }
        return null;
    }
    
    /**
     * Set a platform property.
     * @param key the property name
     * @param object the new property value
     * @ibm-api
     */
    public void putProperty(String key, String object) {
        _objects.put(key,object);
    }
    
    /**
     * Remove a platform property.
     * @param key the property name
     * @ibm-api
     */
    public void removeProperty(String key) {
        _objects.remove(key);
    }
    
    /**
     * Get a global resource file as an input stream.
     * @return the input stream, or null if the resource is not available
     * @ibm-api
     */
    public InputStream getGlobalResource(String resourceName) {
        return null;
    }
    
    /**
     * Get a global resource as a file.
     * This returns a file object when the resource is an actual file. This allows
     * an application to get the lastModificationDate as well as the file length
     * when needed. If the resource is not backed by a file, it return null.
     * @param resourceName
     * @return the corresponding file, if exists
     * @ibm-api
     */
    public File getGlobalResourceFile(String resourceName) {
        return null;
    }

    /**
     * Get an object cached by the platform.
     * @return
     * @ibm-api
     */
    public Object getObject(String key) {
    	return null;
    }

    
    // ===================================================================================
    // Platform logger
    // This is used to log exceptions in the xpages trace file
    // ===================================================================================

    /**
     * Log an exception.
     * <p>
     * Note that the log not only goes to default output stream, but it can also
     * go a to log file if implemented by the platform.
     * </p>
     * @ibm-api
     */
    public void log(Throwable ex) {
    	ex.printStackTrace();
    }

    /**
     * Log a message.
     * <p>
     * Note that the log not only goes to default output stream, but it can also
     * go a to log file if implemented by the platform.
     * </p>
     * @ibm-api
     */
    public void log(String message) {
    	System.out.println(message);
    }


    /**
     * Log a message after formatting it.
     * <p>
     * Note that the log not only goes to default output stream, but it can also
     * go a to log file if implemented by the platform.
     * </p>
     * @ibm-api
     */
    public void log(String message, Object... parameters) {
    	System.out.println(StringUtil.format(message, parameters));
    }
    
    
    // ===================================================================================
    // Platform finder.
    //
    // This code is finding the current running platform, whenever it is Eclipse based
    // or a web app server.
    // ===================================================================================
    
    private static Platform findPlatform() {
        // PHI:
        // We look for the app server first as Portal may already contains the eclipse
        // extension point classes. On another hand, com.ibm.commons doesn't have the
        // J2EE classes in its class path when running inside Eclipse, so it is safe.
        // But we want to detect it BEFORE the Eclipse platform.
        
        //-----------------------------------------------------------------------------------
        // 1- Look if we are in a well know application server
        // We do that by looking at either global properties or available classes
        if( tryClass("javax.servlet.Servlet") ) { // $NON-NLS-1$
            // 1. Check for a properties file META-INF
            // This allows third parties to create their own platform here
            Platform p = loadPlatformFromPropertiesFile();
            if(p!=null) {
                return p;
            }
            
            // 1. Check for a portal environment
            if( tryClass("javax.portlet.Portlet") ) { // $NON-NLS-1$
                // Check for Portal 6
                if( tryClass("com.ibm.wps.datastore.domains.DatabaseDomain") ) { // $NON-NLS-1$
                    return loadPlatform(PORTAL6_PLATFORM_CLASS);
                }   
            }

            // 2. Check WebSphere
            // TODO...
            
//            // 3. Check TOMCAT
//            String catalinaHome = System.getProperty("catalina.home"); // $NON-NLS-1$
//            if( StringUtil.isNotEmpty(catalinaHome) ) {
//                return loadPlatform(TOMCAT_PLATFORM_CLASS);
//            }
//            
//            // 4. Check WAS-CE
//            // TODO...
            
            // Generic WebAppServer
            return new GenericWebAppServerPlatform();
        }

        //-----------------------------------------------------------------------------------
        // 2- look if we are executing in an Eclipse environment
        // If so, use an extension point to get the actual registered platform
        if( tryClass("org.eclipse.core.runtime.Platform") ) { // $NON-NLS-1$
            // We do a dynamic class loading to ensure that there is no hard-links to the class
            try {
                Class<?> c = Class.forName("com.ibm.commons.platform.EclipsePlatformFactory"); // $NON-NLS-1$
                IPlatformFactory factory = (IPlatformFactory)c.newInstance();
                return factory.createPlatform();
            } catch(Throwable e) {
                e.printStackTrace();
            }
            
            // Generic Eclipse
            return new GenericEclipsePlatform();
        }
        
        // no luck!
        // The platform is unknown
        return null;
    }

    // Load a platform from a properties file
    private static Platform loadPlatformFromPropertiesFile() {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if(loader==null) {
                loader = Platform.class.getClassLoader();
            }
            InputStream is = null;
            try {
                is = loader.getResourceAsStream(PLATFORM_PROPERTIES);
                if(is!=null) {
                    Properties props = new Properties();
                    props.load(is);
                    String className = props.getProperty(PLATFORM_PROPERTY_KEY);
                    if(StringUtil.isEmpty(className)) {
                        return loadPlatform(className);
                    }
                }
            } finally {
                StreamUtil.close(is);
            }
        } catch(Throwable ex) {}
        return null;
    }
    
    // Check if a class exists in the class path
    private static boolean tryClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (Throwable e) {}
        return false;
    }

    // Load a Platform object from the class path
    private static Platform loadPlatform(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            return (Platform)clazz.newInstance();
        } catch (Throwable e) {}
        return null;
    }
    
    public abstract boolean isFeatureEnabled (String featureId);
}