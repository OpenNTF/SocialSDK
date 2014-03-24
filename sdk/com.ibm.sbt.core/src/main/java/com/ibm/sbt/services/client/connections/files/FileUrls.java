package com.ibm.sbt.services.client.connections.files;

import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.URLBuilder;
import com.ibm.sbt.services.client.base.URLContainer;
import com.ibm.sbt.services.client.base.Version;
import com.ibm.sbt.services.client.base.VersionedUrl;

public enum FileUrls implements URLContainer {
    SERVICE_DOCUMENT(new VersionedUrl(ConnectionsConstants.v4_0, 					"files/{authType}/{accessType}/introspection")),

    GET_NONCE(new VersionedUrl(ConnectionsConstants.v4_0, 							"files/{authType}/{accessType}/nonce")),
    FLAG_AS_INAPPROPRIATE(new VersionedUrl(ConnectionsConstants.v4_0, 				"files/{authType}/{accessType}/reports")),
    MODERATION_SERVICE_DOCUMENT(new VersionedUrl(ConnectionsConstants.v4_0, 		"files/{authType}/{accessType}/moderation/atomsvc")),

    GET_COMMENTS_AWAITING_APPROVAL(new VersionedUrl(ConnectionsConstants.v4_0, 		"files/{authType}/{accessType}/approval/comments")),
    GET_FILES_AWAITING_APPROVAL(new VersionedUrl(ConnectionsConstants.v4_0, 		"files/{authType}/{accessType}/approval/documents")),
    GET_FLAGGED_COMMENTS(new VersionedUrl(ConnectionsConstants.v4_0, 				"files/{authType}/{accessType}/review/comments")),
    GET_FLAGGED_FILES(new VersionedUrl(ConnectionsConstants.v4_0, 					"files/{authType}/{accessType}/review/documents")),

    DELETE_FILE_SHARE(new VersionedUrl(ConnectionsConstants.v4_0, 					"files/{authType}/{accessType}/shares/feed")),
    GET_PUBLIC_FILES(new VersionedUrl(ConnectionsConstants.v4_0, 					"files/{authType}/{accessType}/documents/feed")),
    CREATE_FOLDER(new VersionedUrl(ConnectionsConstants.v4_0, 						"files/{authType}/{accessType}/collections/feed")),
    GET_MY_FOLDERS(new VersionedUrl(ConnectionsConstants.v4_0, 						"files/{authType}/{accessType}/collections/feed")),
    GET_PUBLIC_FOLDERS(new VersionedUrl(ConnectionsConstants.v4_0, 					"files/{authType}/{accessType}/collections/feed")),


    GET_FILES_SHARED_ME(new VersionedUrl(ConnectionsConstants.v4_0, 				"files/{authType}/{accessType}/documents/shared/feed")),
    GET_FILE_SHARES(new VersionedUrl(ConnectionsConstants.v4_0, 					"files/{authType}/{accessType}/documents/shared/feed")),

    UPLOAD_FILE(new VersionedUrl(ConnectionsConstants.v4_0, 						"files/{authType}/{accessType}/myuserlibrary/feed")),
    GET_MY_FILES(new VersionedUrl(ConnectionsConstants.v4_0, 						"files/{authType}/{accessType}/myuserlibrary/feed")),

    GET_ALL_USER_FILES(new VersionedUrl(ConnectionsConstants.v4_0, 					"files/{authType}/{accessType}/userlibrary/{userid}/feed")),

    DELETE_ALL_VERSIONS_OF_FILE(new VersionedUrl(ConnectionsConstants.v4_0, 		"files/{authType}/{accessType}/myuserlibrary/document/{fileId}/feed")),
    SHARE_FILE_WITH_COMMUNITIES(new VersionedUrl(ConnectionsConstants.v4_0, 		"files/{authType}/{accessType}/myuserlibrary/document/{fileId}/feed")),

    UPLOAD_COMMUNITY_FILE(new VersionedUrl(ConnectionsConstants.v4_0, 				"files/{authType}/{accessType}/communitylibrary/{communityId}/feed")),

    UPDATE_FILE_METADATA(new VersionedUrl(ConnectionsConstants.v4_0, 				"files/{authType}/{accessType}/myuserlibrary/document/{fileId}/entry")),
    DELETE_FILE(new VersionedUrl(ConnectionsConstants.v4_0, 						"files/{authType}/{accessType}/myuserlibrary/document/{fileId}/entry")),
    GET_FILE(new VersionedUrl(ConnectionsConstants.v4_0, 							"files/{authType}/{accessType}/myuserlibrary/document/{fileId}/entry")),
    UPLOAD_NEW_VERSION_FILE(new VersionedUrl(ConnectionsConstants.v4_0, 			"files/{authType}/{accessType}/myuserlibrary/document/{fileId}/entry")),


