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
package com.ibm.sbt.services.client.smartcloud.profiles;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.sbt.services.client.BaseService;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.SBTServiceException;
import com.ibm.sbt.services.client.connections.files.utils.Messages;
import com.ibm.sbt.services.client.smartcloud.SmartCloudService;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.sbt.util.DataNavigator;

/**
 * ProfileService can be used to perform Profile Related operations. This is a dedicated Service for
 * SmartCloud Profiles.
 * 
 * @Represents SmartCloud ProfileService
 * @author Vimal Dhupar
 */
public class ProfileService extends BaseService {
	Endpoint									endpoint;

	private static HashMap<String, JsonObject>	cache		= new HashMap<String, JsonObject>();
	private static final String					sourceClass	= ProfileService.class.getName();
	private static final Logger					logger		= Logger.getLogger(sourceClass);

	/**
	 * Default Constructor - 0 argument constructor Calls the Constructor of BaseService Class.
	 */
	public ProfileService() {
		super();
		this.endpoint = EndpointFactory.getEndpoint(DEFAULT_ENDPOINT_NAME);
	}

	/**
	 * Constructor - 1 argument constructor
	 * 
	 * @param endpoint
	 *            Creates ProfileService with specified endpoint and a default CacheSize
	 */
	public ProfileService(String endpoint) {
		super(endpoint);
		this.endpoint = EndpointFactory.getEndpoint(endpoint);
	}

	/**
	 * Constructor - 2 argument constructor
	 * 
	 * @param endpoint
	 * @param cacheSize
	 *            Creates ProfileService with specified values of endpoint and CacheSize
	 */
	public ProfileService(String endpoint, int cacheSize) {
		super(endpoint, cacheSize);
		this.endpoint = EndpointFactory.getEndpoint(endpoint);
	}

	/**
	 * getProfile
	 * <p>
	 * This method is used to fetch the Profile of users. This method fetches the Subscriber ID of the logged
	 * in user, and then calls the getProfile Method to load the user's profile.
	 * 
	 * @return Profile
	 * @throws SBTServiceException
	 */
	public Profile getProfile() throws SBTServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getProfile");
		}
		String userId = null;
		Subscriber subscriber = new Subscriber();
		userId = subscriber.getSubscriberId(endpoint);
		if (StringUtil.isEmpty(userId)) {
			throw new IllegalArgumentException(Messages.InvalidValue_1);
		}
		logger.log(Level.FINEST, Messages.ProfileInfo_3 + userId);
		return getProfile(userId, true);
	}

	/**
	 * getProfile
	 * <p>
	 * This method makes the network call to fetch the Profile's of a user based on load parameter being true
	 * / false. This method can be called directly by passing the userId / Subscriber id of the user and a
	 * load argument as true / false
	 * 
	 * @param userId
	 * @param load
	 * @return Profile
	 * @throws SBTServiceException
	 */
	public Profile getProfile(String userId, boolean load) throws SBTServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getProfile", new Object[] { userId, load });
		}
		if (StringUtil.isEmpty(userId)) {
			throw new IllegalArgumentException(Messages.InvalidArgument_6);
		}
		Profile profile = new Profile(userId, this);
		if (load) {
			load(profile); // fetches profile content from server and populates content of data member
		}
		return profile;
	}

	/**
	 * load()
	 * <p>
	 * Method responsible for loading the profile.
	 * 
	 * @param profile
	 * @throws SBTServiceException
	 */
	public void load(Profile profile) throws SBTServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "load", new Object[] { profile });
		}
		JsonObject data = getProfileDataFromCache(profile.getReqId());
		if (data != null) {
			profile.setData(data);
		} else {
			SmartCloudService svc = new SmartCloudService(this.endpoint);
			Map<String, String> parameters = new HashMap<String, String>();
			Object result = null;
			try {
				result = svc.get(ProfilesAPIMap.GETPROFILE.getUrl(), parameters, ClientService.FORMAT_JSON);
			} catch (ClientServicesException e) {
				throw new ProfileServiceException(e);
			}

			logger.log(Level.FINEST, Messages.ProfileInfo_5);
			profile.setData((JsonObject) result);
			if (cacheSize > 0) {
				logger.log(Level.FINEST, Messages.ProfileInfo_4);
				addProfileDataToCache(profile.getUniqueId(), (JsonObject) result);
			}
		}
	}

	/**
	 * getProfileDataFromCache()
	 * <p>
	 * Method to check if the Profile is cached. Calls findInCache to find for the profile in Cache.
	 * 
	 * @param userId
	 * @return JsonObject
	 */
	private JsonObject getProfileDataFromCache(String userId) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getProfileDataFromCache", new Object[] { userId });
		}
		if (StringUtil.isEmpty(userId)) {
			throw new IllegalArgumentException(Messages.InvalidArgument_6);
		}
		JsonObject data = null;
		if (isEmail(userId)) {
			data = findInCache(userId);
		} else {
			data = cache.get(userId);
		}
		return data;
	}

	/**
	 * addProfileDataToCache() Method to cache the Profile of the User.
	 * 
	 * @param userId
	 * @param content
	 */
	private void addProfileDataToCache(String userId, JsonObject content) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "addProfileDataToCache", new Object[] { userId, content });
		}
		cache.put(userId, content);
	}

	/**
	 * findInCache()
	 * <p>
	 * Method to search the cache
	 * 
	 * @param userId
	 * @return JsonObject
	 */
	private JsonObject findInCache(String userId) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "findInCache", new Object[] { userId });
		}
		String key;
		Set<String> keys = cache.keySet();
		Iterator<String> itr = keys.iterator();
		String email = "";
		while (itr.hasNext()) {
			key = itr.next();
			JsonObject data = cache.get(key);
			// check if email in profile object is same as input userId

			// getting email from Profile object
			DataNavigator.Json nav = new DataNavigator.Json(data); // this.data has the response feed.
			DataNavigator entry = nav.get("entry");
			email = entry.stringValue("emailAddress");

			// cache hit
			if (StringUtil.equalsIgnoreCase(email, userId)) {
				logger.finest(Messages.ProfileInfo_1 + email);
				return data;
			}
		}
		// Cache miss
		logger.finest(Messages.ProfileInfo_2 + email);
		return null;
	}

	/**
	 * isEmail()
	 * <p>
	 * Checking if the userid is email. Current check is based on finding @ in the userid.
	 * 
	 * @param userId
	 * @return boolean
	 */
	private boolean isEmail(String userId) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "isEmail", new Object[] { userId });
		}
		return userId.contains("@");
	}

}
