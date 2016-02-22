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
package com.ibm.sbt.services.client.smartcloud.bss;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.Arrays;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.JsonEntity;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * Use authorizaton services to manage roles.
 * 
 * @author mwallace
 */
public class AuthorizationService extends BssService {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 */
	public AuthorizationService() {
	}

	/**
	 * Constructor
	 * 
	 * @param endpointName
	 */
	public AuthorizationService(String endpointName) {
		super(endpointName);
	}

	/**
     * Constructor
     * 
     * @param endpointName
     * @param cacheSize
     */
    public AuthorizationService(String endpointName, int cacheSize) {
       super(endpointName, cacheSize);
    }

	/**
     * Constructor
     * 
     * @param endpoint
     */
    public AuthorizationService(Endpoint endpoint) {
        super(endpoint);
    }

	/**
     * Constructor
     * 
     * @param endpoint
     * @param cacheSize
     */
    public AuthorizationService(Endpoint endpoint, int cacheSize) {
    	super(endpoint, cacheSize);
    }
    
    /**
     * Get a list of authorization roles for a subscriber.
     * 
     * @return {String[]}
     * @throws BssException
     */
    public String[] getRoles(String loginName) throws BssException {
    	try {
			String serviceUrl = BssUrls.API_AUTHORIZATION_GETROLELIST.format(this, new NamedUrlPart("loginName", loginName));
			Response serverResponse = createData(serviceUrl, null, null, null, ClientService.FORMAT_JSON);
			
			JsonJavaObject rolesObject = (JsonJavaObject)serverResponse.getData();
			List<Object> roles = rolesObject.getAsList(PROPERTY_LIST);
			return (String[])roles.toArray(new String[roles.size()]);
		} catch (Exception e) {		
			throw new BssException(e, "Error retrieving role list for {0} caused by {1}", loginName, e.getMessage());
		}
    }
    
    /**
     * Assign a role to a subscriber.
     * 
     * @param loginName
     * @param role
     * @throws BssException
     */
    public void assignRole(String loginName, String role) throws BssException {
    	try {
			String serviceUrl = BssUrls.API_AUTHORIZATION_ASSIGNROLE.format(this, 
					new NamedUrlPart("loginName", loginName), new NamedUrlPart("role", role));
			Response response = createData(serviceUrl, null, null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error assigning role {0} to {1}", role, loginName);
    		}
		} catch (Exception e) {		
			throw new BssException(e, "Error assigning role {0} to {1} caused by {2}", role, loginName, e.getMessage());			
		} 
    }

    /**
     * Remove a role from a subscriber.
     * 
     * @param loginName
     * @param role
     * @throws BssException
     */
    public void unassignRole(String loginName, String role) throws BssException {
    	try {
			String serviceUrl = BssUrls.API_AUTHORIZATION_UNASSIGNROLE.format(this, 
					new NamedUrlPart("loginName", loginName), new NamedUrlPart("role", role));
			Response response = createData(serviceUrl, null, null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error unassigning role {0} to {1}", role, loginName);
    		}
		} catch (Exception e) {		
			throw new BssException(e, "Error unassigning role {0} to {1} caused by {2}", role, loginName, e.getMessage());			
		} 
    }
    
    /**
     * Wait for the Role Set to change and then call the state change listener.
     * 
     * @param loginName
     * @param roles
     * @param maxAttempts
     * @param waitInterval
     * @param listener
     * 
     * @throws BssException 
     */
    public boolean waitRoleSetState(String loginName, String[] roles, int maxAttempts, long waitInterval, StateChangeListener listener) throws BssException {
    	for (int i=0; i<maxAttempts; i++) {
    		String[] currentRoles = getRoles(loginName);
    		if (!Arrays.equals(roles, currentRoles)) {
    			try {
    				if (listener != null) {
    					JsonEntity jsonEntity = null;
    					listener.stateChanged(jsonEntity);
    				}
    				return true;
    			} catch (Exception e) {
    				logger.log(Level.WARNING, "Error invoking state change listener", e);
    			}
    		}
    		
    		// wait the specified interval
			try {
				Thread.sleep(waitInterval);
			} catch (InterruptedException ie) {}
    	}
    	return false;
    }

}