    COMMUNITY_FILE_COMMENT(new VersionedUrl(ConnectionsConstants.v4_0, 				"files/{authType}/{accessType}/communitylibrary/{communityId}/document/{fileId}/feed")),
    GET_COMMUNITY_FILE(new VersionedUrl(ConnectionsConstants.v4_0, 					"files/{authType}/{accessType}/communitylibrary/{communityId}/document/{fileId}/entry")),

    POST_COMMENT_TO_FILE(new VersionedUrl(ConnectionsConstants.v4_0, 				"files/{authType}/{accessType}/userlibrary/{userId}/document/{fileId}/feed")),
    GET_COMMENTS_FEED_FROM_USER_FILE(new VersionedUrl(ConnectionsConstants.v4_0, 	"files/{authType}/{accessType}/userlibrary/{userId}/document/{fileId}/feed")),
    ADD_FILE_TO_FOLDER(new VersionedUrl(ConnectionsConstants.v4_0, 					"files/{authType}/{accessType}/userlibrary/{userId}/document/{fileId}/feed")),
    GET_FILE_IN_MY_LIBRARY(new VersionedUrl(ConnectionsConstants.v4_0, 				"files/{authType}/{accessType}/userlibrary/{userId}/document/{fileId}/entry")),
    UPLOAD_NEW_VERSION_COMMUNITY_FILE(new VersionedUrl(ConnectionsConstants.v4_0, 	"files/{authType}/{accessType}/library/{communityId}/document/{fileId}/entry")),


    POST_COMMENT_TO_FILE_ON_MY_LIBRARY(new VersionedUrl(ConnectionsConstants.v4_0, 	"files/{authType}/{accessType}/myuserlibrary/document/{fileId}/feed")),
    GET_COMMENTS_FEED_FROM_MY_FILE(new VersionedUrl(ConnectionsConstants.v4_0, 		"files}{authType}/{accessType}/myuserlibrary/document/{fileId}/feed")),
    ADD_FILE_FROM_MY_LIBRARY_TO_FOLDER(new VersionedUrl(ConnectionsConstants.v4_0, 	"files/{authType}/{accessType}/myuserlibrary/document/{fileId}/feed")),

    SINGLE_COMMENT_USER_FILE(new VersionedUrl(ConnectionsConstants.v4_0, 			"files/{authType}/{accessType}/userlibrary/{userId}/document/{fileId}/comment/{commentId}/entry")),
    DELETE_COMMENT(new VersionedUrl(ConnectionsConstants.v4_0, 						"files/{authType}/{accessType}/userlibrary/{userId}/document/{fileId}/comment/{commentId}/entry")),

    SINGLE_COMMENT_MY_FILE(new VersionedUrl(ConnectionsConstants.v4_0, 				"files/{authType}/{accessType}/myuserlibrary/document/{fileId}/comment/{commentId}/entry")),
    DELETE_COMMENT_FROM_MY_LIBRARY(new VersionedUrl(ConnectionsConstants.v4_0, 		"files/{authType}/{accessType}/myuserlibrary/document/{fileId}/comment/{commentId}/entry")),


    GET_COMMUNITY_FILES(new VersionedUrl(ConnectionsConstants.v4_0, 				"files/{authType}/{accessType}/communitylibrary/{communityId}/feed")),
    GET_COMMUNITY_COLLECTION(new VersionedUrl(ConnectionsConstants.v4_0, 			"files/{authType}/{accessType}/communitycollection/{communityCollectionId}/feed")),

    DOWNLOAD_FILE(new VersionedUrl(ConnectionsConstants.v4_0, 						"files/{authType}{accessType}/{category}/file/{fileId}/{libraryFilter}/{libraryId}/media")),
    COMMUNITY_FILE_METADATA(new VersionedUrl(ConnectionsConstants.v4_0, 			"files/{authType}/{accessType}/library/{communityLibraryId}/document/{fileId}/entry")),


