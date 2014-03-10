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
import java.io.ByteArrayOutputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ibm.commons.util.StringUtil;
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
public class FileDownloadTest {
	
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
		file.save(null);
		
		file = fileService.getFile(file.getFileId());
		Assert.assertEquals("Error making file public", "public", file.getVisibility());
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		long bytes = fileService.downloadFile(baos, file, null);
		Assert.assertEquals("Error downloading public file", file.getTotalMediaSize().longValue(), bytes);
		
		fileService.deleteFile(file.getFileId());
	}
	
	@Test
	public void testDownloadSharedWithMeFiles() throws Exception {
		EntityList<File> fileList = fileService.getFilesSharedWithMe();
		for (File file : fileList) {
			//System.out.println(DOMUtil.getXMLString(file.getDataHandler().getData()));
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
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
	
}
