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
package com.ibm.sbt.services.client.connections.cmisfiles.model;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;
/**
 * @author Vimal Dhupar
 */
public enum CMISFileXPath implements FieldEntry {
	
	uid ("a:id"),
	id("a:id"),
	authorDisplayName("lcmis:displayName"),
	authorPrincipalId("lcmis:principalId"),
	content("a:content[@type='text/plain']"),
	editMediaUrl("a:link[@rel='edit-media']/@href"),
	downloadUrl("a:link[@rel='enclosure']/@href"),
	serviceDocUrl("a:link[@rel='service']/@href"),
	allowableActionsURL("a:link[@rel='http://docs.oasis-open.org/ns/cmis/link/200908/allowableactions']/@href"),
	describedByUrl("a:link[@rel='describedby']/@href"),
	edited("a:edited"),
	relationshipsUrl("a:link[@rel='http://docs.oasis-open.org/ns/cmis/link/200908/relationships']/@href"),
	aclUrl("a:link[@rel='http://docs.oasis-open.org/ns/cmis/link/200908/acl']/@href"),
	aclHistoryUrl("a:link[@rel='http://www.ibm.com/xmlns/prod/sn/cmis/acl-history']/@href"),
	aclRemoverUrl("a:link[@rel='http://www.ibm.com/xmlns/prod/sn/cmis/acl-remover']/@href"),
	policiesUrl("http://docs.oasis-open.org/ns/cmis/link/200908/policies']/@href"),
	versionHistoryUrl("a:link[@rel='version-history']/@href"),
	downloadHistoryUrl("a:link[@rel='http://www.ibm.com/xmlns/prod/sn/cmis/download-history']/@href"),
	recommendationsUrl("a:link[@rel='http://www.ibm.com/xmlns/prod/sn/cmis/recommendations']/@href"),
	recommendationUrl("a:link[@rel='http://www.ibm.com/xmlns/prod/sn/cmis/recommendation']/@href"),
	sharedUrl("a:link[@rel='shared']/@href"),
	pathSegment("cmisra:pathSegment"),
	
