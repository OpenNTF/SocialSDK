/*
 * © Copyright IBM Corp. 2014
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
package com.ibm.sbt.services.client.connections.files;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.v4_0;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.URLBuilder;
import com.ibm.sbt.services.client.base.URLContainer;
import com.ibm.sbt.services.client.base.Version;
import com.ibm.sbt.services.client.base.VersionedUrl;

/**
 * @author Carlos Manias
 */
public enum FileUrls implements URLContainer {
    SERVICE_DOCUMENT(new VersionedUrl(v4_0, 					"{files}/{authType}/{accessType}/introspection")),
    GET_NONCE(new VersionedUrl(v4_0, 							"{files}/{authType}/{accessType}/nonce")),
    FLAG_AS_INAPPROPRIATE(new VersionedUrl(v4_0, 				"{files}/{authType}/{accessType}/reports")),
    MODERATION_SERVICE_DOCUMENT(new VersionedUrl(v4_0, 			"{files}/{authType}/{accessType}/moderation/atomsvc")),
    GET_COMMENTS_AWAITING_APPROVAL(new VersionedUrl(v4_0, 		"{files}/{authType}/{accessType}/approval/comments")),
    GET_FILES_AWAITING_APPROVAL(new VersionedUrl(v4_0, 			"{files}/{authType}/{accessType}/approval/documents")),
    GET_FLAGGED_COMMENTS(new VersionedUrl(v4_0, 				"{files}/{authType}/{accessType}/review/comments")),
    GET_FLAGGED_FILES(new VersionedUrl(v4_0, 					"{files}/{authType}/{accessType}/review/documents")),
    DELETE_FILE_SHARE(new VersionedUrl(v4_0, 					"{files}/{authType}/{accessType}/shares/feed")),
    GET_PUBLIC_FILES(new VersionedUrl(v4_0, 					"{files}/{authType}/{accessType}/documents/feed?visibility=public")),
    COLLECTIONS_FEED(new VersionedUrl(v4_0, 					"{files}/{authType}/{accessType}/collections/feed")),
    DOCUMENTS_SHARED_FEED(new VersionedUrl(v4_0, 				"{files}/{authType}/{accessType}/documents/shared/feed")),
    MYUSERLIBRARY_FEED(new VersionedUrl(v4_0, 					"{files}/{authType}/{accessType}/myuserlibrary/feed")),
    GET_ALL_USER_FILES(new VersionedUrl(v4_0, 					"{files}/{authType}/{accessType}/userlibrary/{userId}/feed")),
    MYUSERLIBRARY_DOCUMENT_FEED(new VersionedUrl(v4_0, 			"{files}/{authType}/{accessType}/myuserlibrary/document/{fileId}/feed")),
    COMMUNITYLIBRARY_FEED(new VersionedUrl(v4_0, 				"{files}/{authType}/{accessType}/communitylibrary/{communityId}/feed")),
    MYUSERLIBRARY_DOCUMENT_ENTRY(new VersionedUrl(v4_0, 		"{files}/{authType}/{accessType}/myuserlibrary/document/{fileId}/entry")),
    COMMUNITY_FILE_COMMENT(new VersionedUrl(v4_0, 				"{files}/{authType}/{accessType}/communitylibrary/{communityId}/document/{fileId}/feed")),
    GET_COMMUNITY_FILE(new VersionedUrl(v4_0, 					"{files}/{authType}/{accessType}/communitylibrary/{communityId}/document/{fileId}/entry")),
    USERLIBRARY_DOCUMENT_FEED(new VersionedUrl(v4_0, 			"{files}/{authType}/{accessType}/userlibrary/{userId}/document/{fileId}/feed")),
    UPLOAD_NEW_VERSION_COMMUNITY_FILE(new VersionedUrl(v4_0, 	"{files}/{authType}/{accessType}/library/{communityId}/document/{fileId}/entry")),
    USERLIBRARY_DOCUMENT_COMMENT_ENTRY(new VersionedUrl(v4_0, 	"{files}/{authType}/{accessType}/userlibrary/{userId}/document/{fileId}/comment/{commentId}/entry")),
    MYUSERLIBRARY_DOCUMENT_COMMENT_ENTRY(new VersionedUrl(v4_0, "{files}/{authType}/{accessType}/myuserlibrary/document/{fileId}/comment/{commentId}/entry")),
    GET_COMMUNITY_COLLECTION(new VersionedUrl(v4_0, 			"{files}/{authType}/{accessType}/communitycollection/{communityId}/feed")),
    DOWNLOAD_FILE(new VersionedUrl(v4_0, 						"{files}/{authType}/{accessType}/{category}/file/{fileId}/{libraryFilter}/{libraryId}/media")),
    COMMUNITY_FILE_METADATA(new VersionedUrl(v4_0, 				"{files}/{authType}/{accessType}/library/{communityLibraryId}/document/{fileId}/entry")),
    COLLECTION_FEED(new VersionedUrl(v4_0, 						"{files}/{authType}/{accessType}/collection/{folderId}/feed")),
    COLLECTION_ENTRY(new VersionedUrl(v4_0,						"{files}/{authType}/{accessType}/collection/{folderId}/entry")),
    COMMUNITY_COLLECTIONS_FEED(new VersionedUrl(v4_0, 		"{files}/{authType}/{accessType}/communitycollection/{communityId}/feed")),
    GET_FOLDERS_WITH_RECENT_FILES(new VersionedUrl(v4_0, 		"{files}/{authType}/{accessType}/collections/addedto/feed")),
    MYUSERLIBRARY_RECYCLEBIN_FEED(new VersionedUrl(v4_0, 		"{files}/{authType}/{accessType}/myuserlibrary/view/recyclebin/feed")),
    MYUSERLIBRARY_RECYCLEBIN_ENTRY(new VersionedUrl(v4_0, 		"{files}/{authType}/{accessType}/myuserlibrary/view/recyclebin/{fileId}/entry")),
    EMPTY_RECYCLE_BIN(new VersionedUrl(v4_0, 					"{files}/{authType}/{accessType}/userlibrary/{userId}/view/recyclebin/feed")),
    USERLIBRARY_RECYCLEBIN_ENTRY(new VersionedUrl(v4_0, 		"{files}/{authType}/{accessType}/userlibrary/{userid}/view/recyclebin/{fileId}/entry")),
    GET_FILE_WITH_GIVEN_VERSION(new VersionedUrl(v4_0, 			"{files}/{authType}/{accessType}/myuserlibrary/document/{fileId}/version/{versionId}/entry")),
    GET_FLAGGED_FILE_HISTORY(new VersionedUrl(v4_0, 			"{files}/{authType}/{accessType}/review/actions/documents/{fileId}")),
    MYFAVORITES_DOCUMENTS_FEED(new VersionedUrl(v4_0,			"{files}/{authType}/{accessType}/myfavorites/documents/feed")),
    MYFAVORITES_COLLECTIONS_FEED(new VersionedUrl(v4_0, 		"{files}/{authType}/{accessType}/myfavorites/collections/feed")),
    LOCK_FILE(new VersionedUrl(v4_0, 							"{files}/{authType}/{accessType}/document/{fileId}/lock")),
    MODERATION(new VersionedUrl(v4_0, 							"{files}/{authType}/{accessType}/actions/{action}/{contentType}")),
    DOWNLOADURL(new VersionedUrl(v4_0, 							"service/files/{proxyPath}/{proxyName}/DownloadFile/{fileId}/{libraryId}"));

	private URLBuilder builder;
	
	private FileUrls(VersionedUrl... urlVersions) {
		builder = new URLBuilder(urlVersions);
	}
	
	public String format(BaseService service, NamedUrlPart... args) {
		return builder.format(service, args);
	}

	public String getPattern(Version version){
		return builder.getPattern(version).getUrlPattern();
	}
}
