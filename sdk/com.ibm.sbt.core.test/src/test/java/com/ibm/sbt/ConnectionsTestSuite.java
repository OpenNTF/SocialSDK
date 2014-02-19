package com.ibm.sbt;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.services.client.connections.activity.ActivityServiceTestSuite;
import com.ibm.sbt.services.client.connections.activitystreams.ActivityStreamServiceTest;
import com.ibm.sbt.services.client.connections.blogs.BlogServiceTestSuite;
import com.ibm.sbt.services.client.connections.bookmarks.BookmarkServiceTest;
import com.ibm.sbt.services.client.connections.cmisfiles.CMISFileServiceTest;
import com.ibm.sbt.services.client.connections.communities.CommunityServiceTestSuite;
import com.ibm.sbt.services.client.connections.files.FileServiceTest;
import com.ibm.sbt.services.client.connections.follow.FollowServiceTest;
import com.ibm.sbt.services.client.connections.forums.ForumServiceTestSuite;
import com.ibm.sbt.services.client.connections.profiles.ProfileServiceTestSuite;
import com.ibm.sbt.services.client.connections.wikis.WikiServiceTestSuite;
@RunWith(Suite.class)
@SuiteClasses({
										// 		Errors/Failures/Ignored
	ActivityServiceTestSuite.class,		//		1/0/2
	ActivityStreamServiceTest.class,	//		0/0/0
	BlogServiceTestSuite.class,			//		0/0/0
	BookmarkServiceTest.class,			//		0/0/0
	CMISFileServiceTest.class,			//		0/0/0
	CommunityServiceTestSuite.class, 	// 		0/0/0
	FileServiceTest.class,				// 		0/0/0
	FollowServiceTest.class,			//		0/0/0
	ForumServiceTestSuite.class,		// 		0/0/0
	ProfileServiceTestSuite.class,		//		0/0/0
	WikiServiceTestSuite.class			//		0/0/0
})
public class ConnectionsTestSuite {

}
