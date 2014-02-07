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
package com.ibm.sbt.services.client.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * 
 * @author mwallace
 *
 */
public class JsonService extends BaseService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public JsonService() {
	}

	/**
	 * Constructor
	 * 
	 * @param endpointName
	 */
	public JsonService(String endpointName) {
		super(endpointName);
	}

	/**
     * Constructor
     * 
     * @param endpointName
     * @param cacheSize
     */
    public JsonService(String endpointName, int cacheSize) {
       super(endpointName, cacheSize);
    }

	/**
     * Constructor
     * 
     * @param endpoint
     * @param cacheSize
     */
    public JsonService(Endpoint endpoint) {
        super(endpoint);
    }

	/**
     * Constructor
     * 
     * @param endpoint
     * @param cacheSize
     */
    public JsonService(Endpoint endpoint, int cacheSize) {
    	super(endpoint, cacheSize);
    }
    
	protected EntityList<JsonEntity> getJsonEntityList(String requestUrl, Map<String, String> parameters, String entitiesPath) throws ClientServicesException {
		try {
			return (EntityList<JsonEntity>)getEntities(requestUrl, getParameters(parameters), getJsonFeedHandler(entitiesPath));
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}
	
	protected IFeedHandler<JsonEntity> getJsonFeedHandler(String entitiesPath) {
		return new JsonFeedHandler<JsonEntity>(this, entitiesPath) {
			@Override
			protected JsonEntity newEntity(BaseService service, JsonJavaObject jsonObject) {
				return new JsonEntity(service, jsonObject);
			}
		};
	}
	
	protected Map<String, String> getParameters(Map<String, String> parameters) {
		if(parameters == null) return new HashMap<String, String>();
		else return parameters;
	}
	
}
