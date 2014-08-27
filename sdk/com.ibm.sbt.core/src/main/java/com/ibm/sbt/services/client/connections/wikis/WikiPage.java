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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Node;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

/**
 * WikiPage class represents an entry for a Wiki page returned by the
 * Connections REST API.
 * 
 * @author Mario Duarte
 *
 */
public class WikiPage extends WikiBaseEntity {

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
		super(service, (XmlDataHandler)null);
	}
	
	public WikiPage(){}
	
	@Override
	public String getContent() {
		if (fields.containsKey(AtomXPath.content.getName())){
			Object value = fields.get(AtomXPath.content.getName());
			return (value == null) ? null : value.toString();
		} 

		String content = "";
		try {
			content = getHtmlMarkup();
		} catch (ClientServicesException e) {
			e.printStackTrace();
		}

		return content;
	}
	
	/**
	 * Make a request to get the HTML markup of the wiki page.
	 * @return
	 * @throws ClientServicesException
	 */
	public String getHtmlMarkup() throws ClientServicesException{
		String contentUrl = getAsString(WikiXPath.enclosureUrl);
		StringWriter writer = new StringWriter();
		InputStream contentStream = null;
		String html = null;
		try {
			Object data = getService().getEndpoint().xhrGet(contentUrl).getData();
			if (data instanceof String) {
				html = (String)data;
			} else if (data instanceof InputStream) {
				contentStream = (InputStream)data;
				IOUtils.copy(contentStream, writer, "UTF-8");
				html = writer.toString();
			}
		} catch (IOException e) {
			throw new ClientServicesException(e);
		} finally{
			try {
				if (contentStream != null) {
					contentStream.close();
				}
			} catch (IOException e) {
				throw new ClientServicesException(e);
			}
		}
		return html;
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
