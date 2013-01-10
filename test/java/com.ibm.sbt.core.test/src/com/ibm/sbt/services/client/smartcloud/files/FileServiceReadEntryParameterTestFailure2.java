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
import org.junit.Test;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientService.Args;
import com.ibm.sbt.services.client.ClientServicesException;

/**
 * this test case returns the service index (getServiceDoc API) but fails subsequent class, as in the case of
 * entry not found errors
 * 
 * @see FileServiceReadEntryTestFailure1
 * @author Lorenzo Boccaccia
 * @date Dec 10, 2012
 */
public class FileServiceReadEntryParameterTestFailure2 extends FileServiceReadEntryParameterTest {

	public final static String	TEST_ID	= "bead66f9-2b8d-4609-ac43-07f1541e5566";

	@Override
	@Test(expected = FileServiceException.class)
	public void testRead() throws FileServiceException {
		super.testRead();
		fail();

	}

	@Override
	@Test(expected = FileServiceException.class)
	public void testRead2() throws FileServiceException {

		super.testRead2();
		fail();
	}

	@Override
	@Test(expected = FileServiceException.class)
	public void testRead3() throws FileServiceException {
		super.testRead3();
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
