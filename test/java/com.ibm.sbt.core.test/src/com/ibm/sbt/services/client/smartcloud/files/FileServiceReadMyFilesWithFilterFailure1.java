package com.ibm.sbt.services.client.smartcloud.files;

import static org.junit.Assert.fail;
import org.junit.Test;
import com.ibm.sbt.services.client.ClientService.Args;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.SBTServiceException;

public class FileServiceReadMyFilesWithFilterFailure1 extends FileServiceReadMyFilesWithFilters {

	@Override
	@Test(expected = FileServiceException.class)
	public void testReadEntries() throws SBTServiceException {
		super.testReadEntries();
		fail();
	}

	@Override
	@Test(expected = FileServiceException.class)
	public void testReadConsistency() throws SBTServiceException {
		super.testReadConsistency();
		fail();
	}

	@Override
	@Test(expected = FileServiceException.class)
	public void testHighSkip() throws SBTServiceException {
		super.testHighSkip();
		fail();
	}

	@Override
	@Test(expected = FileServiceException.class)
	public void testNoFilters() throws SBTServiceException {
		super.testNoFilters();
		fail();
	}

	@Override
	protected Object testRequest(String method, Args args, Object content) throws ClientServicesException {
		throw new ClientServicesException(new Exception("Testing endpoint errors"));
	}
}
