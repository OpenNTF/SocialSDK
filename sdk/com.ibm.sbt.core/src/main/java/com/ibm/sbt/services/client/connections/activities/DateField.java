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

import java.util.Date;

import org.w3c.dom.Node;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.BaseService;

/**
 * @author mwallace
 *
 */
public class DateField extends Field {

	/**
	 * Default constructor
	 */
	public DateField() {
	}

	/**
	 * Construct DateField based on the specified node
	 * 
	 * @param service
	 * @param node
	 * @param namespaceCtx
	 * @param xpathExpression
	 */
	public DateField(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.connections.activities.Field#getType()
	 */
	@Override
	public String getType() {
		return DATE;
	}
	
	/**
	 * Return the date value.
	 * 
	 * @return date
	 */
	public Date getDate() {
		return getAsDate(ActivityXPath.field_date);
	}
	
	/**
	 * Set the date value.
	 * 
	 * @param date
	 */
	public void setDate(Date date) {
		setAsDate(ActivityXPath.field_date, date);
	}
	
}
