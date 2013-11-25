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

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;

/**
 * @author Mario Duarte
 *
 */
public enum WikiXPath implements FieldEntry {
	uuid("./td:uuid"),
	label("./td:label"),
	communityUuid("./snx:communityUuid"),
	permissions("./td:permissions"),
	sharedWith("./sharedWith"),
	
	// WikiPageXpaths
	versionUuid("./td:versionUuid"),
	versionLabel("./td:versionLabel"),
	recommendationsCount("./snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/recommendations']"),
	commentsCount("./snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/comment']"),
	viewCount("./snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/hit']"),
	anonymousViewsCount("./snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/anonymous_hit']"),
	attachmentsCount("./snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/attachments']"),
	versionsCount("./snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/versions']"),
	;
	
	private final XPathExpression path;
	
	private WikiXPath(String xpath) {
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