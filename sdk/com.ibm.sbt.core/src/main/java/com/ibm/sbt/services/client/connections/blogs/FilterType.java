/*
 * � Copyright IBM Corp. 2013
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

/**
 * Class used in constructing URL for Blogs service
 * @author Swati Singh
 */

public enum FilterType {
	
	ALL_BLOGS("/{blogs}/{blogHandle}/feed/blogs/atom"),
	MY_BLOGS("/{blogs}/{blogHandle}/api/blogs"),
	FEATURED_BLOGS("/{blogs}/{blogHandle}/feed/featuredblogs/atom"),
	ALL_BLOG_POSTS("/{blogs}/{blogHandle}/feed/entries/atom"),
	ALL_FEATURED_BLOG_POSTS("/{blogs}/{blogHandle}/feed/featured/atom"),
	ALL_RECOMMENDED_BLOG_POSTS("/{blogs}/{blogHandle}/feed/recommended/atom"),
	ALL_BLOG_COMMENTS("/{blogs}/{blogHandle}/feed/comments/atom"),
	ALL_BLOG_TAGS("/{blogs}/{blogHandle}/feed/tags/atom"),
	BLOG_POSTS("/{blogs}/{blogHandle}/feed/entries/atom"),
	BLOG_COMMENTS("/{blogs}/{blogHandle}/feed/comments/atom"),
	BLOG_TAGS("/{blogs}/{blogHandle}/feed/tags/atom"),
	CREATE_BLOG("/{blogs}/{blogHandle}/api/blogs"),
	GET_UPDATE_REMOVE_BLOG("/{blogs}/{blogHandle}/api/blogs/{entryAnchor}"),
	BLOG_POST("/{blogs}/{blogHandle}/feed/entry/atom"),
	CREATE_BLOG_POST("/{blogs}/{blogHandle}/api/entries"),
	UPDATE_REMOVE_POST("/{blogs}/{blogHandle}/api/entries/{entryAnchor}"),
	CREATE_COMMENT("/{blogs}/{blogHandle}/api/comments/{entryAnchor}"),
	GET_REMOVE_COMMENT("/{blogs}/{blogHandle}/api/comments/{entryAnchor}"),
	BLOG_ENTRYCOMMENTS("/{blogs}/{blogHandle}/feed/entrycomments/{entryAnchor}/atom"),
	RECOMMEND_POST("/{blogs}/{blogHandle}/api/recommend/entries/{entryAnchor}");

	String filterType;
	
	private FilterType(String filterType) {
		this.filterType = filterType;
	}
	
	/**
	 * Wrapper method to return filter type
	 * <p>
	 */
	public String getFilterType(){return filterType;}

}
