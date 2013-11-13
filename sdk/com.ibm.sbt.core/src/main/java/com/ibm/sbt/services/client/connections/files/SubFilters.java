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
package com.ibm.sbt.services.client.connections.files;

import com.ibm.commons.util.StringUtil;

/**
 * Files Sub Filters<br>
 * Class which determines the SubFilters to be used for Executing FileService API <br>
 * This class takes care of the Value { document : "documentId" } <br>
 * and the Key { "document" : documentId }
 * 
 * @author Vimal Dhupar
 */
public class SubFilters {

	public static String	FILE		= "/document";
	public static String	DOCUMENTS	= "/documents";
	public static String	COMMENT		= "/comment";
	public static String	COLLECTION	= "/collection";
	public static String	USERLIBRARY	= "/userlibrary";
	public static String	LIBRARY		= "/library";
	public static String	RECYCLEBIN	= "/view/recyclebin";
	public static String	VERSION		= "/version";
	public static String 	COMMUNITYCOLLECTION = "/communitycollection";
	public static String 	COMMUNITYLIBRARY = "/communitylibrary";

	private String			userId;
	private String			libraryId;
	private String			fileId;
	private String			documentsId;
	private String			commentId;
	private String			collectionId;
	private String			recycleBinDocumentId;
	private String			versionId;
	private String			communityCollectionId;
	private String			communityLibraryId;

	public SubFilters() {
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String documentId) {
		this.fileId = documentId;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(String collection_id) {
		this.collectionId = collection_id;
	}

	public String getRecycleBinDocumentId() {
		return recycleBinDocumentId;
	}

	public void setRecycleBinDocumentId(String recycleBinDocumentId) {
		this.recycleBinDocumentId = recycleBinDocumentId;
	}

	public String getDocumentsId() {
		return documentsId;
	}

	public void setDocumentsId(String documentsId) {
		this.documentsId = documentsId;
	}

	public boolean isEmpty() {
		if (StringUtil.isEmpty(userId) && StringUtil.isEmpty(fileId) && StringUtil.isEmpty(commentId)
				&& StringUtil.isEmpty(collectionId)) {
			return true;
		}
		return false;
	}

	public void setCommunityCollectionId(String communityCollectionId) {
		this.communityCollectionId = communityCollectionId;
	}

	public String getCommunityCollectionId() {
		return communityCollectionId;
	}

	public void setCommunityLibraryId(String communityLibraryId) {
		this.communityLibraryId = communityLibraryId;
	}

	public String getCommunityLibraryId() {
		return communityLibraryId;
	}

	public void setLibraryId(String libraryId) {
		this.libraryId = libraryId;
	}

	public String getLibraryId() {
		return libraryId;
	}
}
