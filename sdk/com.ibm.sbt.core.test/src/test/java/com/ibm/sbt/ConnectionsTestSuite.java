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

package com.ibm.sbt;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.services.client.base.URLBuilderTest;
import com.ibm.sbt.services.client.connections.ActivitiesTestSuite;
import com.ibm.sbt.services.client.connections.activitystreams.ActivityStreamServiceTest;
import com.ibm.sbt.services.client.connections.blogs.BlogServiceTestSuite;
import com.ibm.sbt.services.client.connections.bookmarks.BookmarkServiceTest;
import com.ibm.sbt.services.client.connections.cmisfiles.CMISFileServiceTest;
import com.ibm.sbt.services.client.connections.communities.CommunityServiceTestSuite;
import com.ibm.sbt.services.client.connections.files.FileServiceTestSuite;
import com.ibm.sbt.services.client.connections.follow.FollowServiceTest;
import com.ibm.sbt.services.client.connections.forums.ForumServiceTestSuite;
import com.ibm.sbt.services.client.connections.profiles.ProfileServiceTestSuite;
import com.ibm.sbt.services.client.connections.wikis.WikiServiceTestSuite;

/**
 * 
 * @author Carlos Manias
 *
 */
@RunWith(Suite.class)
@SuiteClasses({
										// 		Errors/Failures/Ignored
	ActivitiesTestSuite.class,			//		0/0/0
	ActivityStreamServiceTest.class,	//		0/0/0
	BlogServiceTestSuite.class,			//		0/0/0	
	BookmarkServiceTest.class,			//		0/0/0	
	CMISFileServiceTest.class,			//		0/0/0	
	CommunityServiceTestSuite.class, 	// 		0/0/0	
	FileServiceTestSuite.class,			// 		0/0/0	
	FollowServiceTest.class,			//		0/0/0	
	ForumServiceTestSuite.class,		// 		0/0/0	
	ProfileServiceTestSuite.class,		//		0/0/0	
	WikiServiceTestSuite.class,			//		0/0/0	
	URLBuilderTest.class
})
public class ConnectionsTestSuite {

}
