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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.connections.files.utils.ContentMapFiles;
import com.ibm.sbt.services.client.connections.files.utils.Messages;
import com.ibm.sbt.services.client.connections.files.utils.NamespacesConnections;

/**
 * @Represents Connections File
 * @author Vimal Dhupar
 */
public class FileEntry {
	private Document		data;											// Document object which stores
																			// the response feed obtained from
																			// Server.
	private String			fileId;										// this is the Document Id
	private String			creatorId;
	private String			label;
	private String			lock;
	private String			libraryType;
	private String			category;
	private String			totalResults;
	private CommentEntry	commentEntry;
	private PersonEntry		personEntry;
	private String			title;

	static final String		sourceClass	= FileEntry.class.getName();
	static final Logger		logger		= Logger.getLogger(sourceClass);

	public FileEntry() {
		commentEntry = new CommentEntry();
		personEntry = new PersonEntry();
	}

	public FileEntry(String fileId) {
		this.fileId = fileId;
		commentEntry = new CommentEntry();
		personEntry = new PersonEntry();
	}

	/**
	 * get
	 * 
	 * @param fieldName
	 * @return String
	 */
	private String get(String fieldName) {
		String xpQuery = getXPathQuery(fieldName);
		return getFieldUsingXPath(xpQuery);
	}

	/**
	 * getXPathQuery
	 * 
	 * @return xpath query for specified field. Field names follow IBM Connections naming convention
	 */
	private String getXPathQuery(String fieldName) {
		return ContentMapFiles.xpathMap.get(fieldName);
	}

	/**
	 * getFieldUsingXPath
	 * <p>
	 * Execute xpath query on Profile XML
	 * 
	 * @return String
	 */
	private String getFieldUsingXPath(String xpathQuery) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getFieldUsingXPath");
		}
		String result = null;
		try {
			result = DOMUtil.value(this.data, xpathQuery, NamespacesConnections.nameSpaceCtx);
		} catch (XMLException e) {
			logger.log(Level.SEVERE, Messages.FileError_1 + "getFieldUsingXPath");
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getFieldUsingXPath");
		}
		return result;
	}

	public String getFileId() {
		if (!StringUtil.isEmpty(fileId)) {
			return fileId;
		}
		return get("uuidFromEntry");
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getLabel() {
		if (!StringUtil.isEmpty(label)) {
			return label;
		}
		return get("labelFromEntry");
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLock() {
		if (!StringUtil.isEmpty(lock)) {
			return lock;
		}
		return get("lockFromEntry");
	}

	public void setLock(String lock) {
		this.lock = lock;
	}

	public String getTitle() {
		if (!StringUtil.isEmpty(title)) {
			return title;
		}
		return get("titleFromEntry");
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLibraryType() {
		if (!StringUtil.isEmpty(libraryType)) {
			return libraryType;
		}
		return get("libraryTypeFromEntry");
	}

	public void setLibraryType(String libraryType) {
		this.libraryType = libraryType;
	}

	public String getCreatorId() { // not in the response feed. so this is a dummy implementation.. check is
									// we need this !!???
		return creatorId;
	}

	public void setCreatorId(String creatorId) {// not in the response feed. so this is a dummy
												// implementation.. check is we need this !!???
		this.creatorId = creatorId;
	}

	public String getCategory() {
		if (!StringUtil.isEmpty(category)) {
			return category;
		}
		return get("categoryFromEntry");
	}

	public void setCategory(String category) {
		this.category = category;
	}

	private Object getData() {
		return data;
	}

	public void setData(Document data) {
		this.data = data;
	}

	public String getDownloadLink() {
		return get("downLinkFromEntry");
	}

	public CommentEntry getCommentEntry() {
		return commentEntry.getCommentEntry();
	}

	public void setCommentEntry(CommentEntry commentEntry) {
		this.commentEntry = commentEntry;
	}

	public PersonEntry getAuthorEntry() {
		return personEntry.getAuthorEntry();
	}

	private void setAuthorEntry(PersonEntry authorEntry) {
		this.personEntry = authorEntry;
	}

	public String getTotalResults() {
		if (!StringUtil.isEmpty(totalResults)) {
			return totalResults;
		}
		return get("totalResults");
	}

	private void setTotalResults(String totalResults) {
		this.totalResults = totalResults;
	}

	/**
	 * createResultFileWithData
	 * <p>
	 * method to create the FileEntry object for the response feed
	 * 
	 * @param result
	 * @return FileEntry
	 */
	public static Object createResultFileWithData(Document result, Class objectType) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "createResultFileWithData");
		}
		FileEntry file = new FileEntry();
		file.setData(result);
		if (file.get("categoryFromEntry").equals("comment")) {
			file.commentEntry.setData(result);
		}
		file.personEntry.setData(result);

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "createResultFileWithData");
		}

		if (objectType.equals(CommentEntry.class)) {
			return file.getCommentEntry();
		} else if (objectType.equals(PersonEntry.class)) {
			return file.getAuthorEntry();
		}
		return file;
	}

	// ONLY GETTERS FOR SOME OF THE FIELDS IN RESPONSE FEED
	public String getPublished() {
		return get("publishedFromEntry");
	}

	public String getUpdated() {
		return get("updatedFromEntry");
	}

	public String getCreated() {
		return get("createdFromEntry");
	}

	public String getModified() {
		return get("modifiedFromEntry");
	}

	public String getLastAccessed() {
		return get("lastAccessedFromEntry");
	}

	public PersonEntry getModifier() {
		return personEntry.getModifierEntry();
	}

	public String getVisibility() {
		return get("visibilityFromEntry");
	}

	public String getLibraryId() {
		return get("libraryIdFromEntry");
	}

	public String getVersionLabel() {
		return get("versionLabelFromEntry");
	}

	public String getPropogation() {
		return get("propagationFromEntry");
	}

	public String getTotalMediaSize() {
		return get("totalMediaSizeFromEntry");
	}

	public String getObjectTypeId() {
		return get("objectTypeIdFromEntry");
	}
}