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

import com.ibm.sbt.test.controls.grid.search.SearchActivitiesPublic;
import com.ibm.sbt.test.controls.grid.search.SearchAll;
import com.ibm.sbt.test.controls.grid.search.SearchBlogsPublic;
import com.ibm.sbt.test.controls.grid.search.SearchBookmarks;
import com.ibm.sbt.test.controls.grid.search.SearchCommunitiesPublic;
import com.ibm.sbt.test.controls.grid.search.SearchFiles;
import com.ibm.sbt.test.controls.grid.search.SearchForums;
import com.ibm.sbt.test.controls.grid.search.SearchProfilesPublic;
import com.ibm.sbt.test.controls.grid.search.SearchStatusUpdates;
import com.ibm.sbt.test.controls.grid.search.SearchWikis;

/**
 * @author mwallace
 * 
 * @date 6 Mar 2013
 */
@RunWith(Suite.class)
@SuiteClasses({ SearchActivitiesPublic.class,
	SearchAll.class, SearchBlogsPublic.class, SearchBookmarks.class,
	SearchCommunitiesPublic.class, SearchFiles.class,
	SearchForums.class, SearchProfilesPublic.class,
	SearchStatusUpdates.class, SearchWikis.class })
public class SearchGridTestSuite {
	 
	@BeforeClass
	public static void setup(){
	}
	
	@AfterClass
    public static void cleanup() {
    }
}
