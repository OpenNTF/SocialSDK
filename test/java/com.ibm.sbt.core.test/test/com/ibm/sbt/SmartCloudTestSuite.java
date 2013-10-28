package com.ibm.sbt;

import lib.TestEnvironment;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.services.client.connections.communities.CommunityServiceTest;

@RunWith(Suite.class)
@SuiteClasses({ CommunityServiceTest.class })
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
