package com.ibm.sbt;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.services.client.connections.communities.CommunityServiceTest;
import com.ibm.sbt.services.client.connections.files.FileServiceTest;
import com.ibm.sbt.services.client.connections.wikis.WikiServiceTestSuite;
import com.ibm.sbt.test.lib.TestEnvironment;

@RunWith(Suite.class)
@SuiteClasses({
	CommunityServiceTest.class, 
	FileServiceTest.class//,
	//WikiServiceTestSuite.class
})
public class SmartCloudTestSuite {
	@BeforeClass
	public static void before() {
		TestEnvironment.enableSmartCloud(true);
	}
	@AfterClass
	public static void after() {
		TestEnvironment.enableSmartCloud(false);
	}

}
