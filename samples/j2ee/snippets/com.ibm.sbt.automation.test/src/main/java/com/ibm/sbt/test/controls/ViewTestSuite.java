/*
 * � Copyright IBM Corp. 2013
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
import com.ibm.sbt.test.controls.grid.CommunityRenderer;
import com.ibm.sbt.test.controls.grid.ConnectionsRenderer;
import com.ibm.sbt.test.controls.grid.Grid;
import com.ibm.sbt.test.controls.grid.TemplatedGridRow;
import com.ibm.sbt.test.controls.view.ActionBar;
import com.ibm.sbt.test.controls.view.Section;
import com.ibm.sbt.test.controls.view.ShowMessageView;
import com.ibm.sbt.test.controls.view.TabBar;
import com.ibm.sbt.test.controls.view.files.AddTagsWidget;
import com.ibm.sbt.test.controls.view.files.FileActions;
import com.ibm.sbt.test.controls.view.files.FilesView;
import com.ibm.sbt.test.controls.view.files.UploadFileWidget;
import com.ibm.sbt.test.controls.view.forums.CreateForumWidget;

/**
 * @author mwallace
 * 
 * @date 12 Dec 2013
 */
@RunWith(Suite.class)
@SuiteClasses({ 
	ActionBar.class,
	Section.class,
	ShowMessageView.class,
	TabBar.class,
	
	AddTagsWidget.class, 
	UploadFileWidget.class,

	FileActions.class,
	FilesView.class
})
public class ViewTestSuite {
	 
	@BeforeClass
	public static void setup() {
	}
	
	@AfterClass
    public static void cleanup() {
    }
}
