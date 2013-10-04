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

package com.ibm.sbt.services.client.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.jslibrary.SBTEnvironment;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientService.Handler;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;


/**
 * This class defines common behaviour for all the services 
 * @author Carlos Manias
 *
 */
public abstract class BaseService {

	public static final int						DEFAULT_CACHE_SIZE		= 0;
	public static final String					DEFAULT_ENDPOINT_NAME	= "connections";
	protected static HashMap<String, Object>	cache					= new HashMap<String, Object>();
	protected int								cacheSize;
	protected Handler							dataFormat;
	protected Endpoint							endpoint;

	/**
	 * Constructor
	 */
	public BaseService() {
		this(DEFAULT_ENDPOINT_NAME, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Constructor
	 * 
	 * @param endpointName
	 */
	public BaseService(String endpointName) {
		this(endpointName, DEFAULT_CACHE_SIZE);
	}

	/**
     * Constructor
     * 
     * @param endpointName
     * @param cacheSize
     */
    public BaseService(String endpointName, int cacheSize) {
        if (StringUtil.isEmpty(endpointName)) {
            endpointName = DEFAULT_ENDPOINT_NAME;
        }
        
        this.endpoint = getEnvironmentEndpoint(endpointName);
        this.cacheSize = cacheSize;
    }

	/**
     * Constructor
     * 
     * @param endpoint
     * @param cacheSize
     */
    public BaseService(Endpoint endpoint) {
        this(endpoint, DEFAULT_CACHE_SIZE);
    }

	/**
     * Constructor
     * 
     * @param endpoint
     * @param cacheSize
     */
    public BaseService(Endpoint endpoint, int cacheSize) {
        this.endpoint = endpoint;
        this.cacheSize = cacheSize;
    }

    /*
     * Check the environment to see which endpoints are available.
     * @param endpointName Requested endpoint.
     * @return The Endpoint which matches endpointName, first by alias then by name. 
     * @throws SBTException if endpointName is not found in the environment.
     */
    private Endpoint getEnvironmentEndpoint(String endpointName){
    	Context context = Context.getUnchecked();
    	if (context == null) {
    		return null;
    	}
        String environment = context.getProperty("environment");
        if(environment != null) {
            SBTEnvironment env = (SBTEnvironment) context.getBean(environment);
            SBTEnvironment.Endpoint[] endpointsArray = env.getEndpointsArray();
            for(SBTEnvironment.Endpoint endpoint : endpointsArray){
                if(StringUtil.equals(endpointName, endpoint.getAlias())){
                    endpointName = endpoint.getName();
                    break;
                } else if (StringUtil.equals(endpointName, endpoint.getName())){
                    break;
                }
            }
        }
        return EndpointFactory.getEndpoint(endpointName);
    }


	/**
	 * @return dataFormat
	 */
	public Handler getDataFormat() {
		return this.dataFormat;
	}

	/**
	 * Sets the endpoint
	 * 
	 * @param endpoint
	 *            the endpoint
	 */
	public void setEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
	}

	/**
	 * Returns the Endpoint
	 * 
	 * @return endpoint the Endpoint
	 */
	public Endpoint getEndpoint() {
		return endpoint;
	}

	/**
	 * Return the size of the cache
	 * 
	 * @return cacheSize the cache size
	 */
	public int getCacheSize() {
		return cacheSize;
	}

	/**
	 * Sets the size of the cache
	 * 
	 * @param cacheSize
	 */
	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}

	/*
	 * This method makes a network call and returns an entity
	 */
	protected BaseEntity getEntity(String url, Map<String, String> parameters, IFeedHandler feedHandler) throws IOException, ClientServicesException {
		Response dataHolder = null;
		dataHolder = retrieveData(url, parameters);
		return feedHandler.createEntity(dataHolder);
	}
	
	/*
	 * This method makes a network call and returns a Collection of Entities
	 */
	protected EntityList<? extends BaseEntity> getEntities(String url, Map<String, String> parameters, IFeedHandler feedHandler) throws ClientServicesException, IOException {
		Response dataHolder = retrieveData(url, parameters);
		return feedHandler.createEntityList(dataHolder);
	}

