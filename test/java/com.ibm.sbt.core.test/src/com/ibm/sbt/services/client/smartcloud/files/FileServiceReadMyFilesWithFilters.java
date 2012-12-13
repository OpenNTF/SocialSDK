package com.ibm.sbt.services.client.smartcloud.files;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.util.List;
import lib.TestEndpoint;
import org.junit.Test;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientService.Args;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.SBTServiceException;

/**
 * Tests for the filtered search API; note that there are no error checks on the input as they will be sent as
 * is to the server resulting in 404 and file service exceptions: this condition is tested as part of another
 * case, @see FileServiceReadMyFilesWithFiltersFailure2.
 * 
 * @author Lorenzo Boccaccia
 * @date Dec 10, 2012
 */
public class FileServiceReadMyFilesWithFilters extends TestEndpoint {

	@Test
	public void testReadEntries() throws SBTServiceException {
		FileService svc = new FileService();

		// prepare a filter. object id and type id are added by default.
		FileService.FieldFilter filter = new FileService.FieldFilter();
		// add the entry name to the fields that will be requested
		filter.add("cmis:name");
		List<FileEntry> entries = svc.getMyFiles(2, 1, filter);

		assertEquals(2, entries.size());
		assertNull(entries.get(0).getCreatedBy());
		assertNotNull(entries.get(0).getDisplayName());
		assertNotNull(entries.get(0).getObjectId());
		assertNotNull(entries.get(0).getObjectTypeId());
		assertNull(entries.get(1).getCreatedBy());
		assertNotNull(entries.get(1).getDisplayName());
		assertNotNull(entries.get(1).getObjectId());
		assertNotNull(entries.get(1).getObjectTypeId());
	}

	@Test
	public void testReadConsistency() throws SBTServiceException {
		FileService svc = new FileService();

		// prepare a filter. object id and type id are added by default.
		FileService.FieldFilter filter = new FileService.FieldFilter();
		// add the entry name to the fields that will be requested
		filter.add("cmis:name");
		List<FileEntry> entries1 = svc.getMyFiles(2, 1, filter);
		List<FileEntry> entries2 = svc.getMyFiles(2, 2, filter);

		assertEquals(2, entries1.size());
		assertEquals(2, entries2.size());

		assertEquals(entries1.get(1).getObjectId(), entries2.get(0).getObjectId());

	}

	@Test
	public void testHighSkip() throws SBTServiceException {
		FileService svc = new FileService();

		// prepare a filter. object id and type id are added by default.
		FileService.FieldFilter filter = new FileService.FieldFilter();
		// add the entry name to the fields that will be requested
		filter.add("cmis:name");
		List<FileEntry> entries1 = svc.getMyFiles(2, 100, filter);

		assertEquals(0, entries1.size());

	}

	/**
	 * in this test we're interested in the parameters constructions, to see that none gets passed to the
	 * server when none are passed in input
	 * 
	 * @throws FileServiceException
	 */
	@Test
	public void testNoFilters() throws SBTServiceException {
		FileService svc = new FileService();

		// prepare a filter. object id and type id are added by default.
		FileService.FieldFilter filter = new FileService.FieldFilter();
		// add the entry name to the fields that will be requested
		filter.add("cmis:name");
		List<FileEntry> entries1 = svc.getMyFiles(null, null, null);

		assertEquals(2, entries1.size());

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
				&& args.getServiceUrl().equals("/files/basic/cmis/repository/p!20527378/folderc/snx:files")) {
			try {
				if (args.getParameters().get("skipCount") != null
						&& args.getParameters().get("skipCount").equals("1")) {
					return DOMUtil.createDocument(this.getClass().getResourceAsStream(
							"MyFiles2-3WithNameFilter.xml"));

				} else if (args.getParameters().get("skipCount") != null
						&& args.getParameters().get("skipCount").equals("2")) {
					return DOMUtil.createDocument(this.getClass().getResourceAsStream(
							"MyFiles3-4WithNameFilter.xml"));
				} else if (args.getParameters().get("skipCount") != null
						&& args.getParameters().get("skipCount").equals("100")) {
					return DOMUtil.createDocument(this.getClass().getResourceAsStream(
							"MyFilesEmptySkipTooHigh.xml"));
				} else if (args.getParameters().size() == 0) {
					return DOMUtil.createDocument(this.getClass().getResourceAsStream("MyFiles.xml"));
				}
			} catch (XMLException e) {
				throw new ClientServicesException(e);
			}
		}

		return null;
	}

}
