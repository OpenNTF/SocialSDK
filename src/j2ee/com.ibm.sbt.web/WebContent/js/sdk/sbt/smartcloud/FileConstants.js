/*
 * © Copyright IBM Corp. 2012
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
/**
 * Social Business Toolkit SDK. Definition of constants for FileService.
 */
define([], function() {
	return sbt.smartcloud.fileConstants = {
		sbtErrorCodes : {
			badRequest : 400
		},
		entityServiceBaseUrl : "/files/basic/cmis/repository/p!{subscriberId}",
		serviceEntity : {
			FILE : "/folderc",
			ENTRY : "/object",
		},
		entityType : {
			GET_MY_FILES : "/snx:files",
			GET_FILE_ENTRY : "/snx:file!{fileId}",
			POST_FILE_UPLOAD : "/snx:files",
			GET_MY_FILES_ALT : "/snx:virtual!.!filesownedby",
			GET_MY_FILES_WITH_FILTER : "/snx:files",
			UPDATE_FILE : "/snx:file!{fileId}/stream/{fileId}"
		},
		entityServiceBaseUrl : "/files/basic/cmis/repository/p!{subscriberId}",		
		xpath_feed_File : {
			entry : "/a:feed/a:entry",
			id : "a:id",
		},
		xpath_File : {
			entry : "/a:entry",
			authorName : "a:author/a:name",
			authorDisplayName : "a:author/lcmis:displayName",
			authorEmail : "a:author/a:email",
			otherEmail : "a:author/lcmis:email",
			userId : "a:author/snx:userid", // TODO: do we go for uniform case here or
			// just what the service provide?
			userState : "a:author/snx:userState",
			authorPrincipalId : "author/lcmis:principalId",
			authorOrgId : "a:author/snx:orgId",
			authorOrgName : "a:author/snx:orgName",
			publishDate : "a:published",
			lastEditDate : "app:edited",
			lastUpdateDate : "a:updated",
			summary : "a:summary",
			title : "a:title",
			contentLink : "a:content/@src",
			downloadLink : "a:link[@rel='enclosure']/@href",
			pageLink : "a:link[@rel='alternate']/@href",
			serviceURL : "a:link[@rel='service']/@href", // TODO: name convention (A
			// link is for human, a
			// url is for service?)
			selfURL : "a:link[@rel='self']/@href",
			editLink : "a:link[@rel='edit']/@href",
			editMediaLink : "a:link[@rel='edit-media']/@href",
			permissionsURL : "a:link[@rel='http://docs.oasis-open.org/ns/cmis/link/200908/allowableactions']/@href",
			descriptionLink : "a:link[@rel='describedby']/@href",
			relationshipsURL : "a:link[@rel='http://docs.oasis-open.org/ns/cmis/link/200908/relationships']/@href",
			aclURL : "a:link[@rel='http://docs.oasis-open.org/ns/cmis/link/200908/acl']/@href",
			aclHistoryURL : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/cmis/acl-history']/@href",
			aclRemoverURL : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/cmis/acl-remover']/@href",
			policiesURL : "a:link[@rel='http://docs.oasis-open.org/ns/cmis/link/200908/policies']/@href",
			dataURL : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/cmis/multipart-form/object']/@href",
			versionHistoryURL : "a:link[@rel='version-history']/@href",
			versionHistoryAlternateURL : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/cmis/version-history']/@href",
			downloadHistoryURL : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/cmis/download-history']/@href",
			recommendationsURL : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/cmis/recommendations']/@href",
			recommendationURL : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/cmis/recommendation']/@href",
			sharingInformationURL : "a:link[@rel='shared']/@href",
			pathSegment : "cmisra:pathSegment",
			fileEntryDisplayName : "cmisra:object/cmis:properties//*[@queryName='cmis:name']/cmis:value",

			// TODO: test the decoding over these entries
			fileEntrySharePermission : "cmisra:object/cmis:properties//*[@queryName='snx:sharePermission']/cmis:value",
			fileEntryVersionSeriesCheckedOutId : "cmisra:object/cmis:properties//*[@queryName='cmis:versionSeriesCheckedOutId']/cmis:value",

			fileEntryCreatedBy : "cmisra:object/cmis:properties//*[@queryName='cmis:createdBy']",
			fileEntryModifiedBy : "cmisra:object/cmis:properties//*[@queryName='cmis:lastModifiedBy']",
			fileEntryCheckedOutBy : "cmisra:object/cmis:properties//*[@queryName='cmis:versionSeriesCheckedOutBy']",
			fileEntryLockedBy : "cmisra:object/cmis:properties//*[@queryName='snx:lockedBy']",

			// END

			fileEntryObjectId : "cmisra:object/cmis:properties//*[@queryName='cmis:objectId']/cmis:value",
			fileEntryBaseTypeId : "cmisra:object/cmis:properties//*[@queryName='cmis:baseTypeId']/cmis:value",
			fileEntryObjectTypeId : "cmisra:object/cmis:properties//*[@queryName='cmis:objectTypeId']/cmis:value",
			fileEntryCreationDate : "cmisra:object/cmis:properties//*[@queryName='cmis:creationDate']/cmis:value",
			fileEntryLastModificationDate : "cmisra:object/cmis:properties//*[@queryName='cmis:lastModificationDate']/cmis:value",
			fileEntryChangeToken : "cmisra:object/cmis:properties//*[@queryName='cmis:changeToken']/cmis:value",
			fileEntryIsImmutable : "cmisra:object/cmis:properties//*[@queryName='cmis:isImmutable']/cmis:value",
			fileEntryIsLastVersion : "cmisra:object/cmis:properties//*[@queryName='cmis:isLatestVersion']/cmis:value",
			fileEntryIsMajorVersion : "cmisra:object/cmis:properties//*[@queryName='cmis:isMajorVersion']/cmis:value",
			fileEntryIsLatestMajorVersion : "cmisra:object/cmis:properties//*[@queryName='cmis:isLatestMajorVersion']/cmis:value",
			fileEntryVersionLabel : "cmisra:object/cmis:properties//*[@queryName='cmis:versionLabel']/cmis:value",
			fileEntryVersionSeriesId : "cmisra:object/cmis:properties//*[@queryName='cmis:versionSeriesId']/cmis:value",
			fileEntryIsVersionSeriesCheckedOut : "cmisra:object/cmis:properties//*[@queryName='cmis:isVersionSeriesCheckedOut']/cmis:value",
			fileEntryCheckinComment : "cmisra:object/cmis:properties//*[@queryName='cmis:checkinComment']/cmis:value",
			fileEntryContentStreamLength : "cmisra:object/cmis:properties//*[@queryName='cmis:contentStreamLength']/cmis:value",
			fileEntryContentStreamMimeType : "cmisra:object/cmis:properties//*[@queryName='cmis:contentStreamMimeType']/cmis:value",
			fileEntryContentStreamFileName : "cmisra:object/cmis:properties//*[@queryName='cmis:contentStreamFileName']/cmis:value",
			fileEntryContentStreamId : "cmisra:object/cmis:properties//*[@queryName='cmis:contentStreamId']/cmis:value",
			fileEntryrepositoryId : "cmisra:object/cmis:properties//*[@queryName='snx:repositoryId']/cmis:value",
			fileEntryCommentCount : "cmisra:object/cmis:properties//*[@queryName='snx:commentCount']/cmis:value",
			fileEntryDownloadCount : "cmisra:object/cmis:properties//*[@queryName='snx:downloadCount']/cmis:value",
			fileEntryDownloadCountAnon : "cmisra:object/cmis:properties//*[@queryName='snx:downloadCountAnon']/cmis:value",
			fileEntryTotalSize : "cmisra:object/cmis:properties//*[@queryName='snx:sizeAppliedToQuota']/cmis:value",
			fileEntryContentLanguage : "cmisra:object/cmis:properties//*[@queryName='snx:language']/cmis:value",
			fileEntryFileExtension : "cmisra:object/cmis:properties//*[@queryName='snx:contentStreamFileExt']/cmis:value",
			fileEntryIsPublic : "cmisra:object/cmis:properties//*[@queryName='snx:isPublic']/cmis:value",
			fileEntryVisibility : "cmisra:object/cmis:properties//*[@queryName='snx:visibilityComputed']/cmis:value",
			fileEntryIsViral : "cmisra:object/cmis:properties//*[@queryName='snx:isSharedViral']/cmis:value",
			fileEntryRepositoryType : "cmisra:object/cmis:properties//*[@queryName='snx:repositoryType']/cmis:value",
			fileEntryRecommendationCount : "cmisra:object/cmis:properties//*[@queryName='snx:recommendationCount']/cmis:value",
			fileEntryIsRecommendedByCaller : "cmisra:object/cmis:properties//*[@queryName='snx:isRecommendedByCaller']/cmis:value",
			fileEntryStreamLastModified : "cmisra:object/cmis:properties//*[@queryName='snx:contentStreamLastModified']/cmis:value",
			fileEntryIsExternal : "cmisra:object/cmis:properties//*[@queryName='snx:isExternal']/cmis:value",
			fileEntryLockType : "cmisra:object/cmis:properties//*[@queryName='snx:lockType']/cmis:value",
			fileEntryLockedWhen : "cmisra:object/cmis:properties//*[@queryName='snx:lockedWhen']/cmis:value",
			fileEntryIsEncrypted : "cmisra:object/cmis:properties//*[@queryName='snx:encrypt']/cmis:value",
			fileEntryOrgnizationId : "cmisra:object/cmis:properties//*[@queryName='snx:orgId']/cmis:value",
			fileEntryOrganizationName : "cmisra:object/cmis:properties//*[@queryName='snx:orgName']/cmis:value"
		},
		xpath_UserProfile : {
			profileNode_value : "cmis:value",
			profileNode_name : "a:name",
			profileNode_email : "a:email",
			profileNode_otherEmail : "lcmis:email",
			profileNode_userId : "snx:userid",
			profileNode_userState : "snx:userState",
			profileNode_displayName : "lcmis:displayName",
			profileNode_principalId : "lcmis:principalId",
			profileNode_orgId : "snx:orgId",
			profileNode_orgName : "snx:orgName"
		},
		fileUpdateProperties : {
			isPublic : "Boolean",
			lockType : "String"
		}
	};
});
