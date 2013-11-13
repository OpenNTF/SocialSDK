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
	
	private final String POSTID = "urn:lsid:ibm.com:blogs:entry-";
	private String postUuid = "";
	 /**
     * Constructor
     *
     * @param BlogService
     * @param id
     */
	public Comment(BlogService blogService, String id) {
		super(blogService,id);
	}
	 /**
     * Constructor
     *
     * @param BlogService
     * @param DataHandler
     */
	public Comment(BaseService svc, DataHandler<?> handler) {
		super(svc,handler);
	}
	 /**
	* Return the value of IBM Connections blog post comment id
	* entry document.
	*
	* @method getCommentUuid
	* @return {String} id of the blog post comment
	*/
	public String getCommentUuid() throws BlogServiceException{
		return super.getUid();
	}
	/**
	* Sets blog post id of IBM Connections blog post comment.
	*
	* @method setBlogPostUuid
	* @param {String} blogPostUuid of the comment's blog post
	*/
	public void setPostUuid(String postUuid) {
		this.postUuid = postUuid;
    }
	/**
	* Return the postUuid of the blog post on which comment was created.
	*
	* @method getPostUuid
	* @return {String} Blog post Id of the entity in reply to which comment was created
	*/
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
	/**
	* Return the content of the IBM Connections blog post comment from
	* blog ATOM entry document.
	*
	* @method getContent
	* @return {String} content of the Blog post comment
	*/
	public String getContent() throws BlogServiceException{
		return getAsString(BlogXPath.content);
	}
	 /**
	* Sets content of IBM Connections blog post comment.
	*
	* @method setContent
	* @param {String} content Content of the blog post comment
	*/
	public void setContent(String handle) {
        setAsString(BlogXPath.content, handle);
    }
	/**
	* Return the value of IBM Connections blog post comment URL from blog ATOM
	* entry document.
	*
	* @method getSelfUrl
	* @return {String} Blog URL of the blog post comment
	*/
	public String getSelfUrl() throws BlogServiceException{
		return getAsString(BlogXPath.selfUrl);
	}
	/**
	* Return the Trackback Title of the IBM Connections blog post comment from
	* blog ATOM entry document.
	*
	* @method getTrackbackTitle
	* @return {String} TrackbackTitle of the Blog post comment
	*/
	public String getTrackbacktitle() throws BlogServiceException{
		return getAsString(BlogXPath.trackbacktitle);
	}
	/**
	* Return the get-reply-to of the IBM Connections blog post comment from
	* blog ATOM entry document.
	*
	* @method getInReplyTo
	* @return {String} Entity Id of the entity in reply to which comment was created
	*/
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
