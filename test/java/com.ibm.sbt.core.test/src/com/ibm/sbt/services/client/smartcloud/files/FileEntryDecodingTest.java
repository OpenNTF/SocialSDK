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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;
import org.w3c.dom.Node;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.Format;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.SBTServiceException;
import com.ibm.sbt.services.client.smartcloud.files.FileEntry.UserProfile;

public class FileEntryDecodingTest {

	// TestContext c = TestContext.initDefaultTestingContext();

	@Test
	public void testSingleDecoding() throws SBTServiceException {
		Node data = null;
		try {
			data = DOMUtil.createDocument(this.getClass().getResourceAsStream("FileEntry1.xml"));
			List<Node> entryNodesList = FileEntry.getNodeExtractorFromData(data)
					.getEntitiesFromServiceResult(data);
			data = entryNodesList.get(0);
		} catch (XMLException e) {
			e.printStackTrace();
			fail();
		}

		FileEntry<Node> entry = null;

		entry = new FileEntry<Node>(data, null);

		Logger.getAnonymousLogger().info(""+entry);
		assertEquals(entry.getVisibility(), "private");
		assertTrue(entry.isExternal());
		assertEquals(entry.getFileId(),
				"http://www.ibm.com/xmlns/prod/sn/cmis/snx:file!a041af41-d2c0-4bec-a106-4e16a4f15195");
		assertEquals(
				entry.getContentLink(),
				"https://apps.lotuslive.com/files/basic/cmis/repository/p%2120527378/object/snx%3Afile%21a041af41-d2c0-4bec-a106-4e16a4f15195/stream/a041af41-d2c0-4bec-a106-4e16a4f15195/smcl4645780839489271795tmp");
		assertEquals(
				entry.getEditMediaLink(),
				"https://apps.lotuslive.com/files/basic/cmis/repository/p%2120527378/object/snx%3Afile%21a041af41-d2c0-4bec-a106-4e16a4f15195/stream/a041af41-d2c0-4bec-a106-4e16a4f15195/smcl4645780839489271795tmp");
		assertEquals(entry.getObjectTypeId(), "snx:file");
		assertEquals(entry.getUserState(), "active");
		assertEquals(
				entry.getACLUrl(),
				"https://apps.lotuslive.com/files/basic/cmis/repository/p%2120527378/acl/snx%3Afile%21a041af41-d2c0-4bec-a106-4e16a4f15195");
		assertEquals(entry.getAuthorOrganizationName(), "Renovations Inc.");
		assertFalse(entry.isEncrypted());
		assertEquals(
				entry.getEntityURL(),
				"https://apps.lotuslive.com/files/basic/cmis/repository/p%2120527378/object/snx%3Afile%21a041af41-d2c0-4bec-a106-4e16a4f15195");
		assertEquals(entry.getDescrptionLink(),
				"https://apps.lotuslive.com/files/basic/cmis/repository/p%2120527378/type/snx%3Afile");
		assertEquals(entry.getRepositoryTypeId(), "personalFiles");
		assertEquals(entry.getBaseTypeId(), "cmis:document");
		assertEquals(entry.getDownloadsAnonCount(), Integer.valueOf(0));
		assertEquals(entry.getContentStreamId(), "a041af41-d2c0-4bec-a106-4e16a4f15195");
		assertEquals(entry.getOranizationId(), "20051314");
		assertTrue(entry.isMajorVersion());
		assertEquals(entry.getSharingInformationURL(),
				"https://apps.lotuslive.com/files/basic/api/myshares/feed?sharedWhat=a041af41-d2c0-4bec-a106-4e16a4f15195");
		assertEquals(entry.getReccomendationsCount(), Integer.valueOf(0));
		assertTrue(entry.isLastVersion());
		assertEquals(
				entry.getDownloadLink(),
				"https://apps.lotuslive.com/files/basic/cmis/repository/p%2120527378/object/snx%3Afile%21a041af41-d2c0-4bec-a106-4e16a4f15195/stream/a041af41-d2c0-4bec-a106-4e16a4f15195/smcl4645780839489271795tmp");
		assertEquals(
				entry.getEditLink(),
				"https://apps.lotuslive.com/files/basic/cmis/repository/p%2120527378/object/snx%3Afile%21a041af41-d2c0-4bec-a106-4e16a4f15195");
		assertEquals(entry.getSummary(), "");
		assertFalse(entry.isPublic());
		assertEquals(entry.getPageURL(),
				"https://apps.lotuslive.com/files/app/file/a041af41-d2c0-4bec-a106-4e16a4f15195");
		assertEquals(entry.getTotalSize(), Integer.valueOf(96));
		assertEquals(entry.getObjectId(), "snx:file!a041af41-d2c0-4bec-a106-4e16a4f15195");
		assertEquals(entry.getDownloadCount(), Integer.valueOf(0));
		assertEquals(
				entry.getVersionHistoryURL(),
				"https://apps.lotuslive.com/files/basic/cmis/repository/p%2120527378/versions/snx%3Afile%21a041af41-d2c0-4bec-a106-4e16a4f15195");
		assertEquals(entry.getVersionSeriesId(), "a041af41-d2c0-4bec-a106-4e16a4f15195");
		assertEquals(
				entry.getRelationshipsURL(),
				"https://apps.lotuslive.com/files/basic/cmis/repository/p%2120527378/relationships/snx%3Afile%21a041af41-d2c0-4bec-a106-4e16a4f15195");
		assertEquals(
				entry.getRecommendationsURL(),
				"https://apps.lotuslive.com/files/basic/cmis/repository/p%2120527378/recommendations/snx%3Afile%21a041af41-d2c0-4bec-a106-4e16a4f15195");
		assertFalse(entry.isVersionSeriesCheckedOut());
		assertEquals(entry.getAuthorName(), "Shankar Vakil");
		assertEquals(entry.getDisplayName(), "smcl4645780839489271795tmp");
		assertEquals(entry.getContentLanguage(), "");
		assertFalse(entry.isImmutable());
		assertTrue(entry.isLatestMajorVersion());

		assertEquals(entry.getUserId(), "20527378");
		assertFalse(entry.isViral());
		assertEquals(
				entry.getPermissionsURL(),
				"https://apps.lotuslive.com/files/basic/cmis/repository/p%2120527378/allowable/snx%3Afile%21a041af41-d2c0-4bec-a106-4e16a4f15195");
		assertEquals(
				entry.getACLRemoverURL(),
				"https://apps.lotuslive.com/files/basic/cmis/repository/p%2120527378/acl-remover/snx%3Afile%21a041af41-d2c0-4bec-a106-4e16a4f15195");
		assertNotNull(entry.getLastUpdateDate());
		assertEquals(entry.getPathSegment(), "smcl4645780839489271795tmp");
		assertEquals(entry.getContentStreamMimeType(), "application/octet-stream");
		assertEquals(entry.getAuthorEmail(), "shankar.vakil@notes.llrenovations.com");
		assertEquals(
				entry.getVersionHistoryAlternateURL(),
				"https://apps.lotuslive.com/files/basic/cmis/repository/p%2120527378/versions-social/snx%3Afile%21a041af41-d2c0-4bec-a106-4e16a4f15195");
		assertEquals(entry.getOrganizationName(), "Renovations Inc.");
		assertEquals(entry.getAuthorOrgId(), "20051314");
		assertEquals(entry.getTitle(), "smcl4645780839489271795tmp");
		assertEquals(entry.getSharePermission(), "");
		assertNull(entry.getLockedWhen());
		assertEquals(entry.getRepositoryId(), "p!20527378");
		assertNotNull(entry.getPublishDate());

		assertEquals(entry.getAuthorPrincipalId(), "20527378");
		assertEquals(entry.getAuthorDisplayName(), "Shankar Vakil");
		assertEquals(
				entry.getDownloadHistoryURL(),
				"https://apps.lotuslive.com/files/basic/cmis/repository/p%2120527378/downloads/snx%3Afile%21a041af41-d2c0-4bec-a106-4e16a4f15195");
		assertEquals(
				entry.getACLHistoryURL(),
				"https://apps.lotuslive.com/files/basic/cmis/repository/p%2120527378/acl-history/snx%3Afile%21a041af41-d2c0-4bec-a106-4e16a4f15195");
		assertEquals(
				entry.getDataURL(),
				"https://apps.lotuslive.com/files/basic/cmis/repository/p%2120527378/mobject/snx%3Afile%21a041af41-d2c0-4bec-a106-4e16a4f15195");
		assertEquals(entry.getCommentCount(), Integer.valueOf(0));
		assertEquals(entry.getCheckinComment(), "");
		assertEquals(entry.getContentStreamLength(), Integer.valueOf(96));
		assertEquals(entry.getContentStreamFileName(), "smcl4645780839489271795tmp");
		assertEquals(entry.getOtherEmail(), "shankar.vakil@notes.llrenovations.com");
		assertEquals(entry.getFileExtension(), "");
		assertEquals(entry.getVersionLabel(), "1");
		assertNotNull(entry.getChangeTokenDate());
		assertEquals(
				entry.getRecommendationURL(),
				"https://apps.lotuslive.com/files/basic/cmis/repository/p%2120527378/recommendation/snx%3Afile%21a041af41-d2c0-4bec-a106-4e16a4f15195");
		assertEquals(entry.getServiceURL(),
				"https://apps.lotuslive.com/files/basic/cmis/repository/p%2120527378/servicedoc");
		assertEquals(entry.getLockType(), "NONE");
		assertEquals(entry.getVersionSeriesCheckedOutId(), "");
		assertFalse(entry.isRecommendedByCaller());
		assertEquals(
				entry.getPoliciesURL(),
				"https://apps.lotuslive.com/files/basic/cmis/repository/p%2120527378/policy/snx%3Afile%21a041af41-d2c0-4bec-a106-4e16a4f15195");

		// TODO: meaningful tests on dates
		assertNotNull(entry.getLastStreamModifiedDate());
		assertNotNull(entry.getLastEditDate());
		assertNotNull(entry.getLastEntryModifiedDate());
		assertNotNull(entry.getCreationDate());

		UserProfile createdBy = entry.getCreatedBy();
		assertNotNull(createdBy);

		assertEquals(createdBy.getPrincipalId(), "20527378");
		assertEquals(createdBy.getUserId(), "20527378");
		assertEquals(createdBy.getDisplayName(), "Shankar Vakil");
		assertEquals(createdBy.getOrgName(), "Renovations Inc.");
		assertEquals(createdBy.getValue(), "Shankar Vakil");
		assertEquals(createdBy.getName(), "Shankar Vakil");
		assertEquals(createdBy.getEmail(), "shankar.vakil@notes.llrenovations.com");
		assertEquals(createdBy.getOtherEmail(), "shankar.vakil@notes.llrenovations.com");
		assertEquals(createdBy.getOrgId(), "20051314");

		UserProfile modifiedBy = entry.getModifiedBy();
		assertNotNull(modifiedBy);
		assertEquals(modifiedBy.getPrincipalId(), "20527378");
		assertEquals(modifiedBy.getUserId(), "20527378");
		assertEquals(modifiedBy.getDisplayName(), "Shankar Vakil");
		assertEquals(modifiedBy.getOrgName(), "Renovations Inc.");
		assertEquals(modifiedBy.getValue(), "Shankar Vakil");
		assertEquals(modifiedBy.getName(), "Shankar Vakil");
		assertEquals(modifiedBy.getEmail(), "shankar.vakil@notes.llrenovations.com");
		assertEquals(modifiedBy.getOtherEmail(), "shankar.vakil@notes.llrenovations.com");
		assertEquals(modifiedBy.getOrgId(), "20051314");

		assertNull(entry.getCheckedOutBy());
		assertNull(entry.getLockedBy());
	}

	@Test
	public void testListdecoding() throws SBTServiceException, XMLException {
		Node atomEntryList = null;
		try {
			atomEntryList = DOMUtil.createDocument(this.getClass().getResourceAsStream("MyFiles.xml"));
		} catch (XMLException e) {
			e.printStackTrace();
			fail();
		}

		FileEntry<Node> fileEntry = null;
		Collection<Node> entryNodesList = FileEntry.getNodeExtractorFromData(atomEntryList)
				.getEntitiesFromServiceResult(atomEntryList);

		for (Node entryNode : entryNodesList) {
			//DOMUtil.serialize(System.out, entryNode, Format.defaultFormat);

			fileEntry = new FileEntry<Node>(entryNode, null);
			Logger.getAnonymousLogger().info(fileEntry.getObjectId());
			assertNotNull(fileEntry.getObjectId());
			assertFalse(fileEntry.getObjectId().length() == 0);

		}
	}

}
