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
package com.ibm.sbt.services.client.connections.activities;

import org.w3c.dom.Node;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.connections.common.Link;

/**
 * @author mwallace
 *
 */
public class FileField extends Field {

	private Link editMediaLink;
	private Link enclosureLink;
	
	/**
	 * Default constructor
	 */
	public FileField() {
	}

	/**
	 * Construct FileField based on the specified node
	 * 
	 * @param service
	 * @param node
	 * @param namespaceCtx
	 * @param xpathExpression
	 */
	public FileField(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
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
	 * Return the edit-media link.
	 * 
	 * @return link
	 */
	public Link getEditMediaLink() {
		if (editMediaLink == null && getDataHandler() != null) {
			editMediaLink = createLink((Node)getDataHandler().getData(), ActivityXPath.field_link_editmedia);
		}
		return editMediaLink;
	}

	/**
	 * Return the enclosure link.
	 * 
	 * @return link
	 */
	public Link getEnclosureLink() {
		if (enclosureLink == null && getDataHandler() != null) {
			enclosureLink = createLink((Node)getDataHandler().getData(), ActivityXPath.field_link_enclosure);
		}
		return enclosureLink;
	}
	
	/**
	 * @param editMediaLink the editMediaLink to set
	 */
	public void setEditMediaLink(Link editMediaLink) {
		this.editMediaLink = editMediaLink;
	}
	
	/**
	 * @param enclosureLink the enclosureLink to set
	 */
	public void setEnclosureLink(Link enclosureLink) {
		this.enclosureLink = enclosureLink;
	}
}
