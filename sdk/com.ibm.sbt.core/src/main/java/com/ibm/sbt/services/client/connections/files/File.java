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

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Node;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.service.basic.ConnectionsFileProxyService;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.files.model.Author;
import com.ibm.sbt.services.client.connections.files.model.FileEntryXPath;
import com.ibm.sbt.services.client.connections.files.model.FileRequestPayload;
import com.ibm.sbt.services.client.connections.files.model.Modifier;
import com.ibm.sbt.services.client.connections.files.model.Person;

/**
 * @Represents Connections File
 * @author Vimal Dhupar
 */
public class File extends AtomEntity {
	private Author			authorEntry;
	private Modifier		modifierEntry;

	public File() {
	}

	/**
	 * Constructor
	 * @param fileId
	 */
	public File(String fileId) {
		setAsString(AtomXPath.id, fileId);
	}

    /**
     * Constructor
     * @param svc
     * @param dh
     */
    public File(FileService svc, XmlDataHandler dh) {
        super(svc, dh);
    }
    
	/**
	 * Method to get the FileId of the File
	 * @return String 
	 */
	public String getFileId() {
		String id = super.getId();
		// here we extract the id value from the string, by truncating the prefix.
		if(StringUtil.isNotEmpty(id)) {
			int startOfId = id.lastIndexOf(":");
			if(startOfId == -1)
				return id;
			return id.substring(startOfId+1);
		} else {
			return id;
		}
	}

	/**
	 * Method to get the Label of the File
	 * @return String
	 */
	public String getLabel() {
		return getAsString(FileEntryXPath.Label);
	}

	/**
	 * Method to get the lock type of the File
	 * @return String
	 */
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

	/**
	 * Method to get the Library Type of the File
	 * @return String
	 */
	public String getLibraryType() {
		return getAsString(FileEntryXPath.LibraryType);
	}

	/**
	 * Method to get the category of the File
	 * @return String
	 */
	public String getCategory() {
		return getAsString(FileEntryXPath.Category);
	}

	/**
	 * Method to get the download Url of the File. This Url can be used to download the file.
	 * @return String
	 */
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

	/**
	 * Method to get the Author ojbect for the File
	 * @see Author.java 
	 * @return Author
	 */
	public Author getAuthor() {
		if(null == authorEntry) {
			 authorEntry = new Author(getService(), new XmlDataHandler((Node)this.getDataHandler().getData(), 
		        		ConnectionsConstants.nameSpaceCtx, (XPathExpression)AtomXPath.author.getPath()));
		}
		return authorEntry;
	}

	/**
	 * Method to get the total results returned
	 * @return String
	 */
	public String getTotalResults() {
		return getAsString(FileEntryXPath.TotalResults);
	}

	/**
	 * Method to get the Created Date
	 * @return Date
	 */
	public Date getCreated() {
		return getAsDate(FileEntryXPath.Created);
	}

	/**
	 * Method to get the Modified Date
	 * @return Date
	 */
	public Date getModified() {
		return getAsDate(FileEntryXPath.Modified);
	}

	/**
	 * Method to get the LastAccessed information of the File
	 * @return Date
	 */
	public Date getLastAccessed() {
		return getAsDate(FileEntryXPath.LastAccessed);
	}

