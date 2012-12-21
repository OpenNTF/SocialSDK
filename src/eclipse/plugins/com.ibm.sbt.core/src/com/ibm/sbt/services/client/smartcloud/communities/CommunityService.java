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
import com.ibm.sbt.services.client.BaseService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.smartcloud.base.BaseEntity;
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
 * 	CommunityService service = new CommunityService();
 * 	List<Community<Node>> communities = (List) service.getUpdatesFromMyNetwork();
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
	 */
	public <DataFormat> Community<DataFormat> getCommunityEntry(String communityUuid, boolean load) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getCommunityEntry");
		}

		@SuppressWarnings("unchecked")
		Community<DataFormat> community = null;
		try {
			community = super.getSingleEntry(communityUuid, load, Community.class);
		} catch (Exception e) {
			// TODO add service specific checked exception and relative handling in examples.
			e.printStackTrace();
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getCommunityEntry", community);
		}
		return community;
	}

	/**
	 * <p>
	 * Returns a List of all the public {link @Community} objects.
	 * </p>
	 * 
	 * @return A list of public communities
	 */
	public <DataFormat> Collection<Community<DataFormat>> getAllCommunities() {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getAllCommunities");
		}

		Map<String, String> parameters = new HashMap<String, String>();

		@SuppressWarnings("unchecked")
		Collection<Community<DataFormat>> communities = null;
		try {
			communities = super.getMultipleEntities(CommunitiesAPI.GETALLCOMMUNITIES.getUrl(), parameters,
					Community.class);
		} catch (Exception e) {
			// TODO add service specific checked exception and relative handling in examples.
			e.printStackTrace();
		}

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getAllCommunities", communities);
		}
		return communities;
	}

	/**
	 * <p>
	 * Returns a List of all the {link @Community} objects of the current user.
	 * </p>
	 * 
	 * @return A list of the user's communities
	 */
	public <DataFormat> Collection<Community<DataFormat>> getMyCommunities() {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getMyCommunities");
		}

		Map<String, String> parameters = new HashMap<String, String>();

		@SuppressWarnings("unchecked")
		Collection<Community<DataFormat>> communities = null;
		try {
			communities = super.getMultipleEntities(CommunitiesAPI.GETMYCOMMUNITIES.getUrl(), parameters,
					Community.class);
		} catch (Exception e) {
			// TODO add service specific checked exception and relative handling in examples.
			e.printStackTrace();
		}

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getMyCommunities", communities);
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
	 * @return A list of members of the given
	 */
	public <DataFormat> Collection<Member<DataFormat>> getCommunityMembers(String communityUuid) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getCommunityMembers");
		}
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("communityUuid", communityUuid);
		@SuppressWarnings("unchecked")
		Collection<Member<DataFormat>> members = null;
		try {
			members = super.getMultipleEntities(CommunitiesAPI.GETCOMMUNITYMEMBERS.getUrl(), parameters,
					Member.class);
		} catch (Exception e) {
			// TODO add service specific checked exception and relative handling in examples.
			e.printStackTrace();
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getCommunityMembers", members);
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
	 * @return success
	 */
	public boolean createCommunity(String communityName, String communityDescription) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "createCommunity", new Object[] { communityName,
					communityDescription });
		}
		XMLCommunityPayloadBuilder builder = XMLCommunityPayloadBuilder.INSTANCE;
		Map<String, String> info = new HashMap<String, String>();
		info.put("title", communityName);
		info.put("content", communityDescription);
		info.put("snx:communityType", "private");
		// Object content = builder.generateCreateCommunityPayload(info);
		Object content = builder.dummyTestPayload();
		Map<String, String> parameters = new HashMap<String, String>();
		boolean success = false;
		try {
			success = super.createData(CommunitiesAPI.CREATECOMMUNITY.getUrl(), parameters, content,
					"communityUuid");
		} catch (Exception e) {
			// TODO add service specific checked exception and relative handling in examples.
			e.printStackTrace();
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "createCommunity", success);
		}
		return success;
	}

	/**
	 * Updates an existing community
	 * 
	 * @param communityUuid
	 *            The name of the community
	 * @param content
	 *            The content to be updated
	 * @return success
	 */
	public boolean updateCommunity(String communityUuid, Object content) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "updateCommunity", new Object[] { communityUuid, content });
		}
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("communityUuid", communityUuid);
		boolean success = false;
		try {
			success = super.updateData(CommunitiesAPI.UPDATECOMMUNITY.getUrl(), parameters, content,
					"communityUuid");
		} catch (Exception e) {
			// TODO add service specific checked exception and relative handling in examples.
			e.printStackTrace();
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "updateCommunity", success);
		}
		return success;
	}

	/**
	 * Deletes an existing community
	 * 
	 * @param communityUuid
	 *            The community id
	 * @return success
	 */
	public boolean deleteCommunity(String communityUuid) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "deleteCommunity", new Object[] { communityUuid });
		}
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("communityUuid", communityUuid);
		boolean success = false;
		try {
			success = super.deleteData(CommunitiesAPI.DELETECOMMUNITY.getUrl(), parameters, "communityUuid");
		} catch (Exception e) {
			// TODO add service specific checked exception and relative handling in examples.
			e.printStackTrace();
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "deleteCommunity", success);
		}
		return success;
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
	 * @return success
	 */
	public boolean addCommunityMember(String communityUuid, String userId, String role) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "addCommunityMember", new Object[] { communityUuid, userId, role });
		}
		XMLCommunityPayloadBuilder builder = XMLCommunityPayloadBuilder.INSTANCE;
		Map<String, String> info = new HashMap<String, String>();
		info.put("snx:userid", userId);
		info.put("snx:role", role);
		Object content = builder.generateAddMemberPayload(info);
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("communityUuid", communityUuid);
		boolean success = false;
		try {
			success = super.createData(CommunitiesAPI.ADDCOMMUNITYMEMBER.getUrl(), parameters, content,
					"communityUuid");
		} catch (ClientServicesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "addCommunityMember", success);
		}
		return success;
	}

	/**
	 * Deletes a community member
	 * 
	 * @param communityUuid
	 *            The community id
	 * @param userId
	 *            The user id
	 * @return success
	 */
	public boolean deleteCommunityMember(String communityUuid, String userId) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "deleteCommunityMember", new Object[] { communityUuid });
		}
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("communityUuid", communityUuid);
		parameters.put("userId", userId);
		boolean success = false;
		try {
			success = super.deleteData(CommunitiesAPI.DELETECOMMUNITYMEMBER.getUrl(), parameters,
					"communityUuid");
		} catch (Exception e) {
			// TODO add service specific checked exception and relative handling in examples.
			e.printStackTrace();
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "deleteCommunityMember", success);
		}
		return success;
	}

	/**
	 * Factory method to get an entity instance from the id
	 * 
	 * @param entityName
	 *            The Entity class name
	 * @param uuid
	 *            The id of the entity
	 * @return entity the new entity
	 */
	@Override
	public <DataFormat> BaseEntity<DataFormat> getEntityFromId(String entityName, String uuid) {
		if (Community.class.getSimpleName().equalsIgnoreCase(entityName)) {
			return new Community<DataFormat>(uuid, this);
		} else if (Member.class.getSimpleName().equalsIgnoreCase(entityName)) {
			return new Member<DataFormat>(uuid, this);
		}
		return null;
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
	public <DataFormat> BaseEntity<DataFormat> getEntityFromData(String entityName, DataFormat data) {
		if (Community.class.getSimpleName().equalsIgnoreCase(entityName)) {
			return new Community<DataFormat>(data, this);
		} else if (Member.class.getSimpleName().equalsIgnoreCase(entityName)) {
			return new Member<DataFormat>(data, this);
		}
		return null;
	}
}
