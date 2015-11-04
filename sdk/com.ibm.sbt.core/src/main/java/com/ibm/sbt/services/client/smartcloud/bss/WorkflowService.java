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

import com.ibm.sbt.services.client.base.JsonEntity;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * Use workflow services to get workflow lists
 * 
 * @author mwallace
 */
public class WorkflowService extends BssService {
	
	/*
	 * Allowed values for the workflow status.  
	 */
	static final public String STATUS_COMPLETED = "COMPLETED";
	static final public String STATUS_FAILED = "FAILED";
	static final public String STATUS_DEFERRED = "DEFERRED";
	static final public String STATUS_RUNNING = "RUNNING";

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 */
	public WorkflowService() {
	}

	/**
	 * Constructor
	 * 
	 * @param endpointName
	 */
	public WorkflowService(String endpointName) {
		super(endpointName);
	}

	/**
     * Constructor
     * 
     * @param endpointName
     * @param cacheSize
     */
    public WorkflowService(String endpointName, int cacheSize) {
       super(endpointName, cacheSize);
    }

	/**
     * Constructor
     * 
     * @param endpoint
     */
    public WorkflowService(Endpoint endpoint) {
        super(endpoint);
    }

	/**
     * Constructor
     * 
     * @param endpoint
     * @param cacheSize
     */
    public WorkflowService(Endpoint endpoint, int cacheSize) {
    	super(endpoint, cacheSize);
    }
    
    /**
     * Get a list of workflows by customer id.
     * 
     * @param customerId
     * 
     * @return {EntityList<JsonEntity>}
     * @throws BssException
     */
    public EntityList<JsonEntity> getWorkflowsByCustomerId(String customerId, int pageNumber, int pageSize) throws BssException {
    	try {
    		String serviceUrl = BssUrls.API_RESOURCE_GET_WORKFLOW_BY_CUSTOMERID.format(this, new NamedUrlPart("customerId", customerId));    		
    		HashMap<String, String> params = new HashMap<String, String>();
    		params.put("_pageNumber", String.valueOf(pageNumber));
    		params.put("_pageSize", String.valueOf(pageSize));
			return (EntityList<JsonEntity>)getEntities(serviceUrl, params, getJsonFeedHandler());
		} catch (Exception e) {		
			throw new BssException(e, "Error workflows for customer {0} caused by {1}", customerId, e.getMessage());
		}
    }

    /**
     * Get a list of workflows by workflow status.
     * 
     * @param workflowStatus
     * 
     * @return {EntityList<JsonEntity>}
     * @throws BssException
     */
    public EntityList<JsonEntity> getWorkflowsByStatus(String workflowStatus, int pageNumber, int pageSize) throws BssException {
    	try {
    		String serviceUrl = BssUrls.API_RESOURCE_GET_WORKFLOW_BY_STATUS.format(this, new NamedUrlPart("workflowStatus", workflowStatus));    		
    		HashMap<String, String> params = new HashMap<String, String>();    		
    		params.put("_pageNumber", String.valueOf(pageNumber));
    		params.put("_pageSize", String.valueOf(pageSize));
			return (EntityList<JsonEntity>)getEntities(serviceUrl, params, getJsonFeedHandler());
		} catch (Exception e) {		
			throw new BssException(e, "Error workflows for workflow status {0} caused by {1}", workflowStatus, e.getMessage());
		}
    }

    /**
     * Get a list of workflows by subscriber  id.
     * 
     * @param subscriberId
     * 
     * @return {EntityList<JsonEntity>}
     * @throws BssException
     */
    public EntityList<JsonEntity> getWorkflowsBySubscriberId(String subscriberId, int pageNumber, int pageSize) throws BssException {
    	try {
    		String serviceUrl = BssUrls.API_RESOURCE_GET_WORKFLOW_BY_SUBSCRIBERID.format(this, new NamedUrlPart("subscriberId", subscriberId));    		
    		HashMap<String, String> params = new HashMap<String, String>();
    		params.put("_pageNumber", String.valueOf(pageNumber));
    		params.put("_pageSize", String.valueOf(pageSize));
			return (EntityList<JsonEntity>)getEntities(serviceUrl, params, getJsonFeedHandler());
		} catch (Exception e) {		
			throw new BssException(e, "Error workflows for subscriber {0} caused by {1}", subscriberId, e.getMessage());
		}
    }

}
