/*
 * © Copyright IBM Corp. 2014
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

import java.io.ByteArrayOutputStream;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.ibm.sbt.services.client.base.datahandlers.EntityList;

/**
 * @author mwallace
 *
 */
public class FileDownloadTest extends BaseFileServiceTest {
	
	@Test
	public void testDownloadPrivateFile() throws Exception {
		File file = uploadFile("testDownloadPrivateFile");
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		long bytes = fileService.downloadFile(baos, file, null);
		Assert.assertEquals("Error downloading private file", file.getTotalMediaSize().longValue(), bytes);
		
		fileService.deleteFile(file.getFileId());
	}

	@Test
	public void testDownloadPublicFile() throws Exception {
		File file = uploadFile("testDownloadPublicFile");
		
		file.setVisibility("public");
		file.save();
		String fileId = file.getFileId();
		
		file = fileService.getFile(fileId);
		Assert.assertEquals("Error making file public", "public", file.getVisibility());
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		long bytes = fileService.downloadFile(baos, file, null);
		Assert.assertEquals("Error downloading public file", file.getTotalMediaSize().longValue(), bytes);
		
		fileService.deleteFile(file.getFileId());
	}

	@Ignore
	@Test
	public void testDownloadSharedWithMeFiles() throws Exception {
		EntityList<File> fileList = fileService.getFilesSharedWithMe();
		for (File file : fileList) {
			//System.out.println(DOMUtil.getXMLString(file.getDataHandler().getData()));
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			//FIX: Investigate: SOME files have the length specified on the metadata, others don't
			long bytes = fileService.downloadFile(baos, file, null);
			Assert.assertEquals("Error downloading file shared with me", file.getEnclosureLength().longValue(), bytes);
		}
	}
	
	@Test
	public void testDownloadPublicCommunityFile() throws Exception {
		String communityUuid = createCommunity("testDownloadPublicCommunityFile", "public");
		
		File file = uploadCommunityFile("testDownloadCommunityFile", communityUuid);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		long bytes = fileService.downloadFile(baos, file, null);
		Assert.assertEquals("Error downloading community file", file.getTotalMediaSize().longValue(), bytes);
		
		communityService.deleteCommunity(communityUuid);
	}

	@Test
	public void testDownloadPrivateCommunityFile() throws Exception {
		String communityUuid = createCommunity("testDownloadPrivateCommunityFile", "private");
		
		File file = uploadCommunityFile("testDownloadCommunityFile", communityUuid);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		long bytes = fileService.downloadFile(baos, file, null);
		Assert.assertEquals("Error downloading community file", file.getTotalMediaSize().longValue(), bytes);
		
		communityService.deleteCommunity(communityUuid);
	}

	@Ignore
	@Test
	public void testGetSharedWithMeFiles() throws Exception {
		EntityList<File> fileList = fileService.getFilesSharedWithMe();
		for (File file : fileList) {
			//System.out.println(DOMUtil.getXMLString(file.getDataHandler().getData()));
			
			String fileId = file.getFileId();
			String libraryId = file.getLibraryId();
			File sharedWithMe = fileService.getFile(fileId, libraryId, null);
			
			Assert.assertEquals("Error reading file shared with me", file.getTitle(), sharedWithMe.getTitle());
		}
	}
}
