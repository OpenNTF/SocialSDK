/*
 * © Copyright IBM Corp. 2014
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

import static com.ibm.sbt.services.client.base.ConnectionsConstants.v4_0;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.URLBuilder;
import com.ibm.sbt.services.client.base.URLContainer;
import com.ibm.sbt.services.client.base.Version;
import com.ibm.sbt.services.client.base.VersionedUrl;

/**
 * @author Carlos Manias
 */
public enum CommunityUrls implements URLContainer {
	COMMUNITIES_ALL(new VersionedUrl(v4_0, 			"{communities}/service/atom/{authType}/communities/all")),
	COMMUNITIES_MY(new VersionedUrl(v4_0, 			"{communities}/service/atom/{authType}/communities/my")),
	COMMUNITY_INSTANCE(new VersionedUrl(v4_0, 		"{communities}/service/atom/{authType}/community/instance?{communityUuid}")),
	COMMUNITY_UPDATE(new VersionedUrl(v4_0, 		"{communities}/service/atom/{authType}/community/instance")),
	COMMUNITY_MEMBERS(new VersionedUrl(v4_0, 		"{communities}/service/atom/{authType}/community/members?{communityUuid}")),
	COMMUNITY_SUBCOMMUNITIES(new VersionedUrl(v4_0, "{communities}/service/atom/{authType}/community/subcommunities?{communityUuid}")),
	COMMUNITY_MEMBER(new VersionedUrl(v4_0, 		"{communities}/service/atom/{authType}/community/members?{communityUuid}&{userid}")),
	COMMUNITY_BOOKMARKS(new VersionedUrl(v4_0, 		"{communities}/service/atom/{authType}/community/bookmarks")),
	COMMUNITY_FORUMTOPICS(new VersionedUrl(v4_0, 	"{communities}/service/atom/{authType}/community/forum/topics")),
	COMMUNITY_MYINVITES(new VersionedUrl(v4_0, 		"{communities}/service/atom/{authType}/community/invites/my")),
	COMMUNITY_INVITES(new VersionedUrl(v4_0, 		"{communities}/service/atom/{authType}/community/invites?{communityUuid}")),
	COMMUNITY_REMOVE_APPLICATIONS(new VersionedUrl(v4_0, "{communities/service/atom/community/remoteApplications?{communityUuid}"));

	private URLBuilder builder;
	
	public static NamedUrlPart getCommunityUuid(String id){
		return new NamedUrlPart("communityUuid", "communityUuid"+"="+id);		
	}
	
	public static NamedUrlPart getUserid(String id){
		return new NamedUrlPart("userid", "userid"+"="+id);		
	}
	
	public String format(BaseService service, NamedUrlPart... args) {
		return builder.format(service, args);
	}

	public String getPattern(Version version){
		return builder.getPattern(version).getUrlPattern();
	}
	
	private CommunityUrls(VersionedUrl... urlVersions) {
		builder = new URLBuilder(urlVersions);
	}
}


