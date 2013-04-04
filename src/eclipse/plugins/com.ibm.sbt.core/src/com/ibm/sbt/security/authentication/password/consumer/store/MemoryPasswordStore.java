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

import java.util.HashMap;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.security.authentication.password.PasswordException;
import com.ibm.sbt.security.authentication.password.consumer.UserPassword;




/**
 * Store the user/password in memory.
 * @author Philippe Riand
 */
public class MemoryPasswordStore implements PasswordStore {
    
    protected class UserPwd extends UserPassword {
        protected UserPwd() {
        }
        protected UserPwd(String requestUri, String user, String password) {
            super(user, password);
        }
    }
    
    private HashMap<String,HashMap<String,UserPwd>> inMemoryStore = new HashMap<String,HashMap<String,UserPwd>>();
    
    public MemoryPasswordStore() {
    }
    
    @Override
	public synchronized UserPassword loadUserPassword(String requestUri) throws PasswordException {
        String designerKey = getDesignerKey();
        HashMap<String,UserPwd> map = inMemoryStore.get(designerKey);
        if(map!=null) {
            String userKey = getUserKey();
            if(StringUtil.isNotEmpty(userKey)) {
	            String key = composeAccessKey(requestUri, userKey);
	            UserPwd u = map.get(key);
	            if(u!=null) {
	                return u;
	            }
            }
        }
        return null;
    }

    @Override
	public synchronized void saveUserPassword(String requestUri, String userId, String password) throws PasswordException {
        // If the user id is null, then remove the keys
        if(StringUtil.isEmpty(userId)) {
            deleteUserPassword(requestUri);
        } else {
            String designerKey = getDesignerKey();
            HashMap<String,UserPwd> map = inMemoryStore.get(designerKey);
            if(map==null) {
                map = new HashMap<String,UserPwd>();
                inMemoryStore.put(designerKey, map);
            }
            String userKey = getUserKey();
            if(StringUtil.isNotEmpty(userKey)) {
	            UserPwd userPwd = new UserPwd(requestUri, userId, password);
	            String key = composeAccessKey(requestUri, userKey);
	            map.put(key, userPwd);
            }
        }
    }

    @Override
	public synchronized void deleteUserPassword(String requestUri) throws PasswordException {
        String designerKey = getDesignerKey();
        HashMap<String,UserPwd> map = inMemoryStore.get(designerKey);
        if(map!=null) {
            String userKey = getUserKey();
            if(StringUtil.isNotEmpty(userKey)) {
            	String key = composeAccessKey(requestUri, userKey);
            	map.remove(key);
            }
        }
    }
    
    protected String composeAccessKey(String requestUri, String user) {
        return requestUri+"|"+user;
    }
    
    // the designer is used to maintain a list of all the keys for the 
    // applications build by this user.
    // thus, an application not signed by the same designer will not access the
    // same store
    protected String getDesignerKey()throws PasswordException {
    	return "<default>";
    }
    
    // the user key is used to store the key for a particular end user
    // this key is used in the context of a designer key 
    protected String getUserKey()throws PasswordException {
    	Context ctx = Context.get();
    	if(!ctx.isCurrentUserAnonymous()) {
    		return null;
    	}
    	return Context.get().getCurrentUserId();
    }
}
