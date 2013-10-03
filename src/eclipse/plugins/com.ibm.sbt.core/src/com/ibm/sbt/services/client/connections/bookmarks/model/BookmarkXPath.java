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
package com.ibm.sbt.services.client.connections.bookmarks.model;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;

/**
 * @author mwallace
 *
 */
public enum BookmarkXPath implements FieldEntry {
	
	href("a:link[not(@ref)]/@href"),
	linkid("snx:link/@linkid"),
	clickCount("snx:clickcount"),
	linkCount("snx:linkcount");

	private final XPathExpression xpath;
	
	private BookmarkXPath(String path) {
		XPathExpression xpath = null;
		try {
			xpath = DOMUtil.createXPath(path);
		} catch (XMLException e) {
			e.printStackTrace();
		}
		this.xpath = xpath;
	}

	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.base.datahandlers.FieldEntry#getPath()
	 */
	@Override
	public Object getPath() {
		return xpath;
	}

	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.client.base.datahandlers.FieldEntry#getName()
	 */
	@Override
	public String getName() {
		return this.name();
	}

}
