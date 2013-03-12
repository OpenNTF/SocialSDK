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
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.BaseService;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.SBTServiceException;
import com.ibm.sbt.services.client.connections.communities.utils.Messages;
import com.ibm.sbt.services.util.AuthUtil;

/**
 * CommunityService can be used to perform Community Related operations. This is a dedicated Service for
 * Connections Communities.
 * 
 * @Represents Connections Community Service
 * @author Swati Singh
 */

public class CommunityService extends BaseService {
	static final String			sourceClass			= CommunityService.class.getName();
	static final Logger			logger				= Logger.getLogger(sourceClass);

	public static final String	CommunityBaseUrl	= "communities/service/atom";
	public static final String	seperator			= "/";

	/**
	 * Default Constructor - 0 argument constructor Calls the Constructor of BaseService Class.
	 */

	public CommunityService() {
		this(DEFAULT_ENDPOINT_NAME, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Constructor - 1 argument constructor
	 * 
	 * @param endpoint
	 *            Creates ProfileService with specified endpoint and a default CacheSize
	 */

	public CommunityService(String endpoint) {
		this(endpoint, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Constructor - 2 argument constructor
	 * 
	 * @param endpoint
	 * @param cacheSize
	 *            Creates ProfileService with specified values of endpoint and CacheSize
	 */

	public CommunityService(String endpoint, int cacheSize) {
		super(endpoint, cacheSize);
	}

	public Community getCommunity(String communityUuid) throws CommunityServiceException {
		return getCommunity(communityUuid, true);
	}
	
	public Community getCommunity(boolean loadIt) throws CommunityServiceException {
		return getCommunity("", loadIt);
	}

	/**
	 * This method is the intermediate between the JSP methods and the Java implementation. This will
	 * make a call to the server to get the feed for the specific api provided by the calling method.
	 * This method calls the load method at a lower level to actually fetch the content over network,
	 * if load is true
	 * 
	 * @param userId
	 * @param load
	 * @return community
	 * @throws XMLException
	 * @throws SBTServiceException
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
	 * @return list of All Communities
	 * @throws XMLException
	 * @throws SBTServiceException
	 */
	public Collection<Community> getPublicCommunities()
	throws CommunityServiceException{
		return getPublicCommunities(null);
	}

	/**
	 *  Wrapper method to get All Communities with different parameters for eg. Tag, sortBy. argument should be the parameter map
	 * 
	 * @param parameters - parameter Map
	 * @return list of all communities
	 * @throws XMLException
	 * @throws SBTServiceException
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
	 * This method is used to get Communities of which the user is a member or owner.
	 * 
	 * @return list of communities
	 * @throws SBTServiceException
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
	 * This method is used to get SubCommunities of a community
	 * 
	 * @param community - community for which SubCommunities are to be fetched
	 * @return Community[] - array of community
	 * @throws XMLException
	 * @throws SBTServiceException
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
	 * This method is used get members for a community.
	 * 
	 * @param community - community for which members are to be fetched
	 * @return Member[] - array of member
	 * @throws XMLException
	 * @throws SBTServiceException
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
	 * This method is used get bookmarks for a community.
	 * 
	 * @param community
	 * @return Bookmark[]
	 * @throws XMLException
	 * @throws SBTServiceException
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
	 * This method is used get a community forum topics.
	 * 
	 * @param community
	 * @return ForumTopic[] 
	 * @throws XMLException
	 * @throws SBTServiceException
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
	 * executeGet
	 * 
	 * @param uri
	 * @param params
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


	
	public Community createCommunity(Community community) throws CommunityServiceException {
		return createCommunity(community, true);
	}
	/**
	 * This method is used create a community. User should be authenticated to call this method
	 * 
	 * @param community
	 * @param loadIt
	 * @return Community
	 * @throws XMLException
	 * @throws SBTServiceException
	 */
	public Community createCommunity(Community community, boolean loadIt) throws CommunityServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "createCommunity", community);
		}
		if (null == community){
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
	 * This method is used update a community.User should be logged in as a owner of the community to call this method.
	 * 
	 * @param community
	 * @return boolean - true/false 
	 * @throws XMLException
	 * @throws SBTServiceException
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
	 * This method is used to add a member to a community. User should be logged in as a owner of the community to call this method
	 * 
	 * @param community - community to which the member needs to be added
	 * @param member - member which is to be added
	 * @return boolean - true/false 
	 * @throws XMLException
	 * @throws SBTServiceException
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
	 * This method is used to remove a member from a community
	 * 
	 * @param community
	 * @param member
	 * @return true/false 
	 * @throws XMLException
	 * @throws SBTServiceException
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
	 * This method is used to delete a community.User should be logged in as a owner of the community to call this method.
	 * 
	 * @param community
	 * @return true/false 
	 * @throws XMLException
	 * @throws SBTServiceException
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
	 * Method responsible for loading the community.
	 * 
	 * @param community
	 */

	public void load(Community community) throws CommunityServiceException {
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
	 * Method responsible for generating appropriate REST URLs
	 * 
	 * @param communityEntity ( Ref Class : CommunityEntity )
	 * @param communityType ( Ref Class : CommunityType )
	 */
	public String resolveCommunityUrl(String communityEntity, String communityType) {
		return resolveCommunityUrl(communityEntity, communityType, null);
	}

	/*
	 * Method responsible for generating appropriate REST URLs
	 * 
	 * @param communityEntity ( Ref Class : CommunityEntity )
	 * @param communityType ( Ref Class : CommunityType )
	 * @param params : ( Ref Class : CommunityParams )
	 */
	public String resolveCommunityUrl(String communityEntity, String communityType, Map<String, String> params) {
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
	 * Method to check if the userid is email. Current check is based on finding @ in the userid.
	 * 
	 * @param userId
	 * @return boolean
	 */
	private boolean isEmail(String userId) {
		return userId.contains("@");
	}
}
