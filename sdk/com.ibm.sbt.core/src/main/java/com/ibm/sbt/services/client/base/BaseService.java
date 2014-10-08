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

import static com.ibm.sbt.services.client.base.CommonConstants.APPLICATION_ATOM_XML;
import static com.ibm.sbt.services.client.base.CommonConstants.AT;
import static com.ibm.sbt.services.client.base.CommonConstants.CONTENT_TYPE;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientService.Handler;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.CommonConstants.HTTPCode;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;

/**
 * This class defines common behaviour for all the services 
 * @author Carlos Manias
 *
 */
public abstract class BaseService implements Serializable {

	private static final long 					serialVersionUID = 1L;

	public static final int						DEFAULT_CACHE_SIZE		= 0;
	public static final String					DEFAULT_ENDPOINT_NAME	= "connections";
	
	protected transient static HashMap<String, Object>	cache					= new HashMap<String, Object>();
	protected transient int						cacheSize;
	protected Handler							dataFormat;
	protected transient Endpoint				endpoint;

	protected String[] serviceMappingKeys = new String[]{};
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
        
        this.endpoint = EndpointFactory.getEndpointFromEnvironment(endpointName, null);
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
     * Get authentication type for the endpoint. like basicAuth, oauth etc.
     * @return
     */
	public NamedUrlPart getAuthType(){
		return new NamedUrlPart("authType",AuthType.getAuthTypePart(endpoint));
    }
	
	/**
	 * Returns a Version object with the API version of the Endpoint
	 * @return
	 */
	public Version getApiVersion(){
		return Version.parse(endpoint.getApiVersion());
	}
	
	/**
	 * Subclasses must list the context roots of the Connections APIs they use.
	 * @return
	 */
	public String[] getServiceMappingKeys(){
		return serviceMappingKeys;
	}
	
