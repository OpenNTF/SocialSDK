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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.service.basic.ConnectionsFileProxyService;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.files.model.Author;
import com.ibm.sbt.services.client.connections.files.model.Modifier;
import com.ibm.sbt.services.client.connections.files.model.Person;
import com.ibm.sbt.services.client.connections.files.model.FileEntryXPath;
import com.ibm.sbt.services.client.connections.files.model.FileRequestPayload;

/**
 * @Represents Connections File
 * @author Vimal Dhupar
 */
public class File extends BaseEntity {
	private String			fileId;	
	private Author			authorEntry;
	private Modifier		modifierEntry;

	public File() {
		this(null,null);
	}

	public File(String fileId) {
		this.fileId = fileId;
	}

    public File(FileService svc, DataHandler<?> dh) {
        super(svc, dh);
        authorEntry = new Author(getService(), this.dataHandler);
        modifierEntry = new Modifier(getService(), this.dataHandler);
    }
    
	public String getFileId() {
		if (!StringUtil.isEmpty(fileId)) {
			return fileId;
		}
		return getAsString(FileEntryXPath.Uuid);
	}

	public String getLabel() {
		return getAsString(FileEntryXPath.Label);
	}

	public String getLockType() {
		return getAsString(FileEntryXPath.Lock);
	}

	/**
	 * isLocked
	 * <p>
	 * method to determine whether a file is locked. returns true if the file is locked, false otherwise.
	 * @return boolean 
	 * 
	 */
	public boolean isLocked() {
		if(FileConstants.LockType_HARD.equalsIgnoreCase(getLockType()))
			return true;
		else 
			return false;
	}
	
	public String getTitle() {
		return getAsString(FileEntryXPath.Title);
	}

	public String getLibraryType() {
		return getAsString(FileEntryXPath.LibraryType);
	}

	public String getCategory() {
		return getAsString(FileEntryXPath.Category);
	}

	public String getDownloadUrl() {
		String proxypath = this.getService().getEndpoint().getProxyPath("connections");
		String fileId = this.getFileId();
		String libId = this.getLibraryId();
		HttpServletRequest req = Context.get().getHttpRequest();
		String sbtServiceUrl = UrlUtil.getContextUrl(req);
		String url =  sbtServiceUrl + "/service" +  FileServiceURIBuilder.FILES.getBaseUrl() + 
						FileConstants.SEPARATOR + proxypath + "/" + ConnectionsFileProxyService.FILEPROXYNAME + "/" + "DownloadFile" + "/" + fileId + "/" + libId;
		return url;
	}

	public Author getAuthor() {
		return authorEntry;
	}

	public String getTotalResults() {
		return getAsString(FileEntryXPath.TotalResults);
	}

	public String getPublished() {
		return getAsString(FileEntryXPath.Published);
	}

	public String getUpdated() {
		return getAsString(FileEntryXPath.Updated);
	}

	public String getCreated() {
		return getAsString(FileEntryXPath.Created);
	}

	public String getModified() {
		return getAsString(FileEntryXPath.Modified);
	}

	public String getLastAccessed() {
		return getAsString(FileEntryXPath.LastAccessed);
	}

	public Person getModifier() {
		return modifierEntry;
	}

	/**
	 * getVisibility
	 * <p>
	 * returns the visibility status of the file, whether private, public or shared?
	 * 
	 * @return
	 */
	public String getVisibility() {
		return getAsString(FileEntryXPath.Visibility);
	}

	public String getLibraryId() {
		return getAsString(FileEntryXPath.LibraryId);
	}

	public String getVersionLabel() {
		return getAsString(FileEntryXPath.VersionLabel);
	}

	public String getPropogation() {
		return getAsString(FileEntryXPath.Propagation);
	}

	/**
	 * getSize
	 * <p>
	 * return the total size of the file/media in Bytes
	 * @return Integer size
	 */
	public Long getSize() {
		return getAsLong(FileEntryXPath.TotalMediaSize);
	}

	public String getObjectTypeId() {
		return getAsString(FileEntryXPath.ObjectTypeId);
	}
	
