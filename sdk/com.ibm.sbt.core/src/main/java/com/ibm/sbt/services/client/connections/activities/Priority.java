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
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

/**
 * @author mwallace
 *
 */
public class Priority extends BaseEntity {

	/*
	 * Identifies the priority of the activity. Options are High, Medium, or Normal. 
	 * Prioritization settings are not global, but are unique to each user; no other members can see these collections. 
	 */
	static final public String HIGH = "High"; //$NON-NLS-1$
	static final public String MEDIUM = "Medium"; //$NON-NLS-1$
	static final public String NORMAL = "Normal"; //$NON-NLS-1$

	/**
	 * Default constructor
	 */
	public Priority() {
	}

	/**
	 * Construct Priority based on the specified node
	 * 
	 * @param service
	 * @param node
	 * @param namespaceCtx
	 * @param xpathExpression
	 */
	public Priority(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, new XmlDataHandler(node, namespaceCtx, xpathExpression));
	}
	
	/**
	 * 
	 * @return label
	 */
	public String getLabel() {
		return getAsString(ActivityXPath.attr_label);
	}
	
	/**
	 * 
	 * @param label
	 */
	public void setLabel(String label) {
		setAsString(ActivityXPath.attr_label, label);
	}
	
	/**
	 * 
	 * @return term
	 */
	public long getTerm() {
		return getAsLong(ActivityXPath.attr_term);
	}
	
	/**
	 * 
	 * @param label
	 */
	public void setTerm(long term) {
		setAsLong(ActivityXPath.attr_term, term);
	}
	
}
