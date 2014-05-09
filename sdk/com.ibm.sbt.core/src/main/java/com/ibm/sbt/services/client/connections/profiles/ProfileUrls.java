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
package com.ibm.sbt.services.client.connections.profiles;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.v4_0;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.URLBuilder;
import com.ibm.sbt.services.client.base.URLContainer;
import com.ibm.sbt.services.client.base.Version;
import com.ibm.sbt.services.client.base.VersionedUrl;

/**
 * This class handles and formats the URLS for the ProfileService
 * 
 * @author Carlos Manias
 *
 */
public enum ProfileUrls implements URLContainer {
	/**
	 * NON-ADMIN URLS
	 */
	PROFILE(new VersionedUrl(v4_0, 					"{profiles}/{authType}/atom/profile.do?{userId}")),
	PROFILE_ENTRY(new VersionedUrl(v4_0, 			"{profiles}/{authType}/atom/profileEntry.do?{userId}")),
	PHOTO(new VersionedUrl(v4_0, 					"{profiles}/{authType}/atom/photo.do?{userId}")),
	GET_TAGS(new VersionedUrl(v4_0, 				"{profiles}/{authType}/atom/profileTags.do?{targetId}")),
	TAGS(new VersionedUrl(v4_0, 					"{profiles}/{authType}/atom/profileTags.do?{sourceId}&{targetId}")),
	SEARCH(new VersionedUrl(v4_0, 					"{profiles}/{authType}/atom/search.do")),
	CONNECTIONS(new VersionedUrl(v4_0, 				"{profiles}/{authType}/atom/connections.do?{userId}")),
	CONNECTION(new VersionedUrl(v4_0, 				"{profiles}/{authType}/atom/connection.do?connectionId={connectionId}")),
	CHECK_COLLEAGUE(new VersionedUrl(v4_0, 			"{profiles}/{authType}/atom/connection.do?{sourceId}&{targetId}&connectionType=colleague")),
	CONNECTIONS_IN_COMMON(new VersionedUrl(v4_0, 	"{profiles}/{authType}/atom/connectionsInCommon.do?{userId}")),
	REPORTING_CHAIN(new VersionedUrl(v4_0, 			"{profiles}/{authType}/atom/reportingChain.do?{userId}")),
	PEOPLE_MANAGED(new VersionedUrl(v4_0, 			"{profiles}/{authType}/atom/peopleManaged.do?{userId}")),
	MESSAGES_ALL(new VersionedUrl(v4_0, 			"{profiles}/{authType}/atom/mv/theboard/entries/all.do")),
	MESSAGE_BOARD_ENTRIES(new VersionedUrl(v4_0, 	"{profiles}/{authType}/atom/mv/theboard/entries.do")),
	MESSAGE_BOARD_COMMENTS(new VersionedUrl(v4_0, 	"{profiles}/{authType}/atom/mv/theboard/comments.do")),
	MESSAGES_COLLEAGUES(new VersionedUrl(v4_0, 		"{profiles}/{authType}/atom/mv/theboard/entries/related.do")),
	MY_USER_ID(new VersionedUrl(v4_0, 				"{connections}/opensocial/basic/rest/people/@me/")),

	/**
	 * ADMIN URLS
	 */
	ADMIN_PROFILE_ENTRY(new VersionedUrl(v4_0, 		"{profiles}/{authType}/admin/atom/profileEntry.do?{userId}")),
	ADMIN_PROFILES(new VersionedUrl(v4_0, 			"{profiles}/{authType}/admin/atom/profiles.do"));
	
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
	
	public static NamedUrlPart getConnectionId(String value){
		return new NamedUrlPart("connectionId", value);
	}

}
