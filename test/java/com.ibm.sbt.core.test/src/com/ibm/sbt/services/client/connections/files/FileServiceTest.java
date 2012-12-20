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

package com.ibm.sbt.services.client.connections.files;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;
import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.SBTServiceException;
import com.ibm.sbt.services.client.connections.files.model.FileEntry;
import com.ibm.sbt.services.client.connections.files.model.FileRequestParams;
import com.ibm.sbt.services.client.connections.files.model.FileRequestPayload;
import com.ibm.sbt.services.client.smartcloud.files.FileServiceException;

public class FileServiceTest extends BaseUnitTest {

	public final static String	TEST_FILEID				= "7afe3eae-f64e-43ba-8a2f-16be74500c20";
	public final static String	TEST_USERID				= "0EE5A7FA-3434-9A59-4825-7A7000278DAA";
	public final static String	TEST_SHAREWITHUSERID	= "0EE5A7FA-3434-9A59-4825-7A7000278DAA";
	public final static String	TEST_DELETEFILEID		= "1c19e88c-879d-404d-a832-ec1d16e8ac4c";
	public final static String	TEST_COLLECTIONID		= "c34360ff-173d-4911-8d8d-243f9bd49830";
	public final static String	TEST_CONTENT			= "This is a sample Content in the Test File. "
																+ "Used mainly for Testing the Upload functionality of the FileService Connections API."
																+ "Test Input : ddsfafw4t547£%*£^U£^JUL&><\03242";
	public final static String	TEST_NAME				= "FS_TestUpload.txt";

	@Test
	public void testReadFile() throws Exception {
		FileService svc = new FileService();
		authenticateEndpoint(svc.getEndpoint(), "fadams", "passw0rd");
		FileEntry entry = svc.getFile(TEST_FILEID, true);
		assertEquals(entry.getCategory(), "document");
		assertEquals(entry.getFileId(), TEST_FILEID);
	}

	@Test
	public void testReadFileWithLoadFalse() throws Exception {
		FileService svc = new FileService();
		FileEntry entry = svc.getFile(TEST_FILEID, false);
		Assert.assertNull(entry.getCategory());
	}

	@Test
	public void testReadFileWithNullFileId() throws IOException, Exception {
		FileService svc = new FileService();
		FileEntry entry = svc.getFile(null, true);
		Assert.assertNull(entry);
	}

