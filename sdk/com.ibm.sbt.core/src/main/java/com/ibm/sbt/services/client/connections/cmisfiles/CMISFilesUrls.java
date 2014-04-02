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
package com.ibm.sbt.services.client.connections.cmisfiles;

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
public enum CMISFilesUrls implements URLContainer {
    GET_SERVICE_DOCUMENT(new VersionedUrl(ConnectionsConstants.v4_0, "{files}/basic/cmis/my/servicedoc")),
    GET_MY_FILES(new VersionedUrl(ConnectionsConstants.v4_0, "{files}/basic/cmis/repository/{repositoryId}/folderc/snx:files")),
    GET_FILES_SHARED_WITH_ME(new VersionedUrl(ConnectionsConstants.v4_0, "{files}/basic/cmis/repository/{repositoryId}/folderc/snx:virtual!.!filessharedwith")),
    GET_MY_COLLECTIONS(new VersionedUrl(ConnectionsConstants.v4_0, "{files}/basic/cmis/repository/{repositoryId}/folderc/snx:collections")),
    GET_COLLECTIONS_SHARED_WITH_ME(new VersionedUrl(ConnectionsConstants.v4_0, "{files}/basic/cmis/repository/{repositoryId}/folderc/snx:virtual!.!collectionssharedwith")),
    GET_MY_SHARES(new VersionedUrl(ConnectionsConstants.v4_0, "{files}/basic/api/myshares/feed")),
    
    ATOM_GET_USER_ID(new VersionedUrl(ConnectionsConstants.v4_0, "connections/opensocial/basic/rest/people/@me/")),
    ATOM_GET_SUBSCRIBER_ID(new VersionedUrl(ConnectionsConstants.v4_0,"connections/opensocial/basic/rest/people/@me/"));

	private URLBuilder builder;
	
	private CMISFilesUrls(VersionedUrl... urlVersions) {
		builder = new URLBuilder(urlVersions);
	}
	
	public String format(BaseService service, NamedUrlPart... args) {
		return builder.format(service, args);
	}

	public String getPattern(Version version){
		return builder.getPattern(version).getUrlPattern();
	}
}
