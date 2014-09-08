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
public class TextField extends Field {

	private Link link;
	
	/**
	 * Default constructor
	 */
	public TextField() {
	}

	/**
	 * Construct TextField based on the specified node
	 * 
	 * @param service
	 * @param node
	 * @param namespaceCtx
	 * @param xpathExpression
	 */
	public TextField(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.connections.activities.Field#getType()
	 */
	@Override
	public String getType() {
		return TEXT;
	}
	
	/**
	 * Return the summary value for this field.
	 * 
	 * @return summary
	 */
	public String getSummary() {
		return getAsString(ActivityXPath.field_summary);
	}
	
	/**
	 * Set the summary value for this field.
	 * 
	 * @param summary
	 */
	public void setSummary(String summary) {
		setAsString(ActivityXPath.field_summary, summary);
	}
	
	
	/**
	 * Return the link.
	 * 
	 * @return link
	 */
	public Link getLink() {
		if (link == null && getDataHandler() != null) {
			link = createLink((Node)getDataHandler().getData(), ActivityXPath.field_link);
		}
		return link;
	}
}
