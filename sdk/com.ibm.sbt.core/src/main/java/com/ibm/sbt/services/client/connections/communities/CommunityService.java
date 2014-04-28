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

import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.AtomFeedHandler;
import com.ibm.sbt.services.client.base.AuthType;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.base.util.EntityUtil;
import com.ibm.sbt.services.client.connections.communities.model.CommunityXPath;
import com.ibm.sbt.services.client.connections.communities.transformers.CommunityMemberTransformer;
import com.ibm.sbt.services.client.connections.communities.transformers.InviteTransformer;
import com.ibm.sbt.services.client.connections.communities.util.Messages;
import com.ibm.sbt.services.client.connections.files.File;
import com.ibm.sbt.services.client.connections.files.FileService;
import com.ibm.sbt.services.client.connections.files.FileServiceException;
import com.ibm.sbt.services.endpoints.Endpoint;

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
	private static final long serialVersionUID = 4832918694422006289L;
	private static final String COMMUNITY_UNIQUE_IDENTIFIER = "communityUuid";
	private static final String USERID 						= "userid";
	
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
	 * Return mapping key for this service
	 */
	@Override
	public String getServiceMappingKey() {
		return "communities";
	}
	
	@Override
	public NamedUrlPart getAuthType(){
		String auth = super.getAuthType().getValue();
		auth = AuthType.BASIC.get().equalsIgnoreCase(auth)?"":auth;
		return new NamedUrlPart("authType", auth);
	}

	/***************************************************************
	 * FeedHandlers for each entity type
	 ****************************************************************/

	/**
	 * Factory method to instantiate a FeedHandler for Communities
	 * @return IFeedHandler<Community>
	 */
	public IFeedHandler<Community> getCommunityFeedHandler() {
		return new AtomFeedHandler<Community>(this) {
			@Override
			protected Community entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new Community(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	/**
	 * Factory method to instantiate a FeedHandler for Members
	 * @return IFeedHandler<Member>
	 */
	public IFeedHandler<Member> getMemberFeedHandler() {
		return new AtomFeedHandler<Member>(this) {
			@Override
			protected Member entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new Member(service, node, nameSpaceCtx, xpath);
			}
		};
	}
	
	/**
	 * Factory method to instantiate a FeedHandler for Invites
	 * @return IFeedHandler<Invite>
	 */
	public IFeedHandler<Invite> getInviteFeedHandler() {
		return new AtomFeedHandler<Invite>(this) {
			@Override
			protected Invite entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new Invite(service, node, nameSpaceCtx, xpath);
			}

		};
	}
	
	/**
	 * This method returns the public communities
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Community> getPublicCommunities() throws ClientServicesException {
		return getPublicCommunities(null);
	}
	
	/**
	 * This method returns the public communities
	 * 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Community> getPublicCommunities(Map<String, String> parameters) throws ClientServicesException {
        String url = CommunityUrls.COMMUNITIES_ALL.format(this);
		return getCommunityEntityList(url, parameters);
	}
	
	/**
	 * Wrapper method to get a Community
	 * <p>
	 * fetches community content from server and populates the data member of {@link Community} with the fetched content 
	 *
	 * @param communityUuid
	 *			   id of community
	 * @return A Community
	 * @throws ClientServicesException
	 */
	public Community getCommunity(String communityUuid) throws ClientServicesException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
        String url = CommunityUrls.COMMUNITY_INSTANCE.format(this);
		
		return getCommunityEntity(url, parameters);
	}
	
	/**
	 * This method returns the Communities of which the user is a member or owner.
	 * 
	 * @param communityUuid
	 * @return MemberList
	 * @throws ClientServicesException
	 */
	public EntityList<Member> getMembers(String communityUuid) throws ClientServicesException {
		return getMembers(communityUuid, null);
	}
	
	/** Wrapper method to get list of the members of a community
	 * 
	 * @param communityUuid
	 * @param query parameters
	 * @return MemberList
	 * @throws ClientServicesException
	 */
	public EntityList<Member> getMembers(String communityUuid, Map<String, String> parameters) throws ClientServicesException {
		if (StringUtil.isEmpty(communityUuid)){
			throw new ClientServicesException(null, Messages.NullCommunityIdException);
		}
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
        String url = CommunityUrls.COMMUNITY_MEMBERS.format(this);
		
		return getMemberEntityList(url, parameters);
	}

	/**
	 * This method returns the Communities of which the user is a member or owner.
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Community> getMyCommunities() throws ClientServicesException {
		return getMyCommunities(null);
	}
	/**
	 * Wrapper method to get Communities of which the user is a member or owner.
	 * 
	 * @return A list of communities of which the user is a member or owner
	 * @throws ClientServicesException
	 */
	public EntityList<Community> getMyCommunities(Map<String, String> parameters) throws ClientServicesException {
        String url = CommunityUrls.COMMUNITIES_MY.format(this);		
		return getCommunityEntityList(url, parameters);
	}
	
	public EntityList<Community> getSubCommunities(String communityUuid) throws ClientServicesException {
		return getSubCommunities(communityUuid,	null);
	}
	
	/**
	 * Wrapper method to get SubCommunities of a community
	 * 
	 * @param communityUuid 
	 * 				 community Id of which SubCommunities are to be fetched
	 * @return A list of communities
	 * @throws ClientServicesException
	 */
	public EntityList<Community> getSubCommunities(String communityUuid, Map<String, String> parameters) throws ClientServicesException {
		
		if (StringUtil.isEmpty(communityUuid)){
			throw new ClientServicesException(null, Messages.NullCommunityIdException);
		}
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
        String url = CommunityUrls.COMMUNITY_SUBCOMMUNITIES.format(this);
		return getCommunityEntityList(url, parameters);
	}

	/**
     * Get a list of the outstanding community invitations of the currently authenticated 
     * user or provide parameters to search for a subset of those invitations.
     * 
     * @method getMyInvites
     * @return pending invites for the authenticated user
     * @throws ClientServicesException
     */
	public EntityList<Invite> getMyInvites() throws ClientServicesException {
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
     * @throws ClientServicesException
     */
	public EntityList<Invite> getMyInvites(Map<String, String> parameters) throws ClientServicesException {
		
		String url = CommunityUrls.COMMUNITY_MYINVITES.format(this);
		return getInviteEntityList(url, parameters);
	}
	/**
     * Retrieve a community invite.
     * 
     * @method getInvite
     * @param {String} communityUuid
     * @param (String} inviteUuid
     * @throws ClientServicesException
     */
	public Invite getInvite(String communityUuid, String inviteUuid) throws ClientServicesException{
		if (StringUtil.isEmpty(communityUuid) || StringUtil.isEmpty(inviteUuid)){
			throw new ClientServicesException(null, Messages.getInviteException);
		}
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
		parameters.put(USERID, inviteUuid); // the parameter name should be inviteUuid, this is a bug on connections
        String url = CommunityUrls.COMMUNITY_INVITES.format(this);
		return getInviteEntity(url, parameters);
		
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
     * @throws ClientServicesException
     */
	
	public Invite createInvite(Invite invite) throws ClientServicesException {
		
		if (StringUtil.isEmpty(invite.getCommunityUuid())){
			throw new ClientServicesException(null, Messages.NullCommunityIdException);
		}
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, invite.getCommunityUuid());
        String url = CommunityUrls.COMMUNITY_INVITES.format(this);
		Object communityPayload;
		try {
			communityPayload = new InviteTransformer().transform(invite.getFieldsMap());
		} catch (TransformerException e) {
			throw new ClientServicesException(e, Messages.CreateCommunityPayloadException);
		}
		try {
			Response result = super.createData(url, parameters, communityPayload);
			invite = getInviteFeedHandler().createEntity(result);
		} catch (Exception e) {
			throw new ClientServicesException(e, Messages.CreateInvitationException);
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
     * @throws ClientServicesException
     */
	
	public boolean acceptInvite(String communityUuid, String contributorId) throws ClientServicesException {
		
		if (StringUtil.isEmpty(communityUuid)){
			throw new ClientServicesException(null, Messages.NullCommunityIdException);
		}
		boolean success = true;
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
        String url = CommunityUrls.COMMUNITY_MEMBERS.format(this);
	
		Object communityPayload;
		
		Member member = new Member(this, contributorId);
		try {
			communityPayload = new CommunityMemberTransformer().transform(member.getFieldsMap());
		} catch (TransformerException e) {
			success = false;
			throw new ClientServicesException(e, Messages.CreateCommunityPayloadException);
		}
		
		try {
			super.createData(url, parameters, communityPayload);
		} catch (Exception e) {
			success = false;
			throw new ClientServicesException(e, Messages.AcceptInvitationException);
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
     * @throws ClientServicesException
     */
	
	public boolean declineInvite(String communityUuid, String contributorId) throws ClientServicesException {
		
		if (StringUtil.isEmpty(communityUuid)){
			throw new ClientServicesException(null, Messages.NullCommunityIdException);
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
        String url = CommunityUrls.COMMUNITY_INVITES.format(this);
		
		try {
			super.deleteData(url, parameters, communityUuid);
		} catch (Exception e) {
			success = false;
			throw new ClientServicesException(e, Messages.DeclineInvitationException);
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
	 * @throws ClientServicesException
	 */
	public String createCommunity(String title, String content, String type) throws ClientServicesException {
		Community community = new Community();
		community.setTitle(title);
		community.setContent(content);
		community.setCommunityType(type);
		return createCommunity(community);
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
	 * @throws ClientServicesException
	 */
	public String createCommunity(Community community) throws ClientServicesException {
		if (null == community){
			throw new ClientServicesException(null, Messages.NullCommunityObjectException);
		}

		try {
			Object communityPayload;
			try {
				communityPayload =  community.constructCreateRequestBody();
			} catch (TransformerException e) {
				throw new ClientServicesException(e, Messages.CreateCommunityPayloadException);
			}
			String url = CommunityUrls.COMMUNITIES_MY.format(this);
			Response requestData = createData(url, null, communityPayload,ClientService.FORMAT_CONNECTIONS_OUTPUT);
			community.clearFieldsMap();
			return extractCommunityIdFromHeaders(requestData);
		} catch (ClientServicesException e) {
			throw new ClientServicesException(e, Messages.CreateCommunityException);
		} catch (IOException e) {
			throw new ClientServicesException(e, Messages.CreateCommunityException);
		}
	}
	
	private String extractCommunityIdFromHeaders(Response requestData){
		Header header = requestData.getResponse().getFirstHeader("Location");
		String urlLocation = header!=null?header.getValue():"";
		return urlLocation.substring(urlLocation.indexOf(COMMUNITY_UNIQUE_IDENTIFIER+"=") + (COMMUNITY_UNIQUE_IDENTIFIER+"=").length());
	}

	/**
	 * Wrapper method to update a community
	 * <p>
	 * User should be logged in as a owner of the community to call this method.
	 * 
	 * @param community
	 * 				community which is to be updated
	 * @throws ClientServicesException
	 */
	public void updateCommunity(Community community) throws ClientServicesException {
		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, community.getCommunityUuid());
			String url = CommunityUrls.COMMUNITY_INSTANCE.format(this);
			Object communityPayload;
			if(community.getFieldsMap().get(CommunityXPath.title)== null)
				community.setTitle(community.getTitle());
			if(community.getFieldsMap().get(CommunityXPath.content)== null)
				community.setContent(community.getContent());
			if(community.getFieldsMap().get(CommunityXPath.communityType)== null)
				community.setCommunityType(community.getCommunityType());
			if(!community.getFieldsMap().toString().contains(CommunityXPath.tags.toString()))
				community.setTags(community.getTags());
			community.setAsString(CommunityXPath.communityUuid, community.getCommunityUuid());
			
			try {
				communityPayload = community.constructCreateRequestBody();
			} catch (TransformerException e) {
				throw new ClientServicesException(e, Messages.CreateCommunityPayloadException);
			}
			super.updateData(url, parameters,communityPayload, COMMUNITY_UNIQUE_IDENTIFIER);
			community.clearFieldsMap();
		} catch (ClientServicesException e) {
			throw new ClientServicesException(e, Messages.UpdateCommunityException);
		} catch (IOException e) {
			throw new ClientServicesException(e, Messages.UpdateCommunityException);
		}
	}
	/**
	 * Wrapper method to update Community Logo, supported for connections
	 * 
	 * @param File
	 * 			image to be uploaded as Community Logo
	 * @param communityId
	 * @throws ClientServicesException
	 */
	public void updateCommunityLogo(java.io.File file, String communityId) throws ClientServicesException{

		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityId);
			String name = file.getName();
			int dot = StringUtil.lastIndexOfIgnoreCase(name, ".");
			String ext = "";
			if (dot > -1) {
				ext = name.substring(dot + 1); // add one for the dot!
			}
			if (!StringUtil.isEmpty(ext)) {
				Map<String, String> headers = new HashMap<String, String>();
				if (StringUtil.equalsIgnoreCase(ext,"jpg")) {
					headers.put("Content-Type", "image/jpeg");	// content-type should be image/jpeg for file extension - jpeg/jpg
				} else {
					headers.put("Content-Type", "image/" + ext);
				}
				// the url doesn't have atom in base 
				String url = "/communities/service/html/image";
				getClientService().put(url, parameters, headers, file, ClientService.FORMAT_NULL);
				
			}
		} catch (ClientServicesException e) {
			throw new ClientServicesException(e, Messages.UpdateCommunityLogoException);
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
	 * @throws ClientServicesException
	 */
	
	public Member getMember(String communityUuid, String memberId) throws ClientServicesException {
		if (StringUtil.isEmpty(communityUuid)||StringUtil.isEmpty(memberId)){
			throw new ClientServicesException(null, Messages.NullCommunityIdOrUserIdException);
		}
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
		if(memberId.contains("@")){
			parameters.put("email", memberId);
		}
		else{
			parameters.put("userid", memberId);
		}
		String url = CommunityUrls.COMMUNITY_MEMBERS.format(this);		
		
		return getMemberEntity(url, parameters);
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
	 * @throws ClientServicesException
	 */
	public boolean addMember(String communityUuid, Member member) throws ClientServicesException {
		
		if (StringUtil.isEmpty(communityUuid)){
			throw new ClientServicesException(null, Messages.NullCommunityIdUserIdOrRoleException);
		}
		String memberId = member.getUserid();
		if(StringUtil.isEmpty(memberId)){
			if(StringUtil.isEmpty(member.getEmail()))
				throw new ClientServicesException(null, Messages.NullCommunityIdUserIdOrRoleException);
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
			throw new ClientServicesException(e, Messages.CreateCommunityPayloadException);
		}
		
		String url = CommunityUrls.COMMUNITY_MEMBERS.format(this);
		try {
			Response response = super.createData(url, parameters, communityPayload);
			int statusCode = response.getResponse().getStatusLine().getStatusCode();
			return statusCode == HttpServletResponse.SC_CREATED;
		} catch (ClientServicesException e) {
			throw new ClientServicesException(e, Messages.AddMemberException, memberId, communityUuid);
		} catch (IOException e) {
			throw new ClientServicesException(e, Messages.AddMemberException, memberId, communityUuid);
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
	 * @throws ClientServicesException
	 */
	public void updateMember(String communityId, Member member)throws ClientServicesException {
		if (StringUtil.isEmpty(communityId)){
			throw new ClientServicesException(null, Messages.NullCommunityIdUserIdOrRoleException);
		}
		String memberId = member.getUserid();
		if(StringUtil.isEmpty(memberId)){
			memberId = member.getEmail();
		}

		if (StringUtil.isEmpty(memberId)){
			throw new ClientServicesException(null, Messages.NullCommunityIdUserIdOrRoleException);
		}
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityId);
	
		parameters.put("userid", member.getUserid());
	
		Object memberPayload;
		try {
			
			member.setUserid(member.getUserid()); // to add this in fields map for update
			memberPayload = new CommunityMemberTransformer().transform(member.getFieldsMap());
		} catch (TransformerException e) {
			throw new ClientServicesException(e, Messages.UpdateMemberException);
		}
		
		String url = CommunityUrls.COMMUNITY_MEMBERS.format(this);
		try {
			super.createData(url, parameters, memberPayload);
		} catch (ClientServicesException e) {
			throw new ClientServicesException(e, Messages.UpdateMemberException, memberId, member.getRole(), communityId);
		} catch (IOException e) {
			throw new ClientServicesException(e, Messages.UpdateMemberException, memberId, member.getRole(), communityId);
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
	 * @throws ClientServicesException
	 */
	public void removeMember(String communityUuid, String memberId) throws ClientServicesException { 
		if (StringUtil.isEmpty(communityUuid)||StringUtil.isEmpty(memberId)){
			throw new ClientServicesException(null, Messages.NullCommunityIdOrUserIdException);
		}
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
		if(EntityUtil.isEmail(memberId)){
			parameters.put("email", memberId);
		}else{
			parameters.put("userid", memberId);
		}
		
		try {
			String url = CommunityUrls.COMMUNITY_MEMBERS.format(this);
			super.deleteData(url, parameters, COMMUNITY_UNIQUE_IDENTIFIER);
		} catch (ClientServicesException e) {
			throw new ClientServicesException(e, Messages.RemoveMemberException, memberId, communityUuid);
		} catch (IOException e) {
			throw new ClientServicesException(e, Messages.RemoveMemberException, memberId, communityUuid);
		}
	}

	/**
	 * Wrapper method to delete a community
	 * <p>
	 * User should be logged in as a owner of the community to call this method.
	 * 
	 * @param String
	 * 				communityUuid which is to be deleted
	 * @throws ClientServicesException
	 */
	public void deleteCommunity(String communityUuid) throws ClientServicesException {
		if (StringUtil.isEmpty(communityUuid)){
			throw new ClientServicesException(null, Messages.NullCommunityIdException);
		}

		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
			String url = CommunityUrls.COMMUNITY_INSTANCE.format(this);
			super.deleteData(url, parameters, COMMUNITY_UNIQUE_IDENTIFIER);
		} catch (ClientServicesException e) {
			throw new ClientServicesException(e, Messages.DeleteCommunityException, communityUuid);
		} catch (IOException e) {
			throw new ClientServicesException(e, Messages.DeleteCommunityException, communityUuid);
		}		
		
	}
	
	/**
	 * Method to get a list of Community Files
	 * @param communityId
	 * @param params
	 * @return FileList
	 * @throws ClientServicesException
	 */
	public EntityList<File> getCommunityFiles(String communityId, HashMap<String, String> params) throws ClientServicesException {
		FileService fileService = new FileService(this.endpoint);
		try {
			return fileService.getCommunityFiles(communityId, params);
		} catch (FileServiceException e) {
			throw new ClientServicesException(e);
		}
	}
	
	/**
	 * Method to download a community file
	 * @param ostream
	 * @param fileId
	 * @param libraryId - Library Id of which the file is a part. This value can be obtained by using File's getLibraryId method.
	 * @param params
	 * @return long
	 * @throws ClientServicesException
	 */
	public long downloadCommunityFile(OutputStream ostream, final String fileId, final String libraryId, Map<String, String> params) throws ClientServicesException {
		FileService svc = new FileService(this.endpoint);
		try {
			return svc.downloadCommunityFile(ostream, fileId, libraryId, params);
		} catch (FileServiceException e) {
			throw new ClientServicesException(e, Messages.DownloadCommunitiesException);
		} 
	}
	
	/**
	 * Method to upload a File to Community
	 * @param iStream
	 * @param communityId
	 * @param title
	 * @param length
	 * @throws ClientServicesException
	 */
	public File uploadFile(InputStream iStream, String communityId, final String title, long length) throws ClientServicesException {
		FileService svc = new FileService(this.endpoint);
		try {
			return svc.uploadCommunityFile(iStream, communityId, title, length);
		} catch (FileServiceException e) {
			throw new ClientServicesException(e, Messages.UploadCommunitiesException);
		}
	}
	
	/***************************************************************
	 * Factory methods
	 ****************************************************************/
	
	protected Community getCommunityEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		try {
			return getEntity(requestUrl, getParameters(parameters), getCommunityFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}
	
	protected Member getMemberEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		try {
			return getEntity(requestUrl, getParameters(parameters), getMemberFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}
	
	
	protected Invite getInviteEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		try {
			return getEntity(requestUrl, getParameters(parameters), getInviteFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}
	
	protected EntityList<Community> getCommunityEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		try {
			return getEntities(requestUrl, getParameters(parameters), getCommunityFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}
	
	protected EntityList<Member> getMemberEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		try {
			return getEntities(requestUrl, getParameters(parameters), getMemberFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}
	
	protected EntityList<Invite> getInviteEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		try {
			return getEntities(requestUrl, getParameters(parameters), getInviteFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}
}
