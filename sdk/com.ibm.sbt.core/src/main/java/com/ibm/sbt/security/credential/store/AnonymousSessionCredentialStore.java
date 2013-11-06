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


/**
 * Load /store user credential from the current session object.
 * <p>
 * This is used to temporarily store the user credential when the user is
 * anonymous, and thus a persistent store cannot be used. 
 * </p>
 * @author Philippe Riand
 */
public class AnonymousSessionCredentialStore extends BaseStore {

	private static AnonymousSessionCredentialStore instance = new AnonymousSessionCredentialStore();
	
	public static AnonymousSessionCredentialStore get() {
		return instance; 
	}
    
	public AnonymousSessionCredentialStore() {
	}
	
	@Override
	public Object load(String service, String type, String user) throws CredentialStoreException {
		Context context = Context.get();
		String key = createKey(service, type, user);
		return deSerialize((byte[])context.getSessionMap().get(key));
	}
	
	@Override
	public void store(String service, String type, String user, Object credentials) throws CredentialStoreException {
		Context context = Context.get();
		String key = createKey(service, type, user);
		context.getSessionMap().put(key, serialize(credentials));
	}

	@Override
	public void remove(String service, String type, String user) throws CredentialStoreException {
		Context context = Context.get();
		String key = createKey(service, type, user);
		context.getSessionMap().remove(key);
	}
	
	/**
	 * Create a key for the internal map.
	 */
	protected String createKey(String service, String type, String user) throws CredentialStoreException {
		StringBuilder b = new StringBuilder(128);
		b.append("::sbt.credentials::");
		b.append(StringUtil.getNonNullString(service));
		b.append('|');
		b.append(StringUtil.getNonNullString(type));
		b.append('|');
		b.append(StringUtil.getNonNullString(user));
		return b.toString();
	}
    
}
