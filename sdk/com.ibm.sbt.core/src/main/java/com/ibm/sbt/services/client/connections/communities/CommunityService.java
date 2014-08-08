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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

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
import com.ibm.sbt.services.client.base.CommonConstants.HTTPCode;
import com.ibm.sbt.services.client.base.ConnectionsService;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.util.EntityUtil;
import com.ibm.sbt.services.client.connections.communities.model.CommunityXPath;
import com.ibm.sbt.services.client.connections.communities.serializers.CommunityInviteSerializer;
import com.ibm.sbt.services.client.connections.communities.serializers.CommunityMemberSerializer;
import com.ibm.sbt.services.client.connections.communities.util.Messages;
import com.ibm.sbt.services.client.connections.files.File;
import com.ibm.sbt.services.client.connections.files.FileService;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * CommunityService can be used to perform Community Related operations.
 * 
 * @see
 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.5+API+Documentation#action=openDocument&res_title=Communities_API_ic45&content=pdcontent">
 *			Communities API</a>
 *
 * @author Swati Singh
 * @author Manish Kataria
 * @author Carlos Manias
 */

public class CommunityService extends ConnectionsService {
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

	@Override
	protected void initServiceMappingKeys(){
		serviceMappingKeys = new String[]{"communities"};
	}

	@Override
	public NamedUrlPart getAuthType(){
		String auth = super.getAuthType().getValue();
		auth = AuthType.BASIC.get().equalsIgnoreCase(auth)?"":auth;
		return new NamedUrlPart("authType", auth);
	}

	/***************************************************************
	 * Getting Communities feeds
	 ****************************************************************/

	/**
	 * Get the All Communities feed to see a list of all public communities to which the authenticated user 
	 * has access or pass in parameters to search for communities that match a specific criteria.
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Community> getPublicCommunities() throws ClientServicesException {
		return getPublicCommunities(null);
	}

	/**
	 * Get the All Communities feed to see a list of all public communities to which the authenticated user 
	 * has access or pass in parameters to search for communities that match a specific criteria. 
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
	 * Retrieve the members feed to view a list of the members who belong to a given community.
	 * 
	 * @param communityUuid
	 * @return MemberList
	 * @throws ClientServicesException
	 */
	public EntityList<Member> getMembers(String communityUuid) throws ClientServicesException {
		return getMembers(communityUuid, null);
	}

	/** 
	 * Retrieve the members feed to view a list of the members who belong to a given community.
	 * 
	 * @param communityUuid
	 * @param query parameters
	 * @return MemberList
	 * @throws ClientServicesException
	 */
	public EntityList<Member> getMembers(String communityUuid, Map<String, String> parameters) throws ClientServicesException {
		String url = CommunityUrls.COMMUNITY_MEMBERS.format(this,CommunityUrls.getCommunityUuid(communityUuid));
		return getMemberEntityList(url, parameters);
	}

	/**
	 * Get the My Communities feed to see a list of the communities to which the authenticated user is a member 
	 * or pass in parameters to search for a subset of those communities that match a specific criteria.
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Community> getMyCommunities() throws ClientServicesException {
		return getMyCommunities(null);
	}
	
	/**
	 * Get the My Communities feed to see a list of the communities to which the authenticated user is a member 
	 * or pass in parameters to search for a subset of those communities that match a specific criteria.
	 * 
	 * @return A list of communities of which the user is a member or owner
	 * @throws ClientServicesException
	 */
	public EntityList<Community> getMyCommunities(Map<String, String> parameters) throws ClientServicesException {
		String url = CommunityUrls.COMMUNITIES_MY.format(this);		
		return getCommunityEntityList(url, parameters);
	}
	
	/**
	 * Get a list of subcommunities associated with a community.
	 * 
	 * @param communityUuid 
	 * 				 community Id of which SubCommunities are to be fetched
	 * @return A list of communities
	 * @throws ClientServicesException
	 */
	public EntityList<Community> getSubCommunities(String communityUuid) throws ClientServicesException {
		return getSubCommunities(communityUuid,	null);
	}

