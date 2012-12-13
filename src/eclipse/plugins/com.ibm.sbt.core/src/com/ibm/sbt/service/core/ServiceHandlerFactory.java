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
package com.ibm.sbt.service.core;

import java.util.HashMap;

import com.ibm.commons.Platform;
import com.ibm.sbt.security.authentication.oauth.consumer.oauth_10a.servlet.OA2Callback;
import com.ibm.sbt.security.authentication.oauth.consumer.oauth_10a.servlet.OACallback;
import com.ibm.sbt.security.authentication.oauth.consumer.oauth_10a.servlet.OAClientAuthentication;
import com.ibm.sbt.service.core.handlers.BasicAuthCredsHandler;
import com.ibm.sbt.service.core.handlers.ProxyHandler;
import com.ibm.sbt.service.core.handlers.EmailHandler;
import com.ibm.sbt.service.core.handlers.PingHandler;


/**
 * Proxy handler factory.
 * @author priand
 */
public class ServiceHandlerFactory {

    private static ServiceHandlerFactory instance = new ServiceHandlerFactory();
    public static ServiceHandlerFactory get() {
        return instance;
    }

    private HashMap<String,Object> aliases;
    
    public ServiceHandlerFactory() {
        registerHandler(PingHandler.URL_PATH,PingHandler.class); 
        registerHandler(ProxyHandler.URL_PATH,ProxyHandler.class); 
        registerHandler(BasicAuthCredsHandler.URL_PATH, BasicAuthCredsHandler.class); // $NON-NLS-1$
        registerHandler(OACallback.URL_PATH,OACallback.class); 
        registerHandler(OAClientAuthentication.URL_PATH,OAClientAuthentication.class);
        registerHandler(OA2Callback.URL_PATH,OA2Callback.class);  // Register OAuth2 callback handler with oauth20_cb url pattern
        registerHandler(EmailHandler.URL_PATH, EmailHandler.class);
    }
    
    public IServiceHandler get(String name) {
        // Look for an alias
        if(aliases!=null) {
            Object s = aliases.get(name);
            if(s!=null) {
                // Then instanciate the object
            	return createInstance(s);
            }
        }
        
        // The handler does not exist
        return null;
    }
    
    public void registerHandler(String name, Object clazz) {
        if(aliases==null) {
            aliases = new HashMap<String, Object>();
        }
        aliases.put(name,clazz);
    }
    
    public void unregisterHandler(String name) {
        if(aliases!=null) {
            aliases.remove(name);
        }
    }
    
    protected IServiceHandler createInstance(Object clazz) {
        try {
            if(clazz instanceof Class<?>) {
                return (IServiceHandler)((Class<?>)clazz).newInstance();
            }
            if(clazz instanceof String) {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                if(cl==null) {
                    cl = getClass().getClassLoader();
                }
                clazz = cl.loadClass((String)clazz);
            }
        } catch(Exception ex) {
            // Can't find/load it - return null
        	Platform.getInstance().log(ex);
        }
        return null;
    }
}