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

package com.ibm.sbt.services.client.connections.forums;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import org.w3c.dom.Node;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.common.Person;

/**
 * Recommendation Entry Class - represents forum Recommendation Atom entry.
 *
 * @author Swati Singh
 * @author Carlos Manias
 */
public class Recommendation extends AtomEntity{

	public Recommendation(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}
	
	public Recommendation(){}

	@Override
	public Person getAuthor(){
		return new Person(getService(), new XmlDataHandler((Node)this.getDataHandler().getData(), 
    		nameSpaceCtx, (XPathExpression)AtomXPath.author.getPath()));
	}
	


	public String getName() {
		return getAuthor().getName();
	}

	public String getEmail() {
		return getAuthor().getEmail();
	}

	public String getUserId() {
		return getAuthor().getId();
	}

	public String getUserState() {
		return getAuthor().getUserState();
	}
}