	/**
	 * Get a list of subcommunities associated with a community.
	 * 
	 * @param communityUuid 
	 * 				 community Id of which SubCommunities are to be fetched
	 * @return A list of communities
	 * @throws ClientServicesException
	 */
	public EntityList<Community> getSubCommunities(String communityUuid, Map<String, String> parameters) throws ClientServicesException {
		String url = CommunityUrls.COMMUNITY_SUBCOMMUNITIES.format(this,CommunityUrls.getCommunityUuid(communityUuid));
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
	 * Check if a community is a sub community of another community. 
	 * @return True if the community is a sub community else returns false
	 */
	public boolean isSubCommunity(Community community) {		
		return community.exists(CommunityXPath.parentCommunityUrl);
	}
	
	public String getParentCommunityUrl(Community community) {
		if(isSubCommunity(community)){
			return community.getAsString(CommunityXPath.parentCommunityUrl);
		}else 
			return null;
		
	}


	/***************************************************************
	 * Working with communities programmatically
	 ****************************************************************/

	/**
	 * To create a community, send an Atom entry document 
	 * containing the new community to the My Communities resource.
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
	 * To create a community, send an Atom entry document 
	 * containing the new community to the My Communities resource.
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
		if (null == community) {
			throw new ClientServicesException(null, Messages.NullCommunityObjectException);
		}
		
		Object communityPayload = null;
	    if(isSubCommunity(community)){
	        communityPayload = community.constructSubCommUpdateRequestBody();
	    }else{
	        communityPayload = community.constructCreateRequestBody();
	    }
	    String url = CommunityUrls.COMMUNITIES_MY.format(this);
		Response response = createData(url, null, communityPayload,ClientService.FORMAT_CONNECTIONS_OUTPUT);
		checkResponseCode(response, HTTPCode.CREATED);
		community.clearFieldsMap();
		return extractCommunityIdFromHeaders(response);
		
	}

	private String extractCommunityIdFromHeaders(Response requestData){
		Header header = requestData.getResponse().getFirstHeader("Location");
		String urlLocation = header!=null?header.getValue():"";
		return urlLocation.substring(urlLocation.indexOf(COMMUNITY_UNIQUE_IDENTIFIER+"=") + (COMMUNITY_UNIQUE_IDENTIFIER+"=").length());
	}
	
	public String createSubCommunity(Community subCommunity, Community parentCommunity) throws ClientServicesException{
		if (null == subCommunity || parentCommunity == null) {
			throw new ClientServicesException(null, Messages.NullCommunityObjectException);
		}
		subCommunity.setParentCommunityUrl(parentCommunity.getSelfUrl());
		return createCommunity(subCommunity);
	}
	
	/**
	 * To retrieve a community entry, use the edit link for the 
	 * community entry which can be found in the my communities feed.
	 * <p>
	 * fetches community content from server and populates the data member of {@link Community} with the fetched content 
	 *
	 * @param communityUuid
	 *			   id of community
	 * @return A Community
	 * @throws ClientServicesException
	 */
	public Community getCommunity(String communityUuid) throws ClientServicesException {
		return getCommunity(communityUuid, null);
	}
	
	/**
	 * To retrieve a community entry, use the edit link for the 
	 * community entry which can be found in the my communities feed.
	 * <p>
	 * fetches community content from server and populates the data member of {@link Community} with the fetched content 
	 *
	 * @param communityUuid
	 *			   id of community
	 * @return A Community
	 * @throws ClientServicesException
	 */
	public Community getCommunity(String communityUuid, Map<String, String> parameters) throws ClientServicesException {
		String url = CommunityUrls.COMMUNITY_INSTANCE.format(this,CommunityUrls.getCommunityUuid(communityUuid));
		return getCommunityEntity(url, parameters);
	}

	/**
	 * To update a community, send a replacement community entry document 
	 * in Atom format to the existing community's edit web address.
	 * <p>
	 * User should be logged in as a owner of the community to call this method.
	 * 
	 * @param community
	 * 				community which is to be updated
	 * @throws ClientServicesException
	 */
	public void updateCommunity(Community community) throws ClientServicesException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, community.getCommunityUuid());
		String url = CommunityUrls.COMMUNITY_UPDATE.format(this);
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
		
		if(isSubCommunity(community)){
			communityPayload = community.constructSubCommUpdateRequestBody();
		}else{
			communityPayload = community.constructCreateRequestBody();
		}
		
