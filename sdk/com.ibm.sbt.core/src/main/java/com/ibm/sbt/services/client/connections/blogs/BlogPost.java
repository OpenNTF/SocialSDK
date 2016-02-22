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

import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
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
	 * @param blogService
	 * @param id
	 */
	public BlogPost(BlogService blogService, String id) {
		super(blogService, id);
	}

	/**
	 * Constructor
	 * 
	 * @param service
	 * @param node
	 * @param namespaceCtx
	 * @param xpathExpression
	 */
	public BlogPost(BaseService service, Node node,
			NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}

	/**
	 * Return the value of IBM Connections blogPost entry anchor from blog post ATOM
	 * entry document.
	 * 
	 * @method getEntryAnchor
	 * @return entry anchor of the blog post
	 */
	public String getEntryAnchor() {
		Element entry = (Element)getDataHandler().getData();
		if (entry != null) {
			String base = DOMUtil.getAttributeValue(entry, "xml:base");
			if (!StringUtil.isEmpty(base)) {
				return base.substring(base.lastIndexOf('/')+1);
			}
		}
		return null;
	}

	/**
	 * Return the value of IBM Connections blogPost Uuid from blog post ATOM
	 * entry document.
	 * 
	 * @method getPostUuid
	 * @return postUuid of the blog post
	 */
	public String getPostUuid() {
		return super.getUid();
	}

	/**
	 * Return the replies url of the IBM Connections blog post from blog ATOM
	 * entry document.
	 * 
	 * @method getRepliesUrl
	 * @return {String} replies url of the Blog post
	 */
	public String getRepliesUrl() {
		return getAsString(BlogXPath.repliesUrl);
	}

	/**
	 * Return the comment count of the IBM Connections blog post from blog ATOM
	 * entry document.
	 * 
	 * @method getCommentCount
	 * @return {String} comment count of the Blog post
	 */
	public int getCommentCount() {
		return getAsInt(BlogXPath.commentCount);
	}

	/**
	 * Return the hit count of the IBM Connections blog post from blog ATOM
	 * entry document.
	 * 
	 * @method getHitCount
	 * @return {String} hit count of the Blog post
	 */
	public int getHitCount() {
		return getAsInt(BlogXPath.hitCount);
	}

	/**
	 * Return the bloghandle of the blog post.
	 * 
	 * @method getBlogHandle
	 * @return {String} Blog handle of the blog post
	 */
	public String getBlogHandle() {
		if (StringUtil.isNotEmpty(getAsString(BlogXPath.alternateUrl)))
			return extractBlogHandle(getAsString(BlogXPath.alternateUrl));
		else {
			return getAsString(BlogXPath.handle);
		}
	}

	/**
	 * Sets blog handle of IBM Connections blog post.
	 * 
	 * @method setBlogHandle
	 * @param handle of the blog post's blog
	 */
	public void setBlogHandle(String handle) {
		setAsString(BlogXPath.handle, handle);
	}

	/**
	 * Save this blog post
	 * 
	 * @method save
	 * @throws ClientServicesException
	 */
	public BlogPost save() throws ClientServicesException {
		if (StringUtil.isEmpty(getPostUuid())) {
			return getService().createBlogPost(this,
					getAsString(BlogXPath.handle));
		} else {
			return getService().updateBlogPost(this, getBlogHandle());
		}
	}

	/**
	 * Loads the blog post object with the atom entry associated with the blog
	 * post. By default, a network call is made to load the atom entry document
	 * in the blog post object.
	 * 
	 * @method load
	 * @throws ClientServicesException
	 */
	public BlogPost load() throws ClientServicesException {
		return getService().getBlogPost(getBlogHandle(), getPostUuid());
	}

	/**
	 * This method removes the blogPost on the server
	 * 
	 * @throws BlogServiceException
	 */
	public void delete() throws ClientServicesException {
		getService().deleteBlogPost(getPostUuid(), getBlogHandle());
	}
	
	/**
	 * This method returns a list of the comments associated with this blog post.
	 * 
	 * @return EntityList<Comment>
	 * @throws ClientServicesException
	 */
	public EntityList<Comment> getComments() throws ClientServicesException {
		return getComments(null);
	}
	
	/**
	 * This method returns a list of the comments associated with this blog post.
	 * 
	 * @param parameters
	 * @return EntityList<Comment>
	 * @throws ClientServicesException
	 */
	public EntityList<Comment> getComments(Map<String, String> parameters) throws ClientServicesException {
		String blogHandle = getBlogHandle();
		String entryAnchor = getEntryAnchor();
		return getService().getEntryComments(blogHandle, entryAnchor, parameters);
	}
	
	public String getType() {
		return getAsString(BlogXPath.blogType);
	}
	
	public void setType(String type) {
		setAsString(BlogXPath.blogType, type);
	}

	/*
	 * Extract Blog handle from BlogPost alternate url
	 */
	private String extractBlogHandle(String url) {
		String urlSuffix = "/entry/";
		url = url.substring(0, url.indexOf(urlSuffix));
		String bloghandle = url.substring(url.lastIndexOf("/") + 1,
				url.length());
		return bloghandle;

	}
}
