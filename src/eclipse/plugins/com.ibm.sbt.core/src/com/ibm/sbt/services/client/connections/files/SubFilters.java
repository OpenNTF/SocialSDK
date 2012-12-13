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
 * Files Sub Filters
 * @author Vimal Dhupar
 */
public class SubFilters {
	// This class takes care of the Value { document : "documentId" }
	// The enum SubFilterKey takes care of the Key { "document" : documentId }
	
	public static String DOCUMENT = "/document";
	public static String COMMENT = "/comment";
	public static String COLLECTION = "/collection";
	public static String LIBRARY = "/userlibrary";
	
	private String userId;
	private String documentId;
	private String commentId;
	private String collectionId;
	
	public SubFilters() {
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
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
	public boolean isEmpty() {
		if(StringUtil.isEmpty(userId) && StringUtil.isEmpty(documentId) && StringUtil.isEmpty(commentId) && StringUtil.isEmpty(collectionId))
			return true;
		return false;
	}
}
