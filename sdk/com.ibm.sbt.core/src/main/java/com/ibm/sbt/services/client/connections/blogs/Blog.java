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

package com.ibm.sbt.services.client.connections.blogs;

import org.w3c.dom.Node;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.BaseService;
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
	
	public Blog(){}
	
	/**
     * Constructor
     * @param BaseService
     * @param Node
     * @param NamespaceContext
     * @param XPathExpression
     */
	public Blog(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
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
	public String getBlogUuid() {
		return getUid();
	}
	
	/**
	* Returns the Blog handle
	*
	* @method getHandle
	* @return Blog handle
	*/
	public String getHandle() {
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
	public String getTimeZone() {
		return getAsString(BlogXPath.timeZone);
	}
	
	public String getReplyTo() {
		return getAsString(BlogXPath.inReplyToRef);
	}
	
	public String getFlagTerm() {
		return getAsString(BlogXPath.flagTerm);
	}
	
	public void setFlagTerm(String flagTerm) {
		setAsString(BlogXPath.flagTerm, flagTerm);
	}
	
	public String getFlagLabel() {
		return getAsString(BlogXPath.flagLabel);
	}
	
	public void setFlagLabel(String flagLabel) {
		setAsString(BlogXPath.flagLabel, flagLabel);
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
	 * This method creates the blog on the server
	 * 
	 * @return Blog
	 * @throws ClientServicesException
	 */
	public Blog save() throws ClientServicesException {
		return getService().createBlog(this);
	}
	
	/**
     * This method removes the blog on the server
     *
     * @throws BlogServiceException
     */
    public void remove() throws ClientServicesException {
		getService().removeBlog(getUid());
	}
}
