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
package com.ibm.sbt.services.client.connections.bookmarks;

import org.w3c.dom.Node;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.connections.bookmarks.model.BookmarkXPath;

/**
 * @author mwallace
 *
 */
public class Bookmark extends AtomEntity {
	
	static final private String ID_PREFIX = "tag:dogear.ibm.com,2005:link:";

	/**
	 * Construct a Bookmark instance.
	 * 
	 * @param service
	 * @param data
	 * @param namespaceCtx
	 * @param xpathExpression
	 */
	public Bookmark(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}
	
	/**
	 * Return the Bookmark universal id.
	 * @return
	 */
	public String getBookmarkUuid() {
		return getAsString(BookmarkXPath.linkid);
	}

	/**
	 * Return the href.
	 * 
	 * @return
	 */
	public String getHref() {
		return getAsString(BookmarkXPath.href);
	}
	
	/**
	 * Return the click count.
	 * 
	 * @return
	 */
	public long getClickCount() {
		return getAsLong(BookmarkXPath.clickCount);
	}
	
	/**
	 * Return the link count.
	 * 
	 * @return
	 */
	public long getLinkCount() {
		return getAsLong(BookmarkXPath.linkCount);
	}
	
}
