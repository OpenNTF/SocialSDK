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

import static org.junit.Assert.fail;
import java.io.IOException;
import org.junit.Test;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientService.Args;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.SBTServiceException;

/**
 * this test case returns the service index (getServiceDoc API) but fails subsequent class, as in the case of
 * duplicated entry name errors
 * 
 * @see FileServiceCreationTestFailure1
 * @author Lorenzo Boccaccia
 * @date Dec 10, 2012
 */
public class FileServiceCreationTestFailure2 extends FileServiceCreationTest {

	@Override
	@Test(expected = FileServiceException.class)
	public void testFile() throws IOException, SBTServiceException {
		// TODO Auto-generated method stub
		super.testFile();
		fail();
	}

	@Override
	@Test(expected = FileServiceException.class)
	public void testStream() throws SBTServiceException {
		// TODO Auto-generated method stub
		super.testStream();
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
		throw new ClientServicesException(new Exception("Testing endpoint errors"));
	}
}
