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

package com.ibm.sbt.security.authentication.oauth.consumer.store;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.security.authentication.oauth.OAuthException;


/**
 * Find an application token store.
 * @author Philippe Riand
 */
public class OATokenStoreFactory {

    private static TokenStore defaultStore;
    
    public static synchronized TokenStore getDefaultStore() throws OAuthException {
        if(defaultStore==null) {
            defaultStore = new MemoryTokenStore();
        }
        return defaultStore;
    }    
    public static synchronized void setDefaultStore(TokenStore defaultStore) throws OAuthException {
        OATokenStoreFactory.defaultStore = defaultStore;
    }    

    public static TokenStore getTokenStore() throws OAuthException {
        return getTokenStore(null);
    }
    
    public static TokenStore getTokenStore(String name) throws OAuthException {
        // Look for a global property if the name is empty
        if(StringUtil.isEmpty(name)) {
            name = Context.get().getProperty("sbt.oauth.tokenStore", null);
        }
        // Look for a bean and/or class
        if(StringUtil.isNotEmpty(name)) {
            Object o = Context.get().getBean(name);
            if(!(o instanceof TokenStore)) {
                throw new OAuthException(null,"Invalid OAuth token store implementation {0}",name); 
            }
            return (TokenStore)o;
        }
        // Ok, use the default one
        return getDefaultStore();
    }
}
