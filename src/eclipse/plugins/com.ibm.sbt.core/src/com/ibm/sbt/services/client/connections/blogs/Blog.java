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
 * Blog object
 * 
 * @author Swati Singh
 */


public class Blog extends BaseBlogEntity {
	
	/**
	 * Constructor
	 *  
	 * @param BlogService
	 * @param BlogId
	 */
	public Blog(BlogService blogService, String id) {
		super(blogService,id);
	}

	public Blog(BaseService svc, DataHandler<?> handler) {
		super(svc,handler);
	}
	

	public void setBlogUuid(String forumUuid) {
        setAsString(BlogXPath.uid, forumUuid);
    }
	
	
	/*
	 * This method returns uid of blog
	 */
	public String getBlogUuid() throws BlogServiceException{
		return super.getUid();
	}
	
	public String getHandle() throws BlogServiceException{
		return getAsString(BlogXPath.handle);
	}
	
	public void setHandle(String handle) {
        setAsString(BlogXPath.handle, handle);
    }
	
	public String getTimeZone() throws BlogServiceException{
		return getAsString(BlogXPath.timeZone);
	}
	
	public void setTimeZone(String handle) {
        setAsString(BlogXPath.timeZone, handle);
    }
	

	public String getSummary() throws BlogServiceException{
		return getAsString(BlogXPath.summary);
	}
	
	public void setSummary(String summary) {
        setAsString(BlogXPath.summary, summary);
    }
	/**
	 * This method creates the blog on the server
	 * 
	 * @return
	 * @throws BlogServiceException
	 */
	public Blog save() throws BlogServiceException{
		return getService().createBlog(this);
	}
	
    public void remove() throws BlogServiceException{
		getService().removeBlog(getUid());
	}
	

}
