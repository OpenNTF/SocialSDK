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
package com.ibm.sbt.services.client.connections.profiles.model;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;

public enum ColleagueConnectionXPath implements FieldEntry {
	authorName("./snx:connection/a:contributor[@snx:rel=\"http://www.ibm.com/xmlns/prod/sn/connection/source\"]/a:name"),
	authorUserId("./snx:connection/a:contributor[@snx:rel=\"http://www.ibm.com/xmlns/prod/sn/connection/source\"]/snx:userid"),
	authorEmail("./snx:connection/a:contributor[@snx:rel=\"http://www.ibm.com/xmlns/prod/sn/connection/source\"]/a:email"),
	contributorName("./snx:connection/a:contributor[@snx:rel=\"http://www.ibm.com/xmlns/prod/sn/connection/target\"]/a:name"),
	contributorUserId("./snx:connection/a:contributor[@snx:rel=\"http://www.ibm.com/xmlns/prod/sn/connection/target\"]/snx:userid"),
	contributorEmail("./snx:connection/a:contributor[@snx:rel=\"http://www.ibm.com/xmlns/prod/sn/connection/target\"]/a:email"),
	selfLinkFromEntry("./a:link[@rel=\"self\"]/@href"),
	editLinkFromEntry("./a:link[@rel=\"edit\"]/@href");
	
	private final XPathExpression path;
	
	private ColleagueConnectionXPath(String xpath) {
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
