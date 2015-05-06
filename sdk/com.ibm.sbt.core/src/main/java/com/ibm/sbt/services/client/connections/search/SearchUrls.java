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
package com.ibm.sbt.services.client.connections.search;

import com.ibm.sbt.services.client.base.BaseService;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.*;

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
public enum SearchUrls implements URLContainer {
	SEARCH(new VersionedUrl(v4_0, "{search}/atom/search")),
	MYSEARCH(new VersionedUrl(v4_0, "{search}/atom/mysearch?{query}")),
	SCOPES(new VersionedUrl(v4_0, "{search}/atom/scopes"));

	private URLBuilder builder;
	
	private SearchUrls(VersionedUrl... urlVersions) {
		builder = new URLBuilder(urlVersions);
	}
	
	public String format(BaseService service, NamedUrlPart... args) {
		return builder.format(service, args);
	}

	public String getPattern(Version version){
		return builder.getPattern(version).getUrlPattern();
	}
	
	public static NamedUrlPart getQuery(String query){
		return new NamedUrlPart("query", "query"+"="+query);		
	} 
}
