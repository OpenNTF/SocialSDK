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

import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import org.w3c.dom.Node;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.blogs.model.BlogXPath;
import com.ibm.sbt.services.client.connections.common.Person;

/**
 * Blog class represents an entry for a Blog returned by the
 * Connections REST API.
 * 
 * @author Benjamin Jakobus
 */
public abstract class BlogBaseEntity extends AtomEntity {

	public BlogBaseEntity(BaseService service, Node node,
			NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}

	public BlogBaseEntity(BaseService service, XmlDataHandler dataHandler) {
		super(service, dataHandler);
	}
	
	public BlogBaseEntity(BaseService service) {
		super(service);
	}
	
	public BlogBaseEntity() {}
	
	/**
	 * Unique identifier of a blog.
	 * @return uid
	 */
	public String getUid() {
		return getAsString(BlogXPath.uid);
	}
	
	/**
	 * Blog handle.
	 * @return handle
	 */
	public String getHandle() {
		return getAsString(BlogXPath.handle);
	}
	
	/**
	 * Sets the handle of blog.
	 * @param handle
	 */
	public void setHandle(String handle) {
		setAsString(BlogXPath.handle, handle);
	}
	

	
	/**
	 * 
	 * @return {Person} person who modified the blog entry
	 */
	public Person getModifier() {
		return new Person(getService(), new XmlDataHandler((Node)this.getDataHandler().getData(), 
    			nameSpaceCtx, (XPathExpression)AtomXPath.modifier.getPath()));
	}
}
