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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import lib.TestEndpoint;
import org.junit.Test;
import org.w3c.dom.Node;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientService.Args;
import com.ibm.sbt.services.client.ClientServicesException;

public class FileServiceCreationTest extends TestEndpoint {

	public final static String	TEST_CONTENT	= "ddsfafw4t547£%*£^U£^JUL&><\03242";
	public final static String	TEST_NAME		= "new.txt";

	@Test
	public void testStream() throws FileServiceException {
		InputStream s = new ByteArrayInputStream(TEST_CONTENT.getBytes());
		FileService service = new FileService();
		FileEntry entry = service.uploadFile(s, TEST_NAME, TEST_CONTENT.length());
		assertNotNull(entry.getDisplayName());
	}

	@Test
	public void testFile() throws IOException, FileServiceException {

		File t = File.createTempFile("junit", "test");
		FileOutputStream s = new FileOutputStream(t);
		s.write(TEST_CONTENT.getBytes());
		s.flush();
		s.close();

		FileService service = new FileService();

		FileEntry entry = service.uploadFile(t, TEST_NAME);

		assertNotNull(entry.getDisplayName());

	}

	@Test(expected = IllegalArgumentException.class)
	public void testError1() throws IOException, FileServiceException {
		FileService service = new FileService();
		service.uploadFile((File) null, TEST_NAME);
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testError2() throws FileServiceException, IOException {
		FileService service = new FileService();
		service.uploadFile(File.createTempFile("junit", "tst"), null);
		fail();
	}

	@Test(expected = FileServiceException.class)
	public void testError3() throws IOException, FileServiceException {
		File t = new File(new File("."), "not existing file");
		FileService service = new FileService();
		service.uploadFile(t, TEST_NAME);
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testError4() throws IOException, FileServiceException {
		FileService service = new FileService();
		service.uploadFile(null, TEST_NAME, -1);
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testError5() throws IOException, FileServiceException {
		FileService service = new FileService();
		InputStream s = new ByteArrayInputStream(TEST_CONTENT.getBytes());
		service.uploadFile(s, null, -1);
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testError6() throws IOException, FileServiceException {
		FileService service = new FileService();
		service.uploadFile(null, TEST_NAME, 10);
		fail();
	}

	@Override
	protected Object testRequest(String method, Args args, Object content) throws ClientServicesException {
		if (method.equals("get") && args.getServiceUrl().equals("/files/basic/cmis/my/servicedoc")) {
			try {
				return DOMUtil.createDocument(this.getClass().getResourceAsStream("ServiceDescriptor.xml"));
			} catch (XMLException e) {
				throw new ClientServicesException(e);
			}
		}

		if (method.equals("post")
				&& args.getServiceUrl().equals("/files/basic/cmis/repository/p!20527378/folderc/snx:files")) {
			try {
				Node n = DOMUtil.createDocument(this.getClass().getResourceAsStream("FileEntry1.xml"));
				// use the xml set util to overwrite the name here
				return n;
			} catch (XMLException e) {
				throw new ClientServicesException(e);
			}
		}
		return null;
	}
}
