/*
 * © Copyright IBM Corp. 2012,2013
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
define([ "../lang", "./ConnectionsConstants" ], function(lang,conn) {

    return lang.mixin({    	
               
        /**
         * XPath expressions used when parsing a Connections Files ATOM feed
         */
        FileFeedXPath : conn.ConnectionsFeedXPath,
        
        /**
         * XPath expressions used when parsing a Connections Comments ATOM feed
         */
        CommentFeedXPath : conn.ConnectionsFeedXPath,

        /**
         * XPath expressions to be used when reading a File Entry
         */
        FileXPath : {        	
            // used by getEntityData
            entry : "/a:entry",
            id : "a:id",
            // used by getEntityId
            uid : "td:uuid",            
            label : "td:label",
            selfUrl : "a:link[@rel='self']/@href",
            alternateUrl : "a:link[@rel='alternate']/@href",
            downloadUrl : "a:link[@rel='enclosure']/@href",
            type : "a:link[@rel='enclosure']/@type",
            length : "a:link[@rel='enclosure']/@length",
			editLink : "a:link[@rel='edit']/@href",
			editMediaLink : "a:link[@rel='edit-media']/@href",
			thumbnailUrl : "a:link[@rel='thumbnail']/@href",
			commentsUrl : "a:link[@rel='replies']/@href",
			fileSize : "td:totalMediaSize",
			content : "a:content[@type='html']",
			shareCount : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/share']",
									
			authorName : "a:author/a:name",			
			authorUserId : "a:author/snx:userid",
			authorEmail : "a:author/a:email",
			authorUserState : "a:author/snx:userState",
			
			title: "a:title",
			published : "a:published",
			updated : "a:updated",
			created: "td:created",
			modified: "td:modified",
			lastAccessed : "td:lastAccessed",
			
			
			modifierName : "td:modifier/td:name",			
			modifierUserId : "td:modifier/snx:userid",
			modifierEmail : "td:modifier/td:email",
			modifierUserState : "td:modifier/snx:userState",
			
			
			visibility : "td:visibility",
			libraryId : "td:libraryId",
			libraryType : "td:libraryType",
			versionUuid : "td:versionUuid",
			versionLabel : "td:versionLabel",
			propagation : "td:propagation",
			recommendationsCount : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/recommendations']",
			commentsCount : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/comment']",
			sharesCount : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/share']",
			foldersCount :  "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/collections']",
			attachmentsCount : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/attachments']",
			versionsCount : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/versions']",
			referencesCount : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/references']",
			totalMediaSize : "td:totalMediaSize",
			summary : "a:summary",
			contentUrl : "a:content/@src",
			contentType : "a:content/@type",
			objectTypeId : "td:objectTypeId",
			lock : "td:lock/@type",
			acls : "td:permissions",
			hitCount : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/hit']",
			anonymousHitCount : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/anonymous_hit']",
			tags : "a:category/@term",
			category : "a:category/@label"
        },
        
        /**
         * XPath expression for parsing folder informartion from the File ATOM Feed.
         */
        FolderXPath : {
    		"id" : "id",
    		"uid" : "td:uuid",
    		"title" : "a:title",
    		"label" : "td:label",
    		"folderUrl" : "a:link[@rel='alternate']/@href",
    		"logoUrl" : "a:link[@rel='http://www.ibm.com/xmlns/prod/sn/logo']/@href",
    		"tags" : "a:category/@term",
    		"summary" : "a:summary[@type='text']",
    		"content" : "a:content[@type='html']",
    		"visibility" : "td:visibility",
    		"notification" : "td:notification",
    		"versionUuid" : "td:versionUuid",
    		"versionLabel" : "td:versionLabel",
    		"documentVersionUuid" : "td:documentVersionUuid",
    		"documentVersionLabel" : "td:documentVersionLabel",
    		"itemCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/item']",
    		"shareCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/user']",
    		"groupShareCount" : "snx:rank[@scheme='http://www.ibm.com/xmlns/prod/sn/group']",
    		"modified" : "td:modified",
    		"created" : "td:created",
    		"updated" : "a:updated",
    		"authorName" : "a:author/a:name",
    		"authorUserId" : "a:author/snx:userid",
    		"authorEmail" : "a:author/a:email",
    		"content" : "a:content[@type='text']",
    		"modifierName" : "a:modifier/a:name",
    		"modifierId" : "a:modifier/snx:userid",
    		"modifierEmail" : "a:modifier/a:email"
        },
        
        /**
         * XPath expressions to be used when reading a Comment
         */
		CommentXPath : {
			entry : "a:entry",
			id : "a:id",
			uid : "td:uuid",
			title : "a:title",
			content : "a:content[@type='text']",
			created : "td:created",
			modified : "td:modified",
			versionLabel : "td:versionLabel",
			updated : "a:updated",
			published : "a:published",
			modifierName : "td:modifier/td:name",			
			modifierUserId : "td:modifier/snx:userid",
			modifierEmail : "td:modifier/td:email",
			modifierUserState : "td:modifier/snx:userState",		
			authorName : "a:author/a:name",			
			authorUserId : "a:author/snx:userid",
			authorEmail : "a:author/a:email",
			authorUserState : "a:author/snx:userState",
			language : "td:language",			
			deleteWithRecord : "td:deleteWithRecord"		
		},   
		
		/**
		 * XPath expressions used for parsing information on shared files and folders.
		 */
		SharesXPath : {
			"id" : "id",
			"uuid" : "td:uuid",
			"title" : "a:title",
			"summary" : "a:summary[@type='text']",
			"sharedResourceType" : "td:sharedResourceType", // always set to document, but will add the attribute anyway
			"sharePermission" : "td:sharePermission",
			"sharedWhat" : "td:sharedWhat",
			"sharedWithName" : "a:sharedWith/a:name",
			"sharedWithId" : "a:sharedWith/snx:userid",
			"sharedWithEmail" : "a:sharedWith/a:email",
			"documentOwner" : "td:documentOwner",
			"updated" : "a:updated",
			"published" : "a:published",
			"authorName" : "a:author/a:name",
			"authorUid" : "a:author/snx:userid",
			"authorEmail" : "a:author/a:email"
		},
		
		/**
		 * Get a Feed for a File 
		 */
		AtomFileInstance : "${files}/basic/api/myuserlibrary/document/{documentId}/entry",
		
		/**
		 * 
		 */
		AtomFileInstancePublic : "${files}/basic/anonymous/api/library/{libraryId}/document/{documentId}/entry",

        /**
         * A feed of files of which the authenticated user owns.
         * 
         * Get the My Files feed to see a list of the files which the authenticated owns. 
         * Supports: acls , includePath , includeQuota , includeTags , page , ps , shared , sI , since , sortBy , sortOrder , tag , visibility
         */
        AtomFilesMy : "/${files}/basic/api/myuserlibrary/feed",
        
        /**
         * A feed of files with are shared by or with the authenticated user.
         * 
         * Get the My Files feed to see a list of the files which the authenticated owns. 
         * Supports: direction (default inbound : with me, outbound : by me),  acls , includePath , includeQuota , includeTags , page , ps , shared , sI , since , sortBy , sortOrder , tag , visibility
         */
        AtomFilesShared : "/${files}/basic/api/documents/shared/feed",
        
        /**
         * Get a feed that lists all public files.
         *         
         * Supports: acls , includePath , includeQuota , includeTags , page , ps , shared , sI , since , sortBy , sortOrder , tag , visibility
         */
        AtomFilesPublic : "/${files}/basic/anonymous/api/documents/feed?visibility=public",
        
        /**
         * Get feed of recycled files
         */
        AtomFilesRecycled : "/${files}/basic/api/myuserlibrary/view/recyclebin/feed",
        
        /**
         * Get a feed that lists your folders
         *         
         * Supports: access(editor or manager), creator, page, ps, shared, sharedWithMe, sI, sortBy, sortOrder, title, visibility 
         */
        AtomFoldersMy : "/${files}/basic/api/collections/feed",
        
        /**
         * Feed of public folders
         */
        AtomFoldersPublic : "/${files}/basic/anonymous/api/collections/feed",
        
        /**
         * Feed of folders you recently added files to
         */
        AtomFoldersActive : "/${files}/basic/api/collections/addedto/feed",
        
        /**
         * A feed of comments associated with all public files. Do not authenticate this request.
         *         
         * Supports: acls, category  Note: This parameter is required., commentId, identifier, page, ps, sI, sortBy, sortOrder  
         */
        AtomFileCommentsPublic : "/${files}/basic/anonymous/api/userlibrary/{userId}/document/{documentId}/feed?category=comment",
        
        /**
         * A feed of comments associated with files to which you have access. You must authenticate this request. 
         *         
         * Supports: acls, category  Note: This parameter is required., commentId, identifier, page, ps, sI, sortBy, sortOrder  
         */
        AtomFileCommentsMy : "/${files}/basic/api/userlibrary/{userId}/document/{documentId}/feed?category=comment",
        
        /**
         * Adds a comment to the specified file.
         * 
         * Supports : identifier - Indicates how the document is identified in the {document-id} variable segment of the web address. By default, look up is performed with the expectation that the URL contains the value from the <td:uuid> element of a File Atom entry. Specify "label" if the URL instead contains the value from the <td:label> element of a File Atom entry. 
         */
        AtomAddCommentToFile : "/${files}/basic/api/userlibrary/{userId}/document/{documentId}/feed",
        
        /**
         * Adds a comment to the specified file for logged in user.
         * 
         * Supports : identifier - Indicates how the document is identified in the {document-id} variable segment of the web address. By default, look up is performed with the expectation that the URL contains the value from the <td:uuid> element of a File Atom entry. Specify "label" if the URL instead contains the value from the <td:label> element of a File Atom entry. 
         */
        AtomAddCommentToMyFile : "/${files}/basic/api/myuserlibrary/document/{documentId}/feed",        
        
        /**
         * 	Update the Atom document representation of the metadata for a file from logged in user's library.
         * 
         * supports : 
         * propagate	Indicates if users that are shared with can share this document. The default value is true.
         * sendEmail	Indicates if an email notification is sent when sharing with the specified user. The default value is true.
         */
        AtomUpdateFileMetadata : "/${files}/basic/api/myuserlibrary/document/{documentId}/entry",
        
        /**
         * Get pinned files from my my favorites feed.
         * 
         */
        AtomFilesMyPinned : "/${files}/basic/api/myfavorites/documents/feed",
        
        /**
         * Add a file to my favorites feed of logged in user
         * 
         */
        AtomPinFile : "/${files}/basic/api/myfavorites/documents/feed",
        
        /**
         * Add file of list of files to folder
         */
        AtomAddFilesToFolder : "/${files}/basic/api/collection/{collectionId}/feed",
        
        /**
         * Delete a file and the Atom document representation of its associated metadata from logged in user's collection.
         */
        AtomDeleteFile :  "/${files}/basic/api/myuserlibrary/document/{documentId}/entry",
        
        /**
         * Lock or unlock a file
         */
        AtomLockUnlockFile : "/${files}/basic/api/document/{documentId}/lock",
        
        /**
         * Add the document to collections specified by atom entry or feed.
         */
        AtomAddFileToFolders : "/${files}/basic/api/userlibrary/{userId}/document/{documentId}/feed",
        /**
        * Add the document of logged in user to collections specified by atom entry or feed.
        */
        AtomAddMyFileToFolders : "/${files}/basic/api/myuserlibrary/document/{documentId}/feed",
        
        /**
         * Create a file folder programmatically.
         */
        AtomCreateFolder : "/${files}/basic/api/collections/feed",
        
        /**
         * Delete all files from recycle bin of specified user
         */
        AtomDeleteAllFilesFromRecyclebBin : "/${files}/basic/api/userlibrary/{userId}/view/recyclebin/feed",
        
        /**
         * Delete all files from recycle bin of logged in user
         */
        AtomDeleteMyFilesFromRecyclebBin : "/${files}/basic/api/myuserlibrary/view/recyclebin/feed",
        
        /**
         * Delete All Versions of a File
         */
        AtomDeleteAllVersionsOfAFile : "/${files}/basic/api/myuserlibrary/document/{documentId}/feed",
        
        /**
         * Delete a Comment for a File
         */
        AtomDeleteComment : "/${files}/basic/api/userlibrary/{userId}/document/{documentId}/comment/{commentId}/entry",
        
        /**
         * Delete a comment on file for logged in user         
         */
        AtomDeleteMyComment : "/${files}/basic/api/myuserlibrary/document/{documentId}/comment/{commentId}/entry",
        
        /**
         * Purge a file from Recycle Bin
         */
        AtomDeleteFileFromRecycleBin : "/${files}/basic/api/userlibrary/{userId}/view/recyclebin/{documentId}/entry",
        
        /**
         * Purge a file from REcycle Bin for Logged in user
         */
        AtomDeleteMyFileFromRecycleBin : "/${files}/basic/api/myuserlibrary/view/recyclebin/{documentId}/entry",
        
        /**
         * Remove a file Share
         */
        AtomDeleteFileShare : "/${files}/basic/api/shares/feed",
        
        /**
         * Delete a Folder
         */
        AtomDeleteFolder : "/${files}/basic/api/collection/{collectionId}/entry",
        
        /**
         * Get Files for a user 
         */
        AtomGetAllUsersFiles : "/${files}/basic/anonymous/api/userlibrary/{userId}/feed",
        
        /**
         * Get a comment for a file
         */
        AtomGetFileComment : "/${files}/basic/api/userlibrary/{userId}/document/{documentId}/comment/{commentId}/entry",
        
        /**
         * Get a comment for a File for logged in user
         */
        AtomGetMyFileComment : "/${files}/basic/api/myuserlibrary/document/{documentId}/comment/{commentId}/entry",
        
        /**
         * Get File from Recycle Bin
         */
        AtomGetFileFromRecycleBin : "/${files}/basic/api/userlibrary/{userId}/view/recyclebin/{documentId}/entry",
        
        /**
         * Get Files Awaiting Approval
         */
        AtomGetFilesAwaitingApproval : "/${files}/basic/api/approval/documents",
        
        /**
         * Get File Shares
         */
        AtomGetFileShares : "/${files}/basic/api/documents/shared/feed",
        
        /**
         * Get All Files in a Folder
         */
        AtomGetFilesInFolder : "/${files}/basic/api/collection/{collectionId}/feed",
        
        /**
         * Get Files in Recycle Bin of logged in user
         */
        AtomGetFilesInMyRecycleBin : "/${files}/basic/api/myuserlibrary/view/recyclebin/feed",
        
        /**
         * Get file with given version
         */
        AtomGetFileWithGivenVersion : "/${files}/basic/api/myuserlibrary/document/{documentId}/version/{versionId}/entry",
        
        /**
         * Get a folder
         */
        AtomGetFolder : "/${files}/basic/api/collection/{collectionId}/entry",
        
        /**
         * Get Folder with Recenty Added Files
         */
        AtomGetFoldersWithRecentlyAddedFiles : "/${files}/basic/api/collections/addedto/feed",
        
        /**
         * Get Pinned Folders
         */
        AtomGetPinnedFolders : "/${files}/basic/api/myfavorites/collections/feed",
        
        /**
         * Get Public Folders
         */
        AtomGetPublicFolders : "/${files}/basic/anonymous/api/collections/feed",
        
        /**
         * Pin/unpin a Folder
         */
        AtomPinFolder : "/${files}/basic/api/myfavorites/collections/feed",
        
        /**
         * Remove File from Folder
         */
        AtomRemoveFileFromFolder : "/${files}/basic/api/collection/{collectionId}/feed",
        
        /**
         * Restore File from Recycle Bin
         */
        AtomRestoreFileFromRecycleBin : "/${files}/basic/api/userlibrary/{userId}/view/recyclebin/{documentId}/entry",
        
        /**
         * Share File with Community or communities
         */
        AtomShareFileWithCommunities : "/${files}/basic/api/myuserlibrary/document/{documentId}/feed",
        
       /**
        * Update a Comment
        */
        AtomUpdateComment : "/${files}/basic/api/userlibrary/{userId}/document/{documentId}/comment/{commentId}/entry",
        
        /**
         * Update comment of logged in user
         */
        AtomUpdateMyComment : "/${files}/basic/api/myuserlibrary/document/{documentId}/comment/{commentId}/entry",
        
        /**
         * Add Comment To Community File
         */
        AtomAddCommentToCommunityFile : "/${files}/basic/api/communitylibrary/{communityId}/document/{documentId}/feed",
        
        /**
         * Get All Files in a Community
         */
        AtomGetAllFilesInCommunity : "/${files}/basic/api/communitylibrary/{communityId}/feed",
        
        /**
         * Get Community File
         */
        AtomGetCommunityFile : "/${files}/basic/api/communitylibrary/{communityId}/document/{documentId}/entry",
       
        /**
         * Update metadata of community File
         */
        AtomUpdateCommunityFileMetadata : "/${files}/basic/api/library/{libraryId}/document/{documentId}/entry"
    }, conn);
});