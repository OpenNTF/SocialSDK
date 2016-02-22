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

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.BaseService;
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
     * @param blogService
     * @param id
     */
	public Comment(BlogService blogService, String id) {
		super(blogService,id);
	}
	/**
     * Constructor
     * @param service
     * @param node
     * @param namespaceCtx
     * @param xpathExpression
     */
	public Comment(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}
	 /**
	* Return the value of IBM Connections blog post comment id
	* entry document.
	*
	* @method getCommentUuid
	* @return {String} id of the blog post comment
	* @throws ClientServicesException
	*/
	public String getCommentUuid() {
		return super.getUid();
	}
	/**
	* Sets blog post id of IBM Connections blog post comment.
	*
	* @method setBlogPostUuid
	* @param postUuid of the comment's blog post
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
	* Return the Trackback Title of the IBM Connections blog post comment from
	* blog ATOM entry document.
	*
	* @method getTrackbackTitle
	* @return {String} TrackbackTitle of the Blog post comment
	* @throws ClientServicesException
	*/
	public String getTrackbacktitle() throws ClientServicesException{
		return getAsString(BlogXPath.trackbacktitle);
	}
	/**
	* Return the get-reply-to of the IBM Connections blog post comment from
	* blog ATOM entry document.
	*
	* @method getInReplyTo
	* @return {String} Entity Id of the entity in reply to which comment was created
	* @throws ClientServicesException
	*/
	public String getInReplyTo() throws ClientServicesException{
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
