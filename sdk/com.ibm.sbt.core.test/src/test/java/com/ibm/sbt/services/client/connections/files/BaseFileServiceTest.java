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

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.ConnectionsBasicEndpoint;

/**
 * 
 * @author Carlos Manias
 *
 */
public class BaseFileServiceTest extends BaseUnitTest {

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

	protected String createCommunity(String baseName, String type) throws ClientServicesException {
		String title = baseName + System.currentTimeMillis();
		String content = baseName + " content";

		return communityService.createCommunity(title, content, type);
	}
	
	protected File uploadCommunityFile(String baseName, String communityUuid) throws ClientServicesException, XMLException {
		String name = baseName + System.currentTimeMillis();

		byte[] bytes = name.getBytes();
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

		File file = fileService.uploadCommunityFile(bais, communityUuid, name, bytes.length);
		Assert.assertNotNull("Error uploading file", file);
		//System.out.println(DOMUtil.getXMLString(file.getDataHandler().getData()));
		
		return file;
	}

	protected File uploadFile(String baseName) throws ClientServicesException, XMLException {
		String name = baseName + System.currentTimeMillis();

		byte[] bytes = name.getBytes();
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

		File file = fileService.uploadFile(bais, name, bytes.length);
		Assert.assertNotNull("Error uploading file", file);
		//System.out.println(DOMUtil.getXMLString(file.getDataHandler().getData()));
		
		return file;
	}
	
	protected File updateFile(File file, String baseName) throws ClientServicesException, XMLException {
		String name = baseName + System.currentTimeMillis();

		byte[] bytes = name.getBytes();
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

		file = fileService.updateFile(bais, file, null);
		Assert.assertNotNull("Error updating file", file);
		//System.out.println(DOMUtil.getXMLString(file.getDataHandler().getData()));
		
		return file;
	}

}
