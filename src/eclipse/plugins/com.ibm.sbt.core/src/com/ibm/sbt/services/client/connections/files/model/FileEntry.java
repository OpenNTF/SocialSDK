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

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.files.FileEntryXPath;
import com.ibm.sbt.services.client.connections.files.FileService;

/**
 * @Represents Connections File
 * @author Vimal Dhupar
 */
public class FileEntry extends BaseEntity {
	private String			fileId;										// this is the Document Id
	private String			creatorId;
	private String			label;
	private String			lock;
	private String			libraryType;
	private String			category;
	private String			totalResults;
	private CommentEntry	commentEntry;
	private AuthorEntry		authorEntry;
	private ModifierEntry	modifierEntry;
	private String			title;

	public FileEntry() {
		this(null,null);
	}

	public FileEntry(String fileId) {
		this.fileId = fileId;
		commentEntry = new CommentEntry();
		//personEntry = new PersonEntry();
	}

    public FileEntry(FileService svc, DataHandler<?> dh) {
        super(svc, dh);
        commentEntry = new CommentEntry();
        authorEntry = new AuthorEntry(getService(), this.dataHandler);
        modifierEntry = new ModifierEntry(getService(), this.dataHandler);
    }
    
	public String getFileId() {
		if (!StringUtil.isEmpty(fileId)) {
			return fileId;
		}
		return getAsString(FileEntryXPath.UuidFromEntry);
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getLabel() {
		if (!StringUtil.isEmpty(label)) {
			return label;
		}
		return getAsString(FileEntryXPath.LabelFromEntry);
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLock() {
		if (!StringUtil.isEmpty(lock)) {
			return lock;
		}
		return getAsString(FileEntryXPath.LockFromEntry);
	}

	public void setLock(String lock) {
		this.lock = lock;
	}

	public String getTitle() {
		if (!StringUtil.isEmpty(title)) {
			return title;
		}
		return getAsString(FileEntryXPath.TitleFromEntry);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLibraryType() {
		if (!StringUtil.isEmpty(libraryType)) {
			return libraryType;
		}
		return getAsString(FileEntryXPath.LibraryTypeFromEntry);
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
		return getAsString(FileEntryXPath.CategoryFromEntry);
	}

	public void setCategory(String category) {
		this.category = category;
	}

	private Node getData() {
		return (Node)dataHandler.getData();
	}

	public void setData(Document data) {
		dataHandler.setData(data);
	}

	public String getDownloadLink() {
		return getAsString(FileEntryXPath.DownLinkFromEntry);
	}

	public CommentEntry getCommentEntry() {
		return commentEntry.getCommentEntry();
	}

	public void setCommentEntry(CommentEntry commentEntry) {
		this.commentEntry = commentEntry;
	}

	public AuthorEntry getAuthorEntry() {
		return authorEntry;
	}

	private void setAuthorEntry(AuthorEntry authorEntry) {
		this.authorEntry = authorEntry;
	}

	public String getTotalResults() {
		if (!StringUtil.isEmpty(totalResults)) {
			return totalResults;
		}
		return getAsString(FileEntryXPath.TotalResults);
	}

	private void setTotalResults(String totalResults) {
		this.totalResults = totalResults;
	}

	/**
	 * createResultFileWithData
	 * <p>
	 * method to create the FileEntry object for the response feed
	 * requires a document having a entry as the root node
	 * @param result
	 * @return FileEntry
	 */
	public static Object createResultFileWithData(Document result, Class objectType) {
		FileEntry file = new FileEntry();
		file.setData(result);
		if (file.getAsString(FileEntryXPath.CategoryFromEntry).equals("comment")) {
			file.commentEntry.setData(result);
		}
		file.getAuthorEntry().setData(result);

		if (objectType == null) {
			return file.getData();
		} else if (objectType.equals(CommentEntry.class)) {
			return file.getCommentEntry();
		} else if (objectType.equals(PersonEntry.class)) {
			return file.getAuthorEntry();
		}
		return file;
	}

	// ONLY GETTERS FOR SOME OF THE FIELDS IN RESPONSE FEED
	public String getPublished() {
		return getAsString(FileEntryXPath.PublishedFromEntry);
	}

	public String getUpdated() {
		return getAsString(FileEntryXPath.UpdatedFromEntry);
	}

	public String getCreated() {
		return getAsString(FileEntryXPath.CreatedFromEntry);
	}

	public String getModified() {
		return getAsString(FileEntryXPath.ModifiedFromEntry);
	}

	public String getLastAccessed() {
		return getAsString(FileEntryXPath.LastAccessedFromEntry);
	}

	public PersonEntry getModifier() {
		return modifierEntry;
	}

	public String getVisibility() {
		return getAsString(FileEntryXPath.VisibilityFromEntry
);
	}

	public String getLibraryId() {
		return getAsString(FileEntryXPath.LibraryIdFromEntry);
	}

	public String getVersionLabel() {
		return getAsString(FileEntryXPath.VersionLabelFromEntry);
	}

	public String getPropogation() {
		return getAsString(FileEntryXPath.PropagationFromEntry);
	}

	public String getTotalMediaSize() {
		return getAsString(FileEntryXPath.TotalMediaSizeFromEntry);
	}

	public String getObjectTypeId() {
		return getAsString(FileEntryXPath.ObjectTypeIdFromEntry);
	}
}