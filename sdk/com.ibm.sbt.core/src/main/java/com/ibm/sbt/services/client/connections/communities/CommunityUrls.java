package com.ibm.sbt.services.client.connections.communities;

import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.URLBuilder;
import com.ibm.sbt.services.client.base.URLContainer;
import com.ibm.sbt.services.client.base.Version;
import com.ibm.sbt.services.client.base.VersionedUrl;

public enum CommunityUrls implements URLContainer {
	COMMUNITY_URL(new VersionedUrl(ConnectionsConstants.v4_0, "communities/service/atom/{authType}/{communityEntity}/{communityType}"));
	private URLBuilder builder;
	
	private CommunityUrls(VersionedUrl... urlVersions) {
		builder = new URLBuilder(urlVersions);
	}
	
	public String format(Version version, String... args) {
		return builder.format(version, args);
	}

	public String getPattern(Version version){
		return builder.getPattern(version).getValue();
	}
}
