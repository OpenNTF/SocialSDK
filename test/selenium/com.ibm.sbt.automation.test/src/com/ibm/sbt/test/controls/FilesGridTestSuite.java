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
package com.ibm.sbt.test.controls;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.automation.core.test.BaseGridTestSetup;
import com.ibm.sbt.test.controls.grid.files.FileAction;
import com.ibm.sbt.test.controls.grid.files.FileComments;
import com.ibm.sbt.test.controls.grid.files.FileShares;
import com.ibm.sbt.test.controls.grid.files.MyActiveFolders;
import com.ibm.sbt.test.controls.grid.files.MyFiles;
import com.ibm.sbt.test.controls.grid.files.MyFilesDijit;
import com.ibm.sbt.test.controls.grid.files.MyFolders;
import com.ibm.sbt.test.controls.grid.files.PinnedFiles;
import com.ibm.sbt.test.controls.grid.files.PinnedFolders;
import com.ibm.sbt.test.controls.grid.files.PublicFiles;
import com.ibm.sbt.test.controls.grid.files.PublicFolders;
import com.ibm.sbt.test.controls.grid.files.RecycledFiles;

@RunWith(Suite.class)
@SuiteClasses({ FileAction.class, FileComments.class, FileShares.class, MyActiveFolders.class, MyFiles.class, MyFilesDijit.class, PinnedFiles.class,
        PinnedFolders.class, PublicFiles.class, PublicFolders.class, RecycledFiles.class, MyFolders.class })
public class FilesGridTestSuite {
    
	private static BaseGridTestSetup setup ;
	 
	@BeforeClass
	public static void setup(){
		setup = new BaseGridTestSetup();
		setup.createFolder();
		setup.createFile();
	}
	
	@AfterClass
    public static void cleanup() {
		
    	setup.deleteFileAndQuit();
    	setup.emptyTrash();
    }
}
