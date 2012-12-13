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


package com.ibm.commons.extension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;

import com.ibm.commons.log.CommonsLogger;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.TDiag;
import com.ibm.commons.util.io.StreamUtil;


/**
 * Extension Manager.
 * 
 * <p>This class is used to manage the extension in an independant manner. It can
 * use services like extension or Eclipse extension points, depending on the 
 * running platform.</p>
 * 
 * <p>As the runtime can be shared by multiple J2EE applications, some extensions
 * can be defined globally (e.g. for all the applications) or on a per application
 * basis.</p>
 * 
 * @ibm-api
 */
public class ExtensionManager {
	
	/**
	 * Interface that defines an application classloader.
	 * An ApplicationClassLoader is used to load application specific resources.
	 * 
	 * Not intended to be subclassed
	 * @ibm-not-published
	 */
	public interface ApplicationClassLoader {
		public Enumeration<URL> findApplicationResources(String resName) throws IOException;
	}

    private static Provider provider;
    static {
        try {
            // We should detect if we are running in an eclipse environment
            // We cannot use the platform object for this, as the platform object is using the
            // extension manager itself to get the actual class to load.
            // => Platform.getInstance().isEclipseBased() doesn't work properly
        	// Moreover, in case of the Domino server, the OSGI integration sets the OSGi flgas
        	// while we should use the Java services manager. for this reason, we have a flag
        	// to set OSGi properly.
            try {
                String osgi = System.getProperty("osgi.framework.version"); // $NON-NLS-1$
                if(StringUtil.isNotEmpty(osgi)) {
                	try {
                		String prop = System.getProperty("com.ibm.common.ExtensionManager"); //$NON-NLS-1$
                		if(StringUtil.isEmpty(prop) || StringUtil.equals(prop, "osgi")) { //$NON-NLS-1$
                			provider = new EclipseProvider();
                		}
                	} catch(Throwable t) {}
                }
            } catch (Throwable e) {}
            if(provider==null) {
                provider = new JavaServiceProvider();
            }
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    
    /**
     * Find the available implementations of a service for a particular Application.
     * 
     * <p>This includes both the globally available services as well as the one defined in the
     * application class loader as well. The global services are generally read from the
     * Eclipse extension points while the per application are read using the services
     * definition (until OSGi will be available everywhere....)</p>
     * <p>The result is a list of service implementations (concrete classes)</p>
     * @param loader the class loader used to load the services
     * @param serviceType the name of the service to load
     * @return a List of services implementation
     * @ibm-api
     */
    public static List<Object> findApplicationServices(ClassLoader loader, String serviceType) {
        return findApplicationServices(null,loader,serviceType);
    }
    
    /**
     * Find the available implementations of a service for a particular Application.
     * 
     * <p>Similar to com.ibm.commons.extension.ExtensionManager.findApplicationServices(ClassLoader, String)
     * but it also manages a cache which prevents the resources to be read if not
     * necessary.</p>
     * 
     * @param services the Map that holds the cache. Must be one instance per Application
     * @param loader the class loader used to load the services
     * @param serviceType the name of the service to load
     * @return a List of services implementation
     * @ibm-api
     */
    public static List<Object> findApplicationServices(Map<String, List<Object>> services, ClassLoader loader, String serviceType) {
        if(services!=null) {
            List<Object> l = services.get(serviceType);
            if(l!=null) {
                return l;
            }
        }
        List<Object> l = loadServices(loader,serviceType);
        if(services!=null) {
            services.put(serviceType,l);
        }
        return l;
    }

    /**
     * Find the globally available implementations of a service.
     * 
     * <p>The list only contained the services that are shared, and not the application
     * specific ones.</p>
     * 
     * @param list an optional list containing the services. If not null, then it is returned
     * @param clazz the class to get the class loader for loading the services
     * @param serviceType the name of the service to load
     * @return a List of services implementation
     * @ibm-api
     */
    public static List<Object> findServices(List<Object> list, Class<?> clazz, String serviceType) {
        return findServices(list,clazz.getClassLoader(),serviceType);
    }


    /**
     * Find the globally available implementations of a service.
     * 
     * <p>The list only contained the services that are shared, and not the application
     * specific ones.</p>
     * 
     * @param list an optional list containing the services. If not null, then it is returned
     * @param classloader The class loader to look for the services
     * @param serviceType the name of the service to load
     * @return a List of services implementation
     * @ibm-api
     */
    public static List<Object> findServices(List<Object> list, ClassLoader loader, String serviceType) {
        if(list!=null) {
            return list;
        }
        // In case of an application class loader, ignores it
        // Note that this is only for a Domino environment and the XPages runtime
        if(loader instanceof ApplicationClassLoader) {
        	loader = loader.getParent();
        }
        list = loadServices(loader,serviceType);
        return list;
    }

    /**
     * Find the globally available implementations of a service.
     * 
     * <p>This is the type safety implementation of the method using generics. The content
     * of the list is checked to ensure that all the implementations belong to the correct
     * type.</p>
     * 
     * @param <T>
     * @param existingList
     *            already loaded service list; the services will only be loaded
     *            if list is <code>null</code>.
     * @param loader
     *            ClassLoader to use when loading the classes in a non-OSGI
     *            environment.
     * @param serviceType
     *            service id.
     * @param requiredType
     *            {@link Class} to be implemented by all contributing services.
     *            Services which do not implement that class will generate a
     *            warning and will not be included in the result {@link List}.
     *            When <code>null</code> no class validation is performed.
     * @return
     * @ibm-api
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public static <T> List<T> findServices(List<T> existingList, ClassLoader loader, String serviceType, Class<T> requiredType){
        if(existingList!=null) {
            return (List<T>) existingList;
        }
        
        List<T> items = (List<T>)findServices(null, loader, serviceType);
        if( null != requiredType){
            for (int i = 0; i < items.size(); i++) {
                Object item = items.get(i);
                
                if( ! (requiredType.isAssignableFrom(item.getClass())) ){
                    logServiceNotInstanceOf(serviceType, item, requiredType);
                    
                    items.remove(i);
                    i--;
                }
            }
        }
        return items;
    }
    private static void logServiceNotInstanceOf(String extensionPointId, Object obj, Class<?> requiredType) {
        // this is a problem
        if( CommonsLogger.STANDARD.isWarnEnabled() ){
            String warnMsg = "The object {0} cannot contribute to the service {1}, as it is not an instance of {2}";  // $NLW-ExtensionManager.ServiceObjectNotInstanceOfExpectedClass-1$
            CommonsLogger.STANDARD.warnp(ExtensionManager.class,
                    "logClassNotLibrary", warnMsg,//$NON-NLS-1$ 
                    obj, 
                    extensionPointId, 
                    requiredType.getName());
        }
    }

    /**
     * Load the available implementation of a service for a particular class loader.
     */
    private static List<Object> loadServices(final ClassLoader loader, final String serviceType) {
        List<Object> list = new ArrayList<Object>();
        provider.findInitializer(loader, list, serviceType);
        return list;
    }

    private interface Provider {
        public void findInitializer(ClassLoader loader, List<Object> initializers, String serviceType);
    }
    private static class JavaServiceProvider implements Provider {
        final String PREFIX = "META-INF/services/"; //$NON-NLS-1$
        public void findInitializer(ClassLoader loader, List<Object> initializers, String serviceType) {
            try {
                //PHIL: JDK 1.6 is providing a class to do that: java.util.ServiceLoader. Might
                // use that later
                
                // Look for all the resources in the class path
            	Enumeration<URL> e = getResourcesList(loader, serviceType);
            	if(e!=null) {
	                while( e.hasMoreElements() ) {
	                    URL url = e.nextElement();
	                    parseResource(loader,initializers,url, serviceType);
	                }
            	}
            } catch( Throwable t ) {
                t.printStackTrace();
            }
        }
        Enumeration<URL> getResourcesList(ClassLoader loader, String serviceType) throws IOException {
        	return loader.getResources(PREFIX+serviceType);
        }
        void parseResource(ClassLoader loader, List<Object> initializers, URL url, String serviceType) throws IOException {
            BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8")); //$NON-NLS-1$
            try {
                while(true) {
                    String s = r.readLine();
                    if(s==null) {
                        return;
                    }
                    int comment = s.indexOf('#');
                    if(comment>=0) {
                        s = s.substring(0,comment);
                    }
                    s = s.trim();
                    
                    // Try to load the class
                    if(StringUtil.isNotEmpty(s)) {
                        try {
                            Class<?> c = loader.loadClass(s);
                            Object o = c.newInstance();
                            initializers.add(o);
                        } catch(Throwable ex) {
                            logCouldNotCreateService(ex, s, serviceType);
                            TDiag.trace("Designer runtime: Error while parsing service file {0}", url.toString()); //$NON-NLS-1$
                        }
                    }
                }
            } finally {
                StreamUtil.close(r);
            }
        }
        private void logCouldNotCreateService(Throwable ex,
                String className, String serviceType) {
            if( CommonsLogger.STANDARD.isWarnEnabled() ){
                String warnMsg = "Could not create an instance of {0}, contributed to the service {1}."; // $NLW-ExtensionManager.Couldnotcreateaninstanceof0contri.1-1$
                CommonsLogger.STANDARD.warnp(this, "logCouldNotCreateService", ex, warnMsg, //$NON-NLS-1$
                        className, serviceType);
            }
        }
    }
    
    private static class EclipseProvider extends JavaServiceProvider {
        public void findInitializer(ClassLoader loader, List<Object> initializers, String serviceType) {
        	// Load the global providers, from the platform
            try {
                // Look if someone implemented an extension point to retrieve the platform
                IExtensionRegistry reg = org.eclipse.core.runtime.Platform.getExtensionRegistry();
                if (reg != null) {
                    IConfigurationElement[] elt = reg.getConfigurationElementsFor("com.ibm.commons.Extension"); // $NON-NLS-1$
                    for( int i=0; i<elt.length; i++ ) {
                        if( "service".equalsIgnoreCase(elt[i].getName()) ) { // $NON-NLS-1$
                            String type = elt[i].getAttribute("type"); //$NON-NLS-1$
                            if(StringUtil.equals(type, serviceType)) {
                                try {
                                    Object o = elt[i].createExecutableExtension("class"); // $NON-NLS-1$
                                    initializers.add(o);
                                } catch(Throwable ex) {
                                    logCouldNotCreateContribution(ex, elt[i], type);
                                }
                            }
                        }
                    }
                }
            } catch( Throwable t ) {
                t.printStackTrace();
            }
            
            // Then load the specific providers, from the current class loader
            // Change made for WAS environment
        	if(loader instanceof ApplicationClassLoader) {
        		// Domino
        		super.findInitializer(loader, initializers, serviceType);
        	} else {
        		// WAS
        		if(loader==Thread.currentThread().getContextClassLoader()) {
        			super.findInitializer(loader, initializers, serviceType);
        		}
            }
        }
        Enumeration<URL> getResourcesList(ClassLoader loader, String serviceType) throws IOException {
        	// In case of a Domino class loader, only return the resources from the class loader
        	if(loader instanceof ApplicationClassLoader) {
        		return ((ApplicationClassLoader)loader).findApplicationResources(PREFIX+serviceType);
        	}
        	// Else, return a enumeration for all of them
        	return loader.getResources(PREFIX+serviceType);
        }
        void logCouldNotCreateContribution(Throwable ex,
                IConfigurationElement ext, String type) {
            if( CommonsLogger.STANDARD.isWarnEnabled() ){
                String extensionPointId = "com.ibm.commons.Extension"; //$NON-NLS-1$
                String className = ext.getAttribute("class"); //$NON-NLS-1$
                String warnMsg = "Could not create an instance of {0}, contributed to the extension point {1} with type {2}."; // $NLW-ExtensionManager.Couldnotcreateaninstanceof0contri-1$
                CommonsLogger.STANDARD.warnp(this, "logCouldNotCreateContribution", ex, warnMsg, //$NON-NLS-1$
                        className, extensionPointId, type);
            }
        }
    }
}