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


import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.communities.model.CommunityXPath;
import com.ibm.sbt.services.client.connections.communities.serializers.CommunitySerializer;

/**
 * This class represents a Connections Community entity
 * 
 * @Represents Connections Community
 * @author Swati Singh
 * @author Manish Kataria
 * @author Carlos Manias
 */
public class Community extends AtomEntity {

	/**
	 * Constructor
	 *  
	 * @param communityService
	 * @param communityUuid
	 */
	public Community(CommunityService communityService, String communityUuid) {
		setService(communityService);
		setAsString(CommunityXPath.communityUuid, communityUuid);
	}

	public Community(){}

	/**
	 * Constructor
	 * 
	 * @param communityUuid
	 */
	public Community(String communityUuid) {
		setAsString(CommunityXPath.communityUuid, communityUuid);
	}

    /**
     * 
     * @param service
     * @param node
     * @param namespaceCtx
     * @param xpathExpression
     */
	public Community(BaseService service, Node node, NamespaceContext namespaceCtx, 
			XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}

	/**
	 * Constructor
	 * @param svc
	 * @param handler
	 */
	public Community(CommunityService svc, XmlDataHandler handler) {
		super(svc,handler);
	}

	/**
	 * Returns the communityUuid
	 * 
	 * @return communityUuid
	 */
	public String getCommunityUuid() {
		return getAsString(CommunityXPath.communityUuid);
	}

	/**
	 * @sets the communityUuid
	 * 
	 * @param communityUuid
	 */
	public void setCommunityUuid(String communityUuid) {
		setAsString(CommunityXPath.communityUuid, communityUuid);
	}

	/**
	 * Returns the Title
	 * 
	 * @return title
	 */
	public String getTitle() {
		return getAsString(CommunityXPath.title);
	}

	/**
	 * @sets the title
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		setAsString(CommunityXPath.title, title);
	}

	/**
	 * Returns the content
	 * 
	 * @return content
	 */
	public String getContent() {
		return getAsString(CommunityXPath.content);
	}

	/**
	 * Returns the communityUrl
	 * 
	 * @return communityUrl
	 */
	public String getCommunityUrl() {
		return getAsString(CommunityXPath.communityUrl);
	}

	/**
	 * Returns the logoUrl
	 * 
	 * @return logoUrl
	 */
	public String getLogoUrl() {
		return getAsString(CommunityXPath.logoUrl);
	}

	/**
	 * Returns the Summary
	 * 
	 * @return summary
	 */
	public String getSummary() {
		return getAsString(CommunityXPath.summary);
	}

	/**
	 * @sets the content
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		setAsString(CommunityXPath.content, content);
	}

	/**
	 * @return the list of Tags
	 */

	public List<String> getTags() {
		return (List<String>) Arrays.asList(getDataHandler().getAsArray(CommunityXPath.tags));
	}

	/**
	 * @sets the tags
	 */
	public void setTags(List<String> tags) {
		if(!tags.isEmpty()){
			for (int i = 0; i < tags.size(); i++){
				   fields.put((CommunityXPath.tags).toString() + i , tags.get(i));
			}
		}
	}

	/**
	 * @sets the tags
	 */
	public void setTags(String tags) {
		setAsString(CommunityXPath.tags, tags);
	}

	/**
	 * @return the memberCount
	 */
	public int getMemberCount(){
		return getAsInt(CommunityXPath.memberCount);
	}
	/**
	 * @return the communityType
	 */
	public String getCommunityType(){
		return getAsString(CommunityXPath.communityType);		
	}

	/**
	 * @set the communityType
	 */
	public void setCommunityType(String communityType){
		setAsString(CommunityXPath.communityType, communityType);	
	}

	/**
	 * @return the published date of community
	 */
	public Date getPublished(){
		return getAsDate(CommunityXPath.published);
	}

	/**
	 * @return the community theme
	 */
	public String getCommunityTheme(){
		return getAsString(CommunityXPath.communityTheme);
	}

	public void setCommunityTheme(String theme) {
		setAsString(CommunityXPath.communityTheme, theme);
	}
	/**
	 * @return the update date of community
	 */
	public Date getUpdated(){
		return getAsDate(CommunityXPath.updated);
	}


