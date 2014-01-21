/*
 * © Copyright IBM Corp. 2014
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

package com.ibm.sbt.service.proxy;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;

/**
 * Find a Proxy configuration
 * @author Manish Kataria
 */
public class ProxyFactory {

    public static Proxy getProxyConfig() throws ProxyConfigException {
        return getProxyConfig(null);
    }
    
    public static Proxy getProxyConfig(String name) throws ProxyConfigException {
    	
    	if(StringUtil.isEmpty(name)){
    		// No Proxy is defined by endpoint 
    		// return default SBT Proxy
    		return new SBTProxy();
    	}
    	
    	Context context = Context.get();
    	
       // Look for a bean and/or class
        Object o = context.getBean(name);
        if(o==null) {
            throw new ProxyConfigException(null,"Proxy Configuration {0} is not available. Please verify your configuration files.",name); 
        }
        if(!(o instanceof Proxy)) {
            throw new ProxyConfigException(null,"Invalid Proxy Configuration implementation {0}, class {1}",name,o.getClass()); 
        }
        return (Proxy)o;
    }
}
