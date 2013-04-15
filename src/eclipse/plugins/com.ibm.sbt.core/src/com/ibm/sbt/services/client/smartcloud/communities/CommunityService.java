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

package com.ibm.sbt.services.client.smartcloud.communities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.BaseService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.smartcloud.base.BaseEntity;
import com.ibm.sbt.services.client.smartcloud.communities.CommunityServiceException.Reason;
import com.ibm.sbt.services.client.smartcloud.communities.util.XMLCommunityPayloadBuilder;

/**
 * CommunityService can be used to perform operations related to Community.
 * <p>
 * Constructs {@link Community} and {@link Member} objects after parsing the JSON response from Smartcloud server
 * </p>
 * 
 * <pre>
 * Sample Usage
 * {@code
 * 	CommunityService	service	= new CommunityService();
 *  List<Community<Node>> communities = (List)service.getMyCommunities();
 * }
 * </pre>
 * 
 * @author Carlos Manias
 */
public class CommunityService extends BaseService {

	private static final String	sourceClass				= CommunityService.class.getName();
	private static final Logger	logger					= Logger.getLogger(sourceClass);

	// Setting default format to XML. Need to discuss what this should be.
	private static String		DEFAULT_HANDLER_NAME	= "XML";

	// Setting default size to 0. Need to discuss what this should be.
	private static int			DEFAULT_CACHE_SIZE		= 0;

	private static enum CommunitiesAPI {
		GETALLCOMMUNITIES("/communities/service/atom/communities/all"),
		GETCOMMUNITYENTRY("/communities/service/atom/community/instance"),
		GETMYCOMMUNITIES("/communities/service/atom/communities/my"),
		GETCOMMUNITYMEMBERS("/communities/service/atom/community/members"),
		CREATECOMMUNITY("/communities/service/atom/communities/my"),
		DELETECOMMUNITY("/communities/service/atom/community/instance"),
		UPDATECOMMUNITY("/communities/service/atom/community/instance"),
		ADDCOMMUNITYMEMBER("/communities/service/atom/community/members"),
		DELETECOMMUNITYMEMBER("/communities/service/atom/community/members");

		private final String	url;

		CommunitiesAPI(String url) {
			this.url = url;
		}

		public String getUrl() {
			return url;
		}

		@Override
		public String toString() {
			return this.url;
		}
	}

	/**
	 * Default Constructor - Creates CommunityService Object with default endpoint
	 */
	public CommunityService() {
		this("smartcloud", DEFAULT_CACHE_SIZE, DEFAULT_HANDLER_NAME);
	}

	/**
	 * Constructor - Creates CommunityService Object with a specified endpoint
	 * 
	 * @param endpoint
	 *            Creates ProfileService with specified endpoint and a default CacheSize
	 */
	public CommunityService(String endpoint) {
		this(endpoint, DEFAULT_CACHE_SIZE, DEFAULT_HANDLER_NAME);
	}

	/**
	 * Constructor - Creates CommunityService Object with specified endpoint and cache size
	 * 
	 * @param endpoint
	 * @param cacheSize
	 *            Creates ProfileService with specified values of endpoint and CacheSize
	 */
	public CommunityService(String endpoint, int cacheSize) {
		super(endpoint, cacheSize);

	}

	/**
	 * Constructor - Creates CommunityService Object with specified endpoint and data format
	 * 
	 * @param endpoint
	 * @param format
	 */
	public CommunityService(String endpoint, String format) {
		this(endpoint, DEFAULT_CACHE_SIZE, format);
	}

	/**
	 * Constructor - Creates CommunityService Object with specified endpoint, data format and cache size
	 * 
	 * @param endpoint
	 * @param cacheSize
	 * @param format
	 */
	public CommunityService(String endpoint, int cacheSize, String format) {
		super(endpoint, cacheSize, format);

	}

	/**
	 * <p>
	 * Returns a {link @Community} object. If the boolean load parameter is true, the object will contain the data of the community. Otherwise it will only contain the communityUuid.
	 * </p>
	 * 
	 * @param communityUuid
	 *            The community Id
	 * @param load
	 *            Boolean value to define if the returned object must load all the community data
	 * @return A community
	 * @throws CommunityServiceException
	 */
	@SuppressWarnings("unchecked")
	public <DataFormat> Community<DataFormat> getCommunityEntry(String communityUuid, boolean load)
			throws CommunityServiceException {

		Community<DataFormat> community = null;
		try {
			community = super.getSingleEntry(communityUuid, load, Community.class);
		} catch (ClientServicesException e) {
			Reason reason = Reason.SERVER_ERROR;
			if (e.isClientError()) {
				reason = Reason.CLIENT_ERROR;
			}
			if (logger.isLoggable(Level.WARNING)) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			throw new CommunityServiceException(e, StringUtil.format("Error retrieving communities"), reason);
		}
		return community;
	}

