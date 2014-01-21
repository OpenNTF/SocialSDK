/*
 *  Copyright IBM Corp. 2013
 * 
 * Licensed under the Apache License Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing software 
 * distributed under the License is distributed on an "AS IS" BASIS 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.sbt.services.client.connections.cmisfiles;

import java.util.Date;

import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.cmisfiles.model.CMISFileXPath;
import com.ibm.sbt.services.client.connections.cmisfiles.model.CMISModifier;
import com.ibm.sbt.services.client.connections.cmisfiles.model.CMISAuthor;

/**
 * @Represents Connections CMIS File
 * @author Vimal Dhupar
 */
public class CMISFile extends AtomEntity {

	private CMISModifier modifierEntry;
	private CMISAuthor authorEntry;
	
	public CMISFile() {
		this(null,null);
	}

	/**
	 * Constructor
	 * @param fileId
	 */
	public CMISFile(String fileId) {
		this(null,null);
		setAsString(AtomXPath.id, fileId);
	}

    /**
     * Constructor
     * @param svc
     * @param dh
     */
    public CMISFile(CMISFileService svc, XmlDataHandler dh) {
        super(svc, dh);
    }
    
	/**
	 * Method to get the FileId of the File
	 * @return String 
	 */
	public String getId() {
		String id = super.getId();
		// here we extract the id value from the string by truncating the prefix.
		if(StringUtil.isNotEmpty(id)) {
			int startOfId = id.lastIndexOf("!");
			if(startOfId == -1)
				return id;
			return id.substring(startOfId+1);
		} else {
			return id;
		}
	}
	
	public String getEditMediaUrl() {
		return this.getAsString(CMISFileXPath.editMediaUrl);
	}
	
	public String getDownloadUrl() {
		return this.getAsString(CMISFileXPath.downloadUrl);
	}
	
	public String getServiceDocUrl() {
		return this.getAsString(CMISFileXPath.serviceDocUrl);
	}
	
	public String getAllowableActionsURL() {
		return this.getAsString(CMISFileXPath.allowableActionsURL);
	}
	
	public String getDescribedByUrl() {
		return this.getAsString(CMISFileXPath.describedByUrl);
	}
	
	public Date getEdited() {
		return this.getAsDate(CMISFileXPath.edited);
	}
	
	public String getRelationshipsUrl() {
		return this.getAsString(CMISFileXPath.relationshipsUrl);
	}
	
	public String getAclUrl() {
		return this.getAsString(CMISFileXPath.aclUrl);
	}
	
	public String getAclHistoryUrl() {
		return this.getAsString(CMISFileXPath.aclHistoryUrl);
	}
	
	public String getAclRemoverUrl() {
		return this.getAsString(CMISFileXPath.aclRemoverUrl);
	}
	
	public String getPoliciesUrl() {
		return this.getAsString(CMISFileXPath.policiesUrl);
	}
	
	public String getVersionHistoryUrl() {
		return this.getAsString(CMISFileXPath.versionHistoryUrl);
	}
	
	public String getDownloadHistoryUrl() {
		return this.getAsString(CMISFileXPath.downloadHistoryUrl);
	}
	
	public String getRecommendationsUrl() {
		return this.getAsString(CMISFileXPath.recommendationsUrl);
	}
	
	public String getRecommendationUrl() {
		return this.getAsString(CMISFileXPath.recommendationUrl);
	}
	
	public String getSharedUrl() {
		return this.getAsString(CMISFileXPath.sharedUrl);
	}
	
	public String getPathSegment() {
		return this.getAsString(CMISFileXPath.pathSegment);
	}
	
	public String getCmisName() {
		return this.getAsString(CMISFileXPath.cmisName);
	}
	
	public String getCmisObjectId() {
		return this.getAsString(CMISFileXPath.cmisObjectId);
	}
	
	public String getCmisBaseTypeId() {
		return this.getAsString(CMISFileXPath.cmisBaseTypeId);
	}
	
	public String getCmisObjectTypeId() {
		return this.getAsString(CMISFileXPath.cmisObjectTypeId);
	}
	
