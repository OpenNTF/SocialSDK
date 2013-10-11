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
package com.ibm.sbt.services.client.connections.communities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.StreamUtil;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.ClientService.ContentStream;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.base.util.EntityUtil;
import com.ibm.sbt.services.client.connections.communities.feedhandler.BookmarkFeedHandler;
import com.ibm.sbt.services.client.connections.communities.feedhandler.CommunityFeedHandler;
import com.ibm.sbt.services.client.connections.files.AccessType;
import com.ibm.sbt.services.client.connections.files.File;
import com.ibm.sbt.services.client.connections.files.FileList;
import com.ibm.sbt.services.client.connections.files.FileService;
import com.ibm.sbt.services.client.connections.files.FileServiceException;
import com.ibm.sbt.services.client.connections.files.FileServiceURIBuilder;
import com.ibm.sbt.services.client.connections.files.ResultType;
import com.ibm.sbt.services.client.connections.files.SubFilters;
import com.ibm.sbt.services.client.connections.files.feedHandler.FileFeedHandler;
import com.ibm.sbt.services.client.connections.files.model.Headers;
import com.ibm.sbt.services.client.connections.forums.feedhandler.TopicsFeedHandler;
import com.ibm.sbt.services.client.connections.communities.feedhandler.InviteFeedHandler;
import com.ibm.sbt.services.client.connections.communities.feedhandler.MemberFeedHandler;
import com.ibm.sbt.services.client.connections.communities.transformers.CommunityMemberTransformer;
import com.ibm.sbt.services.client.connections.communities.transformers.InviteTransformer;
import com.ibm.sbt.services.client.connections.communities.util.Messages;
import com.ibm.sbt.services.client.connections.forums.ForumService;
import com.ibm.sbt.services.client.connections.forums.TopicList;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.util.AuthUtil;

/**
 * CommunityService can be used to perform Community Related operations.
 * 
 * @Represents Connections Community Service
 * @author Swati Singh
 * @author Manish Kataria
 * @author Carlos Manias
 * <pre>
 * Sample Usage
 * {@code
 * 	CommunityService _service = new CommunityService();
 *	Collection<Community> communities = _service.getPublicCommunities();
 * }
 * </pre>	
 */

public class CommunityService extends BaseService {
	private static final String COMMUNITY_UNIQUE_IDENTIFIER = "communityUuid";
	/**
	 * Used in constructing REST APIs
	 */
	public static final String	CommunityBaseUrl	= "{communities}/service/atom";
	
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
	 * Constructor - Creates CommunityService Object with a specified endpoint
	 * 
	 * @param endpoint
	 *            Creates CommunityService with specified endpoint and a default CacheSize
	 */
	public CommunityService(Endpoint endpoint) {
		this(endpoint, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Constructor - Creates CommunityService Object with specified endpoint and cache size
	 * 
	 * @param endpoint
	 * @param cacheSize
	 *            Creates CommunityService with specified endpoint and CacheSize
	 */
	public CommunityService(Endpoint endpoint, int cacheSize) {
		super(endpoint, cacheSize);
	}

	/**
	 * This method returns the public communities
	 * 
	 * @return
	 * @throws CommunityServiceException
	 */
	public CommunityList getPublicCommunities() throws CommunityServiceException {
		return getPublicCommunities(null);
	}
	
	/**
	 * This method returns the public communities
	 * 
	 * @param parameters
	 * @return
	 * @throws CommunityServiceException
	 */
	public CommunityList getPublicCommunities(Map<String, String> parameters) throws CommunityServiceException {
		String requestUrl = resolveCommunityUrl(CommunityEntity.COMMUNITIES.getCommunityEntityType(),CommunityType.ALL.getCommunityType()); 
		
		CommunityList communities = null;
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			communities = (CommunityList) getEntities(requestUrl, parameters, new CommunityFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new CommunityServiceException(e, Messages.PublicCommunitiesException);
		} catch (IOException e) {
			throw new CommunityServiceException(e, Messages.PublicCommunitiesException);
		}
		
		return communities;
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
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
		String url = resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),
				CommunityType.INSTANCE.getCommunityType());
		Community community;
		try {
			community = (Community)getEntity(url, parameters, new CommunityFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new CommunityServiceException(e, Messages.CommunityException, communityUuid);
		} catch (Exception e) {
			throw new CommunityServiceException(e, Messages.CommunityException, communityUuid);
		}
		
		return community;
	}
	
	/**
	 * This method returns the Communities of which the user is a member or owner.
	 * 
	 * @param communityUuid
	 * @return MemberList
	 * @throws CommunityServiceException
	 */
	public MemberList getMembers(String communityUuid) throws CommunityServiceException {
		return getMembers(communityUuid, null);
	}
	
	/** Wrapper method to get list of the members of a community
	 * 
	 * @param communityUuid
	 * @param query parameters
	 * @return MemberList
	 * @throws CommunityServiceException
	 */
	public MemberList getMembers(String communityUuid, Map<String, String> parameters) throws CommunityServiceException {
		if (StringUtil.isEmpty(communityUuid)){
			throw new CommunityServiceException(null, Messages.NullCommunityIdException);
		}
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
		
		String requestUrl = resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),
				CommunityType.MEMBERS.getCommunityType());
		
