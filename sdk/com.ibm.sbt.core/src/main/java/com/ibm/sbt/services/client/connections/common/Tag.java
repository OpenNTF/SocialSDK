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
package com.ibm.sbt.services.client.connections.common;

import org.w3c.dom.Node;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

/**
 * @author mwallace
 *
 */
public class Tag extends BaseEntity {

	/**
	 * Default constructor
	 */
	public Tag() {
	}

	/**
	 * Construct Tag based on the specified node
	 * 
	 * @param service
	 * @param node
	 * @param nameSpaceCtx
	 * @param xpath
	 */
	public Tag(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, new XmlDataHandler(node, namespaceCtx, xpathExpression));
	}
	
	// term="bug" snx:frequency="1"  snx:bin="1" snx:visibility

	/**
	 * The tag.
	 * 
	 * @return
	 */
	public String getTerm() {
		return getAsString(CommonXPath.term);
	}
	
	/**
	 * The number of posts or entries to which the tag has been assigned.
	 * 
	 * @return
	 */
	public long getFrequency() {
		return getAsLong(CommonXPath.frequency);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isVisible() {
		return getAsBoolean(CommonXPath.visibility);
	}
	
}