	/**
	 * <p>
	 * Returns a List of all the public {link @Community} objects.
	 * </p>
	 * 
	 * @return A list of public communities
	 * @throws CommunityServiceException
	 */
	@SuppressWarnings("unchecked")
	public <DataFormat> Collection<Community<DataFormat>> getAllCommunities()
			throws CommunityServiceException {

		Map<String, String> parameters = new HashMap<String, String>();

		Collection<Community<DataFormat>> communities = null;
		try {
			communities = super.getMultipleEntities(CommunitiesAPI.GETALLCOMMUNITIES.getUrl(), parameters,
					Community.class);
		} catch (ClientServicesException e) {
			Reason reason = Reason.SERVER_ERROR;
			if (e.isClientError()) {
				reason = Reason.CLIENT_ERROR;
			}
			if (logger.isLoggable(Level.WARNING)) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			throw new CommunityServiceException(e, StringUtil.format("Error retrieving communities"), reason);
		}

		return communities;
	}

	/**
	 * <p>
	 * Returns a List of all the {link @Community} objects of the current user.
	 * </p>
	 * 
	 * @return A list of the user's communities
	 * @throws CommunityServiceException
	 */
	@SuppressWarnings("unchecked")
	public <DataFormat> Collection<Community<DataFormat>> getMyCommunities() throws CommunityServiceException {

		Map<String, String> parameters = new HashMap<String, String>();

		Collection<Community<DataFormat>> communities = null;
		try {
			communities = super.getMultipleEntities(CommunitiesAPI.GETMYCOMMUNITIES.getUrl(), parameters,
					Community.class);
		} catch (ClientServicesException e) {
			Reason reason = Reason.SERVER_ERROR;
			if (e.isClientError()) {
				reason = Reason.CLIENT_ERROR;
			}
			if (logger.isLoggable(Level.WARNING)) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			throw new CommunityServiceException(e, StringUtil.format("Error retrieving communities"), reason);
		}

		return communities;
	}

	/**
	 * <p>
	 * Returns a List of all the {link @Member} of the {link @Community} specified by the communityUuid.
	 * </p>
	 * 
	 * @param communityUuid
	 *            The community Id
	 * @return A list of members of the given community
	 * @throws CommunityServiceException
	 */
	@SuppressWarnings("unchecked")
	public <DataFormat> Collection<Member<DataFormat>> getCommunityMembers(String communityUuid)
			throws CommunityServiceException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("communityUuid", communityUuid);

