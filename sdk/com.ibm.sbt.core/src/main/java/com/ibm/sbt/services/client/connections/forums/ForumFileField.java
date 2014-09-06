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
package com.ibm.sbt.services.client.connections.forums;

import org.w3c.dom.Node;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.connections.common.Link;

/**
 * @author mwallace
 *
 */
public class ForumFileField extends Field {

	private Link fileLink;
	
	/**
	 * Default constructor
	 */
	public ForumFileField() {
	}

	/**
	 * Construct FileField based on the specified node
	 * 
	 * @param service
	 * @param node
	 * @param namespaceCtx
	 * @param xpathExpression
	 */
	public ForumFileField(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.connections.activities.Field#getType()
	 */
	@Override
	public String getType() {
		return FILE;
	}

	/**
	 * Return the enclosure link.
	 * 
	 * @return link
	 */
	public Link getFileLink() {
		if (fileLink == null && getDataHandler() != null) {
			fileLink = createLink((Node)getDataHandler().getData(), ForumsXPath.file_link);
		}
		return fileLink;
	}
	
	/**
	 * @param enclosureLink the enclosureLink to set
	 */
	public void setEnclosureLink(Link enclosureLink) {
		this.fileLink = enclosureLink;
	}
}
