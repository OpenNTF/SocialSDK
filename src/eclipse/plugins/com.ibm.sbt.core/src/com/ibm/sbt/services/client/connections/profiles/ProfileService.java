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

package com.ibm.sbt.services.client.connections.profiles;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.profiles.feedhandlers.ConnectionEntryFeedHandler;
import com.ibm.sbt.services.client.connections.profiles.feedhandlers.ProfileFeedHandler;
import com.ibm.sbt.services.client.connections.profiles.feedhandlers.ProfileTagFeedHandler;
import com.ibm.sbt.services.client.connections.profiles.transformers.ConnectionEntryTransformer;
import com.ibm.sbt.services.client.connections.profiles.transformers.ProfileTransformer;
import com.ibm.sbt.services.client.connections.profiles.utils.Messages;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.JsonDataHandler;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.util.AuthUtil;
/**
 * ProfileService can be used to perform operations related to Profiles. 
 * 
 * @Represents Connections ProfileService
 * @author Swati Singh
 * @author Carlos Manias
 * <pre>
 * Sample Usage
 * {@code
 * 	ProfileService _service = new ProfileService();
 *  Profile profile = _service.getProfile(userId);
 * }
 * </pre>
 */
public class ProfileService extends BaseService {


	private static final String				seperator				= "/";

	/**
	 * Used in constructing REST APIs
	 */
	public static final String				ProfileBaseUrl	= "{profiles}";

