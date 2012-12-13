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

package com.ibm.sbt.security.authentication.password.consumer.store;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.security.authentication.password.PasswordException;


/**
 * Find a password store for basic authentication.
 * @author Philippe Riand
 */
public class PasswordStoreFactory {

    public static PasswordStore getPasswordStore() throws PasswordException {
        return getPasswordStore(null);
    }
    
    public static PasswordStore getPasswordStore(String name) throws PasswordException {
        // Look for a global property if the name is empty
        if(StringUtil.isEmpty(name)) {
            name = Context.get().getProperty("sbt.basic.passwordStore", null);
        }
        // Look for a bean and/or class
        if(StringUtil.isNotEmpty(name)) {
            Object o = Context.get().getBean(name);
            if(!(o instanceof PasswordStore)) {
                throw new PasswordException(null,"Invalid Basic Authentication  store implementation {0}",name); 
            }
            return (PasswordStore)o;
        }
        // No pasword store is configured
        return null;
    }
}
