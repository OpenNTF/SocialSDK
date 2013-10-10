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

package com.ibm.sbt.services.client.connections.activity.model;
/**
 * Enum for Activity Service X Paths
 * @author Vimal Dhupar
 *
 */
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;

public enum ActivityXPath implements FieldEntry {
	
	Email(".//a:entry/a:author/a:email"),
	UserUuid(".//a:author/snx:userid"),
	UserName(".//a:author/a:name"),
	UserState(".//a:author/snx:userState"),
	ContributorUserState(".//a:contributor/snx:userState"),
	ContributorName(".//a:contributor/a:name"),
	ContributorUserUuid(".//a:contributor/snx:userid"),
	ContributorEmail(".//a:contributor/a:email"),
	MemberId("id"),
	MemberIdForSC("contributor/snx:org/uri"),
	ActivityNodeTitle(".//a:title"),
	OwnerId(".//contributor/snx:userid[@snx:role=\"owner\"]"),
	FileName("/a:feed/a:entry/a:title"),
	FileUuid("/a:feed/a:entry/td:uuid"),
	Entry("/a:feed/a:entry | /a:entry"),
	Category(".//a:category[@scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"]/@term"),
	CategoryFlagDelete(".//a:category[@term=\"deleted\"]/@label"),
	CommunityLink(".//a:link[@rel=\"http://www.ibm.com/xmlns/prod/sn/container\"]/@href"),
	Title("/a:entry/app:collection/a:title | .//app:collection/a:title"),
	TotalResults("/a:feed/os:totalResults"),
	StartIndex("/feed/os:startIndex"),
	Published(".//a:published"),
	Updated(".//a:updated"),
	Id(".//a:id"), 
	Position(".//snx:position"), 
	Depth(".//snx:depth"), 
	Permissions(".//snx:permissions"), 
	Icon(".//snx:icon"), 
	Content(".//a:content"),
	ContentType(".//a:content/@type"),
	DueDate(".//snx:duedate"),
	CommunityUuid(".//snx:communityUuid"),
	Theme(".//snx:themeId"),
	CategoryFlagCompleted("//a:category[@term=\"completed\"]/@label"),
	CategoryFlagTemplate("//a:category[@term=\"template\"]/@label"),
	TagTerm("./@term"),
	TagFrequency("./@snx:frequency"),
	TagEntry("/app:categories/a:category"),
	role("./snx:role"),
	summary("./summary"),
	inReplyToId(".//thr:in-reply-to/@ref"),
	inReplyToUrl(".//thr:in-reply-to/@href"),
	inReplyToActivityId(".//thr:in-reply-to/@source"),
	assignedToName("./snx:assignedto/@name"),
	assignedToId("./snx:assignedto/@userid"),
	fieldName(".//@name"),
	fieldHidden(".//@hidden"),
	fieldFid(".//@fid"),
	fieldPosition(".//@position"),
	fieldType(".//@type"),
	field(".//snx:field"),
	textField(".//snx:field[@type=\"text\"]"),
	bookmarkField(".//snx:field[@type=\"link\"]"),
	dateField(".//snx:field[@type=\"date\"]"),
	personField(".//snx:field[@type=\"person\"]"),
	fileField(".//snx:field[@type=\"file\"]"),
	fieldFileUrl("./link/@href"),
	fieldFileSize("./link/@size"),
	fieldFileType("./link/@type"),
	fieldLinkUrl("./link/@href"),
	fieldLinkTitle("./link/@href"),
	fieldPersonName("./name"),
	fieldPersonEmail("./email"),
	fieldPersonUserId("./snx:userid"), 
	nodeUrl(".//a:link[@rel=\"self\"]/@href");
	
	private final XPathExpression path;
	
	private ActivityXPath(final String path) {
		XPathExpression xpathExpr = null;
		try {
			xpathExpr = DOMUtil.createXPath(path);
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
