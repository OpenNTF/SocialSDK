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

package com.ibm.sbt.services.client.connections.activitystreams.model;

import com.ibm.sbt.services.client.base.datahandlers.DataHandler;


/**
 * Actor class for persisting Community information from Activity Stream Entry 
 * @author Manish Kataria
 */

public class Community {
	
	private final String COMMUNITY_PATTERN = "urn:lsid:lconn.ibm.com:communities.community:";
	
	public Community() {
		
	}
	
	public Community(DataHandler<?> dataHandler) {
		String communityId = dataHandler.getAsString(ASJsonPath.TargetId);
		communityId = communityId.substring(COMMUNITY_PATTERN.length(), communityId.length());
		setCommunityId(communityId);
		setCommunityName(dataHandler.getAsString(ASJsonPath.TargetDisplayName));
	}
	
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	public String getCommunityId() {
		return communityId;
	}
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
	private String communityName = "";
	private String communityId = "";

}