	public String getSelfUrl() {
		return this.getAsString(FileEntryXPath.SelfUrl);
	}
	
	public String getType() {
		return this.getAsString(FileEntryXPath.Type);
	}
	
	public String getAlternateUrl() {
		return this.getAsString(FileEntryXPath.AlternateUrl);
	}
	
	public String getEditLink() {
		return this.getAsString(FileEntryXPath.EditLink);
	}
	
	public String getEditMediaLink() {
		return this.getAsString(FileEntryXPath.EditMediaLink);
	}
	
	public String getThumbnailUrl() {
		return this.getAsString(FileEntryXPath.ThumbnailUrl);
	}
	
	public String getCommentsUrl() {
		return this.getAsString(FileEntryXPath.CommentsUrl);
	}
	
	public String getVersionUuid() {
		return this.getAsString(FileEntryXPath.VersionUuid);
	}
	
	public int getRecommendationsCount() {
		return this.getAsInt(FileEntryXPath.RecommendationsCount);
	}
	
	public int getCommentsCount() {
		return this.getAsInt(FileEntryXPath.CommentsCount);
	}

	public int getSharesCount() {
		return this.getAsInt(FileEntryXPath.SharesCount);
	}

	public int getFoldersCount() {
		return this.getAsInt(FileEntryXPath.FoldersCount);
	}

	public int getAttachmentsCount() {
		return this.getAsInt(FileEntryXPath.AttachmentsCount);
	}

	public int getVersionsCount() {
		return this.getAsInt(FileEntryXPath.VersionsCount);
	}

	public int getReferencesCount() {
		return this.getAsInt(FileEntryXPath.ReferencesCount);
	}

	public String 	getSummary() {
		return this.getAsString(FileEntryXPath.Summary);
	}
	public String getContentUrl() {
		return this.getAsString(FileEntryXPath.ContentUrl);
	}
	
	public String getContentType() {
		return this.getAsString(FileEntryXPath.ContentType);
	}
	
	public String getAcls() {
		return this.getAsString(FileEntryXPath.Acls);
	}
	
	public int getHitCount() {
		return this.getAsInt(FileEntryXPath.HitCount);
	}
	
	public int  getAnonymousHitCount() {
		return this.getAsInt(FileEntryXPath.AnonymousHitCount);
	}
	
	public String getTags() {
		return this.getAsString(FileEntryXPath.Tags);
	}
	
	public File load() throws FileServiceException {
		return getService().getFile(getFileId());
    }

	public Comment addComment(String comment, Map<String, String> params) throws FileServiceException, TransformerException {
		return this.getService().addCommentToFile(this.getFileId(), comment, this.getAuthor().getUserUuid(), params);
    }
	
	public void pin() throws FileServiceException {
		this.getService().pinFile(this.getFileId());
    }
	
	public void unpin() throws FileServiceException {
		this.getService().unPinFile(this.getFileId());
    }
	
	public void lock() throws FileServiceException {
		this.getService().lock(this.getFileId());
    }
	
	public void unlock() throws FileServiceException {
		this.getService().unlock(this.getFileId());
    }
	
	public void remove() throws FileServiceException {
		this.getService().deleteFile(this.getFileId());
    }
	
	public void update(Map<String, String> params) throws FileServiceException, TransformerException {
		this.getService().updateFileMetadata(this, params);
    }
	
	public void save(Map<String, String> params) throws FileServiceException, TransformerException {
		//TODO
		this.getService().updateFileMetadata(this, params);
    }
	
	@Override
	public FileService getService(){
		return (FileService)super.getService();
	}
	
	@Override
	public XmlDataHandler getDataHandler(){
		return (XmlDataHandler)super.getDataHandler();
	}	
	
	public void setLabel(String label) {
		fields.put(FileRequestPayload.LABEL.getFileRequestPayload(), label);
	}
	
	public void setVisibility(String visibility) {
		fields.put(FileRequestPayload.VISIBILITY.getFileRequestPayload(), visibility);
	}
	public void setSummary(String summary) {
		fields.put(FileRequestPayload.SUMMARY.getFileRequestPayload(), summary);
	}
	
}