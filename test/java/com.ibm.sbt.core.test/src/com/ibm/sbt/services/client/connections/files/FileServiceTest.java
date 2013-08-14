/*
 * � Copyright IBM Corp. 2012
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

import java.util.List;

import org.junit.Test;

import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.connections.files.model.CommentEntry;
import com.ibm.sbt.services.client.connections.files.model.FileEntry;

public class FileServiceTest extends BaseUnitTest {

	public final static String	TEST_USERID				= "0EE5A7FA-3434-9A59-4825-7A7000278DAA";
	public final static String	TEST_SHAREWITHUSERID	= "0EE5A7FA-3434-9A59-4825-7A7000278DAA";
	public final static String	TEST_CONTENT			= "This is a sample Content in the Test File. "
																+ "Used mainly for Testing the Upload functionality of the FileService Connections API."
																+ "Test Input : ddsfafw4t547�%*�^U�^JUL&><\03242";
	public final static String	TEST_NAME				= "FS_TestUpload.txt";
	public final static String USERNAME = "fadams";
	public final static String PASSWORD = "passw0rd";

	@Test
	public void testReadFile() throws Exception {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), USERNAME, PASSWORD);
		FileEntryList listOfFiles = fileService.getMyFiles();
		String testFileId = listOfFiles.get(0).getFileId();
		FileEntry entry = fileService.getFile(testFileId, true);
		assertEquals(entry.getCategory(), "document");
		assertEquals(entry.getFileId(), testFileId);
	}

//	@Test
//	public void testReadFileWithLoadFalse() throws Exception {
//		FileService fileService = new FileService();
//		authenticateEndpoint(fileService.getEndpoint(), USERNAME, PASSWORD);
//		FileEntryList listOfFiles = fileService.getMyFiles();
//		String testFileId = listOfFiles.get(0).getFileId();
//		FileEntry entry = fileService.getFile(testFileId, false);
//		Assert.assertNull(entry.getCategory());
//	}

//	@Test
//	public void testReadFileWithNullFileId() throws IOException, Exception {
//		FileService fileService = new FileService();
//		FileEntry entry = fileService.getFile(null, true);
//		Assert.assertNull(entry);
//	}

	@Test
	public void testGetMyFiles() throws Exception {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), USERNAME, PASSWORD);
		List<FileEntry> fileEntries = fileService.getMyFiles();
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (FileEntry fEntry : fileEntries) {
				assertEquals(fEntry.getCategory(), "document");
				assertEquals(fEntry.getAuthorEntry().getName(), "Frank Adams");
			}
		}
	}

	@Test
	public void testGetFilesSharedWithMe() throws Exception {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), USERNAME, PASSWORD);
		List<FileEntry> fileEntries = fileService.getFilesSharedWithMe();
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (FileEntry fEntry : fileEntries) {
				// assertEquals(fEntry.getFieldUsingXPath("/feed/title"), "Files Shared With You");
				assertEquals(fEntry.getVisibility(), "shared");
			}
		}
	}

	@Test
	public void testGetFilesSharedByMe() throws Exception {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), USERNAME, PASSWORD);
		List<FileEntry> fileEntries = fileService.getFilesSharedByMe();
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (FileEntry fEntry : fileEntries) {
				assertEquals(fEntry.getCategory(), "document");
				// assertEquals(fEntry.getVisibility(), "shared");
			}
		}
	}

	@Test
	public void testGetPublicFiles() throws Exception {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), USERNAME, PASSWORD);
		List<FileEntry> fileEntries = fileService.getPublicFiles(null);
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (FileEntry fEntry : fileEntries) {
				assertEquals(fEntry.getCategory(), "document");
			}
		}
	}

	@Test
	public void testGetPinnedFiles() throws Exception {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), USERNAME, PASSWORD);
		List<FileEntry> fileEntries = fileService.getPinnedFiles(null);
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (FileEntry fEntry : fileEntries) {
				assertEquals(fEntry.getCategory(), "document");
			}
		}
	}

	@Test
	public void testGetMyFolders() throws Exception {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), USERNAME, PASSWORD);
		List<FileEntry> fileEntries = fileService.getMyFolders(null);
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (FileEntry fEntry : fileEntries) {
				assertEquals(fEntry.getCategory(), "collection");
			}
		}
	}

//  Commenting this test as the corresponding wrapper needs to be checked in yet.
//	@Ignore
//	@Test
//	public void testGetMyPinnedFolders() throws Exception {
//		FileService fileService = new FileService();
//		authenticateEndpoint(fileService.getEndpoint(), USERNAME, PASSWORD);
//		List<FileEntry> fileEntries = fileService.getMyPinnedFolders(null);
//		if (fileEntries != null && !fileEntries.isEmpty()) {
//			for (FileEntry fEntry : fileEntries) {
//				assertEquals(fEntry.getCategory(), "collection");
//			}
//		}
//	}

	@Test
	public void testGetFoldersWithRecentlyAddedFiles() throws Exception {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), USERNAME, PASSWORD);
		List<FileEntry> fileEntries = fileService.getFoldersWithRecentlyAddedFiles(null);
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (FileEntry fEntry : fileEntries) {
				assertEquals(fEntry.getCategory(), "collection");
			}
		}
	}

	@Test
	public void testGetFilesInFolder() throws Exception {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), USERNAME, PASSWORD);
		FileEntryList listOfFolders = fileService.getMyFolders();
		String testFolderId = listOfFolders.get(0).getFileId();
		List<FileEntry> fileEntries = fileService.getFilesInFolder(testFolderId, null);
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (FileEntry fEntry : fileEntries) {
				assertEquals(fEntry.getCategory(), "document");
			}
		}
	}

//	@Ignore
//	@Test
//	public void testGetPersonLibrary() throws Exception {
//		FileService fileService = new FileService();
//		authenticateEndpoint(fileService.getEndpoint(), USERNAME, PASSWORD);
//		List<FileEntry> fileEntries = fileService.getPersonLibrary(TEST_USERID, null);
//		if (fileEntries != null && !fileEntries.isEmpty()) {
//			for (FileEntry fEntry : fileEntries) {
//				assertEquals(fEntry.getCategory(), "document");
//			}
//		}
//	}

//	@Ignore
//	@Test
//	public void testGetPublicFilesComments() throws Exception {
//		FileService fileService = new FileService();
//		authenticateEndpoint(fileService.getEndpoint(), USERNAME, PASSWORD);
//		FileEntry fileEntry = fileService.getFile(TEST_FILEID, true);
//		List<CommentEntry> commentEntries = fileService.getPublicFilesComments(fileEntry, null);
//		if (commentEntries != null && !commentEntries.isEmpty()) {
//			for (CommentEntry fEntry : commentEntries) {
//				assertNotNull(fEntry.getComment());
//			}
//		}
//	}

//	@Ignore
//	@Test
//	public void testGetFilesComments() throws Exception {
//		FileService fileService = new FileService();
//		authenticateEndpoint(fileService.getEndpoint(), USERNAME, PASSWORD);
//		FileEntry fileEntry = fileService.getFile(TEST_FILEID, true);
//		List<CommentEntry> commentEntries = fileService.getFilesComments(fileEntry, null);
//		if (commentEntries != null && !commentEntries.isEmpty()) {
//			for (CommentEntry fEntry : commentEntries) {
//				assertNotNull(fEntry.getComment());
//			}
//		}
//	}

//	@Ignore
//	@Test
//	public void testGetMyFilesComments() throws Exception {
//		FileService fileService = new FileService();
//		authenticateEndpoint(fileService.getEndpoint(), USERNAME, PASSWORD);
//		FileEntry fileEntry = fileService.getFile(TEST_FILEID, true);
//		List<CommentEntry> commentEntries = fileService.getMyFilesComments(fileEntry, null);
//		if (commentEntries != null && !commentEntries.isEmpty()) {
//			for (CommentEntry fEntry : commentEntries) {
//				assertNotNull(fEntry.getComment());
//			}
//		}
//	}

	@Test
	public void testGetFilesInMyRecycleBin() throws Exception {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), USERNAME, PASSWORD);
		List<FileEntry> fileEntries = fileService.getFilesInMyRecycleBin(null);
		if (fileEntries != null && !fileEntries.isEmpty()) {
			for (FileEntry fEntry : fileEntries) {
				assertEquals(fEntry.getCategory(), "document");
			}
		}
	}

//	@Test
//	public void testUpdate() throws Exception {
//		FileService fileService = new FileService();
//		authenticateEndpoint(fileService.getEndpoint(), USERNAME, PASSWORD);
//		FileEntryList listOfFiles = fileService.getMyFiles();
//		String testFileId = listOfFiles.get(0).getFileId();
//		FileEntry fileEntry = fileService.getFile(testFileId, false);
//		Map<String, String> paramsMap = new HashMap<String, String>();
//		Random random = new Random();
//		paramsMap.put(FileRequestParams.TAG.getFileRequestParams(), "Junit_Tag" + random.nextInt());
//		Map<String, String> payloadMap = new HashMap<String, String>();
//		payloadMap.put(FileRequestPayload.LABEL.getFileRequestPayload(), "Junit_Label");
//		fileEntry = fileService.updateFile(fileEntry.getFileId(), paramsMap, payloadMap);
//		assertEquals(fileEntry.getTitle(), "Junit_Label");
//	}

	@Test
	public void testLock() throws Exception {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), USERNAME, PASSWORD);
		FileEntryList listOfFiles = fileService.getMyFiles();
		String testFileId = listOfFiles.get(0).getFileId();
		fileService.lock(testFileId);
		FileEntry fileEntry = fileService.getFile(testFileId, true);
		assertEquals(fileEntry.getLock(), "HARD");
	}

	@Test
	public void testUnlock() throws Exception {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), USERNAME, PASSWORD);
		FileEntryList listOfFiles = fileService.getMyFiles();
		String testFileId = listOfFiles.get(0).getFileId();
		fileService.unlock(testFileId);
		FileEntry fileEntry = fileService.getFile(testFileId, true);
		assertEquals(fileEntry.getLock(), "NONE");
	}
	
	@Test
	public void testPinAndUnPin() throws Exception {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), USERNAME, PASSWORD);
		FileEntryList listOfFiles = fileService.getMyFiles();
		String testFileId = listOfFiles.get(0).getFileId();
		fileService.pinFile(testFileId);
		fileService.unPinFile(testFileId);
	}

	@Test
	public void testDelete() throws Exception {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), USERNAME, PASSWORD);
		FileEntryList listOfFiles = fileService.getMyFiles();
		String testDeleteFileId = listOfFiles.get(0).getFileId();
		fileService.deleteFile(testDeleteFileId);
	}

	@Test
	public void testAddCommentToFile() throws Exception {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), USERNAME, PASSWORD);
		FileEntryList listOfFiles = fileService.getMyFiles();
		String testFileId = listOfFiles.get(0).getFileId();
		FileEntry fileEntry = fileService.getFile(testFileId, true);
		String comment = "Junit Comment - Added from FileServiceTest, testAddCommentToFile";
		CommentEntry commentEntry;
		commentEntry = fileService.addCommentToFile(fileEntry.getFileId(), comment, fileEntry.getAuthorEntry().getUserUuid() , null);
		assertEquals(commentEntry.getComment(),
				"Junit Comment - Added from FileServiceTest, testAddCommentToFile");
	}

//	@Ignore
//	@Test
//	public void testAddCommentToMyFile() throws Exception {
//		FileService fileService = new FileService();
//		authenticateEndpoint(fileService.getEndpoint(), USERNAME, PASSWORD);
//		FileEntry fileEntry = fileService.getFile(TEST_FILEID, true);
//		String comment = "Junit Comment - Added from FileServiceTest, testAddCommentToMyFile";
//		fileEntry = fileService.addCommentToMyFile(fileEntry, null, comment);
//		assertEquals(fileEntry.getCommentEntry().getComment(),
//				"Junit Comment - Added from FileServiceTest, testAddCommentToMyFile");
//	}

//	@Ignore
//	@Test
//	public void testFileUpload() throws IOException, ClientServicesException, Exception {
//		File t = new File(TEST_NAME);
//		FileOutputStream s = new FileOutputStream(t);
//		s.write(TEST_CONTENT.getBytes());
//		s.flush();
//		s.close();
//		String fileName = t.getPath();
//		FileService service = new FileService();
//		authenticateEndpoint(service.getEndpoint(), USERNAME, PASSWORD);
//		FileEntry entry = service.upload(fileName);
//		assertNotNull(entry.getCategory());
//	}

	@Test
	public void testGetNonce() {
		FileService fileService = new FileService();
		authenticateEndpoint(fileService.getEndpoint(), USERNAME, PASSWORD);
		String nonce = null;
		try {
			nonce = fileService.getNonce();
		} catch (FileServiceException e) {
			
		}
		assertNotNull(nonce);
	}
}
