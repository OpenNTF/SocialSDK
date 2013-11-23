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
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.test.controls.grid.profiles.Colleagues;
import com.ibm.sbt.test.controls.grid.profiles.MyColleagues;
import com.ibm.sbt.test.controls.grid.profiles.MyProfilePanel;
import com.ibm.sbt.test.controls.grid.profiles.ProfileAction;
import com.ibm.sbt.test.controls.grid.profiles.ProfilePanel;
import com.ibm.sbt.test.controls.grid.profiles.ProfileTagSearch;
import com.ibm.sbt.test.controls.grid.profiles.ProfileTags;
import com.ibm.sbt.test.controls.grid.profiles.ReportingChain;
import com.ibm.sbt.test.controls.grid.profiles.ProfileSearch;

/**
 * @author sberrybyrne
 * @date 6 Mar 2013
 */
// TODO ConnectionsInCommon, DirectReports. Getting 'Empty' on local environment

@RunWith(Suite.class)
@SuiteClasses({ Colleagues.class, MyColleagues.class,
	ProfileAction.class, ReportingChain.class, ProfileTags.class,
	ProfilePanel.class, MyProfilePanel.class, ProfileSearch.class,
	ProfileTagSearch.class })
public class ProfilesGridTestSuite {
    @AfterClass
    public static void cleanup() {
    }
}
