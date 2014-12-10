package com.ibm.sbt.services.client.connections.communities;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.v4_0;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.URLBuilder;
import com.ibm.sbt.services.client.base.URLContainer;
import com.ibm.sbt.services.client.base.Version;
import com.ibm.sbt.services.client.base.VersionedUrl;

/**
 * @author Christian Gosch, inovex GmbH, based on code by Carlos Manias
 */
public enum WidgetCommunityUrls implements URLContainer {

	/** Community widgets Atom Feed URL; parameters: communityUuid; widgetInstanceId */
	COMMUNITY_WIDGETS_FEED(new VersionedUrl(v4_0, 		"{communities}/service/atom/{authType}/community/widgets")),
	/** Community widgets Browser/HTML URL; parameters: communityUuid; filter=status */
	COMMUNITY_WIDGETS_HTML(new VersionedUrl(v4_0, 		"{communities}/service/html/{authType}/community/widgets"));

	private URLBuilder builder;
	
	private WidgetCommunityUrls(VersionedUrl... urlVersions) {
		builder = new URLBuilder(urlVersions);
	}
	
	public String format(BaseService service, NamedUrlPart... args) {
		return builder.format(service, args);
	}

	public String getPattern(Version version){
		return builder.getPattern(version).getUrlPattern();
	}
}
