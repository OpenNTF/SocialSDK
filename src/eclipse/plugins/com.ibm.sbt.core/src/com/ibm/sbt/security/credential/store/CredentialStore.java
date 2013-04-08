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

import com.ibm.sbt.security.authentication.AuthenticationException;





/**
 * Defines a credential store.
 * <p>
 * A credential store can be used to store application wise or user related credential. For example, an 
 * application can store an OAuth key/secret pair at the application level, while it stores every
 * user related tokens on a per user basics
 * </p>
 * @author Philippe Riand
 */
public interface CredentialStore {

	/**
	 * Load credentials from the store. 
	 * 
	 * @param service		the target service (ex: connections, domino...). Cannot be null.
	 * @param type			the type of credentials being stored. Can be null.
	 * @param user			the user associated with these credentials. if null, then it is application wise.
	 * @return
	 * @throws AuthenticationException
	 */
	public Object load(String service, String type, String user) throws CredentialStoreException;
	
	public void store(String service, String type, String user, Object credentials) throws CredentialStoreException;

	public void remove(String service, String type, String user) throws CredentialStoreException;
}
