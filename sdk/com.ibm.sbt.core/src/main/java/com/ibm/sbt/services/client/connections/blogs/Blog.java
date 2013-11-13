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
	/**
     * Constructor
     *
     * @param BlogService
     * @param DataHandler
     */
	public Blog(BaseService svc, DataHandler<?> handler) {
		super(svc,handler);
	}
   /**
	* Sets id of IBM Connections blog.
	*
	* @method setBlogUuid
	* @param blogUuid of the blog
	*/
	public void setBlogUuid(String blogUuid) {
        setAsString(BlogXPath.uid, blogUuid);
    }
    /**
	* Return the value of IBM Connections blog UuiD from blog ATOM
	* entry document.
	*
	* @method getBlogUuid
	* @return blogUuid of the blog
	*/
	public String getBlogUuid() throws BlogServiceException{
		return super.getUid();
	}
	/**
	* Returns the Blog handle
	*
	* @method getHandle
	* @return Blog handle
	*/
	public String getHandle() throws BlogServiceException{
		return getAsString(BlogXPath.handle);
	}
	/**
	* Sets the Blog hanlde
	*
	* @method setHandle
	* @param handle
	*/
	public void setHandle(String handle) {
        setAsString(BlogXPath.handle, handle);
    }
	/**
	* Returns the Blog timeZone
	*
	* @method getTimeZone
	* @return {String} Blog TimeZone
	*/
	public String getTimeZone() throws BlogServiceException{
		return getAsString(BlogXPath.timeZone);
	}
	/**
	* Sets the Blog timezone
	*
	* @method setTimeZone
	* @param timeZone
	*/
	public void setTimeZone(String handle) {
        setAsString(BlogXPath.timeZone, handle);
    }
	/**
	* Returns the summary of the Blog
	*
	* @method getSummary
	* @return {String} Blog summary
	*/
	public String getSummary() throws BlogServiceException{
		return getAsString(BlogXPath.summary);
	}
	/**
	* Sets the summary of the Blog
	*
	* @method setSummary
	* @param summary
	*/
	public void setSummary(String summary) {
        setAsString(BlogXPath.summary, summary);
    }
	/**
	 * This method creates the blog on the server
	 * 
	 * @return Blog
	 * @throws BlogServiceException
	 */
	public Blog save() throws BlogServiceException{
		return getService().createBlog(this);
	}
	/**
     * This method removes the blog on the server
     *
     * @throws BlogServiceException
     */
    public void remove() throws BlogServiceException{
		getService().removeBlog(getUid());
	}
	

}