	/**
	 * Returns either the configured or the default serviceMappings for the service 
	 * @return
	 */
	public NamedUrlPart[] getServiceMappings(){
		String[] serviceMappingKeys = getServiceMappingKeys();
		Map<String, String> serviceMappings = getEndpoint().getServiceMappings();
		
		NamedUrlPart[] urlParts = new NamedUrlPart[serviceMappingKeys.length];
		for(int i = 0;i < serviceMappingKeys.length; i++){
			String key = serviceMappingKeys[i];
			if (serviceMappings.containsKey(key)){
				urlParts[i] = new NamedUrlPart(key, serviceMappings.get(key));
			}else{
				urlParts[i] = new NamedUrlPart(key, key);
			}
		}
		
		return urlParts;
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
	protected <T extends BaseEntity> T getEntity(String url, Map<String, String> parameters, IFeedHandler<T> feedHandler) throws ClientServicesException {
		try {
			Response response = retrieveData(url, parameters);
			return feedHandler.createEntity(response);
		} catch (Exception e){
			throw new ClientServicesException(e);
		}
	}
	
	/*
	 * This method makes a network call and returns a Collection of Entities
	 */
	protected <T extends BaseEntity> EntityList<T> getEntities(String url, Map<String, String> parameters, IFeedHandler<T> feedHandler) throws ClientServicesException {
		try {
			Response dataHolder = retrieveData(url, parameters);
			return feedHandler.createEntityList(dataHolder);
		} catch (Exception e){
			throw new ClientServicesException(e);
		}
	}

	/*
     * This method makes a network call and returns a Collection of Entities
     */
    protected <T extends BaseEntity> EntityList<T> getEntities(String url, Map<String, String> parameters, Map<String, String> headers, IFeedHandler<T> feedHandler) throws ClientServicesException {
		try {
	        Response dataHolder = retrieveData(url, parameters, headers, null);
	        return feedHandler.createEntityList(dataHolder);
		} catch (Exception e){
			throw new ClientServicesException(e);
		}
    }


	/**
	 * Post some data and return the resulting response
	 * 
	 * @param serviceUrl
	 * @param parameters
	 * @param headers
	 * @param content
	 * @param DateFormat
	 * @return
	 * @throws ClientServicesException
	 */
    public Response createData(String serviceUrl, Map<String, String> parameters, Map<String,String> headers, Object content) throws ClientServicesException {
        return createData(serviceUrl, parameters, headers, content, getDataFormat());
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
     */
    public Response createData(String serviceUrl, Map<String, String> parameters, Map<String,String> headers, Object content, Handler format) throws ClientServicesException {
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
	 */
	public Response createData(String serviceUrl, Map<String, String> parameters, Object content, Handler format) throws ClientServicesException {
    	return createData(serviceUrl, parameters, new HashMap<String, String>(), content);	    
	}
	
	
	/**
	 * Creates data and returns the result 
	 * @param serviceUrl
	 * @param parameters
	 * @param content
	 * @return
	 * @throws ClientServicesException
	 */
	public Response createData(String serviceUrl, Map<String, String> parameters, Object content) throws ClientServicesException {
		return createData(serviceUrl, parameters, content, getDataFormat());
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
	 */
	public Response deleteData(String serviceUrl, Map<String, String> parameters, String nameParameterId)
			throws ClientServicesException {
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
	 */
    public Response deleteData(String serviceUrl, Map<String, String> parameters, Map<String, String> headers, String nameParameterId)
            throws ClientServicesException {
        String uniqueId = null;
        if (nameParameterId != null) {
            uniqueId = (parameters == null) ? nameParameterId : parameters.get(nameParameterId);
        }

        Response r = getClientService().delete(serviceUrl, parameters, headers, getDataFormat());
        
        if (cacheSize > 0 && uniqueId != null) {
            removeFromCache(uniqueId);
        }

        return r;
    }
    
    public Response delete(String serviceUrl, Map<String, String> parameters, Map<String, String> headers, String nameParameterId, String content)throws ClientServicesException{
    	String uniqueId = null;
        if (nameParameterId != null) {
            uniqueId = (parameters == null) ? nameParameterId : parameters.get(nameParameterId);
        }

        Response r = getClientService().delete(serviceUrl, parameters, headers, getDataFormat(),content);
        
        if (cacheSize > 0 && uniqueId != null) {
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
	 */
	public Response retrieveData(String url, Map<String, String> parameters) throws ClientServicesException {
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
	 */
	public Response retrieveData(String url, Map<String, String> parameters, Map<String, String> headers, String nameParameterId)
	            throws ClientServicesException {
	        Object data = null;
	        Response dataHolder = null;
	        String uniqueId = "";
	        if (nameParameterId != null) {
	            uniqueId = parameters.get(nameParameterId);
	            data = findInCache(uniqueId);
	            dataHolder = new Response(data);
	        }
	        if (data == null) {
	            dataHolder = getClientService().get(url, parameters, headers, getDataFormat());
	            //in case of 401 errors  client service returns null
	            if (dataHolder == null) return null;
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
	 */
	//TODO Fix cache with DataHolder object
	public Response retrieveData(String url, Map<String, String> parameters, String nameParameterId) throws ClientServicesException {
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
	 */
	//TODO: Use Args pattern to pass the headers and avoid hard-coding the content-type
	public Response updateData(String serviceUrl, Map<String, String> parameters, Object content,
			String nameParameterId) throws ClientServicesException {
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
	 */
   public Response updateData(String serviceUrl, Map<String, String> parameters, Map<String, String> headers, Object content, String nameParameterId) throws ClientServicesException {
        String uniqueId = "";
        if (nameParameterId != null) {
            uniqueId = parameters.get(nameParameterId);
        }

        if (!headers.containsKey(CONTENT_TYPE)) {
            // TODO shouldn't assume this
        	headers.put(CONTENT_TYPE, APPLICATION_ATOM_XML);
        }
        Response result = getClientService().put(serviceUrl, parameters, headers, content, getDataFormat());
        if (cacheSize > 0 && nameParameterId != null) {
            addDataToCache(uniqueId, content);
        }

        return result;
   }

   public Response putData(String serviceUrl, Map<String, String> parameters, Map<String, String> headers, Object content, String uniqueId) throws ClientServicesException {
       Response result = getClientService().put(serviceUrl, parameters, headers, content, getDataFormat());
       
       if (cacheSize > 0 && uniqueId != null) {
           addDataToCache(uniqueId, content);
       }

       return result;
   }
   
   protected void checkResponseCode(Response response, HTTPCode expectedCode) throws ClientServicesException {
	   if (response != null && response.getResponse() != null && response.getRequest()!=null) {
		   if (response.getResponse().getStatusLine() != null && expectedCode.checkCode(response.getResponse().getStatusLine().getStatusCode())) {
			   return;
		   } else {
			   throw new ClientServicesException(response.getResponse(), response.getRequest());
		   }
	   } else {
		   throw new ClientServicesException(null, "Response is null");
	   }
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
	 * Returns a valid parameters HashMap even if null is passed
	 * 
	 * @param parameters
	 * @return
	 */
	protected Map<String, String> getParameters(Map<String, String> parameters) {
		if(parameters == null) return new HashMap<String, String>();
		else return parameters;
	}
	
	/**
	 * Return true if the id as email address
	 * 
	 * @param id
	 * @return
	 */
	protected boolean isEmail(String id) {
		return (id == null) ? false : id.contains(AT);
	}
	
}
