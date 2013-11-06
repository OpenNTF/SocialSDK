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

import java.util.HashMap;
import java.util.Map;

import com.ibm.commons.util.StringUtil;





/**
 * Implementation of a credential store storing the credentials in memory.
 * <p>
 * </p>
 * @author Philippe Riand
 */
public class MemoryStore extends BaseStore {

	private Map<String,byte[]> map = new HashMap<String,byte[]>();
	
	public MemoryStore() {
	}
	
	@Override
	public Object load(String service, String type, String user) throws CredentialStoreException {
		String application = findApplicationName();
		String key = createKey(application, service, type, user);
		return deSerialize(map.get(key));
	}
	
	@Override
	public void store(String service, String type, String user, Object credentials) throws CredentialStoreException {
		String application = findApplicationName();
		String key = createKey(application, service, type, user);
		map.put(key, serialize(credentials) );
	}

	@Override
	public void remove(String service, String type, String user) throws CredentialStoreException {
		String application = findApplicationName();
		String key = createKey(application, service, type, user);
		map.remove(key);
	}
	
	/**
	 * Create a key for the internal map.
	 */
	protected String createKey(String application, String service, String type, String user) throws CredentialStoreException {
		StringBuilder b = new StringBuilder(128);
		b.append(StringUtil.getNonNullString(application));
		b.append('|');
		b.append(StringUtil.getNonNullString(service));
		b.append('|');
		b.append(StringUtil.getNonNullString(type));
		b.append('|');
		b.append(StringUtil.getNonNullString(user));
		return b.toString();
	}
}
