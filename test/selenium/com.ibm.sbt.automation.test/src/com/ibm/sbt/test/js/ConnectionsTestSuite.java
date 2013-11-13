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
package com.ibm.sbt.test.js;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.automation.core.environment.TestEnvironment;
import com.ibm.sbt.test.js.connections.ActivitiesRestTestSuite;
import com.ibm.sbt.test.js.connections.ActivitiesStreamsTestSuite;
import com.ibm.sbt.test.js.connections.ActivitiesTestSuite;
import com.ibm.sbt.test.js.connections.BlogsTestSuite;
import com.ibm.sbt.test.js.connections.BookmarksRestTestSuite;
import com.ibm.sbt.test.js.connections.BookmarksTestSuite;
import com.ibm.sbt.test.js.connections.CommunitiesRestTestSuite;
import com.ibm.sbt.test.js.connections.CommunitiesTestSuite;
import com.ibm.sbt.test.js.connections.FilesTestSuite;
import com.ibm.sbt.test.js.connections.FollowTestSuite;
import com.ibm.sbt.test.js.connections.ForumsRestTestSuite;
import com.ibm.sbt.test.js.connections.ForumsTestSuite;
import com.ibm.sbt.test.js.connections.ProfilesRestTestSuite;
import com.ibm.sbt.test.js.connections.ProfilesTestSuite;
import com.ibm.sbt.test.js.connections.SearchRestTestSuite;
import com.ibm.sbt.test.js.connections.SearchTestSuite;
import com.ibm.sbt.test.js.connections.WikisTestSuite;
import com.ibm.sbt.test.sample.SampleFrameworkTestSuite;

/**
 * @author mwallace
 * 
 * @date 6 Mar 2013
 */
@RunWith(Suite.class)
@SuiteClasses({ 
	ActivitiesTestSuite.class,
	ActivitiesRestTestSuite.class,
	ActivitiesStreamsTestSuite.class,
	BlogsTestSuite.class,
	BookmarksTestSuite.class,
	BookmarksRestTestSuite.class,
	CommunitiesRestTestSuite.class, 
	CommunitiesTestSuite.class, 
	FilesTestSuite.class, 
	ForumsTestSuite.class,
	ForumsRestTestSuite.class,
	ProfilesRestTestSuite.class,
	ProfilesTestSuite.class,
	SearchRestTestSuite.class, 
	SearchTestSuite.class, 
	WikisTestSuite.class,
	FollowTestSuite.class,
	SampleFrameworkTestSuite.class })
public class ConnectionsTestSuite {
	@AfterClass
	public static void cleanup() {
		TestEnvironment.cleanup();
	}
}
