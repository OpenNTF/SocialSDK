package com.ibm.sbt.services.client.smartcloud.files;

import static org.junit.Assert.fail;
import org.junit.Test;
import com.ibm.sbt.services.client.ClientService.Args;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.SBTServiceException;

public class FileServiceReadMyEntriesAltFailure1 extends FileServiceReadMyEntries {

	@Override
	@Test(expected = FileServiceException.class)
	public void testReadEntries() throws SBTServiceException {
		super.testReadEntries();
		fail();
	}

	@Override
	protected Object testRequest(String method, Args args, Object content) throws ClientServicesException {
		throw new ClientServicesException(new Exception("Testing endpoint errors"));
	}
}
