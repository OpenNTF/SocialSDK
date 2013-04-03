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

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.ByteStreamCache;


/**
 * Base implementation of a credential store.
 * <p>
 * This class contains a set of utility methods to be used by actual implementations.
 * </p>
 * @author Philippe Riand
 */
public abstract class BaseStore implements CredentialStore {

	private String applicationName;
	private String encryptor;
	
	public BaseStore() {
	}
	
	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getEncryptor() {
		return encryptor;
	}

	public void setEncryptor(String encryptor) {
		this.encryptor = encryptor;
	}

	public String findApplicationName() {
		// Look if the application name had been forced and the return it
		String appName = getApplicationName();
		if(StringUtil.isNotEmpty(appName)) {
			return appName;
		}
		
		// Else, look at the application
		Application app = Application.getUnchecked();
		if(app!=null) {
			return app.getName();
		}
		
		// Else, we don't know...
		return null;
	}
	
	public CredentialEncryptor findEncryptor() throws CredentialStoreException {
		String name = getEncryptor();
		if(StringUtil.isNotEmpty(name)) {
	    	Context context = Context.get();
	        // Look for a bean and/or class
	        Object o = context.getBean(name);
	        if(o==null) {
	            throw new CredentialStoreException(null,"Encryptor bean {0} is not available. Please verify your configuration files.",name); 
	        }
	        if(!(o instanceof CredentialStore)) {
	            throw new CredentialStoreException(null,"Invalid Encryptor bean implementation {0}, class {1}",name,o.getClass()); 
	        }
	        return (CredentialEncryptor)o;
		}
		return null;
	}

	protected byte[] serialize(Object object) throws CredentialStoreException {
		byte[] ser = toByteArray(object);
		if(ser!=null) {
			CredentialEncryptor encryptor = findEncryptor();
			if(encryptor!=null) {
				return encryptor.encrypt(ser);
			}
			return ser;
		}
		return null;
	}

	protected Object deSerialize(byte[] bytes) throws CredentialStoreException {
		if(bytes!=null) {
			CredentialEncryptor encryptor = findEncryptor();
			if(encryptor!=null) {
				bytes = encryptor.decrypt(bytes);
			}
		}
		return fromByteArray(bytes);
	}

	protected byte[] toByteArray(Object object) throws CredentialStoreException {
		try {
			if(object!=null) {
				ByteStreamCache bs = new ByteStreamCache(1024);
				ObjectOutputStream os = new ObjectOutputStream(bs.getOutputStream());
				try {
					os.writeObject(object);
					return bs.toByteArray();
				} finally {
					os.close();
				}
			}
			return null;
		} catch(Exception ex) {
			throw new CredentialStoreException(ex,"Error while serializing credentials");
		}
	}

	protected Object fromByteArray(byte[] array) throws CredentialStoreException {
		try {
			if(array!=null && array.length>0) {
				ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(array));
				try {
					return is.readObject();
				} finally {
					is.close();
				}
			}
			return null;
		} catch(Exception ex) {
			throw new CredentialStoreException(ex,"Error while de-serializing credentials");
		}
	}
}