	/**
	 * @return the authorUid
	 */
	public Member getAuthor(){
		Member author = new Member(getService(), getAsString(CommunityXPath.authorUserid));
		author.setName(getAsString(CommunityXPath.authorName));
		author.setEmail(getAsString(CommunityXPath.authorEmail));
		return author;
	}

	/**
	 * @return the ContributorId
	 */
	public Member getContributor(){
		Member contributor = new Member(getService(), getAsString(CommunityXPath.contributorUserid));
		contributor.setName(getAsString(CommunityXPath.contributorName));
		contributor.setEmail(getAsString(CommunityXPath.contributorEmail));
		return contributor;
	}

	/**
	 * @return the membersUrl
	 */
	public String getMembersUrl() {
		return getAsString(CommunityXPath.membersUrl);
	}

	/**
	 * This method is used by communityService wrapper methods to construct request body for Add/Update operations
	 * @return Object
	 */
	public Object constructCreateRequestBody() throws ClientServicesException {
		return createCommunityRequestPayload();
	}

	/**
	 * This method loads the community 
	 * 
	 * @return
	 * @throws ClientServicesException
	 */

	public Community load() throws ClientServicesException {
		return getService().getCommunity(getCommunityUuid());
    	}

	/**
	 * This method updates the community 
	 * 
	 * @return
	 * @throws ClientServicesException
	 */

	public void update() throws ClientServicesException {
		getService().updateCommunity(this);
    	}

	/**
	 * This method deletes the community on the server
	 * 
	 * @return
	 * @throws ClientServicesException
	 */

	public void remove() throws ClientServicesException {
	   	getService().deleteCommunity(getCommunityUuid());
	}
	/**
	 * This method updates the community on the server
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	public Community save() throws ClientServicesException {
		if(StringUtil.isEmpty(getCommunityUuid())) {
			String id = getService().createCommunity(this);
			return getService().getCommunity(id);
		} else {
			getService().updateCommunity(this);
			return getService().getCommunity(getCommunityUuid());
		}
	}

	/**
	 * This method gets Community member
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	public Member getMember(String memberID) throws ClientServicesException {
		return getService().getMember(getCommunityUuid(), memberID );
    	}

	/**
	 * This method adds Community member
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	public void addMember(Member member) throws ClientServicesException {
		getService().addMember(getCommunityUuid(), member);
    }

	/**
	 * This method removes Community member
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	public void removeMember(String memberID) throws ClientServicesException {
		getService().removeMember(getCommunityUuid(), memberID );
    	}
    	
	/**
	 * This method gets the subcommunities of a community
	 * 
	 * @return list of sub-communities
	 * @throws ClientServicesException
	 */
	public EntityList<Community> getSubCommunities() throws ClientServicesException {
	   	return getService().getSubCommunities(getCommunityUuid());
	}

	/**
	 * This method gets the subcommunities of a community
	 * 
	 * @param parameters
     * 				 Various parameters that can be passed to get a feed of members of a community. 
     * 				 The parameters must be exactly as they are supported by IBM Connections like ps, sortBy etc.
   	 * @return list of sub-communities
	 * @throws ClientServicesException
	 */
	public EntityList<Community> getSubCommunities(Map<String, String> parameters) throws ClientServicesException {
	   	return getService().getSubCommunities(getCommunityUuid(), parameters );
	}

	/**
	 * This method gets the members of a community
	 * 
	 * @return list of members
	 * @throws ClientServicesException
	 */
	public EntityList<Member> getMembers() throws ClientServicesException {
	   	return getService().getMembers(getCommunityUuid());
	}

	/**
	 * This method gets the members of a community
	 * 
	 * @param parameters
     * 				 Various parameters that can be passed to get a feed of members of a community. 
     * 				 The parameters must be exactly as they are supported by IBM Connections like ps, sortBy etc.
 
	 * @return list of members
	 * @throws ClientServicesException
	 */
	public EntityList<Member> getMembers(Map<String, String> parameters) throws ClientServicesException {
	   	return getService().getMembers(getCommunityUuid(), parameters);
	}

	private String createCommunityRequestPayload() throws ClientServicesException {
		CommunitySerializer serializer = new CommunitySerializer(this);
		String xml = serializer.createPayload();
		return xml;		
	}

	@Override
	public CommunityService getService() {
		return (CommunityService)super.getService();
	}

	@Override
	public XmlDataHandler getDataHandler() {
		return (XmlDataHandler)super.getDataHandler();
	}

}