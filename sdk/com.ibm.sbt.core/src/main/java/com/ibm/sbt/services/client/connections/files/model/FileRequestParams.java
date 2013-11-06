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
package com.ibm.sbt.services.client.connections.files.model;

/**
 * FileRequestParams represents the Parameters which can be passed while API execution.
 * 
 * @author Vimal Dhupar
 */
public enum FileRequestParams {

	REMOVETAG("removeTag"), DELETEFROM("deleteFrom"), ITEMID("itemId"), VISIBILITY("visibility"), IDENTIFIER(
			"identifier"), SHAREWITH("shareWith"), SHARESUMMARY("shareSummary"), CREATOR("creator"), PAGE(
			"page"), PS("ps"), SI("sI"), INCLUDEEXTENDEDATTRIBUTES("includeExtendedAttributes"), ACLS("acls"), DIRECTION(
			"direction"), SC("sC"), INCLUDEPATH("includePath"), INCLUDETAGS("includeTags"), SORTBY("sortBy"), SORTORDER(
			"sortOrder"), TAG("tag"), FILETYPE("fileType"), FORMAT("format"), INCLUDECOUNT("includeCount"), ACCESS(
			"access"), SHARED("shared"), SHAREDWITHME("sharedWithMe"), ADDED("added"), ADDEDBY("addedBy"), INCLUDEQUOTA(
			"includeQuota"), SHAREPERMISSION("sharePermission"), SHAREDBY("sharedBy"), SHAREDWITH(
			"sharedWith"), SEARCH("search"), CATEGORY("category"), INLINE("inline"), INCLUDELIBRARYINFO(
			"includeLibraryInfo"), INCLUDENOTIFICATION("includeNotification"), RECOMMENDATION(
			"recommendation"), VERSIONUUID("versionUuid"), RESTRICTEDVISIBILITY("restrictedVisibility"), LOCK(
			"type"), LIBRARYTYPE("libraryType"), EMAIL("email"), USERSTATE("userState"), UNDELETE("undelete"), SHAREDWHAT(
			"sharedWhat");

	String	fileRequestParams;

	private FileRequestParams(String params) {
		this.fileRequestParams = params;
	}

	public String getFileRequestParams() {
		return fileRequestParams;
	}
}
