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

import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.blogs.BlogService;
import com.ibm.sbt.services.client.connections.common.Person;

/**
 * Base model object to be used with Blogs, Posts and Comments
 * 
 * @author Swati Singh 
 */
public class BaseBlogEntity extends AtomEntity {
	
	private final String BLOGID = "urn:lsid:ibm.com:blogs:blog-";
	private final String POSTID = "urn:lsid:ibm.com:blogs:entry-";
	private final String COMMENTID = "urn:lsid:ibm.com:blogs:comment-";
	
	 /**
     * Constructor
     *
     * @param blogService
     * @param id
     */
	public BaseBlogEntity(BlogService blogService, String id) {
		super(blogService,id);
		setService(blogService);
		setAsString(BlogXPath.uid, id);
	}
	
	public BaseBlogEntity(){}
	/**
     * Constructor
     * @param service
     * @param node
     * @param namespaceCtx
     * @param xpathExpression
     */
	public BaseBlogEntity(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}
	/**
	* Returns the Uuid of the blog, post or comment
	*
	* @return {String} uuid
	*/
	public String getUid(){
		String id = getAsString(BlogXPath.uid);
		if(StringUtil.isNotEmpty(id)){
			if(StringUtil.startsWithIgnoreCase(id, BLOGID)){
				id = id.substring(BLOGID.length());
			}
			else if(StringUtil.startsWithIgnoreCase(id, POSTID)){
				id = id.substring(POSTID.length());
			}
			else{
				id = id.substring(COMMENTID.length());
			}
		}
		return id;
	}
   
	/**
	* Gets an author of IBM Connections Blog.
	*
	* @return {Author}
	*/
	@Override
	public Person getAuthor(){
		return new Person(getService(),new XmlDataHandler((Node)this.getDataHandler().getData(), 
	    		nameSpaceCtx, (XPathExpression)AtomXPath.author.getPath()));
	}

	/**
	* Gets a contributor of IBM Connections Blog.
	*
	* @return {Author}
	*/
	@Override
	public Person getContributor(){
		return new Person(getService(),new XmlDataHandler((Node)this.getDataHandler().getData(), 
	    		nameSpaceCtx, (XPathExpression)AtomXPath.contributor.getPath()));
	}
	
	/**
	* Return the Recommendations count
	*
	* @return {String} recommendations Count
	*/
	public String getRecommendationsCount() {
		return getAsString(BlogXPath.recommendationsCount);
	}
	
	/**Returns the list of Tags
    *
    * @return {List<String>} the list of Tags
    */
	public List<String> getTags() {
		return super.getBaseTags();
	}
	/**sets the tags
    *
    * @param tags List of Tags
    */
	public void setTags(List<String> tags) {
		super.setBaseTags(tags);
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
	private String extractBlogUuid(String uid) {
        if (StringUtil.isNotEmpty(uid) && uid.indexOf(BLOGID) == 0) {
            return uid.substring(BLOGID.length());
        } else {
            return uid;
        }
    }; 

}
