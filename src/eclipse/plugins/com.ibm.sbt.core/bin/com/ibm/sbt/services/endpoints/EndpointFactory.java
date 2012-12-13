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

package com.ibm.sbt.services.endpoints;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.util.SBTException;


/**
 * Factory for acessing an endpoint bean.
 * <p>
 * </p>
 * @author Philippe Riand
 */
public class EndpointFactory {

    // Prefix for the property
    public static final String SERVERPROP_PREFIX        = "sbt.endpoint.";
    public static final String SERVERBEAN_PREFIX        = "";
    
    // Some generic server predefined names
    public static final String SERVER_CONNECTIONS       = "connections";
    public static final String SERVER_SMARTCLOUD       = "smartcloud";
    public static final String SERVER_SAMETIME          = "sametime";
    public static final String SERVER_LOTUSLIVE         = "lotuslive";
    public static final String SERVER_DOMINO            = "domino";
    public static final String SERVER_FACEBOOK          = "facebook";
    public static final String SERVER_TWITTER           = "twitter";
    public static final String SERVER_DROPBOX           = "dropbox";
    public static final String SERVER_GOOGLE            = "google";

    /**
     * Get an endpoint based on its name and throw a FacesException if it is not available. 
     */    
    public static Endpoint getEndpoint(String name) {
        return getEndpoint(name,null);
    }
    public static Endpoint getEndpoint(String name, String defaultName) {
        Endpoint e = getEndpointUnchecked(name,defaultName);
        if(e==null) {
            if(StringUtil.isEmpty(name)) {
                name = defaultName;
            }
            throw new SBTException(null,"Cannot find Endpoint {0}",name); 
        }
        return e;
    }

    /**
     * Get an endpoint based on its name and return null if it is not available. 
     */    
    public static Endpoint getEndpointUnchecked(String name) {
        return getEndpointUnchecked(name, null);
    }
    public static Endpoint getEndpointUnchecked(String name, String defaultName) {
        if(StringUtil.isEmpty(name)) {
            name = defaultName;
        }
        // Look for a global property setting the default server, if the parameter is empty
        if(StringUtil.isEmpty(name)) {
            throw new SBTException(null,"The name of the endpoint is empty"); 
        }
        
        // Look for a property defining the bean associated to this server
        Context context = Context.get();
        String beanName = context.getProperty(SERVERPROP_PREFIX+name, null);
        if(StringUtil.isEmpty(beanName)) {
            beanName = SERVERBEAN_PREFIX+name;
        }
            
        // Look for a bean and/or class
        Object o = context.getBean(beanName);
        if(o!=null) {
            if(!(o instanceof Endpoint)) {
                throw new SBTException(null,"Invalid ServerBean implementation {0}",beanName); 
            }
            return (Endpoint)o;
        }
        return null;
    }
    
    public static String getEndpointName(String name) {
        Context context = Context.get();
        String beanName = context.getProperty(SERVERPROP_PREFIX+name, null);
        if(StringUtil.isEmpty(beanName)) {
            beanName = SERVERBEAN_PREFIX+name;
            return beanName;
        }
        return beanName;
    }
    
    public static String getEndpointLabel(String name) {
        Endpoint ep = getEndpoint(name);
        if(ep!=null) {
            return ep.getLabel();
        }
        return null;
    }
}
