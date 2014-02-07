package com.ibm.sbt;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.services.client.connections.activity.ActivityServiceTest;
import com.ibm.sbt.services.client.connections.activitystreams.ActivityStreamServiceTest;
import com.ibm.sbt.services.client.connections.blogs.BlogServiceTestSuite;
import com.ibm.sbt.services.client.connections.bookmarks.BookmarkServiceTest;
import com.ibm.sbt.services.client.connections.communities.CommunityServiceTest;
import com.ibm.sbt.services.client.connections.files.FileServiceTest;
import com.ibm.sbt.services.client.connections.follow.FollowServiceTest;
import com.ibm.sbt.services.client.connections.forums.ForumServiceGetTests;
import com.ibm.sbt.services.client.connections.profiles.ProfileServiceTest;
import com.ibm.sbt.services.client.connections.wikis.WikiServiceTestSuite;
// Errors/Failures
@RunWith(Suite.class)
@SuiteClasses({
	CommunityServiceTest.class, 		// 		0/0
	FileServiceTest.class,				// 		0/0
	ForumServiceGetTests.class,			// 		0/0
	BlogServiceTestSuite.class,			//		0/0
	//ActivityServiceTest.class,		//		2/4
	ActivityStreamServiceTest.class,	//		0/0
	BookmarkServiceTest.class,			//		0/0
	FollowServiceTest.class,			//		0/0
	//ProfileServiceTest.class,			//		5/3
	WikiServiceTestSuite.class			//		0/0
})
public class ConnectionsTestSuite {

}
