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

package com.ibm.sbt.services.client.connections.blogs.model;

import java.util.Arrays;
import java.util.List;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.blogs.BlogService;
import com.ibm.sbt.services.client.connections.blogs.BlogServiceException;
import com.ibm.sbt.services.client.connections.blogs.model.BlogXPath;
import com.ibm.sbt.services.client.connections.blogs.model.Author;

/**
 * Base model object to be used with Blogs, Posts and Comments
 * 
 * @author Swati Singh 
 */


public class BaseBlogEntity extends BaseEntity {
	
	private final String BLOGID = "urn:lsid:ibm.com:blogs:blog-";
	private final String POSTID = "urn:lsid:ibm.com:blogs:entry-";
	private final String COMMENTID = "urn:lsid:ibm.com:blogs:comment-";
	
	/**
	 * Constructor
	 *  
	 * @param ForumService
	 * @param ForumId
	 */
	public BaseBlogEntity(BlogService blogService, String id) {
		setService(blogService);
		setAsString(BlogXPath.uid, id);
	}

	public BaseBlogEntity(BaseService svc, DataHandler<?> handler) {
		super(svc,handler);
	}
	
	public String getUid(){
		String id = getAsString(BlogXPath.uid);
		if(StringUtil.startsWithIgnoreCase(id, BLOGID)){
			id = id.substring(BLOGID.length());
		}
		else if(StringUtil.startsWithIgnoreCase(id, POSTID)){
			id = id.substring(POSTID.length());
		}
		else{
			id = id.substring(COMMENTID.length());
		}
		return id;
	}
	
	public String getPublished(){
		return getAsString(BlogXPath.published);
		
	}
	public Author getAuthor(){
		return new Author(super.dataHandler);
	}
	
	public String getUpdated(){
		return getAsString(BlogXPath.updated);
	}
	
	public String getTitle(){
		return getAsString(BlogXPath.title);
	}

	public String getAlternateUrl() throws BlogServiceException{
		return getAsString(BlogXPath.alternateUrl);
	}
	

	
	public String getRecommendationsCount() throws BlogServiceException{
		return getAsString(BlogXPath.recommendationsCount);
	}
	
	
	public void setTitle(String title) {
		setAsString(BlogXPath.title, title);
	}
	

	/**
	 * @return the list of Tags
	 */
	
	public List<String> getTags() {
		return (List<String>) Arrays.asList(getDataHandler().getAsString(BlogXPath.tags).split(" "));
	}
	
	/**
	 * @sets the tags
	 */
	public void setTags(List<String> tags) {
		if(!tags.isEmpty()){
			for (int i = 0; i < tags.size(); i++){
				   fields.put("tag" + i , tags.get(i));
			}
		}
	}
	
	@Override
	public BlogService getService(){
		return (BlogService)super.getService();
	}
	
	@Override
	public XmlDataHandler getDataHandler(){
		return (XmlDataHandler)super.getDataHandler();
	}
	
    /*
     * Method used to extract the blog uuid for an id string.
     */
	public String extractBlogUuid(String uid) {
        if (StringUtil.isNotEmpty(uid) && uid.indexOf(BLOGID) == 0) {
            return uid.substring(BLOGID.length());
        } else {
            return uid;
        }
    }; 

}
