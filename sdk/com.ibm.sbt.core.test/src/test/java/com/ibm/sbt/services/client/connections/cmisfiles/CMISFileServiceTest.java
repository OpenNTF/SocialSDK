package com.ibm.sbt.services.client.connections.cmisfiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ibm.sbt.services.BaseUnitTest;

public class CMISFileServiceTest extends BaseUnitTest {

	protected CMISFileService fileService;
	
	@Before
	public void initBookmarkServiceTest() {
		if (fileService==null) {
			fileService = new CMISFileService();
		}
	}

	@Test
	public void testGetMyFiles() throws Exception {
		List<CMISFile> fileEntries = fileService.getMyFiles();
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (CMISFile fEntry : fileEntries) {
				assertEquals(fEntry.getAuthor().getName(), "Frank Adams");
			}
		}
	}
	
	@Test
	public void testGetFileSharedWithMe() throws Exception {
		List<CMISFile> fileEntries = fileService.getFilesSharedWithMe();
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (CMISFile fEntry : fileEntries) {
				assertNotNull(fEntry.getTitle());
			}
		}
	}
	
	@Test
	public void testGetMyCollections() throws Exception {
		List<CMISFile> fileEntries = fileService.getMyCollections();
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (CMISFile fEntry : fileEntries) {
				assertEquals(fEntry.getAuthor().getName(), "Frank Adams");
			}
		}
	}
	
	@Test
	public void testGetCollectionsSharedWithMe() throws Exception {
		List<CMISFile> fileEntries = fileService.getCollectionsSharedWithMe();
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (CMISFile fEntry : fileEntries) {
				assertNotNull(fEntry.getTitle());
			}
		}
	}

	@Test
	public void testGetMyShares() throws Exception {
		List<CMISFile> fileEntries = fileService.getMyShares();
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (CMISFile fEntry : fileEntries) {
				assertEquals(fEntry.getAuthor().getName(), "Frank Adams");
			}
		}
	}
}
