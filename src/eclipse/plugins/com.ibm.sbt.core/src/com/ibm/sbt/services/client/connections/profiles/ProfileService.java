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

package com.ibm.sbt.services.client.connections.profiles;

import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.BaseService;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.ClientService.Handler;
import com.ibm.sbt.services.client.connections.profiles.exception.ProfileServiceException;
import com.ibm.sbt.services.client.connections.profiles.utils.Messages;
import com.ibm.sbt.services.util.AuthUtil;

/**
 * ProfileService can be used to perform operations related to Profiles. 
 * 
 * @Represents Connections ProfileService
 * @author Swati Singh
 * <pre>
 * Sample Usage
 * {@code
 * 	ProfileService _service = new ProfileService();
 *  Profile profile = _service.getProfile(userId);
 * }
 * </pre>
 */
public class ProfileService extends BaseService {

	static final String						sourceClass		= ProfileService.class.getName();
	static final Logger						logger			= Logger.getLogger(sourceClass);
	public static final String				ProfileBaseUrl	= "profiles";
	public static final String				seperator		= "/";
	private LRUCache lruCache;

	/**
	 * Constructor Creates ProfileService Object with default endpoint and default cache size
	 */
	
	public ProfileService() {
		this(DEFAULT_ENDPOINT_NAME, DEFAULT_CACHE_SIZE);
		initializeCache(DEFAULT_CACHE_SIZE);
	}

	/**
	 * Constructor 
	 * 
	 * @param endpoint
	 *            Creates ProfileService with specified endpoint and a default CacheSize
	 */

	public ProfileService(String endpoint) {
		this(endpoint, DEFAULT_CACHE_SIZE);
		initializeCache(DEFAULT_CACHE_SIZE);
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
		initializeCache(cacheSize);
	}
	/**
	 * Initializes the LRU cache with given size. 
	 * 
	 * @param cacheSize
	 */
	
	private void initializeCache(int cacheSize){
		lruCache = new LRUCache(cacheSize);
	}