	public CMISModifier getModifier() {
		if(null == modifierEntry) {
			modifierEntry = new CMISModifier(getService(), new XmlDataHandler((Node)this.getDataHandler().getData(), 
		        		ConnectionsConstants.nameSpaceCtx, (XPathExpression)CMISFileXPath.modifier.getPath()));
		}
		return modifierEntry;
    }
	
	public CMISAuthor getAuthor() {
		if(null == authorEntry) {
			authorEntry = new CMISAuthor(getService(), new XmlDataHandler((Node)this.getDataHandler().getData(), 
		        		ConnectionsConstants.nameSpaceCtx, (XPathExpression)AtomXPath.author.getPath()));
		}
		return authorEntry;
    }
    
	public Date getModificationDate() {
		return this.getAsDate(CMISFileXPath.modificationDate);
	}
	
	public String getChangeToken() {
		return this.getAsString(CMISFileXPath.changeToken);
	}
	
	public boolean isImmutable() {
		return this.getAsBoolean(CMISFileXPath.isImmutable);
	}
	
	public boolean isLatestVersion() {
		return this.getAsBoolean(CMISFileXPath.isLatestVersion);
	}
	
	public boolean isLatestMajorVersion() {
		return this.getAsBoolean(CMISFileXPath.isLatestMajorVersion);
	}
	
	public String getVersionLabel() {
		return this.getAsString(CMISFileXPath.versionLabel);
	}
	
	public String getVersionSeriesId() {
		return this.getAsString(CMISFileXPath.versionSeriesId);
	}
	
	public String getVersionSeriesCheckedOutBy() {
		return this.getAsString(CMISFileXPath.versionSeriesCheckedOutBy);
	}
	
	public String getCheckinComment() {
		return this.getAsString(CMISFileXPath.checkinComment);
	}
	
	public int getContentStreamLength() {
		return this.getAsInt(CMISFileXPath.contentStreamLength);
	}
	
	public String getContentStreamMimeType() {
		return this.getAsString(CMISFileXPath.contentStreamMimeType);
	}
	
	public String getContentStreamFileName() {
		return this.getAsString(CMISFileXPath.contentStreamFileName);
	}
	
	public String getContentStreamId() {
		return this.getAsString(CMISFileXPath.contentStreamId);
	}
	
	public String getRepositoryId() {
		return this.getAsString(CMISFileXPath.repositoryId);
	}
	
	public int getCommentCount() {
		return this.getAsInt(CMISFileXPath.commentCount);
	}
	
	public int getDownloadCount() {
		return this.getAsInt(CMISFileXPath.downloadCount);
	}
	
	public int getDownloadCountAnon() {
		return this.getAsInt(CMISFileXPath.downloadCountAnon);
	}
	
	public int getSizeAppliedToQuota() {
		return this.getAsInt(CMISFileXPath.sizeAppliedToQuota);
	}
	
	public String getLanguage() {
		return this.getAsString(CMISFileXPath.language);
	}
	
	public String getSummary() {
		return this.getAsString(CMISFileXPath.summary);
	}
	
	public String getContentStreamFileExt() {
		return this.getAsString(CMISFileXPath.contentStreamFileExt);
	}
	
	public boolean isPublic() {
		return this.getAsBoolean(CMISFileXPath.isPublic);
	}
	
	public String getVisibility() {
		return this.getAsString(CMISFileXPath.visibility);
	}
	
	public boolean isSharedViral() {
		return this.getAsBoolean(CMISFileXPath.isSharedViral);
	}
	
	public String getRepositoryType() {
		return this.getAsString(CMISFileXPath.repositoryType);
	}
	
	public int getRecommendationCount() {
		return getAsInt(CMISFileXPath.recommendationCount);
	}
	
	public boolean isRecommendedByCaller() {
		return this.getAsBoolean(CMISFileXPath.isRecommendedByCaller);
	}
	
	public Date getContentStreamLastModified() {
		return this.getAsDate(CMISFileXPath.contentStreamLastModified);
	}
	
	public String getLockType() {
		return this.getAsString(CMISFileXPath.lockType);
	}
	
	public String getLockedBy() {
		return this.getAsString(CMISFileXPath.lockedBy);
	}
	
	public Date getLockedWhen() {
		return this.getAsDate(CMISFileXPath.lockedWhen);
	}
	
	public String getSharePermission() {
		return this.getAsString(CMISFileXPath.sharePermission);
	}
}