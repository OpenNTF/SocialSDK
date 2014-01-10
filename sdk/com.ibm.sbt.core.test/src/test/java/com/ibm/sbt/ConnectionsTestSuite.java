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

@RunWith(Suite.class)
@SuiteClasses({
	CommunityServiceTest.class, 
	FileServiceTest.class/*,
	ForumServiceGetTests.class,
	BlogServiceTestSuite.class,
	ActivityServiceTest.class,
	ActivityStreamServiceTest.class,
	BookmarkServiceTest.class,
	FollowServiceTest.class,
	ProfileServiceTest.class,
	WikiServiceTestSuite.class*/
})
public class ConnectionsTestSuite {

}
