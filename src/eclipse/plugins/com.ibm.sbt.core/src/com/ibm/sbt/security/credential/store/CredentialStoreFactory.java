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

package com.ibm.sbt.security.credential.store;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.security.authentication.AuthenticationException;


/**
 * Find a credential store.
 * <p>
 * This factory gets a credential store by name, or using a default namer.
 * </p> 
 * @author Philippe Riand
 */
public class CredentialStoreFactory {

    public static CredentialStore getCredentialStore() throws CredentialStoreException {
        return getCredentialStore(null);
    }
    
    public static CredentialStore getCredentialStore(String name) throws CredentialStoreException {
    	Context context = Context.get();
    	
    	// If the user is anonynmous, then the store should happen in memory
    	// This is a shared object for all the users
    	if(context.isCurrentUserAnonymous()) {
    		return AnonymousSessionCredentialStore.get();
    	}
    	
        // Look for a global property if the name is empty
        if(StringUtil.isEmpty(name)) {
            name = context.getProperty("sbt.credentialstore", null);
            if(StringUtil.isEmpty(name)) {
                name = "CredentialStore";
            }
        }
        // Look for a bean and/or class
        Object o = context.getBean(name);
        if(o==null) {
            throw new CredentialStoreException(null,"Credential store {0} is not available. Please verify your configuration file.",name); 
        }
        if(!(o instanceof CredentialStore)) {
            throw new CredentialStoreException(null,"Invalid credential store implementation {0}, class {1}",name,o.getClass()); 
        }
        return (CredentialStore)o;
    }
}
