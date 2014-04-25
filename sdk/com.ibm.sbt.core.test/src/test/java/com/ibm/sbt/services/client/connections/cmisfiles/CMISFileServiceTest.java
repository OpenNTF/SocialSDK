/*
 * © Copyright IBM Corp. 2013
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

package com.ibm.sbt.services.client.connections.cmisfiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
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

	@Ignore
	@Test
	public void testGetMyShares() throws Exception {
		//Fix the entries in My Shares have as Author Lucille Suarez
		List<CMISFile> fileEntries = fileService.getMyShares();
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (CMISFile fEntry : fileEntries) {
				assertEquals(fEntry.getAuthor().getName(), "Frank Adams");
			}
		}
	}
}
