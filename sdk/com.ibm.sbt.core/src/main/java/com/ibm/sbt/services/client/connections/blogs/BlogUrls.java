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
package com.ibm.sbt.services.client.connections.blogs;

import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.URLBuilder;
import com.ibm.sbt.services.client.base.URLContainer;
import com.ibm.sbt.services.client.base.Version;
import com.ibm.sbt.services.client.base.VersionedUrl;

/**
 * @author Carlos Manias
 */
public enum BlogUrls implements URLContainer {

	ALL_BLOGS(new VersionedUrl(ConnectionsConstants.v4_0, "/blogs/{blogHandle}/feed/blogs/atom")),
	MY_BLOGS(new VersionedUrl(ConnectionsConstants.v4_0, "/blogs/{blogHandle}/api/blogs")),
	FEATURED_BLOGS(new VersionedUrl(ConnectionsConstants.v4_0, "/blogs/{blogHandle}/feed/featuredblogs/atom")),
	ALL_BLOG_POSTS(new VersionedUrl(ConnectionsConstants.v4_0, "/blogs/{blogHandle}/feed/entries/atom")),
	ALL_FEATURED_BLOG_POSTS(new VersionedUrl(ConnectionsConstants.v4_0, "/blogs/{blogHandle}/feed/featured/atom")),
	ALL_RECOMMENDED_BLOG_POSTS(new VersionedUrl(ConnectionsConstants.v4_0, "/blogs/{blogHandle}/feed/recommended/atom")),
	ALL_BLOG_COMMENTS(new VersionedUrl(ConnectionsConstants.v4_0, "/blogs/{blogHandle}/feed/comments/atom")),
	ALL_BLOG_TAGS(new VersionedUrl(ConnectionsConstants.v4_0, "/blogs/{blogHandle}/feed/tags/atom")),
	BLOG_POSTS(new VersionedUrl(ConnectionsConstants.v4_0, "/blogs/{blogHandle}/feed/entries/atom")),
	BLOG_COMMENTS(new VersionedUrl(ConnectionsConstants.v4_0, "/blogs/{blogHandle}/feed/comments/atom")),
	BLOG_TAGS(new VersionedUrl(ConnectionsConstants.v4_0, "/blogs/{blogHandle}/feed/tags/atom")),
	CREATE_BLOG(new VersionedUrl(ConnectionsConstants.v4_0, "/blogs/{blogHandle}/api/blogs")),
	GET_UPDATE_REMOVE_BLOG(new VersionedUrl(ConnectionsConstants.v4_0, "/blogs/{blogHandle}/api/blogs/{entryAnchor}")),
	BLOG_POST(new VersionedUrl(ConnectionsConstants.v4_0, "/blogs/{blogHandle}/feed/entry/atom")),
	CREATE_BLOG_POST(new VersionedUrl(ConnectionsConstants.v4_0, "/blogs/{blogHandle}/api/entries")),
	UPDATE_REMOVE_POST(new VersionedUrl(ConnectionsConstants.v4_0, "/blogs/{blogHandle}/api/entries/{entryAnchor}")),
	CREATE_COMMENT(new VersionedUrl(ConnectionsConstants.v4_0, "/blogs/{blogHandle}/api/comments/{entryAnchor}")),
	GET_REMOVE_COMMENT(new VersionedUrl(ConnectionsConstants.v4_0, "/blogs/{blogHandle}/api/comments/{entryAnchor}")),
	BLOG_ENTRYCOMMENTS(new VersionedUrl(ConnectionsConstants.v4_0, "/blogs/{blogHandle}/feed/entrycomments/{entryAnchor}/atom")),
	RECOMMEND_POST(new VersionedUrl(ConnectionsConstants.v4_0, "/blogs/{blogHandle}/api/recommend/entries/{entryAnchor}")); ;

	private URLBuilder builder;
	
	private BlogUrls(VersionedUrl... urlVersions) {
		builder = new URLBuilder(urlVersions);
	}
	
	public String format(Version version, NamedUrlPart... args) {
		return builder.format(version, args);
	}

	public String getPattern(Version version){
		return builder.getPattern(version).getUrlPattern();
	}
}
