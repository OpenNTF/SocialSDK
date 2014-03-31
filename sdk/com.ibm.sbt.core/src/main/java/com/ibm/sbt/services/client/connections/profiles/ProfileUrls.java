/*
 * � Copyright IBM Corp. 2014
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
package com.ibm.sbt.services.client.connections.profiles;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.URLBuilder;
import com.ibm.sbt.services.client.base.URLContainer;
import com.ibm.sbt.services.client.base.Version;
import com.ibm.sbt.services.client.base.VersionedUrl;

/**
 * 
 * @author Carlos Manias
 *
 */
public enum ProfileUrls implements URLContainer {
	PROFILE(new VersionedUrl(ConnectionsConstants.v4_0, "profiles/{authType}/atom/profile.do")),
	PROFILE_ENTRY(new VersionedUrl(ConnectionsConstants.v4_0, "profiles/{authType}/atom/profileEntry.do")),
	PHOTO(new VersionedUrl(ConnectionsConstants.v4_0, "profiles/{authType}/atom/photo.do")),
	TAGS(new VersionedUrl(ConnectionsConstants.v4_0, "profiles/{authType}/atom/profileTags.do")),
	SEARCH(new VersionedUrl(ConnectionsConstants.v4_0, "profiles/{authType}/atom/search.do")),
	CONNECTIONS(new VersionedUrl(ConnectionsConstants.v4_0, "profiles/{authType}/atom/connections.do")),
	CONNECTION(new VersionedUrl(ConnectionsConstants.v4_0, "profiles/{authType}/atom/connection.do")),
	CONNECTIONS_IN_COMMON(new VersionedUrl(ConnectionsConstants.v4_0, "profiles/{authType}/atom/connectionsInCommon.do")),
	REPORTING_CHAIN(new VersionedUrl(ConnectionsConstants.v4_0, "profiles/{authType}/atom/reportingChain.do")),
	PEOPLE_MANAGED(new VersionedUrl(ConnectionsConstants.v4_0, "profiles/{authType}/atom/peopleManaged.do")),
	MESSAGES_ALL(new VersionedUrl(ConnectionsConstants.v4_0, "profiles/{authType}/atom/mv/theboard/entries/all.do")),
	MESSAGE_BOARD_ENTRIES(new VersionedUrl(ConnectionsConstants.v4_0, "profiles/{authType}/atom/mv/theboard/entries.do")),
	MESSAGE_BOARD_COMMENTS(new VersionedUrl(ConnectionsConstants.v4_0, "profiles/{authType}/atom/mv/theboard/comments.do")),
	MESSAGES_COLLEAGUES(new VersionedUrl(ConnectionsConstants.v4_0, "profiles/{authType}/atom/mv/theboard/entries/related.do")),

	//ADMIN urls
	ADMIN_PROFILE_ENTRY(new VersionedUrl(ConnectionsConstants.v4_0, "profiles/{authType}/admin/atom/profileEntry.do")),
	ADMIN_PROFILES(new VersionedUrl(ConnectionsConstants.v4_0, "profiles/{authType}/admin/atom/profiles.do"));
	
	private URLBuilder builder;
	
	private ProfileUrls(VersionedUrl... urlVersions) {
		builder = new URLBuilder(urlVersions);
	}
	
	public String format(BaseService service, NamedUrlPart... args) {
		return builder.format(service, args);
	}

	public String getPattern(Version version){
		return builder.getPattern(version).getUrlPattern();
	}

}
