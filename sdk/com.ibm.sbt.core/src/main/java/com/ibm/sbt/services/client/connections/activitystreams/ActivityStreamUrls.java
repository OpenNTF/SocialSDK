/*
 * ��� Copyright IBM Corp. 2014
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
package com.ibm.sbt.services.client.connections.activitystreams;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.URLBuilder;
import com.ibm.sbt.services.client.base.URLContainer;
import com.ibm.sbt.services.client.base.Version;
import com.ibm.sbt.services.client.base.VersionedUrl;

/**
 * @author Carlos Manias
 */
public enum ActivityStreamUrls implements URLContainer {
	AS_PUBLIC_ALL_STATUS( new VersionedUrl(ConnectionsConstants.v4_0, 	"{connections}/opensocial/{authType}/rest/activitystreams/@public/@all/@status")),
	AS_PUBLIC_ALL_ALL( new VersionedUrl(ConnectionsConstants.v4_0, 		"{connections}/opensocial/{authType}/rest/activitystreams/@public/@all/@all")),
	AS_ME_FRIENDS_ALL( new VersionedUrl(ConnectionsConstants.v4_0, 		"{connections}/opensocial/{authType}/rest/activitystreams/@me/@friends/@all")),
	AS_ME_FRIENDS_STATUS( new VersionedUrl(ConnectionsConstants.v4_0, 	"{connections}/opensocial/{authType}/rest/activitystreams/@me/@friends/@status")),
	AS_ME_ALL_STATUS( new VersionedUrl(ConnectionsConstants.v4_0, 		"{connections}/opensocial/{authType}/rest/activitystreams/@me/@all/@status")),
	AS_ME_FOLLOWING_STATUS( new VersionedUrl(ConnectionsConstants.v4_0, "{connections}/opensocial/{authType}/rest/activitystreams/@me/@following/@status")),
	AS_ME_ALL_COMMUNITIES( new VersionedUrl(ConnectionsConstants.v4_0, 	"{connections}/opensocial/{authType}/rest/activitystreams/@me/@all/@communities")),
	AS_USER_INVOLVED_ALL( new VersionedUrl(ConnectionsConstants.v4_0, 	"{connections}/opensocial/{authType}/rest/activitystreams/{user}/@involved/@all")),
	AS_COMMUNITY_ALL( new VersionedUrl(ConnectionsConstants.v4_0, 		"{connections}/opensocial/{authType}/rest/activitystreams/{user}/@all")),
	AS_ME_RESPONSES( new VersionedUrl(ConnectionsConstants.v4_0, 		"{connections}/opensocial/{authType}/rest/activitystreams/@me/@responses")),
	AS_ME_NOTESFROMME( new VersionedUrl(ConnectionsConstants.v4_0, 		"{connections}/opensocial/{authType}/rest/activitystreams/@me/@notesfromme")),
	AS_ME_ACTION( new VersionedUrl(ConnectionsConstants.v4_0, 			"{connections}/opensocial/{authType}/rest/activitystreams/@me/@action")),
	AS_ME_ACTION_APP( new VersionedUrl(ConnectionsConstants.v4_0, 		"{connections}/opensocial/{authType}/rest/activitystreams/@me/@action/{app}")),
	AS_ME_SAVED( new VersionedUrl(ConnectionsConstants.v4_0, 			"{connections}/opensocial/{authType}/rest/activitystreams/@me/@saved")),
	AS_ME_SAVED_APP( new VersionedUrl(ConnectionsConstants.v4_0,		"{connections}/opensocial/{authType}/rest/activitystreams/@me/@saved/{app}")),
	AS_ME_ALL_ALL( new VersionedUrl(ConnectionsConstants.v4_0, 			"{connections}/opensocial/{authType}/rest/activitystreams/@me/@all/@all")),
	AS_USER_GROUP_APP( new VersionedUrl(ConnectionsConstants.v4_0, 		"{connections}/opensocial/{authType}/rest/activitystreams/{user}/{group}/{app}")),
	UBLOG_USER_GROUP_APP( new VersionedUrl(ConnectionsConstants.v4_0, 	"{connections}/opensocial/{authType}/rest/ublog/{user}/{group}/{app}"));


	private URLBuilder builder;
	
	private ActivityStreamUrls(VersionedUrl... urlVersions) {
		builder = new URLBuilder(urlVersions);
	}
	
	public String format(BaseService service, NamedUrlPart... args) {
		return builder.format(service, args);
	}

	public String getPattern(Version version){
		return builder.getPattern(version).getUrlPattern();
	}
}