	/**
	 * Method to return the Modifier details
	 * @see Person.java
	 * @return Person
	 */
	public Person getModifier() {
		if(null == modifierEntry) {
			modifierEntry = new Modifier(getService(), new XmlDataHandler((Node)this.getDataHandler().getData(), 
	        		ConnectionsConstants.nameSpaceCtx, (XPathExpression)AtomXPath.modifier.getPath()));
		}
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

	/**
	 * Method to return the Library Id of the File
	 * @return String
	 */
	public String getLibraryId() {
		return getAsString(FileEntryXPath.LibraryId);
	}

	/**
	 * Method to get the version label
	 * @return String
	 */
	public String getVersionLabel() {
		return getAsString(FileEntryXPath.VersionLabel);
	}

	/**
	 * Method to get the propogation
	 * @return String
	 */
	public String getPropogation() {
		return getAsString(FileEntryXPath.Propagation);
	}

	/**
	 * getSize
	 * @deprecated Use getTotalMediaSize instead
	 */
	public Long getSize() {
		return getTotalMediaSize();
	}

	/**
	 * getTotalMediaSize
	 * <p>
	 * Return the total size of the file/media in bytes
	 * @return Integer size
	 */
	public Long getTotalMediaSize() {
		return getAsLong(FileEntryXPath.TotalMediaSize);
	}

	/**
	 * getEnclosureLength
	 * <p>
	 * Return the size of the enclosure in bytes
	 * @return Integer size
	 */
	public Long getEnclosureLength() {
		return getAsLong(FileEntryXPath.EnclosureLength);
	}

	/**
	 * Method to get the Object Type Id 
	 * @return String
	 */
	public String getObjectTypeId() {
		return getAsString(FileEntryXPath.ObjectTypeId);
	}
	
	/**
	 * Method to get Type
	 * @return String
	 */
	public String getType() {
		return this.getAsString(FileEntryXPath.Type);
	}
	
	/**
	 * Method to get Edit Link
	 * @return String
	 */
	public String getEditLink() {
		return this.getAsString(FileEntryXPath.EditLink);
	}
	
	/**
	 * Method to get Edit Media Link
	 * @return String
	 */
	public String getEditMediaLink() {
		return this.getAsString(FileEntryXPath.EditMediaLink);
	}
	
	/**
	 * Method to get Edit Media Url
	 * @return String
	 */
	public String getEditMediaUrl() {
		return this.getAsString(FileEntryXPath.EditMediaLink);
	}
	
	/**
	 * Method to get Thumbnail Url
	 * @return String
	 */
	public String getThumbnailUrl() {
		return this.getAsString(FileEntryXPath.ThumbnailUrl);
	}
	
	/**
	 * Method to get Comments Url
	 * @return String
	 */
	public String getCommentsUrl() {
		return this.getAsString(FileEntryXPath.CommentsUrl);
	}
	
	/**
	 * Method to get Version Uuid
	 * @return String
	 */
	public String getVersionUuid() {
		return this.getAsString(FileEntryXPath.VersionUuid);
	}
	
	/**
	 * Method to get Recommendations Count
	 * @return int
	 */
	public int getRecommendationsCount() {
		return this.getAsInt(FileEntryXPath.RecommendationsCount);
	}
	
	/**
	 * Method to get Comments Count
	 * @return int
	 */
	public int getCommentsCount() {
		return this.getAsInt(FileEntryXPath.CommentsCount);
	}

	/**
	 * Method to get Shares Count
	 * @return int
	 */
	public int getSharesCount() {
		return this.getAsInt(FileEntryXPath.SharesCount);
	}

	/**
	 * Method to get Folders Count
	 * @return int
	 */
	public int getFoldersCount() {
		return this.getAsInt(FileEntryXPath.FoldersCount);
	}

	/**
	 * Method to get Attachments Count
	 * @return int
	 */
	public int getAttachmentsCount() {
		return this.getAsInt(FileEntryXPath.AttachmentsCount);
	}

	/**
	 * Method to get Versions Count
	 * @return int
	 */
	public int getVersionsCount() {
		return this.getAsInt(FileEntryXPath.VersionsCount);
	}

	/**
	 * Method to get References Count
	 * @return int
	 */
	public int getReferencesCount() {
		return this.getAsInt(FileEntryXPath.ReferencesCount);
	}
	
	/**
	 * Method to get Content Url
	 * @return String
	 */
	public String getContentUrl() {
		return this.getAsString(FileEntryXPath.ContentUrl);
	}
	
	/**
	 * Method to get Content Type
	 * @return String
	 */
	public String getContentType() {
		return this.getAsString(FileEntryXPath.ContentType);
	}
	
	/**
	 * Method to get Acls
	 * @return String
	 */
	public String getAcls() {
		return this.getAsString(FileEntryXPath.Acls);
	}
	
	/**
	 * Method to get Hit Count
	 * @return int
	 */
	public int getHitCount() {
		return this.getAsInt(FileEntryXPath.HitCount);
	}
	
	/**
	 * Method to get Anonymous Hit Count
	 * @return int
	 */
	public int  getAnonymousHitCount() {
		return this.getAsInt(FileEntryXPath.AnonymousHitCount);
	}
	
	/**
	 * Method to get Tags
	 * @return String
	 */
	public List<String> getTags() {
		return this.getAsList(FileEntryXPath.Tags);
	}
	
	/**
	 * Method to load the File Object
	 * @return File
	 * @throws FileServiceException
	 */
	public File load() throws FileServiceException {
		return getService().getFile(getFileId());
    }
	
	/**
	 * Method to add comment to a File 
	 * @param comment
	 * @param params
	 * @return Comment
	 * @throws FileServiceException
	 * @throws TransformerException
	 */
	public Comment addComment(String comment, Map<String, String> params) throws FileServiceException, TransformerException {
		return this.getService().addCommentToFile(this.getFileId(), comment, this.getAuthor().getId(), params);
    }
	
	/**
	 * Method to Pin a File
	 * @throws FileServiceException
	 */
	public void pin() throws FileServiceException {
		this.getService().pinFile(this.getFileId());
    }
	
	/**
	 * Method to Un Pin a File
	 * @throws FileServiceException
	 */
	public void unpin() throws FileServiceException {
		this.getService().unPinFile(this.getFileId());
    }
	
	/**
	 * Method to Lock a File
	 * @throws FileServiceException
	 */
	public void lock() throws FileServiceException {
		this.getService().lock(this.getFileId());
    }
	
	/**
	 * Method to un Lock a File
	 * @throws FileServiceException
	 */
	public void unlock() throws FileServiceException {
		this.getService().unlock(this.getFileId());
    }
	
	/**
	 * Method to remove/delete a File
	 * @throws FileServiceException
	 */
	public void remove() throws FileServiceException {
		this.getService().deleteFile(this.getFileId());
    }
	
	/**
	 * Method to update a File
	 * @param params
	 * @throws FileServiceException
	 * @throws TransformerException
	 */
	public void update(Map<String, String> params) throws FileServiceException, TransformerException {
		this.getService().updateFileMetadata(this, params);
    }
	
	/**
	 * Method to save a File 
	 * @param params
	 * @throws FileServiceException
	 * @throws TransformerException
	 */
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
	
	/**
	 * Method to set the label on a File
	 * @param label
	 */
	public void setLabel(String label) {
		fields.put(FileRequestPayload.LABEL.getFileRequestPayload(), label);
	}
	
	/**
	 * Method to set Visibility of a File
	 * @param visibility
	 */
	public void setVisibility(String visibility) {
		fields.put(FileRequestPayload.VISIBILITY.getFileRequestPayload(), visibility);
	}
	
	/**
	 * Method to set Summary of a File
	 * @param summary
	 */
	public void setSummary(String summary) {
		fields.put(FileRequestPayload.SUMMARY.getFileRequestPayload(), summary);
	}
	
}