	/*
     * This method makes a network call and returns a Collection of Entities
     */
    protected EntityList<? extends BaseEntity> getEntities(String url, Map<String, String> parameters, Map<String, String> headers, IFeedHandler feedHandler) throws ClientServicesException, IOException {
        Response dataHolder = retrieveData(url, parameters, headers, null);
        return feedHandler.createEntityList(dataHolder);
    }


	/**
	 * Post some data and return the resulting response
	 * 
	 * @param serviceUrl
	 * @param parameters
	 * @param headers
	 * @param content
	 * @param format
	 * @return
	 * @throws ClientServicesException
	 * @throws IOException
	 */
    public Response createData(String serviceUrl, Map<String, String> parameters, Map<String,String> headers, Object content) throws ClientServicesException, IOException {
        return createData(serviceUrl, parameters, headers, content, dataFormat);
    }
	
    /**
     * Post some data and return the resulting response
     * 
     * @param serviceUrl
     * @param parameters
     * @param headers
     * @param content
     * @param format
     * @return
     * @throws ClientServicesException
     * @throws IOException
     */
    public Response createData(String serviceUrl, Map<String, String> parameters, Map<String,String> headers, Object content, Handler format) throws ClientServicesException, IOException {
        Response result = getClientService().post(serviceUrl, parameters, headers, content, format);
        return result;
    }
    	
	/**
	 * Creates data and returns the result
	 * 
	 * @param clientService
	 * @param serviceUrl
	 * @param parameters
	 * @param content
	 * @return the result of the creation operations
	 * @throws ClientServicesException
	 *             when the creation fails, as null/false return may have other meaning here
	 * @throws IOException 
	 */
	public Response createData(String serviceUrl, Map<String, String> parameters, Object content, Handler format) throws ClientServicesException, IOException {
	    return createData(serviceUrl, parameters, new HashMap<String, String>(), content);	    
	}
	
	
	/**
	 * Creates data and returns the result 
	 * @param serviceUrl
	 * @param parameters
	 * @param content
	 * @return
	 * @throws ClientServicesException
	 * @throws IOException
	 */
	public Response createData(String serviceUrl, Map<String, String> parameters, Object content)
			throws ClientServicesException, IOException {
		return createData(serviceUrl, parameters, content, dataFormat);
	}

	/**
	 * This method encapsulate the access to the Endpoint client service.
	 * 
	 * @return the client serivce associated with the current Endpoint
	 * @throws ClientServicesException
	 */
	protected ClientService getClientService() throws ClientServicesException {
		return endpoint.getClientService();
	}

	
	/**
	 * Returns true if delete completed successfully and false if an error happened
	 * 
	 * @param clientService
	 * @param serviceUrl
	 * @param parameters
	 * @param nameParameterId
	 * @return
	 * @throws ClientServicesException
	 * @throws IOException 
	 */
	public Response deleteData(String serviceUrl, Map<String, String> parameters, String nameParameterId)
			throws ClientServicesException, IOException {
	    return deleteData(serviceUrl, parameters, new HashMap<String, String>(), nameParameterId);
	}
	
	/**
	 * execute a delete and returns the result
	 * @param serviceUrl
	 * @param parameters
	 * @param headers
	 * @param nameParameterId
	 * @return
	 * @throws ClientServicesException
	 * @throws IOException
	 */
    public Response deleteData(String serviceUrl, Map<String, String> parameters, Map<String, String> headers, String nameParameterId)
            throws ClientServicesException, IOException {
        //TODO : remove the data format after review with Phil
        String uniqueId = "";
        if (nameParameterId != null) {
            uniqueId = parameters.get(nameParameterId);
        }

        Response r = getClientService().delete(serviceUrl, parameters, headers, dataFormat);
        if (cacheSize > 0 && nameParameterId != null) {
            removeFromCache(uniqueId);
        }

        return r;
    }
	/**
	 * retrieveData
	 * 
	 * @param clientService
	 * @param url
	 * @param parameters
	 * @return Convenience method for retrieving multiple JsonObjects from the server
	 * @throws ClientServicesException
	 * @throws IOException 
	 */
	public Response retrieveData(String url, Map<String, String> parameters) throws ClientServicesException, IOException {
		return retrieveData(url, parameters, null);
	}

