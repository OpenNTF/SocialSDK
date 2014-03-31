package com.ibm.sbt.services.client.connections.communities;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.URLBuilder;
import com.ibm.sbt.services.client.base.URLContainer;
import com.ibm.sbt.services.client.base.Version;
import com.ibm.sbt.services.client.base.VersionedUrl;

public enum CommunityUrls implements URLContainer {
	COMMUNITIES_ALL(new VersionedUrl(ConnectionsConstants.v4_0, "{communities}/service/atom/{authType}/communities/all")),
	COMMUNITIES_MY(new VersionedUrl(ConnectionsConstants.v4_0, "{communities}/service/atom/{authType}/communities/my")),
	COMMUNITY_INSTANCE(new VersionedUrl(ConnectionsConstants.v4_0, "{communities}/service/atom/{authType}/community/instance")),
	COMMUNITY_MEMBERS(new VersionedUrl(ConnectionsConstants.v4_0, "{communities}/service/atom/{authType}/community/members")),
	COMMUNITY_SUBCOMMUNITIES(new VersionedUrl(ConnectionsConstants.v4_0, "{communities}/service/atom/{authType}/community/subcommunities")),
	COMMUNITY_BOOKMARKS(new VersionedUrl(ConnectionsConstants.v4_0, "{communities}/service/atom/{authType}/community/bookmarks")),
	COMMUNITY_FORUMTOPICS(new VersionedUrl(ConnectionsConstants.v4_0, "{communities}/service/atom/{authType}/community/forum/topics")),
	COMMUNITY_MYINVITES(new VersionedUrl(ConnectionsConstants.v4_0, "{communities}/service/atom/{authType}/community/invites/my")),
	COMMUNITY_INVITES(new VersionedUrl(ConnectionsConstants.v4_0, "{communities}/service/atom/{authType}/community/invites"));
	// CommunityType remoteApplications???

	private URLBuilder builder;
	
	private CommunityUrls(VersionedUrl... urlVersions) {
		builder = new URLBuilder(urlVersions);
	}
	
	public String format(BaseService service, NamedUrlPart... args) {
		return builder.format(service, args);
	}

	public String getPattern(Version version){
		return builder.getPattern(version).getUrlPattern();
	}
}