    FILES_FOLDER(new VersionedUrl(ConnectionsConstants.v4_0, 						"files/{authType}/{accessType}/collection/{collectionId}/feed")),
    REMOVE_FILE_FROM_FOLDER(new VersionedUrl(ConnectionsConstants.v4_0, 			"files/{authType}/{accessType}/collection/{collectionId}/feed")),
    DELETE_FOLDER(new VersionedUrl(ConnectionsConstants.v4_0, 						"files/{authType}/{accessType}/collection/{collectionId}/entry")),
    GET_FOLDER(new VersionedUrl(ConnectionsConstants.v4_0, 							"files/{authType}/{accessType}/collection/{collectionId}/entry")),
    UPDATE_FOLDER(new VersionedUrl(ConnectionsConstants.v4_0, 						"files/{authType}/{accessType}/collection/{collectionId}/entry")),
    GET_FOLDERS_WITH_RECENT_FILES(new VersionedUrl(ConnectionsConstants.v4_0, 		"files/{authType}/{accessType}/collection/addedto/feed")),


    GET_FILES_IN_MY_RECYCLE_BIN(new VersionedUrl(ConnectionsConstants.v4_0, 		"files/{authType}/{accessType}/myuserlibrary/view/recyclebin/feed")),
    EMPTY_MY_RECYCLE_BIN(new VersionedUrl(ConnectionsConstants.v4_0, 				"files/{authType}/{accessType}/myuserlibrary/view/recyclebin/feed")),
    DELETE_FILE_FROM_MY_RECYCLE_BIN(new VersionedUrl(ConnectionsConstants.v4_0, 	"files/{authType}/{accessType}/myuserlibrary/view/recyclebin/{fileId}/entry")),
    GET_FILE_FROM_MY_RECYCLE_BIN(new VersionedUrl(ConnectionsConstants.v4_0, 		"files/{authType}/{accessType}/myuserlibrary/view/recyclebin/{fileId}/entry")),
    RESTORE_FILE_FROM_MY_RECYCLE_BIN(new VersionedUrl(ConnectionsConstants.v4_0, 	"files/{authType}/{accessType}/myuserlibrary/view/recyclebin/{fileId}/entry")),

    EMPTY_RECYCLE_BIN(new VersionedUrl(ConnectionsConstants.v4_0, 					"files/{authType}/{accessType}/userlibrary/{userId}/view/recyclebin/feed")),
    DELETE_FILE_FROM_RECYCLE_BIN(new VersionedUrl(ConnectionsConstants.v4_0, 		"files/{authType}/{accessType}/userlibrary/{userid}/view/recyclebin/{fileId}/entry")),
    GET_FILE_FROM_RECYCLE_BIN(new VersionedUrl(ConnectionsConstants.v4_0, 			"files/{authType}/{accessType}/userlibrary/{userId}/view/recyclebin/{fileId}/entry")),
    RESTORE_FILE_FROM_RECYCLE_BIN(new VersionedUrl(ConnectionsConstants.v4_0, 		"files/{authType}/{accessType}/userlibrary/{userid}/view/recyclebin/{fileId}/entry")),

    GET_FILE_WITH_GIVEN_VERSION(new VersionedUrl(ConnectionsConstants.v4_0, 		"files/{authType}/{accessType}/myuserlibrary/document/{fileId}/version/{versionId}/entry")),
    GET_FLAGGED_FILE_HISTORY(new VersionedUrl(ConnectionsConstants.v4_0, 			"files/{authType}/{accessType}/review/actions/documents/{fileId}")),

    GET_PINNED_FILES(new VersionedUrl(ConnectionsConstants.v4_0, 					"files/{authType}/{accessType}/myfavorites/document/feed")),
    PIN_FILE(new VersionedUrl(ConnectionsConstants.v4_0, 							"files/{authType}/{accessType}/myfavorites/documents/feed")),
    GET_PINNED_FOLDERS(new VersionedUrl(ConnectionsConstants.v4_0, 					"files/{authType}/{accessType}/myfavorites/collections/feed")),
    PIN_FOLDER(new VersionedUrl(ConnectionsConstants.v4_0, 							"files/{authType}/{accessType}/myfavorites/collections/feed")),

    LOCK_FILE(new VersionedUrl(ConnectionsConstants.v4_0, 							"files/{authType}/{accessType}/document/{fileId}/lock")),

    MODERATION(new VersionedUrl(ConnectionsConstants.v4_0, 							"files/{authType}/{accessType}/actions/{action}/{contentType}")),

    DOWNLOADURL(new VersionedUrl(ConnectionsConstants.v4_0, 						"service/files/{proxyPath}/{proxyName}/DownloadFile/{fileId}/{libraryId}"));

	private URLBuilder builder;
	
	private FileUrls(VersionedUrl... urlVersions) {
		builder = new URLBuilder(urlVersions);
	}
	
	public String format(Version version, NamedUrlPart... args) {
		return builder.format(version, args);
	}

	public String getPattern(Version version){
		return builder.getPattern(version).getUrlPattern();
	}
}