		Collection<Member<DataFormat>> members = null;
		try {
			members = super.getMultipleEntities(CommunitiesAPI.GETCOMMUNITYMEMBERS.getUrl(), parameters,
					Member.class);
		} catch (ClientServicesException e) {
			Reason reason = Reason.SERVER_ERROR;
			if (e.isClientError()) {
				reason = Reason.CLIENT_ERROR;
			}
			if (logger.isLoggable(Level.WARNING)) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			throw new CommunityServiceException(e, StringUtil.format("Error retrieving community members"),
					reason);
		}
		return members;
	}

	/**
	 * Creates a new Community
	 * 
	 * @param communityName
	 *            The name of the community
	 * @param communityDescription
	 *            The description of the community
	 * @throws CommunityServiceException
	 */
	public void createCommunity(String communityName, String communityDescription)
			throws CommunityServiceException {
		XMLCommunityPayloadBuilder builder = XMLCommunityPayloadBuilder.INSTANCE;
		Map<String, String> info = new HashMap<String, String>();
		info.put("title", communityName);
		info.put("content", communityDescription);
		info.put("snx:communityType", "private");
		// Object content = builder.generateCreateCommunityPayload(info);
		Object content = builder.dummyTestPayload();
		Map<String, String> parameters = new HashMap<String, String>();
		try {
			super.createData(CommunitiesAPI.CREATECOMMUNITY.getUrl(), parameters, content, "communityUuid");
		} catch (ClientServicesException e) {
			Reason reason = Reason.SERVER_ERROR;
			if (e.isClientError()) {
				reason = Reason.CLIENT_ERROR;
			}
			if (logger.isLoggable(Level.WARNING)) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			throw new CommunityServiceException(e, StringUtil.format("Error creating community"), reason);
		}

	}

	/**
	 * Updates an existing community
	 * 
	 * @param communityUuid
	 *            The name of the community
	 * @param content
	 *            The content to be updated
	 * @throws CommunityServiceException
	 */
	public void updateCommunity(String communityUuid, Object content) throws CommunityServiceException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("communityUuid", communityUuid);
		try {
			super.updateData(CommunitiesAPI.UPDATECOMMUNITY.getUrl(), parameters, content, "communityUuid");
		} catch (ClientServicesException e) {
			Reason reason = Reason.SERVER_ERROR;
			if (e.isClientError()) {
				reason = Reason.CLIENT_ERROR;
			}
			if (logger.isLoggable(Level.WARNING)) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			throw new CommunityServiceException(e, StringUtil.format("Error updating community"), reason);
		}
	}

	/**
	 * Deletes an existing community
	 * 
	 * @param communityUuid
	 *            The community id
	 * @throws CommunityServiceException
	 */
	public void deleteCommunity(String communityUuid) throws CommunityServiceException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("communityUuid", communityUuid);
		try {
			super.deleteData(CommunitiesAPI.DELETECOMMUNITY.getUrl(), parameters, "communityUuid");
		} catch (ClientServicesException e) {
			Reason reason = Reason.SERVER_ERROR;
			if (e.isClientError()) {
				reason = Reason.CLIENT_ERROR;
			}
			if (logger.isLoggable(Level.WARNING)) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			throw new CommunityServiceException(e, StringUtil.format("Error deleting community"), reason);
		}
	}

	/**
	 * Adds a community member
	 * 
	 * @param communityUuid
	 *            The community id
	 * @param userId
	 *            THe user id
	 * @param role
	 *            The role of the user
	 * @throws CommunityServiceException
	 */
	public void addCommunityMember(String communityUuid, String userId, String role)
			throws CommunityServiceException {
		XMLCommunityPayloadBuilder builder = XMLCommunityPayloadBuilder.INSTANCE;
		Map<String, String> info = new HashMap<String, String>();
		info.put("snx:userid", userId);
		info.put("snx:role", role);
		Object content = builder.generateAddMemberPayload(info);
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("communityUuid", communityUuid);
		try {
			super.createData(CommunitiesAPI.ADDCOMMUNITYMEMBER.getUrl(), parameters, content, "communityUuid");
		} catch (ClientServicesException e) {
			Reason reason = Reason.SERVER_ERROR;
			if (e.isClientError()) {
				reason = Reason.CLIENT_ERROR;
			}
			if (logger.isLoggable(Level.WARNING)) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			throw new CommunityServiceException(e, StringUtil.format("Error adding community member"), reason);
		}
	}

	/**
	 * Deletes a community member
	 * 
	 * @param communityUuid
	 *            The community id
	 * @param userId
	 *            The user id
	 * @throws CommunityServiceException
	 */
	public void deleteCommunityMember(String communityUuid, String userId) throws CommunityServiceException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("communityUuid", communityUuid);
		parameters.put("userId", userId);
		try {
			super.deleteData(CommunitiesAPI.DELETECOMMUNITYMEMBER.getUrl(), parameters, "communityUuid");
		} catch (ClientServicesException e) {
			Reason reason = Reason.SERVER_ERROR;
			if (e.isClientError()) {
				reason = Reason.CLIENT_ERROR;
			}
			if (logger.isLoggable(Level.WARNING)) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			throw new CommunityServiceException(e, StringUtil.format("Error deleting community member"),
					reason);
		}
	}

	/**
	 * Factory method to get an entity instance from the id
	 * 
	 * @param entityName
	 *            The Entity class name
	 * @param uuid
	 *            The id of the entity
	 * @return entity the new entity
	 * @throws CommunityServiceException
	 */
	@Override
	public <DataFormat> BaseEntity<DataFormat> getEntityFromId(String entityName, String uuid)
			throws ClientServicesException {
		if (Community.class.getSimpleName().equalsIgnoreCase(entityName)) {
			return new Community<DataFormat>(uuid, this);
		} else if (Member.class.getSimpleName().equalsIgnoreCase(entityName)) {
			return new Member<DataFormat>(uuid, this);
		}
		throw new ClientServicesException(new IllegalArgumentException(), "Entity decoding not supported"
				+ entityName, Reason.INVALID_INPUT);
	}

	/**
	 * Factory method to get an entity instance from the data
	 * 
	 * @param entityName
	 *            The Entity class name
	 * @param data
	 *            The data of the entity
	 * @return entity the new entity
	 */
	@Override
	public <DataFormat> BaseEntity<DataFormat> getEntityFromData(String entityName, DataFormat data)
			throws ClientServicesException {
		if (Community.class.getSimpleName().equalsIgnoreCase(entityName)) {
			return new Community<DataFormat>(data, this);
		} else if (Member.class.getSimpleName().equalsIgnoreCase(entityName)) {
			return new Member<DataFormat>(data, this);
		}
		throw new ClientServicesException(new IllegalArgumentException(), "Entity decoding not supported"
				+ entityName, Reason.INVALID_INPUT);
	}
}