		MemberList members = null;
		try {
			members = (MemberList) getEntities(requestUrl, parameters, new MemberFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new CommunityServiceException(e, Messages.CommunityMembersException, communityUuid);
		} catch (IOException e) {
			throw new CommunityServiceException(e, Messages.CommunityMembersException, communityUuid);
		}
		
		return members;
	}

	/**
	 * This method returns the Communities of which the user is a member or owner.
	 * 
	 * @return
	 * @throws CommunityServiceException
	 */
	public CommunityList getMyCommunities() throws CommunityServiceException {
		return getMyCommunities(null);
	}
	/**
	 * Wrapper method to get Communities of which the user is a member or owner.
	 * 
	 * @return A list of communities of which the user is a member or owner
	 * @throws CommunityServiceException
	 */
	public CommunityList getMyCommunities(Map<String, String> parameters) throws CommunityServiceException {
		String requestUrl = resolveCommunityUrl(CommunityEntity.COMMUNITIES.getCommunityEntityType(), CommunityType.MY.getCommunityType());
			
		CommunityList communities = null;
		if(null == parameters){
			 parameters = new HashMap<String, String>();
		}
		try {
			communities = (CommunityList) getEntities(requestUrl, parameters, new CommunityFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new CommunityServiceException(e, Messages.MyCommunitiesException);
		} catch (IOException e) {
			throw new CommunityServiceException(e, Messages.MyCommunitiesException);
		}
		
		return communities;
	}
	
	public CommunityList getSubCommunities(String communityUuid) throws CommunityServiceException {
		return getSubCommunities(communityUuid,	null);
	}
	
	/**
	 * Wrapper method to get SubCommunities of a community
	 * 
	 * @param communityUuid 
	 * 				 community Id of which SubCommunities are to be fetched
	 * @return A list of communities
	 * @throws CommunityServiceException
	 */
	public CommunityList getSubCommunities(String communityUuid, Map<String, String> parameters) throws CommunityServiceException {
		
		if (StringUtil.isEmpty(communityUuid)){
			throw new CommunityServiceException(null, Messages.NullCommunityIdException);
		}
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
	
		String requestUrl = resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),	CommunityType.SUBCOMMUNITIES.getCommunityType());
		
		CommunityList communities = null;
		try {
			communities = (CommunityList) getEntities(requestUrl, parameters, new CommunityFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new CommunityServiceException(e, Messages.SubCommunitiesException, communityUuid);
		} catch (IOException e) {
			throw new CommunityServiceException(e, Messages.SubCommunitiesException, communityUuid);
		}
		
		return communities;
	}


