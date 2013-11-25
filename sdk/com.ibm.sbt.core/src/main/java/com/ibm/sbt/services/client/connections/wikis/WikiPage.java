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

package com.ibm.sbt.services.client.connections.wikis;

import org.w3c.dom.Node;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

/**
 * @author Mario Duarte
 *
 */
public class WikiPage extends AtomEntity {

	/**
	 * @param service
	 * @param node
	 * @param namespaceCtx
	 * @param xpathExpression
	 */
	public WikiPage(BaseService service, Node node,
			NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}
	
	/**
	 * 
	 * @param service
	 * @param dataHandler
	 */
	public WikiPage(BaseService service, XmlDataHandler dataHandler) {
		super(service, dataHandler);
	}
	
	/**
	 * Create empty wikipage with no DataHandler
	 * @param service
	 */
	public WikiPage(BaseService service) {
		super(service, null);
	}
	
	/**
	 * Unique identifier of the wiki page.
	 * @return wikiUuid
	 */
	public String getUuid() {
		return getAsString(WikiXPath.uuid);
	}
	
	/**
	 * Label of the wiki page.
	 * @return wikiUuid
	 */
	public String getLabel() {
		return getAsString(WikiXPath.label);
	}
	/**
	 * Unique version identifier.
	 * @return
	 */
	public String getVersionUuid() {
		return getAsString(WikiXPath.versionUuid);
	}

	/**
	 * Incremental version number, starting at 1.
	 * @return
	 */
	public String getVersionLabel() {
		return getAsString(WikiXPath.versionLabel);
	}
	
	/**
	 * Total number of recommendations of this page.
	 * @return
	 */
	public int getNumberOfRecomendations() {
		return getAsInt(WikiXPath.recommendationsCount);
	}
	
	/**
	 * Total number of comments to this page.
	 * @return
	 */
	public int getNumberOfComments() {
		return getAsInt(WikiXPath.commentsCount);
	}
	
	/**
	 * Total number of views of this page.
	 * @return
	 */
	public int getNumberOfViews() {
		return getAsInt(WikiXPath.viewCount);
	}
	
	/**
	 * Total number of anonymous views of this page.
	 * @return
	 */
	public int getNumberOfAnonymousViews() {
		return getAsInt(WikiXPath.anonymousViewsCount);
	}
	
	/**
	 * Total number of attachments to this page.
	 * @return
	 */
	public int getNumberOfAttachments() {
		return getAsInt(WikiXPath.attachmentsCount);
	}
	
	/**
	 * Total number of versions of this page.
	 * @return
	 */
	public int getNumberOfVersions() {
		return getAsInt(WikiXPath.versionsCount);
	}
}
