package com.ibm.sbt.services.client.smartcloud.files;

import static org.junit.Assert.fail;
import org.junit.Test;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientService.Args;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.SBTServiceException;

public class FileServiceReadMyEntriesFailure2 extends FileServiceReadMyEntries {

	@Override
	@Test(expected = FileServiceException.class)
	public void testReadEntries() throws FileServiceException {
		super.testReadEntries();
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
