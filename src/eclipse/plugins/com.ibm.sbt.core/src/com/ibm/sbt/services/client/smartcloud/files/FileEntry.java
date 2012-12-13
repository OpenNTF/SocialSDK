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
package com.ibm.sbt.services.client.smartcloud.files;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.SBTServiceException;
import com.ibm.sbt.services.client.smartcloud.base.BaseEntity;

/**
 * base class to represent the file entries within the SmartCloud API each object represent a reference to an
 * entry and hold all the links for the user and for subsequent API calls.
 * 
 * @author Lorenzo Boccaccia
 * @date Dec 6, 2012
 * @param <DataFormat>
 */
public class FileEntry<DataFormat> extends BaseEntity<DataFormat> implements Serializable {
	// TODO: check for the Z and T
	private final static DateFormat	format				= new SimpleDateFormat("yyyy-MM-DD'T'HH:MM:SS.S'Z'");

	private static final long		serialVersionUID	= 7827896200011334849L;

	protected FileService			svc;

	/**
	 * standard constructor is private as entries are only created after an upload
	 */
	protected FileEntry() {
		super();
	}

	/**
	 * constructor that creates a file entry from its object id
	 * 
	 * @param uuid
	 *            the entry identifier
	 * @param svc
	 *            Service used for operations over the entry
	 * @throws SBTServiceException
	 */
	public FileEntry(String uuid, FileService svc) throws SBTServiceException {
		super(uuid, svc);
		this.svc = svc;
		initParams();
	}

	/**
	 * constructor that creates a file entry using the provided data
	 * 
	 * @param data
	 *            Data from one of the suported formats, @see DataNavigatorFactory
	 * @param svc
	 *            Service used for operations over the entry
	 * @throws SBTServiceException
	 */
	public FileEntry(DataFormat data, FileService svc) throws SBTServiceException {
		super(data, svc);
		initParams();
	}

	private void initParams() throws SBTServiceException {
		if (svc != null) {
			this.selfLoadUrl = svc.getEntryLoadURL();
		}
		this.nameParameterId = null;
	}

	@Override
	protected String getSelfLoadURL() {
		return selfLoadUrl.replace("{fileId}", uuid);
	}

	public String getVisibility() {
		// TODO Auto-generated method stub
		return get("fileEntryVisibility");
	}

	public Boolean isExternal() {
		// TODO Auto-generated method stub
		return Boolean.valueOf(get("fileEntryIsExternal"));
	}

	public String getFileId() {
		// TODO Auto-generated method stub
		return get("fileEntryId");
	}

	public String getContentLink() {
		// TODO Auto-generated method stub
		return get("contentLink");
	}

	public String getEditMediaLink() {
		// TODO Auto-generated method stub
		return get("editMediaLink");
	}

	public Date getLastStreamModifiedDate() {
		// TODO Auto-generated method stub
		return getDate("fileEntryStreamLastModified");
	}

	public UserProfile getCreatedBy() {
		return extractProfileFromKey("fileEntryCreatedBy");
	}

	public UserProfile getCheckedOutBy() {
		return extractProfileFromKey("fileEntryCheckedOutBy");
	}

	public String getEntityURL() {
		return get("selfURL");
	}

	public Boolean isEncrypted() {
		return Boolean.valueOf(get("fileEntryIsEncrypted"));
	}

	public String getAuthorOrganizationName() {
		return get("authorOrgName");
	}

	public String getACLUrl() {
		return get("aclURL");
	}

	public String getUserState() {
		return get("userState");
	}

	public String getObjectTypeId() {
		return get("fileEntryObjectTypeId");
	}

	public Date getLastEditDate() {
		return getDate("lastEditDate");
	}

	private Date getDate(String d) {
		String value = get(d);
		if (StringUtil.isEmpty(value)) {
			return null;
		}
		try {
			return format.parse(value);
		} catch (ParseException e) {
			throw new RuntimeException("Unable to parse date " + get("lastEditDate"), e);
		}
	}

	public UserProfile getLockedBy() {
		return extractProfileFromKey("fileEntryLockedBy");
	}

	public UserProfile getModifiedBy() {
		return extractProfileFromKey("fileEntryModifiedBy");
	}

	// TODO: move to superclass?
	protected UserProfile extractProfileFromKey(String entry) {
		Collection<DataFormat> list = getEntities(entry);
		if (list.isEmpty()) {
			return null;
		}
		// TODO: proper error handling
		if (list.size() > 1) {
			throw new RuntimeException();
		}
		UserProfile profile = new UserProfile(list.iterator().next());
		if (profile.isEmpty()) {
			return null;
		}
		return profile;
	}

