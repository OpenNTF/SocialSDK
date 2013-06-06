package com.ibm.sbt.services.client.connections.communities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.BaseService;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.communities.utils.Messages;
import com.ibm.sbt.services.util.AuthUtil;
/**
 * CommunityService can be used to perform Community Related operations.
 * 
 * @Represents Connections Community Service
 * @author Swati Singh
 * <pre>
 * Sample Usage
 * {@code
 * 	CommunityService _service = new CommunityService();
 *	Collection<Community> communities = _service.getPublicCommunities();
 * }
 * </pre>	
 */

public class CommunityService extends BaseService {
	static final String			sourceClass			= CommunityService.class.getName();
	static final Logger			logger				= Logger.getLogger(sourceClass);
	private static final String	seperator			= "/";
	
	/**
	 * Used in constructing REST APIs
	 */
	public static final String	CommunityBaseUrl	= "communities/service/atom";
	
	/**
	 * Constructor Creates CommunityService Object with default endpoint and default cache size
	 */
	public CommunityService() {
		this(DEFAULT_ENDPOINT_NAME, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Constructor - Creates CommunityService Object with a specified endpoint
	 * 
	 * @param endpoint
	 *            Creates CommunityService with specified endpoint and a default CacheSize
	 */
	public CommunityService(String endpoint) {
		this(endpoint, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Constructor - Creates CommunityService Object with specified endpoint and cache size
	 * 
	 * @param endpoint
	 * @param cacheSize
	 *            Creates CommunityService with specified endpoint and CacheSize
	 */
	public CommunityService(String endpoint, int cacheSize) {
		super(endpoint, cacheSize);
	}

	/**
	 * Wrapper method to get a Community
	 * <p>
	 * fetches community content from server and populates the data member of {@link Community} with the fetched content 
	 *
	 * @param communityUuid
	 *			   id of community
	 * @return A Community
	 * @throws CommunityServiceException
	 */
	public Community getCommunity(String communityUuid) throws CommunityServiceException {
		return getCommunity(communityUuid, true);
	}
	
	/**
	 * Wrapper method to get a empty Community object
	 *
	 * @param loadIt
	 * @return Community
	 * @throws CommunityServiceException
	 */
	public Community getCommunity(boolean loadIt) throws CommunityServiceException {
		return getCommunity("", loadIt);
	}

	/**
	 * Wrapper method to get a Community, it makes network call to fetch the community based on load parameter
	 * being true/false.
	 *
	 * @param communityUuid
	 *			    id of community
	 * @param loadIt
	 * 				if true, fetches community content from server and populates the data member of {@link Community} with the fetched content
	 * @return A Community
	 * @throws CommunityServiceException
	 */
	public Community getCommunity(String communityUuid, boolean loadIt) throws CommunityServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getCommunity", new Object[] { communityUuid, loadIt });
		}
//		if (StringUtil.isEmpty(communityUuid)) {
//			throw new IllegalArgumentException("communityUuid passed was null");
//		}
		Community community = new Community(communityUuid);
		if (loadIt) {
			load(community);
		}

		if (logger.isLoggable(Level.FINEST)) {
			String log = "";
			if (community.getCommunityUuid() != null) {
				log = Messages.CommunityInfo_10;
			} else {
				log = Messages.CommunityInfo_9;
			}
			logger.exiting(sourceClass, log);
		}
		return community;
	}

	/**
	 * Wrapper method to get Public Communities
	 * 
	 * @return A list of public communities
	 * @throws CommunityServiceException
	 */
	public Collection<Community> getPublicCommunities()
	throws CommunityServiceException{
		return getPublicCommunities(null);
	}

	/**
	 * Wrapper method to get public Communities filtered by query parameters
	 * 
	 * @param parameters 
	 * 				list of query string parameters to pass to API
	 * @return A list of public communities>
	 * @throws CommunityServiceException
	 */
	public Collection<Community> getPublicCommunities(Map<String, String> parameters)
	throws CommunityServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getPublicCommunities", parameters);
		}
		Document data = executeGet(
				resolveCommunityUrl(CommunityEntity.COMMUNITIES.getCommunityEntityType(),
						CommunityType.ALL.getCommunityType()), parameters);
		Collection<Community> communities = Converter.returnCommunities(this, data);
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getPublicCommunities", communities.toString());
		}
		if (logger.isLoggable(Level.FINEST)) {
			String log = "";
			if (communities != null) {
				log = Integer.toString(communities.size());
			} else {
				log = Messages.CommunityInfo_9;
			}
			logger.exiting(sourceClass, "getPublicCommunities", log);
		}
		return communities;
	}

	/**
	 * Wrapper method to get Communities of which the user is a member or owner.
	 * 
	 * @return A list of communities of which the user is a member or owner
	 * @throws CommunityServiceException
	 */
	public Collection<Community> getMyCommunities() throws CommunityServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getMyCommunities");
		}
		Map<String, String> parameters = new HashMap<String, String>();
		Document data = executeGet(
				resolveCommunityUrl(CommunityEntity.COMMUNITIES.getCommunityEntityType(),
						CommunityType.MY.getCommunityType()), parameters);
		Collection<Community> communities = Converter.returnCommunities(this, data);
		if (logger.isLoggable(Level.FINEST)) {
			String log = "";
			if (communities != null) {
				log = Integer.toString(communities.size());
			} else {
				log = Messages.CommunityInfo_9;
			}
			logger.exiting(sourceClass, "getMyCommunities", log);
		}
		return communities;
	}

	/**
	 * Wrapper method to get SubCommunities of a community
	 * 
	 * @param community 
	 * 				 community for which SubCommunities are to be fetched
	 * @return A list of communities
	 * @throws CommunityServiceException
	 */
	public Collection<Community> getSubCommunities(Community community) throws CommunityServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getSubCommunities", community);
		}
		if (null == community){
			throw new IllegalArgumentException(Messages.InvalidArgument_1);
		}
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("communityUuid", community.getCommunityUuid());
		Document data = executeGet(
				resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),
						CommunityType.SUBCOMMUNITIES.getCommunityType()), parameters);
		Collection<Community> communities = Converter.returnCommunities(this, data);
		if (logger.isLoggable(Level.FINEST)) {
			String log = "";
			if (communities != null) {
				log = Integer.toString(communities.size());
			} else {
				log = Messages.CommunityInfo_9;
			}
			logger.exiting(sourceClass, "getSubCommunities", log);
		}
		return communities;
	}

	/**
	 * Wrapper method to get members for a community.
	 * 
	 * @param community 
	 * 				 community for which members are to be fetched
	 * @return Members of the given Community
	 * @throws CommunityServiceException
	 */
	public Member[] getMembers(Community community) throws CommunityServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getMembers", community);
		}
		if (null == community){
			throw new IllegalArgumentException(Messages.InvalidArgument_1);
		}
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("communityUuid", community.getCommunityUuid());
		Document data = executeGet(
				resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),
						CommunityType.MEMBERS.getCommunityType()), parameters);
		Member[] members = Converter.returnMembers(this, data);
		if (logger.isLoggable(Level.FINEST)) {
			String log = "";
			if (members != null) {
				log = Integer.toString(members.length);
			} else {
				log = Messages.CommunityInfo_9;
			}
			logger.exiting(sourceClass, "getMembers", log);
		}
		return members;
	}

	/**
	 * Wrapper method to get bookmarks for a community.
	 * 
	 * @param community
	 * 				community for which bookmarks are to be fetched
	 * @return Bookmarks of the given Community
	 * @throws CommunityServiceException
	 */
	public Bookmark[] getBookmarks(Community community) throws CommunityServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getBookmarks", community);
		}
		if (null == community){
			throw new IllegalArgumentException(Messages.InvalidArgument_1);
		}
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("communityUuid", community.getCommunityUuid());
		Document data = executeGet(
				resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),
						CommunityType.BOOKMARKS.getCommunityType()), parameters);
		Bookmark[] bookmarks = Converter.returnBookmarks(this, data);
		if (logger.isLoggable(Level.FINEST)) {
			String log = "";
			if (bookmarks != null) {
				log = Integer.toString(bookmarks.length);
			} else {
				log = Messages.CommunityInfo_9;
			}
			logger.exiting(sourceClass, "getBookmarks", log);
		}
		return bookmarks;

	}

	/**
	 * Wrapper method to get forum topics of a community .
	 * 
	 * @param community
	 * 				community for which forum topics are to be fetched
	 * @return Forum topics of the given Community 
	 * @throws CommunityServiceException
	 */
	public ForumTopic[] getForumTopics(Community community) throws CommunityServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getForumTopics", community);
		}
		if (null == community){
			throw new IllegalArgumentException(Messages.InvalidArgument_1);
		}
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("communityUuid", community.getCommunityUuid());
		Document data = executeGet(
				resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),
						CommunityType.FORUMTOPICS.getCommunityType()), parameters);
		ForumTopic[] forumTopics = Converter.returnForumTopics(this, data);
		if (logger.isLoggable(Level.FINEST)) {
			String log = "";
			if (forumTopics != null) {
				log = Integer.toString(forumTopics.length);
			} else {
				log = Messages.CommunityInfo_9;
			}
			logger.exiting(sourceClass, "getForumTopics", log);
		}
		return forumTopics;
	}

	/**
	 * Method to execute GET Request
	 * 
	 * @param uri
	 *           api to be executed.
	 * @param params
	 *           Map of Parameters. See {@link CommunityParams} for possible values.
	 * @return Document          
	 * @throws CommunityServiceException
	 */
	protected Document executeGet(String uri, Map<String, String> params)
	throws CommunityServiceException{
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "executeGet", new Object[] { uri, params });
		}
		Document data = null;
		try {
			data = (Document) getClientService().get(uri, params);
		} catch (ClientServicesException e) {
			if (logger.isLoggable(Level.SEVERE)) {
				logger.log(Level.SEVERE, Messages.CommunityServiceException_1 + "executeGet()", e);
			}
			throw new CommunityServiceException(e);
		}
		if (data == null) {
			return null;
		}
		return data;
	}

	/**
	 * Wrapper method to create a community,User should be authenticated to call this method
	 * <p>
	 * The data member of {@link Community} is populated with the created Community content
	 * 
	 * @param community
	 * @param loadIt
	 * 			if true populates the data member of {@link Community} with the created Community content
	 * @return The created Community
	 * @throws CommunityServiceException
	 */	
	public Community createCommunity(Community community) throws CommunityServiceException {
		return createCommunity(community, true);
	}
	/**
	 * Wrapper method to create a community
	 * <p>
	 * User should be authenticated to call this method
	 * 
	 * @param community
	 * @param loadIt
	 * 			if true populates the data member of {@link Community} with the created Community content
	 * @return The created Community
	 * @throws CommunityServiceException
	 */
	public Community createCommunity(Community community, boolean loadIt) throws CommunityServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "createCommunity", community);
		}
		if (null == community) {
			throw new IllegalArgumentException(Messages.InvalidArgument_1);
		}

		try {
			Object createPayload = community.constructCreateRequestBody();
			
			String newCommunityUrl  = (String)getClientService().post(
					resolveCommunityUrl(CommunityEntity.COMMUNITIES.getCommunityEntityType(),
							CommunityType.MY.getCommunityType()),null, createPayload, ClientService.FORMAT_CONNECTIONS_OUTPUT);
		
			String communityId = newCommunityUrl.substring(newCommunityUrl.indexOf("communityUuid=") + "communityUuid=".length());
			
			if (loadIt) {
				community = new Community(communityId);
				load(community);
			}
			else{
				community.setCommunityUuid(communityId);
			}
		} catch (ClientServicesException e) {
			if (e.getResponseStatusCode() == ClientServicesException.CONFLICT) {
				throw new DuplicateCommunityException(e, StringUtil.format(
						Messages.CommunityInfo_8, community.getTitle()));
			}
			if (logger.isLoggable(Level.SEVERE)) {
				logger.log(Level.SEVERE, Messages.CommunityInfo_7, e);
			}
			throw new CommunityServiceException(e);
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "createCommunity", community.toString());
		}
		return community;
	}

	/**
	 * Wrapper method to update a community
	 * <p>
	 * User should be logged in as a owner of the community to call this method.
	 * 
	 * @param community
	 * 				community which is to be updated
	 * @return value is true if community is updated successfully else value is false
	 * @throws CommunityServiceException
	 */
	public boolean updateCommunity(Community community) throws CommunityServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "updateCommunity", community);
		}
		boolean returnVal = true;
		if (null == community){
			throw new IllegalArgumentException(Messages.InvalidArgument_1);
		}

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("communityUuid", community.getCommunityUuid());
		try {
			Object createPayload = community.constructCreateRequestBody();
			getClientService().put(
					resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),
							CommunityType.INSTANCE.getCommunityType()), parameters, createPayload,
					ClientService.FORMAT_XML);
		} catch (ClientServicesException e) {
			returnVal = false;
			if (logger.isLoggable(Level.SEVERE)) {
				logger.log(Level.SEVERE, Messages.CommunityInfo_6, e);
			}
			throw new CommunityServiceException(e);
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "updateCommunity", returnVal);
		}
		community.clearFieldsMap();
		return returnVal;
	}

	/**
	 * Wrapper method to add member to a community.
	 * <p> 
	 * User should be logged in as a owner of the community to call this method
	 * 
	 * @param community 
	 * 				 community to which the member needs to be added
	 * @param member
	 * 				 member which is to be added
	 * @return value is true if member is added successfully to the community else value is false
	 * @throws CommunityServiceException
	 */
	public boolean addMember(Community community, Member member) throws CommunityServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "AddCommunityMember", new Object[] { community, member });
		}
		boolean returnVal = true;
		if (null == community){
			throw new IllegalArgumentException(Messages.InvalidArgument_1);
		}

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("communityUuid", community.getCommunityUuid());
		try {
			Object createPayload = member.createMemberEntry();
			getClientService().post(
					resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),
							CommunityType.MEMBERS.getCommunityType()), parameters, createPayload,
					ClientService.FORMAT_NULL);

		} catch (ClientServicesException e) {
			returnVal = false;
			if (logger.isLoggable(Level.SEVERE)) {
				logger.log(Level.SEVERE, Messages.CommunityInfo_5, e);
			}
			throw new CommunityServiceException(e);
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "AddCommunityMember", returnVal);
		}
		return returnVal;
	}

	/**
	 * Wrapper method to remove member from a community.
	 * <p> 
	 * User should be logged in as a owner of the community to call this method
	 * 
	 * @param community 
	 * 				 community to which the member is to be removed
	 * @param member
	 * 				 member which is to be removed
	 * @return value is true if member is removed successfully else value is false
	 * @throws CommunityServiceException
	 */
	public boolean removeMember(Community community, Member member) throws CommunityServiceException { 
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "removeMember", new Object[] { community, member });
		}
		boolean returnVal = true;
		if (null == community){
			throw new IllegalArgumentException(Messages.InvalidArgument_1);
		}

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("communityUuid", community.getCommunityUuid());

		if (isEmail(member.getId())) {
			parameters.put("email", member.getId());
		} else {
			parameters.put("userid", member.getId());
		}
		parameters.put("communityUuid", community.getCommunityUuid());

		try {
			getClientService().delete(
					resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),
							CommunityType.MEMBERS.getCommunityType()), parameters);
		} catch (ClientServicesException e) {
			returnVal = false;
			if (logger.isLoggable(Level.SEVERE)) {
				logger.log(Level.SEVERE, Messages.CommunityInfo_4, e);
			}
			throw new CommunityServiceException(e);

		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "removeMember", returnVal);
		}
		return returnVal;
	}

	/**
	 * Wrapper method to delete a community
	 * <p>
	 * User should be logged in as a owner of the community to call this method.
	 * 
	 * @param community
	 * 				community which is to be deleted
	 * @return value is true, if community is deleted successfully, else false is returned 
	 * @throws CommunityServiceException
	 */
	public boolean deleteCommunity(Community community) throws CommunityServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "deleteCommunity", community);
		}
		boolean returnVal = true;
		if (null == community){
			throw new IllegalArgumentException(Messages.InvalidArgument_1);
		}

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("communityUuid", community.getCommunityUuid());
		try {
			getClientService().delete(
					resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),
							CommunityType.INSTANCE.getCommunityType()), parameters);
		} catch (ClientServicesException e) {
			returnVal = false;
			if (logger.isLoggable(Level.SEVERE)) {
				logger.log(Level.SEVERE, Messages.CommunityInfo_3, e);
			}
			throw new CommunityServiceException(e);
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "deleteCommunity", returnVal);
		}
		return returnVal;
	}
	
	/*
	 * Method to fetch the community content from server and populates the data member of {@link Community}.
	 * 
	 * @param community
	 * @throws CommunityServiceException
	 */
	protected void load(Community community) throws CommunityServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "load");
		}

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("communityUuid", community.getCommunityUuid());
		Object result = null;
		try {
			String url = resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),
					CommunityType.INSTANCE.getCommunityType());
			result = getClientService().get(url, parameters, ClientService.FORMAT_XML);
		} catch (ClientServicesException e) {
			logger.log(Level.SEVERE, Messages.CommunityServiceException_1 + "load()", e);
			throw new CommunityServiceException(e);
		}
		community.setData((Document) result);

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "load");
		}
	}

	/*
	 * Method to generate appropriate REST URLs
	 * 
	 * @param communityEntity ( Ref Class : CommunityEntity )
	 * @param communityType ( Ref Class : CommunityType )
	 */
	protected String resolveCommunityUrl(String communityEntity, String communityType) {
		return resolveCommunityUrl(communityEntity, communityType, null);
	}

	/*
	 * Method to generate appropriate REST URLs
	 * 
	 * @param communityEntity ( Ref Class : CommunityEntity )
	 * @param communityType ( Ref Class : CommunityType )
	 * @param params : ( Ref Class : CommunityParams )
	 */
	protected String resolveCommunityUrl(String communityEntity, String communityType, Map<String, String> params) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "resolveCommunityUrl", communityEntity + communityType);
		}

		StringBuilder comBaseUrl = new StringBuilder(CommunityBaseUrl);
		if (StringUtil.isEmpty(communityEntity)) {
			communityEntity = CommunityEntity.COMMUNITIES.getCommunityEntityType(); // Default Entity Type
		}
		if (StringUtil.isEmpty(communityType)) {
			communityType = CommunityType.ALL.getCommunityType(); // Default Community Type
		}

		if (AuthUtil.INSTANCE.getAuthValue(endpoint).equalsIgnoreCase("oauth")) {
			comBaseUrl.append(seperator).append("oauth");
		}

		comBaseUrl.append(seperator).append(communityEntity).append(seperator).append(communityType);

		// Add required parameters
		if (null != params) {
			if (params.size() > 0) {
				comBaseUrl.append("?");
				boolean setSeperator = false;
				for (Map.Entry<String, String> param : params.entrySet()) {
					if (setSeperator) {
						comBaseUrl.append("&");
					}
					String paramvalue = "";
					try {
						paramvalue = URLEncoder.encode(param.getValue(), "UTF-8");
					} catch (UnsupportedEncodingException e) {}
					comBaseUrl.append(param.getKey() + "=" + paramvalue);
					setSeperator = true;
				}
			}
		}

		if (logger.isLoggable(Level.FINEST)) {
			logger.log(Level.FINEST, Messages.CommunityInfo_2 + comBaseUrl.toString());
		}

		return comBaseUrl.toString();
	}

	/*
	 * Method to check if the id is userid or email 
	 * <p>
	 * Current check is based on finding @ in the id.
	 * 
	 * @param id
	 * @return boolean
	 */
	private boolean isEmail(String id) {
		return id.contains("@");
	}
}