	cmisName("cmisra:object/cmis:properties/cmis:propertyString[@propertyDefinitionId='cmis:name']/cmis:value"),
	cmisObjectId("cmisra:object/cmis:properties/cmis:propertyId[@propertyDefinitionId='cmis:objectId']/cmis:value"),
	cmisBaseTypeId("cmisra:object/cmis:properties/cmis:propertyId[@propertyDefinitionId='cmis:baseTypeId']/cmis:value"),
	cmisObjectTypeId("cmisra:object/cmis:properties/cmis:propertyId[@propertyDefinitionId='cmis:objectTypeId']/cmis:value"),
	modifier("cmisra:object/cmis:properties/cmis:propertyString[@propertyDefinitionId='cmis:lastModifiedBy']"),
	modifierName("a:name"),
	modifierEmail("a:email"),
	modifierUserId("snx:userid"),
	modifierUserState("snx:userState"),
	modifierDisplayName("lcmis:displayName"),
	modifierPrincipalId("lcmis:principalId"),
	modificationDate("cmisra:object/cmis:properties/cmis:propertyDateTime[@propertyDefinitionId='cmis:lastModificationDate']/cmis:value"),
	changeToken("cmisra:object/cmis:properties/cmis:propertyString[@propertyDefinitionId='cmis:changeToken']/cmis:value"),
	isImmutable("cmisra:object/cmis:properties/cmis:propertyBoolean[@propertyDefinitionId='cmis:isImmutable']/cmis:value"),
	isLatestVersion("cmisra:object/cmis:properties/cmis:propertyBoolean[@propertyDefinitionId='cmis:isLatestVersion']/cmis:value"),
	isMajorVersion("cmisra:object/cmis:properties/cmis:propertyBoolean[@propertyDefinitionId='cmis:isMajorVersion']/cmis:value"),
	isLatestMajorVersion("cmisra:object/cmis:properties/cmis:propertyBoolean[@propertyDefinitionId='cmis:isLatestMajorVersion']/cmis:value"),
	versionLabel("cmisra:object/cmis:properties/cmis:propertyString[@propertyDefinitionId='cmis:versionLabel']/cmis:value"),  
	versionSeriesId("cmisra:object/cmis:properties/cmis:propertyString[@propertyDefinitionId='cmis:versionSeriesId']/cmis:value"),
	versionSeriesCheckedOutBy("cmisra:object/cmis:properties/cmis:propertyString[@propertyDefinitionId='cmis:versionSeriesCheckedOutBy']/cmis:value"),
	versionSeriesCheckedOutId("cmisra:object/cmis:properties/cmis:propertyId[@propertyDefinitionId='cmis:versionSeriesCheckedOutId']/cmis:value"),
	checkinComment("cmisra:object/cmis:properties/cmis:propertyString[@propertyDefinitionId='cmis:checkinComment']/cmis:value"),
	contentStreamLength("cmisra:object/cmis:properties/cmis:propertyInteger[@propertyDefinitionId='cmis:contentStreamLength']/cmis:value"),
	contentStreamMimeType("cmisra:object/cmis:properties/cmis:propertyString[@propertyDefinitionId='cmis:contentStreamMimeType']/cmis:value"),
	contentStreamFileName("cmisra:object/cmis:properties/cmis:propertyString[@propertyDefinitionId='cmis:contentStreamFileName']/cmis:value"),
	contentStreamId("cmisra:object/cmis:properties/cmis:propertyId[@propertyDefinitionId='cmis:contentStreamId']/cmis:value"),
	repositoryId("cmisra:object/cmis:properties/cmis:propertyId[@propertyDefinitionId='snx:repositoryId']/cmis:value"),
	commentCount("cmisra:object/cmis:properties/cmis:propertyInteger[@propertyDefinitionId='snx:commentCount']/cmis:value"),
	downloadCount("cmisra:object/cmis:properties/cmis:propertyInteger[@propertyDefinitionId='snx:downloadCount']/cmis:value"),
	downloadCountAnon("cmisra:object/cmis:properties/cmis:propertyInteger[@propertyDefinitionId='snx:downloadCountAnon']/cmis:value"),
	sizeAppliedToQuota("cmisra:object/cmis:properties/cmis:propertyInteger[@propertyDefinitionId='snx:sizeAppliedToQuota']/cmis:value"),
	language("cmisra:object/cmis:properties/cmis:propertyString[@propertyDefinitionId='snx:language']/cmis:value"),
	summary("cmisra:object/cmis:properties/cmis:propertyString[@propertyDefinitionId='snx:summary']/cmis:value"),
	contentStreamFileExt("cmisra:object/cmis:properties/cmis:propertyString[@propertyDefinitionId='snx:contentStreamFileExt']/cmis:value"),
	isPublic("cmisra:object/cmis:properties/cmis:propertyBoolean[@propertyDefinitionId='snx:isPublic']/cmis:value"),
	visibility("cmisra:object/cmis:properties/cmis:propertyString[@propertyDefinitionId='snx:visibilityComputed']/cmis:value"),
	isSharedViral("cmisra:object/cmis:properties/cmis:propertyBoolean[@propertyDefinitionId='snx:isSharedViral']/cmis:value"),
	repositoryType("cmisra:object/cmis:properties/cmis:propertyString[@propertyDefinitionId='snx:repositoryType']/cmis:value"),
	recommendationCount("cmisra:object/cmis:properties/cmis:propertyInteger[@propertyDefinitionId='snx:recommendationCount']/cmis:value"),
	isRecommendedByCaller("cmisra:object/cmis:properties/cmis:propertyBoolean[@propertyDefinitionId='snx:isRecommendedByCaller']/cmis:value"),
	contentStreamLastModified("cmisra:object/cmis:properties/cmis:propertyDateTime[@propertyDefinitionId='snx:contentStreamLastModified']/cmis:value"),
	lockType("cmisra:object/cmis:properties/cmis:propertyString[@propertyDefinitionId='snx:lockType']/cmis:value"),
	lockedBy("cmisra:object/cmis:properties/cmis:propertyString[@propertyDefinitionId='snx:lockedBy']/cmis:value"),
	lockedWhen("cmisra:object/cmis:properties/cmis:propertyDateTime[@propertyDefinitionId='snx:lockedWhen']/cmis:value"),
	sharePermission("cmisra:object/cmis:properties/cmis:propertyString[@propertyDefinitionId='snx:sharePermission']/cmis:value");
	
	
	private final XPathExpression path;
	
	private CMISFileXPath(String xpath) {
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
