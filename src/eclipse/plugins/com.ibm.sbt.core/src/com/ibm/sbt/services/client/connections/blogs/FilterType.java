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

package com.ibm.sbt.services.client.connections.blogs;

/**
 * Class used in constructing URL for Blogs service
 * @author Swati Singh
 */

public enum FilterType {
	
	ALL("feed/blogs/atom"),
	MY("api/blogs"),
	FEATURED_BLOGS("feed/featuredblogs/atom"),
	BLOGS_POSTS("feed/entries/atom"),
	FEATURED_BLOGS_POSTS("feed/featured/atom"),
	RECOMMENDED_BLOGS_POSTS("feed/recommended/atom"),
	BLOGS_COMMENTS("feed/comments/atom"),
	BLOGS_TAGS("feed/tags/atom"),
	BLOG_POSTS("feed/entries/atom"),
	BLOG_COMMENTS("feed/comments/atom"),
	BLOG_TAGS("feed/tags/atom"),
	CREATE_BLOG("api/blogs"),
	GET_UPDATE_REMOVE_BLOG("api/blogs/"),
	BLOG_POST("feed/entry/atom"),
	CREATE_BLOG_POST("api/entries"),
	UPDATE_REMOVE_POST("api/entries/"),
	CREATE_COMMENT("api/comments/"),
	GET_REMOVE_COMMENT("api/comments/"),
	RECOMMEND_POST("api/recommend/entries/");
	

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
