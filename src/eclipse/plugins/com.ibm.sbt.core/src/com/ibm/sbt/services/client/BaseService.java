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

package com.ibm.sbt.services.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.sbt.services.client.ClientService.Handler;
import com.ibm.sbt.services.client.smartcloud.base.BaseEntity;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.sbt.services.util.extractor.field.DataExtractor;

public abstract class BaseService {

	public static final int						DEFAULT_CACHE_SIZE		= 0;
	public static final String					DEFAULT_ENDPOINT_NAME	= "connections";
	private static final String					sourceClass				= BaseService.class.getName();
	private static final Logger					logger					= Logger.getLogger(sourceClass);
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
		this.endpoint = EndpointFactory.getEndpoint(endpointName);
		this.cacheSize = cacheSize;
	}

	/**
	 * Constructor
	 * 
	 * @param endpointName
	 * @param cacheSize
	 * @param format
	 */
	public BaseService(String endpointName, int cacheSize, String format) {
		setHandler(format);
		if (StringUtil.isEmpty(endpointName)) {
			endpointName = DEFAULT_ENDPOINT_NAME;
		}
		this.endpoint = EndpointFactory.getEndpoint(endpointName);
		this.cacheSize = cacheSize;
	}

	/**
	 * Constructor
	 * 
	 * @param endpointName
	 * @param format
	 */
	public BaseService(String endpointName, String format) {
		this(endpointName, DEFAULT_CACHE_SIZE, format);
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

	/**
	 * Create data and returns the result
	 * 
	 * @param clientService
	 * @param serviceUrl
	 * @param parameters
	 * @param content
	 * @return the result of the creation operations
	 * @throws ClientServicesException
	 *             when the creation fails, as null/false return may have other meaning here
	 */
	public Object createData(String serviceUrl, Map<String, String> parameters, Object content)
			throws ClientServicesException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "createData");
		}

		Map<String, String> headers = new HashMap<String, String>();
		Object result = getClientService().post(serviceUrl, parameters, headers, content, dataFormat);
		logger.log(Level.FINEST, "Result of data creation", result);

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "createData");
		}
		return result;
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
	 * Returns true if creation completed successfully and false if an error happened
	 * 
	 * @param clientService
	 * @param serviceUrl
	 * @param parameters
	 * @param content
	 * @param nameParameterId
	 * @return
	 * @throws ClientServicesException
	 */
	public boolean createData(String serviceUrl, Map<String, String> parameters, Object content,
			String nameParameterId) throws ClientServicesException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "createData");
		}
		boolean success = false;

		Map<String, String> headers = getHeadersFromDataType();
		getClientService().post(serviceUrl, parameters, headers, content, dataFormat);
		success = true;

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "createData");
		}
		return success;
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
	public boolean deleteData(String serviceUrl, Map<String, String> parameters, String nameParameterId)
			throws ClientServicesException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "deleteData");
		}
		boolean success = false;
		String uniqueId = "";
		if (nameParameterId != null) {
			uniqueId = parameters.get(nameParameterId);
		}

		getClientService().delete(serviceUrl, parameters, dataFormat);
		if (cacheSize > 0 && nameParameterId != null) {
			removeFromCache(uniqueId);
		}
		success = true;

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "deleteData");
		}
		return success;
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
	public Object retrieveData(String url, Map<String, String> parameters) throws ClientServicesException {
		return retrieveData(url, parameters, null);
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
	public Object retrieveData(String url, Map<String, String> parameters, String nameParameterId)
			throws ClientServicesException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "retrieveData");
		}
		Object data = null;
		String uniqueId = "";
		if (nameParameterId != null) {
			uniqueId = parameters.get(nameParameterId);
			data = getDataFromCache(uniqueId);
		}
		if (data == null) {

			data = getClientService().get(url, parameters, dataFormat);

			if (cacheSize > 0 && nameParameterId != null) {
				addDataToCache(uniqueId, data);
			}
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "retrieveData");
		}
		return data;
	}

	/**
	 * Returns true if update completed successfully and false if an error happened
	 * 
	 * @param clientService
	 * @param serviceUrl
	 * @param parameters
	 * @param content
	 * @param nameParameterId
	 * @return
	 * @throws ClientServicesException
	 */
	public boolean updateData(String serviceUrl, Map<String, String> parameters, Object content,
			String nameParameterId) throws ClientServicesException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "updateData");
		}
		boolean success = false;
		String uniqueId = "";
		if (nameParameterId != null) {
			uniqueId = parameters.get(nameParameterId);
		}

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/atom+xml");
		Object result = getClientService().put(serviceUrl, parameters, headers, content, dataFormat);
		logger.log(Level.FINEST, "Result of data update", result);
		if (cacheSize > 0 && nameParameterId != null) {
			addDataToCache(uniqueId, content);
		}
		success = true;

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "updateData");
		}
		return success;
	}

	/**
	 * @param entityName
	 * @param data
	 * @return
	 */
	public <DataFormat> BaseEntity<DataFormat> getEntityFromData(String entityName, DataFormat data)
			throws ClientServicesException {
		return null;
	}

	/**
	 * @param entityName
	 * @param uuid
	 * @return
	 */
	public <DataFormat> BaseEntity<DataFormat> getEntityFromId(String entityName, String uuid)
			throws ClientServicesException {
		return null;
	}

	private Map<String, String> getHeadersFromDataType() {
		Map<String, String> headers = new HashMap<String, String>();
		if (this.dataFormat == ClientService.FORMAT_XML) {
			headers.put("Content-type", "application/atom+xml");
		} else if (this.dataFormat == ClientService.FORMAT_XML) {
			headers.put("Content-type", "application/json");
		}
		return headers;
	}

	private void setHandler(String handlerName) {
		if (handlerName.equalsIgnoreCase("XML")) {
			this.dataFormat = ClientService.FORMAT_XML;
		} else if (handlerName.equalsIgnoreCase("JSON")) {
			this.dataFormat = ClientService.FORMAT_JSON;
		}
	}

	/**
	 * findInCache()
	 * 
	 * @param userId
	 * @return Method to search the cache
	 */
	private Object findInCache(String key) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "findInCache");
		}

		Object data = null;

		if (cache.containsKey(key)) {
			logger.finest("Cache Hit - Object " + key + " found in Cache");
			data = cache.get(key);
		} else {
			// Cache miss
			logger.finest("Cache Miss - Object " + key + " not found in Cache");
		}

		return data;
	}

	/**
	 * Removes from cache the data stored under the given key
	 * 
	 * @param key
	 */
	protected void removeFromCache(String key) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "deleteFromCache");
		}
		cache.remove(key);
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "deleteFromCache");
		}
	}

	/**
	 * Adds to cache the object using the given key
	 * 
	 * @param key
	 * @param content
	 */
	protected void addDataToCache(String key, Object content) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "addDataToCache");
		}
		// Limit the cache size as per options
		// to check if cache is full , remove if full using LRU algorithm
		cache.put(key, content);
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "addDataToCache");
		}
	}

	/**
	 * Gets data from cache using the given key
	 * 
	 * @param key
	 * @return Method to check if the Community is cached. Calls findInCache to find for the community in
	 *         Cache.
	 */
	protected JsonObject getDataFromCache(String key) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getDataFromCache");
		}
		// this should return just the content.
		// should a have a common caching framework for all services
		JsonObject data = null;
		data = (JsonObject) findInCache(key);
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getDataFromCache");
		}
		return data;
	}

	/**
	 * Retrieves multiple generic entities using Generics and Reflection
	 * 
	 * @param <K>
	 * @param url
	 * @param parameters
	 * @param entityClass
	 * @return List of entities
	 * @throws ClientServicesException
	 *             when the communication with the endpoint fails
	 * @throws SBTServiceException
	 *             when the conversion from the retrieved data to the entity fails
	 */
	protected <Entity extends BaseEntity<DataFormat>, DataFormat> List<Entity> getMultipleEntities(
			String url, Map<String, String> parameters, Class<Entity> entityClass)
			throws ClientServicesException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getMultipleEntries");
		}

		@SuppressWarnings("unchecked")
		DataFormat result = (DataFormat) retrieveData(url, parameters);
		DataExtractor<DataFormat> dataExtractor = getNodeExtractorFromData(result);
		Collection<DataFormat> entries = dataExtractor.getEntitiesFromServiceResult(result);
		List<Entity> entities = new ArrayList<Entity>();
		for (DataFormat entry : entries) {
			@SuppressWarnings("unchecked")
			Entity entity = (Entity) getEntityFromData(entityClass.getSimpleName(), entry);
			entities.add(entity);
		}

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getMultipleEntries");
		}
		return entities;
	}

	/**
	 * Retrieves a single generic entity using Generics and Reflection
	 * 
	 * @param id
	 * @param entityClass
	 * @return entity
	 * @throws SBTServiceException
	 *             When the conversion from data to entity fails
	 * @throws ClientServicesException
	 */
	protected <Entity extends BaseEntity<DataFormat>, DataFormat> Entity getSingleEntry(String id,
			boolean load, Class<Entity> entityClass) throws ClientServicesException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getSingleEntry");
		}

		@SuppressWarnings("unchecked")
		Entity entity = (Entity) getEntityFromId(entityClass.getSimpleName(), id);
		if (load) {
			entity.load();
		}

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getSingleEntry");
		}
		return entity;
	}

	protected <DataFormat> DataExtractor<DataFormat> getNodeExtractorFromData(DataFormat result) {
		return BaseEntity.getNodeExtractorFromData(result);
	}
}
