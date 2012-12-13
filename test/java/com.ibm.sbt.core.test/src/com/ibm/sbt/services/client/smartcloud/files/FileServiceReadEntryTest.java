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
import java.io.IOException;
import lib.TestEndpoint;
import org.junit.Test;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientService.Args;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.SBTServiceException;

public class FileServiceReadEntryTest extends TestEndpoint {

	public final static String	TEST_ID	= "bead66f9-2b8d-4609-ac43-07f1541e5566";

	@Test
	public void testRead() throws SBTServiceException {
		FileService svc = new FileService();
		FileEntry entry = svc.getEntry(TEST_ID);
		assertNotNull(entry.getPageURL());

	}

	@Test(expected = NullPointerException.class)
	public void testError1() throws IOException, SBTServiceException {
		FileService svc = new FileService();
		FileEntry entry = svc.getEntry(null);
		assertNotNull(entry.getPageURL());
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
		if (method.equals("get")
				&& args.getServiceUrl()
						.equals("/files/basic/cmis/repository/p!20527378/object/snx:file!bead66f9-2b8d-4609-ac43-07f1541e5566")) {
			try {
				return DOMUtil.createDocument(this.getClass().getResourceAsStream("ReadEntry.xml"));
			} catch (XMLException e) {
				throw new ClientServicesException(e);
			}
		}

		return null;
	}
}
