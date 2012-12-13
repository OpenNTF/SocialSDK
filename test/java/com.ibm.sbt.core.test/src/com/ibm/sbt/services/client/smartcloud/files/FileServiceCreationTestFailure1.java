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
import com.ibm.sbt.services.client.ClientService.Args;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.SBTServiceException;

/**
 * this test case fails every client call, as in the case of no connections or no authentication to the
 * Endpoint
 * 
 * @see FileServiceCreationTestFailure2
 * @author Lorenzo Boccaccia
 * @date Dec 10, 2012
 */
public class FileServiceCreationTestFailure1 extends FileServiceCreationTest {

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
		throw new ClientServicesException(new Exception("Testing endpoint errors"));
	}
}