	/**
	 * Constructor Creates ProfileService Object with default endpoint and default cache size
	 */
	public ProfileService() {
		this(DEFAULT_ENDPOINT_NAME, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Constructor  - Creates ProfileService Object with a specified endpoint
	 * 
	 * @param endpoint
	 *            Creates ProfileService with specified endpoint and a default CacheSize
	 */

	public ProfileService(String endpoint) {
		this(endpoint, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Constructor - Creates ProfileService Object with specified endpoint and CacheSize
	 * 
	 * @param endpoint
	 * @param cacheSize
	 *            Creates ProfileService with specified endpoint and CacheSize
	 */

	public ProfileService(String endpoint, int cacheSize) {
		super(endpoint, cacheSize);
	}
	
	/**
	 * Wrapper method to get profile of a user
	 *
	 * @param id
	 *             unique identifier of User , it can either be email or id 				
	 * @return Profile
	 * @throws ProfileServiceException
	 */
	public Profile getProfile(String id) throws ProfileServiceException{
		return getProfile(id, null);
	}
	/**
	 * Wrapper method to get profile of a user
	 *
	 * @param id
	 *             unique identifier of User , it can either be email or id 	
	 * @param parameters          			
	 * @return Profile
	 * @throws ProfileServiceException
	 */
	public Profile getProfile(String id, Map<String, String> parameters) throws ProfileServiceException{
		// TODO: Do a cache lookup first. If cache miss, make a network call to get profile

		if (StringUtil.isEmpty(id)){
			throw new ProfileServiceException(null, Messages.InvalidArgument_1);
		}
		Profile profile;
		if(parameters == null){
			parameters = new HashMap<String, String>();
		}
		setIdParameter(parameters, id);
		String url = resolveProfileUrl(ProfileAPI.NONADMIN.getProfileEntityType(),ProfileType.GETPROFILE.getProfileType());

		try {
			profile = (Profile)getEntity(url, parameters, new ProfileFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.ProfileException, id);
		} catch (IOException e) {
			throw new ProfileServiceException(e, Messages.ProfileException, id);
		}

		return profile;
	}

	/**
	 * Wrapper method to search profiles based on different parameters
	 * 
	 * @param parameters 
	 * 				  list of query string parameters to pass to API
	 * @return list of searched Profiles
	 */
	public ProfileList searchProfiles( Map<String, String> parameters) throws ProfileServiceException {
		if (null == parameters) {
			parameters = new HashMap<String, String>();
		}
		String url = resolveProfileUrl(ProfileAPI.NONADMIN.getProfileEntityType(),
				ProfileType.SEARCH.getProfileType());

		ProfileList profiles = null;
		try {
			profiles = (ProfileList) getEntities(url, parameters, new ProfileFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.SearchException);
		} catch (IOException e) {
			throw new ProfileServiceException(e, Messages.SearchException);
		}

		return profiles;
	}

	/**
	 * Wrapper method to get user's tags
	 * 
	 * @param id 
	 * 		   unique identifier of the user whose colleagues are required, it can be email or userKey
	 * @return ProfileTagList
	 * @throws ProfileServiceException
	 */
	public ProfileTagList getTags(String id) throws ProfileServiceException{
		return getTags(id,null);
	}
	/**
	 * Wrapper method to get user's tags
	 * 
	 * @param id 
	 * 		   unique identifier of the user whose colleagues are required, it can be email or userKey
	 * @param parameters 
	 * 				list of query string parameters to pass to API
	 * @return ProfileTagList 
	 * @throws ProfileServiceException
	 */
	public ProfileTagList getTags(String id, Map<String, String> parameters)
	throws ProfileServiceException{

		if (StringUtil.isEmpty(id)){
			throw new ProfileServiceException(null, Messages.InvalidArgument_1);
		}

		if(parameters == null)
			parameters = new HashMap<String, String>();
		String url = resolveProfileUrl(ProfileAPI.NONADMIN.getProfileEntityType(),
				ProfileType.TAGS.getProfileType());
		if(id.contains("@"))
			parameters.put("targetEmail", id);
		else
			parameters.put("targetKey", id);
		ProfileTagList tags = null;
		try {
			tags = (ProfileTagList) getEntities(url, parameters, new ProfileTagFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.TagsException, id);
		} catch (IOException e) {
			throw new ProfileServiceException(e, Messages.TagsException, id);
		}

		return tags;
	}
	/**
	 * Wrapper method to get user's colleagues
	 * 
	 * @param id 
	 * 		   unique identifier of the user whose colleagues are required, it can be email or userID
	 * @return list of User's colleagues profiles
	 * @throws ProfileServiceException
	 */
	public EntityList getColleagues(String id) throws ProfileServiceException{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("outputType", "connection");
		return getColleagues(id,parameters);
	}

	/**
	 * Wrapper method to get user's colleagues
	 * 
	 * @param id 
	 * 		   unique identifier of the user whose colleagues are required, it can be email or userID
	 * @param parameters 
	 * 				list of query string parameters to pass to API
	 * @return Profiles of User's colleagues  
	 * @throws ProfileServiceException
	 */
	public EntityList getColleagues(String id, Map<String, String> parameters)
	throws ProfileServiceException{

		if (StringUtil.isEmpty(id)){
			throw new ProfileServiceException(null, Messages.InvalidArgument_1);
		}

		if(parameters == null)
			parameters = new HashMap<String, String>();
		String url = resolveProfileUrl(ProfileAPI.NONADMIN.getProfileEntityType(),
				ProfileType.CONNECTIONS.getProfileType());
		setIdParameter(parameters, id);
		parameters.put("connectionType","colleague");
		EntityList entities = null;
		try {
			if(StringUtil.isNotEmpty(parameters.get("outputType"))){
				if(StringUtil.equalsIgnoreCase(parameters.get("outputType"), "profile")){
					entities = getEntities(url, parameters, new ProfileFeedHandler(this));
				}
				else{
					entities = getEntities(url, parameters, new ConnectionEntryFeedHandler(this));
				}
			}
			else{
				entities = getEntities(url, parameters, new ConnectionEntryFeedHandler(this));
			}

		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.ColleaguesException, id);
		} catch (IOException e) {
			throw new ProfileServiceException(e, Messages.ColleaguesException, id);
		}

		return entities;
	}

	/**
	 * Wrapper method to get check if two users are colleagues
	 * 
	 * @param sourceId 
	 * 				 userid or email of first user
	 * @param targetId 
	 * 				 userid or email of second user
	 * @return ConnectionEntry 
	 * @throws ProfileServiceException
	 */
	public ConnectionEntry checkColleague(String sourceId, String targetId) throws ProfileServiceException{
		return checkColleague(sourceId, targetId, null);
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
	public ConnectionEntry checkColleague(String sourceId, String targetId, Map<String, String> parameters) throws ProfileServiceException{

		if (StringUtil.isEmpty(sourceId)) {
			throw new ProfileServiceException(null, Messages.InvalidArgument_4);
		}
		if (StringUtil.isEmpty(targetId)) {
			throw new ProfileServiceException(null, Messages.InvalidArgument_5);
		}
		if(parameters == null){
			parameters = new HashMap<String, String>();
		}
		String url = resolveProfileUrl(ProfileAPI.NONADMIN.getProfileEntityType(),
				ProfileType.CONNECTION.getProfileType());
		if (isEmail(sourceId)) {
			parameters.put(ProfilesConstants.SOURCEEMAIL, sourceId);
		} else {
			parameters.put(ProfilesConstants.SOURCEUSERID, sourceId);
		}
		if (isEmail(targetId)) {
			parameters.put(ProfilesConstants.TARGETEMAIL, targetId);
		} else {
			parameters.put(ProfilesConstants.TARGETUSERID, targetId);
		}
		parameters.put("connectionType","colleague");

		ConnectionEntry connectionEntry;
		try {
			connectionEntry = (ConnectionEntry)getEntity(url, parameters, new ConnectionEntryFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.CheckColleaguesException);
		} catch (IOException e) {
			throw new ProfileServiceException(e, Messages.CheckColleaguesException);
		}

		return connectionEntry;

	}

	/**
	 * Wrapper method to get common colleagues of two users
	 * 
	 * @param sourceId 
	 * 				 userid or email of first user
	 * @param targetId 
	 * 				 userid or email of second user
	 * @return list of common colleagues profiles
	 * @throws ProfileServiceException
	 */
	public EntityList getCommonColleagues(String sourceId, String targetId) throws ProfileServiceException{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("outputType", "connection");
		return getCommonColleagues(sourceId, targetId, parameters);
	}

	/**
	 * Wrapper method to get common colleagues of two users
	 * 
	 * @param sourceId 
	 * 				 userid or email of first user
	 * @param targetId 
	 * 				 userid or email of second user
	 * @return list of common colleagues profiles
	 * @throws ProfileServiceException
	 */
	public EntityList getCommonColleagues(String sourceId, String targetId,  Map<String, String> parameters) throws ProfileServiceException{

		if (StringUtil.isEmpty(sourceId)) {
			throw new ProfileServiceException(null, Messages.InvalidArgument_4);
		}
		if (StringUtil.isEmpty(targetId)) {
			throw new ProfileServiceException(null, Messages.InvalidArgument_5);
		}
		if(parameters == null){
			parameters = new HashMap<String, String>();
		}
		String url = resolveProfileUrl(ProfileAPI.NONADMIN.getProfileEntityType(),
				ProfileType.CONNECTIONS_IN_COMMON.getProfileType());
		if (isEmail(sourceId)) {
			StringBuilder value =  new StringBuilder(sourceId);
			value = value.append(",").append(targetId);
			parameters.put(ProfilesConstants.EMAIL, value.toString());
		} else {

			StringBuilder value =  new StringBuilder(sourceId);
			value = value.append(",").append(targetId);
			parameters.put(ProfilesConstants.USERID, value.toString());
		}
		parameters.put("connectionType","colleague");

		EntityList entities = null;
		try {
			if(StringUtil.isNotEmpty(parameters.get("outputType"))){
				if(StringUtil.equalsIgnoreCase(parameters.get("outputType"), "profile")){
					entities = getEntities(url, parameters, new ProfileFeedHandler(this));
				}
				else{
					entities = getEntities(url, parameters, new ConnectionEntryFeedHandler(this));
				}
			}
			else{
				entities = getEntities(url, parameters, new ConnectionEntryFeedHandler(this));
			}

		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.CommonColleaguesException, sourceId, targetId );
		} catch (IOException e) {
			throw new ProfileServiceException(e, Messages.CommonColleaguesException, sourceId, targetId);
		}

		return entities;
	}

	/**
	 * Wrapper method to get a user's report to chain
	 * 
	 * @param id 
	 * 		   unique identifier of the user whose report to chain is required, it can be email or userID
	 * @return List of Profiles 
	 * @throws ProfileServiceException
	 */
	public ProfileList getReportingChain(String id) throws ProfileServiceException{
		return getReportingChain(id,null);
	}
	/**
	 * Wrapper method to get a user's report to chain
	 * 
	 * @param id 
	 * 		   unique identifier of the user whose report to chain is required, it can be email or userID
	 * @param parameters 
	 * 				list of query string parameters to pass to API
	 * @return List of Profiles
	 * @throws ProfileServiceException	
	 */
	public ProfileList getReportingChain (String id, Map<String, String> parameters)throws ProfileServiceException{

		if (StringUtil.isEmpty(id)) {
			throw new ProfileServiceException(null, Messages.InvalidArgument_1);
		}
		if(parameters == null)
			parameters = new HashMap<String, String>();
		setIdParameter(parameters, id);
		String url = resolveProfileUrl(ProfileAPI.NONADMIN.getProfileEntityType(),
				ProfileType.REPORTINGCHAIN.getProfileType());

		ProfileList profiles = null;
		try {
			profiles = (ProfileList) getEntities(url, parameters, new ProfileFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.ReportingChainException, id);
		} catch (IOException e) {
			throw new ProfileServiceException(e, Messages.ReportingChainException, id);
		}

		return profiles;

	}

	/**
	 * Wrapper method to get a person's people managed
	 * 
	 * @param id 
	 * 		   unique identifier of the user whose direct reports are required, it can be email or userID
	 * @return List of Profiles 
	 * @throws ProfileServiceException
	 */
	public ProfileList getPeopleManaged(String id) throws ProfileServiceException{
		return getPeopleManaged(id,null);
	}
	/**
	 * Wrapper method to get a person's people managed
	 * 
	 * @param id
	 * 		   unique identifier of the user whose direct reports are required, it can be email or userID
	 * @param parameters
	 * 		   list of query string parameters to pass to API
	 * @return List of Profiles
	 * @throws ProfileServiceException
	 * 
	 */
	public ProfileList getPeopleManaged(String id, Map<String, String> parameters)throws ProfileServiceException{

		if (StringUtil.isEmpty(id)) {
			throw new ProfileServiceException(null, Messages.InvalidArgument_1);
		}
		if(parameters == null)
			parameters = new HashMap<String, String>();
		setIdParameter(parameters, id);
		String url = resolveProfileUrl(ProfileAPI.NONADMIN.getProfileEntityType(),
				ProfileType.DIRECTREPORTS.getProfileType());

		ProfileList profiles = null;
		try {
			profiles = (ProfileList) getEntities(url, parameters, new ProfileFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.DirectReportsException, id);
		} catch (IOException e) {
			throw new ProfileServiceException(e, Messages.DirectReportsException, id);
		}
		return profiles;
	}

	/**
	 * Wrapper method to send Invite to a user to become colleague
	 * <p>
	 * a default Invite message is used while sending the invite
	 *  
	 * @param id
	 * 		   unique identifier of the user to whom the invite is to be sent, it can be email or userID
	 * @throws ProfileServiceException
	 */
	public void sendInvite(String id)throws ProfileServiceException{
		String defaultInviteMsg = Messages.SendInviteMsg;
		sendInvite(id, defaultInviteMsg);

	}

	/**
	 * Wrapper method to send Invite to a user to become colleague
	 * 
	 * @param id
	 * 		   unique identifier of the user to whom the invite is to be sent, it can be email or userID
	 * @param inviteMsg 
	 * 				Invite message to the other user
	 * @return value is true if invite is sent successfully else value is false
	 * @throws ProfileServiceException
	 */
	public void sendInvite(String id, String inviteMsg)throws ProfileServiceException{
		if (StringUtil.isEmpty(id)) {
			throw new ProfileServiceException(null, Messages.InvalidArgument_1);
		}

		try {
			Map<String, String> parameters = new HashMap<String, String>();
			String url = resolveProfileUrl(ProfileAPI.NONADMIN.getProfileEntityType(),
					ProfileType.CONNECTIONS.getProfileType());
			setIdParameter(parameters, id);
			parameters.put("connectionType","colleague");
			Object payload = constructSendInviteRequestBody(inviteMsg);
			super.createData(url, parameters, payload);
		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.SendInviteException, id);
		} catch (TransformerException e) {
			throw new ProfileServiceException(e, Messages.SendInvitePayloadException);
		} catch (IOException e) {
			throw new ProfileServiceException(e, Messages.SendInviteException, id);
		}

	}


	/**
	 * Wrapper method to accept a Invite 
	 * 
	 * @param ConnectionEntry 
	 * @throws ProfileServiceException
	 * 
	 */
	public void acceptInvite(ConnectionEntry connection)throws ProfileServiceException{

		if (connection == null) {
			throw new ProfileServiceException(null, Messages.InvalidArgument_6);
		}
		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(ProfilesConstants.CONNECTIONID, connection.getConnectionId());
			String url = resolveProfileUrl(ProfileAPI.NONADMIN.getProfileEntityType(),
					ProfileType.CONNECTION.getProfileType());
			Object payload = constructAcceptInviteRequestBody(connection, "accepted");

			super.updateData(url, parameters,payload, connection.getConnectionId());
		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.AcceptInviteException, connection.getConnectionId());
		} catch (TransformerException e) {
			throw new ProfileServiceException(e, Messages.AcceptInvitePayloadException);
		} catch (IOException e) {
			throw new ProfileServiceException(e, Messages.AcceptInviteException, connection.getConnectionId());
		}
	}

