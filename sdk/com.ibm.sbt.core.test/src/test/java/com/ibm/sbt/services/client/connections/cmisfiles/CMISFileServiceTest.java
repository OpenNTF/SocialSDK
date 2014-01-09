package com.ibm.sbt.services.client.connections.cmisfiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.ibm.sbt.services.BaseUnitTest;

public class CMISFileServiceTest extends BaseUnitTest {

	@Test
	public void testGetMyFiles() throws Exception {
		CMISFileService fileService = new CMISFileService();
		List<CMISFile> fileEntries = fileService.getMyFiles();
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (CMISFile fEntry : fileEntries) {
				assertEquals(fEntry.getAuthor().getName(), "Frank Adams");
			}
		}
	}
	
	@Test
	public void testGetFileSharedWithMe() throws Exception {
		CMISFileService fileService = new CMISFileService();
		List<CMISFile> fileEntries = fileService.getFilesSharedWithMe();
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (CMISFile fEntry : fileEntries) {
				assertNotNull(fEntry.getTitle());
			}
		}
	}
	
	@Test
	public void testGetMyCollections() throws Exception {
		CMISFileService fileService = new CMISFileService();
		List<CMISFile> fileEntries = fileService.getMyCollections();
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (CMISFile fEntry : fileEntries) {
				assertEquals(fEntry.getAuthor().getName(), "Frank Adams");
			}
		}
	}
	
	@Test
	public void testGetCollectionsSharedWithMe() throws Exception {
		CMISFileService fileService = new CMISFileService();
		List<CMISFile> fileEntries = fileService.getCollectionsSharedWithMe();
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (CMISFile fEntry : fileEntries) {
				assertNotNull(fEntry.getTitle());
			}
		}
	}
		

	@Test
	public void testGetMyShares() throws Exception {
		CMISFileService fileService = new CMISFileService();
		List<CMISFile> fileEntries = fileService.getMyShares();
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (CMISFile fEntry : fileEntries) {
				assertEquals(fEntry.getAuthor().getName(), "Frank Adams");
			}
		}
	}
	
	

}
