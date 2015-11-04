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
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

/**
 * The category document identifies the tags that have been assigned to particular items,
 * such as blog posts or community entries.
 * Tags are single-word keywords that categorize a posting or entry.
 * A tag classifies the information in the posting or entry to make it easier to find the content later.
 * The format of the tags document is an Atom publishing protocol (APP) categories document. 
 * 
 * @see
 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.5+API+Documentation#action=openDocument&res_title=Tags_category_document_ic45&content=pdcontent">
 *			Tags category content</a>
 * 
 * @author mwallace
 *
 */
public class Tag extends AtomEntity {

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
	 * @param namespaceCtx
	 * @param xpathExpression
	 */
	public Tag(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, new XmlDataHandler(node, namespaceCtx, xpathExpression));
	}
	
	// term="bug" snx:frequency="1"  snx:bin="1" snx:visibility

	/**
	 * The tag.
	 * 
	 * @return {String}
	 */
	public String getTerm() {
		return getAsString(CommonXPath.term);
	}
	
	/**
	 * The number of posts or entries to which the tag has been assigned.
	 * 
	 * @return {long}
	 */
	public long getFrequency() {
		return getAsLong(CommonXPath.frequency);
	}
	
	/**
	 * 
	 * @return {boolean}
	 */
	public boolean isVisible() {
		return getAsBoolean(CommonXPath.visibility);
	}
	
}
