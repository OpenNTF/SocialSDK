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

import java.util.HashMap;
import java.util.Map;

import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.JsonEntity;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
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
     * @param cacheSize
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
     * @return
     * @throws BssException
     */
    public EntityList<JsonEntity> getRoles(String loginName) throws BssException {
    	try {
    		Map<String, String> params = new HashMap<String, String>();
    		params.put("loginName", loginName);
			return (EntityList<JsonEntity>)getEntities(API_AUTHORIZATION_GETROLELIST, params, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving role list");
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
    		Map<String, String> params = new HashMap<String, String>();
    		params.put("loginName", loginName);
    		params.put("role", role);
			Response response = createData(API_AUTHORIZATION_ASSIGNROLE, params, null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error assigning role {0} to {1}", role, loginName);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving role list");
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
    		Map<String, String> params = new HashMap<String, String>();
    		params.put("loginName", loginName);
    		params.put("role", role);
			Response response = createData(API_AUTHORIZATION_UNASSIGNROLE, params, null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error assigning role {0} to {1}", role, loginName);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving role list");
		}
    }

}
