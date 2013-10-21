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

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.blogs.model.BaseBlogEntity;
import com.ibm.sbt.services.client.connections.blogs.model.BlogXPath;


/**
 * BlogPost model object
 * 
 * @author Swati Singh
 */


public class BlogPost extends BaseBlogEntity {
	
	/**
	 * Constructor
	 *  
	 * @param BlogService
	 * @param BlogId
	 */
	public BlogPost(BlogService blogService, String id) {
		super(blogService,id);
	}

	public BlogPost(BaseService svc, DataHandler<?> handler) {
		super(svc,handler);
	}
	
	/*
	 * This method returns uid of post
	 */
	public String getPostUuid() throws BlogServiceException{
		return super.getUid();
	}
	
	public String getSelfUrl() throws BlogServiceException{
		return getAsString(BlogXPath.selfUrl);
	}
	

	public String getContent() throws BlogServiceException{
		return getAsString(BlogXPath.content);
	}
	
	public void setContent(String handle) {
        setAsString(BlogXPath.content, handle);
    }
	
	public String getRepliesUrl() throws BlogServiceException{
		return getAsString(BlogXPath.repliesUrl);
	}
	
	
	public String getCommentCount() throws BlogServiceException{
		return getAsString(BlogXPath.commentCount);
	}
	
	public String getHitCount() throws BlogServiceException{
		return getAsString(BlogXPath.hitCount);
	}
	
	public BlogPost save(String blogHandle) throws BlogServiceException{
		return getService().createBlogPost(this, blogHandle);
	}
	
	public BlogPost load(String blogHandle, String postId)throws BlogServiceException{
		return getService().getBlogPost(blogHandle, postId);
	}
	
	

}