	public Integer getDownloadsAnonCount() {
		return Integer.valueOf(get("fileEntryDownloadCountAnon"));
	}

	public Date getLastEntryModifiedDate() {
		return getDate("fileEntryLastModificationDate");
	}

	public String getDescrptionLink() {
		return get("descriptionLink");
	}

	public String getRepositoryTypeId() {
		return get("fileEntryRepositoryType");
	}

	public String getBaseTypeId() {
		return get("fileEntryBaseTypeId");
	}

	public Date getCreationDate() {
		return getDate("fileEntryCreationDate");
	}

	public String getEditLink() {
		return get("editLink");
	}

	public String getDownloadLink() {
		return get("downloadLink");
	}

	public Boolean isLastVersion() {
		// TODO Auto-generated method stub
		return Boolean.valueOf(get("fileEntryIsLastVersion"));
	}

	public Integer getReccomendationsCount() {
		// TODO Auto-generated method stub
		return Integer.valueOf(get("fileEntryRecommendationCount"));
	}

	public String getSharingInformationURL() {
		return get("sharingInformationURL");
	}

	public Boolean isMajorVersion() {
		return Boolean.valueOf(get("fileEntryIsMajorVersion"));
	}

	public String getOranizationId() {
		return get("fileEntryOrgnizationId");
	}

	public String getContentStreamId() {
		return get("fileEntryContentStreamId");
	}

	public String getSummary() {
		// TODO Auto-generated method stub
		return get("summary");
	}

	public Boolean isPublic() {
		return Boolean.valueOf(get("fileEntryIsPublic"));
	}

	/**
	 * An URL that points to the file page on the SmartCloud server, to be consumed by humans
	 * 
	 * @return a string representing the URL
	 */
	public String getPageURL() {
		return get("pageLink");
	}

	/**
	 * @return the total size for the entry
	 */
	public Integer getTotalSize() {
		return Integer.valueOf(get("fileEntryTotalSize"));
	}

	/**
	 * The identifier for this entry
	 * 
	 * @return a string
	 */
	public String getObjectId() {
		return get("fileEntryObjectId");
	}

	/**
	 * The number of downloads for the file represented by this entry
	 * 
	 * @return an Integer if the information is available or null
	 */
	public Integer getDownloadCount() {
		return Integer.valueOf(get("fileEntryDownloadCount"));
	}

	public String getVersionHistoryURL() {
		return get("versionHistoryURL");
	}

	public String getVersionSeriesId() {
		return get("fileEntryVersionSeriesId");
	}

	public String getRelationshipsURL() {
		return get("relationshipsURL");
	}

	public String getRecommendationsURL() {
		return get("recommendationsURL");
	}

	public String getAuthorName() {
		return get("authorName");
	}

	public String getDisplayName() {
		return get("fileEntryDisplayName");
	}

	public String getContentLanguage() {
		return get("fileEntryContentLanguage");
	}

	public Boolean isImmutable() {
		return Boolean.valueOf(get("fileEntryIsImmutable"));
	}

	public Boolean isLatestMajorVersion() {
		return Boolean.valueOf(get("fileEntryIsLatestMajorVersion"));
	}

	public boolean isVersionSeriesCheckedOut() {
		return Boolean.valueOf(get("fileEntryIsVersionSeriesCheckedOut"));
	}

	public String getUserId() {
		return get("userId");
	}

	public Boolean isViral() {
		return Boolean.valueOf(get("fileEntryIsViral"));
	}

	public String getPermissionsURL() {
		return get("permissionsURL");
	}

	public String getACLRemoverURL() {
		return get("aclRemoverURL");
	}

	public String getPathSegment() {
		return get("pathSegment");
	}

	public String getContentStreamMimeType() {
		return get("fileEntryContentStreamMimeType");
	}

	public String getAuthorEmail() {
		return get("authorEmail");
	}

	public String getVersionHistoryAlternateURL() {
		return get("versionHistoryAlternateURL");
	}

	public String getOrganizationName() {
		return get("fileEntryOrganizationName");
	}

	public String getAuthorOrgId() {
		return get("authorOrgId");
	}

	public String getTitle() {
		return get("title");
	}

	public String getSharePermission() {
		return get("fileEntrySharePermission");
	}

	public String getRepositoryId() {
		return get("fileEntryrepositoryId");
	}

	public Date getPublishDate() {
		return getDate("publishDate");
	}

	public Date getLastUpdateDate() {
		return getDate("lastUpdateDate");
	}

	public Date getLockedWhen() {
		return getDate("fileEntryLockedWhen");
	}

