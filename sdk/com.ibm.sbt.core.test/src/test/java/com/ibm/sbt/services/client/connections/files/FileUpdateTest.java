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

import java.io.ByteArrayInputStream;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;

/**
 * @author mwallace
 *
 */
public class FileUpdateTest extends BaseFileServiceTest {
	
	@Test
	public void testUpdatePrivateFile() throws Exception {
		File file = uploadFile("testUpdatePrivateFile");
		int version = file.getVersion();
		
		file = updateFile(file, "testUpdatePrivateFile");
		Assert.assertEquals("Error updating private file", file.getVersion(), version+1);
		
		fileService.deleteFile(file.getFileId());
	}

	@Test
	public void testUpdatePublicFile() throws Exception {
		File file = uploadFile("testUpdatePublicFile");
		int version = file.getVersion();
		
		file.setVisibility("public");
		file.save(null);
		
		file = fileService.getFile(file.getFileId());
		Assert.assertEquals("Error making file public", "public", file.getVisibility());
		
		file = updateFile(file, "testUpdatePublicFile");
		Assert.assertEquals("Error updating public file", file.getVersion(), version+1);
				
		fileService.deleteFile(file.getFileId());
	}
	
	@Test
	public void testUpdateSharedWithMeFiles() throws Exception {
		EntityList<File> fileList = fileService.getFilesSharedWithMe();
		for (File file : fileList) {
			System.out.println(DOMUtil.getXMLString(file.getDataHandler().getData()));
			int version = file.getVersion();
			String visibility = file.getVisibility();
			
			if ("public".equals(visibility)) {
				file = updateFile(file, "testUpdatePublicFile");
				Assert.assertEquals("Error updating public file", file.getVersion(), version+1);
				//System.out.println("Updated: "+file.getTitle());
			}
		}
	}
	
	@Test
	public void testUpdatePublicCommunityFile() throws Exception {
		String communityUuid = createCommunity("testUpdatePublicCommunityFile", "public");
		
		File file = uploadCommunityFile("testUpdatePublicCommunityFile", communityUuid);
		int version = file.getVersion();
		
		file = updateFile(file, "testUpdatePublicCommunityFile");
		Assert.assertEquals("Error updating public community file", file.getVersion(), version+1);
		
		communityService.deleteCommunity(communityUuid);
	}

	@Test
	public void testUpdatePrivateCommunityFile() throws Exception {
		String communityUuid = createCommunity("testUpdatePrivateCommunityFile", "private");
		
		File file = uploadCommunityFile("testUpdatePrivateCommunityFile", communityUuid);
		int version = file.getVersion();
		
		file = updateFile(file, "testUpdatePrivateCommunityFile");
		Assert.assertEquals("Error updating private community file", file.getVersion(), version+1);
				
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