	public BookmarkList getBookmarks(String communityUuid) throws CommunityServiceException {
		return getBookmarks(communityUuid,	null);
	}
	/**
	 * Wrapper method to get bookmarks for a community.
	 *
	 * @param communityUuid 
	 * 				 community Id of which bookmarks are to be fetched
	 * @return Bookmarks of the given Community
	 * @throws CommunityServiceException
	 */
	public BookmarkList getBookmarks(String communityUuid, Map<String, String> parameters) throws CommunityServiceException {
	
		if (StringUtil.isEmpty(communityUuid)){
			throw new CommunityServiceException(null, Messages.NullCommunityIdException);
		}
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
		String requestUrl = resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),
				CommunityType.BOOKMARKS.getCommunityType());
		
		BookmarkList bookmarks = null;
		try {
			bookmarks = (BookmarkList) getEntities(requestUrl, parameters, new BookmarkFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new CommunityServiceException(e, Messages.CommunityBookmarksException, communityUuid);
		} catch (IOException e) {
			throw new CommunityServiceException(e, Messages.CommunityBookmarksException, communityUuid);
		}
		
		return bookmarks;

	}

	public TopicList getForumTopics(String communityUuid) throws CommunityServiceException {
		return getForumTopics(communityUuid, null);
	}
	/**
	 * Wrapper method to get forum topics of a community .
	 * 
	 * @param communityUuid 
	 * 				 community Id of which forum topics are to be fetched
	 * @return Forum topics of the given Community 
	 * @throws CommunityServiceException
	 */
	public TopicList getForumTopics(String communityUuid, Map<String, String> parameters) throws CommunityServiceException {
		
		if (StringUtil.isEmpty(communityUuid)){
			throw new CommunityServiceException(null, Messages.NullCommunityIdException);
		}
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}		
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
		String requestUrl = resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),	CommunityType.FORUMTOPICS.getCommunityType());
		
		TopicList forumTopics = null;
		try {
			forumTopics = (TopicList) getEntities(requestUrl, parameters, new TopicsFeedHandler(new ForumService()));
		}catch (ClientServicesException e) {
			throw new CommunityServiceException(e, Messages.CommunityForumTopicsException, communityUuid);
		} catch (IOException e) {
			throw new CommunityServiceException(e, Messages.CommunityForumTopicsException, communityUuid);
		}
		
		return forumTopics;
	}
	
	public InviteList getMyInvites() throws CommunityServiceException {
		return getMyInvites(null);
	}
	 /**
     * Get a list of the outstanding community invitations of the currently authenticated 
     * user or provide parameters to search for a subset of those invitations.
     * 
     * @method getMyInvites
     * @param parameters
     * 				 Various parameters that can be passed to get a feed of members of a community. 
     * 				 The parameters must be exactly as they are supported by IBM Connections like ps, sortBy etc.
     * @return pending invites for the authenticated user
     */
	public InviteList getMyInvites(Map<String, String> parameters) throws CommunityServiceException {
		
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}		
		String requestUrl = resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),	CommunityType.MYINVITES.getCommunityType());
		InviteList invites = null;
		try {
			invites = (InviteList) getEntities(requestUrl, parameters, new InviteFeedHandler(this));
			
		}catch (ClientServicesException e) {
			throw new CommunityServiceException(e, Messages.CommunityInvitationsException);
		} catch (IOException e) {
			throw new CommunityServiceException(e, Messages.CommunityInvitationsException);
		}
		
		return invites;
	}
	
	/**
     * Method to create a community invitation, user should be authenticated to perform this operation
	 * 
     * @method createInvite
     * @param communityUuid 
	 * 				 community Id for which invite is to be sent
	 * @param contributorId
	 *				 user id of contributor
     * @return pending invites for the authenticated user
     */
	
	public Invite createInvite(String communityUuid, String contributorId) throws CommunityServiceException {
		
		if (StringUtil.isEmpty(communityUuid)){
			throw new CommunityServiceException(null, Messages.NullCommunityIdException);
		}
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
		String inviteUrl = resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),
				CommunityType.INVITES.getCommunityType());
		Object communityPayload;
		Member member = new Member(this, contributorId);
		try {
			communityPayload = new InviteTransformer().transform(member.getFieldsMap());
		} catch (TransformerException e) {
			throw new CommunityServiceException(e, Messages.CreateCommunityPayloadException);
		}
		Invite invite = null;
		
		try {
			Response result = super.createData(inviteUrl, parameters, communityPayload);
			invite = (Invite) new InviteFeedHandler(this).createEntity(result);

		} catch (Exception e) {
			throw new CommunityServiceException(e, Messages.CreateInvitationException);
		}
		return invite;
	}
	
	/**
     * Method to accept a outstanding community invitation, user should be authenticated to perform this operation
	 * 
     * @method acceptInvite
     * @param communityUuid 
	 * 				 community Id for which invite is sent
	 * @param contributorId
	 *				 user id of contributor
     * @return boolean
     */
	
	public boolean acceptInvite(String communityUuid, String contributorId) throws CommunityServiceException {
		
		if (StringUtil.isEmpty(communityUuid)){
			throw new CommunityServiceException(null, Messages.NullCommunityIdException);
		}
		boolean success = true;
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
		String inviteUrl = resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),
				CommunityType.MEMBERS.getCommunityType());
	
		Object communityPayload;
		
		Member member = new Member(this, contributorId);
		try {
			communityPayload = new CommunityMemberTransformer().transform(member.getFieldsMap());
		} catch (TransformerException e) {
			success = false;
			throw new CommunityServiceException(e, Messages.CreateCommunityPayloadException);
		}
		
		try {
			super.createData(inviteUrl, parameters, communityPayload);
		} catch (Exception e) {
			success = false;
			throw new CommunityServiceException(e, Messages.AcceptInvitationException);
		} 
		return success;
		
	}
	
	/**
     * Method to decline a outstanding community invitation, user should be authenticated to perform this operation
	 * 
     * @method declineInvite
     * @param communityUuid 
	 * 				 community Id for which invite is sent
	 * @param contributorId
	 *				 user id of contributor
     * @return boolean
     */
	
	public boolean declineInvite(String communityUuid, String contributorId) throws CommunityServiceException {
		
		if (StringUtil.isEmpty(communityUuid)){
			throw new CommunityServiceException(null, Messages.NullCommunityIdException);
		}
		boolean success = true;
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
		if(EntityUtil.isEmail(contributorId)){
			parameters.put("email", contributorId);
		}
		else{
			parameters.put("userid", contributorId);	
		}
		String inviteUrl = resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),CommunityType.INVITES.getCommunityType());
		
		try {
			super.deleteData(inviteUrl, parameters, communityUuid);
		} catch (Exception e) {
			success = false;
			throw new CommunityServiceException(e, Messages.DeclineInvitationException);
		}
		return success;
	}

	/**
	 * Wrapper method to create a community
	 * <p>
	 * User should be authenticated to call this method
	 * 
	 * In response to successful creation of a community server does not return the id in response payload.
	 * In headers Location has the id to newly created community, we use this to return the communityid.
	 * Location: https://server/communities/service/atom/community/instance?communityUuid=c93bfb43-0bf2-4125-a8a4-7acd4
	 * 
	 * @param Community
	 * @return String
	 * 			communityid of newly created Community
	 * @throws CommunityServiceException
	 */
	public String createCommunity(Community community) throws CommunityServiceException {
		if (null == community){
			throw new CommunityServiceException(null, Messages.NullCommunityObjectException);
		}

		try {
			Object communityPayload;
			try {
				communityPayload =  community.constructCreateRequestBody();
			} catch (TransformerException e) {
				throw new CommunityServiceException(e, Messages.CreateCommunityPayloadException);
			}
			String communityPostUrl = resolveCommunityUrl(CommunityEntity.COMMUNITIES.getCommunityEntityType(),CommunityType.MY.getCommunityType());
			Response requestData = createData(communityPostUrl, null, communityPayload,ClientService.FORMAT_CONNECTIONS_OUTPUT);
			community.clearFieldsMap();
			return extractCommunityIdFromHeaders(requestData);
		} catch (ClientServicesException e) {
			throw new CommunityServiceException(e, Messages.CreateCommunityException);
		} catch (IOException e) {
			throw new CommunityServiceException(e, Messages.CreateCommunityException);
		}
	}
	
	private String extractCommunityIdFromHeaders(Response requestData){
		Header[] headers = requestData.getResponse().getAllHeaders();
		String urlLocation = "";
		for (Header header: headers){
			if (header.getName().equalsIgnoreCase("Location")) {
				urlLocation = header.getValue();
			}
		}
		return urlLocation.substring(urlLocation.indexOf(COMMUNITY_UNIQUE_IDENTIFIER+"=") + (COMMUNITY_UNIQUE_IDENTIFIER+"=").length());
	}

	/**
	 * Wrapper method to update a community
	 * <p>
	 * User should be logged in as a owner of the community to call this method.
	 * 
	 * @param community
	 * 				community which is to be updated
	 * @throws CommunityServiceException
	 */
	public void updateCommunity(Community community) throws CommunityServiceException {
		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, community.getCommunityUuid());
			String updateUrl = resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),	CommunityType.INSTANCE.getCommunityType());
			Object communityPayload;
			try {
				communityPayload = community.constructCreateRequestBody();
			} catch (TransformerException e) {
				throw new CommunityServiceException(e, Messages.CreateCommunityPayloadException);
			}
			super.updateData(updateUrl, parameters,communityPayload, COMMUNITY_UNIQUE_IDENTIFIER);
			community.clearFieldsMap();
		} catch (ClientServicesException e) {
			throw new CommunityServiceException(e, Messages.UpdateCommunityException);
		} catch (IOException e) {
			throw new CommunityServiceException(e, Messages.UpdateCommunityException);
		}
	}
	
	/**
	 * Wrapper method to get member of a community.
	 * <p> 
	 * 
	 * @param communityUuid 
	 * 				 Id of Community
	 * @param memberId
	 * 				 Id of Member 
	 * @return Member
	 * @throws CommunityServiceException
	 */
	
	public Member getMember(String communityUuid, String memberId) throws CommunityServiceException {
		if (StringUtil.isEmpty(communityUuid)||StringUtil.isEmpty(memberId)){
			throw new CommunityServiceException(null, Messages.NullCommunityIdOrUserIdException);
		}
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
		if(memberId.contains("@")){
			parameters.put("email", memberId);
		}
		else{
			parameters.put("userid", memberId);
		}
		String url = resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),
				CommunityType.MEMBERS.getCommunityType());
		Member member;
		try {
			member = (Member)getEntity(url, parameters, new MemberFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new CommunityServiceException(e, Messages.GetMemberException, memberId, communityUuid);
		} catch (Exception e) {
			throw new CommunityServiceException(e, Messages.GetMemberException, memberId, communityUuid);
		}
		
		return member;
	}
	/**
	 * Wrapper method to add member to a community.
	 * <p> 
	 * User should be logged in as a owner of the community to call this method
	 * 
	 * @param communityUuid 
	 * 				 Id of Community to which the member needs to be added
	 * @param memberId
	 * 				 Id of Member which is to be added
	 * @throws CommunityServiceException
	 */
	public boolean addMember(String communityUuid, Member member) throws CommunityServiceException {
		
		if (StringUtil.isEmpty(communityUuid)){
			throw new CommunityServiceException(null, Messages.NullCommunityIdUserIdOrRoleException);
		}
		String memberId = member.getUserid();
		if(StringUtil.isEmpty(memberId)){
			if(StringUtil.isEmpty(member.getEmail()))
				throw new CommunityServiceException(null, Messages.NullCommunityIdUserIdOrRoleException);
			else
				memberId = member.getEmail();
		}
		try {
			if(StringUtil.isEmpty(member.getRole())){
				member.setRole("member"); //default role is member
			}
		} catch (Exception e) {	
			member.setRole("member"); 
		}
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
		
		Object communityPayload;
		try {
			communityPayload = new CommunityMemberTransformer().transform(member.getFieldsMap());
		} catch (TransformerException e) {
			throw new CommunityServiceException(e, Messages.CreateCommunityPayloadException);
		}
		
		String communityUpdateMembertUrl = resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),CommunityType.MEMBERS.getCommunityType());
		try {
			Response response = super.createData(communityUpdateMembertUrl, parameters, communityPayload);
			int statusCode = response.getResponse().getStatusLine().getStatusCode();
			return statusCode == HttpServletResponse.SC_CREATED;
		} catch (ClientServicesException e) {
			throw new CommunityServiceException(e, Messages.AddMemberException, memberId, communityUuid);
		} catch (IOException e) {
			throw new CommunityServiceException(e, Messages.AddMemberException, memberId, communityUuid);
		}
	}
	/**
	 * Wrapper method to update member of a community.
	 * <p> 
	 * User should be logged in as a owner of the community to call this method
	 * 
	 * @param community 
	 * 				 Id of Community 
	 * @param memberId
	 * 				 Id of Member 
	 * @throws CommunityServiceException
	 */
	public void updateMember(String communityId, Member member)throws CommunityServiceException {
		if (StringUtil.isEmpty(communityId)){
			throw new CommunityServiceException(null, Messages.NullCommunityIdUserIdOrRoleException);
		}
		String memberId = "";
		if(StringUtil.isEmpty(member.getUserid())){
			memberId = member.getEmail();
		}
		if (StringUtil.isEmpty(memberId)){
			throw new CommunityServiceException(null, Messages.NullCommunityIdUserIdOrRoleException);
		}
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityId);
	
		parameters.put("userid", member.getUserid());
	
		Object memberPayload;
		try {
			
			member.setUserid(member.getUserid()); // to add this in fields map for update
			memberPayload = new CommunityMemberTransformer().transform(member.getFieldsMap());
		} catch (TransformerException e) {
			throw new CommunityServiceException(e, Messages.UpdateMemberException);
		}
		
		String communityUpdateMembertUrl = resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),CommunityType.MEMBERS.getCommunityType());
		try {
			super.createData(communityUpdateMembertUrl, parameters, memberPayload);
		} catch (ClientServicesException e) {
			throw new CommunityServiceException(e, Messages.UpdateMemberException, memberId, member.getRole(), communityId);
		} catch (IOException e) {
			throw new CommunityServiceException(e, Messages.UpdateMemberException, memberId, member.getRole(), communityId);
		}
		
	}
	/**
	 * Wrapper method to remove member from a community.
	 * <p> 
	 * User should be logged in as a owner of the community to call this method
	 * 
	 * @param community 
	 * 				 Id of Community from which the member is to be removed
	 * @param memberId
	 * 				 Id of Member who is to be removed
	 * @throws CommunityServiceException
	 */
	public void removeMember(String communityUuid, String memberId) throws CommunityServiceException { 
		if (StringUtil.isEmpty(communityUuid)||StringUtil.isEmpty(memberId)){
			throw new CommunityServiceException(null, Messages.NullCommunityIdOrUserIdException);
		}
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
		if(EntityUtil.isEmail(memberId)){
			parameters.put("email", memberId);
		}else{
			parameters.put("userid", memberId);
		}
		
		try {
			String deleteCommunityUrl = resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),CommunityType.MEMBERS.getCommunityType());
			super.deleteData(deleteCommunityUrl, parameters, COMMUNITY_UNIQUE_IDENTIFIER);
		} catch (ClientServicesException e) {
			throw new CommunityServiceException(e, Messages.RemoveMemberException, memberId, communityUuid);
		} catch (IOException e) {
			throw new CommunityServiceException(e, Messages.RemoveMemberException, memberId, communityUuid);
		}
	}

	/**
	 * Wrapper method to delete a community
	 * <p>
	 * User should be logged in as a owner of the community to call this method.
	 * 
	 * @param String
	 * 				communityUuid which is to be deleted
	 * @throws CommunityServiceException
	 */
	public void deleteCommunity(String communityUuid) throws CommunityServiceException {
		if (StringUtil.isEmpty(communityUuid)){
			throw new CommunityServiceException(null, Messages.NullCommunityIdException);
		}

		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
			String deleteCommunityUrl = resolveCommunityUrl(CommunityEntity.COMMUNITY.getCommunityEntityType(),	CommunityType.INSTANCE.getCommunityType());
			super.deleteData(deleteCommunityUrl, parameters, COMMUNITY_UNIQUE_IDENTIFIER);
		} catch (ClientServicesException e) {
			throw new CommunityServiceException(e, Messages.DeleteCommunityException, communityUuid);
		} catch (IOException e) {
			throw new CommunityServiceException(e, Messages.DeleteCommunityException, communityUuid);
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
		StringBuilder comBaseUrl = new StringBuilder(CommunityBaseUrl);
		if (StringUtil.isEmpty(communityEntity)) {
			communityEntity = CommunityEntity.COMMUNITIES.getCommunityEntityType(); // Default Entity Type
		}
		if (StringUtil.isEmpty(communityType)) {
			communityType = CommunityType.ALL.getCommunityType(); // Default Community Type
		}

		if (AuthUtil.INSTANCE.getAuthValue(endpoint).equalsIgnoreCase(ConnectionsConstants.OAUTH)) {
			comBaseUrl.append(ConnectionsConstants.SEPARATOR).append(ConnectionsConstants.OAUTH);
		}

		comBaseUrl.append(ConnectionsConstants.SEPARATOR).append(communityEntity).append(ConnectionsConstants.SEPARATOR).append(communityType);

		// Add required parameters
		if (null != params && params.size() > 0) {
			comBaseUrl.append(ConnectionsConstants.INIT_URL_PARAM);
			boolean setSeparator = false;
			for (Map.Entry<String, String> param : params.entrySet()) {
				String key = param.getKey();
				if (StringUtil.isEmpty(key)) continue;
				String value = EntityUtil.encodeURLParam(param.getValue());
				if (StringUtil.isEmpty(value)) continue;
				if (setSeparator) {
					comBaseUrl.append(ConnectionsConstants.URL_PARAM);
				} else {
					setSeparator = true;
				}
				comBaseUrl.append(key).append(ConnectionsConstants.EQUALS).append(value);
			}
		}

		return comBaseUrl.toString();
	}
	
	/**
	 * Method to get a list of Community Files
	 * @param communityId
	 * @param params
	 * @return
	 * @throws CommunityServiceException
	 */
	public FileList getCommunityFiles(String communityId, HashMap<String, String> params) throws CommunityServiceException {
		FileService fileService = new FileService();
		try {
			return fileService.getCommunityFiles(communityId, params);
		} catch (FileServiceException e) {
			throw new CommunityServiceException(e);
		}
	}
	
	/**
	 * Method to download a community file
	 * @param ostream
	 * @param fileId
	 * @param communityId
	 * @param params
	 * @return
	 * @throws CommunityServiceException
	 */
	public long downloadCommunityFile(OutputStream ostream, final String fileId, final String communityId, Map<String, String> params) throws CommunityServiceException {
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		SubFilters subFilters = new SubFilters();
        if (StringUtil.isEmpty(communityId)) {
        	throw new CommunityServiceException(null, Messages.NullCommunityIdUserIdOrRoleException);
        }
        if (StringUtil.isEmpty(fileId)) {
        	throw new CommunityServiceException(null, Messages.NullFileId);
        }
        if(null == params){
			 params = new HashMap<String, String>();
		}
        subFilters.setCommunityLibraryId(communityId);
        subFilters.setFileId(fileId);
        String resultType = ResultType.ENTRY.getResultType();
		String requestUrl = FileServiceURIBuilder.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, null,
                null, subFilters, resultType); 
		File file = null;
		try {
			file = (File) super.getEntity(requestUrl, params, new FileFeedHandler(new FileService())); 
		} catch (ClientServicesException e) {
			throw new CommunityServiceException(e, Messages.DownloadCommunitiesException);
		} catch (IOException e) {
			throw new CommunityServiceException(e, Messages.DownloadCommunitiesException);
		}
		// now we have the file.. we need to download it.. 
		SubFilters downloadFilters = new SubFilters();
		downloadFilters.setLibraryId(file.getLibraryId());
		downloadFilters.setFileId(file.getFileId());
		resultType = ResultType.MEDIA.getResultType();
		requestUrl = FileServiceURIBuilder.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, null,
                null, downloadFilters, resultType); 
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(Headers.ContentType, Headers.BINARY);
		Response response = null;
		try {
			response = this.getClientService().get(requestUrl, params, headers, ClientService.FORMAT_INPUTSTREAM);
		} catch (ClientServicesException e) {
			throw new CommunityServiceException(e, Messages.DownloadCommunitiesException);
		} 
		InputStream istream = (InputStream) response.getData();
		long noOfBytes = 0;
		try {
			if (istream != null) {
				noOfBytes = StreamUtil.copyStream(istream, ostream);
				ostream.flush();
			}
		} catch (IllegalStateException e) {
			throw new CommunityServiceException(e, Messages.DownloadCommunitiesException);
		} catch (IOException e) {
			throw new CommunityServiceException(e, Messages.DownloadCommunitiesException);
		}
		return noOfBytes;
	}
	
	/**
	 * Method to upload a File to Community
	 * @param iStream
	 * @param communityId
	 * @param title
	 * @param length
	 * @throws CommunityServiceException
	 */
	public File uploadFile(InputStream iStream, String communityId, final String title, long length) throws CommunityServiceException {
		if (iStream == null) {
            throw new CommunityServiceException(null, "null stream");
        }
        if (title == null) {
            throw new CommunityServiceException(null, "null name");
        }
        ContentStream contentFile = new ContentStream(iStream, length, title);
		String accessType = AccessType.AUTHENTICATED.getAccessType();
		SubFilters subFilters = new SubFilters();
        if (StringUtil.isEmpty(communityId)) {
        	throw new CommunityServiceException(null, Messages.NullCommunityIdUserIdOrRoleException);
        }
        subFilters.setCommunityLibraryId(communityId);
        String resultType = ResultType.FEED.getResultType();
		String requestUri = FileServiceURIBuilder.constructUrl(FileServiceURIBuilder.FILES.getBaseUrl(), accessType, null, null,
                null, subFilters, resultType); 
	    try {
	    	Response data = (Response) super.createData(requestUri, null, null, contentFile);
	    	return (File)new FileFeedHandler(new FileService()).createEntity(data);
	    } catch (ClientServicesException e) {
			throw new CommunityServiceException(e, Messages.UploadCommunitiesException);
		} catch (IOException e) {
			throw new CommunityServiceException(e, Messages.UploadCommunitiesException);
		}
	}
}
