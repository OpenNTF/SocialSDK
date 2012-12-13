package com.ibm.sbt.services.client.connections.communities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
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
import com.ibm.sbt.services.client.smartcloud.base.BaseEntity;
import com.ibm.sbt.services.util.AuthUtil;

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

	public Community getCommunity(String communityUuid) throws XMLException, SBTServiceException {
		return getCommunity(communityUuid, true);
	}

	/**
	 * @param userId
	 * @param load
	 * @return This method is the intermediate between the JSP methods and the Java implementation. This will
	 *         make a call to the server to get the feed for the specific api provided by the calling method.
	 *         This method calls the load method at a lower level to actually fetch the content over network,
	 *         if load is true
	 * @throws XMLException
	 *             // remove xmlexception using for testing
	 * @throws SBTServiceException
	 */

	public Community getCommunity(String communityUuid, boolean load) throws XMLException,
			SBTServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getCommunity", new Object[] { communityUuid, load });
		}
		Community community = new Community(communityUuid);
		if (load) {
			load(community);
		}

		if (logger.isLoggable(Level.FINEST)) {
			String log = "";
			if (community.getCommunityUuid() != null) {
				log = "returning  requested community";
			} else {
				log = "empty response from server for requested community";
			}
			logger.exiting(sourceClass, log);
		}
		return community;
	}

	/**
	 * getPublicCommunities - get Public Communities
	 * 
	 * @param
	 * @return Community[] - array of community This method is used to get All Communities
	 * @throws SBTServiceException
	 */
	public Community[] getPublicCommunities() throws XMLException // getPublicCommunities
			, SBTServiceException {
		return getPublicCommunities(null);
	}

	/**
	 * getPublicCommunities - to get Public Communities with given parameters
	 * 
	 * @param parameters
	 *            - parameter Map
	 * @return Community[] - array of community This method is used to get All Communities with different
	 *         parameters for eg. Tag, sortBy. argument should be the parameter map
	 * @throws SBTServiceException
	 */
	public Community[] getPublicCommunities(Map<String, String> parameters) throws XMLException,
			SBTServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getPublicCommunities", parameters);
		}
		Document data = getCommunityEntities(
				resolveCommunityUrl(CommunityEntity.COMMUNITIES.getCommunityEntityType(),
						CommunityType.ALL.getCommunityType()), parameters);
		Community[] communities = Converter.returnCommunities(this, data);
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getPublicCommunities", Arrays.toString(communities));
		}
		if (logger.isLoggable(Level.FINEST)) {
			String log = "";
			if (communities != null) {
				log = Integer.toString(communities.length);
			} else {
				log = "empty response from server for requested communities";
			}
			logger.exiting(sourceClass, "getPublicCommunities", log);
		}
		return communities;
	}

	/**
	 * getMyCommunities - to get My Communities
	 * 
	 * @return Community[] - array of community This method is used to get Communities of which the user is a
	 *         member or owner. argument should be the email or userid of the user for which communities are
	 *         to be fetched
	 * @throws SBTServiceException
	 */
	public Community[] getMyCommunities() throws XMLException, SBTServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getMyCommunities");
		}
		Map<String, String> parameters = new HashMap<String, String>();
		Document data = getCommunityEntities(
				resolveCommunityUrl(CommunityEntity.COMMUNITIES.getCommunityEntityType(),
						CommunityType.MY.getCommunityType()), parameters);
		Community[] communities = Converter.returnCommunities(this, data);
		if (logger.isLoggable(Level.FINEST)) {
			String log = "";
			if (communities != null) {
				log = Integer.toString(communities.length);
			} else {
				log = "empty response from server for requested communities";
			}
			logger.exiting(sourceClass, "getMyCommunities", log);
		}
		return communities;
	}

	/**
	 * getSubCommunities - to get SubCommunities of a community
	 * 
	 * @param community
	 * @return Community[] - array of community This method is used get SubCommunities for a community.
	 *         arguments should be the community for which SubCommunities are to be fetched
	 * @throws SBTServiceException
	 */
	public Community[] getSubCommunities(Community community) throws XMLException, SBTServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getSubCommunities", community);
		}

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("communityUuid", community.getCommunityUuid());
		Document data = getCommunityEntities(
				resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),
						CommunityType.SUBCOMMUNITIES.getCommunityType()), parameters);
		Community[] communities = Converter.returnCommunities(this, data);
		if (logger.isLoggable(Level.FINEST)) {
			String log = "";
			if (communities != null) {
				log = Integer.toString(communities.length);
			} else {
				log = "empty response from server for requested communities";
			}
			logger.exiting(sourceClass, "getSubCommunities", log);
		}
		return communities;
	}

	/**
	 * getMembers - to get community members
	 * 
	 * @param community
	 * @return Member[] - array of member This method is used get members for a community. arguments should be
	 *         the community for which members are to be fetched
	 * @throws SBTServiceException
	 */
	public Member[] getMembers(Community community) throws XMLException, SBTServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getMembers", community);
		}
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("communityUuid", community.getCommunityUuid());
		Document data = getCommunityEntities(
				resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),
						CommunityType.MEMBERS.getCommunityType()), parameters);
		Member[] members = Converter.returnMembers(this, data);
		if (logger.isLoggable(Level.FINEST)) {
			String log = "";
			if (members != null) {
				log = Integer.toString(members.length);
			} else {
				log = "empty response from server for members";
			}
			logger.exiting(sourceClass, "getMembers", log);
		}
		return members;
	}

	/**
	 * getBookmarks - to get community bookmarks
	 * 
	 * @param community
	 * @return Bookmark[] - array of bookmark This method is used get bookmarks for a community. arguments
	 *         should be the community for which bookmarks are to be fetched
	 * @throws SBTServiceException
	 */

	public Bookmark[] getBookmarks(Community community) throws XMLException, SBTServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getBookmarks", community);
		}

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("communityUuid", community.getCommunityUuid());
		Document data = getCommunityEntities(
				resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),
						CommunityType.BOOKMARKS.getCommunityType()), parameters);
		Bookmark[] bookmarks = Converter.returnBookmarks(this, data);
		if (logger.isLoggable(Level.FINEST)) {
			String log = "";
			if (bookmarks != null) {
				log = Integer.toString(bookmarks.length);
			} else {
				log = "empty response from server for bookmarks";
			}
			logger.exiting(sourceClass, "getBookmarks", log);
		}
		return bookmarks;

	}

	/**
	 * getForumTopics - to get community forum topics
	 * 
	 * @param community
	 * @return ForumTopic[] - array of forum topics This method is used get a community forum topics.
	 *         arguments should be the community for which forum topics are to be fetched
	 * @throws SBTServiceException
	 */
	public ForumTopic[] getForumTopics(Community community) throws XMLException, SBTServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getCommunityForumTopics", community);
		}
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("communityUuid", community.getCommunityUuid());
		Document data = getCommunityEntities(
				resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),
						CommunityType.FORUMTOPICS.getCommunityType()), parameters);
		ForumTopic[] forumTopics = Converter.returnForumTopics(this, data);
		if (logger.isLoggable(Level.FINEST)) {
			String log = "";
			if (forumTopics != null) {
				log = Integer.toString(forumTopics.length);
			} else {
				log = "empty response from server for forum Topics";
			}
			logger.exiting(sourceClass, "getBookmarks", log);
		}
		return forumTopics;
	}

	public Document getCommunityEntities(String uri, Map<String, String> params) throws XMLException,
			SBTServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getCommunityEntities", new Object[] { uri, params });
		}
		Document data = null;
		try {
			data = (Document) getClientService().get(uri, params);
		} catch (ClientServicesException e) {
			if (logger.isLoggable(Level.SEVERE)) {
				logger.log(Level.SEVERE, "Error encountered in getting community bookmarks", e);
			}
			throw new CommunityServiceException(e);
		}
		if (logger.isLoggable(Level.FINEST)) {
			String log = "";
			if (data != null) {
				log = "returning data for getCommunityEntities";
			} else {
				log = "empty response from server for forum Topics";
			}
			logger.exiting(sourceClass, "getCommunityEntities", log);
		}
		return data;
	}

	/**
	 * createCommunity - to create a community
	 * 
	 * @param community
	 * @return true/false This method is used create a community. arguments should be the community which is
	 *         to be created User should be authenticated to call this method
	 * @throws SBTServiceException
	 */
	public boolean createCommunity(Community community) throws XMLException, SBTServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "createCommunity", community);
		}
		boolean returnVal = true;

		try {
			Object createPayload = community.constructCreateRequestBody();
			getClientService().post(
					resolveCommunityUrl(CommunityEntity.COMMUNITIES.getCommunityEntityType(),
							CommunityType.MY.getCommunityType()), createPayload);
		} catch (ClientServicesException e) {
			if (e.getResponseStatusCode() == ClientServicesException.CONFLICT) {
				throw new DuplicateCommunityException(e, StringUtil.format(
						"A community already exists with this title {0} ", community.getTitle()));
			}
			returnVal = false;
			if (logger.isLoggable(Level.SEVERE)) {
				logger.log(Level.SEVERE, "Error encountered in creating community", e);
			}
			throw new CommunityServiceException(e);
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "createCommunity", returnVal);
		}
		return returnVal;
	}

	/**
	 * updateCommunity - to update a community
	 * 
	 * @param community
	 * @return true/false This method is used update a community. arguments should be the community which is
	 *         to be updated User should be logged in as a owner of the community to call this method
	 * @throws SBTServiceException
	 */
	public boolean updateCommunity(Community community) throws XMLException, SBTServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "updateCommunity", community);
		}
		boolean returnVal = true;

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
				logger.log(Level.SEVERE, "Error encountered in updating community", e);
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
	 * AddCommunityMember - to add a member to a community
	 * 
	 * @param community
	 * @param member
	 * @return true/false This method is used to add a member to a community. arguments should be the
	 *         community to which the member needs to be added and member which is to be added User should be
	 *         logged in as a owner of the community to call this method
	 * @throws SBTServiceException
	 */
	public boolean addMember(Community community, Member member) throws XMLException, SBTServiceException { // change
																											// it
																											// to
		// addMember
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "AddCommunityMember", new Object[] { community, member });
		}
		boolean returnVal = true;

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
				logger.log(Level.SEVERE, "Error encountered in adding member", e);
			}
			throw new CommunityServiceException(e);
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "AddCommunityMember", returnVal);
		}
		return returnVal;
	}

	/**
	 * removeMember - to remove a member from a community
	 * 
	 * @param community
	 * @param member
	 * @return true/false This method is used to remove a member from a community. arguments should be the
	 *         community to which the member needs to be removed and member which is to be removed User should
	 *         be logged in as a owner of the community to call this method
	 * @throws SBTServiceException
	 */
	public boolean removeMember(Community community, Member member) throws XMLException, SBTServiceException { // change
																												// it
																												// to
		// addMember
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "removeMember", new Object[] { community, member });
		}
		boolean returnVal = true;

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
				logger.log(Level.SEVERE, "Error encountered in removing member", e);
			}
			throw new CommunityServiceException(e);

		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "removeMember", returnVal);
		}
		return returnVal;
	}

	/**
	 * deleteCommunity - to delete community
	 * 
	 * @param userId
	 * @return true/false This method is used to delete a community. argument should be the community needs to
	 *         be deleted User should be logged in as a owner of the community to call this method
	 * @throws SBTServiceException
	 */
	public boolean deleteCommunity(Community community) throws XMLException, SBTServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "deleteCommunity", community);
		}
		boolean returnVal = true;

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("communityUuid", community.getCommunityUuid());
		try {
			getClientService().delete(
					resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),
							CommunityType.INSTANCE.getCommunityType()), parameters);
		} catch (ClientServicesException e) {
			returnVal = false;
			if (logger.isLoggable(Level.SEVERE)) {
				logger.log(Level.SEVERE, "Error encountered in deleting Community", e);
			}
			throw new CommunityServiceException(e);
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "deleteCommunity", returnVal);
		}
		return returnVal;
	}

	public void load(Community community) throws XMLException, SBTServiceException {
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
			logger.log(Level.SEVERE, "Error loading Community", e);
			throw new CommunityServiceException(e);
		}
		community.setData((Document) result);

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "load");
		}
	}

	/*
	 * @param communityEntity ( Ref Class : CommunityEntity )
	 * @param communityType ( Ref Class : CommunityType )
	 */
	public String resolveCommunityUrl(String communityEntity, String communityType) {
		return resolveCommunityUrl(communityEntity, communityType, null);
	}

	/*
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
			logger.log(Level.FINEST, "resolved Community URL :" + comBaseUrl.toString());
		}

		return comBaseUrl.toString();
	}

	private boolean isEmail(String userId) {
		return userId.contains("@");
	}

	@Override
	public <DataFormat> BaseEntity<DataFormat> getEntityFromData(String entityName, DataFormat data)
			throws SBTServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <DataFormat> BaseEntity<DataFormat> getEntityFromId(String entityName, String uuid)
			throws SBTServiceException {
		// TODO Auto-generated method stub
		return null;
	}

}
