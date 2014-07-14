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
public class Category extends BaseEntity {

	static final public String SCHEME_TYPE = "http://www.ibm.com/xmlns/prod/sn/type";
	static final public String SCHEME_FLAGS = "http://www.ibm.com/xmlns/prod/sn/flags";

	/*
	 * Defines the built-in categoRies. The following values are supported:
	 */
	static final public String TYPE_SECTION = "section";
	static final public String TYPE_ENTRY = "entry"; 
	static final public String TYPE_REPLY = "reply";
	static final public String TYPE_TODO = "todo"; 
	static final public String TYPE_CHAT = "chat"; 
	static final public String TYPE_EMAIL = "email";
	
	static final public String FLAG_DELETED = "deleted";
	static final public String FLAG_PRIVATE = "private";
	static final public String FLAG_COMPLETED = "completed";

	/**
	 * Default constructor
	 */
	public Category() {
	}

	/**
	 * Construct Field based on the specified node
	 * 
	 * @param service
	 * @param node
	 * @param namespaceCtx
	 * @param xpathExpression
	 */
	public Category(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, new XmlDataHandler(node, namespaceCtx, xpathExpression));
	}
	
	/**
	 * Return if this category represents a flag
	 * 
	 * @return true if this category is a flag
	 */
	public boolean isFlag() {
		return SCHEME_FLAGS.equalsIgnoreCase(getScheme());
	}
	
	/**
	 * Return if this category represents a type
	 * 
	 * @return true if this category is a type
	 */
	public boolean isType() {
		return SCHEME_TYPE.equalsIgnoreCase(getScheme());
	}
	
	/**
	 * 
	 * @return scheme
	 */
	public String getScheme() {
		return getAsString(ActivityXPath.attr_scheme);
	}
	
	/**
	 * 
	 * @return term
	 */
	public String getTerm() {
		return getAsString(ActivityXPath.attr_term);
	}
	

	/**
	 * 
	 * @return label
	 */
	public String getLabel() {
		return getAsString(ActivityXPath.attr_label);
	}
	
}
