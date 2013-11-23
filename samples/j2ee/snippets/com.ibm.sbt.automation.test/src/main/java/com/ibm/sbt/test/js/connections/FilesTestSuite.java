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
package com.ibm.sbt.test.js.connections;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.test.js.connections.files.GetMyFileComments;
import com.ibm.sbt.test.js.connections.files.LoadUpdateLockPinDeleteFile;
import com.ibm.sbt.test.js.connections.files.UploadFile;
import com.ibm.sbt.test.js.connections.files.UploadNewVersion;
import com.ibm.sbt.test.js.connections.files.api.AddCommentToFile;
import com.ibm.sbt.test.js.connections.files.api.AddFilesToFolder;
import com.ibm.sbt.test.js.connections.files.api.DeleteFile;
import com.ibm.sbt.test.js.connections.files.api.FileAddComment;
import com.ibm.sbt.test.js.connections.files.api.GetFile;
import com.ibm.sbt.test.js.connections.files.api.GetFilesSharedByMe;
import com.ibm.sbt.test.js.connections.files.api.GetFilesSharedWithMe;
import com.ibm.sbt.test.js.connections.files.api.GetMyFiles;
import com.ibm.sbt.test.js.connections.files.api.GetMyFolders;
import com.ibm.sbt.test.js.connections.files.api.GetPinnedFiles;
import com.ibm.sbt.test.js.connections.files.api.GetPublicFileComments;
import com.ibm.sbt.test.js.connections.files.api.GetPublicFiles;
import com.ibm.sbt.test.js.connections.files.api.LockAndUnlockFile;
import com.ibm.sbt.test.js.connections.files.api.PinFileAndUnpinFile;
import com.ibm.sbt.test.js.connections.files.api.UpdateFileMetadata;

/**
 * @author Vineet Kanwal
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({ 	
		AddCommentToFile.class,
		FileAddComment.class,
		AddFilesToFolder.class,
		DeleteFile.class,
		GetFilesSharedByMe.class,
		GetFilesSharedWithMe.class,
		GetMyFiles.class,
		GetMyFolders.class,
		GetPinnedFiles.class,
		GetPublicFileComments.class,
		GetPublicFiles.class,
		PinFileAndUnpinFile.class,
		LockAndUnlockFile.class,
		UpdateFileMetadata.class,
		GetFile.class,
		com.ibm.sbt.test.js.connections.files.AddCommentToFile.class,
		com.ibm.sbt.test.js.connections.files.GetFilesSharedByMe.class,
		com.ibm.sbt.test.js.connections.files.GetFilesSharedWithMe.class,
		GetMyFileComments.class,
		com.ibm.sbt.test.js.connections.files.GetMyFiles.class,
		com.ibm.sbt.test.js.connections.files.GetMyFolders.class,
		com.ibm.sbt.test.js.connections.files.GetPublicFileComments.class,
		com.ibm.sbt.test.js.connections.files.GetPublicFiles.class,
		LoadUpdateLockPinDeleteFile.class,
		UploadFile.class,
		UploadNewVersion.class
    })
public class FilesTestSuite {
    @AfterClass
    public static void cleanup() {
    }
}