	@Test
	public void testGetMyFiles() throws Exception  {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), "fadams", "passw0rd");
		List<FileEntry> fileEntries = fileService.getMyFiles();
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (FileEntry fEntry : fileEntries) {
				assertEquals(fEntry.getCategory(), "document");
				assertEquals(fEntry.getAuthorEntry().getName(), "Frank Adams");
			}
		}
	}

	@Test
	public void testGetFilesSharedWithMe() throws Exception{
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), "fadams", "passw0rd");
		List<FileEntry> fileEntries = fileService.getFilesSharedWithMe();
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (FileEntry fEntry : fileEntries) {
				// assertEquals(fEntry.getFieldUsingXPath("/feed/title"), "Files Shared With You");
				assertEquals(fEntry.getVisibility(), "shared");
			}
		}
	}

	@Test
	public void testGetFilesSharedByMe() throws Exception{
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), "fadams", "passw0rd");
		List<FileEntry> fileEntries = fileService.getFilesSharedByMe();
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (FileEntry fEntry : fileEntries) {
				assertEquals(fEntry.getCategory(), "document");
				// assertEquals(fEntry.getVisibility(), "shared");
			}
		}
	}

	@Test
	public void testGetPublicFiles() throws Exception{
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), "fadams", "passw0rd");
		List<FileEntry> fileEntries = fileService.getPublicFiles(null);
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (FileEntry fEntry : fileEntries) {
				assertEquals(fEntry.getCategory(), "document");
			}
		}
	}

	@Test
	public void testGetPinnedFiles() throws Exception{
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), "fadams", "passw0rd");
		List<FileEntry> fileEntries = fileService.getPinnedFiles(null);
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (FileEntry fEntry : fileEntries) {
				assertEquals(fEntry.getCategory(), "document");
			}
		}
	}

	@Test
	public void testGetMyFolders() throws Exception{
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), "fadams", "passw0rd");
		List<FileEntry> fileEntries = fileService.getMyFolders(null);
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (FileEntry fEntry : fileEntries) {
				assertEquals(fEntry.getCategory(), "collection");
			}
		}
	}

	@Test
	public void testGetMyPinnedFolders() throws Exception{
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), "fadams", "passw0rd");
		List<FileEntry> fileEntries = fileService.getMyPinnedFolders(null);
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (FileEntry fEntry : fileEntries) {
				assertEquals(fEntry.getCategory(), "collection");
			}
		}
	}

	@Test
	public void testGetFoldersWithRecentlyAddedFiles() throws Exception{
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), "fadams", "passw0rd");
		List<FileEntry> fileEntries = fileService.getFoldersWithRecentlyAddedFiles(null);
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (FileEntry fEntry : fileEntries) {
				assertEquals(fEntry.getCategory(), "collection");
			}
		}
	}

	@Test
	public void testGetFilesInFolder()throws Exception {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), "fadams", "passw0rd");
		List<FileEntry> fileEntries = fileService.getFilesInFolder(TEST_COLLECTIONID, null);
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (FileEntry fEntry : fileEntries) {
				assertEquals(fEntry.getCategory(), "document");
			}
		}
	}

	@Test
	public void testGetPersonLibrary()throws Exception {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), "fadams", "passw0rd");
		List<FileEntry> fileEntries = fileService.getPersonLibrary(TEST_USERID, null);
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (FileEntry fEntry : fileEntries) {
				assertEquals(fEntry.getCategory(), "document");
			}
		}
	}

	@Test
	public void testGetPublicFilesComments()throws Exception {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), "fadams", "passw0rd");
		FileEntry fileEntry = fileService.getFile(TEST_FILEID, true);
		List<FileEntry> fileEntries = fileService.getPublicFilesComments(fileEntry, null);
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (FileEntry fEntry : fileEntries) {
				assertEquals(fEntry.getCategory(), "comment");
			}
		}
	}

	@Test
	public void testGetFilesComments() throws Exception{
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), "fadams", "passw0rd");
		FileEntry fileEntry = fileService.getFile(TEST_FILEID, true);
		List<FileEntry> fileEntries = fileService.getFilesComments(fileEntry, null);
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (FileEntry fEntry : fileEntries) {
				assertEquals(fEntry.getCategory(), "comment");
			}
		}
	}

	@Test
	public void testGetMyFilesComments() throws Exception{
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), "fadams", "passw0rd");
		FileEntry fileEntry = fileService.getFile(TEST_FILEID, true);
		List<FileEntry> fileEntries = fileService.getMyFilesComments(fileEntry, null);
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (FileEntry fEntry : fileEntries) {
				assertEquals(fEntry.getCategory(), "comment");
			}
		}
	}

	@Test
	public void testGetFilesInMyRecycleBin()throws Exception {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), "fadams", "passw0rd");
		List<FileEntry> fileEntries = fileService.getFilesInMyRecycleBin(null);
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (FileEntry fEntry : fileEntries) {
				assertEquals(fEntry.getCategory(), "document");
			}
		}
	}

	@Test
	public void testUpdate() throws Exception{
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), "fadams", "passw0rd");
		FileEntry fileEntry = fileService.getFile(TEST_FILEID, false);
		Map<String, String> paramsMap = new HashMap<String, String>();
		Random random = new Random();
		paramsMap.put(FileRequestParams.TAG, "Junit_Tag" + random.nextInt());
		Map<String, String> payloadMap = new HashMap<String, String>();
		payloadMap.put(FileRequestPayload.LABEL, "Junit_Label");
		fileEntry = fileService.update(fileEntry, paramsMap, payloadMap);
		assertEquals(fileEntry.getTitle(), "Junit_Label");
	}

	@Test
	public void testLock()throws Exception {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), "fadams", "passw0rd");
		fileService.lock(TEST_FILEID);
		FileEntry fileEntry = fileService.getFile(TEST_FILEID, true);
		assertEquals(fileEntry.getLock(), "HARD");
	}

	@Test
	public void testUnlock()throws Exception {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), "fadams", "passw0rd");
		fileService.unlock(TEST_FILEID);
		FileEntry fileEntry = fileService.getFile(TEST_FILEID, true);
		assertEquals(fileEntry.getLock(), "NONE");
	}

	@Test
	public void testDelete()throws Exception {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), "fadams", "passw0rd");
		fileService.delete(TEST_DELETEFILEID);
		FileEntry fileEntry = fileService.getFile(TEST_DELETEFILEID, true);
		Assert.assertNull(fileEntry.getLabel());
	}

	@Test
	public void testAddCommentToFile()throws Exception {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), "fadams", "passw0rd");
		FileEntry fileEntry = fileService.getFile(TEST_FILEID, true);
		String comment = "Junit Comment - Added from FileServiceTest, testAddCommentToFile";
		fileEntry = fileService.addCommentToFile(fileEntry, null, comment);
		assertEquals(fileEntry.getCommentEntry().getComment(),
				"Junit Comment - Added from FileServiceTest, testAddCommentToFile");
	}

	@Test
	public void testAddCommentToMyFile()throws Exception {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), "fadams", "passw0rd");
		FileEntry fileEntry = fileService.getFile(TEST_FILEID, true);
		String comment = "Junit Comment - Added from FileServiceTest, testAddCommentToMyFile";
		fileEntry = fileService.addCommentToMyFile(fileEntry, null, comment);
		assertEquals(fileEntry.getCommentEntry().getComment(),
				"Junit Comment - Added from FileServiceTest, testAddCommentToMyFile");
	}

	@Test
	public void testFileUpload() throws IOException, ClientServicesException, Exception {
		File t = new File(TEST_NAME);
		FileOutputStream s = new FileOutputStream(t);
		s.write(TEST_CONTENT.getBytes());
		s.flush();
		s.close();
		String fileName = t.getPath();
		FileService service = new FileService();
		authenticateEndpoint(service.getEndpoint(), "fadams", "passw0rd");
		FileEntry entry = service.upload(fileName);
		assertNotNull(entry.getCategory());
	}

	@Test
	public void testGetNonce() {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), "fadams", "passw0rd");
		String nonce = fileService.getNonce();
		assertNotNull(nonce);
	}
}
