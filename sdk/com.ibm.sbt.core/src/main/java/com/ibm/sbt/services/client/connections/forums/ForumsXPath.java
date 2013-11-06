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

package com.ibm.sbt.services.client.connections.forums;

/**
 * Class used in resolving XPaths from Forums service
 * @author Manish Kataria
 */


import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;

public enum ForumsXPath implements FieldEntry{
	entry("/a:feed/a:entry"),
	singleEntry("/a:entry"),
	uid("./id"),
	published("./published"),
	updated("./updated"),
	actorEmail("./author/email"),
	actorName("./author/name"),
	actorUserid("./author/snx:userid"),
	actorUserState("./author/snx:userState"),
	contributorEmail("./contributor/email"),
	contributorName("./contributor/name"),
	contributorUserid("./contributor/snx:userid"),
	contributorUserState("./contributor/snx:userState"),
	title("./title"),
	tags("./a:category[not(@scheme)]/@term"),  
	flag("./a:category[@scheme=\"http://www.ibm.com/xmlns/prod/sn/flags\"]/@term"),
	content("./content"),
	topicUrl("./link"),
	permissions("./snx:permissions"),
	inReplyTo("./thr:in-reply-to/@ref"),
	selfUrl("./a:link[@rel=\"self\"]/@href"),
	alternateUrl("./a:link[@rel=\"alternate\"]/@href"),
	moderation("./snx:moderation/@status"),
	threadCount("./link[@rel='replies']/@thr:count"),
	tagEntry("/app:categories/a:category"),
	term("./@term"),
	frequency("./@snx:frequency"),
	intensity("./@snx:intensityBin"), 
	visibility("./@snx:visibilityBin"),
	ThreadRecommendationCount("./a:category[@term='ThreadRecommendationCount'][@scheme=\"http://www.ibm.com/xmlns/prod/sn/flags\"]/@label"),
	RecommendationCount("./a:link[@rel=\"recommendations\"]/@snx:recommendation")
	;


	private final XPathExpression path;
	
	private ForumsXPath(String xpath) {
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
