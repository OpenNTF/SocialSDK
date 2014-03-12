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
import org.junit.Before;
import org.junit.Test;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.connections.communities.CommunityServiceException;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.ConnectionsBasicEndpoint;

/**
 * @author mwallace
 *
 */
public class FileUpdateTest {
	
	protected FileService fileService;
	protected CommunityService communityService;
	
	@Before
	public void createFileService() {
		String url = System.getProperty("url");
		String user = System.getProperty("user");
		String password = System.getProperty("password");
		if (StringUtil.isNotEmpty(url) && StringUtil.isNotEmpty(user) && StringUtil.isNotEmpty(password)) {
			BasicEndpoint endpoint = new ConnectionsBasicEndpoint();
			endpoint.setUrl(url);
			endpoint.setUser(user);
			endpoint.setPassword(password);
			endpoint.setForceTrustSSLCertificate(true);
			
			fileService = new FileService(endpoint);
			communityService = new CommunityService(endpoint);
		} else {
			fileService = new FileService();
			communityService = new CommunityService();
		}
	}
	
	
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
	
	// Internals
	
	private String createCommunity(String baseName, String type) throws CommunityServiceException {
		String title = baseName + System.currentTimeMillis();
		String content = baseName + " content";

		return communityService.createCommunity(title, content, type);
	}
	
	private File uploadCommunityFile(String baseName, String communityUuid) throws FileServiceException, XMLException {
		String name = baseName + System.currentTimeMillis();

		byte[] bytes = name.getBytes();
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

		File file = fileService.uploadCommunityFile(bais, communityUuid, name, bytes.length);
		Assert.assertNotNull("Error uploading file", file);
		//System.out.println(DOMUtil.getXMLString(file.getDataHandler().getData()));
		
		return file;
	}
	
	private File uploadFile(String baseName) throws FileServiceException, XMLException {
		String name = baseName + System.currentTimeMillis();

		byte[] bytes = name.getBytes();
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

		File file = fileService.uploadFile(bais, name, bytes.length);
		Assert.assertNotNull("Error uploading file", file);
		//System.out.println(DOMUtil.getXMLString(file.getDataHandler().getData()));
		
		return file;
	}
	
	private File updateFile(File file, String baseName) throws FileServiceException, XMLException {
		String name = baseName + System.currentTimeMillis();

		byte[] bytes = name.getBytes();
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

		file = fileService.updateFile(bais, file, null);
		Assert.assertNotNull("Error updating file", file);
		//System.out.println(DOMUtil.getXMLString(file.getDataHandler().getData()));
		
		return file;
	}
	
}