	public Date getChangeTokenDate() {
		return getDate("fileEntryChangeToken");
	}

	public String getAuthorPrincipalId() {
		return get("authorPrincipalId");
	}

	public String getDownloadHistoryURL() {
		return get("downloadHistoryURL");
	}

	public String getACLHistoryURL() {
		return get("aclHistoryURL");
	}

	public String getDataURL() {
		return get("dataURL");
	}

	public Integer getCommentCount() {
		return Integer.valueOf(get("fileEntryCommentCount"));
	}

	public String getCheckinComment() {
		return get("fileEntryCheckinComment");
	}

	public Integer getContentStreamLength() {
		return Integer.valueOf(get("fileEntryContentStreamLength"));
	}

	public String getContentStreamFileName() {
		return get("fileEntryContentStreamFileName");
	}

	public String getOtherEmail() {
		return get("otherEmail");
	}

	public String getFileExtension() {
		return get("fileEntryFileExtension");
	}

	public String getVersionLabel() {
		return get("fileEntryVersionLabel");
	}

	public String getRecommendationURL() {
		return get("recommendationURL");
	}

	public String getServiceURL() {
		return get("serviceURL");
	}

	public String getLockType() {
		return get("fileEntryLockType");
	}

	public String getVersionSeriesCheckedOutId() {
		return get("fileEntryVersionSeriesCheckedOutId");
	}

	public String getPoliciesURL() {
		return get("policiesURL");
	}

	public Boolean isRecommendedByCaller() {
		return Boolean.valueOf(get("fileEntryIsRecommendedByCaller"));
	}

	public String getAuthorDisplayName() {
		return get("authorDisplayName");
	}

	public class UserProfile extends BaseEntity<DataFormat> {
		public UserProfile(DataFormat data) {
			super(data, null);

		}

		public String getName() {
			return get("profileNode.name");
		}

		public String getValue() {
			return get("profileNode.value");
		}

		public String getOtherEmail() {
			return get("profileNode.otherEmail");
		}

		public String getDisplayName() {
			return get("profileNode.displayName");
		}

		public String getOrgId() {
			return get("profileNode.orgId");
		}

		public String getUserState() {
			return get("profileNode.userState");
		}

		public String getOrgName() {
			return get("profileNode.orgName");
		}

		public String getUserId() {
			return get("profileNode.userId");
		}

		public String getPrincipalId() {
			return get("profileNode.principalId");
		}

		public String getEmail() {
			return get("profileNode.email");
		}

		/*
		 * (non-Javadoc)
		 * @see com.ibm.sbt.services.client.smartcloud.base.BaseEntity#getXMLFieldMapping()
		 */
		@Override
		protected String[][] getXMLFieldMapping() {
			return new String[][] { { "profileNode.value", "cmis:value" },
					{ "profileNode.name", "atom:name" }, { "profileNode.email", "atom:email" },
					{ "profileNode.otherEmail", "lcmis:email" }, { "profileNode.userId", "snx:userid" },
					{ "profileNode.userState", "snx:userState" },
					{ "profileNode.displayName", "lcmis:displayName" },
					{ "profileNode.principalId", "lcmis:principalId" }, { "profileNode.orgId", "snx:orgId" },
					{ "profileNode.orgName", "snx:orgName" } };
		}

