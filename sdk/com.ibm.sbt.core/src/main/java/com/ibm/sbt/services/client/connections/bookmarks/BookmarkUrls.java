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
package com.ibm.sbt.services.client.connections.bookmarks;

import static com.ibm.sbt.services.client.base.CommonConstants.AT;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.v4_0;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.URLBuilder;
import com.ibm.sbt.services.client.base.Version;
import com.ibm.sbt.services.client.base.VersionedUrl;

/**
 * Class used in constructing URL for Blogs service
 * @author Swati Singh
 * @author Carlos Manias
 */

public enum BookmarkUrls {
	
	ALL(new VersionedUrl(v4_0, 					"{dogear}/atom")),
	APP(new VersionedUrl(v4_0, 					"{dogear}/api/app?{userId}")),
	POPULAR(new VersionedUrl(v4_0, 				"{dogear}/atom/popular")),
	MYNOTIFICATIONS(new VersionedUrl(v4_0, 		"{dogear}/atom/mynotifications")),
	MYSENTNOTIFICATIONS(new VersionedUrl(v4_0, 	"{dogear}/atom/mysentnotifications")),
	userId("userid", "email");
	
	private String keyParam ;
	private String emailParam ;
	private URLBuilder builder;
	
	public String format(BaseService service, NamedUrlPart... args) {
		return builder.format(service, args);
	}

	public String getPattern(Version version){
		return builder.getPattern(version).getUrlPattern();
	}
	
	public NamedUrlPart get(String id){
		String paramName = isEmail(id)?emailParam:keyParam;
		return new NamedUrlPart(name(), paramName+"="+id);
	}
	
	private BookmarkUrls(VersionedUrl... urlVersions) {
		builder = new URLBuilder(urlVersions);
	}
	private BookmarkUrls(String userid, String email){
		this.keyParam = userid;
		this.emailParam = email;
	}
	
	private static boolean isEmail(String id) {
		return (id == null) ? false : id.contains(AT);
	}
	

}
