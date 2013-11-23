/*
 * © Copyright IBM Corp. 2013
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.smartcloud.profiles.feedhandler.ProfileFeedHandler;
import com.ibm.sbt.services.client.smartcloud.profiles.util.Messages;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * ProfileService can be used to perform Profile Related operations. This is a dedicated Service for
 * SmartCloud Profiles.
 * 
 * @Represents SmartCloud ProfileService
 * @author Vimal Dhupar
 */
public class ProfileService extends BaseService {
	
	public static final String	SMARTCLOUD_DEFAULT_ENDPOINT_NAME	= "smartcloud";
	
	private final ProfileFeedHandler profileFeedHandler	= new ProfileFeedHandler(this);

	/**
	 * Default Constructor - 0 argument constructor Calls the Constructor of BaseService Class.
	 */
	public ProfileService() {
		this(SMARTCLOUD_DEFAULT_ENDPOINT_NAME, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Constructor
	 * 
	 * @param endpoint
	 *            Creates ProfileService with specified endpoint and a default CacheSize
	 */
	public ProfileService(String endpoint) {
		this(endpoint, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Constructor
	 * 
	 * @param endpoint
	 * @param cacheSize
	 *            Creates ProfileService with specified endpoint and CacheSize
	 */
	public ProfileService(String endpoint, int cacheSize) {
		super(endpoint, cacheSize);
	}
		
	/**
	 * Constructor
	 * 
	 * @param endpoint
	 *            Creates ProfileService with specified endpoint and a default CacheSize
	 */
	public ProfileService(Endpoint endpoint) {
		this(endpoint, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Constructor
	 * 
	 * @param endpoint
	 * @param cacheSize
	 *            Creates ProfileService with specified endpoint and CacheSize
	 */
	public ProfileService(Endpoint endpoint, int cacheSize) {
		super(endpoint, cacheSize);
	}
		
	/**
	 * getProfile
	 * <p>
	 * This method is used to fetch the Profile of users. This method fetches the Subscriber ID of the logged
	 * in user, and then calls the getProfile Method to load the user's profile.
	 * 
	 * @return Profile - returns the fetched Profile
	 * @throws ProfileServiceException
	 */
	public Profile getMyProfile() throws ProfileServiceException {
		JsonObject result = null;
		try {
			//making a call directly here using ClientService's get method,  
			Response response = endpoint.getClientService().get(ProfileConstants.GETUSERIDENTITY.getUrl(), ClientService.FORMAT_JSON);
			result = (JsonObject) response.getData() ;
		} catch (ClientServicesException cse) {
			throw new ProfileServiceException(cse, Messages.InvalidValue_1);
		}
		String userId = (String) result.getJsonProperty("subscriberid");
		if (StringUtil.isEmpty(userId)) {
			throw new ProfileServiceException(null, Messages.InvalidValue_2);
		}
		return getProfile(userId);
	}
	
	/**
	 * getProfile
	 * <p>
	 * Method to fetch the profile of the user.
	 * @param userId
	 * @return Profile
	 * @throws ProfileServiceException
	 */
	public Profile getProfile(String userId) throws ProfileServiceException {
		if (StringUtil.isEmpty(userId)) {
			throw new ProfileServiceException(null, Messages.InvalidValue_3);
		}
		String serviceUrl = ProfileConstants.GETPROFILEUSINGUSERGUID.getUrl(userId);
		try {
			return (Profile) getEntity(serviceUrl, null, profileFeedHandler);
		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.ProfileError_1);
		} catch (IOException e) {
			throw new ProfileServiceException(e, Messages.ProfileError_1);
		}
	}
	
	/**
	 * getContact
	 * <p>
	 * Fetches the contact information of the user based on the input GUID.
	 * 
	 * @param userId
	 *            contact Guid of the profile to be fetched
	 * @return Profile
	 * @throws ProfileServiceException
	 */
	public Profile getContact(String userGUId) throws ProfileServiceException {
		if (StringUtil.isEmpty(userGUId)) {
			throw new ProfileServiceException(null, Messages.InvalidValue_3);
		}
		String serviceUrl = ProfileConstants.GETCONTACTBYCONTACTGUID.getUrl(userGUId);
		try {
			return (Profile) getEntity(serviceUrl, null, profileFeedHandler);
		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.ProfileError_1);
		} catch (IOException e) {
			throw new ProfileServiceException(e, Messages.ProfileError_1);
		}
	}
	
	/**
	 * getContactByGUID
	 * <p>
	 * Fetches the contact information of the user based on the input GUID.
	 * 
	 * @param userId
	 *            contact Guid of the profile to be fetched
	 * @return Profile
	 * @throws ProfileServiceException
	 * @deprecated Use getContact instead
	 */
	public Profile getContactByGUID(String userGUId) throws ProfileServiceException {
		return getContact(userGUId);
	}

	/**
	 * getMyContacts
	 * <p>
	 * Fetches User's Contacts
	 * 
	 * @return ProfileList
	 * @throws ProfileServiceException
	 */
	public ProfileList getMyContacts() throws ProfileServiceException {
		String serviceUrl = ProfileConstants.GETMYCONTACTS.getUrl();
		try {
			return (ProfileList) getEntities(serviceUrl, null, profileFeedHandler);
		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.ProfileError_2);
		} catch (IOException e) {
			throw new ProfileServiceException(e, Messages.ProfileError_2);
		}
	}

	/**
	 * getMyContactsByIndex
	 * <p>
	 * Fetches User's Contacts based on the index provided ie fetches the next "count" no of contacts,
	 * starting from the starting index "startIndex" specified as input.
	 * 
	 * @param startIndex
	 * @param count
	 * @return ProfileList
	 * @throws ProfileServiceException
	 */
	public ProfileList getMyContactsByIndex(int startIndex, int count) throws ProfileServiceException {
		String serviceUrl = ProfileConstants.GETMYCONTACTS.getUrl();
		Integer startI = new Integer(startIndex);
		Integer countI = new Integer(count);
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("startIndex", startI.toString());
		paramsMap.put("count", countI.toString());
		try {
			return (ProfileList) getEntities(serviceUrl, null, profileFeedHandler);
		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.ProfileError_2);
		} catch (IOException e) {
			throw new ProfileServiceException(e, Messages.ProfileError_2);
		}
	}

	/**
	 * Fetches the Connections of the User
	 * 
	 * @return ProfileList
	 * @throws ProfileServiceException
	 */
	public ProfileList getMyConnections() throws ProfileServiceException {
		String serviceUrl = ProfileConstants.GETMYCONNECTIONS.getUrl();
		try {
			return (ProfileList) getEntities(serviceUrl, null, profileFeedHandler);
		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.ProfileError_3);
		} catch (IOException e) {
			throw new ProfileServiceException(e, Messages.ProfileError_3);
		}
	}
}
