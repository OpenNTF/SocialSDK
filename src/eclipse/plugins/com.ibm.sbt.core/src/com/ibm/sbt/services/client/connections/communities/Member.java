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

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.communities.model.CommunityXPath;

/**
 * This class represents a Connections Community Member entity
 * 
 * @Represents Connections Community Member
 * @author Manish Kataria
 * @author Carlos Manias
 */
public class Member extends BaseEntity {
	
	/**
     * The Uuid of the community associated with this Member
     */
    private String communityUuid;


	/**
	 * Constructor
	 * 
	 * @param communityService
	 * @param id
	 */
	public Member(CommunityService communityService, String id) {
		setService(communityService);
		if(id.contains("@")){
			setEmail(id);
		}
		else{
			setUserid(id);
		}
	}
	
	/**
	 * Constructor
	 * 
	 * @param svc
	 * @param handler
	 */
	public Member(CommunityService svc, DataHandler<?> handler){
		super(svc,handler);
	}

	public String getCommunityUuid(){
	    	String communityId = "";
	    	try {
	    		communityId = getAsString(CommunityXPath.id);
			} catch (Exception e) {}
	    	
	    	if(StringUtil.isEmpty(communityId)){
	    		communityId = communityUuid;
	    	}
	    	//extract the community id from /service/atom/community/members?communityUuid=7c91e35e-96c9-46e6-8b8d-77c4d2890de6&amp;email=boydgossens@renovations.com
	    	communityId = communityId.substring(communityId.indexOf("=")+1,communityId.indexOf("&"));
	    	return communityId;
	}
	/**
	 * 
	 * @return id
	 */
	public String getUserid() {
		return getAsString(CommunityXPath.contributorUserid);
	}

	/**
	 * 
	 * @return name
	 */
	public String getName() {
		return getAsString(CommunityXPath.contributorName);
	}

	/**
	 * 
	 * @return email
	 */
	public String getEmail() {
		return getAsString(CommunityXPath.contributorEmail);
	}
	
	/**
	 * @set name
	 */
	public void setName(String name) {
		setAsString(CommunityXPath.contributorName, name);
	}

	/**
	 * 
	 * @set id
	 */
	public void setUserid(String id) {
		setAsString(CommunityXPath.contributorUserid, id);
	}
	
	/**
	 * 
	 * @set email
	 */
	public void setEmail(String email) {
		setAsString(CommunityXPath.contributorEmail, email);
	}
	
	/**
	 * 
	 * @return role
	 */
	public String getRole() {
		return getAsString(CommunityXPath.role);
	}
	
	/**
	 * @set role
	 */
	public void setRole(String role){
		setAsString(CommunityXPath.role, role);
	}
		
	public Member load() throws CommunityServiceException
	{
	  	return getService().getMember(getCommunityUuid(), getUserid());
	}
	 
	public void remove() throws CommunityServiceException
	{
	  	getService().removeMember(getCommunityUuid(), getUserid());
	}
	
	@Override
	public CommunityService getService(){
		return (CommunityService)super.getService();
	}
	
	@Override
	public XmlDataHandler getDataHandler(){
		return (XmlDataHandler)super.getDataHandler();
	}
	
}
