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
package com.ibm.sbt.test.js;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.automation.core.environment.TestEnvironment;
import com.ibm.sbt.automation.core.environment.TestEnvironmentFactory;
import com.ibm.sbt.test.js.smartcloud.ActivitiesTestSuite;
import com.ibm.sbt.test.js.smartcloud.SearchRestTestSuite;
import com.ibm.sbt.test.js.smartcloud.SearchTestSuite;
import com.ibm.sbt.test.js.smartcloud.CommunitiesTestSuite;
import com.ibm.sbt.test.js.smartcloud.FilesTestSuite;
import com.ibm.sbt.test.js.smartcloud.ProfilesGridTestSuite;
import com.ibm.sbt.test.js.smartcloud.ProfilesTestSuite;
import com.ibm.sbt.test.sample.SampleFrameworkTestSuite;

/**
 * @author mwallace
 * 
 * @date 6 Mar 2013
 */
@RunWith(Suite.class)
@SuiteClasses({ CommunitiesTestSuite.class, FilesTestSuite.class,
		ProfilesTestSuite.class, ProfilesGridTestSuite.class,
		SearchTestSuite.class, /*SearchRestTestSuite.class,*/
		SampleFrameworkTestSuite.class, ActivitiesTestSuite.class })
public class SmartCloudTestSuite {
	@BeforeClass
	public static void init() {
		TestEnvironment environment = TestEnvironmentFactory.getEnvironment();
		environment.enableSmartCloud();
	}

	@AfterClass
	public static void cleanup() {
		TestEnvironment environment = TestEnvironmentFactory.getEnvironment();
		environment.disableSmartCloud();
		TestEnvironment.cleanup();
	}
}
