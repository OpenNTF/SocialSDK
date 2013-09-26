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

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.blogs.model.BaseBlogEntity;
import com.ibm.sbt.services.client.connections.blogs.model.BlogXPath;


/**
 * BlogComment model object
 * 
 * @author Swati Singh
 */


public class Comment extends BaseBlogEntity {
	
	/**
	 * Constructor
	 *  
	 * @param BlogService
	 * @param BlogId
	 */
	
	private final String POSTID = "urn:lsid:ibm.com:blogs:entry-";
	private String postUuid = "";
	
	public Comment(BlogService blogService, String id) {
		super(blogService,id);
	}

	public Comment(BaseService svc, DataHandler<?> handler) {
		super(svc,handler);
	}
	
	/*
	 * This method returns uid of comment
	 */
	public String getCommentUuid() throws BlogServiceException{
		return super.getUid();
	}

	public void setPostUuid(String postUuid) {
		this.postUuid = postUuid;
    }
	
	public String getPostUuid(){
		String postId = "";
		try {
			postId = getAsString(BlogXPath.inReplyToRef);
		} catch (Exception e) {}
	 	if(StringUtil.isEmpty(postId)){
	 		postId = postUuid;
	 	}
		return extractPostUuid(postId);
	}

	public String getContent() throws BlogServiceException{
		return getAsString(BlogXPath.content);
	}
	
	public void setContent(String handle) {
        setAsString(BlogXPath.content, handle);
    }
	
	public String getSelfUrl() throws BlogServiceException{
		return getAsString(BlogXPath.selfUrl);
	}
	
	public String getTrackbacktitle() throws BlogServiceException{
		return getAsString(BlogXPath.trackbacktitle);
	}
	
	public String getInReplyTo() throws BlogServiceException{
		return getAsString(BlogXPath.inReplyToUrl);
	}

	private String extractPostUuid(String uid) {
        if (StringUtil.isNotEmpty(uid) && uid.indexOf(POSTID) == 0) {
            return uid.substring(POSTID.length());
        } else {
            return uid;
        }
    }; 
}