	/**
	 * Returns profile's of multiple user's. 
	 * 
	 * @param userIds
	 * @return Profile[]
	 * @throws ProfileServiceException
	 */
	private Profile[] getProfiles(String[] userIds) throws ProfileServiceException{
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getProfiles", userIds);
		}
		Profile[] profiles = new Profile[userIds.length];
		int i = 0;
		if (userIds != null) {
			for (String userId : userIds) {
				if (userId != null) {
					profiles[i] = getProfile(userId);
					i++;
				} else // elementary NP handling. Setting the profile null;
				{
					profiles[i] = null;
					i++;
				}
			}
		}
		if (logger.isLoggable(Level.FINEST)) {
			String log = "call to retrive profiles successful";
			if (profiles != null) {
				for (Profile profile : profiles) {
					if (null == profile) {
						log = "Empty response from server for one of the requested profiles";
						break;
					}
				}
				log = "";
			}
			logger.exiting(sourceClass, "getProfiles", log);
		}
		return profiles;
	}
	
	/**
	 * Wrapper method to get profile of a user
	 * <p>
	 * fetches profile content from server and populates the data member of {@link Profile} with the fetched content 
	 *
	 * @param userId
	 *			   unique identifier of User , it can either be email or id
	 * @return Profile
	 * @throws ProfileServiceException
	 */
	public Profile getProfile(String userId) throws ProfileServiceException{
		return getProfile(userId, true);
	}

	/**
	 * Wrapper method to get profile of a user, it makes the network call to fetch the Profile's of a user based on load parameter
	 * being true/false.
	 *
	 * @param userId
	 *             unique identifier of User , it can either be email or id 				
	 * @param loaded
	 * 			    if true, fetches profile content from server and populates the data member of {@link Profile} with the fetched content 
	 * @return Profile
	 * @throws ProfileServiceException
	 */
	public Profile getProfile(String userId, boolean loaded) throws ProfileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getProfile", new Object[] { userId, loaded });
		}
		if (StringUtil.isEmpty(userId)) {
			throw new IllegalArgumentException(Messages.InvalidArgument_1);
		}
		Profile profile = new Profile(this, userId);
		if (loaded) {
			load(profile);
		}
		if (logger.isLoggable(Level.FINEST)) {
			String log = "";
			if (profile.getId() != null) {
				log = Messages.GetProfileInfo_1;
			} else {
				log = Messages.GetProfileInfo_2;
			}
			logger.exiting(sourceClass, "getProfile", log);
		}
		return profile;
	}
	/**
	 * Wrapper method to search profiles based on different parameters
	 * 
	 * @param parameters 
	 * 				  list of query string parameters to pass to API
	 * @return Collection<Profile>
	 */
	public Collection<Profile> searchProfiles( Map<String, String> parameters){
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "searchProfiles", parameters);
		}
		Document data = null;
		Collection<Profile> profiles = null;
		if (null == parameters) {
			parameters = new HashMap<String, String>();
		}
		try {
			String url = resolveProfileUrl(ProfileEntity.NONADMIN.getProfileEntityType(),
					ProfileType.SEARCH.getProfileType());
			data = (Document)getClientService().get(url, parameters);
			profiles = Converter.returnProfileEntries(this, data);
		} catch (ClientServicesException e) {
			if (logger.isLoggable(Level.SEVERE)) {
				logger.log(Level.SEVERE, Messages.ProfileServiceException_1 + "searchProfiles()", e);
			}
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getColleagues");
		}
		return profiles;
	}

	/**
	 * Wrapper method to get user's network contacts
	 * 
	 * @param profile
	 * 				profile of the user whose contacts are to be returned
	 * @return Profile[] 
	 * @throws ProfileServiceException
	 */
	public Profile[] getColleagues(Profile profile) throws ProfileServiceException{
		return getColleagues(profile,null);
	}
	
	/**
	 * Wrapper method to get user's colleagues
	 * 
	 * @param profile
	 * 				profile of the user whose contacts are to be returned
	 * @param parameters 
	 * 				list of query string parameters to pass to API
	 * @return Profile[] 
	 * @throws ProfileServiceException
	 */
	public Profile[] getColleagues(Profile profile, Map<String, String> parameters)
	throws ProfileServiceException{
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getColleagues", parameters);
		}
		if (profile == null) {
			throw new IllegalArgumentException(StringUtil.format("A null profile was passed"));
		}
		Document data = null;
		Profile[] colleagues = null;
		if(parameters == null)
			parameters = new HashMap<String, String>();
		String url = resolveProfileUrl(ProfileEntity.NONADMIN.getProfileEntityType(),
				ProfileType.CONNECTIONS.getProfileType());
		if (isEmail(profile.getReqId())) {
			parameters.put(ProfileRequestParams.EMAIL, profile.getReqId());
		} else {
			parameters.put(ProfileRequestParams.USERID, profile.getReqId());
		}

		if(parameters.get("connectionType") != null)// this is to remove any other values put in by sample user in following
			parameters.remove("connectionType");	// mandatory parameters
		if(parameters.get("outputType") != null)
			parameters.remove("outputType");
		parameters.put("connectionType","colleague");
		parameters.put("outputType","profile");

		data = executeGet(url, parameters, ClientService.FORMAT_XML);
		colleagues = Converter.convertToProfiles(this, data);

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getColleagues");
		}
		return colleagues;

	}
	/**
	 * Wrapper method to get check if two users are colleagues
	 * 
	 * @param sourceId 
	 * 				 userid or email of first user
	 * @param targetId 
	 * 				 userid or email of second user
	 * @param parameters 
	 * 				list of query string parameters to pass to API
	 * @return ConnectionEntry 
	 * @throws ProfileServiceException
	 */
	private ConnectionEntry checkColleague(String sourceId, String targetId, Map<String, String> parameters) throws ProfileServiceException{
		
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "checkColleague");
		}
		if (StringUtil.isEmpty(sourceId)) {
			throw new IllegalArgumentException(Messages.InvalidArgument_4);
		}
		if (StringUtil.isEmpty(targetId)) {
			throw new IllegalArgumentException(Messages.InvalidArgument_5);
		}
		Document data = null;
		if(parameters == null){
			parameters = new HashMap<String, String>();
		}
		String url = resolveProfileUrl(ProfileEntity.NONADMIN.getProfileEntityType(),
				ProfileType.CONNECTION.getProfileType());
		if (isEmail(sourceId)) {
			parameters.put(ProfileRequestParams.SOURCEEMAIL, sourceId);
		} else {
			parameters.put(ProfileRequestParams.SOURCEUSERID, sourceId);
		}
		if (isEmail(targetId)) {
			parameters.put(ProfileRequestParams.TARGETEMAIL, targetId);
		} else {
			parameters.put(ProfileRequestParams.TARGETUSERID, targetId);
		}
		parameters.put("connectionType","colleague");

		data = executeGet(url, parameters, ClientService.FORMAT_XML);
		ConnectionEntry connection = Converter.returnConnectionEntries(data, "connectionEntry").iterator().next();

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getColleagues");
		}
		return connection;
		
	}
	
	/**
	 * Wrapper method to get  common colleagues of two users
	 * 
	 * @param sourceId 
	 * 				 userid or email of first user
	 * @param targetId 
	 * 				 userid or email of second user
	 * @return Collection<ConnectionEntry>
	 * @throws ProfileServiceException
	 */
	private Collection<ConnectionEntry> getColleaguesInCommon(String sourceId, String targetId,  Map<String, String> parameters) throws ProfileServiceException{
		
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "checkColleague");
		}
		if (StringUtil.isEmpty(sourceId)) {
			throw new IllegalArgumentException(Messages.InvalidArgument_4);
		}
		if (StringUtil.isEmpty(targetId)) {
			throw new IllegalArgumentException(Messages.InvalidArgument_5);
		}
		Document data = null;
		if(parameters == null){
			parameters = new HashMap<String, String>();
		}
		String url = resolveProfileUrl(ProfileEntity.NONADMIN.getProfileEntityType(),
				ProfileType.CONNECTIONS_IN_COMMON.getProfileType());
		if (isEmail(sourceId)) {
			StringBuilder value =  new StringBuilder(sourceId);
			value = value.append(",").append(targetId);
			parameters.put(ProfileRequestParams.EMAIL, value.toString());
		} else {

			StringBuilder value =  new StringBuilder(sourceId);
			value = value.append(",").append(targetId);
			parameters.put(ProfileRequestParams.USERID, value.toString());
		}
		parameters.put("connectionType","colleague");

		data = executeGet(url, parameters, ClientService.FORMAT_XML);
		Collection<ConnectionEntry> colleaguesInCommon = Converter.returnConnectionEntries(data, "connectionEntry");

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getColleagues");
		}
		return colleaguesInCommon;
		
	}

	/**
	 * Wrapper method to get a user's report to chain
	 * 
	 * @param id 
	 * 		   unique identifier of the user whose direct reports are needed, it can be email or userID
	 * @param parameters 
	 * 				list of query string parameters to pass to API
	 * @return Collection<Profile>
	 * @throws ProfileServiceException	
	 */
	public Collection<Profile> getReportToChain (String id, Map<String, String> parameters)throws ProfileServiceException{
		
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getReportToChain", parameters);
		}
		if(parameters == null)
			parameters = new HashMap<String, String>();
		String url = resolveProfileUrl(ProfileEntity.NONADMIN.getProfileEntityType(),
				ProfileType.REPORTINGCHAIN.getProfileType());
		
		Document data = executeGet(url, parameters, ClientService.FORMAT_XML);
		Collection<Profile> reportingChain = Converter.returnProfileEntries(this, data);

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getReportToChain");
		}
		return reportingChain;
		
	}
	
	/**
	 * Wrapper method to get a person's direct reports
	 * 
	 * @param id
	 * 		   unique identifier of the user whose direct reports are needed, it can be email or userID
	 * @param parameters
	 * 		   list of query string parameters to pass to API
	 * @return Collection<Profile>
	 * @throws ProfileServiceException
	 * 
	 */
	public Collection<Profile> getDirectReports(String id, Map<String, String> parameters)throws ProfileServiceException{
		
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getDirectReports", parameters);
		}
		if(parameters == null)
			parameters = new HashMap<String, String>();

		String url = resolveProfileUrl(ProfileEntity.NONADMIN.getProfileEntityType(),
				ProfileType.DIRECTREPORTS.getProfileType());
	
		Document data = executeGet(url, parameters, ClientService.FORMAT_XML);
		Collection<Profile> reportingChain = Converter.returnProfileEntries(this, data);

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getDirectReports");
		}
		return reportingChain;
		
	}
	/**
	 * Wrapper method to get a list of pending invites for a user 
	 * 
	 * @param parameters
	 * 				  list of query string parameters to pass to API
	 * @return Collection<ConnectionEntry>
	 * @throws ProfileServiceException
	 * 
	 */
	private Collection<ConnectionEntry> getMyInvites(Map<String, String> parameters)throws ProfileServiceException{
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getMyInvites", parameters);
		}

		Document data = null;
		Collection<ConnectionEntry> invites = null;
		String url = resolveProfileUrl(ProfileEntity.NONADMIN.getProfileEntityType(),
				ProfileType.CONNECTIONS.getProfileType());
		data = executeGet(url, parameters, ClientService.FORMAT_XML);
		if(parameters.containsKey("outputType")){
			if(parameters.get("outputType").equalsIgnoreCase("profile")){
				invites = Converter.returnConnectionEntries(data, "profile");
			}
			else 
				invites = Converter.returnConnectionEntries(data, "connection");
		}
		else{
			invites = Converter.returnConnectionEntries(data, "connection");
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getMyInvites");
		}
		return invites;
	}
	
	/**
	 * Wrapper method to send Invite to a user to become colleague
	 * <p>
	 * a default Invite message is used while sending the invite
	 *  
	 * @param profile 
	 * 			   profile of the user to whom the invite is to be sent
	 * @return boolean 
	 *  		   if invite is sent successfully then value is true else value is false
	 * @throws ProfileServiceException
	 */
	public boolean sendInvite(Profile profile)throws ProfileServiceException{
		String defaultInviteMsg = Messages.SendInviteMsg;
		return sendInvite(profile, defaultInviteMsg);
		
	}
	
	/**
	 * Wrapper method to send Invite to a user to become colleague
	 * 
	 * @param profile 
	 *				profile of the user to whom the invite is to be sent
	 * @param inviteMsg 
	 * 				Invite message to the other user
	 * @return boolean
	 * 				if invite is sent successfully then return true
	 * @throws ProfileServiceException
	 */
	public boolean sendInvite(Profile profile, String inviteMsg)throws ProfileServiceException{
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getColleagues", inviteMsg);
		}
		if (profile == null) {
			throw new IllegalArgumentException(StringUtil.format("A null profile was passed"));
		}
		Map<String, String> parameters = new HashMap<String, String>();
		String url = resolveProfileUrl(ProfileEntity.NONADMIN.getProfileEntityType(),
				ProfileType.CONNECTIONS.getProfileType());
		if (isEmail(profile.getReqId())) {
			parameters.put(ProfileRequestParams.EMAIL,profile.getReqId());
		} else {
			parameters.put(ProfileRequestParams.USERID, profile.getReqId());
		}
		parameters.put("connectionType","colleague");
		XMLProfilesPayloadBuilder builder = XMLProfilesPayloadBuilder.INSTANCE;
		Object content = builder.generateInviteRequestPayload(inviteMsg);
		//getClientService().post(url, parameters, content);
		boolean result = executePost(url, parameters, null, content, null);
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getColleagues");
		}
		return result;

	}

	/**
	 * Wrapper method to accept a Invite 
	 * 
	 * @param connectionId 
	 * 					 unique id of the connection 
	 * @param title 
	 * 			 message to the other user
	 * @param content
	 * 			message to the other user
	 * @return boolean 
	 * 		       if invite is accepted then return true
	 * @throws ProfileServiceException
	 * 
	 */
	public boolean acceptInvite(String connectionId, String title, String content)throws ProfileServiceException{
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "acceptInvite", connectionId);
		}
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(ProfileRequestParams.CONNECTIONID, connectionId);
		String url = resolveProfileUrl(ProfileEntity.NONADMIN.getProfileEntityType(),
				ProfileType.CONNECTION.getProfileType());
		
		XMLProfilesPayloadBuilder builder = XMLProfilesPayloadBuilder.INSTANCE;
		Object payload = builder.generateAcceptInvitePayload(connectionId, title, content);
		boolean	result = executePut(url, parameters, null, payload, null);
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "acceptInvite");
		}
		return result;
	}
	
	/**
	 * Wrapper method is used to delete/ignore a Invite 
	 * 
	 * @param connectionId 
	 * 					unique id of the connection
	 * @return boolean
	 * 				returns true if invite is deleted successfully 
	 * @throws ProfileServiceException
	 */
	public boolean deleteInvite(String connectionId)throws ProfileServiceException{
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "deleteInvite", connectionId);
		}
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(ProfileRequestParams.CONNECTIONID, connectionId);
		String url = resolveProfileUrl(ProfileEntity.NONADMIN.getProfileEntityType(),
				ProfileType.CONNECTION.getProfileType());
		boolean result = executeDelete(url, parameters);
		
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "deleteInvite");
		}
		return result;
	}
	
	/**
	 * Wrapper method to update a User's profile photo
	 * 
	 * @param Profile
	 * @return boolean
	 * 				returns true, if profile photo is updated
	 * @throws ProfileServiceException
	 */
	public boolean updateProfilePhoto(Profile profile) throws ProfileServiceException{
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "updatePhoto", profile.toString());
		}
		if (profile == null) {
			throw new IllegalArgumentException(Messages.InvalidArgument_3);
		}
		boolean returnVal = true;

		Map<String, String> parameters = new HashMap<String, String>();
		if (isEmail(profile.getReqId())) {
			parameters.put(ProfileRequestParams.EMAIL, profile.getReqId());
		} else {
			parameters.put(ProfileRequestParams.USERID, profile.getReqId());
		}
		String filePath = profile.getFieldsMap().get("imageLocation");
		java.io.File file = new java.io.File(filePath);
		String name = filePath.substring(filePath.lastIndexOf('\\') + 1);

		int dot = name.lastIndexOf('.');
		String ext = null;
		if (dot > -1) {
			ext = name.substring(dot + 1); // add one for the dot!
		}
		if (StringUtil.isEmpty(ext)) {
			try {
				throw new Exception(Messages.UpdateProfileInfo_1);
			} catch (Exception e) {
				returnVal = false;
			}
		}
		Map<String, String> headers = new HashMap<String, String>();
		if (ext.equalsIgnoreCase("jpg")) {
			headers.put(Headers.ContentType, "image/jpeg");	// content-type should be image/jpeg for file extension - jpeg/jpg
		} else {
			headers.put(Headers.ContentType, "image/" + ext);
		}
		String url = resolveProfileUrl(ProfileEntity.NONADMIN.getProfileEntityType(),
				ProfileType.UPDATEPROFILEPHOTO.getProfileType());
		returnVal = executePut(url, parameters, headers, file, ClientService.FORMAT_NULL);

		profile.clearFieldsMap();
		removeProfileDataFromCache(profile.getReqId());
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "updatePhoto" + returnVal);
		}
		return returnVal;
	}

	/**
	 * Wrapper method to update a User's profile
	 * 
	 * @param Profile
	 * @return boolean
	 * 				returns true if profile is updated successfully
	 * @throws ProfileServiceException
	 */
	public boolean updateProfile(Profile profile) throws ProfileServiceException{
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "update", profile);
		}
		if (profile == null) {
			throw new IllegalArgumentException(Messages.InvalidArgument_3);
		}
		boolean result = true;

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(ProfileRequestParams.OUTPUT, "vcard");
		parameters.put(ProfileRequestParams.FORMAT, "full");
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(Headers.ContentType, Headers.ATOM);
		if (isEmail(profile.getReqId())) {
			parameters.put(ProfileRequestParams.EMAIL, profile.getReqId());
		} else {
			parameters.put(ProfileRequestParams.USERID, profile.getReqId());
		}
		Object updateProfilePayload = profile.constructUpdateRequestBody();
		String url = resolveProfileUrl(ProfileEntity.NONADMIN.getProfileEntityType(),
				ProfileType.UPDATEPROFILE.getProfileType());
		result = executePut(url, parameters, headers, updateProfilePayload, ClientService.FORMAT_NULL);
		profile.clearFieldsMap();
		removeProfileDataFromCache(profile.getReqId());

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "update");
		}

		return result;
	}

	/*
	 * Method to remove the user profile from cache.
	 * 
	 * @param userId
	 */
	protected void removeProfileDataFromCache(String userId) throws ProfileServiceException{
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "removeProfileDataFromCache", userId);
		}
		if (isEmail(userId)) {
			String key;
			Set<String> keys = lruCache.getKeySet();
			Iterator<String> itr = keys.iterator();
			while (itr.hasNext()) {
				key = itr.next();
				Document data = lruCache.get(key);
				// check if email in profile object is same as input userId
				String email = "";
				try {
					email = DOMUtil.value(data, Profile.xpathMap.get("email"));
				} catch (XMLException e) {
					continue;
				}

				// cache hit
				if (StringUtil.equalsIgnoreCase(email, userId)) {
					lruCache.remove(key);
				}
			}
			// Cache miss

		} else {
			lruCache.remove(userId);
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "removeProfileDataFromCache");
		}

	}

	/*
	 * Method responsible for loading the profile.
	 * 
	 * @param profile
	 */
	protected void load(Profile profile) throws ProfileServiceException{
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "load", profile);
		}
		// Do a cache lookup first. If cache miss, make a network call to get
		// Profile
		
		Document data = getProfileDataFromCache(profile.getReqId());
		if (data != null) {
			profile.setData(data);
		} else {

			Map<String, String> parameters = new HashMap<String, String>();
			if (isEmail(profile.getReqId())) {
				parameters.put("email", profile.getReqId());
			} else {
				parameters.put("userid", profile.getReqId());
			}
			String url = resolveProfileUrl(ProfileEntity.NONADMIN.getProfileEntityType(),
					ProfileType.GETPROFILE.getProfileType());
			Object result = executeGet(url, parameters, ClientService.FORMAT_XML);

			if (result != null) {
				profile.setData((Document) result);
				addProfileDataToCache(profile.getUniqueId(), (Document) result);
			} else {
				profile.setData(null);
			}

			if (logger.isLoggable(Level.FINEST)) {
				logger.exiting(sourceClass, "load");
			}
		}
	}

	/*
	 * Method to check if the Profile is cached. Calls findInCache to find for the profile in Cache.
	 * 
	 * @param userId
	 * @return Document
	 */
	private Document getProfileDataFromCache(String userId) {
		// this should return just the content ..xmldoc
		// should a have a common caching framework for all services
		Document data = null;
		if (isEmail(userId)) {
			data = findInCache(userId);
		} else {
			if(lruCache.hasKey(userId)){
				data = lruCache.get(userId);
			}
		}
		return data;
	}

	/*
	 * addProfileDataToCache() Method to cache the Profile of the User.
	 * 
	 * @param userId
	 * @param content
	 */
	private void addProfileDataToCache(String userId, Document content) {
	
		lruCache.put(userId, content);
	}

	/*
	 * Method to search the cache
	 * 
	 * @param userId
	 * @return Document
	 */
	private Document findInCache(String userId) {
		String key;
		Set<String> keys = lruCache.getKeySet();
		Iterator<String> itr = keys.iterator();
		while (itr.hasNext()) {
			key = itr.next();
			Document data = lruCache.get(key);
			// check if email in profile object is same as input userId
			String email = "";
			try {
				email = DOMUtil.value(data, Profile.xpathMap.get("email"), Profile.nameSpaceCtx);
			} catch (XMLException e) {
				continue;
			}
			// cache hit
			if (StringUtil.equalsIgnoreCase(email, userId)) {
				return data;
			}
		}
		// Cache miss
		return null;
	}

	/**
	 * Method responsible for generating appropriate REST URLs
	 * 
	 * @param ProfileEntity ( Ref Class : ProfileEntity )
	 * @param ProfileType ( Ref Class : ProfileType )
	 *
	 * @return String
	 */
	public String resolveProfileUrl(String profileEntity, String profileType) {
		return resolveProfileUrl(profileEntity, profileType, null);
	}
	
	/**
	 * Method responsible for generating appropriate REST URLs
	 * 
	 * @param ProfileEntity ( Ref Class : ProfileEntity )
	 * @param ProfileType ( Ref Class : ProfileType )
	 * @param params : ( Ref Class : ProfileParams )
	 * @return String
	 */
	public String resolveProfileUrl(String profileEntity, String profileType, Map<String, String> params) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "resolveCommunityUrl", profileEntity + profileType);
		}

		StringBuilder proBaseUrl = new StringBuilder(ProfileBaseUrl);
		if (StringUtil.isEmpty(profileEntity)) {
			profileEntity = ProfileEntity.NONADMIN.getProfileEntityType(); // Default
																			// Entity
																			// Type
		}
		if (StringUtil.isEmpty(profileType)) {
			profileType = ProfileType.GETPROFILE.getProfileType(); // Default
																	// Profile
																	// Type
		}

		if (AuthUtil.INSTANCE.getAuthValue(endpoint).equalsIgnoreCase("oauth")) {
			if (profileEntity.equalsIgnoreCase(ProfileEntity.NONADMIN.getProfileEntityType())) {
				proBaseUrl.append(seperator).append("oauth");
			}
		}
		if (profileEntity.equalsIgnoreCase("")) {// if it is non admin API then
													// no need to append anythin
			proBaseUrl.append(seperator).append(profileType);
		} else {
			proBaseUrl.append(profileEntity).append(seperator).append(profileType);

		}

		// Add required parameters
		if (null != params) {
			if (params.size() > 0) {
				proBaseUrl.append("?");
				boolean setSeperator = false;
				for (Map.Entry<String, String> param : params.entrySet()) {
					if (setSeperator) {
						proBaseUrl.append("&");
					}
					String paramvalue = "";
					try {
						paramvalue = URLEncoder.encode(param.getValue(), "UTF-8");
					} catch (UnsupportedEncodingException e) {}
					proBaseUrl.append(param.getKey() + "=" + paramvalue);
					setSeperator = true;
				}
			}
		}

		if (logger.isLoggable(Level.FINEST)) {
			logger.log(Level.FINEST, Messages.ProfileInfo_7 + proBaseUrl.toString());
		}
		return proBaseUrl.toString();
	}
	
	/**
	 * executeGet
	 * 
	 * @param uri
	 *           api to be executed.
	 * @param params
	 *           Map of Parameters. See {@link ProfileRequestParams} for possible values.
	 * @throws ProfileServiceException
	 */
	public Document executeGet(String uri, Map<String, String> params, Handler format)
	throws ProfileServiceException{
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "executeGet", new Object[] { uri, params });
		}
		Document data = null;
		try {
			data = (Document) getClientService().get(uri, params);
		} catch (ClientServicesException e) {
			if (logger.isLoggable(Level.SEVERE)) {
				logger.log(Level.SEVERE, Messages.ProfileServiceException_1 + "executeGet()", e);
			}
			throw new ProfileServiceException(e);
		}
		if (data == null) {
			return null;
		}
		return data;
	}

	/**
	 * executeDelete
	 * 
	 * @param uri
	 *           api to be executed.
	 * @param params
	 *            Map of Parameters. See {@link ProfileRequestParams} for possible values.
	 * @return boolean
	 * 			  returns true if request is successful	
	 * @throws ProfileServiceException
	 */
	public boolean executeDelete(String uri, Map<String, String> params)
	throws ProfileServiceException{
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "executeDelete", new Object[] { uri, params });
		}
		boolean result = true;
		try {
			getClientService().delete(uri, params);
		} catch (ClientServicesException e) {
			result = false;
			if (logger.isLoggable(Level.SEVERE)) {
				logger.log(Level.SEVERE, Messages.ProfileServiceException_1 + "executeDelete()", e);
			}
			throw new ProfileServiceException(e);
		}
		
		return result;
	}
	
	/**
	 * executePut
	 * 
	 * @param requestUri
	 *            	  api to be executed.
	 * @param params
	 *            Map of Parameters. See {@link ProfileRequestParams} for possible values.
	 * @param headers
	 *            Map of Headers. See {@link Headers} for possible values.
	 * @param payload
	 *            Document which is passed directly as requestBody to the execute request.
	 * @return boolean
	 * 			  returns true if request is successful
	 * @throws ProfileServiceException
	 */
	public boolean executePut(String requestUri, Map<String, String> parameters,
			Map<String, String> headers, Object payload, Handler format) throws ProfileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "executePut");
		}
		boolean success = true;
		try {
			getClientService().put(requestUri, parameters, headers, payload, format);
		} catch (ClientServicesException e) {
			success = false;
			logger.log(Level.SEVERE, Messages.ProfileServiceException_1 + "executePut()", e);
			throw new ProfileServiceException(e);
		}
		return success;
	}
	
	/**
	 * executePost
	 * 
	 * @param requestUri
	 *               api to be executed.
	 * @param params
	 *             Map of Parameters. See {@link ProfileRequestParams} for possible values.
	 * @param headers
	 *             Map of Headers. See {@link Headers} for possible values.
	 * @param payload
	 *             Document which is passed directly as requestBody to the execute request. 
	 * @return boolean
	 * 			   returns true if request is successful
	 * @throws ProfileServiceException
	 */
	public boolean executePost(String requestUri, Map<String, String> parameters,
			Map<String, String> headers, Object payload, Handler format) throws ProfileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "executePut");
		}
		boolean success = true;
		try {
			getClientService().post(requestUri, parameters, headers, payload, format);
		} catch (ClientServicesException e) {
			success = false;
			logger.log(Level.SEVERE, Messages.ProfileServiceException_1 + "executePost()", e);
			throw new ProfileServiceException(e);
		}
		return success;
	}

	/*
	 * Method to check if the userid is email
	 * <p>
	 * Current check is based on finding @ in the userid.
	 * 
	 * @param userId
	 * @return boolean
	 */
	protected boolean isEmail(String userId) {
		if (StringUtil.isEmpty(userId)) {
			return false;
		}
		return userId.contains("@");
	}

}