	/**
	 * Wrapper method is used to delete/ignore a Invite 
	 * 
	 * @param connectionId 
	 * 					unique id of the connection
	 * @throws ProfileServiceException
	 */
	public void deleteInvite(String connectionId)throws ProfileServiceException{

		if (StringUtil.isEmpty(connectionId)) {
			throw new ProfileServiceException(null, Messages.InvalidArgument_2);
		}

		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(ProfilesConstants.CONNECTIONID, connectionId);
			String url = resolveProfileUrl(ProfileAPI.NONADMIN.getProfileEntityType(),
					ProfileType.CONNECTION.getProfileType());
			super.deleteData(url, parameters, connectionId);
		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.DeleteInviteException, connectionId);
		} catch (IOException e) {
			throw new ProfileServiceException(e, Messages.DeleteInviteException, connectionId);
		}
	}

	/**
	 * Wrapper method to update a User's profile
	 * 
	 * @param Profile
	 * @throws ProfileServiceException
	 */
	public void updateProfile(Profile profile) throws ProfileServiceException{

		if (profile == null) {
			throw new ProfileServiceException(null, Messages.InvalidArgument_3);
		}
		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(ProfilesConstants.OUTPUT, "vcard");
			parameters.put(ProfilesConstants.FORMAT, "full");
			setIdParameter(parameters, profile.getUserid());
			Object updateProfilePayload;
			try {
				updateProfilePayload = constructUpdateRequestBody(profile);
			} catch (TransformerException e) {
				throw new ProfileServiceException(e);
			}
			String updateUrl = resolveProfileUrl(ProfileAPI.NONADMIN.getProfileEntityType(),
					ProfileType.UPDATEPROFILE.getProfileType());
			super.updateData(updateUrl, parameters,updateProfilePayload, getUniqueIdentifier(profile.getAsString("uid")));
			profile.clearFieldsMap();
		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.UpdateProfileException);
		} catch (IOException e) {
			throw new ProfileServiceException(e, Messages.UpdateProfileException);
		}

	}
	
	public String getMyUserId()throws ProfileServiceException{
		String id = "";
		String peopleApiUrl ="/connections/opensocial/basic/rest/people/@me/";
		try {
			Response feed = getClientService().get(peopleApiUrl);
			JsonDataHandler dataHandler = new JsonDataHandler((JsonJavaObject)feed.getData());
			id = dataHandler.getAsString("entry/id");
			id = id.substring(id.lastIndexOf(':')+1, id.length());
		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.ProfileException, id);
		}
		return id;
		
	}
	public Profile getMyProfile() throws ProfileServiceException{
		return getProfile(getMyUserId(), null);
	}

	
	/**
	 * Wrapper method to update a User's profile photo
	 * 
	 * @param File
	 * 			image to be uploaded as profile photo
	 * @param userid
	 * @throws ProfileServiceException
	 */
	public void updateProfilePhoto(File file, String userId) throws ProfileServiceException{

		try {
			Map<String, String> parameters = new HashMap<String, String>();
			setIdParameter(parameters, userId);
			String name = file.getName();
			int dot = StringUtil.lastIndexOfIgnoreCase(name, ".");
			String ext = "";
			if (dot > -1) {
				ext = name.substring(dot + 1); // add one for the dot!
			}
			if (!StringUtil.isEmpty(ext)) {
				Map<String, String> headers = new HashMap<String, String>();
				if (StringUtil.equalsIgnoreCase(ext,"jpg")) {
					headers.put(ProfilesConstants.REQ_HEADER_CONTENT_TYPE_PARAM, "image/jpeg");	// content-type should be image/jpeg for file extension - jpeg/jpg
				} else {
					headers.put(ProfilesConstants.REQ_HEADER_CONTENT_TYPE_PARAM, "image/" + ext);
				}
				String url = resolveProfileUrl(ProfileAPI.NONADMIN.getProfileEntityType(),
						ProfileType.UPDATEPROFILEPHOTO.getProfileType());
				getClientService().put(url, parameters, headers, file, ClientService.FORMAT_NULL);
				
			}
		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.UpdateProfilePhotoException);
		}
	
	}

	/*
	 * This method is used by ProfileService wrapper methods to construct request body for Add operations
	 * @return Object
	 */
	protected Object constructCreateRequestBody(Profile profile) throws TransformerException {
		ProfileTransformer transformer = new ProfileTransformer(profile);
		String xml = transformer.createTransform(profile.getFieldsMap());
		return xml;	
	}

	/*
	 * This method is used by ProfileService wrapper methods to construct request body for Update operations
	 * @return Object
	 */
	protected Object constructUpdateRequestBody(Profile profile) throws TransformerException {
		ProfileTransformer transformer = new ProfileTransformer(profile);
		String xml = transformer.updateTransform(profile.getFieldsMap());
		return xml;	
	}

	/*
	 * This method is used by ProfileService wrapper methods to construct request body for Add operations
	 * @return Object
	 */
	protected Object constructAcceptInviteRequestBody(ConnectionEntry connectionEntry, String action) throws TransformerException {
		ConnectionEntryTransformer transformer = new ConnectionEntryTransformer(connectionEntry);
		String xml = "";
		if(!StringUtil.isEmpty(action)){
			xml = transformer.updateTransform(action, connectionEntry.getFieldsMap());
		}
		return xml;	
	}

	/*
	 * This method is used by ProfileService wrapper methods to construct request body for Add operations
	 * @return Object
	 * @throws ProfileServiceException 
	 */
	protected Object constructSendInviteRequestBody(String inviteMsg) throws TransformerException, ProfileServiceException {
		ConnectionEntry connectionEntry = new ConnectionEntry(this, null);
		connectionEntry.setContent(inviteMsg);
		ConnectionEntryTransformer transformer = new ConnectionEntryTransformer(connectionEntry);
		String xml = transformer.createTransform(connectionEntry.getFieldsMap());
		return xml;	
	}

	/*
	 * Method responsible for generating appropriate REST URLs
	 * 
	 * @param ProfileEntity ( Ref Class : ProfileEntity )
	 * @param ProfileType ( Ref Class : ProfileType )
	 *
	 * @return String
	 */
	protected String resolveProfileUrl(String profileEntity, String profileType) {
		return resolveProfileUrl(profileEntity, profileType, null);
	}

	/*
	 * Method responsible for generating appropriate REST URL
	 * 
	 * @param ProfileEntity ( Ref Class : ProfileEntity )
	 * @param ProfileType ( Ref Class : ProfileType )
	 * @param params : ( Ref Class : ProfileParams )
	 * @return String
	 */
	protected String resolveProfileUrl(String profileEntity, String profileType, Map<String, String> params) {

		StringBuilder proBaseUrl = new StringBuilder(ProfileBaseUrl);
		if (StringUtil.isEmpty(profileEntity)) {
			profileEntity = ProfileAPI.NONADMIN.getProfileEntityType(); // Default
			// Entity
			// Type
		}
		if (StringUtil.isEmpty(profileType)) {
			profileType = ProfileType.GETPROFILE.getProfileType(); // Default
			// Profile
			// Type
		}
		if (AuthUtil.INSTANCE.getAuthValue(endpoint).equalsIgnoreCase("oauth")) {
			if (profileEntity.equalsIgnoreCase(ProfileAPI.NONADMIN.getProfileEntityType())) {
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

		return proBaseUrl.toString();
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

	protected void setIdParameter(Map<String, String>parameters, String id){
		if (isEmail(id)) {
			parameters.put(ProfilesConstants.EMAIL, id);
		} else {
			parameters.put(ProfilesConstants.USERID, id);
		}
	}

	protected String getUniqueIdentifier(String id){
		if (isEmail(id)) {
			return ProfilesConstants.EMAIL;
		} else {
			return ProfilesConstants.USERID;
		}
	}


}