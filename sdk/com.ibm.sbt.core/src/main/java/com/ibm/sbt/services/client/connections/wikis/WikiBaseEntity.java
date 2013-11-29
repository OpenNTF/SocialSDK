/*
 * � Copyright IBM Corp. 2013
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

import java.util.Date;

import org.w3c.dom.Node;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.common.Person;

/**
 * Wiki class represents an entry for a Wiki or wiki page returned by the
 * Connections REST API.
 * 
 * @author Mario Duarte
 *
 */
public abstract class WikiBaseEntity extends AtomEntity {

	public WikiBaseEntity(BaseService service, Node node,
			NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}

	public WikiBaseEntity(BaseService service, XmlDataHandler dataHandler) {
		super(service, dataHandler);
	}
	
	public WikiBaseEntity(BaseService service) {
		super(service);
	}

	/**
	 * Unique identifier of a wiki or wiki page.
	 * @return wikiUuid
	 */
	public String getUuid() {
		return getAsString(WikiXPath.uuid);
	}
	
	/**
	 * Label of a wiki or wiki page.
	 * @return wikiUuid
	 */
	public String getLabel() {
		return getAsString(WikiXPath.label);
	}
	
	/**
	 * Sets the label of wiki or wiki page.
	 * @param label
	 */
	public void setLabel(String label) {
		setAsString(WikiXPath.label, label);
	}
	
	/**
	 * 
	 * @return
	 */
	public Person getModifier() {
		return new Person(getService(), new XmlDataHandler((Node)this.getDataHandler().getData(), 
    			ConnectionsConstants.nameSpaceCtx, (XPathExpression)AtomXPath.modifier.getPath()));
	}
	
	/**
	 * Return the date the wiki was created.
	 * @return
	 */
	public Date getCreated() {
		return this.getAsDate(WikiXPath.created);
	}
	
	/**
	 * Date of the last modification
	 * @return
	 */
	public Date getModified() {
		return this.getAsDate(WikiXPath.modified);
	}
}
