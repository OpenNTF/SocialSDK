package com.ibm.sbt.services.client.smartcloud.files;

import static org.junit.Assert.assertEquals;
import java.util.List;
import lib.TestEndpoint;
import org.junit.Test;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientService.Args;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.SBTServiceException;

public class FileServiceReadMyEntriesAltEmpty extends TestEndpoint {

	@Test
	public void testReadEntries() throws FileServiceException {
		FileService svc = new FileService();
		List<FileEntry> entries = svc.getMyFilesAlt();
		assertEquals(entries.size(), 0);
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
				&& args.getServiceUrl().equals(
						"/files/basic/cmis/repository/p!20527378/folderc/snx:virtual!.!filesownedby")) {
			try {
				return DOMUtil.createDocument(this.getClass().getResourceAsStream("MyFilesEmpty.xml"));
			} catch (XMLException e) {
				throw new ClientServicesException(e);
			}
		}

		return null;
	}

}
