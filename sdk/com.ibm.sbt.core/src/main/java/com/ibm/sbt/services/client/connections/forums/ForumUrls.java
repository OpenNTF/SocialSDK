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
package com.ibm.sbt.services.client.connections.forums;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.v4_0;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.URLBuilder;
import com.ibm.sbt.services.client.base.URLContainer;
import com.ibm.sbt.services.client.base.Version;
import com.ibm.sbt.services.client.base.VersionedUrl;

/**
 * This class handles and formats the URLS for the ForumService
 * 
 * @author Carlos Manias
 */
public enum ForumUrls implements URLContainer {
	FORUMS(new VersionedUrl(v4_0, "{forums}/{authType}/atom/forums")),
	FORUM(new VersionedUrl(v4_0, "{forums}/{authType}/atom/forum")),
	FORUMS_PUBLIC(new VersionedUrl(v4_0, "{forums}/{authType}/atom/forums/public")),
	FORUMS_MY(new VersionedUrl(v4_0, "{forums}/{authType}/atom/forums/my")),
	TAGS_FORUMS(new VersionedUrl(v4_0, "{forums}/{authType}/atom/tags/forums")),
	TAGS_TOPICS(new VersionedUrl(v4_0, "{forums}/{authType}/atom/tags/topics")),
	RECOMMENDATION_ENTRIES(new VersionedUrl(v4_0, "{forums}/{authType}/atom/recommendation/entries")),
	TOPIC(new VersionedUrl(v4_0, "{forums}/{authType}/atom/topic")),
	TOPICS(new VersionedUrl(v4_0, "{forums}/{authType}/atom/topics")),
	TOPICS_MY(new VersionedUrl(v4_0, "{forums}/{authType}/atom/topics/my")),
	REPLIES(new VersionedUrl(v4_0, "{forums}/{authType}/atom/replies")),
	REPLY(new VersionedUrl(v4_0, "{forums}/{authType}/atom/reply"));

	private URLBuilder builder;
	
	private ForumUrls(VersionedUrl... urlVersions) {
		builder = new URLBuilder(urlVersions);
	}
	
	public String format(BaseService service, NamedUrlPart... args) {
		return builder.format(service, args);
	}

	public String getPattern(Version version){
		return builder.getPattern(version).getUrlPattern();
	}

}
