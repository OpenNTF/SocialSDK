package com.ibm.sbt;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.services.client.connections.communities.CommunityServiceTest;
import com.ibm.sbt.services.client.connections.files.FileServiceTest;
import com.ibm.sbt.services.client.connections.wikis.WikiServiceTestSuite;

@RunWith(Suite.class)
@SuiteClasses({
	CommunityServiceTest.class, 
	FileServiceTest.class,
	WikiServiceTestSuite.class
})
public class ConnectionsTestSuite {

}
