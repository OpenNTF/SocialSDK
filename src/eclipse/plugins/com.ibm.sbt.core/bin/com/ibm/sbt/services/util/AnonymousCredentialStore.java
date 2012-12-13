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

package com.ibm.sbt.services.util;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;


/**
 * Load /store user credential from the current session object.
 * <p>
 * This is used to temporarily store the user credential when the user is
 * anonymous, and thus a persistent store cannot be used. 
 * </p>
 * @author Philippe Riand
 */
public class AnonymousCredentialStore {
    
    
    //
    // AccessToken store for anonymous user
    // 
    public static Object loadCredentials(Context context, String key1, String key2) {
		String key = makeCredentialsKey(key1,key2);
		return context.getSessionMap().get(key);
    }
    
    public static void storeCredentials(Context context, Object tk, String key1, String key2) {
		String key = makeCredentialsKey(key1,key2);
		context.getSessionMap().put(key,tk);
    }
    
    public static void deleteCredentials(Context context, String key1, String key2) {
		String key = makeCredentialsKey(key1,key2);
		context.getSessionMap().remove(key);
    }
    
    private static String makeCredentialsKey(String key1, String key2) {
    	StringBuilder b = new StringBuilder();
    	b.append("sbt.endpoint");
    	if(StringUtil.isNotEmpty(key1)) { 
        	b.append(".");
        	b.append(key1);
    	}
    	if(StringUtil.isNotEmpty(key2)) { 
        	b.append(".");
        	b.append(key2);
    	}
    	return b.toString();
    }
}