		/*
		 * (non-Javadoc)
		 * @see com.ibm.sbt.services.client.smartcloud.base.BaseEntity#getJsonFieldMapping()
		 */
		@Override
		protected String[][] getJsonFieldMapping() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	@Override
	protected String[][] getXMLFieldMapping() {
		return new String[][] {
				{ "authorName", "atom:author/atom:name" },
				{ "authorDisplayName", "atom:author/lcmis:displayName" },
				{ "authorEmail", "atom:author/atom:email" },
				{ "otherEmail", "atom:author/lcmis:email" },
				{ "userId", "atom:author/snx:userid" }, // TODO: do we go for uniform case here or
				// just what the service provide?
				{ "userState", "atom:author/snx:userState" },
				{ "authorPrincipalId", "author/lcmis:principalId" },
				{ "authorOrgId", "atom:author/snx:orgId" },
				{ "authorOrgName", "atom:author/snx:orgName" },
				{ "publishDate", "atom:published" },
				{ "lastEditDate", "app:edited" },
				{ "lastUpdateDate", "atom:updated" },
				{ "summary", "atom:summary" },
				{ "title", "atom:title" },
				{ "contentLink", "atom:content/@src" },
				{ "fileEntryId", "atom:id" },
				{ "downloadLink", "atom:link[@rel='enclosure']/@href" },
				{ "pageLink", "atom:link[@rel='alternate']/@href" },
				{ "serviceURL", "atom:link[@rel='service']/@href" }, // TODO: name convention (A
				// link is for human, a
				// url is for service?)
				{ "selfURL", "atom:link[@rel='self']/@href" },
				{ "editLink", "atom:link[@rel='edit']/@href" },
				{ "editMediaLink", "atom:link[@rel='edit-media']/@href" },
				{ "permissionsURL",
						"atom:link[@rel='http://docs.oasis-open.org/ns/cmis/link/200908/allowableactions']/@href" },
				{ "descriptionLink", "atom:link[@rel='describedby']/@href" },
				{ "relationshipsURL",
						"atom:link[@rel='http://docs.oasis-open.org/ns/cmis/link/200908/relationships']/@href" },
				{ "aclURL", "atom:link[@rel='http://docs.oasis-open.org/ns/cmis/link/200908/acl']/@href" },
				{ "aclHistoryURL",
						"atom:link[@rel='http://www.ibm.com/xmlns/prod/sn/cmis/acl-history']/@href" },
				{ "aclRemoverURL",
						"atom:link[@rel='http://www.ibm.com/xmlns/prod/sn/cmis/acl-remover']/@href" },
				{ "policiesURL",
						"atom:link[@rel='http://docs.oasis-open.org/ns/cmis/link/200908/policies']/@href" },
				{ "dataURL",
						"atom:link[@rel='http://www.ibm.com/xmlns/prod/sn/cmis/multipart-form/object']/@href" },
				{ "versionHistoryURL", "atom:link[@rel='version-history']/@href" },
				{ "versionHistoryAlternateURL",
						"atom:link[@rel='http://www.ibm.com/xmlns/prod/sn/cmis/version-history']/@href" },
				{ "downloadHistoryURL",
						"atom:link[@rel='http://www.ibm.com/xmlns/prod/sn/cmis/download-history']/@href" },
				{ "recommendationsURL",
						"atom:link[@rel='http://www.ibm.com/xmlns/prod/sn/cmis/recommendations']/@href" },
				{ "recommendationURL",
						"atom:link[@rel='http://www.ibm.com/xmlns/prod/sn/cmis/recommendation']/@href" },
				{ "sharingInformationURL", "atom:link[@rel='shared']/@href" },
				{ "pathSegment", "cmisra:pathSegment" },
				{ "fileEntryDisplayName",
						"cmisra:object/cmis:properties//*[@queryName='cmis:name']/cmis:value" },

				// TODO: test the decoding over these entries
				{ "fileEntrySharePermission",
						"cmisra:object/cmis:properties//*[@queryName='snx:sharePermission']/cmis:value" },
				{ "fileEntryVersionSeriesCheckedOutId",
						"cmisra:object/cmis:properties//*[@queryName='cmis:versionSeriesCheckedOutId']/cmis:value" },

				{ "fileEntryCreatedBy", "cmisra:object/cmis:properties//*[@queryName='cmis:createdBy']" },
				{ "fileEntryModifiedBy", "cmisra:object/cmis:properties//*[@queryName='cmis:lastModifiedBy']" },
				{ "fileEntryCheckedOutBy",
						"cmisra:object/cmis:properties//*[@queryName='cmis:versionSeriesCheckedOutBy']" },
				{ "fileEntryLockedBy", "cmisra:object/cmis:properties//*[@queryName='snx:lockedBy']" },

				// END

				{ "fileEntryObjectId",
						"cmisra:object/cmis:properties//*[@queryName='cmis:objectId']/cmis:value" },
				{ "fileEntryBaseTypeId",
						"cmisra:object/cmis:properties//*[@queryName='cmis:baseTypeId']/cmis:value" },
				{ "fileEntryObjectTypeId",
						"cmisra:object/cmis:properties//*[@queryName='cmis:objectTypeId']/cmis:value" },
				{ "fileEntryCreationDate",
						"cmisra:object/cmis:properties//*[@queryName='cmis:creationDate']/cmis:value" },
				{ "fileEntryLastModificationDate",
						"cmisra:object/cmis:properties//*[@queryName='cmis:lastModificationDate']/cmis:value" },
				{ "fileEntryChangeToken",
						"cmisra:object/cmis:properties//*[@queryName='cmis:changeToken']/cmis:value" },
				{ "fileEntryIsImmutable",
						"cmisra:object/cmis:properties//*[@queryName='cmis:isImmutable']/cmis:value" },
				{ "fileEntryIsLastVersion",
						"cmisra:object/cmis:properties//*[@queryName='cmis:isLatestVersion']/cmis:value" },
				{ "fileEntryIsMajorVersion",
						"cmisra:object/cmis:properties//*[@queryName='cmis:isMajorVersion']/cmis:value" },
				{ "fileEntryIsLatestMajorVersion",
						"cmisra:object/cmis:properties//*[@queryName='cmis:isLatestMajorVersion']/cmis:value" },
				{ "fileEntryVersionLabel",
						"cmisra:object/cmis:properties//*[@queryName='cmis:versionLabel']/cmis:value" },
				{ "fileEntryVersionSeriesId",
						"cmisra:object/cmis:properties//*[@queryName='cmis:versionSeriesId']/cmis:value" },
				{ "fileEntryIsVersionSeriesCheckedOut",
						"cmisra:object/cmis:properties//*[@queryName='cmis:isVersionSeriesCheckedOut']/cmis:value" },
				{ "fileEntryCheckinComment",
						"cmisra:object/cmis:properties//*[@queryName='cmis:checkinComment']/cmis:value" },
				{ "fileEntryContentStreamLength",
						"cmisra:object/cmis:properties//*[@queryName='cmis:contentStreamLength']/cmis:value" },
				{ "fileEntryContentStreamMimeType",
						"cmisra:object/cmis:properties//*[@queryName='cmis:contentStreamMimeType']/cmis:value" },
				{ "fileEntryContentStreamFileName",
						"cmisra:object/cmis:properties//*[@queryName='cmis:contentStreamFileName']/cmis:value" },
				{ "fileEntryContentStreamId",
						"cmisra:object/cmis:properties//*[@queryName='cmis:contentStreamId']/cmis:value" },
				{ "fileEntryrepositoryId",
						"cmisra:object/cmis:properties//*[@queryName='snx:repositoryId']/cmis:value" },
				{ "fileEntryCommentCount",
						"cmisra:object/cmis:properties//*[@queryName='snx:commentCount']/cmis:value" },
				{ "fileEntryDownloadCount",
						"cmisra:object/cmis:properties//*[@queryName='snx:downloadCount']/cmis:value" },
				{ "fileEntryDownloadCountAnon",
						"cmisra:object/cmis:properties//*[@queryName='snx:downloadCountAnon']/cmis:value" },
				{ "fileEntryTotalSize",
						"cmisra:object/cmis:properties//*[@queryName='snx:sizeAppliedToQuota']/cmis:value" },
				{ "fileEntryContentLanguage",
						"cmisra:object/cmis:properties//*[@queryName='snx:language']/cmis:value" },
				{ "fileEntryFileExtension",
						"cmisra:object/cmis:properties//*[@queryName='snx:contentStreamFileExt']/cmis:value" },
				{ "fileEntryIsPublic",
						"cmisra:object/cmis:properties//*[@queryName='snx:isPublic']/cmis:value" },
				{ "fileEntryVisibility",
						"cmisra:object/cmis:properties//*[@queryName='snx:visibilityComputed']/cmis:value" },
				{ "fileEntryIsViral",
						"cmisra:object/cmis:properties//*[@queryName='snx:isSharedViral']/cmis:value" },
				{ "fileEntryRepositoryType",
						"cmisra:object/cmis:properties//*[@queryName='snx:repositoryType']/cmis:value" },
				{ "fileEntryRecommendationCount",
						"cmisra:object/cmis:properties//*[@queryName='snx:recommendationCount']/cmis:value" },
				{ "fileEntryIsRecommendedByCaller",
						"cmisra:object/cmis:properties//*[@queryName='snx:isRecommendedByCaller']/cmis:value" },
				{ "fileEntryStreamLastModified",
						"cmisra:object/cmis:properties//*[@queryName='snx:contentStreamLastModified']/cmis:value" },
				{ "fileEntryIsExternal",
						"cmisra:object/cmis:properties//*[@queryName='snx:isExternal']/cmis:value" },
				{ "fileEntryLockType",
						"cmisra:object/cmis:properties//*[@queryName='snx:lockType']/cmis:value" },
				{ "fileEntryLockedWhen",
						"cmisra:object/cmis:properties//*[@queryName='snx:lockedWhen']/cmis:value" },
				{ "fileEntryIsEncrypted",
						"cmisra:object/cmis:properties//*[@queryName='snx:encrypt']/cmis:value" },
				{ "fileEntryOrgnizationId",
						"cmisra:object/cmis:properties//*[@queryName='snx:orgId']/cmis:value" },
				{ "fileEntryOrganizationName",
						"cmisra:object/cmis:properties//*[@queryName='snx:orgName']/cmis:value" }, };
	}

	@Override
	protected String[][] getJsonFieldMapping() {
		return null;
	}
}
