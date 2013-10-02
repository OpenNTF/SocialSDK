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
package com.ibm.sbt.services.client.connections.communities.model;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;

/**
 * 
 * @author Carlos Manias
 *
 */
public enum CommunityXPath implements FieldEntry {
	entry("/a:entry"),
	id("./a:id"),
	communityUuid("./snx:communityUuid"), 
	title("./a:title"),
	summary("./a:summary[@type='text']"),
	logoUrl("./a:link[@rel='http://www.ibm.com/xmlns/prod/sn/logo']/@href"),
	membersUrl("./a:link[@rel='http://www.ibm.com/xmlns/prod/sn/member-list']/@href"),
	communityUrl("./a:link[@rel='alternate']/@href"),
	communityAtomUrl("a:link[@rel='self']/@href"),
	tags("./category/@term"), 
	content("./a:content[@type='html']"),
	memberCount("./snx:membercount"),
	communityType("./snx:communityType"), 
	published("./a:published"),
	updated("./a:updated"),
	authorUid("./a:author/snx:userid"),
	authorName("./a:author/a:name"),
	authorEmail("./a:author/a:email"),
	contributorUid("./a:contributor/snx:userid"),
	contributorName("./a:contributor/a:name"),
	contributorEmail("./a:contributor/a:email"),
	communityTheme("./snx:communityTheme"),
	role("./snx:role"),
	inviteCommunityUrl("./a:link[@rel='http://www.ibm.com/xmlns/prod/sn/community']/@href"),
	inviteUrl("./a:link[@rel='edit']/@href");
	
	private final XPathExpression path;
	
	private CommunityXPath(String xpath) {
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
