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
 * XPath expressions for parsing common parts of an IBM Connections ATOM feed.
 * 
 * @author mwallace
 *
 */
public enum AtomXPath implements FieldEntry {

	entry("/a:feed/a:entry"),
	singleEntry("/a:entry"),
	id("./id"),
	title("./title"),
	contributor("./contributor"),
	author("./author"),
	modifier("./td:modifier"),
	published("./published"),
	summary("./summary"),
	updated("./updated"),
	categoryTerm("./category/@term"),
	category("/a:category"),
	content("./content"),
	repliesUrl("./a:link[@rel='replies']/@href"),
	editUrl("./a:link[@rel='replies']/@href"),
	selfUrl("./a:link[@rel='edit']/@href"),
	alternateUrl("./a:link[@rel='alternate']/@href"),
	
	personName("./name"),
	personEmail("./email"),
	personUserid("./snx:userid"),
	personUserState("./snx:userState"),
	;
	
	private final XPathExpression path;
	
	/**
	 * Construct an AtomXPath instance
	 * 
	 * @param xpath
	 */
	private AtomXPath(String xpath) {
		XPathExpression xpathExpr = null;
		try {
			xpathExpr = DOMUtil.createXPath(xpath);
		} catch (XMLException e) {
			//e.printStackTrace();
		}
		this.path = xpathExpr;
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.base.datahandlers.FieldEntry#getPath()
	 */
	@Override
	public Object getPath() {
		return this.path;
	}

	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.base.datahandlers.FieldEntry#getName()
	 */
	@Override
	public String getName() {
		return this.name();
	}

}
