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
package com.ibm.sbt.services.client.connections.blogs.model;

/**
 * Class used in resolving XPaths from Blogs service
 * @author Swati Singh
 */


import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;

public enum BlogXPath implements FieldEntry{
	
	entry("/a:feed/a:entry"),
	singleEntry("/a:entry"),
	uid("./id"),
	title("./title"),
	handle("./snx:handle"),
	timeZone("./snx:timezone"),
	authorName("./author/name"),
	authorEmail("./author/email"),
	authorUserid("./author/snx:userid"),
	authorUserState("./author/snx:userState"),
	contributorName("./contributor/name"),
	contributorEmail("./contributor/email"),
	contributorUserid("./contributor/snx:userid"),
	contributorUserState("./contributor/snx:userState"),
	published("./published"),
	summary("./summary"),
	updated("./updated"),
	snxRank("./snx:rank"),
	tags("./category/@term"),
	tagEntry("/app:categories/a:category"),
	targetEmail("app:categories/snx:targetEmail"),
	numberOfContributors("/app:categories/@snx:numberOfContributors"),
	term("./@term"),
	frequency("./@snx:frequency"),
	intensity("./@snx:intensityBin"), 
	visibility("./@snx:visibilityBin"),
	content("./content"),
	recommendationsCount("./snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/recommendations']"),
	commentCount("./snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/comment']"),
	hitCount("./snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/hit']"),
	inReplyToUrl("./thr:in-reply-to/@href"),
	inReplyToRef("./thr:in-reply-to/@ref"),
	
	trackbacktitle("./snx:trackbacktitle"),
	flagTerm("./category/@term"),
	flagLabel("./category/@label"),
	repliesUrl("./a:link[@rel='replies']/@href"),
	selfUrl("./a:link[@rel='self']/@href"),
	alternateUrl("./a:link[@rel='alternate']/@href"),
	recommendationsUrl("./a:link[@rel='http://www.ibm.com/xmlns/prod/sn/recommendations']/@href"),
;
	

	private final XPathExpression path;
	
	private BlogXPath(String xpath) {
		XPathExpression xpathExpr = null;
		try {
			xpathExpr = DOMUtil.createXPath(xpath);
		} catch (XMLException e) {
			e.printStackTrace();
		}
		this.path = xpathExpr;
	}
	
	
	@Override
	public Object getPath() {
		return path;
	}

	@Override
	public String getName() {
		return this.name();
	}

}
