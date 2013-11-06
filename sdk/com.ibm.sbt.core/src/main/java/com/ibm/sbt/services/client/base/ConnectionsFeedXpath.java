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
package com.ibm.sbt.services.client.base;

import com.ibm.commons.xml.DOMUtil;

import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;

/**
 * 
 * @author Carlos Manias
 *
 */
public enum ConnectionsFeedXpath implements FieldEntry {
	Entry("/a:feed/a:entry"),
	TotalResults("/a:feed/opensearch:totalResults"),
	StartIndex("/a:feed/opensearch:startIndex"), 
	ItemsPerPage("/a:feed/opensearch:itemsPerPage"),
	CurrentPage("/rel=self");

	private final XPathExpression path;
	
	private ConnectionsFeedXpath(String xpath) {
		XPathExpression xpathExpr = null;
		try {
			xpathExpr = DOMUtil.createXPath(xpath);
		} catch (XMLException e) {
			e.printStackTrace();
		}
		this.path = xpathExpr;
	}
	
	@Override
	public XPathExpression getPath() {
		return path;
	}

	@Override
	public String getName() {
		return this.name();
	}

}
