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
	/**
     * Constructor
     *
     * @param BlogService
     * @param DataHandler
     */
	public BlogPost(BaseService svc, DataHandler<?> handler) {
		super(svc,handler);
	}
	/**
	* Return the value of IBM Connections blogPost Uuid from blog post ATOM
	* entry document.
	*
	* @method getPostUuid
	* @return postUuid of the blog post
	*/
	public String getPostUuid() throws BlogServiceException{
		return super.getUid();
	}
	/**
	* Return the value of selfUrl of IBM Connections blogPost Uuid from blog post ATOM
	* entry document.
	*
	* @method getSelfUrl
	* @return selfUrl of the blog post
	*/
	public String getSelfUrl() throws BlogServiceException{
		return getAsString(BlogXPath.selfUrl);
	}
	/**
	* Return the content of the IBM Connections blog post from
	* blog ATOM entry document.
	*
	* @method getContent
	* @return {String} content of the Blog
	*/
	public String getContent() throws BlogServiceException{
		return getAsString(BlogXPath.content);
	}
	/**
	* Sets content of IBM Connections blog post.
	*
	* @method setContent
	* @param {String} content Content of the blog post
	*/
	public void setContent(String handle) {
        setAsString(BlogXPath.content, handle);
    }
    /**
	* Return the replies url of the IBM Connections blog post from
	* blog ATOM entry document.
	*
	* @method getRepliesUrl
	* @return {String} replies url of the Blog post
	*/
	public String getRepliesUrl() throws BlogServiceException{
		return getAsString(BlogXPath.repliesUrl);
	}
	/**
	* Return the comment count of the IBM Connections blog post from
	* blog ATOM entry document.
	*
	* @method getCommentCount
	* @return {String} comment count of the Blog post
	*/
	public String getCommentCount() throws BlogServiceException{
		return getAsString(BlogXPath.commentCount);
	}
	/**
	* Return the hit count of the IBM Connections blog post from
	* blog ATOM entry document.
	*
	* @method getHitCount
	* @return {String} hit count of the Blog post
	*/
	public String getHitCount() throws BlogServiceException{
		return getAsString(BlogXPath.hitCount);
	}
	
	 /**
     * Return the bloghandle of the blog post.
     * 
     * @method getBlogHandle
     * @return {String} Blog handle of the blog post
     */
	public String getBlogHandle() throws BlogServiceException{
		if(StringUtil.isNotEmpty(getAsString(BlogXPath.alternateUrl)))
			return extractBlogHandle(getAsString(BlogXPath.alternateUrl));
		else{
			return getAsString(BlogXPath.handle);
		}
	}
	/**
     * Sets blog handle of IBM Connections blog post.
     * 
     * @method setBlogHandle
     * @param {String} blogHandle of the blog post's blog
     */
	public void setBlogHandle(String handle)throws BlogServiceException{
		setAsString(BlogXPath.handle, handle);
	}
	/**
	* Save this blog post
	*
	* @method save
	* @param blogHandle
	*/
	public BlogPost save() throws BlogServiceException{
		if(StringUtil.isEmpty(getPostUuid())){
			return getService().createBlogPost(this, getAsString(BlogXPath.handle));
		}
		else{
			return getService().updateBlogPost(this, getBlogHandle());
		}
		
	}
	/**
	* Loads the blog post object with the atom entry associated with the
	* blog post. By default, a network call is made to load the atom entry
	* document in the blog post object.
	*
	* @method load
	* @param blogHandle
	* @param postUuid
	*/
	public BlogPost load()throws BlogServiceException{
		return getService().getBlogPost(getBlogHandle(), getPostUuid());
	}
	
	/**
     * This method removes the blogPost on the server
     *
     * @throws BlogServiceException
     */
    public void remove() throws BlogServiceException{
    		getService().removeBlogPost(getPostUuid(), getBlogHandle());
	}
	
	/*
     * Extract Blog handle from BlogPost alternate url
     */
    private String extractBlogHandle(String url) {
    	String urlSuffix = "/entry/";
    	url = url.substring(0,url.indexOf(urlSuffix));
    	String bloghandle = url.substring(url.lastIndexOf("/")+1,url.length());
    	return bloghandle;
    	
    }
	

}