		Response response = updateData(url, parameters, communityPayload, COMMUNITY_UNIQUE_IDENTIFIER);
		checkResponseCode(response, HTTPCode.OK);
		community.clearFieldsMap();
	}
	/**
	 * Update Community Logo, supported for connections
	 * 
	 * @param File
	 * 			image to be uploaded as Community Logo
	 * @param communityId
	 * @throws ClientServicesException
	 */
	public void updateCommunityLogo(java.io.File file, String communityId) throws ClientServicesException{
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
	}
	
	/**
	 * Delete a community
	 * <p>
	 * User should be logged in as a owner of the community to call this method.
	 * 
	 * @param String
	 * 				communityUuid which is to be deleted
	 * @throws ClientServicesException
	 */
	public void deleteCommunity(String communityUuid) throws ClientServicesException {
			deleteCommunity(communityUuid, null);
	}
	
	/**
	 * Delete a community
	 * <p>
	 * User should be logged in as a owner of the community to call this method.
	 * 
	 * @param String
	 * 				communityUuid which is to be deleted
	 * @throws ClientServicesException
	 */
	public void deleteCommunity(String communityUuid, Map<String, String> parameters) throws ClientServicesException {
		if (StringUtil.isEmpty(communityUuid)){
			throw new ClientServicesException(null, Messages.NullCommunityIdException);
		}
		String url = CommunityUrls.COMMUNITY_INSTANCE.format(this,CommunityUrls.getCommunityUuid(communityUuid));
		Response response = deleteData(url, parameters, COMMUNITY_UNIQUE_IDENTIFIER);
		checkResponseCode(response, HTTPCode.OK);
	}

	/***************************************************************
	 * Working with community invitations
	 ****************************************************************/
	
	/**
	 * Creates a community invitation, user should be authenticated to perform this operation
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
		return createInvite(invite, null);
	}
	
	/**
	 * Creates a community invitation, user should be authenticated to perform this operation
	 * 
	 * @method createInvite
	 * @param communityUuid 
	 * 				 community Id for which invite is to be sent
	 * @param contributorId
	 *				 user id of contributor
	 * @return pending invites for the authenticated user
	 * @throws ClientServicesException
	 */

	public Invite createInvite(Invite invite, Map<String, String> parameters) throws ClientServicesException {
		
		String communityUuid = invite.getCommunityUuid();
		if (StringUtil.isEmpty(communityUuid)){
			throw new ClientServicesException(null, Messages.NullCommunityIdException);
		}
		String url = CommunityUrls.COMMUNITY_INVITES.format(this, CommunityUrls.getCommunityUuid(communityUuid));
		Object communityPayload;
		CommunityInviteSerializer serializer = new CommunityInviteSerializer(invite);
		communityPayload = serializer.createPayload();
		System.out.println(communityPayload);
		Response response = createData(url, parameters, communityPayload);
		checkResponseCode(response, HTTPCode.CREATED);
		invite = getInviteFeedHandler().createEntity(response);
		return invite;
	}
	
	/**
	 * Accept an outstanding community invitation, user should be authenticated to perform this operation
	 * 
	 * @method acceptInvite
	 * @param communityUuid 
	 * 				 community Id for which invite is sent
	 * @param contributorId
	 *				 user id of contributor
	 * @return boolean
	 * @throws ClientServicesException
	 */

	public void acceptInvite(String communityUuid, String contributorId) throws ClientServicesException {
		acceptInvite(communityUuid, contributorId, null);
	}

	/**
	 * Accept an outstanding community invitation, user should be authenticated to perform this operation
	 * 
	 * @method acceptInvite
	 * @param communityUuid 
	 * 				 community Id for which invite is sent
	 * @param contributorId
	 *				 user id of contributor
	 * @return boolean
	 * @throws ClientServicesException
	 */

	public void acceptInvite(String communityUuid, String contributorId, Map<String, String> parameters) throws ClientServicesException {
		if (StringUtil.isEmpty(communityUuid)){
			throw new ClientServicesException(null, Messages.NullCommunityIdException);
		}
		String url = CommunityUrls.COMMUNITY_MEMBERS.format(this, CommunityUrls.getCommunityUuid(communityUuid));

		Object communityPayload;
		Member member = new Member(this, contributorId);
		CommunityMemberSerializer serializer = new CommunityMemberSerializer(member);
		communityPayload = serializer.createPayload();
		Response response = createData(url, parameters, communityPayload);
		checkResponseCode(response, HTTPCode.CREATED);
	}
	
	/**
	 * Decline an outstanding community invitation, user should be authenticated to perform this operation
	 * 
	 * @method declineInvite
	 * @param communityUuid 
	 * 				 community Id for which invite is sent
	 * @param contributorId
	 *				 user id of contributor
	 * @return boolean
	 * @throws ClientServicesException
	 */
	
	public void declineInvite(String communityUuid, String contributorId) throws ClientServicesException {
		declineInvite(communityUuid, contributorId, null);
	}
	
	/**
	 * Decline an outstanding community invitation, user should be authenticated to perform this operation
	 * 
	 * @method declineInvite
	 * @param communityUuid 
	 * 				 community Id for which invite is sent
	 * @param contributorId
	 *				 user id of contributor
	 * @return boolean
	 * @throws ClientServicesException
	 */

	public void declineInvite(String communityUuid, String contributorId, Map<String, String> parameters) throws ClientServicesException {

		if (StringUtil.isEmpty(communityUuid)){
			throw new ClientServicesException(null, Messages.NullCommunityIdException);
		}
		if(EntityUtil.isEmail(contributorId)){
			parameters.put("email", contributorId);
		}
		else{
			parameters.put("userid", contributorId);	
		}
		String url = CommunityUrls.COMMUNITY_INVITES.format(this, CommunityUrls.getCommunityUuid(communityUuid));
		Response response = deleteData(url, parameters, communityUuid);
		checkResponseCode(response, HTTPCode.OK);
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
		return getInvite(communityUuid, inviteUuid, null);
	}
	
	/**
	 * Retrieve a community invite.
	 * 
	 * @method getInvite
	 * @param {String} communityUuid
	 * @param (String} inviteUuid
	 * @throws ClientServicesException
	 */
	public Invite getInvite(String communityUuid, String inviteUuid, Map<String, String> parameters) throws ClientServicesException{
		if (StringUtil.isEmpty(communityUuid) || StringUtil.isEmpty(inviteUuid)){
			throw new ClientServicesException(null, Messages.getInviteException);
		}
		parameters.put(USERID, inviteUuid); // the parameter name should be inviteUuid, this is a bug on connections
		String url = CommunityUrls.COMMUNITY_INVITES.format(this, CommunityUrls.getCommunityUuid(communityUuid));
		return getInviteEntity(url, parameters);
	}

	/***************************************************************
	 * Working with community members
	 ****************************************************************/

	/**
	 * Get member of a community.
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
		if(memberId.contains("@")){
			parameters.put("email", memberId);
		}
		else{
			parameters.put("userid", memberId);
		}
		String url = CommunityUrls.COMMUNITY_MEMBERS.format(this,CommunityUrls.getCommunityUuid(communityUuid));		

		return getMemberEntity(url, parameters);
	}
	
	/**
	 * Add member to a community.
	 * <p> 
	 * User should be logged in as a owner of the community to call this method
	 * 
	 * @param communityUuid 
	 * 				 Id of Community to which the member needs to be added
	 * @param memberId
	 * 				 Id of Member which is to be added
	 * @throws ClientServicesException
	 */
	public void addMember(String communityUuid, Member member) throws ClientServicesException {
		addMember(communityUuid, member, null);
	}
	
	/**
	 * Add member to a community.
	 * <p> 
	 * User should be logged in as a owner of the community to call this method
	 * 
	 * @param communityUuid 
	 * 				 Id of Community to which the member needs to be added
	 * @param memberId
	 * 				 Id of Member which is to be added
	 * @throws ClientServicesException
	 */
	public void addMember(String communityUuid, Member member, Map<String, String> parameters) throws ClientServicesException {
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

		if(StringUtil.isEmpty(member.getRole())){
			member.setRole("member"); //default role is member
		}
		member.setRole("member"); 
		
		Object communityPayload;
		CommunityMemberSerializer serializer = new CommunityMemberSerializer(member);
		communityPayload = serializer.createPayload();
		String url = CommunityUrls.COMMUNITY_MEMBERS.format(this,CommunityUrls.getCommunityUuid(communityUuid));
		Response response = createData(url, parameters, communityPayload);
		checkResponseCode(response, HTTPCode.CREATED);
	}
	
	/**
	 * Update member of a community.
	 * <p> 
	 * User should be logged in as a owner of the community to call this method
	 * 
	 * @param community 
	 * 				 Id of Community 
	 * @param memberId
	 * 				 Id of Member 
	 * @throws ClientServicesException
	 */
	public void updateMember(String communityUuid, Member member) throws ClientServicesException {
		if (StringUtil.isEmpty(communityUuid)){
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

		parameters.put("userid", member.getUserid());
		parameters.put("email", member.getEmail());

		Object memberPayload;
		CommunityMemberSerializer serializer = new CommunityMemberSerializer(member);
		memberPayload = serializer.updatePayload();
		String url = CommunityUrls.COMMUNITY_MEMBERS.format(this, CommunityUrls.getCommunityUuid(communityUuid));
		Response response = updateData(url, parameters, memberPayload, null);
		checkResponseCode(response, HTTPCode.OK);
	}
	
	/**
	 * Remove member from a community.
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
		if(EntityUtil.isEmail(memberId)){
			parameters.put("email", memberId);
		}else{
			parameters.put("userid", memberId);
		}

		String url = CommunityUrls.COMMUNITY_MEMBERS.format(this,CommunityUrls.getCommunityUuid(communityUuid));
		Response response = deleteData(url, parameters, COMMUNITY_UNIQUE_IDENTIFIER);
		checkResponseCode(response, HTTPCode.OK);
	}

	//------------------------------------------------------------------------------------------------------------------
	// Working with community files
	//------------------------------------------------------------------------------------------------------------------

	/**
	 * Get a list of Community Files
	 * @param communityId
	 * @param params
	 * @return FileList
	 * @throws ClientServicesException
	 */
	public EntityList<File> getCommunityFiles(String communityId, HashMap<String, String> params) throws ClientServicesException {
		FileService fileService = new FileService(endpoint);
		return fileService.getCommunityFiles(communityId, params);
	}

	/**
	 * Download a community file
	 * @param ostream
	 * @param fileId
	 * @param libraryId - Library Id of which the file is a part. This value can be obtained by using File's getLibraryId method.
	 * @param params
	 * @return long
	 * @throws ClientServicesException
	 */
	public long downloadCommunityFile(OutputStream ostream, final String fileId, final String libraryId, Map<String, String> params) throws ClientServicesException {
		FileService svc = new FileService(endpoint);
		return svc.downloadCommunityFile(ostream, fileId, libraryId, params);
	}

	/**
	 * Upload a File to Community
	 * @param iStream
	 * @param communityId
	 * @param title
	 * @param length
	 * @throws ClientServicesException
	 */
	public File uploadFile(InputStream iStream, String communityId, final String title, long length) throws ClientServicesException {
		FileService svc = new FileService(endpoint);
		return svc.uploadCommunityFile(iStream, communityId, title, length);
	}

	/***************************************************************
	 * Factory methods
	 ****************************************************************/

	protected Community getCommunityEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntity(requestUrl, parameters, getCommunityFeedHandler());
	}

	protected Member getMemberEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntity(requestUrl, parameters, getMemberFeedHandler());
	}

	protected Invite getInviteEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntity(requestUrl, parameters, getInviteFeedHandler());
	}

	protected EntityList<Community> getCommunityEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getCommunityFeedHandler());
	}

	protected EntityList<Member> getMemberEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getMemberFeedHandler());
	}

	protected EntityList<Invite> getInviteEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getInviteFeedHandler());
	}

	/***************************************************************
	 * FeedHandlers for each entity type
	 ****************************************************************/

	/**
	 * Factory method to instantiate a FeedHandler for Communities
	 * @return IFeedHandler<Community>
	 */
	protected IFeedHandler<Community> getCommunityFeedHandler() {
		return new AtomFeedHandler<Community>(this, false) {
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
	protected IFeedHandler<Member> getMemberFeedHandler() {
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
	protected IFeedHandler<Invite> getInviteFeedHandler() {
		return new AtomFeedHandler<Invite>(this) {
			@Override
			protected Invite entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new Invite(service, node, nameSpaceCtx, xpath);
			}

		};
	}
}