	/**
	 * execute a get and return the result
	 * 
	 * @param url
	 * @param parameters
	 * @param headers
	 * @param nameParameterId
	 * @return
	 * @throws ClientServicesException
	 * @throws IOException
	 */
	public Response retrieveData(String url, Map<String, String> parameters, Map<String, String> headers, String nameParameterId)
	            throws ClientServicesException, IOException {
	        Object data = null;
	        Response dataHolder = null;
	        String uniqueId = "";
	        if (nameParameterId != null) {
	            uniqueId = parameters.get(nameParameterId);
	            data = findInCache(uniqueId);
	            dataHolder = new Response(data);
	        }
	        if (data == null) {
	            dataHolder = getClientService().get(url, parameters, headers, dataFormat);
	            data = dataHolder.getData();
	            if (cacheSize > 0 && nameParameterId != null) {
	                addDataToCache(uniqueId, data);
	            }
	        }
	        return dataHolder;
	    }
	
	/**
	 * retrieveData()
	 * 
	 * @param clientService
	 * @param url
	 * @param parameters
	 * @param nameParameterId
	 * @return Method to retrieve a single Object from the server
	 * @throws ClientServicesException
	 * @throws IOException 
	 */
	//TODO Fix cache with DataHolder object
	public Response retrieveData(String url, Map<String, String> parameters, String nameParameterId) throws ClientServicesException, IOException {
			return retrieveData(url, parameters, new HashMap<String, String>(), nameParameterId);
	}
	
	/**
	 * Returns true if update completed successfully and false if an error happened
	 * 
	 * @param clientService
	 * @param serviceUrl
	 * @param parameters
	 * @param content
	 * @param nameParameterId - only used to distinguish between cache entries, 
	 *     must be a valid key of the parameters map
	 * @return
	 * @throws ClientServicesException
	 * @throws IOException 
	 */
	//TODO: Use Args pattern to pass the headers and avoid hard-coding the content-type
	public Response updateData(String serviceUrl, Map<String, String> parameters, Object content,
			String nameParameterId) throws ClientServicesException, IOException {
	    return updateData(serviceUrl, parameters, new HashMap<String, String>(), content, nameParameterId);
	}

	/**
	 * execute a put and return the response
	 * 
	 * @param serviceUrl
	 * @param parameters
	 * @param headers
	 * @param content
	 * @param nameParameterId
	 * @return
	 * @throws ClientServicesException
	 * @throws IOException
	 */
   public Response updateData(String serviceUrl, Map<String, String> parameters, Map<String, String> headers, Object content,
	            String nameParameterId) throws ClientServicesException, IOException {
        String uniqueId = "";
        if (nameParameterId != null) {
            uniqueId = parameters.get(nameParameterId);
        }

        headers.put("Content-Type", "application/atom+xml");
        Response result = getClientService().put(serviceUrl, parameters, headers, content, dataFormat);
        if (cacheSize > 0 && nameParameterId != null) {
            addDataToCache(uniqueId, content);
        }

        return result;
    }

	
	/**
	 * findInCache()
	 * 
	 * @param userId
	 * @return Method to search the cache
	 */
	private Object findInCache(String key) {

		Object data = null;

		if (cache.containsKey(key)) {
			data = cache.get(key);
		} else {
			// Cache miss
		}

		return data;
	}

	/**
	 * Removes from cache the data stored under the given key
	 * 
	 * @param key
	 */
	protected void removeFromCache(String key) {
		cache.remove(key);
	}

	/**
	 * Adds to cache the object using the given key
	 * 
	 * @param key
	 * @param content
	 */
	protected void addDataToCache(String key, Object content) {
		// Limit the cache size as per options
		// to check if cache is full , remove if full using LRU algorithm
		cache.put(key, content);
	}
	
	/**
	 * Return true if the id as email address
	 * 
	 * @param id
	 * @return
	 */
	protected boolean isEmail(String id) {
		return (id == null) ? false : id.contains("@");
	}
	
}
